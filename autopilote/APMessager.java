import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
// import java.io.ObjectInputStream;
// import java.io.ObjectOutputStream;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonObject;
import org.json.JSONObject;

import capteurs.Capteur;

public class APMessager {
	private static AP ap;
	private static Capteur capteurs1;
	private static JsonReaderFactory readFactory = Json.createReaderFactory((Map<String, ?>) capteurs1);

	// [{
	// "id" : int,
	// "command" : string,
	// "metadata" : {
	// "x" : float/double,
	// "y" : float/double,
	// "z" : float/double
	// }
	// }, ... ]
	private static ArrayList<JsonObject> commands = new ArrayList<JsonObject>();

	// {
	// "position" : {"x" : float, "y" : float, "z" : float},
	// "contact" : {"f" : float, "b" : float, "l" : float, "r" : float}
	// }
	private static JsonObject capteurs = null;

	
	private static JsonObject instruction = null;

	// send this.instruction to the bus;
	private static boolean sendBusMSG( JsonObject instruction, DataOutputStream bus_out ) throws IOException {

		boolean status = false;

		try {
			
			status = true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw e; 
		}
		return status;
	}

	// fill up this.capteurs
	private static boolean getBusMSG( DataInputStream bus_in ) throws IOException {

		boolean status = false;

		try {
			//@SuppressWarnings("deprecation")
			JsonReader s_json = Json.createReader(new StringReader(bus_in.readLine()));
			JsonReader jsonReader = readFactory.createReader((Reader) s_json);
			JsonObject json = jsonReader.readObject();
			
			capteurs = json;	
			json = jsonReader.readObject();
			jsonReader.close();
			
			status = true;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw e; 
		}

		return status;
	}

	private static void UCLoop( DataInputStream uc_in ) throws IOException {
		
		try {
			//@SuppressWarnings("deprecation")
			JsonReader s_json = Json.createReader(new StringReader(uc_in.readLine()));
			JsonReader jsonReader = readFactory.createReader((Reader) s_json);
			JsonObject json = jsonReader.readObject();
			
			while (json!=null) {
				// on insere la commande en fonction de l'ID
				commands.add(json);	
				json = jsonReader.readObject();
			}
			jsonReader.close();
		}catch (IOException e) {
			System.out.println(e.getMessage());
			throw e; 
		}
				
	}

	private static Command findEnumCmd (String cmd) {
		switch(cmd){
		case "GOTO":
			return Command.GOTO;
		case "FORWARD":
			return Command.FORWARD;
		case "BACKWARD":
			return Command.BACKWARD;
		case "LEFT":
			return Command.LEFT;
		case "RIGHT":
			return Command.RIGHT;
		case "UP":
			return Command.UP;
		case "DOWN":
			return Command.DOWN;
		case "NONE":
			return Command.NONE;
		}
		return null;
	}
	
	public static void main(String[] args)  throws IOException, UnknownHostException {
		// open connection with UC
		//try {
			Socket uc = new Socket("a definir", 7778);
			DataOutputStream uc_out = new DataOutputStream(uc.getOutputStream());
			DataInputStream uc_in = new DataInputStream(uc.getInputStream());

			// open connection with BUS
			Socket bus = new Socket("a definir", 7777);
			DataOutputStream bus_out = new DataOutputStream(bus.getOutputStream());
			DataInputStream bus_in = new DataInputStream(bus.getInputStream());
		//}catch(UnknownHostException e) {
		//	System.out.println(e.getMessage());
		//}
		
        while (uc.isConnected()) {
            UCLoop(uc_in); // inserting commands in queue

            if ( ap.APState() /*ap.getCommand() == Command.NONE*/) {
            	
                if (commands.size() > 0) { // take the first command in queue then init AP
                	
                    JsonObject command = commands.get(0);                    
                    String com = command.getJsonString("command").getString();
                    Command cmd = findEnumCmd (com);
                    
                    try {
                    	// init AP              
                        if( getBusMSG(bus_in) ) {
                        	ap.init(cmd, capteurs, command.getJsonObject("position"));
                            ap.compute();
                            instruction = ap.createInstruction();
                        }else {
                        	//todo
                        }
                        commands.remove(0);
                    }catch (IOException e) {
                    	System.out.println(e.getMessage());
                    }
                    
                }
            } else { // AP is still processing instruction
            	try {
            		if( getBusMSG(bus_in) ) {
                		ap.update(capteurs); // update Capteur
                        ap.compute();
                        instruction = ap.createInstruction();
                	}else {
                		//todo
                	}
                }catch (IOException e) {
                	System.out.println(e.getMessage());
                }
            	
                
            }

            if (instruction != null) { // send instruction on BUS then reset instruction
                try{
                	if (sendBusMSG(instruction, bus_out)) instruction = null;
                }catch(IOException e) {
                	System.out.println(e.getMessage());
                }
            }
        }

		// JsonObject json = (JsonObject) in.readObject();

		// On pourrait faire 2 threads, un pour l'uc l'autre pour le bus
		// On sera alors alerte pour les commandes que l'ont reçoit et la récupération
		// des données des capteurs

	}
	
}

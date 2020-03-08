import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
// import java.io.ObjectInputStream;
// import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

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

public class APMessager implements Runnable {
	private static PipedInputStream in;
	private static PipedOutputStream out;

	private static AP ap;
	// private static Capteur capteurs1;
	private static JsonReaderFactory readFactory = Json.createReaderFactory();

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

	private void autopilote() {

	}

	private void bus() {}

	// send this.instruction to the bus;
	// private static boolean sendBusMSG() throws IOException, UnknownHostException {
	//
	// 	boolean status = false;
	//
	// 	try {
	//
	// 		status = true;
	// 	} catch (Exception e) {
	// 		// TODO: handle exception
	// 	}
	//
	// 	return status;
	// }

	// fill up this.capteurs
	// private static boolean getBusMSG( DataInputStream bus_in ) throws IOException, UnknownHostException {
	//
	// 	boolean status = false;
	//
	// 	try {
	// 		//@SuppressWarnings("deprecation")
	// 		JsonReader s_json = Json.createReader(new StringReader(bus_in.readLine()));
	// 		JsonReader jsonReader = readFactory.createReader((Reader) s_json);
	// 		JsonObject json = jsonReader.readObject();
	//
	// 		capteurs = json;
	// 		json = jsonReader.readObject();
	// 		jsonReader.close();
	//
	// 		status = true;
	//
	// 	} catch (Exception e) {
	// 		// TODO: handle exception
	// 	}
	//
	// 	return status;
	// }

	// private static void UCLoop( DataInputStream uc_in ) throws IOException, UnknownHostException {
	//
	// 	//@SuppressWarnings("deprecation")
	// 	JsonReader s_json = Json.createReader(new StringReader(uc_in.readLine()));
	// 	JsonReader jsonReader = readFactory.createReader((Reader) s_json);
	// 	JsonObject json = jsonReader.readObject();
	//
	// 	while (json!=null) {
	// 		// on insere la commande en fonction de l'ID
	// 		commands.add(json);
	// 		json = jsonReader.readObject();
	// 	}
	// 	jsonReader.close();
	//
	// }

	// private static Command findEnumCmd (String cmd) {
	// 	switch(cmd){
	// 	case "GOTO":
	// 		return Command.GOTO;
	// 	case "FORWARD":
	// 		return Command.FORWARD;
	// 	case "BACKWARD":
	// 		return Command.BACKWARD;
	// 	case "LEFT":
	// 		return Command.LEFT;
	// 	case "RIGHT":
	// 		return Command.RIGHT;
	// 	case "UP":
	// 		return Command.UP;
	// 	case "DOWN":
	// 		return Command.DOWN;
	// 	case "NONE":
	// 		return Command.NONE;
	// 	}
	// }

	public static void main(String[] args)  throws IOException, UnknownHostException {
		// open connection with UC
		// Socket uc = new Socket("a definir", 7778);
		// DataOutputStream uc_out = new DataOutputStream(uc.getOutputStream());
		// DataInputStream uc_in = new DataInputStream(uc.getInputStream());

		// open connection with BUS
		// Socket bus = new Socket("a definir", 7777);
		// DataOutputStream bus_out = new DataOutputStream(bus.getOutputStream());
		// DataInputStream bus_in = new DataInputStream(bus.getInputStream());

        while (uc.isConnected()) {
            UCLoop(uc_in); // inserting commands in queue

            if (ap.getCommand() == Commande.NONE) {
                if (commands.size() > 0) { // take the first command in queue then init AP
                    JsonObject command = commands.get(0);
                    String str_command = command.getJsonString("command").getString();
                    Command cmd = new Command(str_command);


                    // init AP
                    if( getBusMSG(bus_in) ) {
                    	ap.init(cmd, capteurs, command.getJsonObject("position"));
                        ap.compute();
                        instruction = ap.createInstruction();
                    }


                    commands.remove(0);
                }
            } else { // AP is still processing instruction
            	if (getBusMSG(bus_in)) {
            		ap.update(capteurs); // update Capteur
                    ap.compute();
                    instruction = ap.createInstruction();
            	} else {
            		//todo
            	}

            }

            if (instruction != null) { // send instruction on BUS then reset instruction
                if (sendBusMSG()) instruction = null;
            }
        }

		// JsonObject json = (JsonObject) in.readObject();

		// On pourrait faire 2 threads, un pour l'uc l'autre pour le bus
		// On sera alors alerte pour les commandes que l'ont reçoit et la récupération
		// des données des capteurs

	}

}

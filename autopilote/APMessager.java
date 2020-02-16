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

	// {
	// "x" : 1 (forward) || -1 (backward) || 0 (none),
	// "y" : 1 (right) || -1 (left) || 0 (none)
	// "z" : -1 (down) || 1 (up) || 0 (none),
	// }
	private static JsonObject instruction = null;

	// send this.instruction to the bus;
	private static boolean sendBusMSG() throws IOException, UnknownHostException {
		
		boolean status = false;
		
		try {
			
			status = true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return status;
	}

	// fill up this.capteurs
	private static boolean getBusMSG() throws IOException, UnknownHostException {
		
		boolean status = false;
		
		try {
			
			status = true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return status;
	}

	private static void UCLoop() throws IOException, UnknownHostException {
		Socket uc = new Socket("a definir", 7778);
		DataOutputStream out = new DataOutputStream(uc.getOutputStream());
		DataInputStream in = new DataInputStream(uc.getInputStream());

		while (uc.isConnected()) {
			//@SuppressWarnings("deprecation")
			JsonReader s_json = Json.createReader(new StringReader(in.readLine()));
			 
			 JsonReader jsonReader = readFactory.createReader((Reader) s_json);
			 JsonObject json = jsonReader.readObject();
			 jsonReader.close();

			// on insere la commande en fonction de l'ID
			// this.commands.add(json);
		}
	}

	public static void main(String[] args)  throws IOException, UnknownHostException {
		// open connection with UC
		Socket uc = new Socket("a definir", 7778);
		DataOutputStream uc_out = new DataOutputStream(uc.getOutputStream());
		DataInputStream uc_in = new DataInputStream(uc.getInputStream());

		// open connection with BUS
		Socket bus = new Socket("a definir", 7777);
		DataOutputStream bus_out = new DataOutputStream(bus.getOutputStream());
		DataInputStream bus_in = new DataInputStream(bus.getInputStream());

        while (uc.isConnected()) {
            UCLoop(); // inserting commands in queue

            if (ap.getCommand() == Command.NONE) {
                if (commands.size() > 0) { // take the first command in queue then init AP
                    JsonObject command = commands.get(0);
                    Command cmd = null;
                    //= new Command(command.getString("command"));

                    // init AP
                    getBusMSG();
                    ap.init(cmd, capteurs, command.getJsonObject("position"));
                    ap.compute();
                    instruction = ap.createInstruction();

                    commands.remove(0);
                }
            } else { // AP is still processing instruction
                ap.update(capteurs); // update Capteur
                ap.compute();
                instruction = ap.createInstruction();
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

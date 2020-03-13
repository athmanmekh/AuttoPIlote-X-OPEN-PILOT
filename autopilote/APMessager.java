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
	private static DataInputStream in;
	private static DataOutputStream out;

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

	private static boolean getBusMSG( DataInputStream bus_in ) throws IOException {

		boolean status = false;
		
		try {
			status = true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw e; 
		}

		return status;
	}
	
	public static void main(String[] args)  throws IOException, UnknownHostException {
		
		
		try {
			Socket s = new Socket("a definir",7777);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
		}catch( UnknownHostException e ){
			System.out.println(e.getMessage());
		}
		
		try {
			while(true /*s.isconnected()*/) {
				/*
				* On attend une commande ou on prend la commande suivante (dans la file)
				* On recupere les donnees des capteurs (depuis le bus)
				* On initialise l'AP

			    * AP est en train d'executer une commande :
				* On recupere les donnees des capteurs (depuis le bus)
				* On update l'AP

			    * Ensuite on cree l'instruction grâce a l'AP
			    * On pose l'instruction sur le bus
			    	*/
			}
		}catch( IOException e ) {
			System.out.println(e.getMessage());
		}
		
	}
}

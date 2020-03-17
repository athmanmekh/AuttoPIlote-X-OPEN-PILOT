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
import javax.json.JsonArray;

import capteurs.Capteur;

public class APMessager {
	private static Socket bus;
	private static DataOutputStream bus_out;
	private static DataInputStream bus_in;
	private static AP ap;

	// private static Capteur capteurs1;
	// private static JsonReaderFactory readFactory = Json.createReaderFactory(Map<String,?>);

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

	private static void sendBusMSG(JsonObject obj) throws IOException {
		if (bus.isConnected()) {
			try {
				String cmd = "SEND " + obj.toString();
				bus_out.writeBytes(cmd);
			} catch (IOException e) {
				System.out.println("Error sending : " + obj.toString());
				System.out.println(e.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static JsonArray getBusMSG(String id) throws IOException {
		if (bus.isConnected()) {
			try {
				String cmd = "GET " + id;
				bus_out.writeBytes(cmd);

				String resp = bus_in.readLine();
				JsonReader jsonReader = Json.createReader(new StringReader(resp));
				JsonArray arr = jsonReader.readArray();
				jsonReader.close();

				return arr;
			} catch (IOException e) {
				System.out.println("Error getting from : " + id);
				System.out.println(e.getMessage());
			}
		}
		return null;
	}

	// met a jour la liste de commande actuelle avec celle recu en parametre
	private static void fillCommands(JsonArray cmds) {}

	// reformatte les donnees des capteurs recu, stockant le resultat dans l'attribut capteurs
	private static void fillCapteurs(JsonArray sensors) {}

	public static void main(String[] args) throws IOException, UnknownHostException {
		try {
			// open connection with BUS
			bus = new Socket("TBD", 7777);
			bus_out = new DataOutputStream(bus.getOutputStream());
			bus_in = new DataInputStream(bus.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e.getMessage());
		}

		try {
			while (bus.isConnected()) {
				if (ap.getCommand() == Command.NONE) {

					// * On récupère toutes les commandes sur le bus
					JsonArray uc_commands = getBusMSG("UC");
					// * On met a jour notre liste de commande, si l'id est déjà présent dans notre liste on ignore
					// sinon on insere (insert sort) la/les commande(s) dans la liste.
					fillCommands(uc_commands);

					// * On recupere les donnees des capteurs (depuis le bus)
					JsonArray new_capteurs = getBusMSG("CAPTEURS");
					fillCapteurs(new_capteurs);

					// * On initialise l'AP
					ap.init(Command.NONE, null, null);

				} else {
					// * On recupere les donnees des capteurs (depuis le bus)
					JsonArray new_capteurs = getBusMSG("CAPTEURS");
					fillCapteurs(new_capteurs);

					// * On update l'AP
					ap.update(null);
				}

				ap.compute()
				// * Ensuite on cree l'instruction grâce a l'AP
				JsonObject instruction = ap.createInstruction();
				// * On pose l'instruction sur le bus
				sendBusMSG(instruction);
			}
		} catch (IOException e) {
			System.out.println("Unknown Host: " + e.getMessage());
		}


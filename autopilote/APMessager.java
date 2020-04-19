import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;
import org.json.JSONArray;

public class APMessager extends APUtils {
	private static Socket bus;
	private static DataOutputStream bus_out;
	private static DataInputStream bus_in;
	private static AP ap;

	private static void sendBusMSG(JSONObject obj) throws IOException {
		if (bus.isConnected()) {
			try {
				System.out.println("SENDING : " + obj.toString());
				String cmd = "SEND " + obj.toString();
				bus_out.writeBytes(cmd + '\n');
			} catch (IOException e) {
				System.out.print("Error sending " + obj.toString() + ": ");
				System.out.println(e.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static JSONArray getBusMSG(String id) throws IOException {
		if (bus.isConnected()) {
			try {
				System.out.println("GET FROM : " + id);
				String cmd = "GET " + id;
				bus_out.writeBytes(cmd + '\n');

				String resp = bus_in.readLine();
				JSONArray arr = new JSONArray(resp);

				System.out.println("RESPONSE : " + arr.toString());
				return arr;
			} catch (IOException e) {
				System.out.print("Error getting from " + id + ": ");
				System.out.println(e.getMessage());
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, UnknownHostException {
		ap = new AP();
		try {
			// open connection with BUS
			bus = new Socket("localhost", 7777);
			bus_out = new DataOutputStream(bus.getOutputStream());
			bus_in = new DataInputStream(bus.getInputStream());

		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e.getMessage());
		}

		try {
			JSONObject id = new JSONObject();
			id.put("Type", "ap");
			id.put("TTL", 0);
			System.out.println("IDENTIFICATION: " + id.toString());
			bus_out.writeBytes(id.toString() + '\n');
			bus_in.readLine(); // {"IdType" : int}, unnecessary for the moment, may be deleted l8er

			while (bus.isConnected()) {
				// System.out.println("\nBUS IS CONNECTED");
				if (ap.getCommand() == Command.WAIT) {

					// * On récupère toutes les commandes sur le bus
					JSONArray uc_commands = getBusMSG("uc");
					// * On met a jour notre liste de commande, si l'id est déjà présent dans notre liste on ignore
					// sinon on insere (insert sort) la/les commande(s) dans la liste.
					fillCommands(uc_commands);

					// * On recupere les donnees des capteurs (depuis le bus)
					JSONArray new_capteurs = getBusMSG("capteur");
					if (new_capteurs.length() > 0) {
						String resp = new_capteurs.getString(0);
						JSONObject sensor = new JSONObject(resp);
						fillCapteurs(sensor);
					}

					// * On recupere la premiere commande de la liste
					JSONObject cmd_obj = getNextCommand();
					Command e_cmd = str2cmd(cmd_obj.getString("command"));
					JSONObject meta = cmd_obj.getJSONObject("metadata");

					// * On initialise l'AP
					ap.init(e_cmd, getCapteurs(), meta);

				} else {
					// * On recupere les donnees des capteurs (depuis le bus)
					JSONArray new_capteurs = getBusMSG("capteur");
					if (new_capteurs.length() > 0) {
						String resp = new_capteurs.getString(0);
						JSONObject sensor = new JSONObject(resp);
						fillCapteurs(sensor);
					}

					// * On update l'AP
					ap.update(getCapteurs());
				}

				ap.compute();
				// * Ensuite on cree l'instruction grâce a l'AP
				JSONObject instruction = ap.createInstruction();
				// * On pose l'instruction sur le bus
				sendBusMSG(instruction);
			}
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
}

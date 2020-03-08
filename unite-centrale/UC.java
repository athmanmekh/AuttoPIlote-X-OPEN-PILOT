import java.io.IOException;
// import java.io.DataOutputStream;
// import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;

public class UC {
    // peut etre mettre la commande en attribut en cas d'echec d'envoi vers l'autopilote
    private static ArrayList<JsonObject> commands = new ArrayList<JsonObject>();
    private static String status = "send";

    // {"id" : int,"command" : string,"metadata" : {"x" : float/double,"y" : float/double,"z" : float/double }}
    // l'id permet de différencier l'ordre des commandes
    // la command correspond a la commande a executer (enum Command)
    // metadata les données associés permettant le bon fonctionnement de la commande
    public static JsonObject reformat(JsonObject obj) {
        String s_command = "";
        JsonObject meta = null;
        if (obj.getBoolean("buttonLeftPressed")) {
            s_command = "LEFT";
        } else if (obj.getBoolean("buttonRightPressed")) {
            s_command = "RIGHT";
        } else if (obj.getBoolean("buttonTopPressed")) {
            s_command = "FORWARD";
        } else if (obj.getBoolean("buttonBotPressed")) {
            s_command = "BACKWARD";
        } else if (obj.getBoolean("isNewPosition")) {
            s_command = "GOTO";
            meta = Json.createObjectBuilder()
                .add("x", obj.getJsonNumber("x").doubleValue())
                .add("y", obj.getJsonNumber("y").doubleValue())
                .add("z", obj.getJsonNumber("z").doubleValue())
                .build();
        }

        JsonObject res = Json.createObjectBuilder()
            .add("id", obj.getInt("id"))
            .add("command", s_command)
            .add("metadata", meta)
            .build();
        return res;
    }

    public static void main(String[] args) throws SocketException, IOException {
        ServerSocket servSock = new ServerSocket(7778);

        try {
            while (true) {
                Socket socket = servSock.accept();
//                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                System.out.println("Connected to " + socket.getLocalAddress());

                UCThread thread = new UCThread(socket);
                thread.start();

            }

        } catch (SocketException e) {
            System.out.println("SocketException : ");
            System.out.println("\t" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException : ");
            System.out.println("\t" + e.getMessage());
        }

    }

    class UCThread extends Thread {
        Socket sock;
        String id;
        String status;

        ObjectOutputStream out;
        ObjectInputStream in;

        public UCThread(Socket s, String id) throws IOException {
            this.sock = s;
            this.out = new ObjectOutputStream(this.sock.getOutputStream());
            this.in = new ObjectInputStream(this.sock.getInputStream());

            this.id = id;
            if (this.id == "AP") status = "send";

        }

        public void run() {
            if (this.id == "GCS") {
                JsonReader jsonReader = Json.createReader(new StringReader(in.readLine()));
                JsonObject gcs = jsonReader.readObject();
                jsonReader.close();

                commands.add(gcs);
            }

            if (this.id == "AP") {
                if (this.status == "send") {
                    JsonObject cmd = commands.get(0);
                    cmd = reformat(cmd);

                    out.writeChars(cmd.toString());
                    status = "check";

                } else if (this.status == "check") {
                    String s = in.readLine();
                    if (s == "ok") commands.remove(0);

                    this.status = "send";

                }
            }
        }

    }

}

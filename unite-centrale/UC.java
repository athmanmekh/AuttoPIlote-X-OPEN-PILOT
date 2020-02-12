import java.io.IOException;
// import java.io.DataOutputStream;
// import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.json.Json;
import javax.json.JsonReader;
import org.json.JSONObject;

public class UC {
    // peut etre mettre la commande en attribut en cas d'echec d'envoi vers l'autopilote
    private JSONObject gcs;
    private Command cmd = Move.NONE;
    private float x, y, z;

    public static void main(String[] args) throws IOException {
        // TO DO;
        ServerSocket servSock = new ServerSocket(7778);
        while (true) {
            Socket socket = servSock.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

/******************************************************************************/

            // On doit distinguer le socket du GCS et le socket du AP.

            // Si GCS:
            //      - attendre une commande
            //      - reformater si besoin
            //      - envoyer la commande a l'AP

            JsonReader jsonReader = Json.createReader(in.readObject());
            this.gcs = jsonReader.readObject();
            jsonReader.close();

            if (gcs.getBoolean("buttonLeftPressed")) {
                this.cmd = Move.LEFT;
            } else if (gcs.getBoolean("buttonRightPressed")) {
                this.cmd = Move.RIGHT;
            } else if (gcs.getBoolean("buttonTopPressed")) {
                this.cmd = Move.FORAWARD;
            } else if (gcs.getBoolean("buttonBotPressed")) {
                this.cmd = Move.BACKWARD;
            } else if (gcs.getBoolean("isNewPosition")) {
                this.cmd = Move.GOTO;
                this.x = (float) gcs.getDouble("x");
                this.y = (float) gcs.getDouble("y");
                this.z = (float) gcs.getDouble("z");
            }

            // on confirme que la commande est pris en compte
            // on peut aussi ajouter un systeme de file pour gerer plusieurs commande
            // ou alors on mets le systeme de file d'attente au niveau de l'autopilote lui-meme

/******************************************************************************/

            // Si AP:
            //      - envoyer des que possible la commande
            //      - attendre un signal positif de l'AP
            //          Positif :
            //              - reinitialiser la commande
            //              - revenir au debut
            //          Negatif :
            //              - renvoyer la commande
            //              - revenir a l'etape d'attente

            // avec le systeme de file :
            //      - tant que la file n'est pas vide
            JSONObject json = Json.
            this.cmd;
        }
    }
}

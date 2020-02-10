import java.io.IOException;
// import java.io.DataOutputStream;
// import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class UC {
    // peut etre mettre la commande en attribut en cas d'echec d'envoi vers l'autopilote

    public static void main(String[] args) throws IOException {
        // TO DO;
        ServerSocket servSock = new ServerSocket(7778);
        while (true) {
            Socket socket = servSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // On doit distinguer le socket du GCS et le socket du AP.

            // Si GCS:
            //      - attendre une commande
            //      - reformater si besoin
            //      - envoyer la commande a l'AP

            // Si AP:
            //      - envoyer des que possible la commande
            //      - attendre un signal positif de l'AP
            //          Positif :
            //              - reinitialiser la commande
            //              - revenir au debut
            //          Negatif :
            //              - renvoyer la commande
            //              - revenir a l'etape d'attente
        }
    }
}

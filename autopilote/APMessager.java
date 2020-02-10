import java.io.IOException;
// import java.io.DataOutputStream;
// import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public AP ap;

    public void sendMSG();
    public  void getMSG();

    public void UCLoop() {
        Socket uc = new Socket("a definir", 7778);
        ObjectOutputStream out = new ObjectOutputStream(uc.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(uc.getInputStream());

        while(uc.isConnected()) {
            JSONObject json = (JSONObject) in.readObject();

        }
    }

    public void

    public static void main(String[] args) throws IOException, UnknownHostException {
        Socket uc = new Socket("a definir", 7778);
        ObjectOutputStream uc_out = new ObjectOutputStream(uc.getOutputStream());
        ObjectInputStream uc_in = new ObjectInputStream(uc.getInputStream());

        Socket bus = new Socket("a definir", 7777);
        ObjectOutputStream bus_out = new ObjectOutputStream(bus.getOutputStream());
        ObjectInputStream bus_in = new ObjectInputStream(bus.getInputStream());

        while (uc.isConnected()) {
            JSONObject json = (JSONObject) uc_in.readObject();
        }

        // JSONObject json = (JSONObject) in.readObject();

        // On pourrait faire 2 threads, un pour l'uc l'autre pour le bus
        // On sera alors alerte pour les commandes que l'ont reçoit et la récupération des données des capteurs

    }
}

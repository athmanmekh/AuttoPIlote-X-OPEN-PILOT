import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
// import java.io.ObjectInputStream;
// import java.io.ObjectOutputStream;
import java.io.StringReader;

import java.util.Scanner;
import java.util.ArrayList;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.json.Json;
import javax.json.JsonReaderFactory;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class TestUC {

    public static void main(String[] args) throws UnknownHostException, IOException {

        try {

            Socket gcs = new Socket("localhost", 7778);
            DataOutputStream gcs_out = new DataOutputStream(gcs.getOutputStream());
            DataInputStream gcs_in = new DataInputStream(gcs.getInputStream());

            Socket ap = new Socket("localhost", 7778);
            DataOutputStream ap_out = new DataOutputStream(ap.getOutputStream());
            DataInputStream ap_in = new DataInputStream(ap.getInputStream());

            if (gcs.isConnected() && ap.isConnected()) {
                gcs_out.writeChars("GCS");
                ap_out.writeChars("AP");
            }

            if (gcs.isConnected()) {
                JsonObject res = Json.createObjectBuilder()
                    .add("id", 0)
                    .add("command", "GOTO")
                    .add("metadata", Json.createObjectBuilder().add("x", 0.).add("y", 0.).add("z", 0.).build())
                    .build();
                gcs_out.writeChars(res.toString());

                if (ap.isConnected()) {
                    JsonReader jsonReader = Json.createReader(new StringReader(ap_in.readLine()));
                    JsonObject cmd = jsonReader.readObject();
                    jsonReader.close();

                    System.out.println(cmd.toString());

                    ap_out.writeChars("ok");
                }

            }

        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException : ");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException : ");
            System.out.println(e.getMessage());
        }

    }
}

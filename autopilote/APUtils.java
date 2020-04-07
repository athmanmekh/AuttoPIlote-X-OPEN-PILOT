import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

public class APUtils {
    // [{
    // "id" : int,
    // "command" : string,
    // "metadata" : {
    // "x" : float/double,
    // "y" : float/double,
    // "z" : float/double
    // }
    // }, ... ]
    private static ArrayList<JSONObject> commands = new ArrayList<JSONObject>();

    // {
    // "position" : {"x" : float, "y" : float, "z" : float},
    // "contact" : {"f" : float, "b" : float, "l" : float, "r" : float}
    // }
    private static JSONObject capteurs;

    public APUtils() {
        // * initialize with empty atributes
        commands = new ArrayList<JSONObject>();
        capteurs = null;
    }

    private static JSONObject reformatCommand(JSONObject command) {
        JSONObject reformat = new JSONObject();
        reformat.put("id", 0);
        reformat.put("command", "WAIT");

        JSONObject meta = new JSONObject();
        reformat.put("metadata", meta);

        return reformat;
    }

    // Renvoit true si l'objet json est deja dans la liste des commande
    private static boolean commandIsIn(JSONObject obj) {
        for (int i=0; i < commands.size(); i++) {
            if (commands.get(i).getInt("id") == obj.getInt("id")) return true;
        }
        return false;
    }

    // met a jour la liste de commande actuelle avec celle recu en parametre
    public static void fillCommands(JSONArray cmds) {
        if (cmds == null) return;

        for (int i=0; i < cmds.length(); i++) {
            JSONObject obj = cmds.getJSONObject(i);
            obj = reformatCommand(obj);

            if (!commandIsIn(obj)) { // si la commande n'est pas deja dans la liste
                int j = commands.size()-1;
                while (obj.getInt("id") < commands.get(j).getInt("id")) j--;
                // on insere la commande dans l'ordre
                commands.add(j, obj);
            }
        }
    }

    public static JSONObject getNextCommand() {
        JSONObject res;
        if (commands.size() > 0) {
            res = commands.get(0);
            commands.remove(0);
        } else {
            res = new JSONObject();
            res.put("id", -1);
            res.put("command", "WAIT");
            res.put("metadata", new JSONObject("{}"));
        }
        return res;
    }

    private static double b2d(boolean b) { return (b ? 1.0 : 0.0); } // boolean to double

    // reformatte les donnees des capteurs recu, stockant le resultat dans l'attribut capteurs
    public static void fillCapteurs(JSONObject sensors) {
        if (sensors == null) capteurs = null;

        JSONObject pos = capteurs.getJSONObject("position");
        JSONObject new_pos = sensors.getJSONObject("position");

        pos.put("x", new_pos.getDouble("x"));
        pos.put("y", new_pos.getDouble("y"));
        pos.put("z", new_pos.getDouble("z"));
        capteurs.put("position", pos);

        JSONObject contact = capteurs.getJSONObject("contact");
        JSONObject new_contact = sensors.getJSONObject("contact");

        contact.put("f", b2d(new_contact.getBoolean("front_contact")));
        contact.put("b", b2d(new_contact.getBoolean("rear_contact")));
        contact.put("l", b2d(new_contact.getBoolean("left_contact")));
        contact.put("r", b2d(new_contact.getBoolean("right_contact")));
        capteurs.put("contact", contact);
	}

    public static JSONObject getCapteurs() { return capteurs; }

    public static Command str2cmd(String s) {
        Command res;
        switch (s) {
            case "GOTO":
                res = Command.GOTO;
                break;

            case "LAND":
                res = Command.LAND;
                break;

            case "TAKEOFF":
                res = Command.TAKEOFF;
                break;

            case "JOYSTICK":
                res = Command.JOYSTICK;
                break;

            default:
                res = Command.WAIT;
                break;
        }
        return res;
    }

}

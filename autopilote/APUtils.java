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
    private static ArrayList<JSONObject> treated = new ArrayList<JSONObject>();

    // {
    // "position" : {"x" : float, "y" : float, "z" : float},
    // "contact" : {"f" : float, "b" : float, "l" : float, "r" : float}
    // }
    private static JSONObject capteurs = new JSONObject();

    private static JSONObject reformatCommand(JSONObject command) {
        JSONObject reformat = new JSONObject();
        reformat.put("id", command.getInt("id"));

        String str_command = "WAIT";
        JSONObject meta = new JSONObject();

        double xSpd = command.getDouble("xSpeed");
        double ySpd = command.getDouble("ySpeed");
        double zSpd = command.getDouble("zSpeed");

        if (command.getBoolean("isNewPosition")) {
            str_command = "GOTO";
            meta.put("x", command.getDouble("x"));
            meta.put("y", command.getDouble("y"));
            meta.put("z", command.getDouble("z"));
        } else if (command.getBoolean("toLand")) {
            str_command = "LAND";
        } else if (command.getBoolean("toTakeOff")) {
            str_command = "TAKEOFF";
        } else if (xSpd != 0 || ySpd != 0 || zSpd != 0) { // JOYSTICK should be ignored if any other variable is set to True
            str_command = "JOYSTICK";
            meta.put("x", xSpd);
            meta.put("y", ySpd);
            meta.put("z", zSpd);
        }

        reformat.put("command", str_command);
        reformat.put("metadata", meta);
        // System.out.println("REFORMAT : " + reformat.toString());
        return reformat;
    }

    // Renvoit true si l'objet json est deja dans la liste des commande
    private static boolean commandIsIn(JSONObject obj) {
        for (int i=0; i < commands.size(); i++) {
            if (commands.get(i).getInt("id") == obj.getInt("id")) return true;
        }
        return false;
    }

    // Renvoit true si l'objet json à deja été traité
    private static boolean commandTreated(JSONObject obj) {
        for (int i=0; i < treated.size(); i++) {
            JSONObject t = treated.get(i);
            if (t.getInt("id") == obj.getInt("id")) {
                if (t.getString("command") == obj.getString("command")) return true;
            }
        }
        return false;
    }

    // met a jour la liste de commande actuelle avec celle recu en parametre
    public static void fillCommands(JSONArray cmds) {
        if (cmds == null) return;

        for (int i=0; i < cmds.length(); i++) {
            JSONObject obj = new JSONObject(cmds.getString(i));
            if (obj.length() > 0) {
                obj = reformatCommand(obj);
                if (commands.size() == 0) { // ajout de la premiere commande
                    commands.add(obj);
                } else if (!commandIsIn(obj) && !commandTreated(obj)) { // si la commande n'est pas deja dans la liste
                    int j = commands.size()-1;
                    while (obj.getInt("id") < commands.get(j).getInt("id")) j--;
                    // on insere la commande dans l'ordre
                    commands.add(j, obj);
                }
            }
        }
        // System.out.println("COMMANDS LENGTH : " + commands.size());
    }

    public static JSONObject getNextCommand() {
        JSONObject res;
        if (commands.size() > 0) {
            res = commands.get(0);
            treated.add(res);
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

        JSONObject new_pos = sensors.getJSONObject("position");

        JSONObject pos = new JSONObject();
        pos.put("x", new_pos.getDouble("x"));
        pos.put("y", new_pos.getDouble("y"));
        pos.put("z", new_pos.getDouble("z"));
        capteurs.put("position", pos);

        JSONObject new_contact = sensors.getJSONObject("contact");

        JSONObject contact = new JSONObject();
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

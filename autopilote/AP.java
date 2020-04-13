import capteurs.*;

import org.json.JSONObject;

public class AP {
    private Command cmd;
    private Contact contact;
    private Position pos;
    private float def_x, def_y, def_z; // pour le futur
    private float x, y, z; // the motors for each axis

    public AP() {
        this.cmd = Command.WAIT;
        this.contact = new Contact();
        this.pos = new Position();
        this.def_x = 0;
        this.def_y = 0;
        this.def_z = 0;
    }

    public Command getCommand() { return this.cmd; } // if Command("NONE") is returned then the AP is WAITING for a command

    // if no metadata put null
    public void init(Command c, JSONObject capteurs, JSONObject metadata) {
        this.update(capteurs);
        switch (c) {
            case GOTO:
                if (metadata == null) {
                    c = Command.WAIT;
                    break;
                }
                float x = (float) metadata.getDouble("x");
                float y = (float) metadata.getDouble("y");
                float z = (float) metadata.getDouble("z");
                this.pos.setTargetX(x);
                this.pos.setTargetY(y);
                this.pos.setTargetZ(z);
                break;

            case JOYSTICK:
                if (metadata == null) {
                    c = Command.WAIT;
                    break;
                }
                this.def_x = (float) metadata.getDouble("x");
                this.def_y = (float) metadata.getDouble("y");
                this.def_z = (float) metadata.getDouble("z");
                break;

            case LAND:
                this.pos.setTargetX(this.pos.getX());
                this.pos.setTargetY(this.pos.getY());
                this.pos.setTargetZ(0);
                break;

            case TAKEOFF:
                this.pos.setTargetX(this.pos.getX());
                this.pos.setTargetY(this.pos.getY());
                this.pos.setTargetZ(10);
                break;

            default:
                this.def_x = 0;
                this.def_y = 0;
                this.def_z = 0;
                break;
        }

        this.cmd = c;
    }

    // Mise a jour des données des capteurs
    public void update(JSONObject capteurs) {
        // Si aucune donnée n'as pu être receptionnée ou n'as été envoyée,
        // le drone repart dans un état d'attente en restant sur place
        if (capteurs.length() == 0) {
            this.cmd = Command.WAIT;
            return;
        }

        JSONObject pos, contact;
        pos = capteurs.getJSONObject("position");
        contact = capteurs.getJSONObject("contact");

        float x = (float) pos.getDouble("x");
        float y = (float) pos.getDouble("y");
        float z = (float) pos.getDouble("z");

        this.pos.setX(x);
        this.pos.setY(y);
        this.pos.setZ(z);

        float f = (float) contact.getDouble("f");
        float b = (float) contact.getDouble("b");
        float l = (float) contact.getDouble("l");
        float r = (float) contact.getDouble("r");

        this.contact.setForward(f);
        this.contact.setBackward(b);
        this.contact.setLeft(l);
        this.contact.setRight(r);
    }

	// calcule le tableau diff, ainsi que la puissance des moteurs
    public void compute() {
    	this.contact.computeDiff();
    	this.pos.computeDiff();

        switch (this.cmd) {
            case GOTO:
                this.x = this.pos.getDiffX();
                this.y = this.pos.getDiffY();
                this.z = this.pos.getDiffZ();

                if (this.pos.targetedPosition()) this.cmd = Command.WAIT;
                break;

            case LAND:
                this.x = 0;
                this.y = 0;
                this.z = 1;

                //check land achieved
                if (this.pos.targetedPosition()) this.cmd = Command.WAIT;
                break;

            case TAKEOFF:
                this.x = 0;
                this.y = 0;
                this.z = -1;

                //check takeoff achieved
                if (this.pos.targetedPosition()) this.cmd = Command.WAIT;
                break;

            case WAIT:
                this.x = this.def_x;
                this.y = this.def_y;
                this.z = this.def_z;
                break;
        }
    }

    public JSONObject createInstruction() {
        JSONObject ins = new JSONObject();
        ins.put("xAxis", this.x);
        ins.put("yAxis", this.y);
        ins.put("zAxis", this.z);
        return ins;
    }

}

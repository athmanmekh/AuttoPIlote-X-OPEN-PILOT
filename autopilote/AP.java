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
        switch (c) {
            case GOTO:
                this.cmd = Command.GOTO;
            	if (metadata != null) {
                    float x = (float) metadata.getDouble("x");
                    float y = (float) metadata.getDouble("y");
                    float z = (float) metadata.getDouble("z");
                    // float x = (float) metadata.getJsonNumber("x").doubleValue();
                    // float y = (float) metadata.getJsonNumber("y").doubleValue();
                    // float z = (float) metadata.getJsonNumber("z").doubleValue();
                    this.pos.setTargetX(x);
                    this.pos.setTargetY(y);
    	            this.pos.setTargetZ(z);
             	}
            	break;

            case FORWARD:
            	this.cmd = Command.FORWARD;
                break;

            case BACKWARD:
            	this.cmd = Command.BACKWARD;
                break;

            case LEFT:
            	this.cmd = Command.LEFT;
            	break;

            case RIGHT:
            	this.cmd = Command.RIGHT;
                break;

            case UP:
            	this.cmd = Command.UP;
                break;

            case DOWN:
            	this.cmd = Command.DOWN;
                break;
        }
        this.update(capteurs);
    }

	// maj capteurs
    public void update(JSONObject capteurs) {
        JSONObject pos, contact;
        pos = capteurs.getJSONObject("position");
        contact = capteurs.getJSONObject("contact");

        float x = (float) pos.getDouble("x");
        float y = (float) pos.getDouble("y");
        float z = (float) pos.getDouble("z");

        // float x = (float) pos.getJsonNumber("x").doubleValue();
        // float y = (float) pos.getJsonNumber("y").doubleValue();
        // float z = (float) pos.getJsonNumber("z").doubleValue();

        this.pos.setX(x);
        this.pos.setY(y);
        this.pos.setZ(z);

        float f = (float) contact.getDouble("f");
        float b = (float) contact.getDouble("b");
        float l = (float) contact.getDouble("l");
        float r = (float) contact.getDouble("r");

        // float f = (float) pos.getJsonNumber("f").doubleValue();
        // float b = (float) pos.getJsonNumber("b").doubleValue();
        // float l = (float) pos.getJsonNumber("l").doubleValue();
        // float r = (float) pos.getJsonNumber("r").doubleValue();

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
                break;

            case FORWARD:
                this.x = 10;
                this.y = this.def_y;
                this.z = this.def_z;
                break;

            case BACKWARD:
                this.x = -10;
                this.y = this.def_y;
                this.z = this.def_z;
                break;

            case LEFT:
                this.x = this.def_x;
                this.y = -10;
                this.z = this.def_z;
                break;

            case RIGHT:
                this.x = this.def_x;
                this.y = 10;
                this.z = this.def_z;
                break;

            case UP:
                this.x = this.def_x;
                this.y = this.def_y;
                this.z = 10;
                break;

            case DOWN:
                this.x = this.def_x;
                this.y = this.def_y;
                this.z = -10;
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

import capteurs.*;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.json.JSONObject;

import java.math.BigDecimal;

public class AP {
    private Command cmd;
    private Contact contact;
    private Position pos;
    private float def_x, def_y, def_z; // pour le futur
    private float x, y, z; // the motors for each axis

    public AP() {
        this.cmd = Command.NONE;
        this.contact = new Contact();
        this.pos = new Position();
        this.def_x = 0;
        this.def_y = 0;
        this.def_z = 0;
    }

    public Command getCommand() { return this.cmd; } // if Command("NONE") is returned then the AP is WAITING for a command

    // if no metadata put null
    public void init(Command c, JsonObject capteurs, JsonObject metadata) {
        switch (c) {
            case GOTO:
                this.cmd = Command.GOTO;
            	if (metadata != null) {
            		float x = (float) metadata.getJsonNumber("x").doubleValue();
            		float y = (float) metadata.getJsonNumber("y").doubleValue();
            		float z = (float) metadata.getJsonNumber("z").doubleValue();
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
    public void update(JsonObject capteurs) {
    	JsonObject pos, contact;
    	pos = capteurs.getJsonObject("position");
    	contact = capteurs.getJsonObject("contact");

    	float x = (float) pos.getJsonNumber("x").doubleValue();
    	float y = (float) pos.getJsonNumber("y").doubleValue();
    	float z = (float) pos.getJsonNumber("z").doubleValue();

    	this.pos.setX(x);
    	this.pos.setY(y);
    	this.pos.setZ(z);

    	float f = (float) pos.getJsonNumber("f").doubleValue();
    	float b = (float) pos.getJsonNumber("b").doubleValue();
    	float l = (float) pos.getJsonNumber("l").doubleValue();
    	float r = (float) pos.getJsonNumber("r").doubleValue();

    	this.contact.setTargetForward(f);
    	this.contact.setTargetBackward(b);
    	this.contact.setTargetLeft(l);
    	this.contact.setTargetRight(r);
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

            case NONE:
                this.x = this.def_x;
                this.y = this.def_y;
                this.z = this.def_z;
                break;
        }
    }

    public JsonObject createInstruction() {
        JsonObject value = Json.createObjectBuilder()
            .add("xAxis", this.x)
            .add("yAxis", this.y)
            .add("zAxis", this.z)
            .build();
        return value;
    }

}

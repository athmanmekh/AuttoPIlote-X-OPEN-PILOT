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

    public AP() {
        this.cmd = Command.NONE;
        this.contact = new Contact();
        this.pos = new Position();
    }

    public Command getCommand() { return this.cmd; }

    // if no metadata put null
    public void init(Command c, JsonObject capteurs, JsonObject metadata) {
        switch (c) {
            case GOTO: 
            	if( metadata!=null ) {
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
            	this.update(capteurs);
                break;
                
            case BACKWARD:
            	this.cmd = Command.BACKWARD;
            	this.update(capteurs);
                break;
                
            case LEFT:
            	this.cmd = Command.LEFT;
            	this.update(capteurs);                
            	break;
            	
            case RIGHT:
            	this.cmd = Command.RIGHT;
            	this.update(capteurs);                
                break;
                
            case UP:
            	this.cmd = Command.UP;
            	this.update(capteurs);                
                break;
                
            case DOWN:
            	this.cmd = Command.DOWN;
            	this.update(capteurs);                
                break;
        }
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

	// calcule le tableau diff
    public void compute() {
    	this.contact.computeDiff();
    	this.pos.computeDiff();
    }

    public JsonObject createInstruction() {
    	
    	switch (this.cmd) {
        case GOTO: 
        	JsonObject valueT = Json.createObjectBuilder()
            .add("xAxis", this.pos.getDiffX())
            .add("yAxis", this.pos.getDiffY())
            .add("zAxis", this.pos.getDiffZ())
            .build();
        	return valueT;
        	
        case FORWARD:
        	JsonObject valueF = Json.createObjectBuilder()
            .add("xAxis", 10)
            .add("yAxis", 0)
            .add("zAxis", 0)
            .build();
        	return valueF;
            
        case BACKWARD:
        	JsonObject valueB = Json.createObjectBuilder()
            .add("xAxis", -10)
            .add("yAxis", 0)
            .add("zAxis", 0)
            .build();
        	return valueB;
        	
        case LEFT:
        	JsonObject valueL = Json.createObjectBuilder()
            .add("xAxis", 0)
            .add("yAxis", -10)
            .add("zAxis", 0)
            .build();                
        	return valueL;
        	
        case RIGHT:
        	JsonObject valueR = Json.createObjectBuilder()
            .add("xAxis", 0)
            .add("yAxis", 10)
            .add("zAxis", 0)
            .build();                
        	return valueR;
            
        case UP:
        	JsonObject valueU = Json.createObjectBuilder()
            .add("xAxis", 0)
            .add("yAxis", 0)
            .add("zAxis", 10)
            .build();        	             
        	return valueU;
            
        case DOWN:
        	JsonObject valueD = Json.createObjectBuilder()
            .add("xAxis", 0)
            .add("yAxis", 0)
            .add("zAxis", -10)
            .build();
        	return valueD;
        }   		
    	
    	JsonObject value = Json.createObjectBuilder()
                .add("x", 0)
                .add("y", 0)
                .add("z", 0)
                .build();
    	return value;
    }
    
    /* APState nous informe sur l'état de l'AP, s'il est en état WAIT ou PAS WAIT
    true = WAIT : entrain d'attendre une commande 
    false = PAS WAIT : entrain d'éxécuter une commande*/
    boolean APState() {
    	if((this.pos.getDiffX()+this.pos.getDiffY()+this.pos.getDiffZ())>0)
    			return false;
    	return true;
    	
    }

}

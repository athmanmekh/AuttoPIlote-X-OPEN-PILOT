import capteurs.*;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
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
                break;
            case FORWARD:
                break;
            case BACKWARD:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            case UP:
                break;
            case DOWN:
                break;
        }
    }
	
	// maj capteurs 
    public void update(JsonObject capteurs) {
    	float x = BigDecimal.valueOf(capteurs.getDouble("x")).floatValue();
    	float y = BigDecimal.valueOf(capteurs.getDouble("y")).floatValue();
    	float z = BigDecimal.valueOf(capteurs.getDouble("z")).floatValue();
    	
    	this.pos.setX(x);
    	this.pos.setY(y);
    	this.pos.setZ(z);
    	//comment faire la maj du contact ???
    	
    }

	// calcule le tableau diff
    public void compute() {}

    public JsonObject createInstruction() {
        JsonObject value = Json.createObjectBuilder()
            .add("x", 0)
            .add("y", 0)
            .add("z", 0)
            .build();
            // .add("firstName", "John")
            // .add("lastName", "Smith")
            // .add("age", 25)
            // .add("address", Json.createObjectBuilder()
            //     .add("streetAddress", "21 2nd Street")
            //     .add("city", "New York")
            //     .add("state", "NY")
            //     .add("postalCode", "10021"))
            // .add("phoneNumber", Json.createArrayBuilder()
            //     .add(Json.createObjectBuilder()
            //         .add("type", "home")
            //         .add("number", "212 555-1234"))
            //     .add(Json.createObjectBuilder()
            //         .add("type", "fax")
            //         .add("number", "646 555-4567")))
        return value;
    }

}

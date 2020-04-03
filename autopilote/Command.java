public enum Command {

    JOYSTICK ("JOYSTICK"), // {'x': float, 'y': float, 'z': float}
    GOTO ("GOTO"), // {'x': float, 'y': float, 'z': float}

    LAND ("LAND"),
    TAKEOFF ("TAKEOFF"),

    // FORWARD ("FORWARD"),
    // BACKWARD ("BACKWARD"),
    // LEFT ("LEFT"),
    // RIGHT ("RIGHT"),
    // UP ("UP"),
    // DOWN ("DOWN"),
    WAIT ("WAIT") ;

    private String name = "";

    Command(String s) { this.name = s; }

    public String getName() { return this.name; }

}

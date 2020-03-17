public enum Command {

    GOTO ("GOTO"),
    FORWARD ("FORWARD"),
    BACKWARD ("BACKWARD"),
    LEFT ("LEFT"),
    RIGHT ("RIGHT"),
    UP ("UP"),
    DOWN ("DOWN"),
    WAIT ("WAIT") ;

    private String name = "";

    Command(String s) { this.name = s; }

    public String getName() { return this.name; }
    

}

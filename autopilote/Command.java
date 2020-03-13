public enum Command {

    GOTO ("GOTO"),
    FORWARD ("FORWARD"),
    BACKWARD ("BACKWARD"),
    LEFT ("LEFT"),
    RIGHT ("RIGHT"),
    UP ("UP"),
    DOWN ("DOWN"),
    NONE ("NONE") ;

    private String name = "";

    Command(String s) { this.name = s; }

    public String getName() { return this.name; }
    

}

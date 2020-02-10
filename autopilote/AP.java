public class AP {
    private Move cmd;
    private MCapteurs contact;
    private MCapteurs pos;

    public AP() {
        this.cmd = Move.NONE;
        this.contact = new Contact(.0, .0, .0, .0);
        this.pos = new Position(.0, .0, .0);
    }

    public void init(Move c, JSONObject data);

    public Move compute();

}

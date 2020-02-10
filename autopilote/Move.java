public enum Move {
  FORWARD ("FORWARD"),
  BACKWARD ("BACKWARD"),
  LEFT ("LEFT"),
  RIGHT ("RIGHT"),
  UP ("UP"),
  DOWN ("DOWN"),
  NONE ("NONE") ;

  private int cmd;

  Move(int i) { this.cmd = i; }

}

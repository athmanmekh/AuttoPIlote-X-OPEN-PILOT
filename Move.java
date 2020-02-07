public enum Move {
  FORWARD (0),
  BACKWARD (1),
  LEFT (2),
  RIGHT (3),
  UP (4),
  DOWN (5),
  NONE (-1) ;

  private int cmd;

  Move(int i) { this.cmd = i; }

}

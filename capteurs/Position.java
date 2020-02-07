public class Position extends MCapteurs {
  public Position(float x, float y, float target_x, float target_y) {
    this.data = new float[2];
    this.target = new float[2];
    this.diff = new float[2];

    this.data[0] = x;
    this.data[1] = y;

    this.target[0] = target_x;
    this.target[1] = target_y;
  }


  public void setX(float x) { this.data[0] = x; }
  public void setY(float y) { this.data[1] = y; }

  public float getX() { return this.data[0]; }
  public float getY() { return this.data[1]; }

  public void setTargetX(float x) { this.data[0] = x; }
  public void setTargetY(float y) { this.data[1] = y; }

  public float getTargetX() { return this.data[0]; }
  public float getTargetY() { return this.data[1]; }

  public float getDiffX() { return this.diff[0]; }
  public float getDiffY() { return this.diff[1]; }

  public void computeDiff() {}
}

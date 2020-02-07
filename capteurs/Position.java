public class Position extends MCapteurs {
  public Position(float x, float y, float z, float target_x, float target_y, float target_z) {
    this.data = new float[3];
    this.target = new float[3];
    this.diff = new float[3];

    this.data[0] = x;
    this.data[1] = y;
    this.data[2] = z;

    this.target[0] = target_x;
    this.target[1] = target_y;
    this.target[2] = target_z;

    this.diff[0] = 0; this.diff[1] = 0; this.diff[2] = 0;
  }


  public void setX(float x) { this.data[0] = x; }
  public void setY(float y) { this.data[1] = y; }
  public void setZ(float z) { this.data[2] = z; }

  public float getX() { return this.data[0]; }
  public float getY() { return this.data[1]; }
  public float getZ() { return this.data[2]; }

  public void setTargetX(float x) { this.data[0] = x; }
  public void setTargetY(float y) { this.data[1] = y; }
  public void setTargetZ(float z) { this.data[2] = z; }

  public float getTargetX() { return this.data[0]; }
  public float getTargetY() { return this.data[1]; }
  public float getTargetZ() { return this.data[2]; }

  public float getDiffX() { return this.diff[0]; }
  public float getDiffY() { return this.diff[1]; }
  public float getDiffZ() { return this.diff[2]; }

  public void computeDiff() {}
}

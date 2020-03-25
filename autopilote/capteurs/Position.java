package capteurs;

public class Position extends Capteur {
    public Position() {
        super(3);
    }

    public void setX(float x) { this.setData(0, x); }
    public void setY(float y) { this.setData(1, y); }
    public void setZ(float z) { this.setData(2, z); }

    public float getX() { return this.getData(0); }
    public float getY() { return this.getData(1); }
    public float getZ() { return this.getData(2); }

    public void setTargetX(float x) { this.setTarget(0, x); }
    public void setTargetY(float y) { this.setTarget(1, y); }
    public void setTargetZ(float z) { this.setTarget(2, z); }

    public float getTargetX() { return this.getTarget(0); }
    public float getTargetY() { return this.getTarget(1); }
    public float getTargetZ() { return this.getTarget(2); }

    public float getDiffX() { return this.getDiff(0); }
    public float getDiffY() { return this.getDiff(1); }
    public float getDiffZ() { return this.getDiff(2); }

}

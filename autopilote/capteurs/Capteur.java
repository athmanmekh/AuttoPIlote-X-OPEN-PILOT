public class Capteur extends MCapteurs {
    public Capteur(int size) {
        this.length = size;
        this.data = new float[this.length];
        this.target = new float[this.length];
        this.diff = new float[this.length];
    }

    public void setData(int i, float x) { this.data[i] = x; }
    public void setTarget(int i, float x) { this.target[i] = x; }
    public void setDiff(int i, float x) { this.diff[i] = x; }

    public float getData(int i) { return this.data[i]; }
    public float getTarget(int i) { return this.target[i]; }
    public float getDiff(int i) { return this.diff[i]; }

    public void computeDiff() {
        for (int i=0; i < this.length; i++) this.diff[i] = this.target[i] - this.data[i];
    }

}

package capteurs;

public abstract class MCapteurs {
    protected int length;
    protected float[] data;
    protected float[] target;
    protected float[] diff;

    // public float[] getData();
    // public float[] getTarget();
    // public float[] getDiff();

    public abstract void computeDiff();
}

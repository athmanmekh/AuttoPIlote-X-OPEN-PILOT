package capteurs;

public abstract class MCapteurs {
    protected int length;
    protected float[] data;
    protected float[] target;
    protected float[] diff;

    public abstract void computeDiff();
}

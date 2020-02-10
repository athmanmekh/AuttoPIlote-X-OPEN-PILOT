public abstract MCapteurs {
    private int length;
    private float[] data;
    private float[] target;
    private float[] diff;

    // public float[] getData();
    // public float[] getTarget();
    // public float[] getDiff();

    public void computeDiff();
}

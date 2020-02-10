public class Contact extends MCapteurs {
    public Contact(float f, float b, float l, float r) {
        this.length = 4;
        this.data = new float[this.length];
        this.target = new float[this.length];

        // diff doit etre manipule comme un tableau de booleen
        // si une valeur du tableau vaut 1 alors le drone est trop proche d'un obstacle
        this.diff = new float[this.length];
        for (int i=0; i < this.length; i++) this.diff[i] = 0;

        this.data[0] = f;
        this.data[1] = b;
        this.data[2] = l;
        this.data[3] = r;

        float dist = 1.; // la distance minimum entre le drone et un obstacle
        this.target
    }

    public void setForward(float f) { this.data[0] = f; }
    public void setBackward(float b) { this.data[1] = b; }
    public void setLeft(float l) { this.data[2] = l; }
    public void setRight(float r) { this.data[3] = r; }

    public float getForward() { return this.data[0]; }
    public float getBackward() { return this.data[1]; }
    public float getLeft() { return this.data[2]; }
    public float getRight() { return this.data[3]; }

    public void setTargetForward(float f) { this.target[0] = f; }
    public void setTargetBackward(float b) { this.target[1] = b; }
    public void setTargetLeft(float l) { this.target[2] = l; }
    public void setTargetRight(float r) { this.target[3] = r; }

    public float getTargetForward() { return this.target[0]; }
    public float getTargetBackward() { return this.target[1]; }
    public float getTargetLeft() { return this.target[2]; }
    public float getTargetRight() { return this.target[3]; }

    public float getDiffForward() { return this.diff[0]; }
    public float getDiffBackWard() { return this.diff[1]; }
    public float getDiffLeft() { return this.diff[2]; }
    public float getDiffRight() { return this.diff[3]; }

    public void computeDiff() {}
}

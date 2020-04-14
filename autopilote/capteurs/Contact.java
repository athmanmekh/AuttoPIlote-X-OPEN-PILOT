package capteurs;

public class Contact extends Capteur {
    // le tableau this.diff doit etre manipule comme un tableau de booleen
    // si une valeur du tableau vaut 1 alors le drone est trop proche d'un obstacle, sinon la valeur est 0

    public Contact() {
        super(4);
    }

    public void setForward(float f) { this.setData(0, f); }
    public void setBackward(float b) { this.setData(1, b); }
    public void setLeft(float l) { this.setData(2, l); }
    public void setRight(float r) { this.setData(3, r); }

    public float getForward() { return this.getData(0); }
    public float getBackward() { return this.getData(1); }
    public float getLeft() { return this.getData(2); }
    public float getRight() { return this.getData(3); }

    public void setTargetForward(float f) { this.setTarget(0, f); }
    public void setTargetBackward(float b) { this.setTarget(1, b); }
    public void setTargetLeft(float l) { this.setTarget(2, l); }
    public void setTargetRight(float r) { this.setTarget(3, r); }

    public float getTargetForward() { return this.getTarget(0); }
    public float getTargetBackward() { return this.getTarget(1); }
    public float getTargetLeft() { return this.getTarget(2); }
    public float getTargetRight() { return this.getTarget(3); }

    public float getDiffForward() { return this.getDiff(0); }
    public float getDiffBackward() { return this.getDiff(1); }
    public float getDiffLeft() { return this.getDiff(2); }
    public float getDiffRight() { return this.getDiff(3); }

    public void computeDiff() {
        for (int i=0; i < this.size(); i++) {
            this.setDiff(i, this.getData(i));
            // float val = (this.getTarget(i) > this.getData(i)) ? 1 : 0;
            // this.setDiff(i, val);
        }
    }

}

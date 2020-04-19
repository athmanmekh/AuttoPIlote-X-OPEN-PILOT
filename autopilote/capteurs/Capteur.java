package capteurs;

public class Capteur {
	private int length;
	private float data[];
	private float target[];
	private float diff[];

	public Capteur(int size) {
		this.length = size;
		this.data = new float[this.length];
		this.target = new float[this.length];
		this.diff = new float[this.length];

		for (int i = 0; i < this.length; i++) {
			this.data[i] = 0;
			this.target[i] = 0;
			this.diff[i] = 0;
		}
	}

	public int size() { return this.length; }

	public void setData(int i, float x) { this.data[i] = x; }
	public float getData(int i) { return this.data[i]; }

	public void setTarget(int i, float x) { this.target[i] = x; }
	public float getTarget(int i) { return this.target[i]; }

	public void setDiff(int i, float v) { this.diff[i] = v; }
	public float getDiff(int i) { return this.diff[i]; }

	public void computeDiff() {
		for (int i = 0; i < this.length; i++) {
			float i_diff = this.target[i] - this.data[i];
			i_diff = (i_diff < -1) ? -1 : i_diff;
			i_diff = (i_diff > 1) ? 1 : i_diff;
			this.diff[i] = i_diff;
		}
	}

	public void reset() {
		for (int i=0; i < this.length; i++) {
			this.data[i] = 0;
			this.target[i] = 0;
			this.diff[i] = 0;
		}
	}

}

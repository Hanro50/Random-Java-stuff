package vectors;

public class vector4f extends vector3f implements vector{
	protected vector4f(float x,float y,float z,float w, int offst) {
		super(x, y, z, offst);
		A[3] = w;
	}
	public vector4f(float x, float y, float z,float w) {
		super(x, y, z, 2);
		A[3] = w; 
	}
	public float w() {
		return A[3];
	}
	public void w(float w) {
		A[3]=w;
	}
}

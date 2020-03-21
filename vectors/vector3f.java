package vectors;

public class vector3f extends vector2f implements vector{
	protected vector3f(float x,float y,float z, int offst) {
		super(x, y, offst);
		A[2] = z;
	}
	public vector3f(float x, float y, float z) {
		super(x, y, 1);
		A[2] = z; 
		// TODO Auto-generated constructor stub
	}
	public float z() {
		return A[2];
	}
	public void z(float z) {
			A[2]=z;
	}
}

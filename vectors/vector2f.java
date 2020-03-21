package vectors;

public class vector2f implements vector{
	private int offset;
	public float[] A;
	
	protected vector2f(float x,float y, int offst) {
		offset = offst;
		A = new float[2+offset];
		A[0] = x;
		A[1] = y;
	}
	
	public vector2f(float x,float y) {
		A = new float[2];
		A[0] = x;
		A[1] = y;
	}
	public float x() {
		return A[0];
	}
	public void x(float x) {
		A[0]=x;
	}
	
	
	public float y() {
		return A[1];
	}
	public void y(float y) {
		A[1]=y;
	}
	public void add(vector vec){
		float[] A2 = vec.get(); 
		int size = (2+offset);
		if (A2.length >= size) {
			for (int i = 0; i < size; i++) {
				A[i] += A2[i];
			}
		}
		else {
			System.err.println("Invalid "+size+"D vector");
		}	
	}
	
	public float[] get() {
		return A;
	}
	public String tostring() {
		String STR = "(";
		for (int i = 0; i < A.length ; i++) {
			STR = STR + String.format("{%f}", A[i]);
		}
		STR = STR + ")";
		
		return STR;
	}
	

}

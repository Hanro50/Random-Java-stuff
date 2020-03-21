package quark;

import org.lwjgl.opengl.GL15;

import vectors.vector2f;
import vectors.vector4f;

public class Mesh2D extends Base{

	private int offset = 0;	
	private int indicators = 0;
	int vao;
	
	int vrtVBO,clrVBO,indVBO;
	
	boolean drawable = false;
	
	public Mesh2D() {
		vao = GenVAO();
		vrtVBO = GL15.glGenBuffers();
		clrVBO = GL15.glGenBuffers();
		indVBO = GL15.glGenBuffers();
	}
	
	public void AddQuad(vector2f chords, vector4f colour, float fs) {
	float OFS = 0.5f*fs;
	begin(vao);
		float[] vertices = 
			{
				(chords.x()-OFS)*fs,(chords.y()-OFS)*fs,0f,1f,
				(chords.x()+OFS)*fs,(chords.y()-OFS)*fs,0f,1f,
				(chords.x()-OFS)*fs,(chords.y()+OFS)*fs,0f,1f,
				(chords.x()+OFS)*fs,(chords.y()+OFS)*fs,0f,1f
			};

		StoreData(vrtVBO, 0, 4, toBuff(vertices));
		float[] colors = {
				colour.x(), colour.y(), colour.z(), colour.w(),
				colour.x(), colour.y(), colour.z(), colour.w(),
				colour.x(), colour.y(), colour.z(), colour.w(),
				colour.x(), colour.y(), colour.z(), colour.w()
        };
		
		StoreData(clrVBO, 1, 4, toBuff(colors));
		int[] indi = {
				3+offset, 2+offset, 1+offset,
				0+offset, 1+offset, 2+offset
		};

		indicators += 6;
		StoreIndicators(indVBO,toBuff(indi));

		offset +=4;
	}
	
	public void Render() {
		Draw(vao, indVBO, indicators);
	}
}

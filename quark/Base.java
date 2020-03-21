package quark;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Base {
	public static int Drawmode = GL15.GL_STATIC_DRAW;
	
	protected void Draw(int VAO,int IdiVBO,int indicesCount) {
		 	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
         
	        GL20.glUseProgram(Shader.pId);
	         
	        // Bind to the VAO that has all the information about the vertices
	        GL30.glBindVertexArray(VAO);
	        GL20.glEnableVertexAttribArray(0);
	        GL20.glEnableVertexAttribArray(1);
	        
	        // Bind to the index VBO that has all the information about the order of the vertices
	        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, IdiVBO);
	         
	        //System.out.print(indicesCount);
	        // Draw the vertices
	        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
	         
	        // Put everything back to default (deselect)
	        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	        GL20.glDisableVertexAttribArray(0);
	        GL20.glDisableVertexAttribArray(1);
	        GL30.glBindVertexArray(0);
	        GL20.glUseProgram(0);
	        
	        GL11.glFlush();
	        
	        int ierror = GL20.glGetError();
	        if (ierror != GL20.GL_NO_ERROR) {
	        	
	        	throw new Error("OPENGL RENDERING ERROR. Code:" + ierror);
	        }
	}

	protected int GenVAO() {
		int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        return vaoId;
	}
	
	protected void begin(int vaoId) {
        GL30.glBindVertexArray(vaoId);
	}
	
	protected int StoreData(int attribute, int Dimentions,FloatBuffer Buffer ) {
		int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Buffer, Drawmode);
        GL20.glVertexAttribPointer(attribute, Dimentions, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);  
        return vboId;
	}
	
	protected void StoreData(int vboId,int attribute, int Dimentions,FloatBuffer Buffer ) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Buffer, Drawmode);
        GL20.glVertexAttribPointer(attribute, Dimentions, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	
	protected int StoreIndicators(IntBuffer indicesBuffer) {
		int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        return vboId;
	}
	
	protected void StoreIndicators(int vboId,IntBuffer indicesBuffer) {
		//GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	protected void StoreIndicators(int vboId,ByteBuffer indicesBuffer) {
		//GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	
	protected FloatBuffer toBuff(float[] array) {
		FloatBuffer buff = BufferUtils.createFloatBuffer(array.length);
		buff.put(array);
		buff.flip();
		return buff;
	}
	
	protected IntBuffer toBuff(int[] array) {
		IntBuffer buff = BufferUtils.createIntBuffer(array.length);
		buff.put(array);
		buff.flip();
		return buff;
	}
	
	protected ByteBuffer toBuff(byte[] array) {
		ByteBuffer buff = BufferUtils.createByteBuffer(array.length);
		buff.put(array);
		buff.flip();
		return buff;
	}
}

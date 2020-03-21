package dekeract;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import core.Core_inf;
import types.meshf;
import types.vector2f;
import types.vector3f;
import types.vector4f;

public class R3D {
	static Core_inf W;
	static int iv = 0;
	static int iv2 = 0;
	static double d = 1.0;
	
    // JOML matrices
	static Matrix4f projMatrix = new Matrix4f();
	static Matrix4f viewMatrix = new Matrix4f();
	static Matrix4f modelMatrix = new Matrix4f();
	static Matrix4f modelViewMatrix = new Matrix4f();

    // FloatBuffer for transferring matrices to OpenGL
	static FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	
	
	static protected meshf vertex = new meshf();
	static protected meshf colour = new meshf();
	
	static public void Tri(vector3f[] vec, vector4f clr) {
		if (vec.length == 3) {
			vertex.add(vec);
			colour.add(clr);
			colour.add(clr);
			colour.add(clr);
		}
		else System.err.println("Tri:invalid paramaters");
	}
	static public void Tri(meshf mesh, meshf meshc,vector3f[] vec, vector4f clr) {
		if (vec.length == 3) {
			mesh.add(vec);
			meshc.add(clr);
			meshc.add(clr);
			meshc.add(clr);
		}
		else System.err.println("Tri:invalid paramaters");
	}
	
	public static void Quad(vector3f[] vec, vector4f clr) {
		if (vec.length == 4) {
			vector3f[] tri1 = {vec[0], vec[1],vec[2]};
			vector3f[] tri2 = {vec[3], vec[2],vec[1]};
			Tri(tri1, clr);
			Tri(tri2, clr);
		}
		else System.err.println("Tri:invalid paramaters");
	}
	public static void Quad(vector2f chords, vector4f clr, float fs) {
		if ((Math.abs(chords.x()) <= W.getwidth()/2f) && (Math.abs(chords.y()) <= W.getheight()/2f)) {
			
			vector3f[] vex = 
					{
						new vector3f(chords.x()-0.5f*fs,chords.y()-0.5f*fs,-1f),
						new vector3f(chords.x()+0.5f*fs,chords.y()-0.5f*fs,-1f),
						new vector3f(chords.x()-0.5f*fs,chords.y()+0.5f*fs,-1f),
						new vector3f(chords.x()+0.5f*fs,chords.y()+0.5f*fs,-1f)
					};
			
			
			vector3f[] tri1 = {vex[0], vex[1],vex[2]};
			vector3f[] tri2 = {vex[3], vex[2],vex[1]};
			Tri(tri1, clr);
			Tri(tri2, clr);
		}
	}
	
	public static void Quad(meshf mesh, meshf meshc,vector2f chords, vector4f clr, float fs) {
		if ((Math.abs(chords.x()*fs) <= W.getwidth()/2f) && (Math.abs(chords.y()*fs) <= W.getheight()/2f)) {
			float OFS = 0.5f*fs;
			vector3f[] vex = 
					{
						new vector3f((chords.x()-OFS)*fs,(chords.y()-OFS)*fs,-1f),
						new vector3f((chords.x()+OFS)*fs,(chords.y()-OFS)*fs,-1f),
						new vector3f((chords.x()-OFS)*fs,(chords.y()+OFS)*fs,-1f),
						new vector3f((chords.x()+OFS)*fs,(chords.y()+OFS)*fs,-1f)
					};
			
			
			vector3f[] tri1 = {vex[0], vex[1],vex[2]};
			vector3f[] tri2 = {vex[3], vex[2],vex[1]};
			Tri(mesh,meshc, tri1, clr);
			Tri(mesh,meshc, tri2, clr);
		}
	}
	
	static public void init(Core_inf Winit) {
		W = Winit;
	}
	
	static public void begin() {
		if (W == null) {System.err.println("(Critical Error): R3D is not initialized"); return;} 
		vertex.clear();
		colour.clear();
		
		// Make the viewport always fill the whole window.
        glViewport(0, 0, W.getwidth(), W.getheight());
        //GL30.glOrtho( -W.getwidth()/2f+W.getCam().x(),  W.getwidth()/2f+W.getCam().x(), -W.getheight()/2f+W.getCam().y(), W.getheight()/2f+W.getCam().y(), 1f+W.getCam().z(), 100f+W.getCam().z());
        // Build the projection matrix. Watch out here for integer division
        // when computing the aspect ratio!
        float aspect = (float) W.getheight()/(float) W.getwidth();
       // projMatrix.setOrtho(-W.getCam().z()/2f, W.getCam().z(), (-W.getCam().z()*aspect)/2f, (W.getCam().z()*aspect)/2f,1f, 100f);
        projMatrix.setOrtho( -W.getwidth()/2f,  W.getwidth()/2f, -W.getheight()/2f, W.getheight()/2f, 1f, 100f);
        //projMatrix.setPerspective((float) Math.toRadians(45),
          //                     (float) W.getwidth()/W.getheight(), 1f, 100f);

        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(projMatrix.get(fb));

        // Set lookat view matrix
        viewMatrix.setLookAt(
        			W.getCam().x()	, W.getCam().y(), 50.0f -W.getCam().z(),
        			W.getCam().x()	, W.getCam().y(), 0.0f  -W.getCam().z(),
                             0.0f		,-1.0f		, 0.0f				);
        glMatrixMode(GL_MODELVIEW);
	}
	public static void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void end() {
		//if ((fcBuff == null)||(fvBuff == null)) {Export(); return;};
		
		GL20.glEnableClientState(GL20.GL_COLOR_ARRAY);
		GL20.glEnableClientState(GL20.GL_VERTEX_ARRAY);
		
		GL20.glColorPointer(4, GL20.GL_FLOAT, 0, colour.asbuffer());
		GL20.glVertexPointer(3, GL20.GL_FLOAT, 0, vertex.asbuffer());
		
		GL20.glPushMatrix();
		GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, vertex.getsize()/3);
		GL20.glPopMatrix();
		
		GL20.glDisableClientState(GL20.GL_COLOR_ARRAY);
		GL20.glDisableClientState(GL20.GL_VERTEX_ARRAY);
		
		//if (V.MW!=null) {V.MW.swapbuff();}
		//GLFW.glfwSwapBuffers(W.getWin());
	}
	public static void zoom(float in) {
		//clear();
        //projMatrix.setOrtho( -W.getwidth()/2f,  W.getwidth()/2f, -W.getheight()/2f, W.getheight()/2f, 1f, 100f);
        //glLoadMatrixf(projMatrix.get(fb));

		end();
		modelMatrix.scale(in,in,0f);
		glLoadMatrixf(viewMatrix.mul(modelMatrix, modelViewMatrix).get(fb));
		GLFW.glfwSwapBuffers(W.getWin());
	}
	
	
	public static void draw(meshf mesh, meshf meshc) {
		FloatBuffer fcBuff = meshc.asbuffer();
		FloatBuffer fvBuff = mesh.asbuffer();
		if ((fcBuff == null)||(fvBuff == null)) { return;};
		if ((fcBuff.capacity() != meshc.getsize())||(fvBuff.capacity() != mesh.getsize())) { return;};
		GL20.glEnableClientState(GL20.GL_COLOR_ARRAY);
		GL20.glEnableClientState(GL20.GL_VERTEX_ARRAY);
				
		GL20.glColorPointer(4, GL20.GL_FLOAT, 0, fcBuff);
		GL20.glVertexPointer(3, GL20.GL_FLOAT, 0, fvBuff);
		
		GL20.glPushMatrix();
		GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.getsize()/3);
		GL20.glPopMatrix();
		GL20.glDisableClientState(GL20.GL_COLOR_ARRAY);
		GL20.glDisableClientState(GL20.GL_VERTEX_ARRAY);

		
		//modelMatrix.translation(pos.x() , pos.y() , pos.z()-1);
        //glLoadMatrixf(viewMatrix.mul(modelMatrix, modelViewMatrix).get(fb));
	}
	public static void draw(FloatBuffer fvBuff, FloatBuffer fcBuff) {
		if ((fcBuff == null)||(fvBuff == null)) { return;};
		GL20.glEnableClientState(GL20.GL_COLOR_ARRAY);
		GL20.glEnableClientState(GL20.GL_VERTEX_ARRAY);
				
		GL20.glColorPointer(4, GL20.GL_FLOAT, 0, fcBuff);
		GL20.glVertexPointer(3, GL20.GL_FLOAT, 0, fvBuff);
		
		GL20.glPushMatrix();
		GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, fvBuff.capacity()/3);
		GL20.glPopMatrix();
		GL20.glDisableClientState(GL20.GL_COLOR_ARRAY);
		GL20.glDisableClientState(GL20.GL_VERTEX_ARRAY);

		
		//modelMatrix.translation(pos.x() , pos.y() , pos.z()-1);
        //glLoadMatrixf(viewMatrix.mul(modelMatrix, modelViewMatrix).get(fb));
	}
	
	public static void Update() {
		GLFW.glfwSwapBuffers(W.getWin());
		GL20.glFlush();
	}
	
}
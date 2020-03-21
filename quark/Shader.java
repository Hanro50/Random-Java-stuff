package quark;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import vectors.vector3f;

public class Shader{
	private static FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
	private static int locationProjection;

	public static void setupShaders() {
		String vertex_source = 
				"#version 320 es\n" + 
				" \n" + 
				"in vec4 in_Position;\n" + 
				"in vec4 in_Color;\n" + 
				" \n" + 
				"out vec4 pass_Color;\n" + 
				" \n" +
				"uniform mat4 projection;\n" + 
				" \n" + 
				"void main(void) {\n" + 
				"    gl_Position = projection * in_Position;\n" + 
				"    pass_Color = in_Color;\n" + 
				"}";
		String fragment_source = 
				"#version 320 es\n" + 
				" precision mediump float;\n" + 
				"\n" + 
				"in vec4 pass_Color;\n" + 
				" \n" + 
				"out vec4 out_Color;\n" + 
				" \n" + 
				"void main(void) {\n" + 
				"    out_Color = pass_Color;\n" + 
				"}";
		Shader_init(vertex_source,fragment_source);
		System.out.println("Checking shader:");
		System.out.println(GL30.glGetAttribLocation( Shader.pId, "in_Color" ));
		System.out.println(GL30.glGetAttribLocation( Shader.pId, "in_Position" ));
		locationProjection = GL20.glGetUniformLocation(Shader.pId, "projection");

	}
    // Shader variables
    protected static int vsId = 0;
    protected static int fsId = 0;
    protected static int pId = 0;
	
	
	public static void Shader_init(String vertex_source,String fragment_source){
    	System.out.println("OpenGL 3.0 Supported: " + GL.createCapabilities().OpenGL30);

    	
    	//int errorCheckValue = GL11.glGetError();
    	System.out.println("Starting Shader: Running in OpenGL 3.20 es mode\n");
    	//Shader init
    	System.out.println("Creating pId\n");
    	pId = glCreateProgram();
        //vertex init
    	vsId = compile(vertex_source, pId, GL_VERTEX_SHADER);
        //fragment init
    	fsId = compile(fragment_source, pId, GL_FRAGMENT_SHADER);
    	
    	GL20.glBindAttribLocation(pId, 0, "in_Position");
    	GL20.glBindAttribLocation(pId, 1, "in_Color");
    	
        //glUseProgram(programID);
    	
    	GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);
        
        System.out.print("Done\n");
        
        //errorCheckValue = GL11.glGetError();
        //if (errorCheckValue != GL11.GL_NO_ERROR) {
    	    	//throw new RuntimeException(
    	    	//		"Error creating shader. "
    	    			
    	    	//);
    	//}
    }
   
	private static int compile(String Source, int programID, int type) {
		int ID = glCreateShader(type);
		try {
	        glShaderSource(ID, Source);
	        glCompileShader(ID);
	        error_check(ID,Source);
	        glAttachShader(programID, ID);
			
		} catch (Error e) {System.out.println(e);}
		return ID;
	}
	
	private static void error_check(int ID, String Source){
	    if (glGetShaderi(ID, GL_COMPILE_STATUS) == GL_FALSE)
	    	throw new RuntimeException(
	    			"Error creating shader. "
	    			+ "\n Source : "
	    			+ "\n "+Source+
	    			"\n" + glGetShaderInfoLog(ID, glGetShaderi(ID, GL_INFO_LOG_LENGTH))
	    	);
	}
	
	public static int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;
         System.out.println("Loading " + filename);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }
        catch (Error e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }
         
        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
         
        return shaderID;
    }
	public static void loadMatrix(int location, Matrix4f value){
		//matrix = BufferUtils.createFloatBuffer(16);
		value.get(matrix);
		GL20.glUniformMatrix4fv(location, false, matrix);
	}


	
	public static void Resize(long window, int width, int height, vector3f cam){
		
		Matrix4f matrix =new Matrix4f().setOrtho( -width/200f,  width/200f, -height/200f, height/200f, -1f, 100f);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix.get(buffer);
		//System.out.println(matrix.toString()+"|"+buffer.flip());
		GL20.glUseProgram(Shader.pId);
		GL20.glUniformMatrix4fv(locationProjection, false, buffer);
		GL20.glUseProgram(0);

	}

}

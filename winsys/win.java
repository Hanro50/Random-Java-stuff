package winsys;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import core.Core_inf;

public class win {
	Core_inf W;
	//Start call backs - Warning -> Use Interface to talk to these...
	private class Win_Callbacks {
		private GLFWWindowRefreshCallbackI BE_Redraw = new GLFWWindowRefreshCallbackI() {
			@Override public void invoke(long window) {W.Redraw(window);}
		};
				
		private GLFWWindowMaximizeCallbackI BE_Max = new GLFWWindowMaximizeCallbackI() {
			@Override public void invoke(long window, boolean maximized) {W.Max(window, maximized);}
		};
				
		private GLFWWindowSizeCallback BE_Resize = new GLFWWindowSizeCallback(){
			@Override public void invoke(long window, int width, int height){
				System.out.println("Resize![" + width + "," + height + "]");
				W.setwidth(width);
				W.setheight(height);
				W.Resize(window, width, height);
			}
		};
				
		private GLFWKeyCallbackI BE_keyin = new GLFWKeyCallbackI() {
			@Override public void invoke(long window, int key, int scancode, int action, int mods) {
				W.keyin(window,key,scancode,action,mods);	
			}
		};
	}
	
	Win_Callbacks CB = new Win_Callbacks();
		
	//constructors
	public win(String title,Runnable SideThread, Core_inf Output) {
		Thread T = new Thread(SideThread); T.run();run(title, Output);
	}
	
	public win(String title,Core_inf Output) {
		run(title, Output);
	}
	
	//Flow control
	private void run(String title,Core_inf Output) {
		W = Output;
		if (bind_test()) {
			try {
				init(title);
				loop();
			}
			catch (Error e){
				System.err.println(e);
			}
			finally {
				kill();
			}
		}
	}
	
	private boolean bind_test() {
		W.setWin(-1);
		W.setheight(-1);
		W.setwidth(-1);
		if ((W.getWin() != -1) || (W.getheight() != -1) || (W.getwidth() != -1)) {
			System.err.println("(Critical error): Core interface is configured incorrectly.");
			System.err.println("Please insure that: \n"
					+ "[setters]:setWin(),setheight(),setwidth()\n" 
					+ "[getters]:getWin(),getheight(),getwidth()\n"
					+ "are correctly configured avoid this error");
			return false;
		}
		W.setWin(NULL);
		return true; 
	}
	
	//Initializer 
	private void init(String title) {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

		//GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		//GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		
		//GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		//GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		
		
		W.setWin(GLFW.glfwCreateWindow(500, 500, title, NULL, NULL));
		if(W.getWin() == NULL) {throw new IllegalStateException("Unable to create GLFW Window");}
		
		//set callbacks
		GLFW.glfwSetKeyCallback(W.getWin(), CB.BE_keyin);
		GLFW.glfwSetWindowSizeCallback(W.getWin(), CB.BE_Resize);
		GLFW.glfwSetWindowMaximizeCallback(W.getWin(), CB.BE_Max);
		GLFW.glfwSetWindowRefreshCallback(W.getWin(), CB.BE_Redraw);
		
		//enter final init phase
		try(MemoryStack stack = stackPush()){
				IntBuffer pWidth = stack.mallocInt(1);
				IntBuffer pHeight = stack.mallocInt(1);
				
				GLFW.glfwGetWindowSize(W.getWin(), pWidth, pHeight);
				GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
				GLFW.glfwSetWindowPos(
					W.getWin(),
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
				);
				
				GLFW.glfwMakeContextCurrent(W.getWin());
				W.setheight(pHeight.get(0));
				W.setwidth(pWidth.get(0));
				
				GLFW.glfwShowWindow(W.getWin());
		}	
		GL.createCapabilities();	
		W.initCall();
	}
	
	//Loop function
	private void loop() {
		GL.createCapabilities();
		long last_time = System.nanoTime();
		while(!GLFW.glfwWindowShouldClose(W.getWin())) {
			GLFW.glfwPollEvents();
			
			long time = System.nanoTime();
			float delta_time = (int) ((time - last_time) / 1000000);
		    last_time = time;
			
		    W.loopCall(delta_time);
		}
	}
	
	private void kill() {
		try {
			W.killCall(W.getWin());
			Callbacks.glfwFreeCallbacks(W.getWin());
			GLFW.glfwDestroyWindow(W.getWin());
		} 
		finally {		
			GLFW.glfwTerminate();
			GLFW.glfwSetErrorCallback(null).free();
		}
	}
}

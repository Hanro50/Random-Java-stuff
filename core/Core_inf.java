package core;

import types.vector3f;
import types.vector4f;

public interface Core_inf {
	
	//getters. Needs to be set or you'll get an error 
	public long getWin();
	public int getwidth();
	public int getheight();
	public vector4f getCam(); //not checked!
	public float getdelta();
	//setters. Needs to be set or you'll get an error 
	public void setWin(long win);	
	public void setwidth(int iw);
	public void setheight(int ih);
	public void setCam(vector4f vec); //Not checked!
	
	//calls that bind to varies internal functions
	public void initCall(); //Called during init section 
	public void loopCall(float Delta); //Called during loop section 
	public void killCall(long window); //Called as the window closes
	
	//Call backs
	public void Redraw(long window); //A binding for the redraw callback 
	public void Max(long window, boolean maximized);
	public void Resize(long window, int width, int height);
	public void keyin(long window, int key, int scancode, int action, int mods);	
	
}

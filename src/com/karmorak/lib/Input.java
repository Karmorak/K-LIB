package com.karmorak.lib;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import com.karmorak.lib.gamestate.GSM;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.ui.button.Button;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Input {
	
	public static boolean mouseLocked = false, mouseMovedX = false, mouseMovedY = false;
	public static boolean[] keys = new boolean[GLFW_KEY_LAST];
	public static boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private static boolean updateButtons = true;
	public static Vector2 mouse = new Vector2(0, 0);
	public static Vector2 scrolled = new Vector2(0, 0);
//	@Deprecated
	public static char lastKey;
//	public static ArrayList<Character> lastKeys = new ArrayList<>();
	
	public GLFWKeyCallback getKeyboardCallback() {		
		return keyboard;
	}
	
	public GLFWCharCallback getCharCallback() {
		return keyboard_chars;		
	}
	
	
	public void setKeyboardCallback(GLFWKeyCallback keyboard) {
		this.keyboard = keyboard;
	}

	public GLFWCursorPosCallback getMousePositionCallback() {
		return mousePosition;
	}

	public void setMousePositionCallback(GLFWCursorPosCallback mousePosition) {
		this.mousePosition = mousePosition;
	}

	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return mouseButtons;
	}

	public void setMouseButtonCallback(GLFWMouseButtonCallback mouseButtons) {
		this.mouseButtons = mouseButtons;
	}
	
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}

	public void setMouseScrollCallback(GLFWScrollCallback mouseScroll) {
		this.mouseScroll = mouseScroll;
	}

	private GLFWKeyCallback keyboard;
	private GLFWCursorPosCallback mousePosition;
	private GLFWMouseButtonCallback mouseButtons;
	private GLFWScrollCallback mouseScroll;
	private GLFWCharCallback keyboard_chars;
	
//	public char getChar(int code){
//	    switch (code){
//	        case Keyboard.KEY_X: return 'x';
//	        case Keyboard.KEY_Y: return 'y';
//	    }
//	}
	

	static long frame;
	
	private static boolean mouseDown;
	private static boolean mouseDragged;
	
	static void addFrame() {
		if(frame == Long.MAX_VALUE) frame = 0;
		else frame++;
	}
	
	public Input(long windowID) {
		double[] x = new double[1];
		double[] y = new double[1];		
		
		glfwGetCursorPos(windowID, x, y);
		
		mouse.set((float)x[0],(float) y[0]);
				
		keyboard_chars = new GLFWCharCallback() {		
			
			@Override
			public void invoke(long window, int codepoint) {
				lastKey = (char) codepoint;
				GSM.keyTyped(0, (char) codepoint);
				if(updateButtons) Button.keyTyped(0, (char) codepoint);
			}
		};
			
		
		keyboard = new GLFWKeyCallback() {
			
			public void invoke(long window, int key, int scancode, int action, int modifier) {
				char c;				
				
				String key_name = GLFW.glfwGetKeyName(key, scancode);
				if(key_name == null) c = lastKey;
				else {					
					if(modifier == GLFW_MOD_SHIFT) {	
						c = Character.toUpperCase(key_name.charAt(0));						
					} else {
						c = key_name.charAt(0);
					}
				}
				
				
				if(key > 0) {	
					
					keys[key] = action != GLFW_RELEASE;
					
					if(action == GLFW_RELEASE) {
						GSM.keyUp(key, c);
					} else if (action == GLFW_PRESS) {//presss/tap the key
						GSM.keyDown(key, action, modifier);
						if(updateButtons) Button.keyDown(key, action, modifier);
					} else if (action == GLFW_REPEAT) {//hold the key
						GSM.keyDown(key, action, modifier);
						if(updateButtons) Button.keyDown(key, action, modifier);
					}
				}
			
			}
		};
		

		
		mousePosition = new GLFWCursorPosCallback() {
			
			public void invoke(long window, double xpos, double ypos) {
				
				
				ypos = KLIB.graphic.Height() - ypos;
				
				float oldx = mouse.getX();
				float oldy = mouse.getY();
//				System.out.println("x " + xpos + " : y " + ypos);
				if(oldx != xpos) {
					mouse.setX((float)xpos);
					mouseMovedX = true;
				}
				if(oldy != ypos) {
					mouse.setY((float)ypos);
					mouseMovedY = true;
				}
				
				if(updateButtons) Button.mouseMoved((int)mouse.getX(),(int) ( mouse.getY()));
				GSM.mouseMoved((int)mouse.getX(),(int) mouse.getY());
				if(mouseDown) {
					if(updateButtons) Button.touchDragged((int)mouse.getX(), (int) mouse.getY(), 0);
					GSM.touchDragged((int)mouse.getX(), (int) mouse.getY(), 0);
					mouseDragged = true;
				}
				
			}
		};
		
		mouseButtons = new GLFWMouseButtonCallback() {			
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = action != GLFW_RELEASE;
				if(action != GLFW_RELEASE) {
					GSM.touchDown(mouse.getX(), mouse.getY(), action, button);
					mouseDown = true;
				} else {
					GSM.touchUp(mouse.getX(), mouse.getY(), action, mods);
					if(updateButtons) Button.touchUp((int)mouse.getX(), (int)mouse.getY(), action, mods);
					mouseDown = false;
					if(!mouseDragged) {
						if(updateButtons) Button.touchDown((int)mouse.getX(),(int) mouse.getY(), action, mods);
						GSM.tap(mouse.getX(), mouse.getY(), action, button);
					} else {
						mouseDragged = false;
						if(updateButtons) Button.touchDown((int)mouse.getX(),(int) mouse.getY(), action, mods);
						GSM.tap(mouse.getX(), mouse.getY(), action, button);
					}
				}
			}
		};
		
		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				GSM.scrolled(offsety);
				scrolled.set((float)offsetx,(float) offsety);
			}
		};
	}
	
	
	public static void resetInput() {
//		 keys = new boolean[GLFW_KEY_LAST];
		 lastKey = Character.MIN_VALUE;
//		 lastKeys.clear();
	}
	
	public static void updateButtons(boolean bool) {
		updateButtons = bool;
	}
	
	public static void setCursor(Running run, Vector2 pos) {
		glfwSetCursorPos(run.getWindow().getID(),(double) pos.getX(),(double) pos.getY());
		mouse.set(pos);
		run.camera.setOldMouseX(pos.getX());
		run.camera.setOldMouseY(pos.getY());
	}
	
	public void destroy() {
		keyboard.free();
		mouseButtons.free();
		mousePosition.free();
		mouseScroll.free();
	}
	
}

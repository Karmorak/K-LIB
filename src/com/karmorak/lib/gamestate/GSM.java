//v 1.2
package com.karmorak.lib.gamestate;

import java.util.ArrayList;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.karmorak.lib.engine.graphic.MasterRenderer;

public abstract class GSM {
	
	private static final boolean[] inits = new boolean[64];	
	public static final String[] display_names = new String[64];
	
	//public static final ArrayList<String> display_names = new ArrayList<>();
	protected static final ArrayList<GameState> states = new ArrayList<>();
	protected static short currentstate = -1;
	public static short changto = -1;
	

	public static void addState(GameState gs, String displayName, short id) {
		states.add(id, gs);
		display_names[id] = displayName;
	}
	
	public static short init() {
		if(!states.isEmpty() && currentstate != -1) {
			if(states.get(currentstate) != null) {
				states.get(currentstate).init();
				inits[currentstate] = true;
			} else {
				System.out.println("Couldnt init \"" + display_names[currentstate] + "\"(" + currentstate + "). its null");
			}
		} else {
			System.out.println("Couldnt init (" + currentstate + "). its null");
		}
		return currentstate;		
	}
	
	public static short init(int state) {
		if(states.size() > state) {
			if(states.get(state) != null) {
				states.get(state).init();
				inits[state] = true;
			} else {
				System.out.println("Couldnt init \"" + display_names[state] + "\"(" + state + "). its null");
			}
		} else {
			System.out.println("Couldnt init \"" + display_names[state] + "\"(" + state + "). its null");
		}
			
		return currentstate;
			
	}
	
	public static short update(float deltaTime) {
		if(!states.isEmpty()) {
			if(!inits[currentstate]) {
				init();
			}
			states.get(currentstate).update(deltaTime);
		}
		return currentstate;		
	}
	
	public static short draw(MasterRenderer renderer) {
		if(!states.isEmpty()) {
			if(!inits[currentstate]) {
				init();
			}
			states.get(currentstate).draw(renderer);
		}
		return currentstate;		
	}
	
	
	public static short pause() {		
		if(!states.isEmpty()) {
			states.get(currentstate).pause();
		}
			return currentstate;		
	}
	
	public static short resume() {		
		if(!states.isEmpty())
			states.get(currentstate).resume();
		return currentstate;		
	}
	
	public static short resize(int width, int height) {		
		if(!states.isEmpty())
			states.get(currentstate).resize(width, height);
		return currentstate;		
	}
	
	public static int destroy() {		
		if(!states.isEmpty()) {
			states.get(currentstate).dispose();
			inits[currentstate] = true;		
		}
		return currentstate;		
	}
	
	
	public static void changestate(short state) {
		currentstate = state;
		if(!inits[state])
			init(state);		
	}
	
	public static void changestate(int state) {		
		currentstate = (short) state;
		if(!inits[state])
				init(state);		
	}
	
	public static int getStateInt() {
		return currentstate;
	}
	public static GameState getState() {
		return states.get(currentstate);
	}
	
	public static GameState getState(int i) {
		return states.get(i);
	}
	
	public static short tap(float x, float y, int count, int button) {
		if(!states.isEmpty())
			states.get(currentstate).tap(x, y, count, button);
		return currentstate;	
	}
	public static short touchDown(float screenX, float screenY, int pointer, int button) {
		if(!states.isEmpty())
			states.get(currentstate).touchDown(screenX, screenY, pointer, button);
		return currentstate;	
	}
	
	public static short touchUp(float screenX, float screenY, int pointer, int button) {
		if(!states.isEmpty())
			states.get(currentstate).touchUp(screenX, screenY, pointer, button);
		return currentstate;	
	}
	
	public static short touchDragged(int screenX, int screenY, int pointer) {
		if(!states.isEmpty())
			states.get(currentstate).touchDragged(screenX, screenY, pointer);
		return currentstate;			
	}
	public static short mouseMoved(int screenX, int screenY) {
		if(!states.isEmpty())
			states.get(currentstate).mouseMoved(screenX, screenY);
		return currentstate;	
	}
	public static short scrolled(double amount) {
		if(!states.isEmpty())
			states.get(currentstate).scrolled(amount);
		return currentstate;	
	}

	public static short keyDown(int glfw_key, int action, int modifier) {
		if(!states.isEmpty())
			states.get(currentstate).keyDown(glfw_key, action, modifier);
		return currentstate;	
	}	
	
	public static short keyTyped(int glfw_key, char character) {
		if(!states.isEmpty())
			states.get(currentstate).keyTyped(glfw_key, character);
		return currentstate;	
	}

	@Deprecated
	public static short keyUp(int glfw_key, char character) {
		if(!states.isEmpty())
			states.get(currentstate).keyUp(glfw_key, character);
		return currentstate;	
	}
		
	public static short globalkeyDown(NativeKeyEvent e) {
		if(!states.isEmpty())
			states.get(currentstate).globalkeyDown(e);
		return currentstate;	
	}
	
	public static short globalkeyUp(NativeKeyEvent e) {
		if(!states.isEmpty())
			states.get(currentstate).globalkeyUp(e);
		return currentstate;	
	}
	
	

}

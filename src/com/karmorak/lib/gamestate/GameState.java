package com.karmorak.lib.gamestate;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.karmorak.lib.engine.graphic.MasterRenderer;

public interface GameState {

	boolean init();

	void update(double deltaTime);

	void draw(MasterRenderer renderer);

	void pause();

	void resume();

	void resize(int width, int height);

	default void changeState(short state_change_to) {
	}

	void dispose();

	@Deprecated
	public default void keyDown(int glfw_key, char c) {
	}
	public default void keyDown(int glfw_key, int action, int modifier) {
	}
	public default void keyTyped(int glfw_key, char c) {
	}	
	public default void touchDragged(int screenX, int screenY, int pointer) {
	}
	public default void touchUp(float x, float y, int count, int button) {
	}	
	public default void touchDown(float x, float y, int count, int button) {
	}
	public default void mouseMoved(int screenX, int screenY) {
	}

	@Deprecated
	public default void scrolled(double amount) {
	}

	public default void scrolled(double amount_X, double amount_Y) {
	}
	public default void tap(float x, float y, int count, int button) {
	}
	public default void keyUp(int glfw_key, char character) {
	}
	public default void keyUp(int glfw_key) {		
	}

	@Deprecated
	public default void globalkeyDown(NativeKeyEvent e) {
	}

	@Deprecated
	public default void globalkeyUp(NativeKeyEvent e) {		
	}

	public default void globalKeyDown(NativeKeyEvent e) {
	}

	public default void globalKeyUp(NativeKeyEvent e) {
	}
	

}

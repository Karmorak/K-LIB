package com.karmorak.lib.gamestate;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.karmorak.lib.engine.graphic.MasterRenderer;

public interface GameState {
	
	public abstract void init();
	public abstract void update(float deltaTime);
	public abstract void draw(MasterRenderer renderer);
//	public abstract void draw(SpriteBatch batch);

	public abstract void pause();
	public abstract void resume();
	public abstract void resize(int width, int height);
	public abstract void dispose();
	
//	public default void draw(SpriteBatch batch, SpriteCache cache) {
//		
//	}
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
	public default void scrolled(double amount) {
	}
	public default void tap(float x, float y, int count, int button) {
	}
	@Deprecated
	public default void keyUp(int glfw_key, char character) {
	}
	public default void keyUp(int glfw_key) {		
	}
	
	public default void globalkeyDown(NativeKeyEvent e) {
	}
	
	public default void globalkeyUp(NativeKeyEvent e) {		
	}
	

}

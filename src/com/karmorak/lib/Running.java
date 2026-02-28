package com.karmorak.lib;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.GL_VERSION;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.opengl.GL46.glGetString;

import java.util.ArrayList;

import com.karmorak.lib.engine.graphic.GLTaskQueue;
import org.lwjgl.glfw.GLFW;

import com.karmorak.lib.KLIB.WindowOptions;
import com.karmorak.lib.engine.objects.Camera;
import com.karmorak.lib.gamestate.GSM;
import com.karmorak.lib.math.Vector3;

public abstract class Running implements Runnable {

		
	
//	private ArrayList<Thread> threads = new ArrayList<>();

	private Thread thread;
	
	private ArrayList<Window> windows;
	
	public Camera camera;
		
	
	public Running() {
		windows = new ArrayList<Window>();
	}
		
	public void start(String name) {
		thread = new Thread(this, name);	
		thread.start();
	}
	
	public void startDeamon(String name) {
		thread = new Thread(this, name);	
		thread.setDaemon(true);
		thread.start();
	}
	
	
	public void start(Window window) {				
		if(window == null)
			thread = new Thread(this, "No Name");		
		else {
			
			this.windows.add(window);
			thread = new Thread(this, window.getTitle());	
		}
		thread.start();
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void render();	
	
	@Override
	public void run() {
		System.out.println("Thread \"" + thread.getName() + "\"(" +thread.getId()+") initalizing...");
		KLIB.checkVersion();
		if(!windows.isEmpty()) {
			//----init----		
			Window.create();				
//			System.out.println("OpenGL: " + glGetString(GL_VERSION));		
			
			camera = new Camera(new Vector3(0, 2, 7), new Vector3(0, 0, 0));
			
			for (int i = 0; i < windows.size(); i++)  {
				Window window = windows.get(i);				
				window.init();	
				
				System.out.println("Screen: " + window.getVidmode().width() + " x " + window.getVidmode().height() + " -> " + (int)window.getBounds().getWidth() + " x " + (int)window.getBounds().getHeight());
				
			}	
			//----init fertig----	

			init();		
			while(!thread.isInterrupted()) {
				GLTaskQueue.executeAll();
				Input.addFrame();
				Input.resetInput();
				for (Window value : windows) value.update();
				update();	
				render();
				int error = glGetError();
				if (error != GL_NO_ERROR) {
					System.err.println("OpenGL Error detektiert: " + error);
				}
				
				for (int i = 0; i < windows.size(); i++) {
					Window window = windows.get(i);	
					glfwSwapBuffers(window.getID());	
					if(glfwWindowShouldClose(window.getID())) {
						if(windows.size() > 1)
							close(window);
						else
							close();
					}	
				}
			}
		} else {
			//----init----							
			init();	
//			window.setPosition(new Vector2((KLIB.graphic.MonitorWidth() - KLIB.graphic.Width()) * 0.5f, (KLIB.graphic.MonitorHeight() - KLIB.graphic.Width())*0.5f));
			
			//----init fertig----				
			while(!thread.isInterrupted()) {				
				update();	
			}
		}

	}
	
	void open_newWindow(WindowOptions opt) {	
		windows.add(Window.open_newWindow(opt));		
	}
	
	
	public Window getWindow() {
		return windows.getFirst();
	}
	
	public void close() {
		GSM.destroy();

		for (Window window : windows) {
			window.destroy();
		}
		windows.clear();
		
		thread.interrupt();
	}
	
	public void close(Window window) {
			
		window.destroy();			
		windows.remove(window);	
		
//		thread.interrupt();
//		thread.stop();
	}
	
	

}

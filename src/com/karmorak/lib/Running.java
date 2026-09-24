package com.karmorak.lib;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.karmorak.lib.engine.graphic.GLTaskQueue;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.gamestate.StateManager;
import org.lwjgl.glfw.GLFW;

import com.karmorak.lib.KLIB.WindowOptions;
import com.karmorak.lib.engine.objects.Camera;

public abstract class Running implements Runnable {

		
	
//	private ArrayList<Thread> threads = new ArrayList<>();

	private Thread thread;
	private final ArrayList<Window> windows;
	public Camera camera;
	private MasterRenderer renderer;
    @Deprecated
	public static boolean use_GlobalRenderer = false;
	
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

	public void start(Window window, MasterRenderer renderer) {
		if (window == null)
			thread = new Thread(this, "No Name");
		else {
			this.windows.add(window);
			thread = new Thread(this, window.getTitle());
		}
		if (renderer != null) {
			this.renderer = renderer;
		}
		thread.start();
	}
	
	public abstract void init();

	public abstract void update(double delta);

    public abstract void render(MasterRenderer renderer);

	
	@Override
	public void run() {
		System.out.println("Thread \"" + thread.getName() + "\"(" +thread.getId()+") initalizing...");
		KLIB.checkVersion();
		if(!windows.isEmpty()) {
			//----init----		
			Window.create();				
//			System.out.println("OpenGL: " + glGetString(GL_VERSION));		

//			camera = new Camera(new Vector3(0, 2, 7), new Vector3(0, 0, 0));

			for (Window window : windows) {
				window.init();
				System.out.println("Screen: " + window.getVidmode().width() + " x " + window.getVidmode().height() + " -> " + (int) window.getBounds().getWidth() + " x " + (int) window.getBounds().getHeight());
			}
			//----init fertig----
            if (renderer == null) {
                renderer = new MasterRenderer();
            }
            renderer.create();

			init();
			while(!thread.isInterrupted()) {
				GLTaskQueue.executeAll();
				Input.addFrame();
				Input.resetInput();
				for (Window window : windows) window.update();
				update(Window.getDelta());

                render(renderer);
                renderer.renderBatch(this);

				int error = glGetError();
				if (error != GL_NO_ERROR)
					System.err.println("OpenGL Error detektiert: " + error);
				
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
			//----init fertig----				
			while(!thread.isInterrupted()) {
				update(Window.getDelta());
			}
		}

	}
	
	void open_newWindow(WindowOptions opt) {	
		windows.add(Window.open_newWindow(opt));		
	}
	
	
	public Window getWindow() {
        try {
            return windows.getFirst();
        } catch (NoSuchElementException _) {
            return null;
        }
	}
	
	public void close() {
		StateManager.destroy();
        KLIB.dispose();
		for (Window window : windows) {
			window.destroy();
		}
		windows.clear();

		GLTaskQueue.executeAll();

		// 4. Den Thread sauber stoppen und warten
//		is_running = false;
		try {
			thread.interrupt();
			thread.join(1000); // Warte max. 1 Sekunde, bis der Thread wirklich tot ist
		} catch (InterruptedException _) {
		}

		GLFW.glfwTerminate();

		// 6. JNativeHook (falls verwendet)
		try {
			if (GlobalScreen.isNativeHookRegistered()) {
				GlobalScreen.unregisterNativeHook();
			}
		} catch (Exception _) {
		}
        System.out.println("Shutdown...");
		System.exit(0);
	}
	
	public void close(Window window) {
		window.destroy();			
		windows.remove(window);
        close();
        System.out.println("Shutdown win ...");
        System.exit(0);
	}

	public MasterRenderer Renderer() {
		return renderer;
	}
	

}

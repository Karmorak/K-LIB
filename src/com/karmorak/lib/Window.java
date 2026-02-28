//Window v3
package com.karmorak.lib;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import com.karmorak.lib.KLIB.WindowOptions;
import com.karmorak.lib.engine.audio.AudioMaster;
import com.karmorak.lib.gamestate.GSM;
import com.karmorak.lib.math.Matrix4;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.utils.PNGDecoder;
import com.karmorak.lib.utils.PNGDecoder.Format;

public class Window {
	
//	public abstract void init();
//	public abstract void update();
//	public abstract void draw();
	
	public static ArrayList<Window> windows = new ArrayList<>();
	
	private Vector2 bounds;
	private Vector2 position = new Vector2(0, 0);
	private long windowID;
//	private float FOV = 90f;
	

	private int frames;
	private static double time;
	private static double delta;
	private static double lastFrameTime = KLIB.graphic.getTime();
	
	private Input input;
	private Matrix4 projection;
	private GLFWVidMode vidmode;
	GLFWWindowPosCallback positionCallback;
	private GLFWWindowSizeCallback sizeCallback;
	private GLFWWindowCloseCallback closeCallback;
	private boolean isResized = false;
	final WindowOptions options;
	
	private final int SAMPLES;
	private final boolean USE_ALPHA_BACKGROUND;
	
	private static SystemTray tray;
	private static TrayIcon trayIcon;
	
	
	
//	private AudioMaster am;
	
	public Window() {
		float width = 1000f;
		float height = 500f;
		bounds = new Vector2(width, height);
		options = new WindowOptions();
		SAMPLES = options.samples;
		USE_ALPHA_BACKGROUND = options.use_alpha_background;
		
		windows.add(this);
	}
	
	public Window(String title) {
		float width = 1000f;
		float height = 500f;
		bounds = new Vector2(width, height);
		options = new WindowOptions();
		setTitle(title);
		SAMPLES = options.samples;
		USE_ALPHA_BACKGROUND = options.use_alpha_background;
		windows.add(this);
	}
	
	public Window(int width, int height, String title) {		
		bounds = new Vector2(width, height);		
		options = new WindowOptions();
		setTitle(title);
		SAMPLES = options.samples;
		USE_ALPHA_BACKGROUND = options.use_alpha_background;
		windows.add(this);
	}
	
	public Window(WindowOptions options) {	
		bounds = new Vector2(options.width, options.height);		
		
		this.options = options;		
		this.SAMPLES = options.samples;
		USE_ALPHA_BACKGROUND = options.use_alpha_background;		
		
		windows.add(this);	
	}
	
	public Window(int width, int height, String title, int samples, boolean start_hiden, boolean fullscreen, boolean decorated, boolean v_sync, boolean alpha_background) {		
		bounds = new Vector2(width, height);		
		
		options = new WindowOptions();
		
		options.window_title = title;
		
		options.v_sync = v_sync;
		options.is_fullscreen = fullscreen;
		options.is_hidden = start_hiden;
		options.is_decorated = decorated;
		
		this.SAMPLES = samples;
		USE_ALPHA_BACKGROUND = alpha_background;
		windows.add(this);
	}
	
	public static long primaryMonitor = -1;
	public static long[] monitors;
	public static boolean INIT = false;
	
	public static void create() {
		
		if(INIT) return;
		
		if(!glfwInit()) {
			System.err.println("GLFW init failed");
		}
			
		System.out.println("GLFW: " + glfwGetVersionString());
			
		PointerBuffer glfwMonitors = GLFW.glfwGetMonitors();

		if(monitors == null && glfwMonitors != null) {
			monitors = new long[glfwMonitors.limit()];
			for (int i = 0; i < glfwMonitors.limit(); i++) {
			    monitors[i] = glfwMonitors.get(i);
			}
		}
			
		
			
		System.out.print("OpenAL...");
		AudioMaster.init();
		System.out.println("done!");
			
	
		INIT = true;
			
	
	}
		
	public void init() {
		primaryMonitor = glfwGetPrimaryMonitor();	
		GLFWVidMode mode = glfwGetVideoMode(primaryMonitor);
		setVidmode(mode);
		
		glfwWindowHint(GLFW_VISIBLE, options.is_hidden ? GLFW_FALSE : GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, options.is_fullscreen ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_DECORATED, options.is_decorated ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_SAMPLES, SAMPLES);
		glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, USE_ALPHA_BACKGROUND ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, options.is_resizeable ? GLFW_TRUE : GLFW_FALSE);

		openWindow();
		
		if(windowID == 0) {
			System.err.println("Could not create Window!");
		}
		
//		glfwSetWindowMonitor(windowID, monitors[1], 0, 0,(int) bounds.getX(),(int) bounds.getY(), 60);
//		position = new Vector2(0, 0);
		position = new Vector2((int)((getVidmode().width() - bounds.getX()) * 0.5f), (int)((getVidmode().height() - bounds.getY()) * 0.5f));		
		
		GLFW.glfwSetWindowPos(windowID,  (int)position.getX(), (int) position.getY());
		glfwMakeContextCurrent(windowID);
		GLFW.glfwSwapInterval(options.v_sync ? GLFW_TRUE : GLFW_FALSE);
		

		
		
//        FileInputStream inputStream = new FileInputStream(KLIB.ASSET_PATH + "lib_icon.png");         
//        // reads input image from file
//        BufferedImage inputImage = ImageIO.read(inputStream);
		
		String icon_path = KLIB.lib.ASSET_PATH + "lib_icon.png";
        
		if(options.icon_path != null) {
			icon_path = options.icon_path;
		}
		
		try {		 		   
		    ByteBuffer buF = ByteBuffer.allocateDirect(64 * 64 * 4);
		    buF.position(0);
		    
		    InputStream stream = KLIB.URL(icon_path).openStream();
		    
		    PNGDecoder dec = new PNGDecoder(stream);
		    dec.decode(buF, 64*4, Format.RGBA);
		    
		   buF.flip();
		   buF.limit(buF.capacity());
		   buF.position(0);
		    
		    try (final var images = GLFWImage.create(1)) {
		        images.get(0).set(64, 64, buF);
		        glfwSetWindowIcon(windowID, images);
		    }
		    
		    stream.close();
		} catch (IOException io){
		    System.out.println("Could not load window icon!");
		    System.out.println(io.toString());
		}
		
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);	
		
	}
	
	public void update() {
		
		if(isResized) {
			isResized = false;
			GL46.glViewport(0, 0,(int) bounds.getX(),(int) bounds.getY());
//			projection = Matrix4.projection(FOV, bounds.getX() / bounds.getY(), 0.1f, 1000f);
		}		
		
		
		
		frames++;
		double currentTime = KLIB.graphic.getTime();
		delta = (currentTime - lastFrameTime);
		lastFrameTime = currentTime;
		
		
		if(time >= 1) {	
			GLFW.glfwSetWindowTitle(windowID, WindowOptions.app_version + " " + options.window_title + " | FPS: " + frames);
			frames = 0;
			time = 0;
		}
		time += delta;
	
		if(!options.v_sync) syncFrameRate(options.target_fps);		
		
		if(Input.keys[GLFW_KEY_F11]) setFullscreen(!options.is_fullscreen);
		glfwPollEvents();

	}

	public static void updateMonitors() {		//Muss bei änderungen der Monitore manuel aufgerufen werden
		PointerBuffer glfwMonitors = GLFW.glfwGetMonitors();
		if(glfwMonitors != null) {
			Window.monitors = new long[glfwMonitors.limit()];
			for (int i = 0; i < glfwMonitors.limit(); i++) {
				Window.monitors[i] = glfwMonitors.get(i);
			}
		}
	}
	
	private void openWindow() {
		windowID = glfwCreateWindow((int)bounds.getX(),(int) bounds.getY(), getTitle(), NULL, NULL);	
		if(input == null)
			input = new Input(windowID);
		if(closeCallback == null)
			createCallbacks();
	}
	
	public static Window open_newWindow(WindowOptions opt) {
		Window window = new Window(opt);
		
		window.openWindow();	
		
		return window;
	}
	
	@Deprecated
	public void setFOV(float fov) {
//		FOV = fov;
		isResized = true;
	}
	
	public static double getDelta() {
		return delta;
	}
	
	
	public void setViewport(int width, int height) {
		GL46.glViewport(0, 0,width,height);
		glfwSwapBuffers(windowID);
	}
	
	public void setMonitor(long monitor) {
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		setVidmode(mode);
		
		setSize(mode.width(), mode.height());
//		position = new Vector2((int)((getVidmode().width() - bounds.getX()) * 0.5f), (int)((getVidmode().height() - bounds.getY()) * 0.5f));		
		
		GLFW.glfwSetWindowPos(windowID,  (int)position.getX(), (int) position.getY());
		glfwMakeContextCurrent(windowID);
		
		isResized = true;
		
		update();
		
		glfwSwapBuffers(windowID);
	}
	
	public float getWidth() {
		return bounds.getWidth();
	}
	
	public float getHeight() {
		return bounds.getHeight();
	}	
	public Vector2 getBounds() {
		return bounds;
	}
	public Vector2 getSize() {
		return bounds;
	}
	public void setSize(int width, int height) {
		this.bounds.set(width, height);
		isResized = true;
		GLFW.glfwSetWindowSize(windowID,(int) bounds.getWidth(),(int) bounds.getHeight());
	}
	public void setSize(Vector2 bounds) {
		setSize((int)bounds.getWidth(),(int) bounds.getHeight());
	}

	
	public float getX() {
		return position.getX();
	}
	
	public float getY() {
		return position.getY();
	}
	
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(int pos_x, int pos_y) {
		this.position.set(pos_x, pos_y);;
		GLFW.glfwSetWindowPos(windowID,  pos_x, pos_y);
	}	
	
	public void setPosition(Vector2 position) {
		this.position = position;
		GLFW.glfwSetWindowPos(windowID,  (int)position.getX(), (int) position.getY());
		glfwSwapBuffers(windowID);
	}	

	public void setPositionMiddle() {
		glfwSetWindowPos(windowID, (int) ((KLIB.graphic.MonitorWidth() - bounds.getWidth()) * 0.5f),(int) ((KLIB.graphic.MonitorHeight() - bounds.getHeight()) * 0.5f));
		glfwSwapBuffers(windowID);
	}

	public boolean isFullscreen() {
		
		return options.is_fullscreen;
	}

	public void iconifyWindow() {		           
		glfwIconifyWindow(windowID);	
	}
	
	
	public void iconifyInSystemTray() {
		if(trayIcon == null || tray == null) createSystemTrayIcon();
		
		try {
	        tray.add(trayIcon);
	    } catch (AWTException e) {
	    	System.out.println("TrayIcon could not be added.");
	    }
	} 
	
	private static void setTrayIconPopupMenu() {
	    final PopupMenu popup = new PopupMenu();
        
        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");
       
        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
       
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });        
      
	}
	
	public static void addTrayIconPopupMenu(PopupMenu popup) {
		  trayIcon.setPopupMenu(popup);
	}
	
	private static void createSystemTrayIcon() {
		BufferedImage inputImage = null;
	      try {
	    	URL url =   KLIB.URL(KLIB.lib.ASSET_PATH + "lib_icon_16.png");
	    	  
			InputStream stream = url.openStream();
			inputImage = ImageIO.read(stream);
			stream.close();
			
			
	      } catch (IOException e) {
			// TODO Auto-generated catch block
	    	  System.out.println("failed to read trayicon");
	      }
			
			//Check the SystemTray is supported
	        if (!SystemTray.isSupported()) {
	            System.out.println("SystemTray is not supported");
	            return;
	        }
	        
	        tray = SystemTray.getSystemTray();	    
	        trayIcon =  new TrayIcon(inputImage);
	        trayIcon.setImageAutoSize(true);	
	        trayIcon.setToolTip(KLIB.APP_NAME);
	        inputImage.flush();
//	        setTrayIconPopupMenu();
	}
	
	public static void addTrayIconListener(ActionListener listener) {
		trayIcon.addActionListener(listener);
	}
	
	public static TrayIcon getTrayIcon() {
		return trayIcon;		
	}
	
	public static void destroyTrayIcon() {
		tray.remove(trayIcon);
	}
	
	
//	public static final boolean Shell_NotifyIcon (int dwMessage, NOTIFYICONDATA lpData) {
//	    if (IsUnicode) return Shell_NotifyIconW (dwMessage, (NOTIFYICONDATAW)lpData);
//	    return Shell_NotifyIconA (dwMessage, (NOTIFYICONDATAA)lpData);
//	}
//	
	
	public void setHideWindow(boolean hide) {
		if(hide)	{	
			glfwSetWindowAttrib(windowID, GLFW_VISIBLE, GLFW_FALSE);
			glfwHideWindow(windowID);			
		} else  {
			glfwSetWindowAttrib(windowID, GLFW_VISIBLE, GLFW_TRUE);
			glfwShowWindow(windowID);
		}
		update();
	}
	
	public void setWindowSize(int width, int height) {
		glfwSetWindowSize(windowID, width, height);
		bounds.set(width, height);
		update();
	}
	
	
	
	public void setDecorated(boolean b) {
		if(b) {
			glfwSetWindowAttrib(windowID, GLFW_DECORATED, GLFW_TRUE);
			update();
		} else {
			glfwSetWindowAttrib(windowID, GLFW_DECORATED, GLFW_FALSE);
			update();
		}
			
			
	}
	

	public void setFullscreen(boolean isFullscreen) {		
		isResized = true;
		if(isFullscreen) {

			setSize(new Vector2(KLIB.graphic.MonitorWidth(), KLIB.graphic.MonitorHeight()));
			
			glfwSetWindowMonitor(windowID, glfwGetPrimaryMonitor(), 0, 0,(int) bounds.getX(),(int) bounds.getY(), 0);
		} else {
			if(options.is_fullscreen) {
				setSize(new Vector2(1920, 1080));
				glfwSetWindowMonitor(windowID, 0, (int) position.getX(), (int) position.getY(), (int) bounds.getX(),(int) bounds.getY(), 0);
				glfwSetWindowPos(windowID, (int) ((KLIB.graphic.MonitorWidth() - bounds.getWidth()) * 0.5f),(int) ((KLIB.graphic.MonitorHeight() - bounds.getHeight()) * 0.5f));
			}			
		}	
		options.is_fullscreen = isFullscreen;
	}
	
	public void setFullscreen(boolean isFullscreen, Vector2 targetBounds) {		
		isResized = true;
		if(isFullscreen) {
			setSize(new Vector2(KLIB.graphic.MonitorWidth(), KLIB.graphic.MonitorHeight()));
			glfwSetWindowMonitor(windowID, glfwGetPrimaryMonitor(), 0, 0,(int) bounds.getX(),(int) bounds.getY(), 0);
		} else {
			setSize(targetBounds);
			glfwSetWindowMonitor(windowID, 0, (int) position.getX(), (int) position.getY(), (int) bounds.getX(),(int) bounds.getY(), 0);
			glfwSetWindowPos(windowID, (int) ((KLIB.graphic.MonitorWidth() - bounds.getWidth()) * 0.5f),(int) ((KLIB.graphic.MonitorHeight() - bounds.getHeight()) * 0.5f));		
		}	
		options.is_fullscreen = isFullscreen;
	}
		
	public void mouseState(boolean lock) {
		glfwSetInputMode(windowID, GLFW_CURSOR, lock ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
		Input.mouseLocked = lock;
	}
	
//	public void lockMouse() {
//		
//	}
//	
//	public void unlockMouse() {
//		
//	}
	
	public static void Terminate() {
		GSM.destroy();
		
		for (int i = 0; i < windows.size(); i++) {
			windows.get(i).destroy();
		}
		GLFW.glfwTerminate();		
		System.exit(0);
	}

	public void destroy() {
		
		if(input != null) input.destroy();
		sizeCallback.free();
		positionCallback.free();
		glfwWindowShouldClose(windowID);
		GLFW.glfwDestroyWindow(windowID);
//		GLFW.glfwTerminate();
		
		if(tray != null)
			tray.remove(trayIcon);
	}
	
	private void createCallbacks() {
		sizeCallback = new GLFWWindowSizeCallback() {
			
			@Override
			public void invoke(long window, int w, int h) {
					bounds.set(w, h);
					isResized = true;
			}
		};
		
		positionCallback = new GLFWWindowPosCallback() {
			public void invoke(long window, int x, int y) {
				if(!options.is_fullscreen) position.set(x, y);				
			}
		};
		
		closeCallback = new GLFWWindowCloseCallback() {
			
			@Override
			public void invoke(long arg0) {
				glfwSetWindowShouldClose(windowID, true);
			}
		};

		
		glfwSetWindowCloseCallback(windowID, closeCallback);
		glfwSetCharCallback(windowID, input.getCharCallback());
		glfwSetKeyCallback(windowID, input.getKeyboardCallback());
		
		glfwSetCursorPosCallback(windowID, input.getMousePositionCallback());
		glfwSetMouseButtonCallback(windowID, input.getMouseButtonCallback());
		glfwSetWindowSizeCallback(windowID, sizeCallback);
		glfwSetWindowPosCallback(windowID, positionCallback);
		glfwSetScrollCallback(windowID, input.getMouseScrollCallback());
	}

	private long variableYieldTime, lastTime;	 
	/**
	 * An accurate sync method that adapts automatically
	 * to the system it runs on to provide reliable results.
	 *
	 * @param fps The desired frame rate, in frames per second
	 * @author kappa (On the LWJGL Forums)
	 */
	private void syncFrameRate(int fps) {
	    if (fps <= 0) return;
	     
	    long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
	    // yieldTime + remainder micro & nano seconds if smaller than sleepTime
	    long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
	    long overSleep = 0; // time the sync goes over by
	     
	    try {
	        while (true) {
	            long t = System.nanoTime() - lastTime;
	             
	            if (t < sleepTime - yieldTime) {
	                Thread.sleep(1);
	            }else if (t < sleepTime) {
	                // burn the last few CPU cycles to ensure accuracy
	                Thread.yield();
	            }else {
	                overSleep = t - sleepTime;
	                break; // exit while loop
	            }
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }finally{
	        lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);
	       
	        // auto tune the time sync should yield
	        if (overSleep > variableYieldTime) {
	            // increase by 200 microseconds (1/5 a ms)
	            variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
	        }
	        else if (overSleep < variableYieldTime - 200*1000) {
	            // decrease by 2 microseconds
	            variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
	        }
	    }
	}
	
	public GLFWVidMode getVidmode() {
		return vidmode;
	}
	public void setVidmode(GLFWVidMode vidmode) {
		this.vidmode = vidmode;
	}
	
	public long getID() {
		return windowID;
	}

	public String getTitle() {
		return options.window_title;
	}

	public void setTitle(String title) {
		options.window_title = title;
	}
	public Matrix4 getProjectionMatrix() {
		return projection;		
	}
		
	public void setVersion(String string) {
		options.app_version = string;
	}
	

}

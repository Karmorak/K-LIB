package com.karmorak.lib;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.karmorak.lib.KLIB.system.SPI;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.prototype.Config;
import com.karmorak.lib.utils.GraphicUtils;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;


public class KLIB {
	
	//TODO
	// -> Scrollable Horizontal scheint die get position nicht korrekt zurückzugeben
	// -> StatusBar.setAlpha ist Buggy
	// -> ownfont von einer bitmap font konvertieren
	// [X] entgültig die letzten k-api funktionen in k-lib reinhauen
	// [ ] Button tooltips
	// [ ] ganze ordner von A nach B kopieren
	// [ ] input kann nur Vertical scrollen
	// [ ] Button.Background
	// [ ] click reaktion als wenn mann klickt das der button nur kurz die farbe ändert
	// [ ] text max width eine  option hinzufügen das die linie nach unten weitergeführt wird
	// [ ] text new line und tab (/n und /t) gängig machen
	// [X] texture filter einstellbar machen


	public static final String VERSION = "1.0.1";
	public static final int RELEASE = 30;
	public static final String DATE = "28.02.2026";
	/** 0 = off, 1 = on*/
	public static int DEBUG_LEVEL;
	private static boolean isINIT = false;

	public static final String WIN_DIR_PATH = System.getenv("WINDIR");
	public static Config TEMP_DATA; 
	private static int REQ_VERSION = RELEASE;
	
	public static int OS;
			
	public static final String[] VERSION_HISTORY = {"01", "02", "03", "0.4.0", "0.4.1", "0.4.2", "0.4.3", "0.5.0", "0.6.0", "0.6.1", "0.7.0", "0.7.1", "0.7.2", "0.7.3", "0.7.4",
													"0.7.5", "0.7.6", "0.8.0", "0.8.0.1", "0.8.0.2", "0.8.0.3", "0.8.1", "0.8.2", "pre1 0.9.0", "pre2 0.9.0", "pre3 0.9.0", "0.9.0", "0.9.1", "0.9.2",
													"1.0.0"};
	
	public static String APP_NAME;
	public static String APP_VERSION;

	public static void START(Running run, String title) {
		START(run, new Window(title), REQ_VERSION);
	}
	public static void START(Running run, Window window) {
		START(run, window, REQ_VERSION);
	}
	public static void START(Running run, Window window, int req_lib_version) {
		INIT(req_lib_version, "", WindowOptions.app_version);
		run.start(window);
	}
	
	public static void START(Running run, int req_lib_version) {
		START(run, (Window) null, req_lib_version);
	}
	
	public static void START(Running run, WindowOptions options) {
		START(run, options, REQ_VERSION);
	}
	
	public static void START(Running run, WindowOptions options, int req_lib_version) {
		if(options != null) {			
			INIT(req_lib_version, WindowOptions.app_title, WindowOptions.app_version);
			run.start(new Window(options));	
		} else {
			INIT(req_lib_version, "LWJGL App", "");
			run.start("Thread");
		}
	}
	
	public static void OPEN_NEW_THREAD() {
		
	}
	
	public static void OPEN_WINDOW(boolean asnewthread, Running run, WindowOptions options) {
		if(!asnewthread) {		
			run.open_newWindow(options);			
		} else {
			INIT(REQ_VERSION, "LWJGL App", "");
			run.start(new Window(options));				
		}
	}

	@Deprecated
	private static void INIT(int version, String app_name, String app_version) {
		if(!isINIT) {
			System.out.print("Java:" + System.getProperty("java.version")+ " | ");		
			if(app_version != null) {
				System.out.print("KLIB:" + getVersionString() + " | ");	
				System.out.print("LWJGL:" + org.lwjgl.Version.getVersion() + " | ");
				System.out.println(app_name + ":" +app_version);	
			} else {
				System.out.println("KLIB:" + getVersionString());	
			}
			
			APP_NAME = app_name;
			APP_VERSION = app_version;
			
			//LogInfo
			System.out.print("Encoding: " + Charset.defaultCharset().displayName() + " | " + System.getProperty("sun.jnu.encoding") + " | ");
			System.out.println("OS: " + System.getProperty("os.name") + " v."+ System.getProperty("os.version") + " " + System.getProperty("os.arch"));
			System.out.println("Location: " + System.getProperty("user.dir") + " | " + "Save-Dir: " + System.getProperty("user.home"));
//			System.out.println("Language: "+System.getProperty("user.language"));

			if(System.getProperty("os.version").contains("android")) {
				OS = 1;
			}  else {
				OS = 0;
			}
			
			int lvl = 0;			
			String debuglvl = System.getProperty("libdebuglevel");
			if(debuglvl != null) {
				try { 
					lvl = Integer.parseInt(debuglvl);
				} catch (NumberFormatException _) {}
			}
			DEBUG_LEVEL = lvl;			
			
//			KLIB.class.getProtectionDomain().getCodeSource().getLocation();
			System.out.println("Save-Location: " + getLocation(KLIB.class)); //when in eclipse file:/E:/Eclipse/workspace/lwjgl-lib/bin/ when compiled file:/C:/Users/marce/Desktop/test.jar
			
			//return new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().toURI());

//			public static final String temp_path = 		USER_PATH + "/AppData/Local/Temp/epicsoundboard/";
//			System.setProperty("java.io.tmpdir", temp_path);
//			System.out.println(" | Temp-Pat: " + temp_path.replace("//","\\"));			

//			System.out.println(System.getProperty("java.class.path").toString());
//			System.out.println(System.getProperty("java.vendor").toString());
//			System.out.println(System.getProperty("java.vendor.url").toString());
//			System.out.println(System.getProperty("line.separator"));
//			System.out.println(System.getProperty("path.separator").toString());
//			System.out.println(System.getProperty("user.name").toString());
			
			//Alle Properties
//			Properties properties = System.getProperties();
//			properties.list(System.out);
			
			requireVersion(version);
			
			TEMP_DATA = new Config(lib.LIB_TEMP_PATH() + "tmp.txt");
			
			isINIT = true;
		}
	}
	
	

	
	
	/**
	 * Gets the base location of the given class.
	 * <p>
	 * If the class is directly on the file system (e.g.,
	 * "/path/to/my/package/MyClass.class") then it will return the base directory
	 * (e.g., "file:/path/to").
	 * </p>
	 * <p>
	 * If the class is within a JAR file (e.g.,
	 * "/path/to/my-jar.jar!/my/package/MyClass.class") then it will return the
	 * path to the JAR (e.g., "file:/path/to/my-jar.jar").
	 * </p>
	 *
	 * @param c The class whose location is desired.
     */
	public static URL getLocation(final Class<?> c) {
	    if (c == null) return null; // could not load the class

	    // try the easy way first
	    try {
	        final URL codeSourceLocation =
	            c.getProtectionDomain().getCodeSource().getLocation();
	        if (codeSourceLocation != null) return codeSourceLocation;
	    }
	    catch (final SecurityException e) {
	        // NB: Cannot access protection domain.
	    }
	    catch (final NullPointerException e) {
	        // NB: Protection domain or code source is null.
	    }

	    // NB: The easy way failed, so we try the hard way. We ask for the class
	    // itself as a resource, then strip the class's path from the URL string,
	    // leaving the base path.

	    // get the class's raw resource path
	    final URL classResource = c.getResource(c.getSimpleName() + ".class");
	    if (classResource == null) return null; // cannot find class resource

	    final String url = classResource.toString();
	    final String suffix = c.getCanonicalName().replace('.', '/') + ".class";
	    if (!url.endsWith(suffix)) return null; // weird URL

	    // strip the class's path from the URL string
        String path = url.substring(0, url.length() - suffix.length());

	    // remove the "jar:" prefix and "!/" suffix, if present
	    if (path.startsWith("jar:")) path = path.substring(4, path.length() - 2);

	    try {
	        return Paths.get(path).toUri().toURL();
	    }
	    catch (final MalformedURLException e) {
	        e.printStackTrace();
	        return null;
	    }
	} 
	
	public static class system {
		
		
	    public interface SPI extends StdCallLibrary {

	        long SPI_SETDESKWALLPAPER = 20;
	        long SPIF_UPDATEINIFILE = 0x01;
	        long SPIF_SENDWININICHANGE = 0x02;
	       
	        
	        @SuppressWarnings({"deprecation" })
	        SPI INSTANCE = (SPI) Native.loadLibrary("user32", SPI.class,
	                new HashMap<String, Object>() {
	                    {
	                        put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
	                        put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
	                    }
	                });
	        
	        
	        boolean SystemParametersInfo(UINT_PTR uiAction, UINT_PTR uiParam, String pvParam, UINT_PTR fWinIni);
	    }
	    
	    
	    public static String Time() {
	    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
	    	LocalDateTime now = LocalDateTime.now();  
	    		    	
	    	return dtf.format(now);
	    }
	    
	    public static String Date() {
	    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");  
	    	LocalDateTime now = LocalDateTime.now();  
	    	return dtf.format(now);
	    }
	    
	    public static String getDayName() {
	    	LocalDateTime now = LocalDateTime.now();  	    	
			return now.getDayOfWeek().name();	    	
	    }
	    
	    public static String getMonthName() {
	    	LocalDateTime now = LocalDateTime.now();  	    	
			return now.getMonth().name();	    	
	    }
	    
	    
	    public static String getDayName(int i) {
            return switch (i) {
                case 1 -> "Monday";
                case 2 -> "Tuesday";
                case 3 -> "Wednesday";
                case 4 -> "Thursday";
                case 5 -> "Friday";
                case 6 -> "Saturday";
                case 7 -> "Sunday";
                default -> "";
            };
	    }
	    
	    public static String getMonthName(int i) {
            return switch (i) {
                case 1 -> "January";
                case 2 -> "February";
                case 3 -> "March";
                case 4 -> "April";
                case 5 -> "May";
                case 6 -> "June";
                case 7 -> "July";
                case 8 -> "August";
                case 9 -> "September";
                case 10 -> "October";
                case 11 -> "November";
                case 12 -> "December";
                default -> "";
            };
	    }
	    
	}

	public static class lib {

		public static final String ASSET_PATH = "/com/karmorak/lib/assets/";

		public static String LIB_TEMP_PATH() {
			return io.DocumentsPath() +  "klib\\";
		}
	}
	
	public static class io {
		
		/** 
		 * ("user.home")+"//Saved Games//";
		 * */
		public static String SaveGamePath() {
            return System.getProperty("user.home") + "\\Saved Games\\";
		}

		public static String UserPath() {
			return System.getProperty("user.home");
		}

		public static String DocumentsPath() {
			return System.getProperty("user.home") + "\\Documents\\";
		}

		public static String DownloadsPath() {
			return System.getProperty("user.home") + "\\Downloads\\";
		}
		public static String TempPath() {
            return System.getProperty("java.io.tmpdir");
		}

		public static String GameTempPath() {
			String sub = APP_NAME == null ? "" : APP_NAME.toLowerCase().replace(" ", "")+"\\";
			return lib.LIB_TEMP_PATH() + sub;
		}


		
		public static String getClipboardString() {
			String text = "";
			
			Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable transferData = systemClipboard.getContents(null);
			try {
				if(transferData != null && transferData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					return (String) systemClipboard.getData(DataFlavor.stringFlavor);
				}
			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
			return text;
		}

		public static void copyToClipboard(String text) {
			StringSelection selection = new StringSelection(text);

			Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

			systemClipboard.setContents(selection, null);

		}
		
		
	    public static void setWallpaper(String file_path) {
	        SPI.INSTANCE.SystemParametersInfo(new UINT_PTR(SPI.SPI_SETDESKWALLPAPER), new UINT_PTR(0), file_path, new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE));
	    }
	}
	
	
	public static class graphic {		

		public static float Width() {
			return Window.windows.getFirst().getBounds().getWidth();
		}
		
		public static float Height() {
			return Window.windows.getFirst().getBounds().getHeight();
		}
		
		public static float MonitorWidth() {
			return Window.windows.getFirst().getVidmode().width();
		}
		
		public static float MonitorHeight() {
			return Window.windows.getFirst().getVidmode().height();
		}
		
		public static float getTime() {
			return ((float) System.nanoTime() / 1E9f);
		}
		
		public static double getDeltaTime() {
			return Window.getDelta();
		}
				
		public static DrawMap takeScreenshot() {
			return GraphicUtils.takeScreenshot();
		}		
	}
	
	public static void enableGlobalInput() {
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
		}
		GlobalScreen.addNativeKeyListener(new GlobalInput());
	}
	
	public static void updateButtons(boolean do_update) {
		Input.updateButtons(do_update);
	}

	public static String getVersionString() {	
		return "v." + VERSION + " r." +RELEASE;	
	}
	
	public static void requireVersion(int release) {
		REQ_VERSION = release;
	}
	
	public static boolean checkVersion() {
		if(REQ_VERSION != RELEASE) {
			System.err.println("Lib could be incompatible! App requires v." + VERSION_HISTORY[REQ_VERSION-1] + " r." +REQ_VERSION + " . Current: " + getVersionString());
			return false;
		}
		return true;
	}
	

    public static class WindowOptions {
    	
    	public boolean v_sync = true;
    	public boolean is_fullscreen = false;
    	public boolean is_hidden = false;
    	public boolean is_decorated = true;
    	public boolean use_alpha_background = false;
    	public boolean is_resizeable = false;
    	public boolean is_iconified = false;
    	public boolean notification_area_icon = false;
    	
    	
    	public int target_fps = 60;
    	public int samples = 4;    	
    	public int width = 1280;
    	public int height = 720;
    	
    	public long monitor = -1;  	
    	
    	public static String app_title = "LWJGL-APP";
    	public static String app_version = "";
    	public String window_title = "LWJGL-APP"; 	
    	public String icon_path = null;
    	
    	
    }
    
	


	public static URL URL(@SuppressWarnings("rawtypes") Class c, String path) {
		return c.getClassLoader().getSystemResource(path);
	}

	public static URL URL(String path) {		
		return KLIB.class.getResource(path);
	}
	
	public static java.net.URI URI(String path) {
		
		try {
			return URL(path).toURI();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	


	
}

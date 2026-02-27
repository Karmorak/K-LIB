package com.karmorak.lib.utils.file;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.karmorak.lib.KLIB;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import shaders.shader_src.Loader;

public class FileUtils {

	public static String[] selectFiles() {
		// Filter definieren (optional)
		// Erster String: Beschreibung, weitere Strings: Endungen
		PointerBuffer filters = org.lwjgl.system.MemoryUtil.memAllocPointer(2);
		filters.put(org.lwjgl.system.MemoryUtil.memUTF8("*.txt"));
		filters.put(org.lwjgl.system.MemoryUtil.memUTF8("*.png"));
		filters.flip();

		// Der Aufruf für den File-Dialog
		// Parameter: Titel, Startpfad, Filter, Filterbeschreibung, Mehrfachauswahl erlaubt?
		String result = TinyFileDialogs.tinyfd_openFileDialog(
				"Wähle Dateien aus",
				"C:\\",
				filters,
				"Text oder Bilder",
				true // true = Mehrfachauswahl aktiviert
		);

		// Speicher wieder freigeben
		org.lwjgl.system.MemoryUtil.memFree(filters);

		if (result != null) {
			// TinyFileDialogs gibt bei Mehrfachauswahl einen String zurück,
			// der die Pfade mit einem vertikalen Strich '|' trennt.
			return result.split("\\|");
		}

		return null; // Nutzer hat abgebrochen
	}


	public static String loadShader(String path) {
		StringBuilder result = new StringBuilder();
		
		
		try {
			InputStream in = Loader.class.getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return result.toString();
	}
	
	public static String loadShader(URL path) {
		StringBuilder result = new StringBuilder();
		
		
		try {
			InputStream in = path.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return result.toString();
	}
	
	
	public static ArrayList<String> readFiletoArray(File f) {
		
		
		
		ArrayList<String> lines = new ArrayList<String>();
		
		String line = "";
		
		if(checkFile(f, true)) {				
			try {
				FileReader rd = new FileReader(f,  StandardCharsets.UTF_8);
				int i = rd.read();						
				do {				
					char c = (char) i;					
					
					if(c == '\n') {
						c = (char) -1;	
						line = line.substring(0, line.length());
						
						lines.add(""+line);						
						line = "";
					} else {
						line += c;
					}
					i = rd.read();
				} while(i != -1);
				
				rd.close();				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		return lines;
	}
	
    /**
     * Converts an image to another format
     *
     * @param inputImagePath Path of the source image
     * @param outputImagePath Path of the destination image
     * @param formatName the format to be converted to, one of: jpeg, png,
     * bmp, wbmp, and gif
     * @return true if successful, false otherwise
     * @throws IOException if errors occur during writing
     */
    public static boolean convertFormat(String inputImagePath, String outputImagePath, String formatName) throws IOException {
    	if(inputImagePath.contains(".png")) {
    		
    		File input = new File(inputImagePath);
    		File output = new File(outputImagePath);

    	            BufferedImage image = ImageIO.read(input);
    	            BufferedImage result = new BufferedImage(
    	                    image.getWidth(),
    	                    image.getHeight(),
    	                    BufferedImage.TYPE_INT_RGB);
    	            result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
    	          return ImageIO.write(result, formatName, output);
    		
    	} else {
    	
    	
	        FileInputStream inputStream = new FileInputStream(inputImagePath);
	        FileOutputStream outputStream = new FileOutputStream(outputImagePath);
	         
	        // reads input image from file
	        BufferedImage inputImage = ImageIO.read(inputStream);
	         
	        // writes to the output image in specified format
	        boolean result = ImageIO.write(inputImage, formatName, outputStream);
	         
	        // needs to close the streams
	        outputStream.close();
	        inputStream.close();
	         
	        return result;
    	}
    }
	
	public static boolean moveFile(String source, String destination, boolean replace) throws IOException {
		
		File original = new File(source);
		boolean exists = checkFile(original, false);
		if(!exists) {
			if(KLIB.DEBUG_LEVEL >= 0)
				System.err.println("File: \"" + source +"\" does not exist!");
			return false;
		}		
	    File copied = new File(destination);	
	    if(replace) {
	    	checkFile(copied);
	    } else {
	    	if(copied.exists()) {
	    		if(KLIB.DEBUG_LEVEL >= 0)
	    			System.err.println("File: \"" + destination +"\" already exists!");
	    		return false;
	    	}
	    }		
		Files.move(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);		
		return true;
	}
	public static boolean copyFile(String source, String destination, boolean replace) throws IOException {		
		
		File original = new File(source);
		boolean exists = checkFile(original, false);
		if(!exists) {
			if(KLIB.DEBUG_LEVEL >= 0)
				System.err.println("File: \"" + source +"\" does not exist!");
			return false;
		}		
	    File copied = new File(destination);	
	    if(replace) {
	    	checkFile(copied);
	    } else {
	    	if(copied.exists()) {
	    		if(KLIB.DEBUG_LEVEL >= 0)
	    			System.err.println("File: \"" + destination +"\" already exists!");
	    		return false;
	    	}
	    }
	    
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(destination);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	    return true;
	}
	
	
	

	
	/**
	 * @param f
	 * @param data_
	 * @param mode  
	 * mode 0 = replace whole file;  
	 * mode 1 = append;  
	 * mode 2 = replace line on @param line when line is <0 than its appended  
	 * @param line
	 * 
	 * @throws IOException 
	 * 
	 */	
	public static void writeToFile(File f, String[] data_, int mode, int line) throws IOException {

		ArrayList<String> lines = null;
		if(mode > 0) {
			lines = readFiletoArray(f);
			if(lines.isEmpty()) {throw new IOException();}
		} else {
			checkFile(f);
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		
		
		String content = "";
		
		if(mode == 0) {
            for (String s : data_) content = content + s + "\n";
				
		} else if (mode == 1 || (mode == 2 && line < 0)) {
            for (String s : lines) content = content + s + "\n";
            for (String s : data_) content = content + s + "\n";
			
		} else if (mode == 2) {				
			boolean found = false;
			for (int i = 0; i < lines.size(); i++) {
				if(i == line)  {
                    for (String s : data_) content = content + s + "\n";
					found = true;
				} else {
					content = content + lines.get(i) + "\n";
				}
			}	
			if(!found)
                for (String s : data_) content = content + s + "\n";
		} else {
			if(line < 0) {
                for (String s : lines) content = content + s + "\n";
                for (String s : data_) content = content + s + "\n";
				
			} else if(line >= lines.size()){
                for (String s : lines) content = content + s + "\n";
//				for (int i = 0; i < line - lines.size(); i++)
//					content = content + "\n";
                for (String s : data_) content = content + s + "\n";
				
			} else {
				for (int i = 0; i < lines.size(); i++) {
					if(i == line) {

                        for (String string : data_) content = content + string + "\n";
						
					} else 
						content = content + lines.get(i) + "\n";				
				}
			}
		}
			
		bw.write(content);	
		bw.close();		
	}
	
	/**
	 * @param f
	 * @param data
	 * @param mode  
	 * mode 0 = replace whole file;  
	 * mode 1 = append;  
	 * mode 2 = replace line on @param line when line is <0 than its appended  
	 * @param line
	 * 
	 * @throws IOException 
	 * 
	 */	
	public static void writeToFile(File f, String data, int mode, int line) throws IOException {				
		writeToFile(f, new String[] {data}, mode, line); 		
	}	

	public static void writeToFile(File f, String[] data) throws IOException {				
		writeToFile(f, data, 0, 0);			
	}

	public static void writeToFile(File f, String[] data_, int mode) throws IOException {				
		writeToFile(f, data_, mode, -1);
	}
	
	public static boolean checkFile(File f) throws IOException {
		if(!f.exists()) {
			if(KLIB.DEBUG_LEVEL > 0)
				System.out.println("File not existing creating new one in " +  f.getParent());
			
			File path = new File(f.getParent());
			path.mkdirs();
						
			return f.createNewFile();
		}
		return f.exists();
	}
	
	public static boolean checkFile(File f, boolean create) {
		if(!f.exists()) {			
			if(create) {			
				try {
					if(KLIB.DEBUG_LEVEL > 0)
						System.out.println("File not existing creating new one in " +  f.getParent());
					
					File path = new File(f.getParent());
					path.mkdirs();
					f.createNewFile();
					return true;
				} catch (IOException e) {
					if(KLIB.DEBUG_LEVEL >= 0)
						System.err.println("File: \"" + f.getAbsolutePath() +"\" cannot be created.");
					e.printStackTrace();
					return false;
				}
			}
			
		} else {
			return true;
		}
		return false;
	}
	
	
	/** returns true if the file exists or got created */
	public static boolean checkFile(String path) {
		File f = new File(path);
		try {
			return checkFile(f);
		} catch (IOException e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** returns true if the file exists or got created */
	public static boolean checkFile(String path, boolean create) {
		File f = new File(path);
		
		if(!f.exists()) {			
			if(create) {			
				try {
					if(KLIB.DEBUG_LEVEL > 0)
						System.out.println("File not existing creating new one in " +  f.getParent());
					
					File parent_path = new File(f.getParent());
					parent_path.mkdirs();
					
					String[] paths = path.split("//");
										
					if(paths[paths.length-1].contains("."))					
						f.createNewFile();
					else f.mkdirs();
					
					
					return true;
				} catch (IOException e) {
					if(KLIB.DEBUG_LEVEL >= 0)
						System.err.println("File: \"" + f.getAbsolutePath() +"\" cannot be created.");
					e.printStackTrace();
					return false;
				}
			}
			
		} else {
			return true;
		}
		return false;
	}

	
}

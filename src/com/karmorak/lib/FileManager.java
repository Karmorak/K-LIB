package com.karmorak.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {
	
	
	
//	static String android_path = "C://Users//" + System.getProperty( "user.name" ) + "//" +Main.PC_PATH;
//	static String desktop_path = "C://Users//" + System.getProperty( "user.name" ) + "//" +Main.PC_PATH;
	
	
	public static ArrayList<String> list = new ArrayList<>();
	
	private static File checkFile(String root) {
		if(root != null && !root.isEmpty()) {
			try {
				File file = new File(root);
				if(!file.exists()) {
					File file2 = new File(file.getParent());			
					file2.mkdirs();
					file.createNewFile();
				}
				return file;
			} catch (IOException e) {
				System.out.println("Error on creating new File");
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("Failed to load file.");
				e.printStackTrace();
			}		
		}
		return null;
	}
	
	private static ArrayList<String> readFile(File file) {
		ArrayList<String> list = new ArrayList<>();
		
			FileReader file_reader = null;
			BufferedReader br = null;
			
			String line = "";			
			
			try {
				file_reader = new FileReader(file);
				br = new BufferedReader(file_reader);

				while ((line = br.readLine()) != null) {
					list.add(line);
				}
				br.close();
			} catch (IOException e) {
				System.err.println("Desktop: Couldnt read line!");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("FileManager" + "finally:br.close");
					e.printStackTrace();
				}
			}		
		return list;
	}
	
	public static String getString(String root, String key) {	
		File file = checkFile(root);		
		
				
		ArrayList<String> list = readFile(file);
		

			if(!list.isEmpty()) {
				for(String s : list) {
					String[] parts = s.split(" ");
					if(parts.length < 2) {
						parts = s.split(":");
					}			
					if(parts[0].equals(key) || parts[0].equals(key +":")) {
						return parts[1];
					}
				}			
			}		
		return null;	
	}
	
	public static int getInt(String root, String key) {
		String s = getString(root, key);
		if(s != null && !s.isEmpty()) {
			try {
				int i = Integer.parseInt(s);
				return i;
			} catch (NumberFormatException e) {
				System.err.println("Failed to convert from String to Integer.");
			}
		}
		
		return -1;
		
	}
	public static void setString(String root, String key, String value) {
		File file = checkFile(root);
		ArrayList<String> list = readFile(file);

			FileWriter writer = null;			
			boolean found = false;
					
			try {
				writer = new FileWriter(file);
				if(!list.isEmpty()) {
					for(String s : list) {
						if(s.split(" ")[0].equals(key) || s.split(" ")[0].equals(key + ":")) {
							String part1 = s.split("\t")[0];
							writer.write(part1 + "\t" + value + "\n");
							found = true;
						} else {
							writer.write(s + "\n");
							writer.flush();	
						}
						writer.flush();
					}
				}
				
				if(!found) {
					writer.write(key + ": " + value + "\n");
					writer.flush();	
				}
				
				if(writer != null) {
					writer.close();
				}				
			} catch (IOException e) {
				System.err.println("Desktop: failed to write!");
				e.printStackTrace();
			}	
	}
	
	public static void write(String line, String path) {
		File file = new File(path);
		FileWriter writer = null;
		
		if(!file.exists()) {
			File file2 = new File(file.getParent());			
			file2.mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error on creating new File");
				e.printStackTrace();
			}
			
		}		
		try {
			writer = new FileWriter(file);
			writer.write(line);		
			writer.flush();
			if(writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

}

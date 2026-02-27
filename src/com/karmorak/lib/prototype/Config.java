package com.karmorak.lib.prototype;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.karmorak.lib.utils.file.FileUtils;

public class Config {

	//TODO config could use the caching instead of ever time rescan the file, implement as opional parameter
	
	private final String PATH;
	private final File FILE;
	
	private ArrayList<String> file_contents;

	public Config(String path) {
		
		PATH = path;
		FILE = new File(path);
		
		FileUtils.checkFile(FILE, true);
	}
	
	public ArrayList<String> readConfig() {
		return file_contents = FileUtils.readFiletoArray(new File(PATH));
	}
	
	
	public ArrayList<String> getKeys() {
		readConfig();
		
		ArrayList<String> keys = new ArrayList<String>();
		
		for (int i = 0; i < file_contents.size(); i++) {			
			String content = file_contents.get(i).split(":")[0];		
			keys.add(content);
		}	
		
		return keys;
	}
	
	//**return if theres is already a value set
	public boolean isValue(String name) {
		readConfig();
		for (int i = 0; i < file_contents.size(); i++) {			
			if(file_contents.get(i).startsWith(name)) {				
				String content = file_contents.get(i).replaceFirst(name + ": ", "");
				if(content == null || content.equals(""))
					return false;
				return true;				
			}
		}
		return false;
	}
	
	public void setValue(String name, String value) {
		
		ArrayList<String> list = readConfig();		
		
		int found = -1;
		
		for (int i = 0; i < list.size(); i++) {			
			if(list.get(i).startsWith(name)) {
				found = i;
				break;
			}		
		}
		
		
		try {
			FileUtils.writeToFile(FILE, name + ": " + value, 2, found);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	
	}
	
	public String getValue(String name) {
			
		readConfig();
		
		for (int i = 0; i < file_contents.size(); i++) {			
			if(file_contents.get(i).startsWith(name)) {
				return file_contents.get(i).replaceFirst(name + ": ", "");
			}		
		}
		return null;
	}
	
	public String getValue(String name, String def_value) {
			
		readConfig();
		
		for (int i = 0; i < file_contents.size(); i++) {			
			if(file_contents.get(i).startsWith(name)) {
				return file_contents.get(i).replaceFirst(name + ": ", "");
			}		
		}
		setValue(name, def_value);		
		return def_value;
	}
	
	
	
	public boolean getValue_asBoolean(String name) {
		String c = getValue(name);
		if(c.equals("0"))
			return false;
		if(c.equals("1"))
			return true;				
		return Boolean.parseBoolean(c);
	}
	public boolean getValue_asBoolean(String name, boolean def_value) {		
		String c = getValue(name,""+  def_value);
		if(c.equals("0"))
			return false;
		if(c.equals("1"))
			return true;				
		return Boolean.parseBoolean(c);
	}	
	
	public int getValue_asInteger(String name) {
		return Integer.parseInt(getValue(name));
	}
	public int getValue_asInteger(String name, int def_value) {
		return Integer.parseInt(getValue(name, "" + def_value));
	}	
	
	public Double getValue_asDouble(String name) {
		return Double.parseDouble(getValue(name));
	}
	
	public double getValue_asDouble(String name, double def_value) {
		return Double.parseDouble(getValue(name, "" + def_value));
	}	
	
	public float getValue_asFloat(String name) {
		return Float.parseFloat(getValue(name));
	}
	
	public float getValue_asFloat(String name, float def_value) {
		return Float.parseFloat(getValue(name, "" + def_value));
	}	
	
	public String[] getValue_asStringArray(String key) {
		String value = getValue(key).replaceFirst("[", "");
		value = value.substring(0, value.length()-2);	
		String[] list = value.split(", ");
		
		return list;
	}
	
	public String[] getValue_asStringArray(String key, String[] def_value) {
		
		
		
		String value = getValue(key,"[" + String.join(",", def_value)+"];");
		value = value.substring(1, value.length()-2);	
		System.out.println(value);
		String[] list = value.split(", ");
		
		return list;
	}
	
}

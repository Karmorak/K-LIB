package com.karmorak.lib.utils.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class JSON {

	public static class jsonObject {
		
		public ArrayList<String> result;
		public int array_pos;		
		
		public jsonObject() {
			result = new ArrayList<String>();
		}
		
		public jsonObject(int array_pos, String value) {
			result = new ArrayList<String>();
			result.add(value);
			this.array_pos = array_pos;
		}
		
		public String[] resultasArray() {
			String[] out = new String[result.size()];
			for (int i = 0; i < result.size(); i++) {
				out[i] = result.get(i);
			}
			return out;
		}
		
		public String getResult_asString() {
			String out = result.get(0);
			for (int i = 1; i < result.size(); i++) {
				out = out + ", " + result.get(i);
			}
			return out;
		}
		
	}
	
	public static class JSONFile {
			
		
		String file_path;
		HashMap<String, ArrayList<jsonObject>> data = new HashMap<String, ArrayList<jsonObject>>();	
		
		
		@Override
		public int hashCode() {
			return Objects.hash(data, file_path);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			JSONFile other = (JSONFile) obj;
			return Objects.equals(data, other.data) && Objects.equals(file_path, other.file_path);
		}
	}
	
	
	private static HashMap<String, JSONFile> files = new HashMap<String, JSON.JSONFile>();

	
	
	@Deprecated
	public static jsonObject[] get3(String file_path, String destination) {
		
		
		HashMap<String, ArrayList<jsonObject>> locations;
		
		if(!files.containsKey(file_path + ".json")) {
			File f = new File(file_path + (".json"));			
			locations = new HashMap<String, ArrayList<jsonObject>>();
		
			ArrayList<String> curlocation = new ArrayList<>();
			String line = "";			
		
			boolean brace_open = false;
			boolean brace_close = false;
			boolean square_bracket_open = false;
			boolean square_bracket_close = false;
			boolean double_dot = false;
			
			int sqaure_open = 0;
			int sqaure_close = 0;
			
			try {
				FileReader rd = new FileReader(f,  StandardCharsets.UTF_8);
				int i = rd.read();		
				
				do {				
					char c = (char) i;
					line += c;
					
					if(c == '{') {
						brace_open = true;
					} else if(c == '}') {
						brace_close = true;
					} else if(c == '[') {
						square_bracket_open = true;
					} else if(c == ']') {
						square_bracket_close = true;
					} else if(c == ':') {
						double_dot = true;
					} else if(c == '\n') {		
						
						if(brace_open) {						
							if(double_dot) {																				 // brace open + double dot = eg. naughties : { 		(but only start of playlist)
								String loc = line.split(" : ")[0].replace("\"", "").replace("\t", "").replace(" ", "");
								if(!brace_close) curlocation.add(loc); 														 //wenn da ein brace closed wäre würde es heisen das ist eine leere liste eg. naughties : { },
									
								if(square_bracket_open) sqaure_open++;
									
							} else {
								//should be first brace so do nothing cause its the whole file
							}	
						} else if (brace_close) {	
							if(brace_open) {			
								// }, { is for seperating elements in an Array so isArray should be true
							} else {
								curlocation.remove(curlocation.size()-1);
//								if(square_bracket_close) sqaure_close++;
							}						
												
						} else if(double_dot) { // e.g.  "sex" : never,
							String loc = line.split(" : ", 2)[0].replace("\"", "").replace("\t", "").replace(" ", "").replace(",", "");
							String value = line.split(" : ", 2)[1];
							
							if(value.contains("\""))						
							value = value.substring(1, value.length()-3);
							
							
							if(value.contains("\""))
								value = value.replace("\\", "");
												
								//add
								String p = ""; 
								for (int cl = 0; cl < curlocation.size(); cl++) {
									p += curlocation.get(cl) + ".";
								}							
								p += loc + ".";
								
								if(locations.containsKey(p)) {		
									ArrayList<jsonObject> objects = locations.get(p);
									
									
									
									jsonObject al = objects.get(objects.size()-1);
									if(al.array_pos == sqaure_open) {
										al.result.add(value);									
									} else {
										objects.add(new jsonObject(sqaure_open, value));								
									}
									
									
									
									
									locations.put(p, objects);
								} else {
									jsonObject obj = new jsonObject();
									obj.result.add(value);
									obj.array_pos = sqaure_open;
												
									ArrayList<jsonObject> objects = new ArrayList<JSON.jsonObject>();
									objects.add(obj);
									
									locations.put(p, objects);
								}
							
						}
						
						
						
						
						
						
						brace_open = false;
						brace_close = false;
						square_bracket_open = false;
						square_bracket_close = false;
						double_dot = false;
						line = "";
					}
					
					i = rd.read();
				} while(i != -1);
				
				rd.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
			
			//get the content of the data
	//		for (String key : locations.keySet()) {
	//			System.out.println(key + ":");
	//			for(int y = 0; y < locations.get(key).size(); y ++) {
	//				System.out.print(locations.get(key).get(y));				
	//			}	
	//		}
			
			JSONFile jf = new JSONFile();
			jf.file_path = file_path;
			jf.data = locations;
			files.put(file_path + ".json", jf);
		} else {
			locations = files.get(file_path + ".json").data;
		}
		
		
		//Get The Final Results
		jsonObject[] results = new jsonObject[locations.get(destination).size()];
		
		for (int j = 0; j < locations.get(destination).size(); j++) {
			results[j] = locations.get(destination).get(j);
		}		
		
		
		
		return results;
		
	}	
	
	private static String Location_toString (ArrayList<String> location_array){
		String s = "";
		for (int i = 0; i < location_array.size(); i++) {
			
			if(i < location_array.size()-1) {
				s += location_array.get(i) +".";
			} else {
				s += location_array.get(i);
			}
		}
		
		return s;
	}
	
	
}

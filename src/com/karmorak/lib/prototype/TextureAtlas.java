package com.karmorak.lib.prototype;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.karmorak.lib.Color;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.math.Vector4;
import com.karmorak.lib.utils.file.FileUtils;

public class TextureAtlas {
	
	
	private final String path;
	
	
	private final HashMap<String, Texture> textures;

	
	//spotifyid:x:y:width_height:
	
	public TextureAtlas(String dir, String fileName) {
		this.path = dir;
		
		File dir_file = new File(dir);
		if(!dir_file.exists()) dir_file.mkdirs();
		
		File image_file = new File(dir + fileName + ".png");
		File data = new File(dir + fileName + ".txt");
		
		textures = new HashMap<String, Texture>();
				
		if(data.exists() && image_file.exists()) {
			DrawMap image = null;
			try {
				image = new DrawMap((image_file.toURI().toURL()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			ArrayList<String> lines = FileUtils.readFiletoArray(data);
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				
				String[] region = line.split(":");
				if(region.length >= 5) {
					
					String name = region[0];
					int x = Integer.parseInt(region[1]);
					int y = Integer.parseInt(region[2]);
					int width = Integer.parseInt(region[3]);
					int height = Integer.parseInt(region[4]);	
					
					Vector4 vec4 = new Vector4(x, y, width, height);
					
					Texture reg = new Texture(image, vec4);
					reg.create();
					
					textures.put(name, reg);
				}
			}			
		} else {
			try {
				image_file.createNewFile();
				data.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
//		try {			
//			texture = new Texture((f.toURI().toURL()));
//			texture.create();
//		} catch (MalformedURLException e) {
//		
//			e.printStackTrace();
//			throw new NullPointerException();
//		}		
	}
	
	
	public Texture getTexture(String name) {
		return textures.get(name);
	}
	
	

	

	
	
	
//	public Texture createTexture(String name) {
//		return null;		
//	}
//	
//	public Texture createTexture(String name, int index) {
//		return null;		
//	}
	
	
	public static URL URL(String path) {
		return TextureAtlas.class.getResource(path);
	}
	

}

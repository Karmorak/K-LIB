package com.karmorak.lib.prototype;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.TreeSet;

import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.utils.GraphicUtils;
import com.karmorak.lib.utils.file.FileUtils;

public class AtlasPacker {
	
	
	private final String name;
	private final String path;	
	private final File data;
	
	
	private TreeSet<Pack> maps = new TreeSet<>();
	
	
	private static class Pack implements Comparable<Pack> {
		
		final DrawMap m;
		final String name;
		
		public Pack(String name, DrawMap map) {
			this.m = map;
			this.name = name;
		}		
		
		
		
		@Override
		public int hashCode() {
			return Objects.hash(m, name);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pack other = (Pack) obj;
			return Objects.equals(m, other.m) && Objects.equals(name, other.name);
		}



		@Override		
		public int compareTo(Pack o) {
			if(o.m.getSourceHeight() - this.m.getSourceHeight() == 0) {
				return 1;
			} else			
			return (int) (o.m.getSourceHeight() - this.m.getSourceHeight());
		}	
		
		
	}
	
	
	
	
	public AtlasPacker(String dir, String fileName) {
		this.path = dir;
		this.name = fileName;
		
		File dir_file = new File(dir);
		if(!dir_file.exists()) dir_file.mkdirs();
		
		File image_file = new File(dir + fileName + ".png");
		data = new File(dir + fileName + ".txt");
				
//		if(data.exists() && image_file.exists()) {
//			DrawMap image = null;
//			try {
//				image = new DrawMap((image_file.toURI().toURL()));
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//			
//			ArrayList<String> lines = FileUtils.readFiletoArray(data);
//			for (int i = 0; i < lines.size(); i++) {
//				String line = lines.get(i);
//				
//				String[] data = line.split(":");
//				String name = data[0];
//				int x = Integer.parseInt(data[1]);
//				int y = Integer.parseInt(data[2]);
//				int width = Integer.parseInt(data[3]);
//				int height = Integer.parseInt(data[4]);	
//				
//				Vector4 vec4 = new Vector4(x, y, width, height);
//				
//				Texture reg = new Texture(image, vec4);
//				reg.create();
//				
//				maps.put(name, image);
//			}			
//		} else {
//			try {
//				image_file.createNewFile();
//				data.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		
		
		
//		try {			
//			texture = new Texture((f.toURI().toURL()));
//			texture.create();
//		} catch (MalformedURLException e) {
//		
//			e.printStackTrace();
//			throw new NullPointerException();
//		}		
	}
	
	public void sortArtists(int year) {		
		
//		ArrayList<String> sorted = new ArrayList<>();
//			
////		allartists_sortet.clear();
//		
////			maps.entrySet().stream()
////	        .sorted((k1, k2) -> -k1.getValue().getHeight().compareTo(k2.getValue().getHeight()))
////	        .forEach(k -> sorted.add(k.getKey()));	
//		
//		
//		
//		Collections.sort(maps.key, new Comparator<CustomData>() {
//		    @Override
//		    public int compare(CustomData lhs, CustomData rhs) {
//		        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
//		        return lhs.customInt > rhs.customInt ? -1 : (lhs.customInt < rhs.customInt) ? 1 : 0;
//		    }
//		});
//		
//
//		
//		for (Iterator<String> iterator = sorted.iterator(); iterator.hasNext();) {
//			String string = (String) iterator.next();		
//							
//			if(!allartists_unsortet.containsKey(string)) {
//				Artist a = new Artist(string, Year.all_artists_score.get(string));
//				a.ui_score.showTrend(true);
//				a.ui_score.changeType(-1, ScoreType.EVERYTHING);
//				allartists_unsortet.put(string, a);
//				allartists_sortet.add(a);					
//				a.setPosition(pos_x, pos_y);
//				pos_y -= 50;
//			} else {
//				Artist a = allartists_unsortet.get(string);
//				allartists_sortet.add(a);		
//				a.setPosition(pos_x, pos_y);
//				pos_y -= 50;
//			}
//			if(stop_draw_artists == -1 && pos_y < 50) {
//				stop_draw_artists = allartists_sortet.size();
//			}		
//		}	
	}
	
	
	public void addTexture(DrawMap map, String name) {
		
		maps.add(new Pack(name, map));	
		
	}
	
	
	public void saveAtlas() {
		
		
		
		int size = maps.size();
		
		double s = Math.sqrt(size);
		
		int y_amt = (int)s +1;
		int x_amt = (int)s;
		
		int max_height = 0;
		int cur_height = 0;
		int cur_width = 0;
		int max_width = 0;
		
		int count = 0;
				
		for(Pack p : maps) {
			count++; 
			
			max_height += p.m.getSourceHeight();
			if(cur_width < p.m.getSourceWidth()) cur_width = (int) p.m.getSourceWidth();
			
			if(count == y_amt) {
				count = 0;
				if(cur_height > max_height) max_height = cur_height;
				max_width += cur_width;
			}
		}
		
		if(count != 0) {
			count = 0;
			if(cur_height > max_height) max_height = cur_height;
			max_width += cur_width;
		}
		
		System.out.println(Math.max(max_width, max_height) + " " + Math.max(max_width, max_height));
		
		
		DrawMap out = new DrawMap(Math.max(max_width, max_height), Math.max(max_width, max_height), Color.ALPHA());
		System.out.println("penis");
		int x = 0;
		int y = 0;
		
		String file_out = "";
		
		int count2 = 0;
		
		for(Pack p : maps) {
			count++; 
			count2++;
			
			DrawMap in = p.m;
			
			
			
			if(cur_width < in.getSourceWidth()) cur_width = (int) in.getSourceWidth();
			
			for(int ax = 0; ax < in.getSourceWidth(); ax++) {
				for(int ay = 0; ay < in.getSourceHeight(); ay++) {
					Color c = in.getPixel(ax, ay);		
					
					out.drawPixel(x + ax, y + ay, c);					
				}				
			}
			
			y+= in.getSourceHeight();
			
			String c_out = "" + p.name + ":" + x + ":" + (y -(int)  p.m.getSourceHeight()) + ":" +(int) p.m.getSourceWidth() + ":" + (int)  p.m.getSourceHeight() + "\n";
			file_out += c_out;
				

			if(count == y_amt) {
				count = 0;
				y = 0;
				x += cur_width;
				cur_width = 0;
			}
			System.out.println(count2 + "/" + maps.size());
		}		
		
		try {
			GraphicUtils.saveDrawMap(out, path, name + ".png");
			if(!data.exists()) {
				data.createNewFile();
			}
			
			FileUtils.writeToFile(data, file_out, 0, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

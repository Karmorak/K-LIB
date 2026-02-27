package com.karmorak.lib.animation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TextureLoader {
	
	private List<String> IDS = new ArrayList<>();
	private Map<String, Loader> LIST = new HashMap<>();
	
	public TextureLoader() {}
	
	public void addState(Loader l) {		
		IDS.add(l.getID());		
		LIST.put(l.getID(), l);		
	}
	
	public void sort() {	
		Collections.sort(IDS);
		
		String v = "";		
		int max_y = IDS.get(0).length();
			
		for(int y = 0; y < max_y; y++) {
			boolean found = false;
		
			for(int x = 0; x < IDS.size(); x++) {
				String s = IDS.get(x);
				String s2 = s.split("")[y];
				if(s2.contains("1")) {
					v = v + x;
					found = true;
					break;
				}			
			}					
			if(!found) {
				v = v + "0";
			}			
		}		
//		VALUES = v;
	   
		Map<String, Loader> copy = LIST;		
		Map<String, Loader> treeMap = new TreeMap<String, Loader>(copy);
		LIST = treeMap;	
	}
	
	public Loader AutoUpdate(String id, float deltaTime) {
		Loader loader = getLoader(id);		
		for(String s : IDS) {
			Loader l = LIST.get(s);
			if(l.isUpdateAlways() && !loader.ID.equals(l.ID)) {
				if(l.getTimeSince() >= l.getMaxTime()) {
					l.addCurT();
				} else l.setTimeSince(l.getTimeSince()+deltaTime);				
			}			
		}			
		if(loader.getTimeSince() >= loader.getMaxTime()) {
			loader.addCurT();
		} else loader.setTimeSince(loader.getTimeSince()+deltaTime);
		return loader;
	}
	
	public Loader AutoUpdate(float deltaTime, boolean... bools) {
		Loader loader = getLoader(bools);		
		for(String s : IDS) {
			Loader l = LIST.get(s);
			if(l.isUpdateAlways() && !loader.ID.equals(l.ID)) {
				if(l.getTimeSince() >= l.getMaxTime()) {
					l.addCurT();
				} else l.setTimeSince(l.getTimeSince()+deltaTime);				
			}			
		}			
		if(loader.getTimeSince() >= loader.getMaxTime()) {
			loader.addCurT();
		} else loader.setTimeSince(loader.getTimeSince()+deltaTime);
		
		return loader;
	}
	
	public void AutoUpdate(Loader loader, float deltaTime) {
		for(String s : IDS) {
			Loader l = LIST.get(s);
			if(l.isUpdateAlways() && !loader.ID.equals(l.ID)) {
				if(l.getTimeSince() >= l.getMaxTime()) {
					l.addCurT();
				} else l.setTimeSince(l.getTimeSince()+deltaTime);				
			}			
		}			
		if(loader.getTimeSince() >= loader.getMaxTime()) {
			loader.addCurT();
		} else loader.setTimeSince(loader.getTimeSince()+deltaTime);
	}
	
	
	public Loader getLoader(String id) {
		for (String s : IDS) {
			if (s.equals(id)) {
				return LIST.get(s);
			}
		}
		return null;
	}

	public Loader getLoader(boolean... list) {

		String id = "";
		for (boolean b : list) {
			if (!b)
				id = id + "0";
			else
				id = id + "1";
		}

		for (String s : IDS) {
			if (s.equals(id)) {
				return LIST.get(s);
			}
		}
		System.out.println(id);
		return null;
	}
	
	
	public void RESET() {
		for(String s : IDS) {
			Loader l = LIST.get(s);
			l.curT = 0;			
			for(int i = 0; i < l.timeSince.length; i++) {
				l.timeSince[i] = 0;
			}			
		}
	}
	
	
//	private static final int MAXIDLENGTH = 4;
//	
//	private final boolean WALK, SHOOT, IDLE, DEATH;
//	private final boolean[] BOOL_LIST;
//	private final String IDCODE;
//	
//	private String VALUES;
//	public int lastTexture;
//	public float timeSince;
//	public float maxtime;
//	
//	
//	private final Sprite[] textures;
//	private List<String> IDS = new ArrayList<>();
//	private Map<String, PlayerTextureLoader> LIST = new HashMap<>();


}
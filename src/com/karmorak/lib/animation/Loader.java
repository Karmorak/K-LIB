package com.karmorak.lib.animation;

import com.karmorak.lib.engine.graphic.flat.Texture;

public abstract class Loader {
	
	protected final String ID;
	protected final Texture[] textures;
	int curT = 0;
	protected float[] maxT;
	protected float[] timeSince;	
	protected boolean updateAlways;
	protected boolean endonfinish = false;
	public boolean ended = false;
	static final int MAXIDLENGTH = 8;
	
	public Loader(String id, Texture[] s, float MaxTime) {
		timeSince = new float[s.length];
		maxT = new float[s.length];
		for(int i = 0; i < s.length; i++) {
			timeSince[i] = 0;
			maxT[i] = MaxTime;
		}
		
		textures = s;
		if(id.length() < s.length) throw new IllegalArgumentException();
		this.ID = id;		
	} 
	
	public Loader(Texture[] s, float MaxTime, int... list) {
		timeSince = new float[s.length];
		maxT = new float[s.length];
		for(int i = 0; i < s.length; i++) {
			timeSince[i] = 0;
			maxT[i] = MaxTime;
		}
		textures = s;
		
		String c = "";
		for(int b : list) {
			if(b > 2 || b < 0) {
				c = c + "2";
			} else {
				c = c + "" + b;
			}	
		}		
		this.ID = c;		
	} 
	
	public Loader(Texture[] s, float[] times, int... list) {		
		timeSince = new float[s.length];		
		
		if(times.length < s.length) {
			maxT = new float[s.length];
			for(int i = 0; i < times.length; i++) {
				maxT[i] = times[i];
			}			
			for(int i = 0; i < (s.length-times.length); i++) {
				maxT[s.length+i] = times[0];
			}
		} else {
			maxT = new float[s.length];
			for(int i = 0; i < times.length; i++) {
				maxT[i] = times[i];
			}	
		}		
		
		textures = s;
		
		String c = "";
		for(int b : list) {
			if(b > 2 || b < 0) {
				c = c + "2";
			} else {
				c = c + "" + b;
			}			
		}		
		this.ID = c;		
	}
	
	
	public abstract void init();
	
	public String getID() {
		return ID;
	}	
	public Texture getTexture() {
		return textures[curT];
	}
	
	public Texture[] getTextures() {
		return textures;
	}
	
	public float getTimeSince() {
		return timeSince[getCurT()];
	}
	
	public float getTimeSince(int index) {
		return timeSince[index];
	}

	public void setTimeSince(float f) {
		timeSince[getCurT()] = f;
	}
	
	public void setTimeSince(int index, float f) {
		timeSince[index] = f;
	}	

	public float getMaxTime() {
		return maxT[getCurT()];
	}
	
	public float getMaxTime(int index) {
		return maxT[index];
	}
	
	public float[] getMaxTimes() {
		return maxT;
	}	

	public void setMaxTime(float f) {
		maxT[getCurT()] = f;
	}
	
	public void setMaxTime(int index, float f) {
		maxT[index] = f;
	}
	
	public int getCurT() {
		return curT;
	}
	
	public void setCurT(int f) {
		curT = f;
	}
	
	public void addCurT() {
		if(getCurT() >= getTextures().length-1) {
			if(!endonfinish) {
				resetCurT();
			} else {
				ended = true;
			}
		} else {
			curT++;
		}
	}
	
	public void resetCurT() {
		for(int i = 0; i < timeSince.length; i++) {
			setTimeSince(i, 0);
		}
		setCurT(0);
		ended = false;
	}

	public void endOnFinish(boolean b) {
		endonfinish = b;
	}
	
	public void updateAlways(boolean b) {
		setUpdateAlways(b);
	}

	public boolean isUpdateAlways() {
		return updateAlways;
	}

	public void setUpdateAlways(boolean updateAlways) {
		this.updateAlways = updateAlways;
	}

}
package com.karmorak.lib.engine.graphic.roomy;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import com.karmorak.lib.engine.io.images.PNG;
import com.karmorak.lib.math.Vector2;

public class Material {
	
	
	
	private PNG t; //TODO replace with Texture or similiar
	
	private float shine_damper = 1f;
	private float reflectivity = 0f;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int numberofRows = 1;
	
	private boolean created = false;
	
	public Material(URL path, int x, int y, int width, int height) {
		if(path != null) {
			try {
				this.t = new PNG(path, x, y, width, height);
			} catch (IOException e) {
				System.err.println("Couldn't find texture at: "  + path.toString());
			}
		} else {
			System.err.println(">> Couldn't find texture the path is not existing!");
		}	
	}

	public Material(URL path) {
		if(path != null) {
			try {
				this.t = new PNG(path);
			} catch (IOException e) {
				System.err.println("Couldn't find texture at: "  + path.toString());
			}
		} else {
			System.err.println(">> Couldn't find texture the path is not existing!");
		}			
	}

	public void create() {
		if(!created) { 
//			t.create();
			created = true;
		} else System.err.println("Material already created!");
	}
	
	public boolean hasTransparency() {
		return hasTransparency;
	}

	public void sethasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public void destroy() {
//		t.destroy();

	}
	
	public float getShine_damper() {
		return shine_damper;
	}

	public void setShine_damper(float shine_damper) {
		this.shine_damper = shine_damper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public int getId() {
		return t.getID();
	}

	public int getWidth() {
		return t.getWidth();
	}

	public void setWidth(int width) {
		this.t.setWidth(width);
	}

	public int getHeight() {
		return t.getHeight();
	}
	
	public Vector2 getBounds() {
		return new Vector2(getWidth(), getHeight());
	}

	public void setHeight(int height) {
		this.t.setHeight(height);
	}
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	
	public PNG getPNG() {
		return t;		
	}
	
	public float getTextureXOffset(int index) {
		int column = index % getAtlasRows();
		return (float) column / (float) getAtlasRows();
	}
	
	public float getTextureYOffset(int index) {
		int row = index / getAtlasRows();
		return (float) row / (float) getAtlasRows();
	}
	
	public int getAtlasRows() {
		return numberofRows;
	}

	public void setNumberofRows(int numberofRows) {
		this.numberofRows = numberofRows;
	}
	
}

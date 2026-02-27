package com.karmorak.lib.engine.objects;

import com.karmorak.lib.math.Vector3;

public class Light {

	private Vector3 position;
	private Vector3 colour;
	private float minlight;
	
	public Light() {
		this.position = new Vector3(1, 1, 1);
		this.colour = new Vector3(1, 1, 1);
		this.minlight = 0.2f;
	}
	
	public Light(Vector3 position, Vector3 colour) {
		this.position = position;
		this.colour = colour;
		this.minlight = 0.2f;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getColour() {
		return colour;
	}

	public void setColour(Vector3 colour) {
		this.colour = colour;
	}
	
	public void setMinLight(float light) {
		this.minlight = light;
	}
	
	public float getMinLight() {
		return minlight;
	}
	
	
	
	
	
}



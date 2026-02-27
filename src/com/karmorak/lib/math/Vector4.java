package com.karmorak.lib.math;

import com.karmorak.lib.math.vector.Vector;

public class Vector4  implements Vector {
	
	private float x, y, width, height;

	public Vector4() {
	}

	public Vector4(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Vector4(Vector2 pos, Vector2 bounds) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.width = bounds.getWidth();
		this.height = bounds.getHeight();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setPosition(Vector2 pos) {
		this.x = pos.getX();
		this.y = pos.getY();
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 getPosition() {
		return new Vector2(x, y);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setSize(Vector2 bounds) {
		this.width = bounds.getWidth();
		this.height = bounds.getHeight();
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public Vector2 getSize() {
		return new Vector2(width, height);
	}

	public void set(Vector2 position, Vector2 size) {
		x = position.getX();
		y = position.getY();
		this.width = size.getWidth();
		this.height = size.getHeight();
	}
	public void set(float x, float y, Vector2 size) {
		this.x = x;
		this.y = y;
		this.width = size.getWidth();
		this.height = size.getHeight();
	}
	public void set(Vector2 position, float width, float height) {
		x = position.getX();
		y = position.getY();
		this.width = width;
		this.height = height;
	}
	public void set(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Deprecated
	public void setBounds(Vector2 bounds) {
		this.width = bounds.getWidth();
		this.height = bounds.getHeight();
	}
	@Deprecated
	public void setBounds(float width, float height) {
		this.width = width;
		this.height = height;
	}

	@Deprecated
	public Vector2 getBounds() {
		return new Vector2(width, height);
	}
	
	@Override
	public String toString() {
		return x +  " : " + y + " : " + width + " : " + height;
	}
	

}

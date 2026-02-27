package com.karmorak.lib.engine.graphic.flat.collission;

import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector4;

public class Rectangle extends Colideable {		
	
	public Rectangle(Vector2 pos, float width, float height) {
		type = SHAPE_RECTANGLE;
		
		this.pos = new Vector2(pos.getX(), pos.getY());			
		this.bounds = new Vector2(width, height);
		this.hit_box = new Vector4(0, 0, width, height);	
	}
	
	public Rectangle(float pos_x, float pos_y, float width, float height) {
		type = SHAPE_RECTANGLE;
		
		this.pos = new Vector2(pos_x, pos_y);			
		this.bounds = new Vector2(width, height);
		this.hit_box = new Vector4(0, 0, width, height);	
	
	}
	
	public Vector2 getPosition() {
		return pos;
	}

	public float getX() {
		return pos.getX();
	}
	
	public void setX(float x) {
		pos.setX(x);
	}
	
	public float getY() {
		return pos.getY();
	}
	
	public void setY(float y) {
		pos.setY(y);
	}
	
	public float getWidth() {
		return bounds.getWidth();
	}
	
//	public void setWidth(float width) {
//		pos_width = width;
//	}
	
	public float getHeight() {
		return bounds.getHeight();
	}
	
//	public void setHeight(float height) {
//		pos_height = height;
//	}
	
	
	
	
}
package com.karmorak.lib.engine.graphic.flat.collission;

import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector4;

public class Triangle extends Colideable {	
	
	public static final short FACING_NORTH_WEST = 0;
	public static final short FACING_NORTH_EAST = 1;
	public static final short FACING_SOUTH_EAST = 2;
	public static final short FACING_SOUTH_WEST = 3;
	
	public Triangle(Vector2 pos, float width, float height) {
		type = SHAPE_TRIANGLE;
		this.pos = new Vector2(pos.getX(), pos.getY());			
		this.bounds = new Vector2(width, height);
		this.hit_box = new Vector4(0, 0, width, height);	
	}

	public short getRotation() {
		return rotation;
	}

	public void setRotation(short rotation) {
		this.rotation = rotation;
	}
	
	
	
}

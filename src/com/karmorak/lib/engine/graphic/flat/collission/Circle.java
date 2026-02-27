package com.karmorak.lib.engine.graphic.flat.collission;

import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector4;

public class Circle extends Colideable {		
	
	public Circle(Vector2 pos, float radius) {
		type = SHAPE_CIRCLE;
		this.pos = new Vector2(pos.getX(), pos.getY());
		this.bounds = new Vector2(radius, radius);
		this.hit_box = new Vector4(0, 0, radius, radius);	
	}
}
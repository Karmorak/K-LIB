package com.karmorak.lib.prototype.phsyic;

import com.karmorak.lib.Color;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.Vector2;

public class Point {

	
	private static Texture t_point;
	
	public static float gravity = 0.5f;
	public static float friction = 0.999f;
	
	public float x = 100;
	public float y = 800;
	public float old_x = 95;
	public float old_y = 750;
	public float bounce = 0.9f;
	public float angle = 0f;
	public float speed = 0.1f;
	
	public boolean isHidden;
	public boolean pinned;
	
	void initTexture() {
		if(t_point == null) {
			t_point = new Texture(DrawMap.buildCircle(Color.GREEN(), 64));
			t_point.create();	
		}
	}
	
	public Point() {
		initTexture();
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;		
		initTexture();
	}
	
	public Point(int x, int y, boolean isHidden) {
		this.x = x;
		this.y = y;			
		this.isHidden = isHidden;
		initTexture();
	}
	
	public Point(int x, int y, float vx, float vy) {
		this.x = x;
		this.y = y;			
		this.old_x = x - vx;
		this.old_y = y - vy;
		initTexture();
	}
	
	public Point(int x, int y, float vx, float vy, boolean isHidden) {
		this.x = x;
		this.y = y;			
		this.old_x = x - vx;
		this.old_y = y - vy;			
		this.isHidden = isHidden;
		initTexture();
	}
	
	public void draw(MasterRenderer renderer) {			
		if(!isHidden ) {
			t_point.setPosition(x-16, y-16);
			t_point.setSize(32, 32);
			t_point.render();
		}			
//		renderer.processTexture(t_point, x-16, y-16, 32, 32, 2);
	}
	
	public Vector2 getPos() {
		return new Vector2(x, y);
	}		


}

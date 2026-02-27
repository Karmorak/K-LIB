package com.karmorak.lib.prototype.phsyic;

import com.karmorak.lib.Color;
import com.karmorak.lib.engine.graphic.flat.DrawMap;

public class Stick {
	
	public Point p0, p1;
	public float length;
	public DrawMap drawMap;
	public boolean isHidden;		
	public Color color = Color.RED();
	
	
	public Stick(Point p0, Point p1) {
		this.p0 = p0;
		this.p1 = p1;
					
		float d_x = p1.x - p0.x; //100
		float d_y = p1.y - p0.y;
		length = distance();
		
		drawMap = DrawMap.buildLine(color, (int)(length ), (int)(length), 0 , 0,(int)d_x,(int) d_y);			
	}
	
	public Stick(Point p0, Point p1, boolean isHidden) {
		this.p0 = p0;
		this.p1 = p1;
		
		float d_x = p1.x - p0.x; //100
		float d_y = p1.y - p0.y;
		length = distance();
		
		if(!isHidden)
			drawMap = DrawMap.buildLine(color, (int)(length ), (int)(length), 0 , 0,(int)d_x,(int) d_y);
		
		this.isHidden = isHidden;
	}
	
	
	public float distance() {
		float d_x = p1.x - p0.x;
		float d_y = p1.y - p0.y;	
		
		return (float) Math.sqrt(d_x*d_x + d_y * d_y);
	}
	
	public static float distance(Point p0, Point p1) {
		float d_x = p1.x - p0.x;
		float d_y = p1.y - p0.y;	
		
		return (float) Math.sqrt(d_x*d_x + d_y * d_y);
	}
	
	
	public void draw() {	
		if(!isHidden) {
			drawMap.setPosition(p0.x - length, p0.y - length);
			drawMap.render();	
		}
	}
	
	
	


}

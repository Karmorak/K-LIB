package com.karmorak.lib.prototype.phsyic;

import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.Vector2;

public class Form {
	
	Point[] points;
	Color color = Color.GREEN();
	DrawMap drawMap;
	int length;
	Texture texture;
	
	public Form(Point p0, Point p1, Point p2, Point p3) {			
		points = new Point[] {p0, p1, p2, p3};
		
		drawMap = new DrawMap(256, 256, color);
	}
	
	public Form(Point p0, Point p1, Point p2, Point p3, int length) {			
		points = new Point[] {p0, p1, p2, p3};
					
		this.length = (int) (length);
		drawMap = new DrawMap(length, length, color);
		
		texture = new Texture(KLIB.URL("assets/emerald.png"));
	}
	
	public void drawShape(MasterRenderer renderer) {
		float minX = Math.min(points[0].x, Math.min(points[1].x, Math.min(points[2].x, points[3].x)));
		float minY = Math.min(points[0].y, Math.min(points[1].y, Math.min(points[2].y, points[3].y)));
		float maxX = Math.max(points[0].x, Math.max(points[1].x, Math.max(points[2].x, points[3].x)));
		float maxY = Math.max(points[0].y, Math.max(points[1].y, Math.max(points[2].y, points[3].y)));
		
		float dxc = maxX - minX;
		float dyc = maxY - minY;
		
		drawMap.clearDrawMap();	
		
		int abs_x = (int) ((length - dxc) *0.5f);
		int abs_y = (int) ((length - dyc) *0.5f);
		
		drawMap.fillTriangle(
				new Vector2(abs_x +(points[0].x - minX),length + (minY - points[0].y)-abs_y), 					
				new Vector2(abs_x +(points[1].x - minX),length + (minY - points[1].y)-abs_y),					
				new Vector2(abs_x +(points[3].x - minX),length + (minY - points[3].y)-abs_y), 			
				color);
		
		drawMap.fillTriangle(
				new Vector2(abs_x +(points[1].x - minX),length-(points[1].y - minY)-abs_y), 					
				new Vector2(abs_x +(points[2].x - minX),length-(points[2].y - minY)-abs_y),					
				new Vector2(abs_x +(points[3].x - minX),length-(points[3].y - minY)-abs_y), 					
				color);
		
		drawMap.fillTriangle(
				new Vector2(abs_x +(points[2].x - minX),length-(points[2].y - minY)-abs_y), 					
				new Vector2(abs_x +(points[3].x - minX),length-(points[3].y - minY)-abs_y),					
				new Vector2(abs_x +(points[0].x - minX),length-(points[0].y - minY)-abs_y), 					
				color);
		
		drawMap.fillTriangle(
				new Vector2(abs_x +(points[3].x - minX),length-(points[3].y - minY)-abs_y), 					
				new Vector2(abs_x +(points[0].x - minX),length-(points[0].y - minY)-abs_y),					
				new Vector2(abs_x +(points[1].x - minX),length-(points[1].y - minY)-abs_y), 
				color);
		
		drawMap.setPosition(minX + (dxc-length) * 0.5f, minY +  (dyc-length) * 0.5f);
		drawMap.setSize(length, length);
		drawMap.render();
	}
	
	public void drawTexture(MasterRenderer renderer) {			
		float minX = Math.min(points[0].x, Math.min(points[1].x, Math.min(points[2].x, points[3].x)));
		float minY = Math.min(points[0].y, Math.min(points[1].y, Math.min(points[2].y, points[3].y)));
		float maxX = Math.max(points[0].x, Math.max(points[1].x, Math.max(points[2].x, points[3].x)));
		float maxY = Math.max(points[0].y, Math.max(points[1].y, Math.max(points[2].y, points[3].y)));
		
		float dxc = maxX - minX;
		float dyc = maxY - minY;
		
		float w = Stick.distance(points[0], points[1]);
		float h = Stick.distance(points[0], points[3]);
		
		int dx = (int) (points[1].x - points[0].x);
		int dy = (int) (points[1].y - points[0].y);
		
		double angle = Math.atan2(dy, dx);
		
		texture.setPosition(minX + (dxc-w) * 0.5f, minY +  (dyc-h) * 0.5f);
		texture.setSize(w, h);
		texture.setRotation((float)Math.toDegrees(-angle));
		
		texture.render();
	}		
}
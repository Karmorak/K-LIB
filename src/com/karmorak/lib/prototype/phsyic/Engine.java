package com.karmorak.lib.prototype.phsyic;

public class Engine extends Point {
	
	public float baseX = 550, baseY = 900;
	public float range = 100;

	
	public Engine() {
		pinned = true;			
		x = 550;
		y = 900;		
		angle = 0;
		speed = 0.05f;	
	}	
	
	public void updateEngine() {
		x = (float) (baseX + Math.cos(angle) * range);
		y = (float) (baseY + Math.sin(angle) * range);
		angle += speed;
	}
	
}
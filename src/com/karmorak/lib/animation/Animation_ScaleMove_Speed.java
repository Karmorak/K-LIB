package com.karmorak.lib.animation;


public class Animation_ScaleMove_Speed {
	
	
	
	
	
	
	
	final float startX;
	final float startY;
	final float startScale;
	final float targetX;
	final float targetY;
	final float targetScale;	
	final float xc;
	final float yc;
	
	float curX;
	float curY;
	float curScale;
	
	/** 0 = not startet, 1 = not ready, 2 = ready*/
	public int anim_state = 0;
	
	final float animSpeed;
	
	
	
	public Animation_ScaleMove_Speed(float startX, float startY, float startScale, float targetX, float targetY, float targetScale) {
		this.startX = startX;
		this.startY = startY;
		this.startScale = startScale;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetScale = targetScale;
		
		float ax = targetX - startX;
		float ay = targetY - startY;	
										
		float c = (float) Math.sqrt(ax * ax + ay * ay);				
		
		xc = ay / c;
		yc = ay / c;	
		
		this.animSpeed = 1f;
	}
	
	
	public Animation_ScaleMove_Speed(float startX, float startY, float startScale, float targetX, float targetY, float targetScale, float speed) {
		this.startX = startX;
		this.startY = startY;
		this.startScale = startScale;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetScale = targetScale;
		
		float ax = targetX - startX;
		float ay = targetY - startY;	
										
		float c = (float) Math.sqrt(ax * ax + ay * ay);				
		
		xc = ax / c;
		yc = ay / c;	
		
		this.animSpeed = speed;
	}
	

	
	public boolean update() {				
		if(anim_state != 2) {
			anim_state = 1;			
			return updateMoveSpeed();
		}	
		return false;
	}
	
	
	private boolean updateMoveSpeed() {
		boolean s = false;				
		boolean x = false;
		boolean y = false;
		
		if(startScale < targetScale) {
			float scale; 
				
			if(targetY > startY) {						
				scale = (curY) / (targetY - startY) * (targetScale - startScale); 										
			} else {
				scale = (-1*curY) / (startY - targetY) * (targetScale - startScale);
			}
			
			if(scale >= targetScale - startScale) {
				curScale = targetScale - startScale; 
				s = true;
			} else {
				curScale = scale; 
			}
		} else {
			float scale; 
			
			if(targetY > startY) {
				scale = (curY) / (targetY - startY) * (startScale- targetScale);								
			} else {
				scale = (-1*curY) / (startY - targetY) * (startScale- targetScale);					
			}	
			if(scale >= startScale- targetScale) {
				curScale = startScale- targetScale; 
				s = true;
			} else {
				curScale = scale; 
			}	
		}
			
		
		if(xc < 0) {
			if(startX + curX > targetX)
				curX += xc * animSpeed;
			else x = true;
		} else {
			if(startX + curX < targetX)
				curX += xc * animSpeed;
			else x = true;
		}
		
		if(yc > 0 ) {
			if(startY + curY < targetY)
				curY += yc * animSpeed;
			else y = true;
		} else {
			if(startY + curY > targetY)
				curY += yc * animSpeed;
			else y = true;
		}	
		if(x && y && s) return true; 
		else return false;
	}
	
	
	public float getStartX() {
		return startX;
	}
	
	public float getStartY() {
		return startY;
	}
	
	public float getStartScale() {
		return startScale;
	}
	
	public float getCurX() {
		return startX + curX;
	}
	
	public float getCurY() {
		return startY + curY;
	}
	
	public float getCurScale() {
		if(startScale < targetScale) {
			return startScale + curScale;
		} else {
			return startScale - curScale;
		}
		
		
	}
	
	public void reset() {
		curX = 0;
		curY = 0;
		curScale = 0;
		anim_state = 0;
	}
	
	public boolean animFinish() {
		boolean s = false;				
		boolean x = false;
		boolean y = false;
		
		if(startScale + curScale >= targetScale) {				
			s = true;
		}
			
		
		if(xc < 0) {
			if(startX + curX < targetX)
				x = true;
		} else {
			if(startX + curX > targetX)
				x = true;
		}
		
		if(yc > 0 ) {
			if(startY + curY > targetY)
				y = true;
		} else {
			if(startY + curY < targetY)
				y = true;
		}
		
		if(x && y && s) {
			anim_state = 2;
			return true;		
		}
		return false;
	}
	
	
	
	
	
	
}

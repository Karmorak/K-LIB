package com.karmorak.lib.animation;

import com.karmorak.lib.KLIB;

public class Animation_ScaleMove_Timed {
	
	final float startX;
	final float startY;
	final float startScale;
	final float targetX;
	final float targetY;
	final float targetScale;	
	final float targetTime;
	
	float curX;
	float curY;
	float curScale;
	float curTime;
	
	/** 0 = not startet, 1 = not ready, 2 = ready*/
	public int anim_state = 0;
	
	
	
	
	
	public Animation_ScaleMove_Timed(float startX, float startY, float startScale, float targetX, float targetY, float targetScale, float targetTime) {
		this.curTime = 0;
		this.startX = startX;
		this.startY = startY;
		this.startScale = startScale;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetScale = targetScale;
		this.targetTime = targetTime;		
	}
	
	public void update() {			
		updateTime();
	}
	
	
	
	private float timeSincelastFrame = 0;
	private boolean updateTime() {				
		if(anim_state < 2) {
			float time = KLIB.graphic.getTime();
			float result = curTime / targetTime;
			float deltaTime = 0;
			
			if(timeSincelastFrame != 0) deltaTime = time - timeSincelastFrame;	
			if(result > 1) result = 1;
			
			if(targetScale > startScale) curScale = (targetScale - startScale) * result;
			else curScale = (startScale - targetScale) * result;
			curX = (targetX- startX) * result;
			curY = (targetY - startY) * result;
			
			timeSincelastFrame = time;		
			curTime +=  deltaTime;	
			if(result == 1)  {
				anim_state = 2;
				return true;
			} else return false;	
		}	
		return true;
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
		return startX+curX;
	}
	
	public float getCurY() {
		return startY+curY;
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
		timeSincelastFrame = 0;
		curTime = 0;
	}
	
	public boolean isFinish() {
		return anim_state  == 2 ? true : false;
	}
	
	
	
	
	
	

}

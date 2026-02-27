package com.karmorak.lib.prototype;

import com.karmorak.lib.Color;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.ui.button.Scrollable;

public class Shadow {

	protected final DrawMap pix;
	protected Texture c;
	protected Vector2 dir;
	protected Scrollable shadow_pos, shadow_length;
	protected final int shadow_size = 32;
	protected float fade = 3;
	protected final boolean SHADOW_DYNAMIC_TIME = true;
	protected final boolean SHADOW_FADING = true;
	protected final float SHADOW_ALPHA = 0.6f;
	protected final Color SHADOW_COLOR = new Color(0, 0, 0.1f, SHADOW_ALPHA);
	
	public Shadow() {
		
		dir = new Vector2(1f, 1f);
		
		shadow_pos = new Scrollable("penis", false, 300);
		shadow_pos.setPosition(450, 400);
		shadow_length = new Scrollable("penis", false, 300);
		shadow_length.setPosition(450, 400 - shadow_pos.getHeight());
		shadow_length.setValue(1f);
		
		pix = new DrawMap((int)shadow_size,(int) (shadow_size*3), SHADOW_COLOR);
		pix.setColor(SHADOW_COLOR);
		
	}
	
	
	public void update() {		
		
		float value = 1;
		float pos_shadow = shadow_pos.getValue();
		float shadow_l;
		if(SHADOW_DYNAMIC_TIME)	shadow_l = 1;
		else shadow_l = shadow_length.getValue();
		
		if(pos_shadow < 0.25f) {
			value = (pos_shadow *4);
		} else if(pos_shadow < 0.5f) {
			value = 1 - (pos_shadow - 0.25f) * 4;
		} else if(pos_shadow < 0.75f) {
			value = (pos_shadow - 0.5f) * 4;
		} else {
			value = 1- ((pos_shadow-0.75f) *4);
		}
		
		
		if(SHADOW_DYNAMIC_TIME) {
			if(pos_shadow < 0.5) {
				float shadowtime = 0.5f - pos_shadow + 0.05f;			
				shadow_l = 0.25f + shadowtime;			
			} else {
				float shadowtime = pos_shadow - 0.45f;			
				shadow_l = 0.25f + shadowtime;		
			}
		}
		
		int shadow = (int) (shadow_l *shadow_size);
		
		
		if(pos_shadow < 0.25f) {
			int prev_x = -1;	
			for (int y = 0; y < shadow; y++) {
				int pos_y = (int) (y * value);
				
				if(SHADOW_FADING) {
					if(prev_x != pos_y) {
						if(y < shadow-fade) {					
							for (int i = 0; i < shadow_size; i++) {
								float alpha = 1;
								
								if(i < fade) {
									alpha = i/fade;
								} else if(i >= shadow_size-fade) {
									alpha = (shadow_size - i)/fade;
								}	
								pix.drawPixel(pos_y, shadow_size - y + i, new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), SHADOW_ALPHA * alpha));		
								
							}	
						} else {
							for (int i = 0; i < shadow_size; i++) {	
								float alpha = (shadow - y)/fade;
								
								if(i <= fade/2) {
									alpha = i/fade;
								} else if(i >= shadow_size-fade/2) {
									alpha = (shadow_size - i)/fade;
								}
								
								pix.drawPixel(pos_y, shadow_size - y + i, new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), SHADOW_ALPHA * alpha));						
							}			
						}
					}
					prev_x = pos_y;
				} else {
					if(prev_x != pos_y) {
						for (int i = 0; i < shadow_size; i++) {
							pix.drawPixel(pos_y, shadow_size - y + i, SHADOW_COLOR);						
						}	
					}
					prev_x = pos_y;
				}	
			}
		} else if(pos_shadow < 0.5f) {
			for (int x = 0; x < shadow; x++) {
				int pos_x = shadow_size - (int)(x*value);
				if(SHADOW_FADING) {
					if(x < shadow-fade) {					
						for (int i = 0; i < shadow_size; i++) {
							float alpha = 1;
							
							if(i < fade) {
								alpha = i/fade;
							} else if(i >= shadow_size-fade) {
								alpha = (shadow_size - i)/fade;
							}	
							pix.drawPixel(x, pos_x + i, new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), SHADOW_ALPHA * alpha));		
							
						}	
					} else {
						for (int i = 0; i < shadow_size; i++) {	
							float alpha = (shadow - x)/fade;
							
							if(i <= fade/2) {
								alpha = i/fade;
							} else if(i >= shadow_size-fade/2) {
								alpha = (shadow_size - i)/fade;
							}
							
							pix.drawPixel(x, pos_x + i, new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), SHADOW_ALPHA * alpha));							
						}			
					}	
				} else {
					for (int i = 0; i < shadow_size; i++) {
						pix.drawPixel(x, pos_x + i, SHADOW_COLOR);							
					}	
				}			
			}
		} else if(pos_shadow < 0.75f) {
			for (int x = 0; x < shadow; x++) {
				int pos_x = shadow_size + (int)(x*value);
				
				if(SHADOW_FADING) {
					if(x < shadow-fade) {					
						for (int i = 0; i < shadow_size; i++) {
							float alpha = 1;
							
							if(i < fade) {
								alpha = i/fade;
							} else if(i >= shadow_size-fade) {
								alpha = (shadow_size - i)/fade;
							}	
							pix.drawPixel(x, pos_x + i, new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), SHADOW_ALPHA * alpha));		
							
						}	
					} else {
						for (int i = 0; i < shadow_size; i++) {	
							float alpha = (shadow - x)/fade;
							
							if(i <= fade/2) {
								alpha = i/fade;
							} else if(i >= shadow_size-fade/2) {
								alpha = (shadow_size - i)/fade;
							}
							
							pix.drawPixel(x, pos_x + i, new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), SHADOW_ALPHA * alpha));							
						}			
					}	
				} else {
					for (int i = 0; i < shadow_size; i++) {
						pix.drawPixel(x, pos_x + i, SHADOW_COLOR);							
					}	
				}
			}
		} else {
			
			int prev_x = -1;
			for (int y = 0; y < shadow; y++) {
				int pos_y = (int) (y * value);				
				
				if(SHADOW_FADING) {
					if(prev_x != pos_y) {
						if(y < shadow-fade) {					
							for (int i = 0; i < shadow_size; i++) {
								float alpha = 1;
								
								if(i < fade) {
									alpha = i/fade;
								} else if(i >= shadow_size-fade) {
									alpha = (shadow_size - i)/fade;
								}	
								pix.drawPixel(pos_y, shadow_size + y + i, new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), SHADOW_ALPHA * alpha));		
								
							}	
						} else {
							for (int i = 0; i < shadow_size; i++) {	
								float alpha = (shadow - y)/fade;
								
								if(i <= fade/2) {
									alpha = i/fade;
								} else if(i >= shadow_size-fade/2) {
									alpha = (shadow_size - i)/fade;
								}
								
								pix.drawPixel(pos_y, shadow_size + y + i, new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), SHADOW_ALPHA * alpha));					
							}			
						}
					}
					prev_x = pos_y;
				} else {
					if(prev_x != pos_y) {
						for (int i = 0; i < shadow_size; i++) {
							pix.drawPixel(pos_y, shadow_size + y + i, SHADOW_COLOR);						
						}	
					}
					prev_x = pos_y;
				}
			}
		}
		
		c = new Texture(pix);
		pix.clearDrawMap();
		pix.setColor(SHADOW_COLOR);
		
	}
	
	public void draw(MasterRenderer batch) {
		batch.draw(c, 100, 0, 128, 128*3);
		shadow_pos.draw(batch);
		shadow_length.draw(batch);
	}
	
	
}

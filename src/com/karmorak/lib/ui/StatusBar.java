//v1.0
package com.karmorak.lib.ui;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector4;
import com.karmorak.lib.ui.button.Button;

public class StatusBar {
	
	
	private static final String DEF_PATH = "/com/karmorak/lib/assets/healthbar_grey.png";
	private static final Vector4[] DEF_BOUNDS = {new Vector4(0, 0, 4, 4), new Vector4(4, 0, 24, 4), new Vector4(28, 0, 4, 4)};
	private static final int DEF_MAX_WIDTH = 150, DEF_MAX_HEIGHT  = 20;
	
	
	public boolean show = true, two_layers = false, show_value = true, show_value_drops = false, auto_drop = true, show_by_trigger = false;
	
	Vector2 position;
	Vector4[] bounds;
	Texture back_left, back_mid, back_right;
	Texture top_left, top_mid, top_right;
	//size6
//	Texture back_left_2, back_right_2;
	
	
	Button value;
	float cur_value;
	float max_value;
	float max_width;
	float max_height;
	
	
	ArrayList<ValueDrop> ValueDrops;
	
	static class ValueDrop {
		
		static float scale = 1f;
		static float stay_time = 0.5f;
		static float scale_y = 19;
		static float speed_x = 0.15f;
		static float speed_y = 0.05f;
		static Color color;
		
		public ValueDrop(String val_name) {
			b = new Button(val_name);
			b.setInteractable(false);
			b.setScale(0.04f * scale);
			if(color!= null) b.setColor(color);
			
		}
		
		Button b;
		float progress_x;
		float progress_y;
		
		float time_since_ready;
		
		
	}
	
	public StatusBar(Vector2 pos, int value) {
		Texture t = null;	
		ValueDrops = new ArrayList<>();
		
			t = new Texture(KLIB.URL(DEF_PATH));
		back_left = new Texture(t, DEF_BOUNDS[0]);			
		back_mid = new Texture(t,  DEF_BOUNDS[1]);			
		back_right = new Texture(t, DEF_BOUNDS[2]);	
		this.bounds = DEF_BOUNDS;		
		
		max_width = DEF_MAX_WIDTH;
		max_height = DEF_MAX_HEIGHT;
		cur_value = value;
		max_value = value;
		
		this.value = new Button("" + value);
		this.value.setColor(Color.WHITE());
		this.value.setInteractable(false);
		this.value.setHeight(max_height * 0.9f);
		
		setSize(DEF_MAX_WIDTH, DEF_MAX_HEIGHT);
		setPosition(pos);
		setColor(Color.RED());
	}
	
	public StatusBar(Vector2 pos, int width, int height, int value) {		
//		int size = bounds.length;		
		Texture t = null;	
		ValueDrops = new ArrayList<>();
		
			t = new Texture(KLIB.URL(DEF_PATH));
		back_left = new Texture(t, DEF_BOUNDS[0]);			
		back_mid = new Texture(t,  DEF_BOUNDS[1]);			
		back_right = new Texture(t, DEF_BOUNDS[2]);	
		this.bounds = DEF_BOUNDS;		
		
		max_width = width;
		max_height = height;
		cur_value = value;
		max_value = value;
		
		this.value = new Button("" + value);
		this.value.setColor(Color.WHITE());
		this.value.setInteractable(false);
		this.value.setHeight(height * 0.9f);
		
		setSize(width, height);
		setPosition(pos);
		setColor(Color.RED());
	}
	
	
	
	public StatusBar(URL back_texture_path, URL top_texture_path, Vector4[] bounds, Vector2 pos, int width, int height, int value) {
//		int size = bounds.length;		
		Texture t;	
		ValueDrops = new ArrayList<>();
		
		if(back_texture_path == null) {
			t = new Texture(KLIB.URL(DEF_PATH));
			back_left = new Texture(t, DEF_BOUNDS[0]);			
			back_mid = new Texture(t,  DEF_BOUNDS[1]);			
			back_right = new Texture(t, DEF_BOUNDS[2]);	
			this.bounds = DEF_BOUNDS;			
		} else {
			t = new Texture(back_texture_path);
			back_left = new Texture(t, bounds[0]);			
			back_mid = new Texture(t,  bounds[1]);			
			back_right = new Texture(t, bounds[2]);
			this.bounds = bounds;
		}
			
		if(top_texture_path != null) {		
			t = new Texture(top_texture_path);
			top_left = new Texture(t, bounds[0]);			
			top_mid = new Texture(t,  bounds[1]);			
			top_right = new Texture(t, bounds[2]);
			two_layers = true;
		}
		

		
		max_width = width;
		max_height = height;
		cur_value = value;
		max_value = value;
		
		this.value = new Button("" + value);
		this.value.setColor(Color.WHITE());
		this.value.setInteractable(false);
		this.value.setScale(0.005f);
		
		setSize(width, height);
		setPosition(pos);
		if(back_texture_path == null)
			setColor(Color.RED());
	}
	
	public void setSize(int width, int height) {		
		int sum_width = (int) (bounds[1].getWidth());
		float scale_x = (width-height*2) / sum_width; 
		
		back_left.setSize(height, height);
		back_mid.setSize(bounds[1].getWidth() * scale_x, height);
		back_right.setSize(height, height);
		
		if(two_layers) {
			top_left.setSize(height, height);
			top_mid.setSize(bounds[1].getWidth() * scale_x, height);
			top_right.setSize(height, height);
		}
		
		value.setHeight(height * 0.9f);
		ValueDrop.scale = scale_x;
	}
	
	void setSize_(int width, int height) {	
		int sum_width = (int) (bounds[1].getWidth());
		float scale_x = (width-height*2) / sum_width; 
		
		if(!two_layers) {
			back_left.setSize(height, height);
			back_mid.setSize(bounds[1].getWidth() * scale_x, height);
			back_right.setSize(height, height);
		
		} else {
			top_left.setSize(height, height);
			top_mid.setSize(bounds[1].getWidth() * scale_x, height);
			top_right.setSize(height, height);
		}
		
		value.setHeight(height * 0.9f);
		ValueDrop.scale = scale_x;
		
		setPosition(position);
	}
	
	public void setPosition(Vector2 pos) {
		position = pos;
		
		back_left.setPosition(pos.getX(), pos.getY());
		back_mid.setPosition(pos.getX() + back_left.getWidth(), pos.getY());
		back_right.setPosition(back_mid.getPosition().getX() + back_mid.getWidth(), pos.getY());
		
		if(two_layers) {
			top_left.setPosition(pos.getX(), pos.getY());
			top_mid.setPosition(pos.getX() + top_left.getWidth(), pos.getY());
			top_right.setPosition(top_mid.getPosition().getX() + top_mid.getWidth(), pos.getY());
		}
		
		value.setPosition(back_mid.getPosition().getX() + (back_mid.getWidth() - value.getWidth()) *0.5f, pos.getY() + (back_mid.getHeight() - value.getHeight()) *0.5f);
	}
	
	public void setColor(Color c) {
		back_left.setColor(c);
		back_mid.setColor(c);
		back_right.setColor(c);
		
		if(two_layers) {
			top_left.setColor(c);
			top_mid.setColor(c);
			top_right.setColor(c);
		}
	}
	
	public void setColor(Color c, float intensity) {
		back_left.setColor(c, intensity);
		back_mid.setColor(c, intensity);
		back_right.setColor(c, intensity);
		
		if(two_layers) {
			top_left.setColor(c, intensity);
			top_mid.setColor(c, intensity);
			top_right.setColor(c, intensity);
		}
	}
	
	public void setAlpha(int a) {
		back_left.setAlpha(a);
		back_mid.setAlpha(a);
		back_right.setAlpha(a);
		
		if(two_layers) {
			top_left.setAlpha(a);
			top_mid.setAlpha(a);
			top_right.setAlpha(a);
		}
	}
	
	
	
	public Vector2 getPosition() {
		return position;
	}
	
	public static void setDropColor(Color c) {
		ValueDrop.color = c;
	}
	
	public void addValueDrop(String val_name) {
		ValueDrops.add(new ValueDrop(val_name));
	}

	public void setMaxValue(float value) {
		this.max_value = value;
	}
	
	public void addCurValue() {
		if(cur_value+1 <= max_value) {
			cur_value += 1;
			float w = cur_value / max_value;
			setSize_((int)(w * max_width),(int) max_height);	
			value.setName("" +(int)cur_value);
			if(auto_drop) {
				addValueDrop("+1.0");
			}		
		}
		trigger();
	}
	
	public void removeCurValue() {
		if(cur_value-1 >= 0) {
			cur_value = cur_value - 1;
			float w = cur_value / max_value;
			setSize_((int)(w * max_width),(int) max_height);		
			value.setName("" +(int)cur_value);
			if(auto_drop) {
				addValueDrop("-1.0");
			}		
		}
		trigger();
	}
	
	public void removeCurValue(float f) {
		if(cur_value-1 >= 0) {
			cur_value = cur_value - f;
			float w = cur_value / max_value;
			setSize_((int)(w * max_width),(int) max_height);		
			value.setName("" +(int)cur_value);
			if(auto_drop) {
				addValueDrop("-" + f);
			}		
		}
		trigger();
	}
	
	public void setCurValue(float value) {		
		float w = value / max_value;
		setSize_((int)(w * max_width),(int) max_height);	
		this.value.setName("" + (int)value);
		cur_value = value;
		if(auto_drop) {
			String s = "" + (value - cur_value);
			addValueDrop(s);
		}	
		trigger();
	}
	
	void trigger() {
		got_triggered = true;
		time_current_trigger = 0;
	}
	
	public float getCurValue() {
		return cur_value;
	}
	
	public float getMaxValue() {
		return max_value;
	}
	
	public float getMaxHeight() {
		return max_height;
	}
	
	public float getMaxWidth() {
		return max_width;
	}
	
	public void showByTrigger(boolean b) {
		show_by_trigger = b;
	}
	
	boolean got_triggered = false;
	float time_current_trigger;
	final float TIME_MAX_TRIGGER = 3f;
	
	@SuppressWarnings("unchecked")
	public void update(float deltaTime) {
			
		if(show_value_drops) {
			ArrayList<ValueDrop> nlist = new ArrayList<>();
			int sum_width = (int) (back_left.getWidth() + back_mid.getWidth()  + back_right.getWidth());
			
			for (int i = 0; i < ValueDrops.size(); i++) {
				ValueDrop drop = ValueDrops.get(i);
				Button b = drop.b;
				
				float cos = (float) Math.cos(drop.progress_y);
				
				if(cos > 0) {
					float scale = ValueDrop.scale_y*ValueDrop.scale;
					
					b.setPosition(position.getX() + sum_width + drop.progress_x, position.getY() + cos*scale - scale);
					drop.progress_x += ValueDrop.speed_x * ValueDrop.scale;
					drop.progress_y += ValueDrop.speed_y;
					nlist.add(drop);
				} else {
					if(drop.time_since_ready < ValueDrop.stay_time) {
						drop.time_since_ready += deltaTime;
						nlist.add(drop);
					}			
				}
			}	
			
			ValueDrops = (ArrayList<ValueDrop>) nlist.clone();	
		}
		
		if(show_by_trigger && got_triggered) {
			time_current_trigger += deltaTime;
		}
		
	}
	
	public void draw(MasterRenderer renderer, int layer) {
		if(show) {
			if(!show_by_trigger) {
				renderer.processTexture(back_left, layer);
				renderer.processTexture(back_mid, layer);
				renderer.processTexture(back_right, layer);
				
				if(two_layers) {
					renderer.processTexture(top_left, layer+1);
					renderer.processTexture(top_mid, layer+1);
					renderer.processTexture(top_right, layer+1);
				}	
				if (show_value) {
					value.draw(renderer, layer+2);
				}
				
				if(show_value_drops)
					for (int i = 0; i < ValueDrops.size(); i++) {
						ValueDrop drop = ValueDrops.get(i);
						Button b = drop.b;
						b.draw(renderer, layer);
					}	
			} else {
				if(time_current_trigger < TIME_MAX_TRIGGER && got_triggered) {
					renderer.processTexture(back_left, layer);
					renderer.processTexture(back_mid, layer);
					renderer.processTexture(back_right, layer);
					
					if(two_layers) {
						renderer.processTexture(top_left, layer+1);
						renderer.processTexture(top_mid, layer+1);
						renderer.processTexture(top_right, layer+1);
					}	
					if (show_value) {
						value.draw(renderer, layer+2);
					}
					
					if(show_value_drops)
						for (int i = 0; i < ValueDrops.size(); i++) {
							ValueDrop drop = ValueDrops.get(i);
							Button b = drop.b;
							b.draw(renderer, layer);
						}
				} else {
					got_triggered = false;
					time_current_trigger = 0;
				}
			}				
		}		
	}


	
	

}

//v2.0
package com.karmorak.lib.ui.button;

import com.karmorak.lib.Input;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;
import com.karmorak.lib.math.Vector4i;

public class Scrollable extends Button {

	/*
	v2.0
	 - removed setBorder feature completely
	 - new Textures and way of handling them

	 */

	private static final String PATH_NEW_BACKGROUND_TEXTURE = "/com/karmorak/lib/assets/scrollable/circle.png";
	private static final String PATH_NEW_SLIDER_TEXTURE = "/com/karmorak/lib/assets/scrollable/circle_slider.png";

	protected final boolean IS_VERTICAL;

	private final Vector4i background_bounds;
	private final Vector4i slider_bounds_2;
	
	private float value, slider_size = 0.5f, background_scale = 1f;
	
	protected boolean changeName;
	
	static float lastX;
	static float lastY;

	private Texture bg_up, bg_middle, bg_down;
	private Texture slider_up, slider_middle, slider_down;

	private float texture_scale;

	public int[] scaleNearest(int[] src, int w1, int h1, int w2, int h2) {//not used
		int[] dest = new int[w2 * h2];
		double xRatio = (double)w1 / w2;
		double yRatio = (double)h1 / h2;

		for (int y = 0; y < h2; y++) {
			for (int x = 0; x < w2; x++) {
				int px = (int)Math.floor(x * xRatio);
				int py = (int)Math.floor(y * yRatio);
				dest[y * w2 + x] = src[py * w1 + px];
			}
		}
		return dest;
	}

	public Scrollable(String name, boolean is_vertical, float length) {
		this(name, is_vertical, length,length*0.05f);
	}

	public Scrollable(String name, boolean is_vertical, float length, float thickness) {
		super(true);

		background_bounds = new Vector4i();
		slider_bounds_2 = new Vector4i();
		IS_VERTICAL = is_vertical;

		DrawMap map = new DrawMap(KLIB.URL(PATH_NEW_BACKGROUND_TEXTURE));

		if(IS_VERTICAL) {
			background_bounds.setSize(thickness, length);
			slider_bounds_2.setSize((int) thickness, length*slider_size);
			texture_scale = background_bounds.getWidth() / map.getWidth();

			bg_up = new Texture(map, new Vector4i(0, 0, (int) map.getWidth(), (int) (map.getWidth()*0.5f)));
			bg_middle = new Texture(map, new Vector4i(0, (int) (map.getHeight()*0.25f), (int) map.getWidth(), (int) (map.getHeight()*0.25f)));
			bg_down = new Texture(map, new Vector4i(0, (int) (map.getHeight()*0.75f), (int) map.getWidth(), (int) (map.getHeight()*0.25f)));

			map.set(KLIB.URL(PATH_NEW_SLIDER_TEXTURE));

			slider_up = new Texture(map, new Vector4i(0, 0, (int) map.getWidth(), (int) (map.getWidth()*0.5f)));
			slider_middle = new Texture(map, new Vector4i(0, (int) (map.getHeight()*0.25f), (int) map.getWidth(), (int) (map.getHeight()*0.25f)));
			slider_down = new Texture(map, new Vector4i(0, (int) (map.getHeight()*0.75f), (int) map.getWidth(), (int) (map.getHeight()*0.25f)));

			map.destroy();
		} else {
			background_bounds.setSize(length, thickness);
			slider_bounds_2.setSize( length * slider_size, thickness);

			map.rotate90(true);
			texture_scale = background_bounds.getHeight() / map.getHeight();
			int height = (int) map.getHeight();
			int map_width = (int) map.getWidth();

			bg_up = new Texture(map, new Vector4i(0, 0, (int) (map_width * 0.25f), height));
			bg_middle = new Texture(map, new Vector4i( (int) (map_width * 0.25f), 0, (int) (map_width * 0.25f),  height));
			bg_down = new Texture(map, new Vector4i( (int) (map_width * 0.75f), 0, (int) (map_width * 0.25f),  height));

			map.set(KLIB.URL(PATH_NEW_SLIDER_TEXTURE)).rotate90(true);

			slider_up = new Texture(map, new Vector4i(0, 0, (int) (map_width * 0.25f), height));
			slider_middle = new Texture(map, new Vector4i((int) (map_width * 0.25f), 0, (int) (map_width * 0.25f),  height));
			slider_down = new Texture(map, new Vector4i((int) (map_width * 0.75f), 0, (int) (map.getWidth() * 0.25f),  height));

			map.destroy();
		}

		
		
		
		list.add(getID(), this);
		scrols.add(this);
		text.setName("");
		updateName();
		setInteractable(false);
		updateBackgroundSize();
		updateScale();
		updateBackgroundPosition();
	}
	
	
	@Override
	public void setScale(float scale) {
		text.setScale(text.getScale() * scale);
		background_scale = scale;
		updateScale();
	}
	
	public void setTextScale(float scale) {
		text.setScale(scale * background_scale);
	}

	public boolean isDragged() {
        return drag_button == this;
    }
	
	@Override
	public void draw(MasterRenderer renderer) {		
		draw(renderer, 0);
	}
	
	@Override
	public void draw(MasterRenderer renderer, int layer) {		
		if(show) {
			renderer.processTexture(bg_up, layer);
			renderer.processTexture(bg_middle, layer);
			renderer.processTexture(bg_down, layer);
			renderer.processTexture(slider_up, layer);
			renderer.processTexture(slider_middle, layer);
			renderer.processTexture(slider_down, layer);
		}
		super.draw(renderer, layer+1);
	}

	@Override
	protected boolean isColliding() {

		int mouse_pos_x = (int) Input.mouse.getX();
		int mouse_pos_y = (int) Input.mouse.getY();

		float button_pos_x = getX();
		float button_pos_y = getY();


		if(IS_VERTICAL) {
			if(mouse_pos_x > button_pos_x &&
					mouse_pos_x < button_pos_x + getWidth() &&
					mouse_pos_y < button_pos_y &&
					mouse_pos_y > button_pos_y - getHeight()) {
				drag_button = this;
				return true;
			}
		} else {
			if(mouse_pos_x > button_pos_x &&
					mouse_pos_x < button_pos_x + getWidth() &&
					mouse_pos_y > button_pos_y &&
					mouse_pos_y < button_pos_y + getHeight()) {
				drag_button = this;
				return true;
			}
		}


		return false;
	}

	public boolean isSliderSelected() {

		int mouse_pos_x = (int) Input.mouse.getX();
		int mouse_pos_y = (int) Input.mouse.getY();

		float button_pos_x = slider_bounds_2.getX();
		float button_pos_y = slider_bounds_2.getY();


		if(IS_VERTICAL) {
			if(mouse_pos_x > button_pos_x &&
					mouse_pos_x < button_pos_x + getSliderWidth() &&
					mouse_pos_y < button_pos_y &&
					mouse_pos_y > button_pos_y - getSliderHeight()) {
				drag_button = this;
				return true;
			}
		} else {
			if(mouse_pos_x > button_pos_x &&
					mouse_pos_x < button_pos_x + getSliderWidth() &&
					mouse_pos_y > button_pos_y &&
					mouse_pos_y < button_pos_y + getSliderHeight()) {
				drag_button = this;
				return true;
			}
		}

		return false;
	}
	
	public boolean isSliderSelected(boolean ask) {
		if(show) {
			if(ask) {
				isSliderSelected();
			} else {
				if(isSelectable()) {
					return selected_Buttons.contains(this);
				} else {
					isSliderSelected();
				}
			}
		}
		return false;
	}
	
	public float getValue() {
		return value;
	}
	
	
	
	
	public void scroll(double amt) {
		double t = value + amt;
		
		if(t <  0f) t = 0;
		else if (t > 1f) t = 1;
		
		setValue((float) t);
	}

	@Override
	public Vector2 getPosition() {
		return new Vector2(background_bounds.getPosition());
	}

	@Override
	public float getX() {
		return background_bounds.getX();
	}

	@Override
	public float getY() {
		return background_bounds.getY();
	}

	@Override
	public float getWidth() {
		return background_bounds.getWidth();
	}

	@Override
	public float getHeight() {
		return background_bounds.getHeight();
	}



	private void updateSliderPosition() {
		if(IS_VERTICAL) {
			int slider_y = (int) (background_bounds.getHeight() * (1f-slider_size) * value);
			slider_bounds_2.setPosition(getX(), getY() - slider_y);

			slider_up.setPosition(getX(), getY() - slider_y);
			slider_middle.setPosition(getX(), getY() - slider_middle.getHeight() - slider_y);
			slider_down.setPosition(getX(), getY() -  slider_down.getHeight() -  slider_middle.getHeight() - slider_y);
		} else {//Horizontal
			int slider_x = (int) (background_bounds.getWidth() * (1f-slider_size) * value);
			slider_bounds_2.setPosition(getX() + slider_x, getY());

			slider_up.setPosition(getX() + slider_x, getY());
			slider_middle.setPosition(getX()  + slider_up.getWidth() +slider_x, getY());
			slider_down.setPosition(getX() + slider_middle.getWidth() + slider_up.getWidth() + slider_x, getY());
		}
	}

	private void updateSliderSize() {
		if(IS_VERTICAL) {
			slider_bounds_2.setHeight((int) (background_bounds.getHeight()*slider_size));

			slider_middle.setSize(slider_bounds_2.getWidth(), slider_bounds_2.getHeight());
			slider_middle.setPosition(getX(), getY() - slider_middle.getHeight());

			slider_down.setPosition(getX(), getY() -  slider_down.getHeight() -  slider_middle.getHeight());
		} else {
			slider_bounds_2.setWidth((int) (background_bounds.getWidth()  * slider_size));

			slider_middle.setSize(slider_bounds_2.getWidth(), slider_bounds_2.getHeight());
			slider_middle.setPosition(getX() + slider_up.getWidth(), getY());

			slider_down.setPosition(getX() + slider_middle.getWidth() + slider_up.getWidth(), getY());
		}
	}

	private void updateBackgroundPosition() {
		if(IS_VERTICAL) {
			bg_up.setPosition(getX(), getY());
			bg_middle.setPosition(getX(), getY() - bg_middle.getHeight());
			bg_down.setPosition(getX(), getY() -  bg_down.getHeight() -  bg_middle.getHeight());
		} else {

			bg_up.setPosition(getX(), getY());
			bg_middle.setPosition(getX()  + bg_up.getWidth(), getY());
			bg_down.setPosition(getX() + bg_up.getWidth() + bg_middle.getWidth(), getY());
		}
		updateSliderPosition();
		if(changeName) updateName();
	}

	private void updateBackgroundSize() {


		if(IS_VERTICAL) {
			bg_up.setPosition(getX(), getY());

			bg_middle.setSize(bg_middle.getWidth() * texture_scale, background_bounds.getHeight());
			bg_middle.setPosition(getX(), getY() - bg_middle.getHeight());

			bg_down.setPosition(getX(), getY() -  bg_down.getHeight() -  bg_middle.getHeight());

		} else {
			bg_up.setPosition(getX(), getY());

			bg_middle.setSize(background_bounds.getWidth(), bg_middle.getHeight() * texture_scale);
			bg_middle.setPosition(getX()  + bg_up.getWidth(), getY());

			bg_down.setPosition(getX() + bg_up.getWidth() + bg_middle.getWidth(), getY());
		}

		updateSliderSize();
	}

	private void updateScale() {

		if(IS_VERTICAL) {
			bg_up.setScale(texture_scale * background_scale);
			bg_middle.setScale(background_scale);
			bg_middle.setSize(background_bounds.getWidth(), background_bounds.getHeight());
			bg_down.setScale(texture_scale * background_scale);

			slider_up.setScale(texture_scale*background_scale);
			slider_middle.setScale(background_scale);
			slider_middle.setSize(background_bounds.getWidth(), slider_bounds_2.getHeight());
			slider_down.setScale(texture_scale*background_scale);

		} else {
			bg_up.setScale(texture_scale * background_scale);
			bg_middle.setScale(background_scale);
			bg_middle.setSize(background_bounds.getWidth(), background_bounds.getHeight());
			bg_down.setScale(texture_scale * background_scale);

			slider_up.setScale(texture_scale * background_scale);
			slider_middle.setScale(background_scale);
			slider_middle.setSize(slider_bounds_2.getWidth(), slider_bounds_2.getHeight() );
			slider_down.setScale(texture_scale * background_scale);
		}

		updateBackgroundPosition();
	}

	public Scrollable setValue(float value) {
		if(value < 0f) value = 0f;
		else if(value > 1f) value = 1f;
		this.value = value;

		if(changeName) updateName();

		updateSliderPosition();
		return this;
	}

	
	public Scrollable setSliderSize(float size) {
		if(size < 0.01f) size = 0.01f;
		else if(size > 1f) size = 1f;

		slider_size = size;
		updateSliderSize();
		return this;
	}

	@Deprecated
	public void setSliderSize(float x_bound, float y_bound) {
//		slider_bounds.setSize((int)x_bound,(int) y_bound);
//		updateSlider();
	}

	float getSliderWidth() {
		return slider_up.getWidth() + slider_middle.getWidth() + slider_down.getWidth();
	}
	
	float getSliderHeight() {
		return slider_up.getHeight() + slider_middle.getHeight() + slider_down.getHeight();
	}

	public Vector2i getSliderPos() {
		return slider_bounds_2.getPosition();
	}
	
	public void dochangeName(boolean bool) {
		changeName = bool;
	}
	
//	public void setBackground(Texture s) {
//		background = new Texture(s.getPATH());
//	}

	public Button setName(String name) { return this; }
	public Button setName(String[] name) {	return this;	}
	
	public Button setName(int index, String name) { return this; }

	public void updateName() {
		if(changeName) {
			String value_s = "" + value;
			String name;
			String[] name2 = value_s.split("\\.", 2);

			if(name2[1].length() > 2) {
				name = name2[0] + "." + name2[1].substring(0, 2);
			} else {
				name = name2[0] + "." + name2[1];
			}

			text.setName(name);

			if(IS_VERTICAL) {
				text.setWidth((int) getWidth());
				text.setPosition(getX(), getY() - (getHeight() - text.getHeight()) * 0.5f);
			} else {
				text.setHeight((int) getHeight());
				text.setPosition(getX() + (getWidth() - text.getWidth()) * 0.5f, getY());
			}
		}
	}

	@Override
	public void setX(float x) {
		background_bounds.setX(x);
		updateBackgroundPosition();
	}
	
	@Override
	public void setY(float y) {
		background_bounds.setY(y);
		updateBackgroundPosition();
	}
	
	@Override
	public Button setPosition(Vector2 pos) {
		background_bounds.setPosition(pos);
		updateBackgroundPosition();
		return this;		
	}

	public Button setPosition(int x, int y) {
		background_bounds.setPosition(x, y);
		updateBackgroundPosition();
		return this;
	}

	@Override
	public Button setPosition(float x, float y) {
		background_bounds.setPosition(x, y);
		updateBackgroundPosition();
		return this;		
	}

	public static void touchDown(int screenX, int screenY) {
		lastX = 0;
		lastY = 0;
	}
	
	public static boolean touchDragged(int screenX, int screenY) { //TODO

		float speed_x = Math.abs((lastX - screenX));
		float speed_y = Math.abs((lastY - screenY));		
		if(lastX == 0 && lastY == 0) {
			speed_x = 0;
			speed_y = 0;
		}
		
		if(drag_button != null) {
			Scrollable c = (Scrollable) drag_button;
			
			if(c.IS_VERTICAL) {				
				if(lastY > screenY) {
					c.value += (speed_y / c.getHeight());
				} else {
					c.value -= (speed_y / c.getHeight());
				}
				lastY = screenY;
			} else {
				
				if(lastX > screenX) {
					c.value -= (speed_x / c.getWidth());
				} else {
					c.value += (speed_x / c.getWidth());
				}
				lastX = screenX;
			}

			if(c.value > 1f) c.value =1f;
			if(c.value < 0) c.value = 0f;

			if(c.changeName) c.updateName();
			c.updateSliderPosition();
		}
		return false;
	}

	@Override
	public Button setMiddle() {
		super.setMiddle(true, false);
		updateBackgroundPosition();
		return this;
	}

	@Override
	public Button setMiddle(boolean x, boolean y) {
		super.setMiddle(x, y);
		updateBackgroundPosition();
		return this;
	}

	public static void MouseUp() {
		lastX = 0;
		lastY = 0;
	}
	

}
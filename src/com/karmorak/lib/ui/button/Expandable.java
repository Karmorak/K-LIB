//v1.3.0
package com.karmorak.lib.ui.button;

import java.util.ArrayList;

import com.karmorak.lib.ColorPreset;
import com.karmorak.lib.Input;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.ui.button.events.Button_onTouchDown_Event;

public class Expandable extends Button implements Button_onTouchDown_Event {

	//make it a setting in which direction it expands

//	private static final String BACKGROUND_PATH = "/com/karmorak/lib/assets/button_background.png";

	//private ArrayList<Button> options = new ArrayList<>();
	private float options_scale = 0;
	private static final int options_abs_top = 48;
	private static final int options_abs = 32;
	private static final int options_abs_side = 52;
	private static final float fade_value = 8f;

	public Scrollable scrollbar;
	
	private final Texture background;
	private final Vector2 max_bg_bounds;
	private final Vector2 cur_bg_bounds;
	public int expand_state = -1;
	
	private float options_height = 0;
	
	
	public Expandable(Vector2 pos, Vector2 max_expand_size) {
		super("§§triple-dot", pos);
		
		Option_Set_LineSpacing(-4 * getScale());
		show = true;

		background = new Texture((int) max_expand_size.getWidth(), (int) max_expand_size.getHeight(), ColorPreset.LIGHT_GRAY);

		exp_list.add(this);
		max_bg_bounds = new Vector2(max_expand_size.getX(), max_expand_size.getY());
		cur_bg_bounds = new Vector2(0, 0);
		can_select = true;
		option_unselect_selected = true;
		option_keep_on_click_outside = false;

		events.put(this, this);
	}
	

	public void scroll() {
		
	}
	
	public void showHangs(boolean bool) {
		for(Hang c : hangs) {
			c.show(bool);
		}
	}
	
	@Override
	public void show(boolean bool) {
		show = bool;

			for(Hang c : hangs) {
				c.show(bool);
			}
	}
	
	
	public void addOption(String name) {
		
		Hang h = addHang(name);
		h.show(false);
		
		if(options_scale > 0) h.setScale(options_scale);
		else h.setScale(this.getScale() * 0.5f);
		
		h.setMaxTextWidth(max_bg_bounds.getWidth());
	
		float y = options_abs_top;
		
		for(Hang c : hangs) {
			y = y + c.getHeight() + options_abs; 
		}
		options_height = y - hangs.getFirst().getHeight();
		
		if(y > max_bg_bounds.getHeight()) {
			if(scrollbar == null) {
				scrollbar = new Scrollable("1.0", true, max_bg_bounds.getHeight() - getHeight(), 20);
				scrollbar.setPosition(getPosition().getX() + getWidth()- scrollbar.getWidth() -3, getPosition().getY() + getHeight() -max_bg_bounds.getHeight() + 3);
				scrollbar.setSliderSize(scrollbar.getSliderWidth(), scrollbar.getHeight() * (scrollbar.getHeight() / y));
				scrollbar.setValue(1f);
				scrollbar.dochangeName(false);
				scrollbar.setName(" ");
			} else {
				scrollbar.setSliderSize(scrollbar.getSliderWidth(), scrollbar.getHeight() * (scrollbar.getHeight() / y));
				scrollbar.setPosition(getPosition().getX() + getWidth()- scrollbar.getWidth() -3, getPosition().getY() + getHeight() -max_bg_bounds.getHeight() + 3);
				scrollbar.setValue(1f);
			}
		} 
	}

	public Hang getOption(int i) {
		if(i >= hangs.size()) return null;
		
		
		return getHang(i);
	}
	
	public boolean isOptionSelected(int i) {
        return getHang(i) == hovered_Button;
    }
	
	public void setOptionsScale(float scale) {
		if(scale <=  0)
			options_scale  = 0;
		else
			options_scale = scale;
		for(Hang c : hangs) c.setScale(options_scale);
	}
	
	public static ArrayList<Expandable> open_expandables = new ArrayList<Expandable>();



	@Override
	protected boolean isColliding() {
		float mouse_x = Input.mouse.getX();
		float mouse_y = Input.mouse.getY();

		float button_x = getPosition().getX();
		float button_y = getPosition().getY();;

		float width = getWidth();
		float height = getHeight();

		if (getMaxTextWidth() > 0) width = getMaxTextWidth();

		if(expand_state == 1) {
			button_y = getPosition().getY()-max_bg_bounds.getHeight()+getHeight();
			width = max_bg_bounds.getWidth();
			height = max_bg_bounds.getHeight();
		}


		return mouse_x > button_x &&
				mouse_x < button_x + width &&
				mouse_y > button_y &&
				mouse_y < button_y + height;
	}

	public static void trigger() {
		for(Expandable e : exp_list) {
				if(!e.isColliding()) {
					e.showHangs(false);
					e.expand_state = -1;
					e.cur_bg_bounds.setX(0);
					e.cur_bg_bounds.setY(0);
					open_expandables.remove(e);
				}

		}
	}
	@Override
	public void onTouchDown() {
		expand_state = 0;
		showHangs(false);
		if(!open_expandables.contains(this))	open_expandables.add(this);
	}

	public void update() {
		if(show) { 
			if(expand_state == 0) {
				cur_bg_bounds.setWidth(cur_bg_bounds.getWidth() + fade_value);
				cur_bg_bounds.setHeight(cur_bg_bounds.getHeight() + fade_value);
				//for(Button b : hangs) b.setMaxTextWidth(cur_bg_bounds.getWidth());

				if(cur_bg_bounds.getX() >= max_bg_bounds.getX() && cur_bg_bounds.getY() >= max_bg_bounds.getY()) {
					cur_bg_bounds.setX(max_bg_bounds.getX());
					cur_bg_bounds.setY(max_bg_bounds.getY());
					expand_state = 1;
					showHangs(true);

					float x = getPosition().getX() + getWidth() + (options_abs_side * this.getScale());
					float y = getPosition().getY() - (options_abs_top * this.getScale());

					float ay = 0;
					if(scrollbar != null)
						ay = (max_bg_bounds.getHeight() - options_height) * (1- scrollbar.getValue());
					y = y + ay;

					for (Hang c : hangs) {
						float height = c.getHeight();

						c.setPosition(x, y);

						if (y + c.getHeight() > getPosition().getY() + getHeight()) {
							c.show(false);
						} else if (y < getPosition().getY() - max_bg_bounds.getHeight() + getHeight()) {
                            c.show(y + c.getHeight() * 0.2 > getPosition().getY() - max_bg_bounds.getHeight() + getHeight());
						} else c.show(true);


						y = y - height - (options_abs * this.getScale());

					}

					if(scrollbar != null) {
						scrollbar.setPosition(getPosition().getX() + getWidth()- scrollbar.getWidth() -3, getPosition().getY() + getHeight() -max_bg_bounds.getHeight() + 3);
					}



				}
			}
		}			
	}

	@Override
	public void draw(MasterRenderer renderer) {
		draw(renderer, 0);
	}

	@Override
	public void draw(MasterRenderer renderer, int layer) {
		if(show) {
				if(hovered_Button == this && can_hover)
					text.setColor(hoverColor);
				else if(isSelected() && can_select)
					text.setColor(selectColor);
				else
					text.setColor(originColor);

				if(expand_state == -1) {
					text.draw(renderer, layer);
				} else if(expand_state == 0) {
					renderer.processTexture(background, getPosition().getX(), getPosition().getY() - cur_bg_bounds.getHeight() + getHeight(), cur_bg_bounds.getX(), cur_bg_bounds.getY(), layer+1);
				} else if (expand_state == 1) {
					renderer.processTexture(background, getPosition().getX(), getPosition().getY() - max_bg_bounds.getHeight() + getHeight(), max_bg_bounds.getX(), max_bg_bounds.getY(), layer);

					for(Hang h : hangs) {
						h.draw(renderer, layer + 1);
					}

					if(scrollbar != null)
						scrollbar.draw(renderer);

			}
		}
	}
	
	@Override
	public void setInteractable(boolean bool) {}	
	@Override
	public void setSelectable(boolean bool) {}	
	@Override
	public void setHoverable(boolean bool) {}
	@Override
	public Button setName(String name) {
		return this;
	}


	@Override
	public void setX(float x) {
		if(scrollbar !=null) scrollbar.setPosition(x + getWidth()- scrollbar.getWidth() -3, getPosition().getY() + getHeight() -max_bg_bounds.getHeight() + 3);
		super.setX(x);
	}
	
	@Override
	public void setY(float y) {
		if(scrollbar !=null) scrollbar.setPosition(getPosition().getX() + getWidth()- scrollbar.getWidth() -3, y + getHeight() -max_bg_bounds.getHeight() + 3);
		super.setY(y);
	}
	
	
	@Override
	public Button setPosition(Vector2 pos) {
		if(scrollbar !=null) scrollbar.setPosition(pos.getX() + getWidth()- scrollbar.getWidth() -3, pos.getY() + getHeight() -max_bg_bounds.getHeight() + 3);
		return super.setPosition(pos);
	}
	
	@Override
	public Button setPosition(float x, float y) {
		// TODO Auto-generated method stub		
		if(scrollbar !=null) scrollbar.setPosition(x + getWidth()- scrollbar.getWidth() -3, y + getHeight() -max_bg_bounds.getHeight() + 3);
		return super.setPosition(x, y);
	}


}
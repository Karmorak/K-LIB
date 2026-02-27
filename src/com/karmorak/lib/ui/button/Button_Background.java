//v2
package com.karmorak.lib.ui.button;

import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.utils.GraphicUtils;

public class Button_Background {
	
	/* the positioning and size shoudl work better now
	 * removed the relative position option
	 * background will now be drawn automatically have to disable it if you want to draw manually before it was the opposite
	 * 
	 * 
	 */
	
	static final Texture DEF_BACKGROUND = new Texture(KLIB.URL("/com/karmorak/lib/assets/button_background.png"));

	private Button bound;
	public Texture texture;
	protected boolean bg_show, bg_relative;
	@SuppressWarnings("unused")
	private Vector2 bg_pos;
	@SuppressWarnings("unused")
	private Vector2 bounds;
	private Vector2 bg_offset;
	
	public Button_Background(Button bound) {
		this.bound = bound;
		texture = DEF_BACKGROUND;
		bg_show = true;
		bg_pos = new Vector2(0, 0);
		bounds = new Vector2(bound.text.getWholeWidth(), bound.getHeightTotal());
		bg_offset = new Vector2(2, 2);
	}
	
	public void setColor(Color c) {
		texture = GraphicUtils.colorize2Texture(DEF_BACKGROUND.getPATH(), c, true);
	}
	
	public void setPosition(Vector2 pos) {
		bg_pos = new Vector2(pos.getX(), pos.getY());
	}
	
	public void show(boolean bool) {
		bg_show = bool;
	}	
	public void setOffset(float offset) {
		bg_offset.setPosition(offset, offset);
	}
	
	public void setOffset(float offsetX, float offsetY) {
		bg_offset.setPosition(offsetX, offsetY);
	}
	
	public void setXOffset(float offset) {
		bg_offset.setX(offset);
	}
	public void setYOffset(float offset) {
		bg_offset.setY(offset);
	}
	
	public Vector2 getBGPosition() {
		return new Vector2(bound.getPosition().getX() - bg_offset.getX(), bound.getPosition().getY() - bg_offset.getY() + bound.text.getYOffsetDown(0));
	}
	
	public Vector2 getBGBounds() {
		return bounds = new Vector2(bound.text.getWholeWidth() + bg_offset.getX()*2, bound.text.getWholeHeight(0) + bg_offset.getY()*2);
	}
	
	public void draw(MasterRenderer renderer,int layer) {
		if(bound.show)
			renderer.processTexture(texture, getBGPosition(), getBGBounds(), layer);
	}
	
}

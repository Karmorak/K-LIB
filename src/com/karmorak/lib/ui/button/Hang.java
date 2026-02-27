//v 1.2
package com.karmorak.lib.ui.button;

import com.karmorak.lib.Color;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.font.OwnFont;
import com.karmorak.lib.math.Vector2;

public class Hang extends Button {
	
	protected int place = -1;
	
	private Vector2 relPosition;
	//private boolean isrelative;

	private final Button father;
	
	Hang() {
		super(">");
		this.father = null;
	}

	
	Hang(Button father) {		
		super(father != null ? father.getName() : " ", father != null ? father.font : DEF_FONT);			
		this.father = father;
		if(father != null)
			father.hangs.add(this);
		
		relPosition = new Vector2(0, 0);
	}

	Hang(Button father, String name) {
		super(name, father != null ? father.font : DEF_FONT);
		this.father = father;
		if(father != null)
			father.hangs.add(this);

		relPosition = new Vector2(0, 0);
	}

	
	Hang(Button father, boolean isRelative) {
		super(father.getName(), father.font);		
		if(isRelative) {
			this.father = father;
			father.hangs.add(this);
		} else 
			this.father = null;
		relPosition = new Vector2(0, 0);
	}
	
	
	Hang(OwnFont font, String name, Vector2 relpos, Color c, Button father) {
		super(name, font);
		list.add(this);
		this.font = font;
		this.setName(name);
		this.setColor(c);
		old_name = getNames();		
		show = true;		
		this.father = father;
		can_hover = father.can_hover;
		can_select = father.can_select;
		father.hangs.add(this);
		
		this.text.setPosition(father.getX() + relpos.getX(), father.getX() +  relpos.getY());
		relPosition = new Vector2(relpos.getX(), relpos.getY());
		this.setScale(father.getScale());


		place = father.hangs.size()-1;
	}
	
		
	
	@Override
	public Button setMiddle() {
		setRelPosition((father.getWidth() - getWidth()) * 0.5f, - getHeight());
		return this;		
	}
	
	public Button setMiddle(float spaceY) {
		setRelPosition((father.getWidth() - getWidth()) * 0.5f, - (getHeight() + spaceY));
		return this;		
	}

	@Override
	public Button setPosition(Vector2 pos) {
		// TODO Auto-generated method stub
		return super.setPosition(pos);
	}
	
	
	@Override
	public Button setPosition(float x, float y) {
		return super.setPosition(x, y);
	}
	
	@Override
	public void setPosition(int hang, Vector2 pos) {
		// TODO Auto-generated method stub
		super.setPosition(hang, pos);
	}
	
	@Override
	public void setPosition(int hang, float x, float y) {
		// TODO Auto-generated method stub
		super.setPosition(hang, x, y);
	}
	

	
	
	public Button setRelPosition(Vector2 relpos) {
		return setRelPosition(relpos.getX(), relpos.getY());
	}
	
	public Button setRelPosition(float rel_x, float rel_y) {
		relPosition.set(rel_x, rel_y);
		this.text.setPosition(father.text.getTotalPosition().getX() + rel_x, father.text.getTotalPosition().getY() + rel_y);
		return this;
	}
	
	public Vector2 getRelPosition() {
		return relPosition;
	}

	//	public void updatePosition() {
//		text.setPosition(getButton(father).getPosition().getX() + getX(), getButton(father).getPosition().getY() + getY());		
//	}	
	
	@Override
	public Hang addHang(String name) {
		return this;
	}
	@Override
	public Hang addHang(String name, Color color) {
		return this;
	}
	
	@Override
	public Hang addHang(String name, float x, float y) {
		return this;
	}
	
	@Override
	public Hang addHang(String name, float x, float y, Color color) {
		return this;
	}
	
	@Override
	public Hang addHang(String name, Vector2 pos) {
		return this;
	}
	
	@Override
	public Hang addHang(String name, Vector2 pos, Color color) {
		return this;
	}
	
	
	@Override
	public void draw(MasterRenderer renderer) {
		if(show) {
			
			if(hovered_Button == this && can_hover)
				text.setColor(hoverColor);			
			else if(isSelected() && can_select)
				text.setColor(selectColor);
			else
				text.setColor(originColor);		
			
			text.draw(renderer);	
		}
	}



}
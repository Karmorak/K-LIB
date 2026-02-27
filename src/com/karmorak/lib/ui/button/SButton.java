//v 0.1
package com.karmorak.lib.ui.button;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.karmorak.lib.Color;
import com.karmorak.lib.Input;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.font.OwnFont;
import com.karmorak.lib.font.Text;
import com.karmorak.lib.gamestate.GSM;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.prototype.AtlasPacker;
import com.karmorak.lib.prototype.TextureAtlas;
import com.karmorak.lib.utils.GraphicUtils;

public class SButton implements Comparable<SButton>  {
	
			
	static final ArrayList<SButton> list = new ArrayList<>();
	
	protected static boolean first_init = false;
	public static boolean check_gamestate = true;
	protected static final Color defColor = Color.BLACK();
	protected static int max_id;
	protected static int hovered_Button = -1;
	protected static int selected_Button = -1;
	protected static int dragg_button = -1;	
	
	protected int ID = 0;
	public int associated_state = -1;
	
	protected boolean show = true;
	protected boolean can_hover = true;
	protected boolean can_select = true;	
		
	private final Texture texture;
	private final String[] name;
	private final int abs_y;
	
//	protected Vector2 pos;
	protected String hoverText;
	protected String selectText;
	
	protected Color originColor;
	protected Color hoverColor;
	protected Color selectColor;	
	
//	private String method = "";	
//	private Class[] signature;
//	private Object[] parameters;
//	private Class targetClass;
//	private Object target;
		
	public SButton (String name) {		
		this(new String[] {name}, "", "", "", new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, Button.DEF_FONT, defColor, false);
	}
	
	public SButton (String name, String save_name, String atlas_name) {
		this(new String[] {name}, save_name, atlas_name, "",  new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, Button.DEF_FONT, defColor, true);
	}
	
	
	public SButton (String name, String save_name, String atlas_name, String save_path, boolean save) {
		this(new String[] {name}, save_name, atlas_name, save_path, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, Button.DEF_FONT, defColor, save);
	}
	
//	public SButton (String name, Vector2 pos) {
//		this(new String[] {name}, pos, 1f, Button.DEF_FONT, defColor);
//	}
//	
//	public SButton (String name, OwnFont font) {
//		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, font, defColor);
//	}
//	
//	public SButton (String name, float scale, Color c) {		
//		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), scale, Button.DEF_FONT, c);
//	}
//	
//	public SButton (String name, Vector2 pos, OwnFont font) {
//		this(new String[] {name}, pos, 1f, font, defColor);
//	}
//	
//	public SButton(String name, float scale, OwnFont font, Color c) {		
//		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), scale, font, c);
//	}
//	
//	public SButton (String[] name) {		
//		this(name, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, Button.DEF_FONT, defColor);
//	}
//
//	public SButton (String[] name, Vector2 pos) {
//		this(name, pos, 1f, Button.DEF_FONT, defColor);
//	}	
//	
//	public SButton (String[] name, OwnFont font) {
//		this(name, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, font, defColor);
//	}	
//	
//	public SButton (String name[], float scale, Color c) {			
//		this(name, new Vector2(0, KLIB.graphic.Height() * 0.5f), scale, Button.DEF_FONT, c);
//	}
//	
//	public SButton (String[] name, Vector2 pos, OwnFont font) {
//		this(name, pos, 1f, font, defColor);
//	}
	
	static HashMap<String, AtlasPacker> packers = new HashMap<String, AtlasPacker>();
	static HashMap<String, TextureAtlas> atlases = new HashMap<String, TextureAtlas>();
	
	
	SButton (String name[], String id, String atlas_name, String save_path, Vector2 position, float scale, OwnFont font, Color c, boolean save) {		
		if(!first_init) {
			first_init = true;
			}
		
		list.add(this);	
		
		if(ID == 0) {
			ID = max_id;		
			max_id++;
			associated_state = GSM.getStateInt();
		}		
		
		hoverColor = Color.RED();
		selectColor = Color.CYAN();
					
		hoverText = "";
		selectText = "";		
					
		Text t = new Text(font, name);		
		abs_y = t.getAbs(name.length-1);
				
		if(save) {
			
			String save_name = "";		
			// \ / : * ? " | < >
			if(id != "") save_name = id;
			else save_name = name[0].replace("\\", "").replace("/", "").replace("?", "").replace("\"", "");
						
			String path;
			if(save_path == "") path = KLIB.lib.LIB_TEMP_PATH();
			else path = save_path;
					
			if(atlas_name == "") {
				File f = new File(path + save_name + ".png");
				
				if(f.exists())  {		
					texture = new Texture(KLIB.URL(path + save_name + ".png"));
					texture.create();
				} else {	
				
					DrawMap dm = t.toTexture(c);
					
					try {			
						GraphicUtils.saveDrawMap(dm, path + save_name + ".png");				
					} catch (IOException e) {
						System.out.println(name[0]);
						e.printStackTrace();
					}		
					texture = new Texture(dm);
							
				}
			} else {
				if(!packers.containsKey(path + atlas_name)) {				
					File f = new File(path + atlas_name + ".png");
					if(f.exists()) {
						TextureAtlas atlas;
						if(atlases.containsKey(path + atlas_name)) {
							atlas = atlases.get(path + atlas_name);
						} else {
							atlas = new TextureAtlas(path, atlas_name);
							atlases.put(path + atlas_name, atlas);
						}
								
						if(atlas.getTexture(save_name) == null) {
							texture = new Texture(t.toTexture(c));
						} else texture = atlas.getTexture(save_name);		// <-- if null
					} else {
						DrawMap dm = t.toTexture(c);
						texture = new Texture(dm);	
						
						AtlasPacker packer = new AtlasPacker(path, atlas_name);
						packer.addTexture(dm, save_name);	
						packers.put(path + atlas_name, packer);
					}							
				} else {
					DrawMap dm = t.toTexture(c);	
					texture = new Texture(dm);	
					
					AtlasPacker packer = packers.get(path + atlas_name);
					packer.addTexture(dm, save_name);
				}	
			}
			
			
		} else {
			texture = new Texture(t.toTexture(c));
		}
		
		this.name = name;	
		
		
		setColor(c);
		setScale(scale);
		setPosition(position);			
		
	}
	

	public static void saveSButton() {		
		for (String key : packers.keySet()) {
		   packers.get(key).saveAtlas();
		}
	}
	
//	public void setWritable(int hang,int maxChars, int writemode) {
//		Hang h = hangs.get(hang);	
//		Writable w = new Writable(h, maxChars);
////		w.setSelectColor(new Color(0, 1f, 1f, 1f));
//		
//		hangs.set(hang, w);
//		setHangRight(0);
//	}
	
	public boolean isHoverable() {
		return can_hover;
	}
	public boolean isSelectable() {
		return can_select;
	}
	
	public void dispose() {
//		pos = null;
		ID = 0;
		removeButton(this);
		cleanIDs();		
	}
	
	public float maxX() {
		return getX() + getWidth();
	}
	
	public float maxY() {
		return getY() + getHeight();
	}
	
	public void show(boolean bool) {
		show = bool;
	}
	public boolean getshow() {
		return show;
	}
	
//	public void initBackground() {
//		background = new Background(this);
//	}
//	
//	public Background getBackground() {
//		return background;
//	}
	
	public void setX(float x) {
		setPosition(x, getY());
	}
	
	public void setY(float y) {
		setPosition(getX(), y);
	}
	
	public SButton setPosition(Vector2 pos) {		
		return setPosition(pos.getX(), pos.getY());
	}
	
	public SButton setPosition(float x, float y) {
		texture.setPosition(x, y);
		return this;
	}
	
	public Vector2 getPosition() {		
		return texture.getPosition();		
	}
	
	public float getX() {
		return getPosition().getX();
	}
	
	public float getY() {
		return getPosition().getY();
	}
	
	public float getAbsY() {
		return abs_y * texture.getScale();
	}	
	
	public float getRight() {
		return getX() + getWidth();
	}
	
	public float getTop() {
		return getY() + getHeight();
	}
	
		
	public SButton setMiddle() {
		setPosition((KLIB.graphic.Width() - getWidth()) * 0.5f, (KLIB.graphic.Height() - getHeight()) * 0.5f);
		return this;
	}
	
	public String getName() {		
		return this.name[0];
	}
	public String[] getNames() {		
		return this.name;
	}
	public String getName(int index) {		
		return this.name[index];
	}
	
	public void setHoverable(boolean bool) {
		can_hover = bool;
	}
	public void setSelectable(boolean bool) {
		can_select = bool;
	}
	
	public void setInteractable(boolean bool) {
		can_hover = bool;
		can_select = bool;
	}
	
	public SButton setColor(Color color) {
		texture.setColor(color);
		originColor = color;
		return this;
	}
	
	public Color getColor() {
		return originColor;
	}
	
	public SButton setHoverColor(Color color) {
		hoverColor = color;
		return this;
	}
	
	public SButton setSelectColor(Color color) {
		selectColor = color;
		return this;
	}	
	
	public void setHoverText(String text) {
		hoverText = text;
	}
	
	public void setSelectText(String text) {
		selectText = text;
	}
	
	
	
	public float getWidth() {
		return texture.getWidth();
	}
	
//	public IButton setHeight(float height) {
//		text.setHeight(height);
//		return this;
//	}
	
	public float getHeight() {		
		return texture.getHeight();
	}
	
	
	public static SButton getButton(int ID) {
		for(int i = 0; i < list.size(); i++) {
			SButton b = list.get(i);
			if(b.getID() == ID) {
				return b;
			}
		}
		return null;
	}
	
	public int getID() {
		return ID;
	}

	
	public static int getID(SButton b) {
		return b.ID;
	}
	
	public static void cleanIDs() {		
		for(int i = 0; i < list.size(); i++) {
			SButton b = list.get(i);
			b.ID = i;	
		}		
	}
	
	public void setScale(float scale) {
		texture.setScale(scale);
	}
	
	public float getScale() {
		return texture.getScale();
	}
			
	private static void removeButton(SButton b) {		
		list.remove(b);		
		Collections.sort(list);
	}
	
	public static void setSelected(int id) {
		selected_Button = id;
	}
	
	public boolean isSelected(boolean ask) {
		if(show) {
			if(isSelectable())
				if(selected_Button == getID()) return true;
			else {
				if(ask) return isSelected();
			}
		}
		return false;
	}
	
	boolean isSelected() {
		if(show) {
			Vector2 last_mouse_pos = new Vector2(Input.mouse.getX(), Input.mouse.getY());
			int mouse_pos_x = (int) last_mouse_pos.getX();
			int mouse_pos_y = (int) (KLIB.graphic.Height() - last_mouse_pos.getY());
			
			float button_pos_x = getPosition().getX();
			float button_pos_y = getPosition().getY();
			
			
			if(mouse_pos_x > button_pos_x &&
					mouse_pos_x < button_pos_x + getWidth() &&
						mouse_pos_y < button_pos_y + getHeight() &&
							mouse_pos_y > button_pos_y) {
				selected_Button = getID();
				return true;	
			}
		}
		return false;
	}
	
	public boolean isHovered(boolean ask) {
		if(show) {
			if(!isHoverable()) {
				return isHovered();
			} else {
				if(ask) {
					return isHovered();
				} else if (getHovered() == this.getID()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static int setHovered(int ID) {
		hovered_Button = ID;
		return hovered_Button;
	}
	
	public static int getHovered() {
		return hovered_Button;
	}
	
	public static int getSelected() {
		return selected_Button;
	}
	
	boolean isHovered() {
		if(show) {
			
			float mouse_x = Input.mouse.getX();
			float mouse_y = Input.mouse.getY();				
			
			float button_x = getPosition().getX();
			float button_y = getPosition().getY();				
			float width = getWidth();
			float height = getHeight();
			
			if(mouse_x > button_x &&
					mouse_x < button_x + width &&
						mouse_y > button_y &&
							mouse_y < button_y + height) {
				hovered_Button = getID();
				return true;
			}
		}
		return false;
	}
	
	public static int getHoveredButton(ArrayList<SButton> buttons) {		
		if(!check_gamestate) {
			if(buttons != null && buttons.size() > 0) {
				for (SButton c : buttons) {
					if(c.show && c.can_hover) {
						if(c.isHovered()) {
							return c.getID();
						}
					}
				}
			}
		} else {
			if(buttons != null && buttons.size() > 0) {
				for (SButton c : buttons) {
					if(c.show && c.can_hover && GSM.getStateInt() == c.associated_state) {
						if(c.isHovered()) {
							return c.getID();
						}
					}
				}
			}
		}
		return -1;		
	}
	
	public void draw(MasterRenderer renderer) {			
		draw(renderer, 0);
	}
	
	public void draw(MasterRenderer renderer, int layer) {	
//		was_drawn = false;
		if(show) {			
			if(getX() + getWidth() <= KLIB.graphic.Width() && getX() + getWidth() >= 0 && getY()+getHeight() >= 0 && getY() + getHeight() <= KLIB.graphic.Height()) {				
				if(hovered_Button == this.getID() && can_hover)
					texture.setColor(hoverColor);			
				else if(selected_Button == this.getID() && can_select)
					texture.setColor(selectColor);
				else
					texture.setColor(originColor);			
				
//				if(background != null && background.bg_show)
//					renderer.processTexture(background.texture, background.getBGPosition(), background.getBGBounds(), layer);
				
				renderer.processTexture(texture, layer);
//				was_drawn = true;
			}	
		}
	}
	
	
	public void draw(MasterRenderer renderer, float x, float y) {			
		draw(renderer, x, y, 0);
	}
	
	public void draw(MasterRenderer renderer, float x, float y, int layer) {			
		if(show) {			
			if(x <= KLIB.graphic.Width() && x + getWidth() >= 0 && y + getHeight() >= 0 && y <= KLIB.graphic.Height()) {			
				if(hovered_Button == this.getID() && can_hover)
					texture.setColor(hoverColor);			
				else if(selected_Button == this.getID() && can_select)
					texture.setColor(selectColor);
				else
					texture.setColor(originColor);			
				
//				if(background != null && background.bg_show)
//					renderer.processTexture(background.texture, background.getBGPosition(), background.getBGBounds(), layer);
				
				renderer.processTexture(texture, x, y, layer);			
			}		
			
		}
	}
	
	public static void touchDown(int screenX, int screenY, int pointer, int button) {
		int var1 = getHoveredButton(list);
		if(var1 != -1) {
			getButton(var1);
		} else {
			hovered_Button = -1;
			selected_Button = -1;
		}		
	}
	
	
	public static void mouseMoved(int screenX, int screenY) {
		int var1 = getHoveredButton(list);
		hovered_Button = var1;
	}	
	
//	public void add(String method, Class[] signature, Object[] parameters, Class targetClass, Object targetClassinstance) {
//		this.signature = signature;
//		this.parameters = parameters;
//		this.method = method;	
//		this.targetClass = targetClass;
//		this.target = targetClassinstance;
//		
//	}
	
//	static void execute(Button b) {
//		try {
//	        @SuppressWarnings("unchecked")
//			Method m = b.targetClass.getDeclaredMethod(b.method, b.signature);
//	        
//	        if (Modifier.isPrivate(m.getModifiers())) {
//	            m.setAccessible(true);
//	        }
//	   
//	        m.invoke(b.target, b.parameters);
//		} catch(NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public int compareTo(SButton o) {
		if (this.getID()<o.getID()) return -1;
		else if (this.getID()>o.getID()) return 1;
		else return 0;
	}

	@SuppressWarnings("unused")
	private static URL URL(String path) {
		return SButton.class.getResource(path);
	}





	
	

}
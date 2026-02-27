//v 1.9.0
package com.karmorak.lib.ui.button;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.karmorak.lib.*;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.font.OwnFont;
import com.karmorak.lib.font.Text;
import com.karmorak.lib.gamestate.GSM;
import com.karmorak.lib.font.OwnFont.FontFilePathHandle;
import com.karmorak.lib.font.Text.Text_Align;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector4;
import com.karmorak.lib.ui.button.events.ButtonEvent;
import com.karmorak.lib.ui.button.events.Button_onKey_Events;
import com.karmorak.lib.ui.button.events.Button_onTouchDown_Event;

public class Button implements Comparable<Button> {
	
	private static final String FONT_PATH = "/com/karmorak/lib/assets/fonts/";
	
	private static final FontFilePathHandle handle = new FontFilePathHandle(KLIB.URL(FONT_PATH + "font.png"), KLIB.URL(FONT_PATH + "font.txt"));	
	public static final FontFilePathHandle thick_handle = new FontFilePathHandle(KLIB.URL(FONT_PATH + "font_thick.png"), KLIB.URL(FONT_PATH + "font_thick.txt"));	
	public	static final FontFilePathHandle georgia_bold = new FontFilePathHandle(KLIB.URL(FONT_PATH + "font_georgia_bold.png"), KLIB.URL(FONT_PATH + "font_georgia_bold.txt"));
//	static final FontFilePathHandle handle = new FontFilePathHandle(URL(FONT_PATH + "font.png"), URL(FONT_PATH + "font.txt"), URL(FONT_PATH + "font_thick.png"), URL(FONT_PATH + "font_thick.txt"));	
	static final OwnFont DEF_FONT = new OwnFont(handle, ColorPreset.WHITE.toColor());
	static final OwnFont DEF_THICK_FONT = new OwnFont(thick_handle, ColorPreset.BLACK.toColor());
	static final OwnFont DEF_NEW_FONT = new OwnFont(georgia_bold, ColorPreset.BLACK.toColor());
	
	static OwnFont default_font = DEF_FONT;
		
	static final char[] ALPHABET_NUMBER = "abcdefghijklmnopqrstuvwxyzäöüABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ1234567890".toCharArray(); // 3
	static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyzäöü�ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ".toCharArray();// 1
	static final char[] NUMBERS = "1234567890".toCharArray(); //2	
	static final char[] SONDERS = ";:-,._'#+*�`}=)]([/{&%$�\"\\!äöü@<>|^�".toCharArray(); // 4
	static final char[] ALPHABET_SONDERS = "abcdefghijklmnopqrstuvwxyzäöüABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ+;:-,._'#+*�`}=)]([/{&%$�\\\"\\\\!���@<>|^�".toCharArray(); // 5
	static final char[] ALL = "abcdefghijklmnopqrstuvwxyzäöüABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ1234567890;:-,._'#+*�`}=)]([/{&%$�\\\"\\\\!���@<>|^�".toCharArray(); // 0
	
	static final char[][] CHAR_SETS = {ALL, ALPHABET,NUMBERS, ALPHABET_NUMBER,SONDERS,ALPHABET_SONDERS,};
	
	static final ArrayList<Button> list = new ArrayList<>();
	static final ArrayList<Scrollable> scrols = new ArrayList<>();
	static final ArrayList<Expandable> exp_list = new ArrayList<>();
	
	/*	1.6.0
	 * 	 - removed libGDX
	 *   - added setDEFONTColor to Button
	 * 	 - added setDefLineSpacing
	 *   - changed setineYabs to setlineSpacing
	 * 	 - moved initfont to FormButton and renamed it to initDEFFONT 
	 *   - changed names of the getwidth to getWidth and other name conversions
	 *   - changed names of the getheight to getHeight and other name conversions
	 *   - added getMaxTextWidth
	 * 1.6.1 (6.12.2020)
	 *   - removed writeable methods
	 *   - minor fixes
	 *   - selected should work now again
	 *   - writeable is no hang anymore and works for itself
	 * 1.6.2(14.04.2021)
	 *   - fixed a bug with isHovered
	 *   - moved formButton into button
	 *   - ask for cur gamestate + option to turn it off
	 * 1.7.0(28.10.2021)
	 *   - many small improvements
	 *   - added Button.setDefFontColor and getDefFontColor
	 *   - added Button.setHangInteractable
	 *   - added Button.setHangsInteractable
	 *   - added Button.addHangs
	 *   - Writeable are now Secondary hangs 
	 *   - added Button.addWriteable
	 *   - added Button.getWriteable
	 * 1.7.0.1(18.03.2022)
	 *   - fixed when a button will not be drawn cause its partially out of the window
	 *   - added getAbsY
	 * 1.7.1(06.11.22)
	 *   - Buttons can now be created with: Button(name, scale) &  Button(name[], scale)
	 *   - Buttons can`t be selected but hovered by default
	 *   - added setHangLeft(i, abs_x, abs_y) and setHangRight(i, abs_x, abs_y)
	 * 1.8.0
	 *   - added setTextAlign and get
	 *   - added getLineSpacing
	 *   - added getDefaultlineSpacing and setDefaultLineSpacing
	 *   - improved colliding
	 *   - added unpress_selected if you click on a button that is already selected it will be unselected
	 * 1.9.0
	 *   - added ButtonEvent Listeners
	 *   	- they get registered with registerEvent;
	 *   - some code improvements
	 * 	 - slowly but steady the ID system will get removed
	 *   - added Option_...() methods with wich you can set some new options like
	 * 			- unselect_on_click
	 * 			- keep_selected
	 * 	 - hangs now update their position if the father is moved or scaled
	 *	 - due to the new font system getName().length is not allways correct instead now use getNameLength()
	 *
	 *   
	 */
	
	//TODO 1.8.0 die neue bounding methoden für text einbinden; update dispose method
	//Background fixen
	//TODO
	// button zumbeispiel ab der hälfte der pixel nicht mehr zeichnen (y achse)
	
	protected static boolean ISDEFFONTTHICK = false;
	protected static boolean first_init = false;
	
	protected static int default_line_spacing = 6;
	protected static final Color defColor = ColorPreset.BLACK.toColor();
	protected static int max_id = -1;
	protected static Button hovered_Button, drag_button;
	
	protected static ArrayList<Button> selected_Buttons = new ArrayList<>();
	protected static HashMap<Button, ButtonEvent> events = new HashMap<>();

	@Deprecated
	protected int ID = 0;
	public int associated_state = -1;

	public static boolean option_check_gamestate = true;
	protected boolean show = true;
	protected boolean can_hover = true;
	protected boolean can_select = false;
	protected boolean option_unselect_selected = false;
	protected boolean option_keep_on_click_outside = false;
	
	public Text text;
	protected OwnFont font;


	protected String[] old_name;	
	protected String hoverText;
	protected String selectText;
	
	protected Color originColor, hoverColor, selectColor;

	public Button_Background buttonBackground;
		
	protected final ArrayList<Hang> hangs = new ArrayList<>();	
	
	protected Button (boolean add_to_list) {
		if(!first_init) {
			first_init = true;
		}

		if(add_to_list) {
			list.add(this);
			assignID();
		}
        associated_state = GSM.getStateInt();

        originColor = defColor;
		hoverColor = Color.RED();
		selectColor = Color.CYAN();
				
		old_name = new String[] {"Button" + ID};	
		hoverText = "";
		selectText = "";		
			
		this.font = default_font;		
		
		text = new Text(font, old_name);
		text.setLineSpacing(default_line_spacing);	
	}
	
	public Button (String name) {		
		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, default_font, defColor);
	}
	
	public Button (String name, float scale) {		
		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), scale, default_font, defColor);
	}
	
	public Button (String name, Vector2 pos) {
		this(new String[] {name}, pos, 1f, default_font, defColor);
	}
	
	public Button (String name, OwnFont font) {
		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, font, defColor);
	}
	
	public Button (String name, Colorable c) {
		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, default_font, c);
	}	
	
	public Button (String name, float scale, Colorable c) {
		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), scale, default_font, c);
	}
	
	public Button (String name, Vector2 pos, OwnFont font) {
		this(new String[] {name}, pos, 1f, font, defColor);
	}
	
	public Button(String name, float scale, OwnFont font, Colorable c) {
		this(new String[] {name}, new Vector2(0, KLIB.graphic.Height() * 0.5f), scale, font, c);
	}
	
	public Button (String[] name) {		
		this(name, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, default_font, defColor);
	}
	
	public Button (String[] name, float scale) {		
		this(name, new Vector2(0, KLIB.graphic.Height() * 0.5f), scale, default_font, defColor);
	}

	public Button (String[] name, Vector2 pos) {
		this(name, pos, 1f, default_font, defColor);
	}	
	
	public Button (String[] name, OwnFont font) {
		this(name, new Vector2(0, KLIB.graphic.Height() * 0.5f), 1f, font, defColor);
	}	
	
	public Button (String[] name, float scale, Colorable c) {
		this(name, new Vector2(0, KLIB.graphic.Height() * 0.5f), scale, default_font, c);
	}
	
	public Button (String[] name, Vector2 pos, OwnFont font) {
		this(name, pos, 1f, font, defColor);
	}
	
	public Button (String[] name, Vector2 position, float scale, OwnFont font, Colorable c) {
		if(!first_init) {
			first_init = true;
		}
		
		list.add(this);	

		assignID();
		associated_state = GSM.getStateInt();

		originColor = defColor;
		hoverColor = ColorPreset.RED.toColor();
		selectColor = ColorPreset.CYAN.toColor();
				
		old_name = name;	
		hoverText = "";
		selectText = "";		
			
		this.font = font;		
		
		
		text = new Text(font, name);

		Option_Set_LineSpacing(default_line_spacing);
		setColor(c);
		if(scale != 1f)	setScale(scale);
		text.setPosition(position.getX(), position.getY());

	}
	
	public boolean isHoverable() {
		return can_hover;
	}
	public boolean isSelectable() {
		return can_select;
	}
	
	public void dispose() {
//		pos = null;
		font = null;
		removeButton(this);
		ID = 0;
		cleanIDs();		
		hangs.clear();
        events.remove(this);
	}
		

	public int getLine_Spacing() {
		return text.getLineSpacing();
	}

	public int getFontChar_Spacing() {
		return (int) text.getCharSpacing();
	}
	
	public void setFontChar_Spacing(int abs) {
		text.setCharSpacing(abs);
	}		
	
	public static int getDefault_FontChar_Spacing() {
		return DEF_FONT.getCharSpacing();
	}
	
	public static void setDefault_FontChar_Spacing(int spacing) {
		DEF_FONT.setCharSpacing(spacing);
	}


	public Text_Align getTextAlign() {
		return text.getAlignment();
	}	
	
	public Button setTextAlign(Text_Align align) {
		text.setAlignment(align);
		return this;
	}	
	
	
	@Deprecated
	public Button setPositioningMode(int mode) {
		if(mode == 1)		
			text.setAlignment(Text_Align.MIDDLE);
		else if(mode == 2)		
			text.setAlignment(Text_Align.RIGHT_BOUND);
		else 
			text.setAlignment(Text_Align.LEFT_BOUND);
		return this;
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
	
	public void initBackground() {
		buttonBackground = new Button_Background(this);
	}
	
	public Button_Background getBackground() {
		return buttonBackground;
	}
	
	public float getMaxTextWidth() {
		return text.getMaxWidth();
	}
	
	public Button setMaxTextWidth(float width) {
		text.setMaxWidth((int) width);
		if(getBackground()!= null)
			getBackground().getBGBounds();
		return this;
	}
	
	public void setX(float x) {
		text.setX((int) x);

        for (Hang h : hangs) {
            h.setRelPosition(h.getRelPosition().getX(), h.getRelPosition().getY());
        }
	}
	
	public void setY(float y) {
		text.setY((int) y);

        for (Hang h : hangs) {
            h.setRelPosition(h.getRelPosition().getX(), h.getRelPosition().getY());
        }
	}
	
	public Button setPosition(Vector2 pos) {		
		return setPosition(pos.getX(), pos.getY());
	}
	
	public Button setPosition(float x, float y) {
		text.setPosition(x, y);

        for (Hang h : hangs) {
            h.setRelPosition(h.getRelPosition().getX(), h.getRelPosition().getY());
        }
		
		return this;
	}
	
	public void setPosition(int hang, Vector2 pos) {
		hangs.get(hang).setPosition(new Vector2(pos.getX(), pos.getY()));
	}
	
	public void setPosition(int hang, float x, float y) {
		hangs.get(hang).setPosition(x, y);
	}
	
	public Vector2 getPosition() {		
		return text.getTotalPosition();
	}
	public Vector2 getTotalPosition() {
		return text.getTotalPosition();
	}
	
	public Vector2 getPosition(int hang) {
		return hangs.get(hang).getPosition();		
	}
	
	public float getX() {
		return text.getTotalX();
	}
	
	public float getY() {
		return text.getTotalY();
	}
	
	public float getRight() {
		return getX() + getWidth();
	}
	
	public float getTop() {
		return getY() + getHeight();
	}
	
	public float getAbsY() {
		return text.getAbs(text.getNames().length-1);
	}	
	
	public Button setHangLeft(int i) {
		return setHangLeft(hangs.get(i));
	}
	public Button setHangLeft(Hang h) {
		h.setRelPosition(-h.getWidth(), 0);
		return this;
	}
	public Button setHangLeft(Hang h, float abs) {
		h.setRelPosition(-h.getWidth() - abs, 0);
		return this;
	}

	public Button setHangLeft(int i, float abs) {
		Hang b = hangs.get(i);
		b.setRelPosition(-b.getWidth() -abs, 0);		
		return this;
	}
	
	public Button setHangLeft(int i, int abs_x, int abs_y) {
		Hang b = hangs.get(i);
		b.setRelPosition(-b.getWidth() -abs_x, abs_y);		
		return this;
	}
	
	public Button setHangRight(int i) {
		return setHangRight(hangs.get(i));
	}
	public Button setHangRight(Hang h) {
		h.setRelPosition(text.getWidth(), 0);
		return this;
	}

	public Button setHangRight(int i, float abs) {
		Hang b = hangs.get(i);
		b.setRelPosition(text.getWidth() + abs, 0);
		return this;
	}
	public Button setHangRight(Hang h, float abs) {
		h.setRelPosition(text.getWidth() + abs, 0);
		return this;
	}
	
	public Button setHangRight(int i, int abs_x, int abs_y) {
		Hang b = hangs.get(i);
		b.setRelPosition(getWidth() + abs_x, abs_y);		
		return this;
	}
	
	public Button setHangInteractable(int i, boolean interactable) {
		Hang b = hangs.get(i);
		b.setInteractable(interactable);
		return this;		
	}
	
	public Button setHangInteractable(boolean interactable) {
		Hang b = hangs.getLast();
		b.setInteractable(interactable);
		return this;		
	}
	
	public Button setHangsInteractable(boolean interactable) {
        for (Hang b : hangs) {
            b.setInteractable(interactable);
        }
		return this;		
	}
	
//	public void addFontCacheColor(Color c, boolean thick) {
//		this.font.addCacheColor(c, thick);
//	}

	@Deprecated
	public Button setMiddle() {
		return this;
	}

	public Button setMiddle(boolean x, boolean y) {
		if(x && y) {
			setPosition((KLIB.graphic.Width() - getWidth()) * 0.5f, (KLIB.graphic.Height() - getHeight()) * 0.5f);
		} else if(x) {
			setX((KLIB.graphic.Width() - getWidth()) * 0.5f);
		} else if(y) {
			setY((KLIB.graphic.Height() - getHeight()) * 0.5f);
		}
		return this;
	}
	
	public Hang getHang() {
		try {
			return hangs.getLast();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}		
	}
	
	public Hang getHang(int i) {
		try {
			return hangs.get(i);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Button setHangHoverColor(Color c) {
        hangs.getLast().setHoverColor(c);
		return this;
	}
	
	public Button setHangSelectColor(Color c) {
        hangs.getLast().setSelectColor(c);
		return this;
	}
	
	public Button setHangHoverColor(int i, Color c) {
        hangs.get(i).setHoverColor(c);
		return this;
	}
	
	public Button setHangSelectColor(int i, Color c) {
        hangs.get(i).setSelectColor(c);
		return this;
	}
	
	
	public Button setName(String name) {
		text.setName(name);
		if(buttonBackground != null) buttonBackground.getBGBounds();
		return this;
	}	
	public Button setName(String[] name) {
		text.setName(name);
		if(buttonBackground != null) buttonBackground.getBGBounds();
		return this;
	}	
	
	public Button setName(int index, String name) {
		text.setName(index, name);
		if(buttonBackground != null) buttonBackground.getBGBounds();
		return this;
	}	
	
	public String getName() {		
		return this.text.getName();
	}
	public String[] getNames() {		
		return this.text.getNames();
	}
	public String getName(int index) {		
		return this.text.getName(index);
	}

	public int getNameLength() {
		return text.getNameLength();
	}

	public int getNameLength(int i) {
		return text.getNameLength(i);
	}

	public Button setFont(OwnFont font) {
		this.font = font;
		return this;
	}
	
	public void setThick(boolean b) {
		if(b)
			this.font = DEF_THICK_FONT;
		else 
			this.font = DEF_FONT;
	}
	
	public void changeFont(OwnFont type) {
		this.font = type;
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

	public Button setColor(Colorable color) {
		text.setColor(color.toColor());
		originColor = color.toColor();
		return this;
	}

	public Button setColor(int i, Colorable color) {
		hangs.get(i).setColor(color);
		return this;
	}
	
	public Color getColor() {
		return text.getColor();
	}
	
	public Button setHoverColor(Colorable color) {
		hoverColor = color.toColor();
		return this;
	}
	
	public Button setSelectColor(Colorable color) {
		selectColor = color.toColor();
		return this;
	}	

	public Button setAllColor(Color color) {
		for(int i = 0; i < hangs.size(); i++) {
			setColor(i, color);
		}
		setColor(color);
		return this;
	}
	
	public void setHoverText(String text) {
		hoverText = text;
	}
	
	public void setSelectText(String text) {
		selectText = text;
	}
	
	public float getWidth() {
		return text.getWidth();
	}
	
	public float getWidth(int line) {			
		return text.getWidth(line);	
	}
		
	@Deprecated
	public float getWidth_real() {
		return text.getSize_Total().getWidth();
	}
	
	public float getWidthTotal() {
		return text.getSize_Total().getWidth();
	}
		
	public Button setHeight(float height) {
		text.setHeight((int) height);
		return this;
	}
	
	public float getHeight() {		
		return text.getHeight();
	}
	
	public float getHeight(int line) {		
		return text.getHeight(line);
	}

	public float getHeightTotal() {		
		return text.getBounds_Total().getHeight();
	}

	public static Button getButton(int ID) {
        for (Button b : list) {
            if (b.getID() == ID) {
                return b;
            }
        }
		return null;
	}
	
	public int getID() {
		return ID;
	}

	
	public static int getID(Button b) {
		return b.ID;
	}
	
//	private void setID(int i) {
//		if(i == 0 || i == -1) {
//			ID = max_id;		
//			max_id++;
//		} else {
//			ID = i;
//		}
//
//	}
	
	public String getoldName() {
		return old_name[0];
	}
	
	public String getoldName(int index) {
		return old_name[index];
	}
	
	public String getText(int hang) {
		return getHang(hang).getName().replace("_", "");
	}
	
	public Hang addHang(String name) {
		return addHang(name, new Vector2(getWidth(), 0), getColor());
	}
	
	public Hang addHang(String name, Color color) {
		return addHang(name, new Vector2(getWidth(), 0), color);
	}
	
	public Hang addHang(String name, Vector2 pos) {
		return addHang(name, pos, getColor());
	}
	
	public Hang addHang(String name, float x, float y) {
		return addHang(name, new Vector2(x, y), getColor());
	}
	
	public Hang addHang(String name, Vector2 pos, Color color) {
		return new Hang(this.font, name, pos, color, this);
	}
	
	public Hang addHang(String name, float x, float y, Color color) {
		return addHang(name, new Vector2(x, y), color);
	}
		
	public Button addHangs(int amount, String[] names) {
		for (int i = 0; i < amount; i++) {
			addHang(names[i]);			
		}	
		return this;		
	}

	@Deprecated
	public Writeable addWriteable(int max_chars, int writemode) {
		return new Writeable(this, "", max_chars, writemode);
	}

	@Deprecated
	public Writeable getWriteable(int i) {
		return (Writeable) hangs.get(i);
	}
	
	public static void cleanIDs() {		
		for(int i = 0; i < list.size(); i++) {
			Button b = list.get(i);
			b.ID = i;	
		}		
	}
	
	public void setScale(float scale) {
		text.setScale(scale);

		for (Hang h : hangs) {
			h.setScale(scale);
		}

	}
	
	public float getScale() {
		return text.getScale();
	}
	
	public float getFontScale() {
		return font.getScale();
	}
	
	public static void setDefFontScale(float scale) {
		default_font.setScale(scale);
	}
	public float getDefFontScale() {
		return default_font.getScale();
	}
	
	public static void setDefFontColor(Colorable color) {
		defColor.set(color);
	}
	
	public static Color getDefFontColor() {
		return defColor;
	}

	private static void removeButton(Button b) {		
		list.remove(b);		
		Collections.sort(list);
	}

	@Deprecated
	public static void setSelected(int id) {
		if(selected_Buttons.contains(getButton(id))) {
			selected_Buttons.remove(getButton(id));
		} else selected_Buttons.add(getButton(id));
	}

	public static Button setSelected(Button b) {
		if(selected_Buttons.contains(b)) {
			selected_Buttons.remove(b);
		} else selected_Buttons.add(b);
		return b;
	}

	public static Button setSelected(Button b, boolean is_selected) {
		if(selected_Buttons.contains(b)) {
			if(!is_selected) {
				selected_Buttons.remove(b);
			}
		} else {
			if(is_selected) {
				selected_Buttons.add(b);
			}
		}
		return b;
	}

	boolean isSelected() {
        return selected_Buttons.contains(this);
    }
	
	public boolean isSelected(boolean ask) {
		if(show) {
			if(isSelectable())
				return selected_Buttons.contains(this);
			else {
				if(ask) return isColliding();
			}
		}
		return false;
	}
	
	//public static int getSelected() {
	//	return selected_Button;
	//}
	/*

	 */


	boolean isHovered() {		
		return hovered_Button == this;
	}
	
	public boolean isHovered(boolean ask) {
		if(!show) return false; 		

		if(ask) {
			if(isColliding()) {
				hovered_Button = this;
				return true;
			}
		} else {
			return isHovered();
		}
		return false;
	}

	@Deprecated
	public static Button setHovered(int ID) {
		return hovered_Button = getButton(ID);
	}
	
	public static Button getHovered() {
		return hovered_Button;
	}

	public static Button setHovered(Button b) {
		return hovered_Button = b;
	}

	
	
	@Deprecated
	public static int getHoveredButton(ArrayList<Button> buttons) {		
		if(!option_check_gamestate) {
			if(buttons != null && !buttons.isEmpty()) {
				for (Button c : buttons) {
					if(c.show && c.can_hover) {
						if(c.isColliding())
							return c.getID();
					}
				}
			}
		} else {
			if(buttons != null && !buttons.isEmpty()) {
				for (Button c : buttons) {
					if(c.show && c.can_hover && GSM.getStateInt() == c.associated_state) {
						if(c.isColliding())
							return c.getID();
					}
				}
			}
		}
		return -1;		
	}
	
	protected boolean isColliding() {
		
		float mouse_x = Input.mouse.getX();
		float mouse_y = Input.mouse.getY();

		Vector4 button_bounds = new Vector4();

        if (this instanceof Scrollable scrolable) {
            button_bounds.set(scrolable.getSliderPos().getX(), scrolable.getSliderPos().getY(), scrolable.getSliderWidth(), scrolable.getSliderHeight());
        } else {
            if (getNames().length == 1) {
                button_bounds.setPosition(text.getTotalPosition());
            } else {
                button_bounds.setPosition(text.getTotalPosition());
                button_bounds.setY(getPosition().getY() - getHeight() - getLine_Spacing());
            }
            button_bounds.setWidth(getWidth());
            if (getMaxTextWidth() > 0) button_bounds.setWidth(getMaxTextWidth());
            button_bounds.setHeight(getHeight());
        }

        return mouse_x > button_bounds.getX() &&
                mouse_x < button_bounds.getX() + button_bounds.getWidth() &&
                mouse_y > button_bounds.getY() &&
                mouse_y < button_bounds.getY() + button_bounds.getHeight();
    }

	static boolean isColliding(Vector2 pos, Vector2 size) {
		float mouse_x = Input.mouse.getX();
		float mouse_y = Input.mouse.getY();
		return mouse_x > pos.getX() && mouse_x < pos.getX() + size.getWidth() && mouse_y > pos.getY() && mouse_y < pos.getY() + size.getHeight();
	}
	
	static boolean isColliding(int x, int y, int width, int height) {
		float mouse_x = Input.mouse.getX();
		float mouse_y = Input.mouse.getY();
		return mouse_x > x && mouse_x < x + width && mouse_y > y && mouse_y < y + height;
	}

	public static Button getCollidingButton(ArrayList<Button> buttons) {
		if(buttons == null) return null;

		if(!option_check_gamestate) {
				for (Button c : buttons) {
					if(c.show) {
						if(c.isColliding())
							return c;
					}
				}
		} else {
				for (Button c : buttons) {
					if(c.show && GSM.getStateInt() == c.associated_state) {
						if(c.isColliding())
							return c;
					}
				}
		}
		return null;
	}
	

	protected void takeConfig(Button h) {
		list.add(h.getID(), this);
		this.font = h.font;
		text = h.text;
		//setName(h.getNames());
		old_name = h.getNames();	
		
		show = true;
		setPosition(text.getTotalPosition());
		text.setColor(h.getColor());
		ID = h.getID();
//		scale = h.scale;
		selectColor.set(h.getColor());
		hoverColor.set(h.hoverColor);
		originColor.set(h.originColor);
		hoverText = h.hoverText;
		selectText = h.selectText;
	}
	
	protected void setDefconfig() {
		font = DEF_FONT;
		old_name = new String[] {"default"};	

		list.add(ID, this);
//		scale = 1f;
		show = true;
		setPosition(0,0);		
		text = new Text(font, "default");
		
		setMiddle();	
	
		text.setColor(defColor);
		selectColor.set(ColorPreset.CYAN);
		hoverColor.set(ColorPreset.RED);
		originColor.set(defColor);
		hoverText = "";
		selectText = "";
	}
	
	
	public void draw(MasterRenderer renderer) {			
		draw(renderer, 0);
	}
	
	public void draw(MasterRenderer renderer, int layer) {	
//		was_drawn = false;
		if(show) {
				if(hovered_Button == this && can_hover)
					text.setColor(hoverColor);
				else if(can_select && selected_Buttons.contains(this))
					text.setColor(selectColor);
				else
					text.setColor(originColor);			
				
				if(buttonBackground != null && buttonBackground.bg_show)
					renderer.processTexture(buttonBackground.texture, buttonBackground.getBGPosition(), buttonBackground.getBGBounds(), layer);
				
				text.draw(renderer, layer);		
//				was_drawn = true;

            for(Hang h : hangs) {
                h.draw(renderer, layer);
            }
		}
	}
	
	
	public void draw(MasterRenderer renderer, float x, float y) {			
		draw(renderer, x, y, 0);
	}

	public void draw(MasterRenderer renderer, float x, float y, int layer) {			
		if(show) {
				if(hovered_Button == this && can_hover)
					text.setColor(hoverColor);			
				else if(can_select && selected_Buttons.contains(this))
					text.setColor(selectColor);
				else
					text.setColor(originColor);			
				
				if(buttonBackground != null && buttonBackground.bg_show)
					renderer.processTexture(buttonBackground.texture, buttonBackground.getBGPosition(), buttonBackground.getBGBounds(), layer);
				
				text.draw(renderer, x, y, layer);				


            for(Hang h : hangs) {
                h.draw(renderer, layer);
            }
		}
	}
		
//	public void setThickFont(boolean bool) {
//		text.setThickFont(bool);
//	}

	public void setoldName(String string) {
		this.old_name[0] = string;		
	}
	public void setoldName(String[] string) {
		this.old_name = string;		
	}
	public void setoldName(int index, String string) {
		this.old_name[index] = string;		
	}
	
	public static void setDefaultFont(OwnFont font) {
		default_font = font;
	}
	
	
	public static void touchDown(int screenX, int screenY, int pointer, int button) {
		
		Button b = getCollidingButton(list);
		if(b == null) {
			hovered_Button = null;
			selected_Buttons.removeIf(ba -> !ba.option_keep_on_click_outside);
		} else {
			if (b.isHoverable())
				hovered_Button = b;
			if (b.isSelectable()) {
				if (b.option_unselect_selected && selected_Buttons.contains(b)) {
					selected_Buttons.remove(b);
				} else
					selected_Buttons.add(b);
			}
			ButtonEvent e = events.get(b);
			if(e instanceof Button_onTouchDown_Event) ((Button_onTouchDown_Event) e).onTouchDown();
		}

		for(Scrollable scrl : scrols) {
           if(scrl.isSliderSelected()) {
			   drag_button = scrl;
			   return;
		   }
		}

		Expandable.trigger();

	}
	
	public static void touchUp(int screenX, int screenY, int pointer, int button) {
		if(drag_button != null ) {
			Scrollable c = (Scrollable) drag_button;
//			if(c.IS_VERTICAL) {
//				if(c.getSliderPos().getY() < c.getY()) {
//					c.getSliderPos().setY(c.getY());
//					if(c.changeName)
//						c.setName("0.0");
//				} else if(c.getSliderPos().getY() + c.getSliderHeight() > c.getY() + c.getHeight()) {
//					c.getSliderPos().setY(c.getY() + c.getHeight() - c.getSliderHeight());
//					if(c.changeName)
//						c.setName("1.0");
//				}
//			} else {
//				if(c.getSliderPos().getX() < c.getX()) {
//					c.getSliderPos().setX(c.getX());
//					if(c.changeName)
//						c.setName("0.0");
//				} else if(c.getSliderPos().getX() + c.getSliderWidth() > c.getX() + c.getWidth()) {
//					c.getSliderPos().setX(c.getX() + c.getWidth() - c.getSliderWidth());
//					if(c.changeName)
//						c.setName("1.0");
//				}
//			}
			drag_button = null;
		}
	}
	
	public static void touchDragged(int screenX, int screenY, int pointer) {
		Scrollable.touchDragged(screenX, screenY);
	}
	
	public static void mouseMoved(int screenX, int screenY) {
        hovered_Button = getCollidingButton(list);
	}

	public static void keyTyped(int glfw_key, char character) {
		for(Button b : events.keySet()) {
			ButtonEvent e = events.get(b);
			if(e instanceof Button_onKey_Events) {
				((Button_onKey_Events) e).onKeyTyped(glfw_key, character);
			}
		}
	}

	public static void keyDown(int glfw_key, int action, int modifier) {
		for(Button b : events.keySet()) {
			ButtonEvent e = events.get(b);
			if(e instanceof Button_onKey_Events) {
				((Button_onKey_Events) e).onKeyDown(glfw_key, action, modifier);
			}
		}
	}


	void registerEvent(ButtonEvent event) {
		Button.registerEvent(this, event);
	}

	static void registerEvent(Button b, ButtonEvent event) {//TODO
		events.put(b, event);
		if (event instanceof Button_onKey_Events) {
			System.out.println("");
		}
		if (event instanceof  Button_onTouchDown_Event) {
			System.out.println("");
		}
	}

	public int getFontSize() {
		return font.getFontSize();
	}

	public int assignID() {
		if(ID == 0) {
			max_id++;
			ID = max_id;
		}
		return max_id;
	}
	public static int assignID(Button b) {
		max_id++;
		b.ID = max_id;
		return max_id;
	}

	public void Option_Set_LineSpacing(float abs) {
		text.setLineSpacing((int) abs);
	}
	public static int Option_Set_DefaultLineSpacing(int abs) {
		return default_line_spacing;
	}
	public static int Option_Get_DefaultLineSpacing() {
		return default_line_spacing;
	}
	public void Option_SelectableOptions(boolean unselect_when_selected, boolean keep_on_click_outside) {
		this.option_unselect_selected = unselect_when_selected;
		this.option_keep_on_click_outside = keep_on_click_outside;
	}

	@Override
	public int compareTo(Button o) {
        return Integer.compare(this.getID(), o.getID());
	}

	private static URL URL(String path) {
		return Button.class.getResource(path);
	}







	
	

}
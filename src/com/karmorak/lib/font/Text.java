//v1.5.0
package com.karmorak.lib.font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.karmorak.lib.Color;
import com.karmorak.lib.ColorPreset;
import com.karmorak.lib.Colorable;
import com.karmorak.lib.Input;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.CharTexture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.font.identifier.Identifier;
import com.karmorak.lib.font.ownchar.OwnChar;
import com.karmorak.lib.font.ownchar.OwnCharData;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;
import com.karmorak.lib.math.Vector4;

public class Text {

	
	/*changes v1.2
	 *  - removed libgdx
	 *  - texturecache is now static and is REGIONCACHE (better caching)
	 *  - lots of bugfixes
	 *  - added new methods to get the sizes of a word/char 
	 *  	eg. getWholeHeight(int) = can give the whole size including chars like y who going down in a line, 
	 *  		getYOffsetDown(int) = if theres a char like y you get the offset 
	 *  		getYOffsetUp(int) = if theres a char that goes out at the top you get it with that method
	 *  - renamed getRealWidth to getWholeWidth
	 *  changes v1.3
	 *   - added/improved update method
	 *   - improved rendering
	 *  v.1.3.1
	 *   - fixed positioning bug
	 *  v.1.4
	 *   - added TextAlign and improved bounding 
	 *   - temporary disable of maxwidth
	 *   - improved rendering
	 *   - line space now working
	 *   - fixed multiple lines now working
	 *   - many method renames
	 *  v1.4.1
	 *   - max_width should work again now
	 *  v-1.5.0
	 *   - compatible with OwnFont 1.4
	 * 	 - some small fixes
	 *   - added new getWidth(line, startIndex, endIndex) method to get just the width of a certain part
	 *   - if theres is a max width and the textalign is set to right the text will be drawn from the end starting
	 *   - hopefully some performance and stability improvements
	 *   - added getNameLength() to get the char length of the name since the string length is now not always correct due to
	 * 			the new special_chars
	 *   - added lastDrawnChar which indicates which char was the last drawn when using MaxTextWidth
	 *   - added setShifting, getShifting, removeShifting & addShifting
	 *   - renamed get_Position_Total to get_TotalPosition
	 *   - added get_choverd_char to get the char where the mouse is hovered on
	 *   - added get_LastDrawnChar it indicates how much chars got drawn actually (relevant when using maxWidth > 0)
	 *
	 *
	 * 
	 */
	
	//TODO set Anchorpoint of texts with multiple lines
	
	private final OwnFont font;
	
	/** this is only important for multi lines or when max_text_width > 0
	 * 0 - default all lines will be drawn on the left side
	 * 1 - all lines will be drawn in the middle
	 * 2 - all lines will be drawn from the right side
	 * 
	 * 
	 */	
	private Text_Align text_align = Text_Align.LEFT_BOUND;
	
	private String[] stringed_name;
	private OwnCharData[][] name;
	
	private Color color;
	private final Vector2i pos;
	private float scale = 1f;
	private int max_width;
	
	private int line_spacing = 0;
	
	private int longest_line = -1;
	private float longest_line_width_raw = 0;
	
	private int last_drawn_char;
	private int shift_char = 0;

	/**
	 * 0 = namechanged; 
	 * 1 = colorchanged; 
	 * 2 = formatting changed (thick=obsolete); 
	 * 3 = scale changed; 
	 * 4 = position changed
	 * 5 = pos_x changed
	 * 6 = pos_y changed
	 */
	private final boolean[] changed = new boolean[] {true, true, true, true, true, true, true, true};
//	public static final HashMap<Color, HashMap<Character, Texture>> REGIONCACHE = new HashMap<Color, HashMap<Character, Texture>>();

	static HashMap<String, FontCache> caches = new HashMap<>();

	public static final ArrayList<Text> texts = new ArrayList<Text>();


	private record FontCache(HashMap<Color, DrawMap> font_colors, HashMap<Color, HashMap<Identifier, CharTexture>> cached_chars_v2) {
		public FontCache() {
			this(new HashMap<>(), new HashMap<>());
		}
		boolean isEmpty() {
			return cached_chars_v2.isEmpty();
		}


		private CharTexture getCachedTexture(Color color, Identifier ident) {
			HashMap<Identifier, CharTexture> colorGroup = cached_chars_v2.get(color);

			if (colorGroup == null)
				return null;

			return colorGroup.get(ident);
		}

		private void addCharToCache(Color color, Identifier ident, CharTexture texture) {
			// Sicherstellen, dass die Map für die Farbe existiert
			cached_chars_v2.computeIfAbsent(color, k -> new HashMap<>())
					.put(ident, texture);
		}

	}

	public record chardata(Color color, Identifier ident) {}


	public static enum Text_Align {
		LEFT_BOUND, MIDDLE, RIGHT_BOUND
	}
	
	
	public Text(OwnFont font, String name) {
		this.font = font;
		this.name = OwnChar.createOwnChars(new String[]{name}, font);
		this.stringed_name = new String[] {name};

		color = ColorPreset.BLACK.toColor();
		
		longest_line = 0;
		longest_line_width_raw = font.getWordBounds_Raw(name).getWidth();
		
		this.pos = new Vector2i(0, 0);
		texts.add(this);
	}
	
	public Text(OwnFont font, String[] name) {
		this.font = font;
		this.name = OwnChar.createOwnChars(name, font);
		this.stringed_name = name;
		color = ColorPreset.BLACK.toColor();
		
		calcLongestLine();
		
		this.pos = new Vector2i(0, 0);
		texts.add(this);
	}

	
	public void setName(String name) {
		if(!stringed_name[0].equals(name)) {
			this.name[0] = OwnChar.createOwnChars(name, font);
			this.stringed_name[0] = name;
			changed[0] = true;

			longest_line_width_raw = font.getWordBounds_Raw(name).getWidth();
		}
	}
	
	public void setName(String[] name) {
		boolean issame = Arrays.equals(this.stringed_name, name);
		if(issame) return;

		this.name = OwnChar.createOwnChars(name, font);
		this.stringed_name = name;
		changed[0]=true;
					
		calcLongestLine();
	}
	
	public void setName(int i, String name) {
		if(!this.stringed_name[i].equals(name)) {
	
			this.name[i] = OwnChar.createOwnChars(name, font);
			this.stringed_name[i] = name;
			
			changed[0]=true;
			
			calcLongestLine();
		}
	}
	
	public String getName() {
		return stringed_name[0];
	}
	
	public String[] getNames() {
		return stringed_name;
	}
	
	public String getName(int i) {
		return stringed_name[i];
	}

	public int getNameLength() {
		return getNameLength(0);
	}

	public int getNameLength(int i) {
		return name[i].length;
	}

	public void setX(int x) {
		if(x != pos.getX())
			changed[5] = true;
		
		pos.setX(x);
		changed[4] = true;
	}
	
	public void setY(int y) {
		if(y != pos.getY())
			changed[6] = true;
		
		pos.setY(y);
		changed[4] = true;
	}
	
	public void setPosition(int x, int y) {
		if(x != pos.getX())
			changed[5] = true;
		if(y != pos.getY())
			changed[6] = true;
		
		pos.set(x, y);
		changed[4] = true;
	}

	public void setPosition(float x, float y) {
		if(x != pos.getX())
			changed[5] = true;
		if(y != pos.getY())
			changed[6] = true;
		
		pos.set(x, y);
		changed[4] = true;
	}
	
	public int getAbs(int line) {
		
		int abs = 0;
		
		for(int i = 0; i < name[line].length; i++) {
			OwnCharData c = name[line][i];
				
			float[] info = font.DATA.getInfo(c.getIdentifier());
			
			if(info[1] < abs) abs = (int) info[1];			
		}
		return abs;
	}

	public void setColor(Colorable c) {
		if(!c.equals(color)) {
			color = c.toColor();
			changed[1]=true;
		}

	}
	
	public Color getColor() {
		return color;
	}
	
	public void setAlignment(Text_Align align) {
		text_align = align;
		changed[7] = true;
	}
	
	public Text_Align getAlignment() {
		return text_align;
	}	

	public void setLineSpacing(int abs) {
		line_spacing = abs;	
	}
	
	public int getLineSpacing() {
		return (int) (line_spacing );
	}
	
	public int getLineSpacing_Raw() {
		return line_spacing;
	}
	
	public void setCharSpacing(int i) {
		font.setCharSpacing(i);
	}	
	
	public float getCharSpacing() {
		return font.char_spacing * getScale_Total();
	}	
	
	@SuppressWarnings("unused")
	private float getCharSpacing_Raw() {
		return font.char_spacing;
	}
	
	public void setMaxWidth(int width) {
		if(width < getWidth()) {
			changed[0] = true;
		}
		changed[4] = true;
		changed[5] = true;
		max_width = width;
	}

	public void updateState(int i) {
		changed[i] = true;
	}

	public float getMaxWidth() {
		return max_width;
	}

	public int getShifting() {
		return shift_char;
	}
	public void addShifting(int shift) {
		setShifting(shift_char+shift);
	}
	public void removeShifting(int shift) {
		setShifting(shift_char-shift);
	}
	public void setShifting(int i) {
		if(i < getNameLength() && i >= 0) {
			shift_char = i;
			changed[0] = true;
		}
	}

	@Deprecated
	public float getOffsetX() {
		return getOffsetX(0);	
	}
	
	public int getOffsetX(int line_i) {
		if(name.length == 1)
			return 0;
		if(longest_line == -1)
			calcLongestLine();
		if(line_i == longest_line) 
			return 0;
		if(text_align == Text_Align.LEFT_BOUND)
			return 0;	

		if(text_align == Text_Align.MIDDLE) {				
			int line_width = getWidth_Raw(line_i);
            return (int) (((longest_line_width_raw - line_width) * 0.5f) * getScale_Total());
		} else if(text_align == Text_Align.RIGHT_BOUND) {				
			int line_width = getWidth_Raw(line_i);
            return (int) ((longest_line_width_raw - line_width) * getScale_Total());
		}
		
		return 0;
	}
	
	//	public float getOffsetY() { // TODO
	//	if(name.length == 1) {
	//		return pos.getY();
	//	} else {
	//		float y = 0;
	//		for(int i = 1; i < name.length; i++) {
	//			y -= getHeight(i);
	//		}
	//
	//		
	//		return y;
	//	}
	//}
	
	private int calcLongestLine() {
		int longest = 0;		
		int width = (int) getWidth_Raw(0);
		for (int i = 1; i < name.length; i++) {
			int width_ = (int) getWidth_Raw(i);
			if(width_ > width) {
				width = width_;
				longest = i;
			}
		}
		longest_line_width_raw = width;
		return longest_line = longest;	
	}
	
	
	public int getLongestLine() {
		if(longest_line > -1) return longest_line;
		else return calcLongestLine();
	}

	
	public float getWidth() {
		return getWidth(0);				
	}
	
	public float getWidth(int line) {
        return max_width == 0 ? font.getWordWidth_Raw(name[line]) * getScale_Total() : max_width;
	}	

	public int getWidth(int startIndex, int endIndex) {
		return (int) (font.getWordWidth_Raw(name[0], startIndex, endIndex)  * getScale_Total());
	}

	@Deprecated
	private int getWidth_Raw(int line) {
        return (int) font.getWordBounds_Raw(name[line]).getWidth();
	}
	
	public float getHeight() {
		return getHeight(0);
	}
	
	public float getHeight(int i) {
        return font.getWordHeight_Raw(name[i]) * getScale_Total();
	}

	public float getWholeWidth() {
		
		if(longest_line == -1)
			calcLongestLine();
		
		return longest_line_width_raw * getScale_Total();		
	}
	
	//ay wird zwar berücksichtigt aber wahrscheinlich dennoch veraltet
	public float getWholeHeight(int i) {		
		float height = 0;		

		
		for(int a = 0; a < name[i].length;a++) {
			OwnCharData ch = name[i][a];
			
			Vector2 bounds = font.getCharSize(ch.getIdentifier());
			float[] data = font.DATA.getInfo(ch.getIdentifier());
			float a_y = data[1];
			if(a_y < 0 ) a_y = a_y * -1;
			
			
			if(bounds.getHeight() + a_y > height) height = bounds.getHeight() + a_y;
			
			
		}	
		return height;		
	}

//	public Vector2i getTextPosition() {
//		if(name.length == 1) {
//			if(max_width == 0)
//				return pos;
//			else {
//
//			}
//		} else {
//			float y = pos.getY();
//			y -= getHeight(0) - getLineSpacing();
//			return new Vector2i(pos.getX(), y);
//		}
//	}

	public Vector2 getTextPosition() {
		if(name.length == 1) {
			if(max_width == 0)
				return new Vector2(pos);
			else {
				return new Vector2(pos.getX()+al_x, pos.getY());
			}
		} else {
			float y = pos.getY();
			y -= getHeight(0) - getLineSpacing();
			return new Vector2(pos.getX(), y);
		}
	}

	public int getTextX() {
		if(max_width == 0)
			return pos.getX();
		else {
			return pos.getX()+al_x;
		}
	}
	public int getTextY() {
		if(name.length == 1) {
			return pos.getY();
		} else {
			float y = pos.getY();
			y -= getHeight(0) - getLineSpacing();
			return (int) y;
		}
	}


	public Vector2 getTotalPosition() {
		if(name.length == 1) {
			return new Vector2(pos);
		} else {
			float y = pos.getY();
				y -= getHeight(0) - getLineSpacing();
			return new Vector2(pos.getX(), y);
		}
	}
	public int getTotalX() {
		return pos.getX();
	}
	public int getTotalY() {
		if(name.length == 1) {
			return pos.getY();
		} else {
			float y = pos.getY();
			y -= getHeight(0) - getLineSpacing();
			return (int) y;
		}
	}


	//ay wird nicht berücksichtigt TODO
	public Vector2 getSize_Total() {		
		if(name.length == 1) {
			return new Vector2(getWidth(0), getHeight(0));	
		} else {
			float width = getWidth(0);
			float height = getHeight(0);
			
			
			for(int i = 1; i < name.length; i++) {
				
				height += getHeight(i) + getLineSpacing();
				
				float w = getWidth(i);
				if(w > width) {
					width = w;
				}
			}
			return new Vector2(width, height);
		}
	}
	
	//ay wird nicht berücksichtigt TODO
	public Vector4 getBounds_Total() {
		Vector2 v = getTotalPosition();
		
		if(name.length == 1) {
			return new Vector4(v.getX(), v.getY(), getWidth(0), getHeight(0));	
		} else {
			float width = getWidth(0);
			float height = getHeight(0);
			
			int x = (int) (v.getX() - height  - getLineSpacing());
			
			for(int i = 1; i < name.length; i++) {
				
				height += getHeight(i) + getLineSpacing();
				
				float w = getWidth(i);
				if(w > width) {
					width = w;
				}
			}
			return new Vector4(x, v.getY(), width, height);
		}
	}
	
	public float getYOffsetDown(int i) {
		float height = 0;
		
		for(int a = 0; a < name[i].length;a++) {
			OwnCharData ch = name[i][a];
			
			float[] data = font.DATA.getInfo(ch.getIdentifier());
			float a_y = data[1];
			
			
			if(a_y < height) height = a_y;
			
			
		}	
		return height;
	}
	
	public float getYOffsetUp(int i) {
		float height = 0;

        for (OwnCharData ch : name[i]) {
            float[] data = font.DATA.getInfo(ch.getIdentifier());
            float a_y = data[1];

            if (a_y > height) height = a_y;


        }
		return height;
	}
	
	
	public void setScale(float scale) {
		this.scale = scale;
		if(max_width != 0) changed[0] = true;
		changed[3] = true;
	}

	public void setWidth(int width) {
		scale = 1f;
		this.scale = width / getWidth();
		if(max_width != 0) changed[0] = true;
		changed[3] = true;
	}

	public void setHeight(int height) {
		scale = 1f;
        this.scale = height / getHeight();
		if(max_width != 0) changed[0] = true;
		changed[3] = true;
	}
	
	public float getScale() {
		return scale;
	}
	
	public float getScale_Total() {
		return scale * font.scale;
	}

	public Text set_LastDrawnChar(int i) {
		last_drawn_char = i;
		return this;
	}

	public int get_LastDrawnChar() {
		return last_drawn_char;
	}

	public int getHoveredChar() {
		int mouse_x = (int) Input.mouse.getX();
		int mouse_y = (int) Input.mouse.getY();
		int pos_x = (int) getTotalPosition().getX();
		int cur_width = 0;
		int char_spacing = (int) (font.getCharSpacing() * scale);
		if(mouse_y < getTotalY() || mouse_y > getTotalY() + getHeight()) return -1;
		int a = 0;
		if(text_align == Text_Align.RIGHT_BOUND) a = last_drawn_char;

		for (; a < name[0].length; a++) {
			OwnCharData ch = name[0][a];
			int char_width = (int) font.getCharSize(ch.getIdentifier()).getWidth();

			float  bx = pos_x + cur_width * scale;

			if(mouse_x > bx &&  mouse_x < pos_x + (cur_width + char_width*0.5f) * scale + char_spacing) {
				return a;
			} else if(mouse_x > bx && mouse_x < pos_x + (cur_width + char_width) * scale + char_spacing) {
				return a + 1;
			}
			cur_width += char_width + font.getCharSpacing() ;
		}
		return -1;
	}

	public int getHoveredChar(int mouse_x, int mouse_y) {
		int pos_x = (int) getTotalPosition().getX();
		int cur_width = 0;
		int char_spacing = (int) (font.getCharSpacing() * scale);
		if(mouse_y < getTotalY() || mouse_y > getTotalY() + getHeight()) return -1;
		int a = 0;
		if(text_align == Text_Align.RIGHT_BOUND) a = last_drawn_char;

		for (; a < name[0].length; a++) {
			OwnCharData ch = name[0][a];
			int char_width = (int) font.getCharSize(ch.getIdentifier()).getWidth();

			float  bx = pos_x + cur_width * scale;

			if(mouse_x > bx &&  mouse_x < pos_x + (cur_width + char_width*0.5f) * scale + char_spacing) {
				return a;
			} else if(mouse_x > bx && mouse_x < pos_x + (cur_width + char_width) * scale + char_spacing) {
				return a + 1;
			}
			cur_width += char_width + font.getCharSpacing() ;
		}
		return -1;
	}


//	public void set_Skip_Chars(int amount){
//		this.skip_char = amount;
//		changed[0] = true;
//	}


	public DrawMap toTexture(Color color) {

		DrawMap in = new DrawMap(font.getURLPath());

		int name_width = 0;
		int name_height = 0;



		int[] line_widhts = new int[name.length];
		int[] line_heights = new int[name.length];
		int[] abs_y = new int[name.length];





		for(int lines = 0; lines < name.length; lines++) {
			OwnCharData[] line = name[lines];

			int line_width = 0;
			int line_height = 0;
			int line_abs_y = 0;

			for (OwnCharData c : line) {
				float[] data = font.DATA.getInfo(c.getIdentifier());
				Vector4 charRegion = font.DATA.getRegion(c.getIdentifier());

				int char_absy = (int) data[1];
				int char_height = (int) (charRegion.getHeight() + data[1]);


				line_width += (int) (charRegion.getWidth() + font.char_spacing);

				if (line_height < char_height) line_height = (int) char_height;
				if (line_abs_y > char_absy) line_abs_y = char_absy;

			}

			if(name_width < line_width) name_width = line_width;
			name_height += line_height - line_abs_y + getLineSpacing();

			line_heights[lines] = line_height;
			line_widhts[lines] = line_width;
			abs_y[lines] = line_abs_y;
		}

//		System.out.println(name[0] + " : " + name_width + " : " + name_height);
		DrawMap out = new DrawMap(new Vector2(name_width, name_height), Color.ALPHA());


		int pos_x;
		if(text_align == Text_Align.MIDDLE) { //TODO
			pos_x = (int) ((max_width - line_widhts[0]) * 0.5f);
		} else {
			pos_x = 0;
		}

		int pos_y = line_heights[0];


		for(int var1 = 0; var1 < name.length; var1++) {
			for(int i = 0; i < name[var1].length; i++) {
				OwnCharData c = name[var1][i];

				float[] data = font.DATA.getInfo(c.getIdentifier());
				Vector4 charRegion = font.DATA.getRegion(c.getIdentifier());

				for (int x = 0; x < charRegion.getWidth(); x++) {
					for (int y = 0; y < charRegion.getHeight(); y++) {
//						System.out.println(""+c + " " + (charRegion.getX() + x) + " " +(charRegion.getY()+ (charRegion.getHeight()-1-y)));
						Color c2;
						c2 = in.getPixel((int)(charRegion.getX() + x), (int)(charRegion.getY()+ (charRegion.getHeight()-y)));
						if(c2!= null && c2.getAlpha() > 180)
							out.drawPixel(pos_x + x, pos_y - y - (int)data[1] , color);
					}
				}
				pos_x += (int) (charRegion.getWidth() + font.char_spacing);

			}
			if(var1 < name.length-1)
				pos_y += line_heights[var1+1] - abs_y[var1+1];

			if(text_align == Text_Align.MIDDLE) {//TODO
				if(var1 < name.length-1)
					pos_x = (int) ((max_width - line_widhts[var1+1]) * 0.5f);

			}

			else pos_x = 0;

		}

//		out.setScale(font.getScale()*scale);

		return out;
	}

	public static void destroy() {
		for(String url : caches.keySet()) {
			FontCache cc = caches.get(url);
			for (Color c : cc.font_colors.keySet()) {
				cc.font_colors.get(c).destroy();
			}

			for(Color ch : cc.cached_chars_v2.keySet()) {
				HashMap<Identifier, CharTexture> map = cc.cached_chars_v2.get(ch);
				for(Identifier id : map.keySet()) {
					CharTexture ft = map.get(id);
					ft.destroy();
				}
			}

			cc.cached_chars_v2.clear();
		}
	}


	public void draw(MasterRenderer renderer) {	
		draw(renderer, pos.getX(), pos.getY(), 0);
	}
	
	public void draw(MasterRenderer renderer, int layer) {	
		draw(renderer, pos.getX(), pos.getY(), layer);
	}
	
	public void draw(MasterRenderer renderer, float x, float y) {
		draw(renderer, x, y, 0);		
	}

	//button zu textur umwandeln und die dann zeichnen //und jeweils die farben cachen 
	//nachschauen ob font regions die mehrmahls gezeichnet�werden auch nur einmal geladen werden im master renderer


	private void update(int pos_x, int pos_y) {
		FontCache fontCache = caches.get(font.getPath());
		float char_spacing = font.char_spacing * font.getScale();
		float all_scale = getScale_Total();

		if(fontCache == null) {
			caches.put(font.getPath(), new FontCache());
			fontCache = caches.get(font.getPath());
		}
		//	cache					name
		if(changed[0] || fontCache.isEmpty() || (changed[3] && changed[7])) {
			ArrayList<Identifier> needed = new ArrayList<>();
            float y = pos_y;

			for (int i = 0; i < name.length; i++) {
				float width = 0;
				int max_height = 0;
				int length = 0;
				if(Text_Align.LEFT_BOUND == text_align) {
					last_drawn_char = name[i].length;
				} else
					last_drawn_char = 0;
				if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND) {
					length = name[i].length - shift_char -1;
				}
				for (int g = 0; g < name[i].length-shift_char; g++) {
					int a=length-g;
					int j = Math.abs(a);
//					if(Text_Align.RIGHT_BOUND == text_align && a < 0) break;

					OwnCharData oc = name[i][j];
					float[] data = font.DATA.getInfo(oc.getIdentifier());

					int ax = getOffsetX(i);

					Vector2 charBounds = font.getCharSize(oc.getIdentifier());

					int e_x = 0;
					if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND) {
						e_x = (int) (pos_x + max_width - (width  + charBounds.getWidth()) * scale);
					} else {
						e_x = (int) (pos_x + ax + (width * scale));
					}

					int e_height = (int) (charBounds.getHeight()*scale);
					if(e_height > max_height) max_height = e_height;
					float e_y =  y + (data[1]*all_scale);

					if(fontCache.getCachedTexture(color, oc.getIdentifier()) == null) {
						needed.add(oc.getIdentifier());
					}

					oc.setScale(all_scale);
					oc.setPosition(e_x, (int) e_y);

					if(max_width != 0 && (width + charBounds.getWidth() + char_spacing) * scale > max_width) {
						al_x = (int) (max_width - width);
						last_drawn_char = j+1;
						break;
					}
					width += charBounds.getWidth() + char_spacing;
				}

				y -= (max_height + getLineSpacing());

			}
			//load the needed chars into the cache
			if(!needed.isEmpty()) {
				DrawMap m;
				if(fontCache.font_colors.containsKey(color))
					m = fontCache.font_colors.get(color);
				else {
					m = font.colorize(color);	
					fontCache.font_colors.put(color, m);
				}
                for (Identifier identifier : needed) {
                    CharTexture reg = new CharTexture(m, font.DATA.getRegion(identifier));
                    reg.create();
					fontCache.addCharToCache(color, identifier, reg);
                }
			}		
		//-----------------------------------
			changed[0] = false;
			changed[1] = false;
			changed[2] = false;
			changed[3] = false;
			changed[4] = false;
			changed[5] = false;
			changed[6] = false;
			changed[7] = false;
			return;
		}
		if (changed[7]) {
			update_c7_alignments(fontCache, pos_x, pos_y);
		}
		if(changed[1]) {
			update_c1_color(fontCache);
		}
		if (changed[3]) {
			update_c3_scale(fontCache, pos_x, pos_y);
		}
		if (changed[5] && changed[6]) {
			update_c56_xy(fontCache, pos_x, pos_y);
		} else if(changed[5]) {
			update_c5_x(pos_x);
		} else if(changed[6]) {
			update_c6_y(pos_y);
		}
	}

	public static void update() {
//        for (Text t : texts) {
//            t.update(t.pos.getX(), t.pos.getY());
//        }
	}

	/**
	 *		0 = namechanged
	 *		1 = colorchanged
	 *		2 = formatting changed (thick=obsolete)
	 *   	3 = scale changed
	 *   	4 = position changed
	 *
	 */
	public void draw(MasterRenderer renderer, float x, float y, int layer) {
		if(x != pos.getX())
			changed[5] = true;
		if(y != pos.getY())
			changed[6] = true;
//		changed[4] = true;

		update((int) x,(int) y);

		FontCache fontCache = caches.get(font.getPath());

		if(max_width != 0) {
			if(text_align == Text_Align.RIGHT_BOUND) {
				for (OwnCharData[] ownCharData : name) {
//					System.out.println(last_drawn_char);
					for (int j = last_drawn_char; j < ownCharData.length - shift_char; j++) {
						OwnCharData oc = ownCharData[j];
						CharTexture region = fontCache.getCachedTexture(color, oc.getIdentifier());

						if (region != null) {
							renderer.processChar(region, oc, layer);
						}
					}
				}
			} else {
				for (OwnCharData[] ownCharData : name) {
					for (int j = 0; j < last_drawn_char; j++) {
						OwnCharData oc = ownCharData[j];
						CharTexture region = fontCache.getCachedTexture(color, oc.getIdentifier());
						if (region != null) {
							renderer.processChar(region, oc, layer);
						}
					}
				}
			}
		} else {
			for (OwnCharData[] ownCharData : name) {
				for (OwnCharData oc : ownCharData) {
					CharTexture region = fontCache.getCachedTexture(color, oc.getIdentifier());
					if (region != null) {
						renderer.processChar(region, oc, layer);
					}
				}
			}
		}
	}


	private void update_c1_color(FontCache fontCache) {
		ArrayList<Identifier> needed = new ArrayList<>();
		// color changed
		for (OwnCharData[] line : name)
			for (OwnCharData oc : line)
				if(fontCache.getCachedTexture(color, oc.getIdentifier()) == null)
					needed.add(oc.getIdentifier());
		//load the needed chars into the cache
		if(!needed.isEmpty()) {
			DrawMap m;
			if(fontCache.font_colors.containsKey(color))
				m = fontCache.font_colors.get(color);
			else {
				m = font.colorize(color);
				fontCache.font_colors.put(color, m);
			}
			for (Identifier identifier : needed) {
				CharTexture reg = new CharTexture(m, font.DATA.getRegion(identifier));
				reg.create();
				fontCache.addCharToCache(color, identifier, reg);
			}
		}
		changed[1] = false;
	}
	private void update_c3_scale(FontCache fontCache, int pos_x, int pos_y) {
		float all_scale = getScale_Total();
		float char_spacing = font.char_spacing * font.getScale();
		float y = pos_y;
		for (int i = 0; i < name.length; i++) {
			float width = 0;
			int max_height = 0;
			int length = 0;
			if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND) {
				length = name[i].length -1 - shift_char;
			}

			if(Text_Align.LEFT_BOUND == text_align) {
				last_drawn_char = name[i].length;
			} else
				last_drawn_char = 0;

			for (int g = 0; g < name[i].length-shift_char; g++) {
				int a=length-g;
				int j = Math.abs(a);
				if(Text_Align.RIGHT_BOUND == text_align && a < 0) break;
				OwnCharData oc = name[i][j];
				float[] data = font.DATA.getInfo(oc.getIdentifier());

				int ax = getOffsetX(i);

				Vector2 charBounds = font.getCharSize(oc.getIdentifier());

				int e_x = 0;
				if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND) {
					e_x = (int) (pos_x + max_width - (width  + charBounds.getWidth()) * scale);
				} else {
					e_x = (int) (pos_x + ax + (width * scale));
				}

				int e_height = (int) (charBounds.getHeight()*scale);
				if(e_height > max_height) max_height = e_height;
				float e_y =  y + (data[1]*all_scale);

				oc.setScale(all_scale);
				oc.setPosition(e_x, (int) e_y);

				if(max_width != 0 && (width + charBounds.getWidth() + char_spacing) * scale > max_width) {
					al_x = (int) (max_width - width);
					last_drawn_char = j+1;
					break;
				}
				width += charBounds.getWidth() + char_spacing;
			}
			y -= (max_height + getLineSpacing());
		}
		changed[3] = false;
		changed[4] = false;
		changed[5] = false;
		changed[6] = false;
	}
	private void update_c56_xy(FontCache fontCache, int pos_x, int pos_y) {
		float all_scale = getScale_Total();
		float char_spacing = font.char_spacing * font.getScale();
		float y = pos_y;

		for (int i = 0; i < name.length; i++) {
			float width = 0;
			int max_height = 0;
			int length = 0;
			if (max_width != 0 && text_align == Text_Align.RIGHT_BOUND)
				length = name[i].length - 1;

			if(Text_Align.LEFT_BOUND == text_align) {
				last_drawn_char = name[i].length;
			} else
				last_drawn_char = 0;

			for (int g = 0; g < name[i].length; g++) {
				int j = Math.abs(length - g);
				OwnCharData oc = name[i][j];
				float[] data = font.DATA.getInfo(oc.getIdentifier());

				int ax = getOffsetX(i);

				Vector2 charBounds = font.getCharSize(oc.getIdentifier());

				int e_x = 0;
				if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND) {
					e_x = (int) (pos_x + max_width - (width  + charBounds.getWidth()) * scale);
				} else {
					e_x = (int) (pos_x + ax + (width * scale));
				}

				int e_height = (int) (charBounds.getHeight()*scale);
				if(e_height > max_height) max_height = e_height;
				float e_y =  y + (data[1]*all_scale);

				oc.setScale(all_scale);
				oc.setPosition(e_x, (int) e_y);

				if(max_width != 0 && (width + charBounds.getWidth() + char_spacing) * scale > max_width) {
					al_x = (int) (max_width - width);
					last_drawn_char = j+1;
					break;
				}
				width += charBounds.getWidth() + char_spacing;
			}
			y -= (max_height + getLineSpacing());
		}
		changed[4] = false;
		changed[5] = false;
		changed[6] = false;
	}

	private void update_c5_x(int pos_x) { //  position
		float char_spacing =  font.char_spacing * font.getScale();

		for (int i = 0; i < name.length; i++) {
			float width = 0;
			int length = 0;
			if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND)
				length = name[i].length - 1 - shift_char;
			if(Text_Align.LEFT_BOUND == text_align) {
				last_drawn_char = name[i].length;
			} else last_drawn_char = 0;
			for (int g = 0; g < name[i].length; g++) {
				int j = Math.abs(length - g);
				OwnCharData oc = name[i][j];

				int ax = getOffsetX(i);

				Vector2 charBounds = font.getCharSize(oc.getIdentifier());

				int e_x = 0;
				if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND) {
					e_x = (int) (pos_x + max_width - (width  + charBounds.getWidth()) * scale);
				} else {
					e_x = (int) (pos_x + ax + (width * scale));
				}

				oc.setPosX(e_x);

				if(max_width != 0 && (width + charBounds.getWidth() + char_spacing) * scale > max_width) {
					al_x = (int) (max_width - width);
					last_drawn_char = j+1;
					break;
				}
				width += charBounds.getWidth() + char_spacing;
			}
		}
		changed[4] = false;
		changed[5] = false;
	}

	private void update_c6_y(int pos_y) { //  position
		float y = pos_y;
		float all_scale = getScale_Total();

		for (OwnCharData[] ownCharData : name) {
			int max_height = 0;
			for (OwnCharData oc : ownCharData) {
				float[] data = font.DATA.getInfo(oc.getIdentifier());

				int e_y = (int) (y + (data[1] * all_scale));
				oc.setPosY(e_y);
			}
			y -= (max_height + getLineSpacing());
		}
		changed[4] = false;
		changed[6] = false;
	}
	int al_x = 0;

	private void update_c7_alignments(FontCache fontCache, int pos_x, int pos_y) {
		float all_scale = getScale_Total();
		float char_spacing = font.char_spacing * font.getScale();

		ArrayList<Identifier> needed = new ArrayList<>();
		float y = pos_y;

		for (int i = 0; i < name.length; i++) {
			float width = 0;
			int max_height = 0;
			int length = 0;
			if(Text_Align.LEFT_BOUND == text_align) {
				last_drawn_char = name[i].length;
			} else
				last_drawn_char = 0;
			if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND) {
				length = name[i].length - shift_char -1;
			}
			for (int g = 0; g < name[i].length-shift_char; g++) {
				int j = Math.abs(length - g);
				OwnCharData oc = name[i][j];
				float[] data = font.DATA.getInfo(oc.getIdentifier());

				int ax = getOffsetX(i);

				Vector2 charBounds = font.getCharSize(oc.getIdentifier());

				int e_x = 0;
				if(max_width != 0 && text_align == Text_Align.RIGHT_BOUND) {
					e_x = (int) (pos_x + max_width - (width  + charBounds.getWidth()) * scale);
				} else {
					e_x = (int) (pos_x + ax + (width * scale));
				}


				int e_height = (int) (charBounds.getHeight()*scale);
				if(e_height > max_height) max_height = e_height;
				float e_y =  y + (data[1]*all_scale);

				oc.setPosition(e_x, (int) e_y);

				if(fontCache.getCachedTexture(color, oc.getIdentifier()) == null)
					needed.add(oc.getIdentifier());

				if(max_width != 0 && (width + charBounds.getWidth() + char_spacing) * scale > max_width) {
					al_x = (int) (max_width - width);
					last_drawn_char = j+1;
					break;
				}
				width += charBounds.getWidth() + char_spacing;
			}

			y -= (max_height + getLineSpacing());

		}
		//load the needed chars into the cache
		if(!needed.isEmpty()) {
			DrawMap m;
			if(fontCache.font_colors.containsKey(color))
				m = fontCache.font_colors.get(color);
			else {
				m = font.colorize(color);
				fontCache.font_colors.put(color, m);
			}
			for (Identifier identifier : needed) {
				CharTexture reg = new CharTexture(m, font.DATA.getRegion(identifier));
				reg.create();
				fontCache.addCharToCache(color, identifier, reg);
			}
		}
		//-----------------------------------
		changed[0] = false;
		changed[1] = false;
		changed[2] = false;
		changed[4] = false;
		changed[5] = false;
		changed[6] = false;
		changed[7] = false;
	}



}

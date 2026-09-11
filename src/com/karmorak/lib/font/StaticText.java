package com.karmorak.lib.font;

import com.karmorak.lib.Color;
import com.karmorak.lib.ColorPreset;
import com.karmorak.lib.Colorable;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.engine.graphic.flat.CharTexture;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.font.identifier.Identifier;
import com.karmorak.lib.font.ownchar.OwnChar;
import com.karmorak.lib.font.ownchar.OwnCharData;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;
import com.karmorak.lib.math.Vector4;

import java.util.ArrayList;
import java.util.HashMap;

//based on Text v1.5.1
class StaticText {

    private final OwnFont font;

    private final String stringed_name;
    private final OwnCharData[] name;
    private final Color color;
    private final Vector2i pos;
    private float scale = 1f;

    /**
     * 0 = namechanged;
     * 1 = colorchanged;
     * 2 = formatting changed (thick=obsolete);
     * 3 = scale changed;
     * 4 = position changed
     * 5 = pos_x changed
     * 6 = pos_y changed
     */
    private final boolean[] changed = new boolean[]{true, true, true, true, true, true, true, true};
//	public static final HashMap<Color, HashMap<Character, Texture>> REGIONCACHE = new HashMap<Color, HashMap<Character, Texture>>();

    static HashMap<String, FontCache> caches = new HashMap<>();

//    public static final ArrayList<StaticText> texts = new ArrayList<Text>();


    private record FontCache(HashMap<Color, DrawMap> font_colors,
                             HashMap<Color, HashMap<Identifier, CharTexture>> cached_chars_v2) {
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

    public record chardata(Color color, Identifier ident) {
    }


    public StaticText(OwnFont font, String name) {
        this.font = font;
        this.name = OwnChar.createOwnChars(name, font);
        this.stringed_name = name;

        color = ColorPreset.BLACK.toColor();

//        longest_line_width_raw = font.getWordBounds_Raw(name).getWidth();

        this.pos = new Vector2i(0, 0);
    }


    public String getName() {
        return stringed_name;
    }

    public int getNameLength() {
        return name.length;
    }

    public void setX(int x) {
        if (x != pos.getX())
            changed[5] = true;

        pos.setX(x);
        changed[4] = true;
    }

    public void setY(int y) {
        if (y != pos.getY())
            changed[6] = true;

        pos.setY(y);
        changed[4] = true;
    }

    public void setPosition(int x, int y) {
        if (x != pos.getX())
            changed[5] = true;
        if (y != pos.getY())
            changed[6] = true;

        pos.set(x, y);
        changed[4] = true;
    }

    public void setPosition(float x, float y) {
        if (x != pos.getX())
            changed[5] = true;
        if (y != pos.getY())
            changed[6] = true;

        pos.set(x, y);
        changed[4] = true;
    }

    public int getAbs() {

        int abs = 0;

        for (OwnCharData c : name) {
            float[] info = font.DATA.getInfo(c.getIdentifier());

            if (info[1] < abs) abs = (int) info[1];
        }
        return abs;
    }

    public void setColor(Colorable c) {
        if (!c.equals(color)) {
//            color = c.toColor();
            changed[1] = true;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setCharSpacing(int i) {
        font.setCharSpacing(i);
    }

    public float getCharSpacing() {
        return font.char_spacing * getTotalScale();
    }

    @SuppressWarnings("unused")
    private float getCharSpacing_Raw() {
        return font.char_spacing;
    }


    public void updateState(int i) {
        changed[i] = true;
    }


    public float getWidth() {
        return font.getWordWidth_Raw(name) * getTotalScale();
    }

    public int getWidth(int startIndex, int endIndex) {
        return (int) (font.getWordWidth_Raw(name, startIndex, endIndex) * getTotalScale());
    }

    public float getHeight() {
        return font.getWordHeight_Raw(name) * getTotalScale();
    }

    //ay wird zwar berücksichtigt aber wahrscheinlich dennoch veraltet
    public float getWholeHeight() {
        float height = 0;


        for (OwnCharData ch : name) {
            Vector2 bounds = font.getCharSize(ch.getIdentifier());
            float[] data = font.DATA.getInfo(ch.getIdentifier());
            float a_y = data[1];
            if (a_y < 0) a_y = a_y * -1;

            if (bounds.getHeight() + a_y > height) height = bounds.getHeight() + a_y;

        }
        return height;
    }

    public Vector2 getTextPosition() {
        return new Vector2(pos.getX() + al_x, pos.getY());
    }

    public int getTotalHeight() {
        return (int) getHeight();
    }

    public void setScale(float scale) {
        this.scale = scale;
        changed[3] = true;
    }

    public void setWidth(int width) {
        scale = 1f;
        this.scale = width / getWidth();
        changed[3] = true;
    }

    public void setHeight(int height) {
        scale = 1f;
        this.scale = height / getHeight();
        changed[3] = true;
    }

    public void setTotalHeight(float height) {
        scale = 1f;
        this.scale = height / getTotalHeight();
        changed[3] = true;
    }


    public float getScale() {
        return scale;
    }

    public float getTotalScale() {
        return scale * font.scale;
    }

    public Texture toTexture(Color color) {//TODO

        DrawMap in = new DrawMap(font.getURLPath());

//		System.out.println(name[0] + " : " + name_width + " : " + name_height);
        DrawMap out = new DrawMap(new Vector2(getWidth(), getHeight()), Color.ALPHA());


        int pos_x = 0;
        int pos_y = 0;

        for (OwnCharData c : name) {

            float[] data = font.DATA.getInfo(c.getIdentifier());
            Vector4 charRegion = font.DATA.getRegion(c.getIdentifier());

            for (int x = 0; x < charRegion.getWidth(); x++) {
                for (int y = 0; y < charRegion.getHeight(); y++) {
//						System.out.println(""+c + " " + (charRegion.getX() + x) + " " +(charRegion.getY()+ (charRegion.getHeight()-1-y)));
                    Color c2;
                    c2 = in.getPixel((int) (charRegion.getX() + x), (int) (charRegion.getY() + (charRegion.getHeight() - y)));
                    if (c2 != null && c2.getAlpha() > 180)
                        out.drawPixel(pos_x + x, pos_y - y - (int) data[1], color);
                }
            }
            pos_x += (int) (charRegion.getWidth() + font.char_spacing);
        }


        return new Texture(out);
    }

    public static void destroy() {
        for (String url : caches.keySet()) {
            FontCache cc = caches.get(url);
            for (Color c : cc.font_colors.keySet()) {
                cc.font_colors.get(c).destroy();
            }

            for (Color ch : cc.cached_chars_v2.keySet()) {
                HashMap<Identifier, CharTexture> map = cc.cached_chars_v2.get(ch);
                for (Identifier id : map.keySet()) {
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
        float all_scale = getTotalScale();

        if (fontCache == null) {
            caches.put(font.getPath(), new FontCache());
            fontCache = caches.get(font.getPath());
        }
        //	cache					name
        if (fontCache.isEmpty()) {
            ArrayList<Identifier> needed = new ArrayList<>();
            float width = 0;
            int max_height = 0;
            for (OwnCharData oc : name) {
                float[] data = font.DATA.getInfo(oc.getIdentifier());

                Vector2 charBounds = font.getCharSize(oc.getIdentifier());

                int e_x = (int) (pos_x + (width * scale));

                int e_height = (int) (charBounds.getHeight() * scale);
                if (e_height > max_height) max_height = e_height;
                float e_y = pos_y + (data[1] * all_scale);

                if (fontCache.getCachedTexture(color, oc.getIdentifier()) == null) {
                    needed.add(oc.getIdentifier());
                }

                oc.setScale(all_scale);
                oc.setPosition(e_x, (int) e_y);
                width += charBounds.getWidth() + char_spacing;
            }
            //load the needed chars into the cache
            if (!needed.isEmpty()) {
                DrawMap m;
                if (fontCache.font_colors.containsKey(color))
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

        if (changed[3]) {
            update_c3_scale(fontCache, pos_x, pos_y);
        }
        if (changed[5] && changed[6]) {
            update_c56_xy(fontCache, pos_x, pos_y);
        } else if (changed[5]) {
            update_c5_x(pos_x);
        } else if (changed[6]) {
            update_c6_y(pos_y);
        }
    }

    public static void update() {
//        for (Text t : texts) {
//            t.update(t.pos.getX(), t.pos.getY());
//        }
    }

    /**
     * 0 = namechanged
     * 1 = colorchanged
     * 2 = formatting changed (thick=obsolete)
     * 3 = scale changed
     * 4 = position changed
     *
     */
    public void draw(MasterRenderer renderer, float x, float y, int layer) {
        if (x != pos.getX())
            changed[5] = true;
        if (y != pos.getY())
            changed[6] = true;

        update((int) x, (int) y);

        FontCache fontCache = caches.get(font.getPath());


        for (OwnCharData oc : name) {
            CharTexture region = fontCache.getCachedTexture(color, oc.getIdentifier());
            if (region != null) {
                renderer.processChar(region, oc, layer);
            }
        }
    }

    private void update_c3_scale(FontCache fontCache, int pos_x, int pos_y) {
        float all_scale = getTotalScale();
        float char_spacing = font.char_spacing * font.getScale();
        float y = pos_y;
        float width = 0;
        int max_height = 0;

        for (OwnCharData oc : name) {
            float[] data = font.DATA.getInfo(oc.getIdentifier());

            Vector2 charBounds = font.getCharSize(oc.getIdentifier());

            int e_x = (int) (pos_x + (width * scale));


            int e_height = (int) (charBounds.getHeight() * scale);
            if (e_height > max_height) max_height = e_height;
            float e_y = y + (data[1] * all_scale);

            oc.setScale(all_scale);
            oc.setPosition(e_x, (int) e_y);

            width += charBounds.getWidth() + char_spacing;
        }
        changed[3] = false;
        changed[4] = false;
        changed[5] = false;
        changed[6] = false;
    }

    private void update_c56_xy(FontCache fontCache, int pos_x, int pos_y) {
        float all_scale = getTotalScale();
        float char_spacing = font.char_spacing * font.getScale();
        float width = 0;
        int max_height = 0;
        for (OwnCharData oc : name) {
            float[] data = font.DATA.getInfo(oc.getIdentifier());


            Vector2 charBounds = font.getCharSize(oc.getIdentifier());

            int e_x = (int) (pos_x + (width * scale));


            int e_height = (int) (charBounds.getHeight() * scale);
            if (e_height > max_height) max_height = e_height;
            float e_y = pos_y + (data[1] * all_scale);

            oc.setScale(all_scale);
            oc.setPosition(e_x, (int) e_y);

            width += charBounds.getWidth() + char_spacing;
        }
        changed[4] = false;
        changed[5] = false;
        changed[6] = false;
    }

    private void update_c5_x(int pos_x) { //  position
        float char_spacing = font.char_spacing * font.getScale();
        float width = 0;
        for (OwnCharData oc : name) {

            Vector2 charBounds = font.getCharSize(oc.getIdentifier());

            int e_x = (int) (pos_x + (width * scale));

            oc.setPosX(e_x);

            width += charBounds.getWidth() + char_spacing;

        }
        changed[4] = false;
        changed[5] = false;
    }

    private void update_c6_y(int pos_y) { //  position
        float all_scale = getTotalScale();
        for (OwnCharData oc : name) {
            float[] data = font.DATA.getInfo(oc.getIdentifier());
            int e_y = (int) (pos_y + (data[1] * all_scale));
            oc.setPosY(e_y);
        }
        changed[4] = false;
        changed[6] = false;
    }

    int al_x = 0;


}

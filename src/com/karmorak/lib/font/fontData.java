package com.karmorak.lib.font;

import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.font.identifier.CharIdentifier;
import com.karmorak.lib.font.identifier.Identifier;
import com.karmorak.lib.font.identifier.StringIdentifier;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector4;
import com.karmorak.lib.utils.GraphicUtils;
import com.karmorak.lib.utils.file.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.karmorak.lib.font.OwnFont.DEFAULT_FONT_SIZE;

public class fontData {

    final String TITLE_NAME;
    final int FONT_SIZE;
    final URL path;
    final DrawMap texture;
    private final HashMap<Character, String> data;
    private final HashMap<String, String> special_data;
//		final HashMap<Character, TextureRegion> regions;

    public String getData(char c) {
        return data.get(c);
    }

    public String getData(Identifier ident) {
        switch (ident) {
            case CharIdentifier c -> {
                return data.get(c.value());
            }
            case StringIdentifier s -> {
                return special_data.get(s.value());
            }
        }
    }

    @SuppressWarnings("unused")
    public fontData(URL path, HashMap<Character, String> data, HashMap<String, String> special_data) {
        this.TITLE_NAME = "";
        this.FONT_SIZE = DEFAULT_FONT_SIZE;
        this.path = path;
        this.texture = new DrawMap(path);
        this.data = data;
        this.special_data = special_data;
    }

    @SuppressWarnings("unused")
    public fontData(DrawMap map, HashMap<Character, String> data, HashMap<String, String> special_data) {
        this.TITLE_NAME = "";
        this.FONT_SIZE = DEFAULT_FONT_SIZE;
        this.texture = map;
        this.path = null;
        this.data = data;
        this.special_data = special_data;
    }

    public fontData(String name, int font_size, URL path, HashMap<Character, String> data, HashMap<String, String> special_data) {
        this.TITLE_NAME = name;
        this.FONT_SIZE = font_size;
        this.path = path;
        this.texture = new DrawMap(path);
        this.data = data;
        this.special_data = special_data;
    }

    public fontData(String name, int font_size, DrawMap map, HashMap<Character, String> data, HashMap<String, String> special_data) {
        this.TITLE_NAME = name;
        this.FONT_SIZE = font_size;
        this.texture = map;
        this.path = null;
        this.data = data;
        this.special_data = special_data;
    }


    public static void saveFont(fontData data) {
        data.saveFont();
    }

    public void saveFont() {

        try {
            GraphicUtils.saveDrawMap(texture, KLIB.lib.LIB_TEMP_PATH() + "fonts//", TITLE_NAME + ".png");

            File f = new File(KLIB.lib.LIB_TEMP_PATH() + "fonts//" + TITLE_NAME + ".txt");

            String[] dataout = new String[data.size() + 2];

            dataout[0] = "name:" + TITLE_NAME;
            //dataout[1] = "opt_spacing:" + font.char_spacing;
            //dataout[2] = "opt_defscale:" + font.scale;
            dataout[1] = "font_size:" + FONT_SIZE;

            int line = 3;
            for(Character c : data.keySet()) {
                dataout[line] = ((int) c) + "_" + data.get(c);
                line++;
            }

            FileUtils.writeToFile(f, dataout);


        } catch (IOException _) {
            System.out.println("Cannot save FontFile!");
        }
    }

    static final ArrayList<String> new_warned_ids = new ArrayList<String>();

    /**
     * 0 = scale
     * 1 = a_y
     */
    float[] getInfo(Identifier ident) {

        float scale = 1f;
        float a_y = 0f;

        String[] s2;
        if(getData(ident) != null) {
            s2 = getData(ident).split("_");
        } else {
            String c = ident.toString();
            if(!new_warned_ids.contains(c)) {
                System.out.println("ERROR in getPostion(): Couldn't find identifier \""+ c + "\" " + ident.getInt());
                new_warned_ids.add(c);
            }
            s2 = getData((char) '*').split("_");
        }
        if(s2.length > 4) {
            for(int i = 4; i < s2.length; i++) {
                String s = s2[i];
                if(s.startsWith("s")) {
                    scale = Float.parseFloat(s.replace("s", ""));
                } if(s.startsWith("a")) {
                    a_y = Float.parseFloat(s.replace("ay", ""));
                }
            }
        }

        return new float[] {scale, a_y};
    }

    Vector2 getPosition(Identifier ident) {
        String[] s2;
        if(getData(ident) != null) {
            s2 = getData(ident).split("_");
        } else {
            String c = ident.toString();
            if(!new_warned_ids.contains(c)) {
                System.out.println("ERROR in getPostion(): Couldn't find identifier \""+ c + "\" " + ident.getInt());
                new_warned_ids.add(c);
            }
            s2 = getData((char) '*').split("_");
        }
        int x = Integer.parseInt(s2[0].replace("x", ""));
        int y = Integer.parseInt(s2[1].replace("y", ""));
        return new Vector2(x, y);
    }

    public Vector2 getBounds(Identifier ident) {
        String[] s2;
        if(getData(ident) != null) {
            s2 = getData(ident).split("_");
        } else {
            String c = ident.toString();
            if(!new_warned_ids.contains(c)) {
                System.out.println("ERROR in getBounds(): Couldn't find identifier \""+ c + "\" " + ident.getInt());
                new_warned_ids.add(c);
            }
            s2 = getData((char) '*').split("_");
        }

        int w = Integer.parseInt(s2[2].replace("w", ""));
        int h = Integer.parseInt(s2[3].replace("h", ""));
        return new Vector2(w, h);
    }

    public Vector4 getRegion(Identifier ident) {
        String[] s2;
        if(getData(ident) != null) {
            s2 = getData(ident).split("_");
        } else {
            String c = ident.toString();
            if(!new_warned_ids.contains(c)) {
                System.out.println("ERROR in getPostion(): Couldn't find identifier \""+ c + "\" " + ident.getInt());
                new_warned_ids.add(c);
            }
            s2 = getData((char) '*').split("_");
        }
        int x = Integer.parseInt(s2[0].replace("x", ""));
        int y = Integer.parseInt(s2[1].replace("y", ""));
        int w = Integer.parseInt(s2[2].replace("w", ""));
        int h = Integer.parseInt(s2[3].replace("h", ""));
        return new Vector4(x, y, w, h);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        fontData other = (fontData) obj;
        if (path == null) {
            return other.path == null;
        } else return path.equals(other.path);
    }
}
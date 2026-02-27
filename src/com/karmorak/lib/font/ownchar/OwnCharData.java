package com.karmorak.lib.font.ownchar;

import com.karmorak.lib.KLIB;
import com.karmorak.lib.font.OwnFont;
import com.karmorak.lib.font.identifier.CharIdentifier;
import com.karmorak.lib.font.identifier.Identifier;
import com.karmorak.lib.font.identifier.StringIdentifier;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;

import java.util.ArrayList;

public abstract class OwnCharData {

    protected final Vector2i pos;
    protected final Vector2i bounds;

    protected final Identifier identifier;

    protected final Vector2 translated_pos;
    protected final Vector2 translated_size;

    float scale = 1f;

    protected OwnCharData(Identifier identifier) {
        pos = new Vector2i();
        bounds = new Vector2i();
        translated_pos = new Vector2();
        translated_size = new Vector2();
        this.identifier = identifier;
    }


    public void setPosition(int x, int y) {

        if(x != pos.getX())
            translated_pos.setX(translatePosX(x));
        if(y != pos.getY())
            translated_pos.setY(translatePosY(y));

        pos.set(x, y);
    }

    public void setPosX(int x) {
        translated_pos.setX(translatePosX(x));
        pos.setX(x);
    }

    public void setPosY(int y) {
        translated_pos.setY(translatePosY(y));
        pos.setY(y);
    }

    public Vector2i getPosition() {
        return pos;
    }
    public Vector2i getBounds() {
        return bounds;
    }


    public void setScale(float scale) {
        this.scale = scale;

        float tWidth = bounds.getWidth() * scale;
        float tHeight = bounds.getHeight() * scale;

        float sWidth =  KLIB.graphic.Width() ;
        float sHeight =  KLIB.graphic.Height();

        translated_size.set((tWidth / sWidth), (tHeight/sHeight));
    }

    public float getScale() {
        return scale;
    }

    public Vector2 getTranslatedPosition() {
        return translated_pos;
    }

    public Vector2 getTranslatedSize() {
        return translated_size;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    protected float translatePosX(int x) {

        return (bounds.getWidth() * scale + 2f*x) / KLIB.graphic.Width() - 1;
    }

    protected float translatePosY(int y) {

        return (bounds.getHeight() * scale + 2f*y) / KLIB.graphic.Height() - 1;
    }

    @Override
    public abstract String toString();

    public static OwnCharData[] createOwnChars(String name, OwnFont font) {

        //OwnCharData[] names = new OwnChar[name.length()];
        ArrayList<OwnCharData> names = new ArrayList<>();

        String special_char = "";
        boolean found = false;

        for (int j = 0; j < name.length(); j++) {
            char c = name.charAt(j);
            if(!found) {
                if (c == '\u00A7') {
                    if(name.charAt(j+1) == '§') {
                        j++;
                        found = true;
                    }
                } else {
                    names.add(new OwnChar(font, new CharIdentifier(c)));
                }
            } else {
                if(j == name.length()-1) {
                    special_char = special_char + c;
                    names.add(new SpecialChar(font, new StringIdentifier(special_char)));
                    found = false;
                    special_char = "";
                } else if(c == ' ' || c == '_') {
                    names.add(new SpecialChar(font, new StringIdentifier(special_char)));
                    found = false;
                    special_char = "";
                } else {
                    special_char = special_char + c;
                }
            }
        }
        OwnCharData[] output = new OwnCharData[names.size()];
        for (int i = 0; i < names.size(); i++) {
            output[i] = names.get(i);
        }

        return output;
    }

    public static OwnCharData[][] createOwnChars(String[] name, OwnFont font) {
        //OwnCharData[][] names = new OwnChar[name.length][];
        ArrayList<ArrayList<OwnCharData>> names = new ArrayList<>();

        String special_char = "";
        boolean found = false;

        for (int i = 0; i < name.length; i++) {
            names.add(new ArrayList<>());
            String s = name[i];
            for (int j = 0; j < name[i].length(); j++) {
                char c = s.charAt(j);
                if(!found) {
                    if (c == '§') {
                        if(s.charAt(j+1) == '§') {
                            j+=1;
                            found = true;
                        }
                    } else {
                        names.get(i).add(new OwnChar(font, new CharIdentifier(c)));
                    }
                } else {
                    if(j == name[i].length()-1) {
                        special_char = special_char + c;
                        names.get(i).add(new SpecialChar(font, new StringIdentifier(special_char)));
                        found = false;
                        special_char = "";
                    } else if(c == ' ' || c == '_') {
                        names.get(i).add(new SpecialChar(font, new StringIdentifier(special_char)));
                        found = false;
                        special_char = "";
                    } else {
                        special_char = special_char + c;
                    }
                }
            }
        }


        OwnCharData[][] output = new OwnCharData[names.size()][];
        for (int j = 0; j < names.size(); j++) {
            ArrayList<OwnCharData> datas = names.get(j);
            output[j] = new OwnCharData[datas.size()];
            for (int i = 0; i < datas.size(); i++) {
                output[j][i] = datas.get(i);
            }
        }
        return output;
    }
}

package com.karmorak.lib.font.ownchar;

import com.karmorak.lib.font.OwnFont;
import com.karmorak.lib.font.identifier.StringIdentifier;

public class SpecialChar extends OwnCharData {

    SpecialChar(OwnFont font, StringIdentifier c) {
        super(c);
        bounds.set(font.DATA.getBounds(c));
        translated_pos.set(translatePosX(pos.getX()), translatePosY(pos.getY()));
        setScale(1f);
    }

    @Override
    public String toString() {
        return ((StringIdentifier) getIdentifier()).value();
    }

    protected void translatePosition(int x, int y) {
        translated_pos.set(translatePosX(x), translatePosY(y));
        pos.set(x, y);
    }

    protected void updatePosition() {
        translated_pos.set(translatePosX(pos.getX()), translatePosY(pos.getY()));
    }


}
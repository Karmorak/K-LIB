package com.karmorak.lib.font.ownchar;

import com.karmorak.lib.font.OwnFont;
import com.karmorak.lib.font.identifier.StringIdentifier;

public class SpecialChar extends OwnCharData {

    SpecialChar(OwnFont font, StringIdentifier c) {
        super(c);
        bounds.set(font.DATA.getBounds(c));
        pos.set(pos.getX(), pos.getY());
        setScale(1f);
    }

    @Override
    public String toString() {
        return ((StringIdentifier) getIdentifier()).value();
    }


    protected void updatePosition() {
        pos.set(pos.getX(), pos.getY());
    }


}
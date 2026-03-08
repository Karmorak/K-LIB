package com.karmorak.lib.font.ownchar;

import com.karmorak.lib.font.OwnFont;
import com.karmorak.lib.font.identifier.CharIdentifier;

public class OwnChar extends OwnCharData {


	OwnChar(OwnFont font, CharIdentifier c) {
		super(c);
		bounds.set(font.DATA.getBounds(c));
        pos.set(pos.getX(), pos.getY());
		setScale(1f);
	}

	@Override
	public String toString() {
		return "" + ((CharIdentifier) getIdentifier()).value();
	}



}
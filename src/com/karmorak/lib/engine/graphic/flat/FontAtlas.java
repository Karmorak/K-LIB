package com.karmorak.lib.engine.graphic.flat;

import com.karmorak.lib.font.identifier.CharIdentifier;
import com.karmorak.lib.math.Vector4i;
import jdk.jfr.Experimental;

import java.net.URL;
import java.util.HashMap;

@Experimental
class FontAtlas {

    private final HashMap<CharIdentifier, Vector4i> regions;

    private final int TEXTURE_ID, SRC_WIDTH, SRC_HEIGHT;
    private final URL SRC_PATH;

    FontAtlas(URL Path, HashMap<CharIdentifier, Vector4i> regions) {
        TextureData data = TextureConstruct.loadURL(Path, TextureConstruct.Type.TEXTURE);
        TEXTURE_ID = data.ID;
        SRC_PATH = Path;
        SRC_WIDTH = data.WIDTH;
        SRC_HEIGHT = data.HEIGHT;
        this.regions = regions;
    }

    CharTexture retrieveTexture(CharIdentifier identifier) {
        Vector4i region = regions.get(identifier);
        if (region != null) {
//            FontTexture texture = new FontTexture(new TextureData());
        }
        return null;
    }




}

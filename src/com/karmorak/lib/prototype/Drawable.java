package com.karmorak.lib.prototype;

import com.karmorak.lib.engine.graphic.MasterRenderer;

public interface Drawable {

    default void draw(MasterRenderer renderer) {
        this.draw(renderer, 0);
    }

    void draw(MasterRenderer renderer, int layer);

}

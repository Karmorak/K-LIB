package com.karmorak.lib.prototype;

import com.karmorak.lib.Input;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;

public interface Boxable {

    Boxable setX(float x);

    Boxable setY(float y);

    Boxable setWidth(float width);

    Boxable setHeight(float height);

    Boxable setPosition(float x, float y);

    Boxable setPosition(Vector2 position);

    Boxable setSize(float width, float height);

    Boxable setSize(Vector2 boundaries);

    float getX();

    float getY();

    float getWidth();

    float getHeight();

    Vector2 getPosition();

    Vector2 getSize();

    default Boxable setX(int x) {
        this.setX((float) x);
        return this;
    }

    default Boxable setY(int y) {
        this.setY((float) y);
        return this;
    }

    default Boxable setWidth(int width) {
        this.setWidth((float) width);
        return this;
    }

    default Boxable setHeight(int height) {
        this.setHeight((float) height);
        return this;
    }

    default Boxable setPosition(int x, int y) {
        this.setPosition((float) x, (float) y);
        return this;
    }

    default Boxable setSize(int width, int height) {
        this.setSize((float) width, (float) height);
        return this;
    }


}

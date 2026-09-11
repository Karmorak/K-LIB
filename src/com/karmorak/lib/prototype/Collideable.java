package com.karmorak.lib.prototype;

import com.karmorak.lib.Input;
import com.karmorak.lib.math.Vector2;

public abstract class Collideable implements Boxable {

    public Vector2 pos;
    public Vector2 size;

    @Override
    public Boxable setX(float x) {
        pos.setX(x);
        return this;
    }

    @Override
    public Boxable setY(float y) {
        pos.setY(y);
        return this;
    }

    @Override
    public Boxable setWidth(float width) {
        size.setWidth(width);
        return this;
    }

    @Override
    public Boxable setHeight(float height) {
        size.setHeight(height);
        return this;
    }

    @Override
    public Boxable setPosition(float x, float y) {
        pos.set(x, y);
        return this;
    }

    @Override
    public Boxable setPosition(Vector2 position) {
        this.pos.set(position);
        return this;
    }

    @Override
    public Boxable setSize(int width, int height) {
        size.set(width, height);
        return this;
    }

    @Override
    public Boxable setSize(float width, float height) {
        size.set(width, height);
        return this;
    }

    @Override
    public Boxable setSize(Vector2 boundaries) {
        size.set(boundaries);
        return this;
    }

    @Override
    public float getX() {
        return pos.getX();
    }

    @Override
    public float getY() {
        return pos.getY();
    }

    @Override
    public float getWidth() {
        return size.getWidth();
    }

    @Override
    public float getHeight() {
        return size.getHeight();
    }

    @Override
    public Vector2 getPosition() {
        return pos;
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    public boolean isColliding() {
        Vector2 mouse = Input.mouse;
        return mouse.getX() >= getX() && mouse.getX() <= getX() + getWidth()
                && mouse.getY() >= getY() && mouse.getY() <= getY() + getHeight();
    }

    public boolean isColliding(int x, int y) {
        return x >= getX() && x <= getX() + getWidth()
                && y >= getY() && y <= getY() + getHeight();
    }

    public boolean isColliding(float x, float y) {
        return x >= getX() && x <= getX() + getWidth()
                && y >= getY() && y <= getY() + getHeight();
    }

}

package com.karmorak.lib.math;

import com.karmorak.lib.math.vector.Vector;

import java.util.Objects;

public class Vector4i implements Vector {

    private int x, y, width, height;

    public Vector4i() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public Vector4i(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Vector4i(Vector2 pos, Vector2 size) {
        this.x = (int) pos.getX();
        this.y = (int) pos.getY();
        this.width = (int) size.getWidth();
        this.height = (int) size.getHeight();
    }

    public Vector4i(Vector2i pos, Vector2i size) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.width = size.getWidth();
        this.height = size.getHeight();
    }

    public Vector4i(Vector4 bounds) {
        this.x = (int) bounds.getX();
        this.y = (int) bounds.getY();
        this.width = (int) bounds.getWidth();
        this.height = (int) bounds.getHeight();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setX(float x) {
        this.x = (int) x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setY(float y) {
        this.y = (int) y;
    }

    public void setPosition(Vector2 pos) {
        this.x = (int) pos.getX();
        this.y = (int) pos.getY();
    }

    public void setPosition(Vector2i pos) {
        setPosition(pos.getX(), pos.getY());
    }

    public void setPosition(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i getPosition() {
        return new Vector2i(x, y);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(Vector2 bounds) {
      setSize((int) bounds.getWidth(),(int) bounds.getHeight());
    }

    public void setSize(Vector2i bounds) {
        setSize(bounds.getWidth(), bounds.getHeight());
    }

    public void setSize(float width, float height) {
        this.width = (int) width;
        this.height = (int) height;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Vector2i getSize() {
        return new Vector2i(width, height);
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vector4i vector4i = (Vector4i) o;
        return getX() == vector4i.getX() && getY() == vector4i.getY() && getWidth() == vector4i.getWidth() && getHeight() == vector4i.getHeight();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public String toString() {
        return x +  " : " + y + " : " + width + " : " + height;
    }


}

package com.karmorak.lib.engine.graphic.flat;

import java.net.URL;

class TextureData {

    public final int ID;
    public final int WIDTH, HEIGHT;
    public final URL PATH;
    public final int BPP;
    /** 0 = OK; 1 = Loaded with ErrorTexture; 2 = NotLoaded */
    public final int STATE;

    public TextureData(int id, int width, int height, URL path, int bpp) {
        this.PATH = path;
        this.ID = id;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.BPP = bpp;
        this.STATE = 0;
    }

    public TextureData(int id, int width, int height, URL path, int bpp, int state) {
        this.PATH = path;
        this.ID = id;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.BPP = bpp;
        this.STATE = state;
    }

    public int getID() {
        return ID;
    }
    public int getWIDTH() {
        return WIDTH;
    }
    public int getHEIGHT() {
        return HEIGHT;
    }
    public URL getPATH() {
        return PATH;
    }

    public boolean isOK() {
        return STATE == 0;
    }
}
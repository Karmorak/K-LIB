package com.karmorak.lib;

public enum ColorPreset implements Colorable {
    BLACK(0, 0, 0, 255),
    WHITE(255, 255, 255),
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255),
    CYAN(0, 255, 255),
    BROWN(139, 69, 19),
    PINK(255, 20, 147),
    ORANGE(238, 154, 0),
    YELLOW(255, 255, 0),
    LIGHT_BLUE(80, 185, 255),
    LIGHT_GRAY(190, 190, 190),
    GRAY(130, 130, 130),
    DARK_GRAY(70, 70, 70),
    NEUTRAL(1, 1, 1),
    ALPHA(0, 0, 0, 0);

    public final int R, G ,B, A;
    private int code;

    ColorPreset(int r, int g, int b) {
        this.R = r;
        this.G = g;
        this.B = b;
        this.A = 255;
        code = (A << 24) + (B << 16) + (G << 8) + R;
    }

    ColorPreset(int r, int g, int b, int a) {
        this.R = r;
        this.G = g;
        this.B = b;
        this.A = a;
        code = (A << 24) + (B << 16) + (G << 8) + R;
    }
    public Color toColor() {
        return new Color(R,G,B,A);
    }

    @Override
    public int Red() {
        return R;
    }

    @Override
    public int Green() {
        return G;
    }

    @Override
    public int Blue() {
        return B;
    }

    @Override
    public int Alpha() {
        return A;
    }

    @Override
    public int toInt() {
        return code; //RGBA
    }

    @Override
    public String toString() {
        return R +  " " + G + " " + B + " " + A;
    }
}
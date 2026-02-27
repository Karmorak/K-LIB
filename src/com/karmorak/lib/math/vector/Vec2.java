package com.karmorak.lib.math.vector;

import com.karmorak.lib.math.Vector2;

public interface Vec2 extends Vector {

    void set(int x, int y);
    void set(float x, float y);
    void set(Vector2 vec);

    void setX(float x);
    void setX(int x);

    void setY(float y);
    void setY(int y);

}

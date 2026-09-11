package com.karmorak.lib;

import com.karmorak.lib.math.Vector3;
import com.karmorak.lib.math.Vector4;

public interface Colorable {

    int toInt();
    Color toColor();
    String toString();

    int Red();
    int Green();
    int Blue();
    int Alpha();

    Vector3 Vec3f();

    Vector4 Vec4f();

}

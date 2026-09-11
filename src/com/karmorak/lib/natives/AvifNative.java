package com.karmorak.lib.natives;

import java.nio.ByteBuffer;

public class AvifNative {
    static {
        System.loadLibrary("AvifNative");
    }

    public native ByteBuffer decodeAvifToBuffer(String path, int[] outDimensions);
}

package com.karmorak.lib.engine.graphic.shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class InstanceBuffer {

    private static int instanceVboId;
    public static final int MAX_SPRITES = 10000;
    public static final int FLOATS_PER_INSTANCE = 7;
    public static final int STRIDE = FLOATS_PER_INSTANCE * Float.BYTES; // 28 Bytes

    public static void init(int vaoId) {
        glBindVertexArray(vaoId);

        instanceVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, instanceVboId);
        glBufferData(GL_ARRAY_BUFFER, (long) MAX_SPRITES * STRIDE,
                GL_STREAM_DRAW);

        // Location 2: pixelPosition (vec2) -> Offset 0
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, STRIDE, 0);
        glVertexAttribDivisor(2, 1);

        // Location 3: pixelSize (vec2) -> Offset 2 Floats (8 Bytes)
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3, 2, GL_FLOAT, false, STRIDE, 2 * Float.BYTES);
        glVertexAttribDivisor(3, 1);

        // Location 4: rotationZ (float) -> Offset 4 Floats (16 Bytes)
        glEnableVertexAttribArray(4);
        glVertexAttribPointer(4, 1, GL_FLOAT, false, STRIDE, 4 * Float.BYTES);
        glVertexAttribDivisor(4, 1);

        // Location 5: flipX, flipY (vec2) -> Offset 5 Floats (20 Bytes)
        glEnableVertexAttribArray(5);
        glVertexAttribPointer(5, 2, GL_FLOAT, false, STRIDE, 5 * Float.BYTES);
        glVertexAttribDivisor(5, 1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public static int getVboId() {
        return instanceVboId;
    }
}
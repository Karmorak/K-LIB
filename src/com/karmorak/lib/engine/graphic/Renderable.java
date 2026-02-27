package com.karmorak.lib.engine.graphic;

import com.karmorak.lib.engine.graphic.flat.TextureMesh;
import com.karmorak.lib.engine.graphic.flat.TextureShader;
import com.karmorak.lib.math.Vector4;

import java.util.List;

public interface Renderable {

    TextureMesh QUAD = new TextureMesh();
    TextureShader SHADER = new TextureShader();

    void renderManual(List<Vector4> positions, TextureShader shader);
    void create();
    void destroy();

    static TextureShader getShader() {
        return SHADER;
    }

    static void init () {
        if(!QUAD.isCreated()) {
            synchronized (QUAD) {
                if(!QUAD.isCreated()) {
                    QUAD.create();
                }
            }
            QUAD.create();
        }
        if(!SHADER.isCreated()) {
            synchronized (SHADER) {
                if(!SHADER.isCreated()) {
                    SHADER.create();
                }
            }
            SHADER.create();
        }
    }

    static int getVAO() {
        return QUAD.getVAO();
    }
}
package com.karmorak.lib.engine.graphic.flat;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.karmorak.lib.engine.graphic.Renderable;
import com.karmorak.lib.engine.graphic.shaders.TextureShader;
import com.karmorak.lib.math.Vector3;
import com.karmorak.lib.math.Vector4;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL46.*;

public class CharTexture extends TextureConstruct implements Renderable {

	public final TextureData DATA;

	CharTexture(TextureData data) {
		this.DATA = data;
		rotation = Vector3.EMPTY;
	}

	public CharTexture(DrawMap map, Vector4 region) {
		DATA = (getDrawData(map, (int) region.getX(),(int) region.getY(),(int) region.getWidth(),(int) region.getHeight()));
//		scale = 1f;
//		pos = new Vector2(0, 0);
//		size = new Vector2(DATA.getWIDTH(), DATA.getHEIGHT());
		rotation = Vector3.EMPTY;
//		flipY = true;
//		translBounds.set(translatePosition(), translateBounds());
		
//		overlayColor = null;
//		overlayColorintensity = 1f;
	}


	
	
	static TextureData getDrawData(DrawMap drawFrom, int texX, int texY, int texWidth, int texHeight) {

        ByteBuffer buffer = drawFrom.getPixels_asByteBuffer(texX, texY, texWidth, texHeight);
        buffer.rewind();
		int id = TextureConstruct.generateTextureID();
		bindTexture(id, texWidth, texHeight, buffer, GL_LINEAR, GL_LINEAR);
		return new TextureData(id, texWidth, texHeight, null, 4);
	}
	
	@Override
	public void render() {
//		SHADER.bind();
//		glBindVertexArray(QUAD.getVAO());
//		glEnableVertexAttribArray(0);
//		glEnableVertexAttribArray(1);
//		glDisable(GL_DEPTH_TEST);
//		glEnable(GL_BLEND);
//		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//		glActiveTexture(GL_TEXTURE0);
//
//		SHADER.loadTransformationMatrix(translatePosition(), translateBounds(), rotation, flipX, flipY);
//		SHADER.load2DColor(overlayColor, overlayColorintensity);
//
//		glBindTexture(GL_TEXTURE_2D, DATA.ID);
//		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
//
//		glEnable(GL_DEPTH_TEST);
//		glDisable(GL_BLEND);
//		glDisableVertexAttribArray(0);
//		glDisableVertexAttribArray(1);
//		glBindVertexArray(0);
//		SHADER.unbind();
	}

	@Override
	public void destroy() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glDeleteTextures(DATA.ID);
	}

	@Override
	public TextureData getData() {
		return DATA;
	}

	@Override
	public void renderManual(FloatBuffer buffer, int startOffset, int spriteCount, TextureShader shader) {
		if (spriteCount <= 0) return;

		glBindTexture(GL_TEXTURE_2D, getID());
		if (overlayColor != null)
			shader.load2DColor(overlayColor.toColor(), overlayColorIntensity);

		// Rendert direkt den gewünschten Bereich aus dem bereits befüllten VBO
		glDrawArraysInstancedBaseInstance(GL_TRIANGLE_STRIP, 0, 4, spriteCount, startOffset);
	}

	public void create() {

	}

}

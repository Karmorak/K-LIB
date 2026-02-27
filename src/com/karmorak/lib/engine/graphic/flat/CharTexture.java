package com.karmorak.lib.engine.graphic.flat;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.karmorak.lib.font.ownchar.OwnCharData;

import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;
import com.karmorak.lib.math.Vector4;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL46.*;

public class CharTexture extends TextureConstruct {

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
//		translBounds.set(translatePosition(), translateBounds());
		
//		overlayColor = null;
//		overlayColorintensity = 1f;
	}


	
	
	static TextureData getDrawData(DrawMap drawFrom, int texX, int texY, int texWidth, int texHeight) {

		ByteBuffer buffer =  drawFrom.getPixels_asByteBuffer(texX, texY, texWidth, texHeight);
        buffer.rewind();
		int id = TextureConstruct.generateTextureID();
		bindTexture(id, texWidth, texHeight, buffer, GL_NEAREST, GL_LINEAR);
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
	
	public static void render_chars_manual(CharTexture texture, ArrayList<OwnCharData> arrayList, TextureShader shader) {
		// 1. Einmal binden für alle Instanzen dieser Textur
		glBindTexture(GL_TEXTURE_2D, texture.DATA.getID());
		// 2. Farbe einmal setzen (sofern sie für alle Instanzen gleich ist)
//		shader.load2DColor(texture.overlayColor, texture.overlayColorintensity);

		for(OwnCharData oc : arrayList) {
			Vector2 nsize = oc.getTranslatedSize();
			Vector2 npos = oc.getTranslatedPosition();
//			System.out.println(oc.c + "" + npos.getX());
			shader.loadTransformationMatrix(npos, nsize, texture.rotation, texture.flipX, texture.flipY);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		}

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

	public void create() {

	}

//	public void renderManual(List<Vector4> positions, TextureShader shader) {
//		create();
//		// 1. Einmal binden für alle Instanzen dieser Textur
//		glBindTexture(GL_TEXTURE_2D, getID());
//		// 2. Farbe einmal setzen (sofern sie für alle Instanzen gleich ist)
//		shader.load2DColor(overlayColor, overlayColorintensity);
//		for(Vector4 bound : positions) {
//			Vector2 nsize = translateBounds(bound.getWidth(), bound.getHeight());
//			Vector2 npos = translatePosition(bound.getX(), bound.getY(), bound.getSize());
//
//			// Nur die Matrix muss sich pro Objekt ändern
//			shader.loadTransformationMatrix(npos, nsize, rotation, flipX, flipY);
//
//			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
//		}
//	}

}

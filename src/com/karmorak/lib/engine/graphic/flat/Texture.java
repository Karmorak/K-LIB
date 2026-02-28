package com.karmorak.lib.engine.graphic.flat;
  
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL46.*;

import com.karmorak.lib.ColorPreset;
import com.karmorak.lib.Colorable;
import com.karmorak.lib.engine.graphic.Renderable;
import com.karmorak.lib.engine.io.images.ImageLoader;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;
import com.karmorak.lib.math.Vector4;
import com.karmorak.lib.math.Vector4i;

public class Texture extends TextureConstruct implements Renderable {

	static int BPP = 4;
	public final TextureData DATA;
	
	public Texture(URL path) {
		DATA = (loadURL(path));
		init();
	}

	public Texture(Texture texture, Vector4 region) {
		this(texture, (int) region.getX(), (int) region.getY(), (int) region.getWidth(), (int) region.getHeight());
	}
	
	public Texture(Texture texture, int x, int y, int width, int height) {
		if(texture.DATA.getPATH() == null) throw new IllegalArgumentException("Texture Data is null");

		DrawMap drawFrom = new DrawMap(texture.DATA.getPATH());
		this(drawFrom, x, y, width, height);
		init();
	}
	
	public Texture(int width, int height, int[] pixels) {
		DATA = (loadURL(width, height, BPP, ImageLoader.IntArrayToBuffer(width, height, pixels)));
		
		init();
	}

	public Texture(int width, int height, Colorable color) {

		ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4)
				.order(ByteOrder.nativeOrder());
		int c = color.toInt();
		for (int i = 0; i < buffer.capacity(); i+=4) {
			buffer.putInt(i, c);
		}
		buffer.rewind();

		DATA = (loadURL(width, height, BPP, buffer));
		init();
	}

	public Texture(int width, int height, ByteBuffer b) {
		DATA = (loadURL(width, height, BPP, b));
		
		init();
	}

	public Texture(DrawMap map) {
		DATA = (DrawMapToData(map));
		
		init();
	}

	public Texture(DrawMap map, Vector4i cut) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(cut.getWidth() * cut.getHeight() * 4)
				.order(ByteOrder.nativeOrder());

		int[] src = map.getPixels();

		for (int y = 0; y < cut.getHeight(); y++) {
			int pos = (int) ((cut.getY() + y) * map.getWidth());
			for (int x = 0; x < cut.getWidth(); x++) {
				buffer.putInt(src[pos + x + cut.getX()]);
			}
		}
		buffer.rewind();

		int ID = glGenTextures();
		bindTexture(ID, cut.getWidth(), cut.getHeight(), buffer);
		DATA = new TextureData(ID, cut.getWidth(), cut.getHeight(), null, BPP);
		init();
	}

	@Deprecated
	public Texture(DrawMap map, Vector4 cut) {
		this(map, new Vector4i(cut));
	}
	
	public Texture(DrawMap map, int x, int y, int width, int height) {
		this(map, new Vector4i(x, y, width, height));
	}

	@Deprecated
	public Texture(TextureRegion region) {
		DATA = RegionToTexture(region);
		init();
	}

	private void init(){
		scale = 1f;
		pos = new Vector2(0, 0);
		size = new Vector2(DATA.getWIDTH(), DATA.getHEIGHT());
		rotation = new Vector3(0, 0, 0);
		translBounds.set(translatePosition(), translateBounds());

		overlayColor = ColorPreset.WHITE.toColor();
		overlayColorIntensity = 1f;
	}
	



	

	

		
	public static void render(List<Texture> textures, TextureShader shader) {		
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		glActiveTexture(GL_TEXTURE0);
		
		for(Texture t : textures) {
					
			shader.loadTransformationMatrix(t.translBounds.getPosition(), t.translBounds.getSize(), true);
			shader.load2DColor(t.overlayColor.toColor(), t.overlayColorIntensity);
			glBindTexture(GL_TEXTURE_2D, t.DATA.getID());
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		}		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
	}
	
	
	public void render(float x, float y, float width, float height) {
		SHADER.bind();
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glActiveTexture(GL_TEXTURE0);		
		
		
		Vector2 bounds = translateBounds(width, height);
		Vector2 pos = translatePosition(x, y, bounds); // <---
		
		SHADER.loadTransformationMatrix(pos, bounds, rotation, flipX, flipY);
		SHADER.load2DColor(overlayColor.toColor(), overlayColorIntensity);
		glBindTexture(GL_TEXTURE_2D, DATA.getID());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		SHADER.unbind();
		
	}
	
	public static void render(Map<Texture, HashMap<Vector2, ArrayList<Vector2>>> newTextures, TextureShader textureShader) {
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		glActiveTexture(GL_TEXTURE0);
		
		for(Texture texture : newTextures.keySet()) {	
			for(Vector2 size : newTextures.get(texture).keySet()) {
				Vector2 nsize = texture.translateBounds(size.getWidth(), size.getHeight());
				for (Vector2 pos : newTextures.get(texture).get(size)) {
					Vector2 npos = texture.translatePosition(pos.getX(), pos.getY(), size); // <---
					textureShader.loadTransformationMatrix(npos, nsize, texture.rotation, texture.flipX, texture.flipY);
					textureShader.load2DColor(texture.overlayColor.toColor(), texture.overlayColorIntensity);
					glBindTexture(GL_TEXTURE_2D, texture.DATA.getID());
					glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);				
				}		
			}			
		}
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);	
		
	}
	
	public static void render(Texture texture, ArrayList<Vector4> bounds, TextureShader textureShader) {
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		glActiveTexture(GL_TEXTURE0);
				
		for(Vector4 bound : bounds) {
			Vector2 nsize = texture.translateBounds(bound.getWidth(), bound.getHeight());
			Vector2 npos = texture.translatePosition(bound.getX(), bound.getY(), bound.getSize()); // <---
			textureShader.loadTransformationMatrix(npos, nsize, texture.rotation, texture.flipX, texture.flipY);
			textureShader.load2DColor(texture.overlayColor.toColor(), texture.overlayColorIntensity);
			glBindTexture(GL_TEXTURE_2D, texture.DATA.getID());
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);		
		}	
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);		
	}

	public static int getVAO() {
		return QUAD.getVAO();
	}
	



	private static URL URL(String path) {
		return Texture.class.getResource(path);
	}

	@Override
	public void render()  {
//		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
//		glClearColor(1f, 1f, 1f, 1f);
		
		SHADER.bind();
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);		
		
		SHADER.loadTransformationMatrix(translatePosition(), translateBounds(), rotation, flipX, flipY);
		SHADER.load2DColor(overlayColor.toColor(), overlayColorIntensity);
		
		glBindTexture(GL_TEXTURE_2D, DATA.ID);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		SHADER.unbind();
	}
	
	@Override
	public void destroy() {
		if(getID() > 0) {
			glBindTexture(GL_TEXTURE_2D, 0);
			glDeleteTextures(DATA.ID);
		}
	}

	@Override
	public TextureData getData() {
		return DATA;
	}


	@Override
	public void renderManual(List<Vector4> positions, TextureShader shader) {

		// 1. Einmal binden für alle Instanzen dieser Textur
		glBindTexture(GL_TEXTURE_2D, DATA.getID());

		// 2. Farbe einmal setzen (sofern sie für alle Instanzen gleich ist)
		shader.load2DColor(overlayColor.toColor(), overlayColorIntensity);

		for(Vector4 bound : positions) {
			Vector2 nsize = translateBounds(bound.getWidth(), bound.getHeight());
			Vector2 npos = translatePosition(bound.getX(), bound.getY(), bound.getSize());

			// Nur die Matrix muss sich pro Objekt ändern
			shader.loadTransformationMatrix(npos, nsize, rotation, flipX, flipY);

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		}
	}

	@Override
	public synchronized void create() {

	}


}

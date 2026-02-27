package com.karmorak.lib.engine.graphic.flat;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL46.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.karmorak.lib.engine.graphic.Renderable;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector4;

public class TextureRegion extends TextureConstruct implements Renderable {

	static int BPP = 4;
	
	private TextureMesh quad;
	private Vector4 texBounds;
	
	public final TextureData DATA;
	
	public TextureRegion(URL path) {
		DATA = loadURL(path, Type.TEXTURE);
		
		scale = 1f;
		pos = new Vector2(0, 0);
		size = new Vector2(DATA.getWIDTH(), DATA.getHEIGHT());		
		translBounds.set(translatePosition(), translateBounds());
	}
	
	public TextureRegion(URL path, Vector4 texBounds) {
		DATA = loadURL(path, Type.TEXTURE);
			
		this.setTexBounds(texBounds);
		
		float ax, ay, awidth, aheight;
		float mat_width = DATA.getWIDTH();
		float mat_height = DATA.getHEIGHT();
		
		
		ax = texBounds.getX() / mat_width;
		ay = texBounds.getY() / mat_height;
		
		
		awidth = ((texBounds.getX()+texBounds.getWidth()) / mat_width);
		aheight = ((texBounds.getY()+texBounds.getHeight()) / mat_height);	 
		
		Vector2 a = new Vector2(ax, ay);
		Vector2 b = new Vector2(ax, aheight);
		Vector2 c = new Vector2(awidth, ay);
		Vector2 d = new Vector2(awidth, aheight);
		
		quad = new TextureMesh();
		quad.setTexCoords(new Vector2[]{a, b, c, d});		
		
		scale = 1f;
		pos = new Vector2(0, 0);
		size = new Vector2(DATA.getWIDTH(), DATA.getHEIGHT());		
		translBounds.set(translatePosition(), translateBounds());
	}
	
	public TextureRegion(Texture t, Vector4 texBounds) {
		DATA = (new TextureData(t.DATA.getID(), t.getSourceWidth(), t.getSourceHeight(), t.getData().getPATH(), BPP));
			
		this.setTexBounds(texBounds);
		
		float ax, ay, awidth, aheight;
		float mat_width = DATA.getWIDTH();
		float mat_height = DATA.getHEIGHT();
		
		
		ax = texBounds.getX() / mat_width;
		ay = texBounds.getY() / mat_height;
		
		
		awidth = ((texBounds.getX()+texBounds.getWidth()) / mat_width);
		aheight = ((texBounds.getY()+texBounds.getHeight()) / mat_height);	 
		
		Vector2 a = new Vector2(ax, ay);
		Vector2 b = new Vector2(ax, aheight);
		Vector2 c = new Vector2(awidth, ay);
		Vector2 d = new Vector2(awidth, aheight);
		
		quad = new TextureMesh();
		quad.setTexCoords(new Vector2[]{a, b, c, d});		
		
		scale = 1f;
		pos = new Vector2(0, 0);
		size = new Vector2(DATA.getWIDTH(), DATA.getHEIGHT());		
		translBounds.set(translatePosition(), translateBounds());
	}
	
	
		
	@Override
	public void create() {
//		super.create();
//		if(quad != null && !quad.isCreated()) quad.create();
	}	

	
	@Override
	public void render() {
		SHADER.bind();
		if(quad == null) glBindVertexArray(QUAD.getVAO());
		else glBindVertexArray(quad.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		//Sprite2D.translatePixelBoundstoGL(1920, 1080, new Vector2(3840, 2160)
		glActiveTexture(GL_TEXTURE0);
		SHADER.loadTransformationMatrix(translBounds.getPosition(), translBounds.getSize(), true);
		glBindTexture(GL_TEXTURE_2D, DATA.getID());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		SHADER.unbind();
	}
	
	public static void render(List<TextureRegion> textures, TextureShader shader) {
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		glActiveTexture(GL_TEXTURE0);
		
		for(TextureRegion t : textures) {
			if(t.quad == null) glBindVertexArray(QUAD.getVAO());
			else glBindVertexArray(t.quad.getVAO());
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(1);
			
			shader.loadTransformationMatrix(t.translBounds.getPosition(), t.translBounds.getBounds(), true);
			glBindTexture(GL_TEXTURE_2D, t.DATA.getID());
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		}		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
	}
	
	
	public static void render(Map<TextureRegion, HashMap<Vector2, ArrayList<Vector2>>> fontRegions, TextureShader shader) {
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		glActiveTexture(GL_TEXTURE0);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		for(TextureRegion region : fontRegions.keySet()) {			
			for(Vector2 size : fontRegions.get(region).keySet()) {
				region.setSize(size.getWidth(), size.getHeight());
				for (Vector2 pos : fontRegions.get(region).get(size)) {
					region.setPosition(pos.getX(), pos.getY());
					glBindVertexArray(region.quad.getVAO());

					
					shader.loadTransformationMatrix(region.translBounds.getPosition(), region.translBounds.getBounds(), true);
					glBindTexture(GL_TEXTURE_2D, region.DATA.getID());
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
	
	//V2
	public static void render_manual(TextureRegion texture, ArrayList<Vector4> bounds, TextureShader textureShader) {				

	}

	public Vector4 getTexBounds() {
		return texBounds;
	}

	@Deprecated
	public void setTexBounds(Vector4 texBounds) {
		this.texBounds = texBounds;
	}

	@Override
	public TextureData getData() {
		return DATA;
	}
	
	@Override
	public void destroy() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glDeleteTextures(DATA.ID);
	}


	@Override
	public void renderManual(List<Vector4> positions, TextureShader shader) {
		// 1. Einmal binden für alle Instanzen dieser Textur
		glBindTexture(GL_TEXTURE_2D, DATA.getID());

		// 2. Farbe einmal setzen (sofern sie für alle Instanzen gleich ist)
		shader.load2DColor(overlayColor, overlayColorintensity);

		for(Vector4 bound : positions) {
			Vector2 nsize = translateBounds(bound.getWidth(), bound.getHeight());
			Vector2 npos = translatePosition(bound.getX(), bound.getY(), bound.getSize());

			// Nur die Matrix muss sich pro Objekt ändern
			shader.loadTransformationMatrix(npos, nsize, rotation, flipX, flipY);

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		}
	}
}

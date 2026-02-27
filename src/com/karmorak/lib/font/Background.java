package com.karmorak.lib.font;

import java.net.URI;
import java.net.URL;

import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.utils.GameFunktions;

public class Background {
	
	private static final float BACKGROUND_MOVESPEED = (float) 1.51 * 10;
	private static Texture BackGround;
	private static Texture BackGround2;
	private static int Background_X;
	private static int Background_Y;
	private static float width;
	private static float height;
	private static boolean firstStart;
	private static float scale;
	
	public static void initBackGround(boolean scaleY, URL file) {
		if(scaleY) scale = GameFunktions.getScaleMultiY();
		else scale = GameFunktions.getScaleMultiX();
		
		
		
		BackGround = new Texture(file);
		BackGround2 = new Texture(file);
		
		width = (BackGround.getWidth() * scale);
		height = (BackGround.getHeight() * scale);
		Background_X = (int) -(width);
		Background_Y = 0;
		firstStart = true;	
	}
	
	public static void initBackGround(boolean scaleY, Texture texture) {
		if(scaleY) scale = GameFunktions.getScaleMultiY();
		else scale = GameFunktions.getScaleMultiX();
		BackGround = texture;
		BackGround2 = texture;
		
		width = (BackGround.getWidth() * scale);
		height = (BackGround.getHeight() * scale);
		Background_X = (int) -(width);
		Background_Y = 0;
		firstStart = true;	
	}
	
	private static int c;
	
	public static void updateBackGround() {		
		if(firstStart) {	
			firstStart = false;
		} else {
			if(Background_X <= 0) {
				Background_X+=(BACKGROUND_MOVESPEED*scale);
			} else {
				Background_X = (int) -(width);
			}
		}	
		if(c==Background_X) {
			Background_X = (int) -(width);
		}
		c= Background_X;
		
		BackGround.setSize(width, height);
		BackGround.setPosition(Background_X, Background_Y);
		
		BackGround2.setSize(width, height);
		BackGround2.setPosition(width+Background_X, Background_Y);
	}
	
	public static void drawBackGround(MasterRenderer renderer) {
		renderer.processTexture(BackGround);
		renderer.processTexture(BackGround2);
	}

}

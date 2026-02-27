package com.karmorak.lib.engine.io.images;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.BufferUtils;

import com.karmorak.lib.Color;
import com.karmorak.lib.engine.graphic.flat.TextureConstruct;
import com.karmorak.lib.utils.PNGDecoder;

import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL46.*;

@Deprecated
public class PNG {
	
	
	private final int bpp = 4;
	private int id;
	private int width, height;
	private int[] pixels;
	private ByteBuffer buf;
	private final URL path;
	
	public PNG(int width, int height, int[] pixels) throws IOException {
		this.path = null;
		
		this.width = (int) width;
		this.height = (int) height;	
		
		buf = BufferUtils.createByteBuffer(bpp * (int) width *(int) height);


        for (int pixel : pixels) {
            buf.putInt(pixel);
        }
		
		this.pixels = pixels;
		
		buf.rewind();
	}

	public PNG(URL path, int x, int y, int width, int height) throws IOException {
		this.path = path;
		this.width = width;
		this.height = height;

		// 1. Buffer erstellen
		buf = BufferUtils.createByteBuffer(bpp * width * height);
		buf.order(ByteOrder.nativeOrder()); // WICHTIG für putInt/asIntBuffer

		// 2. Die Farbe definieren (Weiß/Opaque)
		int answer = 0xFFFFFFFF; // Entspricht deinem Shift-Code, nur kürzer

		// 3. Das Pixel-Array füllen (CPU Seite)
		pixels = new int[width * height];
		java.util.Arrays.fill(pixels, answer);

		// 4. Den gesamten Buffer in einem Rutsch füllen (GPU Seite vorbereiten)
		// Das ist VIEL schneller und stabiler als Einzelzugriffe
		buf.asIntBuffer().put(pixels);

		// 5. Zurück auf Anfang für OpenGL
		buf.rewind();
	}
	
	
	public PNG(URL path) throws IOException {
		
		this.path = path;
		InputStream input = path.openStream();		
		PNGDecoder dec = new PNGDecoder(input);

			
		width = dec.getWidth();
		height = dec.getHeight();	
		
		//create a new byte buffer which will hold our pixel data
		buf = BufferUtils.createByteBuffer(bpp * width * height);
	
		//decode the image into the byte buffer, in RGBA format
		dec.decode(buf, width * bpp, PNGDecoder.Format.RGBA);

		pixels = new int[width * height];
		buf.rewind();
		buf.order(ByteOrder.nativeOrder()).asIntBuffer().get(pixels);

		//flip the buffer into "read mode" for OpenGL
		buf.rewind();
		
		input.close();
		
//		pixels = toIntArray(getByteArrayFromByteBuffer(bb));

		
	}


	//26;24 161;148;137
	public int getID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
//	public int getPixelRGBInt(int x, int y) {
//
//		int packedValue = pixels[y * getWidth() +x];
//		
//		int R = packedValue & 255; 
//		int G = (packedValue >> 8) & 255; 
//		int B = (packedValue >> 16) & 255; 
//				
//		return R<<16 + G<<8 + B;	
//	}
	
	public Color getPixel(int x, int y) {
		
		return new Color(pixels[y * getWidth() +x]);		
	}
	
}

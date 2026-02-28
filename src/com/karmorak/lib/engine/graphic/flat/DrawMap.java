package com.karmorak.lib.engine.graphic.flat;

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
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL46.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.karmorak.lib.ColorPreset;
import com.karmorak.lib.Colorable;
import com.karmorak.lib.engine.graphic.GLTaskQueue;
import com.karmorak.lib.engine.graphic.Renderable;
import com.karmorak.lib.math.*;
import org.lwjgl.BufferUtils;
import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.io.images.ImageLoader;
import com.karmorak.lib.utils.PNGDecoder;

@SuppressWarnings("ConstantValue")
public class DrawMap extends TextureConstruct implements Renderable {

	static int DEFAULT_BPP = 4;
	
	private int[] pixels;
	private boolean buffer_changed = true, buffer_created = false;
	ByteBuffer buffer_cache;

	private int ID = -1;
	private URL src_path;
	private int src_width, src_height, src_bpp;

//	private TextureData DATA;
	
	void init()  {
		init(GL_NEAREST, GL_LINEAR);
	}
	
	void init(int filter) {
		init(filter, filter);
	}
	void init(int min_filter, int max_falter) {
		scale = 1f;
		pos = new Vector2(100, 100);
		size = new Vector2(src_width, src_height);
		translBounds.set(translatePosition(), translateBounds());
		rotation = Vector3.EMPTY;
		this.overlayColor = ColorPreset.WHITE;
		this.overlayColorIntensity = 1f;
	}

	void initData(TextureData data) {
		src_width = data.getWIDTH();
		src_height = data.getHEIGHT();
		src_bpp = data.BPP;
		src_path = data.getPATH();
	}

	void initData(int width, int height, URL Path, int bpp) {
		src_width = width;
		src_height = height;
		src_bpp = bpp;
		src_path = Path;
	}
	
	public DrawMap(URL path) {
		initPixels(path);		
		init();	
	}

	public DrawMap set(URL path) {
		initPixels(path);
		buffer_changed = true;
		init();
		return this;
	}

	public DrawMap set(int width, int height, ByteBuffer src) {
		initPixels(width, height, 4, src);
		init();
		buffer_created = true;
		return this;
	}
	
	public DrawMap(URL path, int texture_filter) {		
		initPixels(path);		
		init(texture_filter);	
	}
	
	public DrawMap(int width, int height, ByteBuffer buffer) {
		initPixels(width, height, 4, buffer);		
		init();
	} 
	
	public DrawMap(int width, int height, ByteBuffer buffer, int texture_filter) {
		initPixels(width, height, 4, buffer);
		init(texture_filter);
	}

	@Deprecated
	public DrawMap(DrawMap map) {
		initPixels(map.getSourceWidth(), map.getSourceHeight(), 4, ImageLoader.IntArrayToBuffer(map.src_width, map.src_height, map.pixels));
		init();
	}

	public DrawMap(DrawMap map, Vector4i cut) {


		buffer_cache = BufferUtils.createByteBuffer(cut.getWidth() * cut.getHeight() * DEFAULT_BPP);
		pixels = new int[ cut.getWidth() * cut.getHeight()];

		for (int x2 = 0; x2 < cut.getWidth(); x2++) {
			for (int y2 = 0; y2 < cut.getHeight(); y2++) {
				pixels[y2 * cut.getWidth() + x2] = map.getPixelInt(cut.getX() + x2, cut.getY() + y2);
			}
		}

		buffer_cache.asIntBuffer().put(pixels);
		buffer_cache.rewind();
		buffer_changed = true;

		initData(cut.getWidth(), cut.getHeight(), null, DEFAULT_BPP);
		init();
	}

	
	DrawMap(TextureData Data,  int[] pixels, ByteBuffer buffer) {
		this.pixels = pixels;
		this.buffer_cache = buffer;
		buffer_cache.rewind();
		initData(Data);
		init();
	}

	DrawMap(TextureData Data,  int[] pixels) {
		this.pixels = pixels;
		buffer_changed = true;
		initData(Data);
		init();
	}
	
	public DrawMap(float width, float height) {
		initPixels((int)width, (int)height, ColorPreset.ALPHA.toInt());
		init();
	}

	public DrawMap(Vector2i size) {
		initPixels(size.getWidth(), size.getHeight(), ColorPreset.ALPHA.toInt());
		init();
	}

	public DrawMap(int width, int height, Colorable c) {
		initPixels(width, height, ColorPreset.ALPHA.toInt());
		init();
	}

	public DrawMap(float width, float height, Colorable c) {
		initPixels((int)width, (int)height, c.toInt());
		init();
	}
	
	public DrawMap(Vector2 size, Colorable c) {
		initPixels((int)size.getWidth(), (int)size.getHeight(), c.toInt());
		init();
	}

	public DrawMap(Vector2i size, Colorable c) {
		initPixels(size.getWidth(), size.getHeight(), c.toInt());
		init();
	}
	
	public DrawMap(Texture t) {		
		initPixels(t.DATA.getPATH());
		
		init();		
	}

	void initPixels(URL path) throws NullPointerException {
		int width = -1, height = -1;


		try (InputStream stream = path.openStream()) {
			if (!path.toString().toLowerCase().contains(".png")) {
				throw new IOException("Nur PNG wird unterstützt: " + path);
			}
			PNGDecoder dec = new PNGDecoder(stream);
			width = dec.getWidth();
			height = dec.getHeight();

			buffer_cache = BufferUtils.createByteBuffer(width * height * 4)
					.order(ByteOrder.nativeOrder()); // Wichtig für putInt/getInt
			dec.decode(buffer_cache, width * 4, PNGDecoder.Format.RGBA);

			// Rewind statt flip/limit-Mix
			buffer_cache.rewind();

			// Daten für die CPU-Seite sichern
			this.pixels = ImageLoader.ByteBufferToIntArray2(buffer_cache);
			this.buffer_changed = false;

			initData(width, height, path, DEFAULT_BPP);
		} catch (Exception e) {
			System.err.println("Fehler beim Laden von: " + path);
			e.printStackTrace();

			// Fallback: Pinke Textur
			width = 2; // Reicht für Fehleranzeige, spart RAM
			height = 2;
			this.pixels = new int[width * height];
			Arrays.fill(this.pixels, ColorPreset.PINK.toInt());
			buffer_cache = BufferUtils.createByteBuffer(width * height * 4);
			this.buffer_cache.asIntBuffer().put(pixels);
			this.buffer_cache.rewind();
			this.buffer_changed = false;

			initData(width, height, path, DEFAULT_BPP);
		}
	}
	
	void initPixels(int width, int height, int bpp, ByteBuffer buffer) throws NullPointerException {
		
		if(!buffer.isReadOnly()) {
			buffer.rewind();
		}
				
		pixels =  ImageLoader.ByteBufferToIntArray2(buffer);
		buffer_cache = buffer;
		buffer_changed = false;
		buffer_created = false;

		initData(width, height, null, bpp);
	}

	void initPixels(int width, int height, int color) throws NullPointerException {

		buffer_cache = BufferUtils.createByteBuffer((int) (width * height * DEFAULT_BPP));
		pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			int i = y * width;
			for (int x = 0; x < width; x++) {
				pixels[i + x] = color;
			}
		}

		buffer_cache.rewind();

		buffer_changed = true;
		initData(width, height, null, DEFAULT_BPP);
	}

    public static int copy(final ByteBuffer from, final ByteBuffer to) {
        final int len = from.limit();
        return copy(from, 0, to, 0, len);
    }/*from w w w.  ja v  a2 s .c  o  m*/

    public static int copy(final ByteBuffer from, final int offset1,
                           final ByteBuffer to, final int offset2, final int len) {
        System.arraycopy(from.array(), offset1, to.array(), offset2, len);
        to.limit(offset2 + len);
        return len;
    }

	public void drawOutline(Colorable outline_color, int thickness) {
		drawOutline(outline_color, getSize(), thickness);
	}

	public void drawOutline(Color outline_color, int thickness) {
		drawOutline(outline_color, getSize(), thickness);
	}

	public void drawOutline(Color outline_color, Vector2 thickness) {
		drawOutline(outline_color, getSize(), thickness);
	}

	public void drawOutline(Colorable outline_color, Vector2 size, int thickness) {
		drawOutline(outline_color, size, new Vector2(thickness, thickness));
	}

	public void drawOutline(Color outline_color, Vector2 size, int thickness) {
		drawOutline(outline_color, size, new Vector2(thickness, thickness));
	}

	public void drawOutline(Colorable outline_color, Vector2 size, Vector2 thickness) {

		int width = (int) size.getWidth();
		int height = (int) size.getHeight();
		if (width > getWidth()) width = (int) getWidth();
		if (height > getHeight()) height = (int) getHeight();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < thickness.getY(); y++) {
				pixels[y * width + x] = outline_color.toInt();

				int i = (height - 1 - y) * width + x;
				pixels[i] = outline_color.toInt();
			}
		}

		for (int x = 0; x < thickness.getX(); x++) {
			for (int y = 0; y < height; y++) {
				pixels[y * width + x] = outline_color.toInt();

				pixels[y * width + (width-1 -x)] = outline_color.toInt();
			}
		}

		buffer_changed = true;
	}


	
	
	
	public static DrawMap buildCircle(Color c, int radius) {
		
		int size = radius * 2;
		
		ByteBuffer buffer = BufferUtils.createByteBuffer((int) (size * size * DEFAULT_BPP));
		int[] pixels = new int[size * size];
		
		for (int x = 0; x < radius+1; x++) {
			for (int y = 0; y < radius+1; y++) {
				
				int dist_x = Math.abs(radius - x);
				int dist_y = Math.abs(radius - y);			
				
				int length = (int) Math.sqrt(dist_x *dist_x + dist_y * dist_y);				
				if(length < radius) {
					int pos = y * size + x;
					pixels[pos] = c.toInt();
					buffer.put(4 * (y * size + x),(byte) c.getRed());
					buffer.put(4 * (y * size + x) + 1,(byte) c.getGreen());
					buffer.put(4 * (y * size + x) + 2,(byte) c.getBlue());
					buffer.put(4 * (y * size + x) + 3,(byte) c.getAlpha());
					
					pos = (y * size + (size - x));
					pixels[pos] = c.toInt();
					buffer.put(4 * pos,(byte) c.getRed());
					buffer.put(4 * pos + 1,(byte) c.getGreen());
					buffer.put(4 * pos + 2,(byte) c.getBlue());
					buffer.put(4 * pos + 3,(byte) c.getAlpha());
					
					pos = (size - y) * size + x;
					pixels[pos] = c.toInt();
					buffer.put(4 * pos,(byte) c.getRed());
					buffer.put(4 * pos + 1,(byte) c.getGreen());
					buffer.put(4 * pos + 2,(byte) c.getBlue());
					buffer.put(4 * pos + 3,(byte) c.getAlpha());
					
					pos = (size - y) * size + (size - x);
					pixels[pos] = c.toInt();
					buffer.put(4 * pos,(byte) c.getRed());
					buffer.put(4 * pos + 1,(byte) c.getGreen());
					buffer.put(4 * pos + 2,(byte) c.getBlue());
					buffer.put(4 * pos + 3,(byte) c.getAlpha());					
				}				
			}
		}
		
		TextureData data = new TextureData(-1, size, size, null, 4);
		return new DrawMap(data, pixels, buffer);			
	}
	
	//--------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------
	public DrawMap cut(int x, int y, int width, int height) {

		int[] pixels_c = new int[width * height];
        Arrays.fill(pixels_c, ColorPreset.ALPHA.toInt());
		
		for (int x2 = 0; x2 < width; x2++) {
			for (int y2 = 0; y2 < height; y2++) {
				pixels_c[y2 * width + x2] = getPixel(x+ x2, y + y2).toInt();
			}			
		}

		initData(width, height, this.src_path, DEFAULT_BPP);
		this.setSize(width, height);
		this.pixels = pixels_c;
		buffer_changed = true;
		
		return this;	
	}

	//mode 0= replace; 1= blend
	public DrawMap colorize(Color c, int mode) { //TODO
		for (int i = 0; i < pixels.length; i++) {
			Color b = new Color(pixels[i]);
			if(b.getAlpha() > 0) {
				pixels[i] = c.setAlpha(b.getAlpha()).toInt();
			}
		}
        return this;
    }

	public DrawMap fill(Colorable c) {
		return this.clearDrawMap(c);
	}

	public DrawMap fill(Color c, int x, int y, int width, int height) {

		if(x + width > getWidth()) width = (int) (getWidth()-x);
		if(y + height > getHeight()) height = (int) (getHeight()-y);

		int color = c.toInt();

		for(int sX = x; sX < width; sX++) {
			for (int sY = y; sY < height; sY++) {
				pixels[(int) (sY * getHeight() + sX)] = color;
			}
		}

		buffer_changed = true;
		return this;
	}

	public DrawMap rotate90(boolean clockwise) {
		int oldW = (int) getWidth();
		int oldH = (int) getHeight();

		// Das Ziel-Array hat die gleiche Größe, aber die
		// Dimensionen für die Index-Berechnung sind vertauscht.
		int[] dest = new int[oldW * oldH];

		// Die NEUE Breite ist die ALTE Höhe

        for (int y = 0; y < oldH; y++) {
			for (int x = 0; x < oldW; x++) {
				int oldIndex = y * oldW + x;
				int newX, newY;

				if (clockwise) {
					// Rechtsdrehung: Spalte wird zur Zeile, Zeile wird zur Spalte (invertiert)
					newX = oldH - 1 - y;
					newY = x;
				} else {
					// Linksdrehung: Zeile wird zur Spalte, Spalte wird zur Zeile (invertiert)
					newX = y;
					newY = oldW - 1 - x;
				}

				// WICHTIG: Der neue Index muss mit der NEUEN Breite berechnet werden
				int newIndex = newY * oldH + newX;
				dest[newIndex] = pixels[oldIndex];
			}
		}

		// Jetzt die Dimensionen der Klasse aktualisieren
		setWidth(oldH);
		setHeight(oldW);

		initData(oldH, oldW, this.getPATH(), DEFAULT_BPP);
		this.pixels = dest;
		this.buffer_changed = true;

		return this;
	}


	public DrawMap flip(boolean flipX, boolean flipY) {
		if (!flipX && !flipY) return this; // Nichts zu tun

		int w = getSourceWidth();
		int h = getSourceHeight();
		int[] newPixels = new int[w * h];
		int[] srcPixels = this.pixels; // Direktzugriff aufs Array ist schneller

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				// Ziel-Koordinaten berechnen
				int srcX = flipX ? (w - 1 - x) : x;
				int srcY = flipY ? (h - 1 - y) : y;

				// Pixel übertragen (Direkt aus dem Array ohne Color-Objekt)
				newPixels[y * w + x] = srcPixels[srcY * w + srcX];
			}
		}

		this.pixels = newPixels;
		this.buffer_changed = true;
		return this;
	}

	//checked
	public DrawMap drawFromDrawMap(int x, int y, DrawMap drawFrom, boolean drawAlpha) {
		int srcW = (int) drawFrom.getSourceWidth();
		int srcH = (int) drawFrom.getSourceHeight();
		int dstW = (int) this.getSourceWidth();
		int dstH = (int) this.getSourceHeight();
		int[] srcPixels = drawFrom.pixels;

		for (int y2 = 0; y2 < srcH; y2++) {
			// 1. Zeile im Ziel berechnen (Y wird geflippt, wie in deinem Original)
			int targetY = dstH - y - y2 - 1;

			// CLIPPING Y: Wenn die Zeile außerhalb des Zielbildes liegt, überspringen
			if (targetY < 0 || targetY >= dstH) continue;

			// 2. Offsets berechnen
			// WICHTIG: targetY * dstW (Ziel-Breite!)
			int targetOffsetBase = targetY * dstW;
			int sourceOffsetBase = y2 * srcW;

			if (drawAlpha) {
				// CLIPPING X: Startpunkt und Länge berechnen
				int startX = Math.max(0, x);
				int endX = Math.min(dstW, x + srcW);
				int copyLength = endX - startX;

				if (copyLength > 0) {
					// Wir müssen den Quell-Offset anpassen, falls x negativ ist (links herausragt)
					int srcXOffset = (x < 0) ? -x : 0;

					System.arraycopy(srcPixels, sourceOffsetBase + srcXOffset,
							pixels, targetOffsetBase + startX,
							copyLength);
				}
			} else {
				// Einzel-Pixel-Check (für Transparenz-Unterstützung ohne Blending)
				for (int x2 = 0; x2 < srcW; x2++) {
					int targetX = x + x2;

					// CLIPPING X: Nur zeichnen, wenn x im Zielbereich liegt
					if (targetX >= 0 && targetX < dstW) {
						int color = srcPixels[sourceOffsetBase + x2];

						// Nur zeichnen, wenn Pixel nicht volltransparent ist
						if (((color >> 24) & 0xFF) > 0) {
							pixels[targetOffsetBase + targetX] = color;
						}
					}
				}
			}
		}

		buffer_changed = true;
		return this;
	}

//	@Deprecated // sollte von der anderen ersetzt werden hat allerdings noch probleme
//	public DrawMap drawFromDrawMap_OLD(int x, int y, DrawMap drawFrom) { //sollte abfragen ob es out of bounds oder so ist
//
//		int source_width = (int) drawFrom.getSourceWidth();
//		int source_height = (int) drawFrom.getSourceHeight();
//
//		for (int y2 = 0; y2 < source_height; y2++) {
//			int y_1 = getSourceHeight() -  y - y2 -1;
//			int dst_y = y_1 * getSourceWidth();
//
//			int sry_y = y2 * drawFrom.getSourceWidth();
//			for (int x2 = 0; x2 < source_width; x2++) {
//				int c = drawFrom.pixels[sry_y + x2];
//
//				if(x+x2 >= 0 && x+x2 < getSourceWidth() && dst_y >= 0 && dst_y < getSourceHeight()) {
//					pixels[dst_y + x + x2] = c;
//					buffer_changed = true;
//				}
//			}
//		}
//		return this;
//	}


//	@Deprecated
//	public DrawMap drawFromDrawMap_OLD(int x, int y, DrawMap drawFrom) {
//
//		int source_width = (int) drawFrom.getSourceWidth();
//		int source_height = (int) drawFrom.getSourceHeight();
//
//		for (int x2 = 0; x2 < source_width; x2++) {
//			for (int y2 = 0; y2 < source_height; y2++) {
//				int c = drawFrom.getPixelInt(x2, y2);
//				drawPixel(x + x2, getSourceHeight() -  y - y2 -1, c);
//			}
//		}
//
//		buffer_changed = true;
//		return this;
//	}



	
	
	
	public DrawMap drawLine(Color c, int x0, int y0, int x1, int y1) {

		int dx = Math.abs(x1 - x0);
	    int sx = x0 < x1 ? 1 : -1;
	    int dy = -Math.abs(y1 - y0);
	    int sy = y0 < y1 ? 1 : -1;
	    int err = dx + dy;

	    while (true) {
	    	drawPixel(x0, y0, c);
	    	if (x0 == x1 && y0 == y1) break;

	    	int e2 = 2*err;
	    	if (e2 >= dy) {
	    		err += dy;
	    		x0 += sx;
	    	}
	    	if (e2 <= dx) {
	    		err += dx;
	    		y0 += sy;
	    	}
	    }
		
		return this;
	}
	
		
	/** drawLine2 has no middlepoint*/
	public DrawMap drawLine2(Color c, int x1, int y1, int x2, int y2) {
		
		
//		for(int x = 0; x < getWidth(); x++) {
//			for(int y = 0; y < getHEIGHT(); y++) {
//				drawPixel(x, y, Color.ALPHA);
//				drawPixel(x, y, new Color(150, 150, 150, 150));
//			}
//		}
				

		float dx = Math.abs(x2 - x1);
		float dy = Math.abs(y2 - y1);
				
		//draw the first pixel
//		drawPixel(x1, y1, c);				
		
		if(dx > dy) {
			float f = dx *0.5f;
			int ay = y1;			
			
			for (int ax = x1; ax < x1 + dx; ax++) {		
				int ax2 = (int) (x2 - x1 < 0 ? getSourceWidth() -ax : ax);
				f -=  dy;
				
				if(f < 0) {
					if(y2 - y1 > 0) ay += 1;	
					else ay -= 1;
					f += dx;
				}
								
				drawPixel(ax2,ay, c);
			}			
			
		} else {			
		
			float f = dy *0.5f;
			int ax = x1;
						
			for (int ay = y1; ay < y1 + dy; ay++) {				
				int ay2 = y2 - y1 < 0 ? getSourceHeight() -ay : ay;
				f -= dx;
				
				if(f < 0) {
					if(x2 - x1 < 0) ax -= 1;	
					else ax += 1;
					f += dy;
				}								
				drawPixel(ax, ay2, c);
			}	
		}
		return this;
	}
	
	
		
	public static DrawMap buildLine(Color c, int width, int height, int x1, int y1, int x2, int y2) {

		int[] pixels = new int[width * height * 2];
		
//		for (int x = 0; x < width*2; x++) {
//			for (int y = 0; y < height*2; y++) {
//				buffer.put(4 * (y * width*2 + x),(byte) Color.LIGHT_GRAY.getRed());
//				buffer.put(4 * (y * width*2 + x) + 1,(byte) Color.LIGHT_GRAY.getGreen());
//				buffer.put(4 * (y * width*2 + x) + 2,(byte) Color.LIGHT_GRAY.getBlue());
//				buffer.put(4 * (y * width*2 + x) + 3,(byte) 159);
//			}
//		}
		
		float dx = x2 - x1;
		float dy = y2 - y1;
		
		float dxn = Math.abs(dx);
		float dyn = Math.abs(dy);
		
		int pos;
		
		//draw the first pixel
		
		pixels[pos = (y1 * width*2 + x1)] = c.toInt();
		
		if(dxn > dyn) {
			float f = dxn *0.5f;
			int ay = height;
			
			
			for (int ax = width; ax < width + dxn; ax++) {
				int ax2 = dx < 0 ? width + (width - ax) : ax;
				
				f -=  dyn;
				
				if(f < 0) {
					if(dy > 0) ay -= 1;	
					else ay += 1;
					f += dxn;
				}
				pixels[(ay * (width*2) + ax2)] = c.toInt();
			}	
		} else {			
		
			float f = dyn *0.5f;
			
			int ax = width;

			for (int ay = height; ay < height + dyn; ay++) {
				int ay2 = dy > 0 ? height + (height - ay) : ay;
				
				
				f -= dxn;
				
				if(f < 0) {
					if(dx < 0) ax -= 1;	
					else ax += 1;
					f += dyn;
				}	
				
				pixels[(ay2 * (width*2) + ax)] = c.toInt();
			}	
		}

		TextureData data = new TextureData(glGenTextures(), width, height, null, 4);	
		return new DrawMap(data, pixels);
	}
	
	
	public DrawMap clearDrawMap() {	
		return clearDrawMap(ColorPreset.ALPHA);
	}
	
	public DrawMap clearDrawMap(Colorable clearColor) {
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = clearColor.toInt();
		}

		buffer_changed = true;
		return this;
	}

	
	
    /**
     * Calculates crossProduct of two 2D vectors / points.
     * @param v1 first point used as vector
     * @param v2 second point used as vector
     * @return crossProduct of vectors
     */
    protected int crossProduct(Vector2 v1, Vector2 v2) {
        return (int) (v1.getX() * v2.getY() - v1.getY() * v2.getX());
    }

    
    private final int method = 0;
	
	public void fillTriangle(Vector2 v1, Vector2 v2, Vector2 v3, Color c) {
		//http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html#sunbresenhamarticle
		
//		clearDrawMap(Color.BLACK);
		
		if(method == 0) {
		
	        /* get the bounding box of the triangle */
	        int maxX = (int) Math.max(v1.getX(), Math.max(v2.getX(), v3.getX()));
	        int minX = (int) Math.min(v1.getX(), Math.min(v2.getX(), v3.getX()));
	        int maxY = (int) Math.max(v1.getY(), Math.max(v2.getY(), v3.getY()));
	        int minY = (int) Math.min(v1.getY(), Math.min(v2.getY(), v3.getY()));
	        
	        /* spanning vectors of edge (v1,v2) and (v1,v3) */
	        Vector2 vs1 = new Vector2(v2.getX() - v1.getX(), v2.getY() - v1.getY());
	        Vector2 vs2 = new Vector2(v3.getX() - v1.getX(), v3.getY() - v1.getY());
	        
	        
	        /* iterate over each pixel of bounding box and check if it's inside
	         * the traingle using the barycentirc approach.
	         */
	        for (int x = minX; x <= maxX; x++)
	        {
	            for (int y = minY; y <= maxY; y++)
	            {
	            	Vector2 q = new Vector2(x - v1.getX(), y - v1.getY());
	                        
	                float s = (float)crossProduct(q, vs2) / crossProduct(vs1, vs2);
	                float t = (float)crossProduct(vs1, q) / crossProduct(vs1, vs2);
	                
	                if ( (s >= 0) && (t >= 0) && (s + t <= 1))
	                {
	                   	drawPixel(x, y, c);
	                }
	            }
	        }
		} else if(method ==1 || method ==2) {
		
			Vector2 small;
			Vector2 big;
			Vector2 split;	
	
			  /* at first sort the three vertices by y-coordinate ascending so v1 is the topmost vertice */
			if(v1.getY() > v2.getY() && v1.getY() > v3.getY()) {			
				big = v1;
				if(v2.getY() > v3.getY()) {
					split = v2;
					small = v3;
				} else {
					split = v3;
					small = v2;
				}			
			} else if(v2.getY() > v1.getY() && v2.getY() > v3.getY()) {			
				big = v2;
				if(v1.getY() > v3.getY()) {
					split = v1;
					small = v3;
				} else {
					split = v3;
					small = v1;
				}			
			} else {			
				big = v3;
				if(v1.getY() > v2.getY()) {
					split = v1;
					small = v2;
				} else {
					split = v2;
					small = v1;
				}			
			}

			if (big.getY() == small.getY()) return; // Punkt oder Linie, kein Dreieck

			// 2. Den vierten Punkt (a4) auf der Kante small-big finden
			float t = (split.getY() - small.getY()) / (big.getY() - small.getY());
			float a4X = small.getX() + t * (big.getX() - small.getX());
			Vector2 a4 = new Vector2(a4X, split.getY());

			// 3. Zeichnen
			if (split.getY() == small.getY()) {
				drawTopTriangle(small, split, big, c); // (Eigentlich flat-bottom, Namen prüfen)
			} else if (split.getY() == big.getY()) {
				drawBottomTriangle(small, split, big, c);
			} else {
				drawBottomTriangle(small, split, a4, c);
				drawTopTriangle(split, a4, big, c);
			}
		} else if (method == 3) {
			int x1 = (int) v1.getX();
			int y1 = (int) v1.getY();
			int x2 = (int) v2.getX();
			int y2 = (int) v2.getY();
			int x3 = (int) v3.getX();
			int y3 = (int) v3.getY();
			drawLine(c, x1, y1, x2, y2);
			drawLine(c, x2, y2, x3, y3);
			drawLine(c, x3, y3, x1, y1);
		}	
	}
	
	
//	d.fillTriangle(new Vector2(52, 20), new Vector2(50, 1), new Vector2(36, 45), Color.RED);
	//small = 50;1 |  split = 52;20 | a4= 38;20
	
	private void drawBottomTriangle(Vector2 v1, Vector2 v2, Vector2 v3, Color c) {

		//	  v1
		//   /  \
		//	/    \
		// v2-----v3
		//
		
		
		if(method == 1) {
			  float invslope1 = (v2.getX() - v1.getX()) / (v2.getY() - v1.getY());
			  float invslope2 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());
	
			  float curx1 = v1.getX();
			  float curx2 = v1.getX();
	
			  for (int scanlineY = (int) v1.getY(); scanlineY <= v2.getY() ; scanlineY++) {
				drawLine(c, (int)curx1,scanlineY, (int)curx2, scanlineY);
			    curx1 += invslope1;
			    curx2 += invslope2;
			  }
		} else {
			float dx1 =  Math.abs(v1.getX() -  v2.getX()); //50 - 52 = 					-2
			float dx2 =  Math.abs(v3.getX() -  v1.getX()); //38 - 50 = 					-12
			float dy = Math.abs(v1.getY() -  v2.getY());  //1  - 20 = -19 ->  19
			
			
			int curStartX = (int) v1.getX(); // 50
			int curEndX = (int) v1.getX();   // 50
			
			//scanlineY = 1; 1 < 20;
			for (int scanlineY = 0; scanlineY < dy; scanlineY++) {
				
				drawLine(c, curStartX,(int)v1.getY() +  scanlineY, curEndX,(int)v1.getY() + scanlineY);
							
				float progress = (scanlineY/ dy);
				
				curStartX = (int) (v1.getX() + dx1*progress);
				curEndX = (int) (v1.getX() - dx2*progress);
			}
		}
	}
	
	private void drawTopTriangle(Vector2 v1, Vector2 v2, Vector2 v3, Color c) {
	
		// v1-----v2
		//  \    /
		//   \  /
		//	  v3
		
		if(method == 1) {
			  float invslope1 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());
			  float invslope2 = (v3.getX() - v2.getX()) / (v3.getY() -  v2.getY());
	
			  float curx1 = v3.getX();
			  float curx2 = v3.getX();
			  
			  for (int scanlineY = (int) v3.getY(); scanlineY > v1.getY() ; scanlineY--) {
				drawLine(c, (int)curx1, scanlineY, (int)curx2, scanlineY);
			    curx1 -= invslope1;
			    curx2 -= invslope2;
			  }
		} else {
			if(v1.getX() > v3.getX()) {
			
				float dx1 =  Math.abs(v3.getX() -  v2.getX()); //50 - 52 = 					-2
				float dx2 =  Math.abs(v1.getX() -  v3.getX()); //38 - 50 = 					-12
				float dy = Math.abs(v3.getY() -  v2.getY());  //1  - 20 = -19 ->  19
				
				
				int curStartX = (int) v2.getX(); // 50
				int curEndX = (int) v2.getX();   // 50
				
				//scanlineY = 1; 1 < 20;
				for (int scanlineY = -1; scanlineY < dy; scanlineY++) {
					
					drawLine(c, curStartX,(int)v2.getY() +  scanlineY, curEndX,(int)v2.getY() + scanlineY);
								
					float progress = (scanlineY/ dy);
					
					curStartX = (int) (v2.getX() - dx1*progress);
					curEndX = (int) (v3.getX() + dx2*(1f-progress));
				}
			
			} else {
			
				float dx1 =  Math.abs(v3.getX() -  v2.getX()); //50 - 52 = 					-2
				float dx2 =  Math.abs(v1.getX() -  v3.getX()); //38 - 50 = 					-12
				float dy = Math.abs(v3.getY() -  v2.getY());  //1  - 20 = -19 ->  19
				
				
				int curStartX = (int) v1.getX(); // 50
				int curEndX = (int) v1.getX();   // 50
				
				//scanlineY = 1; 1 < 20;
				for (int scanlineY = -1; scanlineY < dy; scanlineY++) {
					
					drawLine(c, curStartX,(int)v1.getY() +  scanlineY, curEndX,(int)v1.getY() + scanlineY);
								
					float progress = (scanlineY/ dy);
					
					curStartX = (int) (v1.getX() - (dx1*(1f-progress)));
					curEndX = (int) (v1.getX() + dx2*(progress));
				}
			}
		}
	}
	


	@Override
	public void create() {
		// Falls die ID noch -1 ist, müssen wir sie generieren.
		// Aber NUR im Main-Thread (über die Queue oder direkt im Renderer)
		if (getID() == -1) {
			ID = TextureConstruct.generateTextureID();
		}

		synchronized (this) {
			if (!buffer_changed && buffer_cache != null && buffer_created) return;

			// Sicherheits-Check: Haben wir überhaupt Daten?
			if (pixels == null || pixels.length == 0) {
				System.out.println("Creation failed no data!");
				return;
			}
			if (!buffer_created && !buffer_changed && buffer_cache != null) {
				buffer_cache.rewind();
				// 4. Upload zur GPU
				TextureConstruct.bindTexture(ID, src_width, src_height, buffer_cache);
			} else {
				// 1. Buffer initialisieren falls nötig
				int capacity = src_width * src_height * 4;
				if (buffer_cache == null || buffer_cache.capacity() != capacity) {
					buffer_cache = BufferUtils.createByteBuffer(capacity);
				}

				// 2. Thread-Safety: Schnappschuss der Pixel-Daten
				// Wir kopieren das Array lokal, damit ein Hintergrund-Thread
				// während des .put() Aufrufs nichts korrumpieren kann.
				int[] pixelsCopy = pixels.clone();

				// 3. In den Native-Buffer schreiben
				buffer_cache.clear();
				buffer_cache.asIntBuffer().put(pixelsCopy);
				buffer_cache.rewind();

				// 4. Upload zur GPU
				TextureConstruct.bindTexture(ID, src_width, src_height, buffer_cache);
			}
			buffer_changed = false;
			buffer_created = true;
		}
	}

	public ByteBuffer getBuffer() {
		if (buffer_cache == null || buffer_changed) create();
		return buffer_cache;
	}

	public void destroy() {
//		GL11.glDeleteTextures(ID);
//		glBindTexture(GL_TEXTURE_2D, 0);
//		glDeleteTextures(getID());

		if (getID() > 0) {
			ID = -1;
			GLTaskQueue.add(() -> {
				glDeleteTextures(ID);
			});
		}

		if(buffer_cache != null)
			buffer_cache.clear();
		buffer_cache = null;

		src_path = null;

		pos = null;
		size = null;
		rotation = null;
		
		pixels = null;
		
	}

	public int[] getPixels() {
		return pixels;
	}

	public boolean isLoaded() {
		return pixels != null && pixels.length != 0;
	}

	@Override
	public URL getPATH() {
		return src_path;
	}

	@Override
	public TextureData getData() {
//		super.getData();
//		System.out.println("<Warning>! Retrieving DrawMap.getData() is not up to date anymore");
		return new TextureData(ID, src_width, src_height, src_path, src_bpp);
	}

	public int getID() {
		return ID;
	}
	
	@Deprecated
	public Color getPixel(int x, int y) {
		try {
			int packedValue = pixels[y * getSourceWidth() + x];
			return new Color(packedValue);		
		} catch (IndexOutOfBoundsException e) {
			if(KLIB.DEBUG_LEVEL >= 2)
				System.err.println(getPATH() + " Pixel x:" + x + " y:" + y + " is out ouf bounds. Max size:[" + getSourceWidth() + ":" + getSourceHeight() + "]");
			return Color.ALPHA();
		}
	}
	public int getPixelInt(int x, int y) {
		try {
            return pixels[y * getSourceWidth() + x];
		} catch (IndexOutOfBoundsException e) {
			if(KLIB.DEBUG_LEVEL >= 2)
				System.err.println(getPATH() + " Pixel x:" + x + " y:" + y + " is out ouf bounds. Max size:[" + getSourceWidth() + ":" + getSourceHeight() + "]");
			return ColorPreset.ALPHA.toInt();
		}
	}
	
	
	public int[] getPixels(int t_x, int t_y, int width, int height) {		
		int[] out = new int[width * height];
				
		try {
	        for (int y = 0; y < height; y++) {
	        	int yc = (t_y + y) * getSourceWidth();
	        	int yc2 = y * width;
	            for (int x = 0; x < width; x++) {
	            	out[yc2 + x] = pixels[yc + x + t_x];
		        }
		    }
			return out;
		} catch (IndexOutOfBoundsException e) {
//			if(KLIB.DEBUG_LEVEL >= 2)
//				System.err.println(getPATH() + " Pixel x:" + t_x + " y:" + y + " is out ouf bounds. Max size:[" + getSourceWidth() + ":" + getSourceHeight() + "]");
			return new int[] {ColorPreset.PINK.toInt()};
		}		
	}


	
	public ByteBuffer getPixels_asByteBuffer(int t_x, int t_y, int width, int height) {
		// 1. Validierung: Verhindert den Crash bevor er passiert
		if (t_x < 0 || t_y < 0 || t_x + width > getSourceWidth() || t_y + height > getSourceHeight()) {
			System.err.println("Fehler: Textur-Ausschnitt liegt außerhalb des Bildes!");
			// Statt null geben wir lieber einen leeren Puffer oder werfen eine klare Exception
			return BufferUtils.createByteBuffer(0);
		}

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4)
				.order(ByteOrder.nativeOrder());

		// Wir nutzen einen IntBuffer-View für schnelleres Schreiben
		java.nio.IntBuffer intBuffer = buffer.asIntBuffer();

		for (int y = 0; y < height; y++) {
			int yc = (t_y + y) * getSourceWidth();
			// Ein ganzer Zeilenabschnitt wird kopiert (schneller als einzelne putInt)
			intBuffer.put(pixels, yc + t_x, width);
		}

		// WICHTIG: rewind() sorgt dafür, dass die Position wieder auf 0 steht
		buffer.rewind();
		return buffer;
	}
	
	

//	public static Color getPixel(int x, int y, int width, ByteBuffer buffer) {	
//		
//		int packedValue = buffer.getInt(BPP * (y * width + x));
//			
//		int R = packedValue & 255; 
//		int G = (packedValue >> 8) & 255; 
//		int B = (packedValue >> 16) & 255; 
//		int A = (packedValue >> 24) & 255;			
//		
//		return new Color(R, G, B, A);			
//	}


	public void drawPixel(int x, int y, int color) {
		try {
			if (x >= 0 && x < getSourceWidth() && y >= 0 && y < getSourceHeight()) {
				pixels[y * getSourceWidth() + x] = color;
				buffer_changed = true;
			}
		} catch (IndexOutOfBoundsException e) {
			if (KLIB.DEBUG_LEVEL >= 2) {
				System.err.println(getPATH() + " Pixel x:" + x + " y:" + y + " is out ouf bounds. Max size:[" + getSourceWidth() + ":" + getSourceHeight() + "]");
			}
		}
	}

	public void drawPixel(int x, int y, Color c) {		
		try {
			if(x >= 0 && x < getSourceWidth() && y >= 0 && y < getSourceHeight()) {			
				pixels[y * getSourceWidth() + x] = c.toInt();
				buffer_changed = true;
			}
		} catch (IndexOutOfBoundsException e) {
			if(KLIB.DEBUG_LEVEL >= 2) {
				System.err.println(getPATH() + " Pixel x:" + x + " y:" + y + " is out ouf bounds. Max size:[" + getSourceWidth() + ":" + getSourceHeight() + "]");
			}
		}		
	}
	
	public static DrawMap setTexBounds(URL from, Vector4 texBounds) {	
		
		int regionX = (int) texBounds.getX(); 
		int regionY = (int) texBounds.getY(); 
		int regionWidth = (int) texBounds.getWidth();
		int regionHeight = (int) texBounds.getHeight();
		
		int[] pixels = new int[regionWidth * regionHeight];
		ByteBuffer drawTo = BufferUtils.createByteBuffer(DEFAULT_BPP * regionWidth * regionHeight);
		
		DrawMap drawFrom = new DrawMap(from);
	
		for(int x = 0; x < regionWidth; x++) {
			for(int y = 0; y < regionHeight; y++) {
				Color c = drawFrom.getPixel(x + regionX, y + regionY);			
				drawTo.put(4 * (y * regionWidth + x),(byte) c.getRed());
				drawTo.put(4 * (y * regionWidth + x) + 1,(byte) c.getGreen());
				drawTo.put(4 * (y * regionWidth + x) + 2,(byte) c.getBlue());
				drawTo.put(4 * (y * regionWidth + x) + 3,(byte) c.getAlpha());		
			}				
		}	
		
		TextureData data = new TextureData(glGenTextures(), regionWidth, regionHeight, null, 4);	
		return new DrawMap(data, pixels, drawTo);	
	}



	public static void bindTexture(int id, int width, int height, ByteBuffer buffer, int min_filter, int max_filter) {
		glBindTexture(GL_TEXTURE_2D, id);

		// Safety: Tell GL to read every single byte without skipping
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min_filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, max_filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		if (buffer != null) {
			buffer.rewind(); // Just in case
			// UPLOAD FIRST
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

			// GENERATE SECOND (only if filter allows)
			if (min_filter != GL_NEAREST && min_filter != GL_LINEAR) {
				glGenerateMipmap(GL_TEXTURE_2D);
			}
		}


		glBindTexture(GL_TEXTURE_2D, 0);
	}


	@Override
	public void render() {
		if(!QUAD.isCreated()) QUAD.create();	
		if(!SHADER.isCreated())  SHADER.create();	
		
		
		create();

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
		
		glBindTexture(GL_TEXTURE_2D, getID());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		SHADER.unbind();
		
	}
	
	public ByteBuffer saveRenderImage(int target_width, int target_height) {
		return saveRenderImage(0, 0, target_width, target_height, 0, 0, target_width, target_height);
	}
	
	
	public ByteBuffer saveRenderImage(int x, int y, int target_width, int target_height) {
		return saveRenderImage(x, y, target_width, target_height, x, y, target_width, target_height);		
	}	
	
	public ByteBuffer saveRenderImage(Vector4 image, int target_x, int target_y, int target_width, int target_height) {
		return saveRenderImage((int) image.getX(),(int) image.getY(),(int) image.getWidth(),(int) image.getHeight(), target_x, target_y, target_width, target_height);		
	}	
	
	public ByteBuffer saveRenderImage(int x, int y, int img_width, int img_height,int target_x, int target_y, int target_width, int target_height) {
		create();

		int textureId = getID();

		if (textureId <= 0) {
			System.err.println("Fehler: saveRenderImage abgebrochen, da Textur-ID ungültig: " + textureId);
			return null;
		}

		setSize(img_width, img_height);
		setPosition(x, y);

		SHADER.bind();
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, getID());

		SHADER.loadTransformationMatrix(translatePosition(), translateBounds(), rotation, flipX, flipY);
		if (overlayColor != null)
			SHADER.load2DColor(overlayColor.toColor(), overlayColorIntensity);
		

		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

		ByteBuffer buffer = BufferUtils.createByteBuffer(target_width * target_height * DEFAULT_BPP);
		glPixelStorei(GL_PACK_ALIGNMENT, 1); // set alignment of data in memory (this time pack alignment; a good thing to do before glReadPixels)
		glReadPixels(target_x, target_y, target_width, target_height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		buffer.rewind();
//		ImageLoader.DEBUGBUFFER(20, 20, target_width, buffer);

		glEnable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		SHADER.unbind();


		return buffer;
	}

	public DrawMap setRenderImage(Vector4 image, int target_x, int target_y, int target_width, int target_height) {
		return setRenderImage((int) image.getX(), (int) image.getY(), (int) image.getWidth(), (int) image.getHeight(), target_x, target_y, target_width, target_height);
	}

	public DrawMap setRenderImage(int x, int y, int img_width, int img_height, int target_x, int target_y, int target_width, int target_height) {
		create();

		int textureId = getID();

		if (textureId <= 0) {
			System.err.println("Fehler: saveRenderImage abgebrochen, da Textur-ID ungültig: " + textureId);
			return null;
		}

		setSize(img_width, img_height);
		setPosition(x, y);

		SHADER.bind();
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, getID());

		SHADER.loadTransformationMatrix(translatePosition(), translateBounds(), rotation, flipX, flipY);
		if (overlayColor != null)
			SHADER.load2DColor(overlayColor.toColor(), overlayColorIntensity);


		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

		ByteBuffer buffer = BufferUtils.createByteBuffer(target_width * target_height * DEFAULT_BPP);
		glPixelStorei(GL_PACK_ALIGNMENT, 1); // set alignment of data in memory (this time pack alignment; a good thing to do before glReadPixels)
		glReadPixels(target_x, target_y, target_width, target_height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		buffer.rewind();
//		ImageLoader.DEBUGBUFFER(20, 20, target_width, buffer);

		glEnable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		SHADER.unbind();

		set(target_width, target_height, buffer);

		return this;
	}
	

	public static void render(HashMap<DrawMap, HashMap<Vector2, ArrayList<Vector2>>> newTextures, TextureShader shader) {
		
		if(!QUAD.isCreated()) QUAD.create();	
		if(!SHADER.isCreated())  SHADER.create();	
		
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		glActiveTexture(GL_TEXTURE0);
		
		for(DrawMap texture : newTextures.keySet()) {	
			for(Vector2 size : newTextures.get(texture).keySet()) {
				Vector2 nsize = texture.translateBounds(size.getWidth(), size.getHeight());
				for (Vector2 pos : newTextures.get(texture).get(size)) {

					texture.create();
					
					Vector2 npos = texture.translatePosition(pos.getX(), pos.getY(), size); // <---
					shader.loadTransformationMatrix(npos, nsize, texture.rotation, texture.flipX, texture.flipY);
					shader.load2DColor(texture.overlayColor.toColor(), texture.overlayColorIntensity);
					glBindTexture(GL_TEXTURE_2D, texture.getID());
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
	
	
	public static void render(DrawMap map, ArrayList<Vector4> bounds, TextureShader shader) {
		
		if(!QUAD.isCreated()) QUAD.create();	
		if(!SHADER.isCreated())  SHADER.create();	
		
		glBindVertexArray(QUAD.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		glActiveTexture(GL_TEXTURE0);
		
				
		for (Vector4 bounding : bounds) {

			Vector4 transl = map.translate(bounding);
					
			map.create();

			shader.loadTransformationMatrix(transl.getPosition(), transl.getSize(), map.rotation, map.flipX, map.flipY);
			shader.load2DColor(map.overlayColor.toColor(), map.overlayColorIntensity);
			glBindTexture(GL_TEXTURE_2D, map.getID());
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);	
			
		}		
		
		
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);			
	}
	

	@Override
	public void renderManual(List<Vector4> positions, TextureShader shader) {
		if(!QUAD.isCreated()) QUAD.create();
		if(!SHADER.isCreated())  SHADER.create();

		create();

		// 1. Einmal binden für alle Instanzen dieser Textur
		glBindTexture(GL_TEXTURE_2D, getID());

		// 2. Farbe einmal setzen (sofern sie für alle Instanzen gleich ist)
		if(overlayColor != null)
			shader.load2DColor(overlayColor.toColor(), overlayColorIntensity);

		for(Vector4 bound : positions) {
			Vector2 nsize = translateBounds(bound.getWidth(), bound.getHeight());
			Vector2 npos = translatePosition(bound.getX(), bound.getY(), bound.getSize());

			// Nur die Matrix muss sich pro Objekt ändern
			shader.loadTransformationMatrix(npos, nsize, rotation, flipX, flipY);

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		}
	}
}
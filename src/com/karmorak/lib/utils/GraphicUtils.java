package com.karmorak.lib.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL46;
import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.utils.file.FileUtils;

public class GraphicUtils {
	
	
	
	public static Texture colorize2Texture(Texture grayscale, Color color) {	
		return colorize2Texture(grayscale.getPATH(), color, 1f, 0, false, false);
	}
	
	public static Texture colorize2Texture(URL grayscale, Color color) {	
		return colorize2Texture(grayscale, color, 1f, 0, false, false);
	}
	
	public static Texture colorize2Texture(URL grayscale, Color color, boolean negate) {		
		return colorize2Texture(grayscale, color, 1f, 0, negate, false);
	}
	
	public static Texture colorize2Texture(URL grayscale, Color color, float intensity, float alpha_tolerancy, boolean negate, boolean capcolours) {		
		DrawMap pix2 = new DrawMap(grayscale);
		
		
//		pixels[i] = data.getBuffer().getInt(4 * i);		
			
		for(int x = 0; x < pix2.getWidth(); x++) {
			for(int y = 0; y < pix2.getHeight(); y++) {
				Color c = pix2.getPixel(x, y);
				if(c.getAlpha() > alpha_tolerancy) {		
					
					float r;
					float g;
					float b;
		
					if(negate) {
						r = 255 - (c.getRed() * (color.getRed()*intensity))/255;
						g = 255 - (c.getGreen() * (color.getGreen()*intensity))/255;
						b = 255 - (c.getBlue() * (color.getBlue()*intensity))/255;
					} else {
						r = (c.getRed() * (color.getRed()*intensity))/255;
						g = (c.getGreen() * (color.getGreen()*intensity))/255;
						b = (c.getBlue() * (color.getBlue()*intensity))/255;
					}				
		
					
					if(capcolours) {
						if(r >255) r = 255;
						if(g > 255) g = 255;
						if(b > 255) b = 255;
					}					
			
					pix2.drawPixel(x, y, new Color(r, g, b, 255));
				} 
			}
		}	
		pix2.create();
		Texture out = new Texture(pix2);
		pix2.destroy();
		return out;
	}
	
	
	public static int[] colorize(int[] pixels, Color color, float intensity, float alpha_tolerancy, boolean negate, boolean capcolours) {		
		int[] out = new int[pixels.length];
		
		for (int p = 0; p < pixels.length; p++) {
			Color c = new Color(pixels[p]);
					
			if(c.getAlpha() > alpha_tolerancy) {		
				
				float r;
				float g;
				float b;
	
				if(negate) {
					r = 255 - (c.getRed() * (color.getRed()*intensity))/255;
					g = 255 - (c.getGreen() * (color.getGreen()*intensity))/255;
					b = 255 - (c.getBlue() * (color.getBlue()*intensity))/255;
				} else {
					r = (c.getRed() * (color.getRed()*intensity))/255;
					g = (c.getGreen() * (color.getGreen()*intensity))/255;
					b = (c.getBlue() * (color.getBlue()*intensity))/255;
				}				
	
				
				if(capcolours) {
					if(r >255) r = 255;
					if(g > 255) g = 255;
					if(b > 255) b = 255;
				}					
		
				out[p] = new Color(r, g, b, c.getAlpha()).toInt();
			} else {
				out[p] = c.toInt();
			}			
		}
		return out;
	}
	
	
	public static Texture colorize2Font(URL grayscale, Color color) {			
		DrawMap pix2 = null;		
		
		pix2 = new DrawMap(grayscale);
		for(int x = 0; x < pix2.getWidth(); x++) {
			for(int y = 0; y < pix2.getHeight(); y++) {
				Color c = pix2.getPixel(x, y);
				if(c.getAlpha() > 0) {		
					
					float r = c.getRed() * color.getRed()/255;
					float g = c.getGreen() * color.getGreen()/255;
					float b = c.getBlue() * color.getBlue()/255;
						
					pix2.drawPixel(x, y, new Color(r, g, b, 255));
				} 
			}
		}	
		pix2.create();
		Texture out = new Texture(pix2);
		pix2.destroy();
		return out;
	}
	
	

	
	
	
	public static DrawMap colorize2DrawMap(URL grayscale, Color color, boolean negate) {			
		DrawMap pix2 = null;	
		
		pix2 = new DrawMap(grayscale);
		for(int x = 0; x < pix2.getWidth(); x++) {
			for(int y = 0; y < pix2.getHeight(); y++) {
				Color c = pix2.getPixel(x, y);
				if(c.getAlpha() > 0) {		
					
					float r;
					float g;
					float b;
		
					if(negate) {
						r = 255 - (c.getRed() * (color.getRed()))/255;
						g = 255 - (c.getGreen() * (color.getGreen()))/255;
						b = 255 - (c.getBlue() * (color.getBlue()))/255;
					} else {
						r = (c.getRed() * (color.getRed()))/255;
						g = (c.getGreen() * (color.getGreen()))/255;
						b = (c.getBlue() * (color.getBlue()))/255;
					}				
		
						
					pix2.drawPixel(x, y, new Color(r, g, b, 255));
				} 
			}
		}	
		return pix2;
	}
	
	public static DrawMap colorize2DrawMap(DrawMap map, Color color, boolean negate) {			
		DrawMap pix2 = null;	
		
		pix2 = map;
		for(int x = 0; x < pix2.getWidth(); x++) {
			for(int y = 0; y < pix2.getHeight(); y++) {
				Color c = pix2.getPixel(x, y);
				if(c.getAlpha() > 0) {		
					
					float r;
					float g;
					float b;
		
					if(negate) {
						r = 255 - (c.getRed() * (color.getRed()))/255;
						g = 255 - (c.getGreen() * (color.getGreen()))/255;
						b = 255 - (c.getBlue() * (color.getBlue()))/255;
					} else {
						r = (c.getRed() * (color.getRed()))/255;
						g = (c.getGreen() * (color.getGreen()))/255;
						b = (c.getBlue() * (color.getBlue()))/255;
					}				
		
						
					pix2.drawPixel(x, y, new Color(r, g, b, 255));
				} 
			}
		}	
		return pix2;
	}
	

	/**
	 * @param map
	 * @param dir_path
	 * @param file_name
	 * @throws IOException
	 * 
	 * Pfad muss mit \\ am ende sein
	 * ja pfad wird erstellt falls nicht da
	 * 
	 * 
	 */@Deprecated
	public static void saveDrawMap(DrawMap map, String dir_path, String file_name) throws IOException {
		saveDrawMap(map, dir_path + file_name);			
	}
	
	/**
	 * @param map
	 * @param dir_path
	 * @throws IOException
	 * 
	 * Pfad muss mit \\ am ende sein
	 * ja pfad wird erstellt falls nicht da
	 * 
	 * 
	 */
	public static void saveDrawMap(DrawMap map, String dir_path) throws IOException {



		File f = new File(dir_path);
		
		FileUtils.checkFile(f);

		if(map.getBuffer() == null) throw new IOException("saving Draw Map failed because map.getBuffer is null");
		ByteBuffer buf2 = map.getBuffer();
		
		buf2.position(0);
		
		byte[] arr = new byte[buf2.remaining()];
		buf2.get(arr);		
		
					
		BufferedImage img = new BufferedImage((int)map.getSourceWidth(),(int) map.getSourceHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		img.getRaster().setDataElements(0, 0, (int)map.getSourceWidth(), (int) map.getSourceHeight(), arr);
					    
		ImageIO.write(img, "png", f);	
		
		img.flush();		
		buf2.rewind();
	
	}
	
	static int offset(int x, int y, int w, int channels) {
	    return (y * w + x) * channels;
	}
	
	public static Texture makeGrayScale(URL path, float alpha) {
		
		
		
		DrawMap pix = new DrawMap(path);
		DrawMap result = new DrawMap(pix.getSize(), Color.ALPHA());
		
		for(int x = 0; x < pix.getWidth(); x++) {
			for(int y = 0; y < pix.getHeight(); y++) {
				Color color = pix.getPixel(x, y);		
				if(color.getAlpha() >= alpha) {					
					float red = 0.299f * color.getRed();
					float green = 0.587f * color.getGreen();
					float blue =  0.114f*color.getBlue();
					float c2 = red + green + blue;
					result.drawPixel(x, y, new Color(c2, c2, c2, 255f));
				}
			}
		}
		Texture s2 = new Texture(result);
		pix.destroy();
		return s2;		
	}
	
	
	public static DrawMap takeScreenshot() {
		int width = (int)(KLIB.graphic.Width());
		int height = (int)(KLIB.graphic.Height());		
		return takeScreenshot(0 , 0, width, height);
	}
	
	public static DrawMap takeScreenshot(int x,int y, int width, int height) {		
		ByteBuffer screenData = BufferUtils.createByteBuffer(width*height*4);			
		GL46.glReadPixels(x,y,(int)KLIB.graphic.Width(), (int)KLIB.graphic.Height(), GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, screenData);	
		
		return new DrawMap((int)KLIB.graphic.Width(),(int)KLIB.graphic.Height(), screenData);
	}
	
	public static double GausianMask(int x, int y, int rad_x, int rad_y, double strength) {		
		double sigma  = strength;		
		double G = (1d / (2d * Math.PI * sigma * sigma)) * Math.pow(Math.E, -((2d/rad_x)*x*x + (2d/rad_y)*y*y)/(2d * sigma * sigma));
		return G;
	}
	
	public static double[][] GausianMask(int rad_x, int rad_y, double strength) {
		double[][] out = new double[rad_x*2+1][rad_y*2+1];
		for(int x = 0; x <= rad_x; x++) {
			for(int y = 0; y <= rad_y; y++) {
				double G = (double) ((int)(GausianMask(x, y, 3, 3, strength) * (1000*strength))/10d);
				out[x+rad_x][y+rad_y] = G;
				out[x+rad_x][rad_y-y] = G;
				out[rad_x -x][rad_y-y] = G;
				out[rad_x -x][y+rad_y] = G;				
			}
		}
		return out;
	}
	
	
	
	public static Texture invertColor(URL from) {
		
		DrawMap pix = new DrawMap(from);
		for(int x = 0; x < pix.getWidth(); x++) {
			for(int y = 0; y < pix.getHeight(); y++) {
				Color c = pix.getPixel(x, y);
				pix.drawPixel(x, y, Color.invert(c));
			}
		}
		Texture s2 = new Texture(pix);
		pix.destroy();
		return s2;		
	}
	
	//kein plan was das geamcht hat war noch aus libgdx
//	public static int createLinear(int color, int a, int b) {	
//		return a*color+b;
//	}
	
	
	public static Texture createColorBar() {
		DrawMap map = new DrawMap(new Vector2(255*6f, 1f), Color.BLACK());
		
		for (float i = 0; i < 255; i++) {
			map.drawPixel((int)i, 0, new Color(255f, i, 0f, 255f));
		}			
		for (float i = 0; i < 255; i++) {			
			map.drawPixel(255*2 - (int)i-1, 0, new Color(i, 255f, 0f, 255f));
		}			
		for (float i = 0; i < 255; i++) {
			map.drawPixel(255*2 + (int)i-1, 0,new Color(0f, 255f, i, 255f));
		}
		for (float i = 0; i < 255; i++) {
			map.drawPixel(255*4 - (int)i-2, 0,new Color(0f, i, 255f, 255f));
		}
		for (float i = 0; i < 255; i++) {
			map.drawPixel(255*4 + (int)i-2, 0,new Color(i, 0f, 255f, 255f));
		}
		
		for (float i = 0; i < 255; i++) {
			map.drawPixel(255*6 - (int)i-3, 0,new Color(255f, 0f, i, 255f));
		}
		
		Texture t = new Texture(map);
		map.destroy();	
		return t;
	}
	
	
	
	
	/**
	 * Convert the {@link BufferedImage} to the {@link GLFWImage}.
	 */
	public static GLFWImage imageToGLFWImage(BufferedImage image) {
	  if (image.getType() != BufferedImage.TYPE_INT_ARGB_PRE) {
	    final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
	    final Graphics2D graphics = convertedImage.createGraphics();
	    final int targetWidth = image.getWidth();
	    final int targetHeight = image.getHeight();
	    graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
	    graphics.dispose();
	    image = convertedImage;
	  }
	  final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
	  for (int i = 0; i < image.getHeight(); i++) {
	    for (int j = 0; j < image.getWidth(); j++) {
	      int colorSpace = image.getRGB(j, i);
	      buffer.put((byte) ((colorSpace << 8) >> 24));
	      buffer.put((byte) ((colorSpace << 16) >> 24));
	      buffer.put((byte) ((colorSpace << 24) >> 24));
	      buffer.put((byte) (colorSpace >> 24));
	    }
	  }
	  buffer.flip();
	  final GLFWImage result = GLFWImage.create();
	  result.set(image.getWidth(), image.getHeight(), buffer);
	  return result;
	}

	
	

	

}

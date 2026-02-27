package com.karmorak.lib.engine.io.images;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
public class ImageLoader {
	
	public int width;
	public int height;
	public int origin_bpp;

	public static ByteBuffer loadImageFast(String path) throws IOException {
		BufferedImage img = ImageIO.read(new File(path));
		int width = img.getWidth();
		int height = img.getHeight();

		// Wir nutzen direkt einen Direct Buffer (wichtig für OpenGL/LWJGL)
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

		// Wir holen uns alle Pixel auf einmal in ein Java-Array (viel schneller als getRGB)
		int[] pixels = new int[width * height];
		img.getRGB(0, 0, width, height, pixels, 0, width);

		// Wir füllen den Buffer effizient über einen IntBuffer-View
		// WICHTIG: Die ByteOrder muss zum System passen (nativeOrder)
		buffer.order(ByteOrder.nativeOrder());
		buffer.asIntBuffer().put(pixels);

		// Sicherstellen, dass die Position für OpenGL auf 0 steht
		buffer.rewind();

		return buffer;
	}
	
	public int[][] get2DPixelArrayFast(BufferedImage image) {
	    byte[] pixelData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    width = image.getWidth();
	    height = image.getHeight();
	    boolean hasAlphaChannel = image.getAlphaRaster() != null;

	    int[][] result = new int[height][width];
	    if (hasAlphaChannel) {
	        int numberOfValues = 4;
	        origin_bpp = 4;
	        for (int valueIndex = 0, row = 0, col = 0; valueIndex + numberOfValues - 1 < pixelData.length; valueIndex += numberOfValues) {
	            
	            int argb = 0;	          
	            argb += ((int) pixelData[valueIndex + 1] & 0xff); // blue value
	            argb += (((int) pixelData[valueIndex + 2] & 0xff) << 8); // green value
	            argb += (((int) pixelData[valueIndex + 3] & 0xff) << 16); // red value
	            argb += (((int) pixelData[valueIndex] & 0xff) << 24); // alpha value
	            result[row][col] = argb;

	            col++;
	            if (col == width) {
	                col = 0;
	                row++;
	            }
	        }
	    } else {
	        int numberOfValues = 3;
	        origin_bpp = 3;
	        for (int valueIndex = 0, row = 0, col = 0; valueIndex + numberOfValues - 1 < pixelData.length; valueIndex += numberOfValues) {
	            int argb = 0;
	            
	            argb += ((int) pixelData[valueIndex] & 0xff); // blue value
	            argb += (((int) pixelData[valueIndex + 1] & 0xff) << 8); // green value
	            argb += (((int) pixelData[valueIndex + 2] & 0xff) << 16); // red value
	            argb += 255; // 255 alpha value (fully opaque)
	            result[row][col] = argb;

	            col++;
	            if (col == width) {
	                col = 0;
	                row++;
	            }
	        }
	    }

	    return result;
	}
	
//	public static int[] ByteBufferToIntArray(ByteBuffer buffer) {
//	    int[] ret = new int[buffer.capacity() / 4];
//	    buffer.asIntBuffer().put(ret);
//	    return ret;
//	}

	public static int[] ByteBufferToIntArray2(ByteBuffer buffer) {
		int[] ret = new int[buffer.capacity() / 4];
		buffer.rewind();
		// Nutze die eingebaute Massen-Kopier-Funktion:
		buffer.order(ByteOrder.nativeOrder()).asIntBuffer().get(ret);
		return ret;
	}


	public static ByteBuffer IntArrayToBuffer(int width, int height, int[] pixels) {
		// Ensure you are using a Direct Buffer for JNI/OpenGL safety
		ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
		buffer.order(ByteOrder.nativeOrder());

		// Using IntBuffer view is significantly faster than calling putInt() in a loop
		buffer.asIntBuffer().put(pixels);

		// Explicitly set position to 0 so the next call reads from the start
		buffer.position(0);

		return buffer;
	}

	public static ByteBuffer IntArrayToBuffer(int t_x, int t_y, int width, int height, int src_width, int src_height, int[] pixels) {
		// 1. Validierung: Verhindert den Crash bevor er passiert
		if (t_x < 0 || t_y < 0 || t_x + width > src_width || t_y + height > src_height) {
			System.err.println("Fehler: Textur-Ausschnitt liegt außerhalb des Bildes!");
			// Statt null geben wir lieber einen leeren Puffer oder werfen eine klare Exception
			return BufferUtils.createByteBuffer(0);
		}

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4)
				.order(ByteOrder.nativeOrder());

		// Wir nutzen einen IntBuffer-View für schnelleres Schreiben
		java.nio.IntBuffer intBuffer = buffer.asIntBuffer();

		for (int y = 0; y < height; y++) {
			int yc = (t_y + y) * src_width;
			// Ein ganzer Zeilenabschnitt wird kopiert (schneller als einzelne putInt)
			intBuffer.put(pixels, yc + t_x, width);
		}

		// WICHTIG: rewind() sorgt dafür, dass die Position wieder auf 0 steht
		buffer.rewind();
		return buffer;
	}

	public static ByteBuffer copyBuffer(ByteBuffer original) {
		ByteBuffer copy = BufferUtils.createByteBuffer(original.capacity());
		copy.put(original);
		copy.rewind();

		return copy;
	}

	public static ByteBuffer copyBuffer(int x, int y, int width, int height, int src_width, ByteBuffer original) {

		ByteBuffer target = ByteBuffer.allocateDirect(width * height * 4)
				.order(ByteOrder.nativeOrder());

		byte[] rowBuffer = new byte[width * 4];

		for(int row = 0; row < height; row++) {
			int sourcePos = ((y + row) * src_width + x) * 4;
			original.position(sourcePos);
			original.get(rowBuffer);

			target.put(rowBuffer);
		}

		target.rewind();
		original.rewind();
		return target;
	}

	private static byte[] toByteArray(ByteBuffer byteBuffer) {
		byte[] bytesArray = new byte[byteBuffer.remaining()];
		byteBuffer.get(bytesArray, 0, bytesArray.length);
		return bytesArray;
	}

	public static byte[] toByteArray(int[] ints) {
		final ByteBuffer buf = ByteBuffer.allocate(ints.length * 4)
				.order(ByteOrder.nativeOrder());
		buf.asIntBuffer().put(ints);
		return buf.array();
	}

	public static void DEBUGBUFFER(int x, int y, int width, ByteBuffer buffer) {
		System.out.print("Cap: " + buffer.capacity() +"; Limit: " + buffer.limit() + "; Pos: " +  buffer.position());
		int  packedValue = buffer.getInt(4 * (y * width + x));
		int R = packedValue & 255;
		int G = (packedValue >> 8) & 255;
		int B = (packedValue >> 16) & 255;
		int A = (packedValue >> 24) & 255;
		System.out.println(" [R " + R + "G " + G + "B " +  B + "A "+ A + "] " + packedValue);
	}
}

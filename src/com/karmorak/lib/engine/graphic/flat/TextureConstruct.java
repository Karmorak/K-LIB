package com.karmorak.lib.engine.graphic.flat;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL46.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.karmorak.lib.Colorable;
import com.karmorak.lib.engine.io.images.ImageLoader;
import org.lwjgl.BufferUtils;

import com.karmorak.lib.Color;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;
import com.karmorak.lib.math.Vector4;
import com.karmorak.lib.utils.PNGDecoder;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public abstract class TextureConstruct {
		

	protected float scale;
	protected Vector2 pos;
	protected Vector2 size;
	protected Vector3 rotation;
	protected boolean flipX;
	protected boolean flipY;
	public Colorable overlayColor;
	public float overlayColorIntensity;

	protected int min_filter = -1;
	protected int mag_filter = -1;
	
	protected static int default_min_filter = GL_NEAREST;
	protected static int default_mag_filter = GL_LINEAR;
	


	public static int generateTextureID() {
		int id = glGenTextures();
		if (id <= 0) {
			System.err.println("OpenGL Fehler: Textur-ID konnte nicht generiert werden! (ID=" + id + ")");
			// Optional: glGetError() abfragen für Details
			int err = glGetError();
			if (err != 0) System.err.println("GL Error Code: " + err);
		}
		return id;
	}


	public static TextureData loadURL(URL path) {
//		return loadURL(path, default_min_filter, default_mag_filter);
		return loadURLNew(path, default_min_filter, default_mag_filter);
	}

	public static TextureData loadURL(String path) {
//		return loadURL(path, default_min_filter, default_mag_filter);
		return loadURLNew(path, default_min_filter, default_mag_filter);
	}



	public static TextureData loadURL(URL path, int min_filter, int mag_filter) {

		ByteBuffer buffer;
		int width = -1;
		int height = -1;

		try (InputStream stream = path.openStream()) {
			if(!path.toString().endsWith(".png"))
				throw new IOException("Nur PNG wird momentan unterstützt: " + path);

			PNGDecoder dec = new PNGDecoder(stream);

			width = dec.getWidth();
					height = dec.getHeight();

			buffer = BufferUtils.createByteBuffer(width * height * 4)
							.order(ByteOrder.nativeOrder());
			dec.decode(buffer, width * 4, PNGDecoder.Format.RGBA);

			buffer.rewind();
			stream.close();

			int ID = generateTextureID();
			bindTexture(ID, width, height, buffer, min_filter, mag_filter);

			return new TextureData(ID, width, height, path, 4);
		} catch (IOException | NullPointerException _) {
			System.err.println(">" + path  + "< failed to load texture from URL.");
			return null;
		}
	}

	static ByteBuffer urlToByteBuffer(URL url) throws IOException {
		ByteBuffer buffer;

		try (InputStream is = url.openStream(); ReadableByteChannel rbc = Channels.newChannel(is)) {
			buffer = BufferUtils.createByteBuffer(128 * 1024);
			while (true) {
				int bytes = rbc.read(buffer);
				if (bytes == -1) break;
				if (buffer.remaining() == 0) {
					ByteBuffer newBuffer = BufferUtils.createByteBuffer(buffer.capacity() * 2);
					buffer.flip();
					newBuffer.put(buffer);
					buffer = newBuffer;
				}
			}
		}
		buffer.flip();
		return buffer;
	}

	public static TextureData loadURLNew(String path, int min_filter, int mag_filter) {

		ByteBuffer buffer;
		int width = -1, height = -1;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

//			STBImage.stbi_set_flip_vertically_on_load(true);
			buffer = STBImage.stbi_load(path, w, h, channels, 4); // 4 = RGBA

			if (buffer == null) {
				throw new RuntimeException("Failed to load texture from URL: " + path + " " + STBImage.stbi_failure_reason());
			}

			width = w.get();
			height = h.get();

			int ID = generateTextureID();
			bindTexture(ID, width, height, buffer, min_filter, mag_filter);
			STBImage.stbi_image_free(buffer);
			return new TextureData(ID, width, height, convertToURL(path), 4);
		}
	}

	public static URL convertToURL(String absolutePath) {
		try {
			return new File(absolutePath).toURI().toURL();
		} catch (Exception e) {
			System.out.println("DrawMap > cant cast String to URL");
		}
		return null;
	}

	public static TextureData loadURLNew(URL path, int min_filter, int mag_filter) {
		try {
			ByteBuffer imageBuffer = urlToByteBuffer(path);
			int width = -1, height = -1;
			ByteBuffer data;

			try (MemoryStack stack = MemoryStack.stackPush()) {
				IntBuffer w = stack.mallocInt(1);
				IntBuffer h = stack.mallocInt(1);
				IntBuffer channels = stack.mallocInt(1);

//				STBImage.stbi_set_flip_vertically_on_load(true);
				data = STBImage.stbi_load_from_memory(imageBuffer, w, h, channels, 4); // 4 = RGBA

				if (data == null) {
					throw new RuntimeException("Failed to load texture from URL: " + path + " " + STBImage.stbi_failure_reason());
				}

				width = w.get();
				height = h.get();

				int ID = generateTextureID();
				bindTexture(ID, width, height, data, min_filter, mag_filter);
				STBImage.stbi_image_free(data);
				return new TextureData(ID, width, height, path, 4);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static TextureData copyTexture(int width, int height, ByteBuffer buffer) {
		ByteBuffer copy = ImageLoader.copyBuffer(buffer);
		copy.rewind();

		int ID = generateTextureID();
		bindTexture(ID, width, height, copy, default_min_filter, default_mag_filter);
		return new TextureData(ID, width, height, null, 4);

	}

	public static TextureData loadURL(int width, int height, int bpp, ByteBuffer buffer) {
		int ID = generateTextureID();

		buffer.rewind();
		
		bindTexture(ID, width, height, buffer, default_min_filter, default_mag_filter);
		return new TextureData(ID, width, height, null, bpp);
				
	}

	
	public static void bindTexture(int id, int width, int height, ByteBuffer buffer) {
		bindTexture(id, width, height, buffer, default_min_filter, default_mag_filter);
	}	
	
	public static void bindTexture(int id, int width, int height, ByteBuffer buffer, int min_filter, int max_filter) {
		if (id <= 0) {
			System.err.println("Abbruch: bindTexture mit ungültiger ID aufgerufen.");
			return;
		}

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
			if (buffer.remaining() < width * height * 4) {
				throw new RuntimeException("Buffer zu klein für Textur-Upload! Erwartet: " + (width * height * 4));
			}
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

			// GENERATE SECOND (only if filter allows)
			if (min_filter != GL_NEAREST && min_filter != GL_LINEAR) {
				glGenerateMipmap(GL_TEXTURE_2D);
			}
		}

		glBindTexture(GL_TEXTURE_2D, 0);
	}

	static TextureData DrawMapToData(DrawMap map) {
		int id = generateTextureID();
		map.create();
		bindTexture(id, map.getSourceWidth(), map.getSourceHeight(), map.buffer_cache);

        return new TextureData(id, map.getSourceWidth(), map.getSourceHeight(), map.getPATH(), 4);
	}

	static TextureData RegionToTexture(TextureRegion region) {

		int regionX = (int) region.getTexBounds().getX();
		int regionY = (int) region.getTexBounds().getY();
		int regionWidth = (int) region.getTexBounds().getWidth();
		int regionHeight = (int) region.getTexBounds().getHeight();

		int ID = generateTextureID();


		glBindTexture(GL_TEXTURE_2D, ID);

		// Safety: Tell GL to read every single byte without skipping
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, regionWidth, regionHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, default_min_filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, default_mag_filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glCopyImageSubData(
				region.getID(), GL_TEXTURE_2D, 0,
				regionX, regionY,0,
				ID, GL_TEXTURE_2D, 0,
				0, 0, 0,
				regionWidth, regionHeight, 1
				);

		glBindTexture(GL_TEXTURE_2D, 0);

		return new TextureData(ID, regionWidth, regionHeight, region.getPATH(), 4);
	}

	
	public abstract void render();
	
	public abstract void destroy();

	public Vector2 getPosition() {		
		return pos;		
	}
	
	public float getX() {
		return pos.getX();
	}
	
	public float getY() {
		return pos.getY();
	}
	
	public void setPosition(Vector2 pos) {
		this.pos = new Vector2(pos.getX(), pos.getY());
	}
	
	public void setPosition(float x, float y) {
		this.pos = new Vector2(x, y);
	}
	
	public void setX(float x) {
		setPosition(x, getPosition().getY());
	}
	
	public void setY(float y) {
		setPosition(getPosition().getX(), y);
	}

	public Vector2 getSourceSize() {
		return new Vector2(getSourceWidth(), getSourceHeight());
	}

	public Vector2 getSize() {
		return size;
	}
	
	public void setWidth(float width) {
		this.size.setWidth(width);
	}
	
	public void setHeight(float height) {
		this.size.setHeight(height);
	}
	
	public void setSize(Vector2 size) {
		this.size = new Vector2(size.getWidth(), size.getHeight());
	}

	public void setSize(float width, float height) {
		this.size = new Vector2(width, height);
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
		
	public float getScale() {
		return this.scale;
	}
	
	public float getWidth() {
		return size.getWidth() * getScale();
	}

	public int getWidthUnscaled() {
		return (int) size.getWidth();
	}
	
	public int getSourceWidth() {
		return getData().WIDTH;
	}
	
	public float getHeight() {
		return size.getHeight() * getScale();
	}

	public int getHeightUnscaled() {
		return (int) size.getHeight();
	}
	
	public int getSourceHeight() {
		return getData().HEIGHT;
	}
	
	public int getID() {
		return getData().getID();
	}
	
	public URL getPATH() {
		return getData().getPATH();
	}
	
	public void setBounds(float x, float y, float width, float height) {
		setPosition(x, y);
		setSize(width, height);
	}
	
	public void setBounds(Vector2 pos, Vector2 size) {
		setBounds(pos.getX(), pos.getY(), size.getWidth(), size.getHeight());
	}
	
	public void setBounds(Vector4 bounds) {
		setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}
	
	public void setRotation(float rotation) {
		this.rotation.setZ(rotation);
	}
	
	public void setRotationX(float rotation) {
		this.rotation.setX(rotation);
	}
	
	public void setRotationY(float rotation) {
		this.rotation.setY(rotation);
	}
	
	public void setRotationZ(float rotation) {
		this.rotation.setZ(rotation);
	}
	
	public void setRotation(Vector3 rotation ) {
		this.rotation = rotation;
	}	
	
	public Vector3 getRotation() {
		return this.rotation;
	}
	
	public void flipX(boolean X) {
		this.flipX = X;
	}
	
	public void flipY(boolean Y) {
		this.flipY = Y;
	}
	
	public TextureConstruct flip(boolean X, boolean Y) {
		this.flipX = X;
		this.flipY = Y;
		return this;
	}
	
	public boolean isFlipX() {
		return flipX;
	}
	
	public boolean isFlipY() {
		return flipY;
	}

	public TextureConstruct setColor(Colorable c) {
		overlayColor = c;
		return this;
	}
	
	public void setColorintensity(float intensity) {
		overlayColorIntensity = intensity;
	}

	public void setColor(Colorable c, float intensity) {
		overlayColor = c;
		overlayColorIntensity = intensity;
	}
		
	public Color getOverlayColor() {
		return overlayColor.toColor();
	}
	
	public float getOverlayColorIntensity() {
		return overlayColorIntensity;
	}
	
	public void setAlpha(int alpha) {
		overlayColor.toColor().setAlpha(alpha);
	}
		
	public abstract TextureData getData();
	

	public static void setDefaultMinFilter(int filter) {
		default_min_filter = filter;
	}
	
	public static void setDefaultMagFilter(int filter) {
		default_mag_filter = filter;
	}
	
	
	public int getMinFilter() {
		
		if(min_filter != -1) return min_filter;
		else return default_min_filter;
		
	}

	public int getMagFilter() {
		
		if(mag_filter != -1) return mag_filter;
		else return default_mag_filter;
		
	}
	
	public void setMinFilter(int filter) {
		min_filter = filter;
	}
	
	public void setMagFilter(int filter) {
		mag_filter = filter;
	}


	
}
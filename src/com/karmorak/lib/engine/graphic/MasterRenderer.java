package com.karmorak.lib.engine.graphic;

import com.karmorak.lib.*;
import com.karmorak.lib.engine.graphic.flat.*;
import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.engine.graphic.roomy.renderers.Renderer;
import com.karmorak.lib.engine.graphic.roomy.renderers.TerrainRenderer;
import com.karmorak.lib.engine.graphic.shaders.InstanceBuffer;
import com.karmorak.lib.engine.graphic.shaders.ObjectShader;
import com.karmorak.lib.engine.graphic.shaders.TerrainShader;
import com.karmorak.lib.engine.graphic.shaders.TextureShader;
import com.karmorak.lib.engine.objects.Camera;
import com.karmorak.lib.engine.objects.Light;
import com.karmorak.lib.engine.objects.OBJECT;
import com.karmorak.lib.engine.terrain.Terrain;
import com.karmorak.lib.font.ownchar.OwnCharData;
import com.karmorak.lib.math.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MasterRenderer {


	private ObjectShader shader  = new ObjectShader();
	private TerrainShader terrainShader = new TerrainShader();
	public static TextureShader textureShader;

	private Renderer renderer;
	private TerrainRenderer terrainRenderer;

	private Map<Mesh, List<OBJECT>> objects = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();


	private int highest_layer = 1;

	private Vector3 backgroundColor = new Vector3(0.7f, 1f, 1f);
	public static boolean updateWindow = true;
	private final FloatBuffer batchBuffer;

	private static class SpriteDataList {
		float[] data = new float[64];
		int size = 0;

		void add(float x, float y, float w, float h, float rot, float fx, float fy) {
			if (size + 7 > data.length) {
				data = java.util.Arrays.copyOf(data, Math.max(data.length * 2, size + 7));
			}
			data[size++] = x;
			data[size++] = y;
			data[size++] = w;
			data[size++] = h;
			data[size++] = rot;
			data[size++] = fx;
			data[size++] = fy;
		}

		void clear() {
			size = 0;
		}
	}

	// Layer -> (Textur -> gesammelte Sprites)
	private final HashMap<Integer, LinkedHashMap<TextureConstruct, SpriteDataList>> layerBatches = new HashMap<>();

	private static class DrawCall {
		TextureConstruct texture;
		int startInstance;
		int count;

		DrawCall(TextureConstruct texture, int startInstance, int count) {
			this.texture = texture;
			this.startInstance = startInstance;
			this.count = count;
		}
	}

	private final ArrayList<DrawCall> drawCalls = new ArrayList<>();


	public MasterRenderer() {
		renderer = new Renderer(shader);
		terrainRenderer = new TerrainRenderer(terrainShader);

		// Buffer erstellen: 10.000 * 4 * 4 Bytes (da Float 4 Bytes hat)
		int capacity = InstanceBuffer.MAX_SPRITES * InstanceBuffer.FLOATS_PER_INSTANCE;
		// Nutze BufferUtils für einen direkten Buffer, falls verfügbar
		batchBuffer = BufferUtils.createFloatBuffer(capacity);

	}

	private static class BatchDrawCall {
		int startOffset; // Startindex im FloatBuffer
		int elementCount; // Anzahl der Sprites (4 Floats pro Sprite)

		public BatchDrawCall(int startOffset) {
			this.startOffset = startOffset;
			this.elementCount = 0;
		}
	}

	public void create() {
		enableCulling();
		if(!shader.isCreated()) shader.create();
//		terrainRenderer.create(); /* not in use */
		Renderable.init();
		textureShader = Renderable.getShader();
		InstanceBuffer.init(Texture.getVAO());

		System.out.println("w5-2-1");
	}


	public void renderBatch(Running run) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(backgroundColor.getX(), backgroundColor.getY(), backgroundColor.getZ(), 0f);

		textureShader.bind();

		if (updateWindow) {
			textureShader.loadProjectionMatrix((int) KLIB.graphic.Width(), (int) KLIB.graphic.Height());
			updateWindow = false;
		}

		batchBuffer.clear();
		drawCalls.clear();

		// 1. Buffer lückenlos befüllen – Layer für Layer, Textur für Textur
		for (int i = 0; i < highest_layer; i++) {
			LinkedHashMap<TextureConstruct, SpriteDataList> map = layerBatches.get(i);
			if (map == null) continue;

			for (Map.Entry<TextureConstruct, SpriteDataList> entry : map.entrySet()) {
				SpriteDataList list = entry.getValue();
				if (list.size == 0) continue;

				int startInstance = batchBuffer.position() / InstanceBuffer.FLOATS_PER_INSTANCE;
				int count = list.size / InstanceBuffer.FLOATS_PER_INSTANCE;

				if (batchBuffer.remaining() < list.size) {
					System.err.println("Batch Buffer Overflow! Zu viele Sprites in einem Frame.");
					break;
				}

				batchBuffer.put(list.data, 0, list.size);
				drawCalls.add(new DrawCall(entry.getKey(), startInstance, count));
			}
		}

		batchBuffer.flip();

		// 2. Ein einziger VBO-Upload für den gesamten Frame
		if (batchBuffer.hasRemaining()) {
			glBindBuffer(GL_ARRAY_BUFFER, InstanceBuffer.getVboId());
			glBufferData(GL_ARRAY_BUFFER, (long) InstanceBuffer.MAX_SPRITES * InstanceBuffer.STRIDE, GL_STREAM_DRAW);
			glBufferSubData(GL_ARRAY_BUFFER, 0, batchBuffer);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}

		// 3. Zeichnen in strikter Layer-Reihenfolge
		startrender(Texture.getVAO());

		for (DrawCall call : drawCalls) {
			if (call.texture instanceof Renderable renderable) {
				renderable.renderManual(batchBuffer, call.startInstance, call.count, textureShader);
			}
		}

		endrender();
		textureShader.unbind();

		// 4. Zurücksetzen für den nächsten Frame (Listen leeren, aber float-Arrays behalten)
		for (LinkedHashMap<TextureConstruct, SpriteDataList> map : layerBatches.values()) {
			for (SpriteDataList list : map.values()) {
				list.clear();
			}
		}
		highest_layer = 1;
	}

	public static void updateWindowSize() {
		updateWindow = true;
	}

//    public void renderBatch(Light l, Camera camera, Running run) {
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
//        glClearColor(backgroundColor.getX(), backgroundColor.getY(), backgroundColor.getZ(), 1f);
//
//        shader.bind();
//        shader.loadSkyColor(backgroundColor);
//        shader.loadLight(l);
//        shader.loadViewMatrix(camera);
//        shader.loadProjectionMatrix(run.getWindow().getProjectionMatrix());
//        renderer.render(objects);
//        shader.unbind();
//
//        terrainShader.bind();
//        terrainShader.loadSkyColor(backgroundColor);
//        terrainShader.loadLight(l);
//        terrainShader.loadViewMatrix(camera);
//        terrainShader.loadProjectionMatrix(run.getWindow().getProjectionMatrix());
//        terrainRenderer.render(terrains);
//        terrainShader.unbind();
//
//
//        textureShader.bind();
//        startrender(Texture.getVAO());
//
//        for (int i = 0; i < highest_layer; i++) {
//
//            if(newChars.containsKey(i)) {
//                HashMap<CharTexture, ArrayList<OwnCharData>> list = newChars.get(i);
//
//                for (CharTexture t : list.keySet()) {
//                    CharTexture.render_chars_manual(t, list.get(t), textureShader);
//
//                }
//            }
//            if(newTextures.containsKey(i)) {
//                for(TextureConstruct t : newTextures.get(i)) {
//                    // Falls t das Interface unterstützt, zeichne es einfach
//                    if (t instanceof Renderable renderable) {
//                        renderable.renderManual(TextureDatas.get(t), textureShader);
//                    }
//                }
//            }
//        }
//        endrender();
//        textureShader.unbind();
//        terrains.clear();
//        objects.clear();
//        newTextures.clear();
//        newChars.clear();
//        drawCallsPerLayer.clear();
//    }


	public void draw(Texture t, float x, float y, float width, float height) {
		textureShader.bind();
		t.render(x, y, width, height);
		textureShader.unbind();
	}

	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}

	public void destroy() {
		shader.destroy();
		terrainShader.destroy();
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void processObject(OBJECT obj) {
		Mesh mesh = obj.getMesh();
		List<OBJECT> batch = objects.get(mesh);
		if(batch == null){
			batch = new ArrayList<OBJECT>();
		}
		batch.add(obj);
		objects.put(mesh, batch);
	}

	public void drawObject(OBJECT obj, Light light, Camera camera, Running run) {
		shader.bind();
		if(light != null) shader.loadLight(light);
		shader.loadViewMatrix(camera);
		shader.loadProjectionMatrix(run.getWindow().getProjectionMatrix());
		renderer.render(obj);
		shader.unbind();
	}


	public void process(TextureConstruct texture) {
		process(texture, texture.getPosition(), texture.getWidthUnscaled(), texture.getHeightUnscaled(), 0);
	}

	public void process(TextureConstruct texture, int layer) {
		process(texture, texture.getPosition(), texture.getWidthUnscaled(), texture.getHeightUnscaled(), layer);
	}

	public void process(TextureConstruct texture, int pos_x, int pos_y) {
		process(texture, pos_x, pos_y, texture.getWidthUnscaled(), texture.getHeightUnscaled(), 0);
	}

	public void process(TextureConstruct texture, int pos_x, int pos_y, int layer) {
		process(texture, pos_x, pos_y, texture.getWidthUnscaled(), texture.getHeightUnscaled(), layer);
	}

	public void process(TextureConstruct texture, float pos_x, float pos_y, int layer) {
		process(texture, (int) pos_x, (int) pos_y, texture.getWidthUnscaled(), texture.getHeightUnscaled(), layer);
	}

	public void process(TextureConstruct texture, Vector2 position, Vector2 size) {
		this.process(texture, (int) position.getX(), (int) position.getY(), (int) size.getWidth(), (int) size.getHeight(), 0);
	}
	public void process(TextureConstruct texture, Vector2 position, Vector2 size, int layer) {
		this.process(texture, (int) position.getX(), (int) position.getY(), (int) size.getWidth(), (int) size.getHeight(), layer);
	}

	public void process(TextureConstruct texture, Vector2 position, int size_x, int size_y) {
		this.process(texture, (int) position.getX(), (int) position.getY(), size_x, size_y, 0);
	}

	public void process(TextureConstruct texture, Vector2 position, int size_x, int size_y, int layer) {
		this.process(texture, (int) position.getX(), (int) position.getY(), size_x, size_y, layer);
	}

	public void process(TextureConstruct texture, Vector2i position, Vector2i size) {
		this.process(texture, position.getX(), position.getY(), size.getWidth(), size.getHeight(), 0);
	}

	public void process(TextureConstruct texture, Vector2i position, Vector2i size, int layer) {
		this.process(texture, position.getX(), position.getY(), size.getWidth(), size.getHeight(), layer);
	}

	public void process(TextureConstruct texture, int pos_x, int pos_y, Vector2i size) {
		this.process(texture, pos_x, pos_y, size.getWidth(), size.getHeight(), 0);
	}

	public void process(TextureConstruct texture, int pos_x, int pos_y, Vector2i size, int layer) {
		this.process(texture, pos_x, pos_y, size.getWidth(), size.getHeight(), layer);
	}

	public void process(TextureConstruct texture, int pos_x, int pos_y, int size_x, int size_y) {
		this.process(texture, pos_x, pos_y, size_x, size_y, 0);
	}

	public void process(TextureConstruct texture, Vector4i bounds, int layer) {
		this.process(texture, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), layer);
	}

	public void process(TextureConstruct texture, Vector4 bounds, int layer) {
		this.process(texture, (int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight(), layer);
	}

	public void process(TextureConstruct texture, int pos_x, int pos_y, int size_x, int size_y, int layer) {
		LinkedHashMap<TextureConstruct, SpriteDataList> map = layerBatches.computeIfAbsent(layer, k -> new LinkedHashMap<>());
		SpriteDataList list = map.computeIfAbsent(texture, k -> new SpriteDataList());

		list.add(
				(float) pos_x,
				(float) pos_y,
				size_x * texture.getScale(),
				size_y * texture.getScale(),
				texture.getRotation().getZ(),
				texture.isFlipX() ? -1.0f : 1.0f,
				texture.isFlipY() ? -1.0f : 1.0f
		);

		if (layer + 1 > highest_layer)
			highest_layer = layer + 1;
	}

	public void processChar(CharTexture texture, OwnCharData oc, int layer) {
		LinkedHashMap<TextureConstruct, SpriteDataList> map = layerBatches.computeIfAbsent(layer, k -> new LinkedHashMap<>());
		SpriteDataList list = map.computeIfAbsent(texture, k -> new SpriteDataList());

		list.add(
				(float) oc.getX(),
				(float) oc.getY(),
				oc.getWidth() * oc.getScale(),
				oc.getHeight() * oc.getScale(),
				texture.getRotation().getZ(),
				texture.isFlipX() ? -1.0f : 1.0f,
				texture.isFlipY() ? -1.0f : 1.0f
		);

		if (layer + 1 > highest_layer)
			highest_layer = layer + 1;
	}


	public Color getBackgroundColor() {
		return new Color(backgroundColor);
	}

	public void setBackgroundColor(Colorable color) {
		this.backgroundColor = color.Vec3f();
	}
	public void setBackgroundColor(Vector3 backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setBackgroundColor(float R, float G, float B) {
		this.backgroundColor.set(R, G, B);
	}

	public void setBackgroundColor(int R, int G, int B) {
		this.backgroundColor.set(R / 255f, G / 255f, B / 255f);
	}

	static void startrender(int vao) {
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0); // pos
		glEnableVertexAttribArray(1); // uv
		glEnableVertexAttribArray(2); // instancePixelPosition
		glEnableVertexAttribArray(3); // instancePixelSize
		glEnableVertexAttribArray(4); // instanceRotationZ
		glEnableVertexAttribArray(5); // instanceFlip

		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);

	}

	static void endrender() {
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glBindVertexArray(0);
	}

	public int getHighestLayer() {
		return highest_layer;
	}





}

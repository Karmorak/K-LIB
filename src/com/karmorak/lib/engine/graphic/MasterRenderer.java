package com.karmorak.lib.engine.graphic;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL46.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.karmorak.lib.KLIB;
import com.karmorak.lib.Running;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.CharTexture;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.engine.graphic.flat.TextureConstruct;
import com.karmorak.lib.engine.graphic.flat.TextureRegion;
import com.karmorak.lib.engine.graphic.flat.TextureShader;
import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.engine.graphic.roomy.renderers.Renderer;
import com.karmorak.lib.engine.graphic.roomy.renderers.TerrainRenderer;
import com.karmorak.lib.engine.graphic.roomy.shaders.ObjectShader;
import com.karmorak.lib.engine.graphic.roomy.shaders.TerrainShader;
import com.karmorak.lib.engine.objects.Camera;
import com.karmorak.lib.engine.objects.Light;
import com.karmorak.lib.engine.objects.OBJECT;
import com.karmorak.lib.engine.terrain.Terrain;
import com.karmorak.lib.font.ownchar.OwnCharData;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;
import com.karmorak.lib.math.Vector3;
import com.karmorak.lib.math.Vector4;


public final class MasterRenderer {

	
	private ObjectShader shader  = new ObjectShader();
	private TerrainShader terrainShader = new TerrainShader();
	public static TextureShader textureShader;
	
	private Renderer renderer; 
	private TerrainRenderer terrainRenderer;
	
	private Map<Mesh, List<OBJECT>> objects = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
	
	private final HashMap<Integer, ArrayList<TextureConstruct>> newTextures;
	private final HashMap<TextureConstruct,  ArrayList<Vector4>> TextureDatas;
	
	private final HashMap<Integer, HashMap<CharTexture, ArrayList<OwnCharData>>> newChars;

	
	private int biggestcache = 1;
	
	private Vector3 backgroundColor = new Vector3(0.7f, 1f, 1f);
    public static boolean updateWindow = true;
	
	
	public static class drawData {
		ArrayList<Texture> textures;
		ArrayList<Vector4> bounds;	
	}
	
	public MasterRenderer() {
		renderer = new Renderer(shader);
		terrainRenderer = new TerrainRenderer(terrainShader);
		
		newTextures = new HashMap<>();
		TextureDatas = new HashMap<>();
		
		newChars = new HashMap<>();		
//		CharDatas = new HashMap<>(); 
	}
	
	public void create() {
        enableCulling();
		if(!shader.isCreated()) shader.create();
//		terrainRenderer.create(); /* not in use */
		Renderable.init();
		textureShader = Renderable.getShader();
	}


	public void render(Running run) {
		synchronized(newTextures) { // Wichtig gegen den Crash!
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glClearColor(backgroundColor.getX(), backgroundColor.getY(), backgroundColor.getZ(), 0f);

			textureShader.bind();
            if (updateWindow)
                textureShader.loadProjectionMatrix((int) KLIB.graphic.Width(), (int) KLIB.graphic.Height());

			startrender(Texture.getVAO());

			for (int i = 0; i < biggestcache; i++) {
				if(newChars.containsKey(i)) {
					HashMap<CharTexture, ArrayList<OwnCharData>> list = newChars.get(i);
					for (CharTexture t : list.keySet()) {
						CharTexture.render_chars_manual(t, list.get(t), textureShader);
					}
				}

				if(newTextures.containsKey(i)) {
					for(TextureConstruct t : newTextures.get(i)) {
						// Falls t das Interface unterstützt, zeichne es einfach
						if (t instanceof Renderable renderable) {
							renderable.renderManual(TextureDatas.get(t), textureShader);
						}
					}
				}
			}

			endrender();
			textureShader.unbind();

			// Listen leeren
			newTextures.clear();
			TextureDatas.clear();
			newChars.clear();
		}
	}

    public static void updateWindowSize() {
        updateWindow = true;
    }
	
	public void render(Light l, Camera camera, Running run) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		glClearColor(backgroundColor.getX(), backgroundColor.getY(), backgroundColor.getZ(), 1f);

		shader.bind();
		shader.loadSkyColor(backgroundColor);
		shader.loadLight(l);
		shader.loadViewMatrix(camera);
		shader.loadProjectionMatrix(run.getWindow().getProjectionMatrix());
		renderer.render(objects);	
		shader.unbind();
		
		terrainShader.bind();
		terrainShader.loadSkyColor(backgroundColor);
		terrainShader.loadLight(l);
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadProjectionMatrix(run.getWindow().getProjectionMatrix());
		terrainRenderer.render(terrains);
		terrainShader.unbind();
		

		textureShader.bind();
		startrender(Texture.getVAO());

		for (int i = 0; i < biggestcache; i++) {

			if(newChars.containsKey(i)) {
				HashMap<CharTexture, ArrayList<OwnCharData>> list = newChars.get(i);
					
				for (CharTexture t : list.keySet()) {
					CharTexture.render_chars_manual(t, list.get(t), textureShader);
						
				}
			}
			if(newTextures.containsKey(i)) {
				for(TextureConstruct t : newTextures.get(i)) {
					// Falls t das Interface unterstützt, zeichne es einfach
					if (t instanceof Renderable renderable) {
						renderable.renderManual(TextureDatas.get(t), textureShader);
					}
				}
			}
		}
		endrender();
		textureShader.unbind();		
		terrains.clear();
		objects.clear();
		newTextures.clear();
		newChars.clear();
		TextureDatas.clear();
	}

	
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

    public void process(TextureConstruct texture, int pos_x, int pos_y, int size_x, int size_y, int layer) {
        Vector4 bounds = new Vector4(pos_x, pos_y, size_x, size_y);

        if (newTextures.get(layer) == null) {
            ArrayList<TextureConstruct> ar = new ArrayList<TextureConstruct>();
            ar.add(texture);
            newTextures.put(layer, ar);
        } else {
            if (!newTextures.get(layer).contains(texture))
                newTextures.get(layer).add(texture);
        }

        if (TextureDatas.get(texture) == null) {
            ArrayList<Vector4> ar2 = new ArrayList<Vector4>();
            ar2.add(bounds);
            TextureDatas.put(texture, ar2);
        } else {
            TextureDatas.get(texture).add(bounds);
        }

        if (layer + 1 > biggestcache)
            biggestcache = layer + 1;
	}
	
	public void processChar(CharTexture t_char, OwnCharData oc, int layer) {
		HashMap<CharTexture, ArrayList<OwnCharData>> l;
		ArrayList<OwnCharData> ocs;

		if(newChars.get(layer) == null) {
			l = new HashMap<>();
			ocs = new ArrayList<>();
		} else if (newChars.get(layer).get(t_char) == null) {
			l = newChars.get(layer);
			ocs = new ArrayList<>();
		} else {
			l = newChars.get(layer);
			ocs = l.get(t_char);
		}

		ocs.add(oc);
		l.put(t_char, ocs);
		newChars.put(layer, l);

		if(layer+1 > biggestcache)
			biggestcache = layer+1;
	}
	
	public void processDrawMap(DrawMap texture, int layer) {
        processDrawMap(texture, texture.getX(), texture.getY(), texture.getSourceWidth(), texture.getSourceHeight(), layer);
	}
	public void processDrawMap(DrawMap texture, float pos_x, float pos_y, int layer) {
        processDrawMap(texture, pos_x, pos_y, texture.getSourceWidth(), texture.getSourceHeight(), layer);
	}
	public void processDrawMap(DrawMap texture, Vector2 pos , Vector2 size, int layer) {		
		processDrawMap(texture, pos.getX(), pos.getY(), size.getWidth(), size.getHeight(), layer);
	}
	public void processDrawMap(DrawMap texture, float pos_x, float pos_y, float size_x, float size_y, int layer) {
        Vector4 bounds = new Vector4(pos_x, pos_y, size_x, size_y);
		
		if(newTextures.get(layer) == null) {			
			ArrayList<TextureConstruct>  ar  = new ArrayList<TextureConstruct>();
			ar.add(texture);
			newTextures.put(layer, ar);			
		} else {				
			if(!newTextures.get(layer).contains(texture))
				newTextures.get(layer).add(texture);			
		}	
		
		if(TextureDatas.get(texture) == null) {
			ArrayList<Vector4>  ar2  = new ArrayList<Vector4>();
            ar2.add(bounds);
			TextureDatas.put(texture, ar2);
		} else {
            TextureDatas.get(texture).add(bounds);
		}
		
		if(layer+1 > biggestcache)
			biggestcache = layer+1;
	}
	
	
	public void processTexture(Texture texture) {
        processTexture(texture, texture.getPosition(), texture.getSize(), 0);
	}
	
	public void processTexture(Texture texture, int layer) {
		processTexture(texture, texture.getPosition(), texture.getSize(), layer);
	}

    public void processTexture(Texture texture, Vector2 pos, Vector2 size) {
        processTexture(texture, pos, size, 0);
    }
	public void processTexture(Texture texture, float pos_x, float pos_y) {
        processTexture(texture, new Vector2(pos_x, pos_y), texture.getSize(), 0);
	}
	
	public void processTexture(Texture texture, float pos_x, float pos_y, int layer) {
        processTexture(texture, new Vector2(pos_x, pos_y), texture.getSize(), layer);
	}
	
	public void processTexture(Texture texture, float pos_x, float pos_y, float size_x, float size_y) {
        processTexture(texture, pos_x, pos_y, size_x, size_y, 0);
	}
	public void processTexture(Texture texture, float pos_x, float pos_y, float size_x, float size_y, int layer) {
        Vector4 bounds = new Vector4(pos_x, pos_y, size_x, size_y);

        if (newTextures.get(layer) == null) {
			ArrayList<TextureConstruct>  ar  = new ArrayList<TextureConstruct>();
			ar.add(texture);
            newTextures.put(layer, ar);
        } else {
			if(!newTextures.get(layer).contains(texture))
                newTextures.get(layer).add(texture);
        }

		if(TextureDatas.get(texture) == null) {
			ArrayList<Vector4>  ar2  = new ArrayList<Vector4>();
            ar2.add(bounds);
			TextureDatas.put(texture, ar2);
		} else {
            TextureDatas.get(texture).add(bounds);
		}

		if(layer+1 > biggestcache)
			biggestcache = layer+1;
    }

    public void processTexture(Texture texture, Vector2 pos, Vector2 size, int layer) {
		if(newTextures.get(layer) == null) {			
			ArrayList<TextureConstruct>  ar  = new ArrayList<TextureConstruct>();
            ar.add(texture);
			newTextures.put(layer, ar);			
		} else {
            if (!newTextures.get(layer).contains(texture))
                newTextures.get(layer).add(texture);
        }

        if (TextureDatas.get(texture) == null) {
			ArrayList<Vector4>  ar2  = new ArrayList<Vector4>();
			ar2.add(new Vector4(pos, size));
            TextureDatas.put(texture, ar2);
		} else {
            TextureDatas.get(texture).add(new Vector4(pos, size));
		}
		
		if(layer+1 > biggestcache)
			biggestcache = layer+1;
    }

    public void processTextureRegion(TextureRegion region, Vector2 pos, Vector2 size) {
        processTextureRegion(region, (int) pos.getX(), (int) pos.getY(), (int) size.getWidth(), (int) size.getHeight(), 0);
    }

    public void processTextureRegion(TextureRegion region, int pos_x, int pos_y, int size_x, int size_height, int layer) {
        process(region, pos_x, pos_y, size_x, size_height, layer);
	}

    public Vector3 getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Vector3 backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	static void startrender(int vao) {
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
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
		glBindVertexArray(0);
	}
	
	
	
	
	
	
}

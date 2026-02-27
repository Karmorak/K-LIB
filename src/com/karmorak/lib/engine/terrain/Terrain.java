package com.karmorak.lib.engine.terrain;

import com.karmorak.lib.engine.graphic.roomy.Material;
import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.engine.graphic.roomy.Vertex;
import com.karmorak.lib.engine.io.images.PNG;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;

public class Terrain {
	
	public static final float SIZE = 600;
//	private static final int VERTEX_COUNT = 128;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	
	private float x,y;
	private Mesh mesh;
	private float heights[][];
	private float height;
	
	private TerrainTexturePack pack;
	private Material blendMap;
	private PNG heightMap; //TODO PNG is outdated

	public Terrain(int GridX, int GridZ, TerrainTexturePack pack, Material blendMap, Material heightMap) {
		this.blendMap = blendMap;
		this.pack = pack;		
		this.heightMap = heightMap.getPNG();
		this.height = heightMap.getHeight();
		this.x = GridX * SIZE;
		this.y = GridZ * SIZE;
		
	}
	
	public void create() {
		pack.create();		
		blendMap.create();		
//		heightMap.create(); //TODO
		this.mesh = generateTerrain(heightMap);
		this.mesh.create();
	}
	
	public float getHeights(float worldX, float worldY ) {
		
		Vertex[] verts = mesh.getVertecies();
		
		
		float terrainX = worldX - this.x;
		float terrainY = worldY - this.y;
		
		float gridSquareSize = SIZE / ((float)height -1);
		
		int gridX = (int)Math.floor(terrainX / gridSquareSize); 
		int gridY = (int)Math.floor(terrainY / gridSquareSize); 
		
		if(gridX >= height -1 || gridY >= height-1 || gridX < 0 || gridY < 0) {
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float yCoord = (terrainY % gridSquareSize) / gridSquareSize;
		
		float answer;

		verts[gridY * heightMap.getHeight() + gridX].getPosition().getY();
		
		if (xCoord <= (1-yCoord)) {
			answer = barryCentric(new Vector3(0, getHeight(gridX, gridY), 0), new Vector3(1,
					getHeight(gridX+1, gridY), 0), new Vector3(0,
							getHeight(gridX, gridY+1), 1), new Vector2(xCoord, yCoord));
		} else {
			answer = barryCentric(new Vector3(1, getHeight(gridX+1, gridY), 0), new Vector3(1,
					getHeight(gridX+1, gridY+1), 1), new Vector3(0,
							getHeight(gridX, gridY+1), 1), new Vector2(xCoord, yCoord));
		}
		
		
		return answer;		
	}

	public static float barryCentric(Vector3 p1, Vector3 p2, Vector3 p3, Vector2 pos) {
		float det = (p2.getZ() - p3.getZ()) * (p1.getX() - p3.getX() ) + (p3.getX()  - p2.getX() ) * (p1.getZ() - p3.getZ());
		float l1 = ((p2.getZ() - p3.getZ()) * (pos.getX()  - p3.getX() ) + (p3.getX()  - p2.getX() ) * (pos.getY() - p3.getZ())) / det;
		float l2 = ((p3.getZ() - p1.getZ()) * (pos.getX()  - p3.getX() ) + (p1.getX()  - p3.getX() ) * (pos.getY()  - p3.getZ())) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.getY()  + l2 * p2.getY()  + l3 * p3.getY() ;
	}
	
	private Mesh generateTerrain(PNG heightMap) {
		
		int VERTEX_COUNT = heightMap.getHeight();
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		
		Vertex[] vertices = new Vertex[count];
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){			
				
				Vector3 position = new Vector3((float)j/((float)VERTEX_COUNT - 1) * SIZE, getHeight(j, i, heightMap), (float)i/((float)VERTEX_COUNT - 1) * SIZE);		
				Vector2 textureCoords = new Vector2((float)j/((float)VERTEX_COUNT - 1), (float)i/((float)VERTEX_COUNT - 1));
				Vector3 normal = calculateNormal(j, i);
							
				vertices[vertexPointer] =  new Vertex(position, textureCoords , normal);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return new Mesh(vertices, indices);
	}
	
	private float getHeight(int x, int y) {
		return mesh.getVertecies()[(int) (y * height + x)].getPosition().getY();
	}
	
	private float getHeight(int x, int y, PNG png) {
		if(x < 0 || x >= png.getHeight() || y < 0 || y >= png.getHeight())  {
			return 0;
		}
		
		float height = png.getPixel(x, y).toInt();
		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;
		return height;		
	}
	
	private Vector3 calculateNormal(int x, int y) {
		float heightL = getHeight(x-1, y, heightMap);
		float heightR = getHeight(x+1, y, heightMap);
		float heightD = getHeight(x, y-1, heightMap);
		float heightU = getHeight(x, y+1, heightMap);
		
		Vector3 normal = new Vector3(heightL-heightR, 2f, heightD - heightU);
		normal = Vector3.normalize(normal);
		
		return normal;
	}

	


	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	
	public Mesh getMesh() {
		return mesh;
	}

	public TerrainTexturePack getPack() {
		return pack;
	}

	public Material getBlendMap() {
		return blendMap;
	}

	
}

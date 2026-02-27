package com.karmorak.lib.engine.graphic.roomy;

import java.net.URI;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46.*;
import org.lwjgl.system.MemoryUtil;

import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;

public class Mesh {
	
	/* VAO (Vertex Array Object)
	 * 0 = position data (buffer)
	 * 1 = texture data (buffer)
	 * 2 = normal data (buffer)
	 * 3 = animation data (buffer)
	 * .. other data (buffer)
	 * 
	 * VBO = Vertex Buffer Object;
	 * IBO = Indicies Buffer Object
 */
	
	private Vertex[] vertices;
	private int[] indices;
	private Material material;
	private int vao, vbo, ibo, cbo, tbo, nbo;

	public Mesh(Vertex[] vertices, int[] indicies) {
		this.vertices = vertices;
		this.indices = indicies;
	}
	
	public Mesh(Vertex[] vertices, int[] indicies, URL path) {
		this.vertices = vertices;
		this.indices = indicies;
		
		this.material = new Material(path);
	}
	
	public Mesh(Vertex[] vertices, int[] indicies, Material mat) {
		this.vertices = vertices;
		this.indices = indicies;		
		this.material = mat;
	}
	
	
	
	public void create() {
		if(material != null) material.create();		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);

				
		//PositionBuffer
		FloatBuffer verteciesBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);		
		float[] positionData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			positionData[i * 3] = vertices[i].getPosition().getX();
			positionData[i * 3 +1] = vertices[i].getPosition().getY();
			positionData[i * 3 +2] = vertices[i].getPosition().getZ();
		}
		verteciesBuffer.put(positionData).flip();		
		vbo = storeData(verteciesBuffer, 0, 3);	
		
		//Texture Buffer
			FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);		
			float[] textureData = new float[vertices.length * 2];
			for (int i = 0; i < vertices.length; i++) {
				textureData[i * 2] = vertices[i].getTextureCoord().getX();
				textureData[i * 2 +1] = vertices[i].getTextureCoord().getY();
			}
			textureBuffer.put(textureData).flip();		
			tbo = storeData(textureBuffer, 1, 2);	
		
		//Normal Buffer
		if(vertices[0].getNormal() != null) {	
			FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(vertices.length*3);
			float[] normalData = new float[vertices.length*3];
			for (int i = 0; i < vertices.length; i++) {
				normalData[i * 3] = vertices[i].getNormal().getX();
				normalData[i * 3 + 1] = vertices[i].getNormal().getY();
				normalData[i * 3 + 2] = vertices[i].getNormal().getZ();
			}		
			normalsBuffer.put(normalData).flip();
			nbo = storeData(normalsBuffer, 2, 3);
		}
		
		//ColorBuffer
		if(vertices[0].getColor() != null) {			
			FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
			float[] colorData = new float[vertices.length * 3];
			for (int i = 0; i < vertices.length; i++) {
				colorData[i * 3] = vertices[i].getColor().getX();
				colorData[i * 3 + 1] = vertices[i].getColor().getY();
				colorData[i * 3 + 2] = vertices[i].getColor().getZ();
			}
			colorBuffer.put(colorData).flip();
			cbo = storeData(colorBuffer, 3, 3);
		}
		
		//Indicies Buffer
		IntBuffer indiciesBuffer = MemoryUtil.memAllocInt(indices.length);
		indiciesBuffer.put(indices).flip();
		
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	private int storeData(FloatBuffer buffer, int index, int size) {
		int bufferID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, bufferID);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return bufferID;
	}
	
	public void destroy() {
		glDeleteBuffers(vbo);
		glDeleteBuffers(cbo);
		glDeleteBuffers(ibo);
		glDeleteBuffers(tbo);
		
		
		glDeleteVertexArrays(vao);
		
		if(material != null) material.destroy();
	}

	public Vertex[] getVertecies() {
		return vertices;
	}
	
	public Mesh CubeMesh(URL path) {		
		Mesh mesh2 = new Mesh(new Vertex[] {
				//Back face
				new Vertex(new Vector3(-0.5f,  0.5f, -0.5f), new Vector2(0.0f, 0.0f)),
				new Vertex(new Vector3(-0.5f, -0.5f, -0.5f), new Vector2(0.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f, -0.5f, -0.5f), new Vector2(1.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f,  0.5f, -0.5f), new Vector2(1.0f, 0.0f)),
				
				//Front face
				new Vertex(new Vector3(-0.5f,  0.5f,  0.5f), new Vector2(0.0f, 0.0f)),
				new Vertex(new Vector3(-0.5f, -0.5f,  0.5f), new Vector2(0.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f, -0.5f,  0.5f), new Vector2(1.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f,  0.5f,  0.5f), new Vector2(1.0f, 0.0f)),
				
				//Right face
				new Vertex(new Vector3( 0.5f,  0.5f, -0.5f), new Vector2(0.0f, 0.0f)),
				new Vertex(new Vector3( 0.5f, -0.5f, -0.5f), new Vector2(0.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f, -0.5f,  0.5f), new Vector2(1.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f,  0.5f,  0.5f), new Vector2(1.0f, 0.0f)),
				
				//Left face
				new Vertex(new Vector3(-0.5f,  0.5f, -0.5f), new Vector2(0.0f, 0.0f)),
				new Vertex(new Vector3(-0.5f, -0.5f, -0.5f), new Vector2(0.0f, 1.0f)),
				new Vertex(new Vector3(-0.5f, -0.5f,  0.5f), new Vector2(1.0f, 1.0f)),
				new Vertex(new Vector3(-0.5f,  0.5f,  0.5f), new Vector2(1.0f, 0.0f)),
				
				//Top face
				new Vertex(new Vector3(-0.5f,  0.5f,  0.5f), new Vector2(0.0f, 0.0f)),
				new Vertex(new Vector3(-0.5f,  0.5f, -0.5f), new Vector2(0.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f,  0.5f, -0.5f), new Vector2(1.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f,  0.5f,  0.5f), new Vector2(1.0f, 0.0f)),
				
				//Bottom face
				new Vertex(new Vector3(-0.5f, -0.5f,  0.5f), new Vector2(0.0f, 0.0f)),
				new Vertex(new Vector3(-0.5f, -0.5f, -0.5f), new Vector2(0.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f, -0.5f, -0.5f), new Vector2(1.0f, 1.0f)),
				new Vertex(new Vector3( 0.5f, -0.5f,  0.5f), new Vector2(1.0f, 0.0f)),
		}, new int[] {
				//Back face
				0, 1, 3,	
				3, 1, 2,	
				
				//Front face
				4, 5, 7,
				7, 5, 6,
				
				//Right face
				8, 9, 11,
				11, 9, 10,
				
				//Left face
				12, 13, 15,
				15, 13, 14,
				
				//Top face
				16, 17, 19,
				19, 17, 18,
				
				//Bottom face
				20, 21, 23,
				23, 21, 22
		}, new Material(path));
		return mesh2;
	}

	public void setVertecies(Vertex[] vertecies) {
		glDeleteBuffers(vbo);
		this.vertices = vertecies;
		FloatBuffer verteciesBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);		
		float[] positionData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			positionData[i * 3] = vertices[i].getPosition().getX();
			positionData[i * 3 +1] = vertices[i].getPosition().getY();
			positionData[i * 3 +2] = vertices[i].getPosition().getZ();
		}
		verteciesBuffer.put(positionData).flip();		
		vbo = storeData(verteciesBuffer, 0, 3);	
	}
	    
    public void sethasTransparency(boolean bool) {
    	material.sethasTransparency(bool);
    }
    public void setUseFakeLighting(boolean bool) {
    	material.setUseFakeLighting(bool);
    }
    public void setNumberofRows(int i) {
    	material.setNumberofRows(i);
    }
    

	public int[] getIndicies() {
		return indices;
	}

	public void setIndicies(int[] indicies) {
		this.indices = indicies;
	}

	public int getVAO() {
		return vao;
	}

	public int getVBO() {
		return vbo;
	}

	public int getIBO() {
		return ibo;
	}

	public int getCBO() {
		return cbo;
	}
	
	public int getTBO() {
		return tbo;
	}
	
	public Material getMaterial() {
		return material;
	}

	public int getNBO() {
		return nbo;
	}

	

}

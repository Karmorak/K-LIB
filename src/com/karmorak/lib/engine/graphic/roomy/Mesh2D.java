package com.karmorak.lib.engine.graphic.roomy;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import com.karmorak.lib.math.Vector2;

public class Mesh2D {
	
		
	
	private Vector2[] postions;
	
	private int vao, vbo, ibo, tbo;
	
	
	public Mesh2D(Vector2[] position, Material mat) {
		this.postions = position;
	}
	
	public static Mesh2D createQUAD() {
		Vector2 a = new Vector2(-1, 1);
		Vector2 b = new Vector2(-1, -1);
		Vector2 c = new Vector2(1, 1);
		Vector2 d = new Vector2(1, -1);
		
		return new Mesh2D(new Vector2[] {a,b,c,d}, null);	
	}
	
	public void create() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);

				
		//PositionBuffer
		FloatBuffer verteciesBuffer = MemoryUtil.memAllocFloat(postions.length * 2);		
		float[] positionData = new float[postions.length * 2];
		for (int i = 0; i < postions.length; i++) {
			positionData[i * 2] = postions[i].getX();
			positionData[i * 2 +1] = postions[i].getY();
		}
		verteciesBuffer.put(positionData).flip();		
		vbo = storeData(verteciesBuffer, 0, 2);	
		
//		//Texture Buffer
//			FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(8);		
//			float[] textureData = new float[] {-1,1, 1,1, -1,-1, 1,-1};
////			for (int i = 0; i < postions.length; i++) {
////				textureData[i * 2] = postions[i].getX();
////				textureData[i * 2 +1] = postions[i].getY();
////			}
//			textureBuffer.put(textureData).flip();		
//			tbo = storeData(textureBuffer, 1, 2);	
//				
//		//Indicies Buffer
//		IntBuffer indiciesBuffer = MemoryUtil.memAllocInt(4);
//		int[] indices = new int[] {0,1,2,3};
//		indiciesBuffer.put(indices).flip();
//		
//		ibo = glGenBuffers();
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL_STATIC_DRAW);
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	private int storeData(FloatBuffer buffer, int index, int size) {
		int bufferID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, bufferID);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return bufferID;
	}
	
	public void destroy() {
		glDeleteBuffers(vbo);
//		glDeleteBuffers(ibo);
//		glDeleteBuffers(tbo);
		
		
		glDeleteVertexArrays(vao);
		
//		if(material != null) material.destroy();
	}
	
	public boolean isCreated()  {
		return vao != 0; 
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

	public int getTBO() {
		return tbo;
	}
	
//	public Material getMaterial() {
//		return material;
//	}	


}

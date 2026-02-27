package com.karmorak.lib.engine.graphic.flat;


import static org.lwjgl.opengl.GL46.*;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;
import com.karmorak.lib.math.Vector2;

public class TextureMesh {
		
	
	
	private Vector2[] postions;
	private Vector2[] textureCoordinates;
	
	private int vao, vbo, tbo;
		
	
	public TextureMesh() {
		this.postions = new Vector2[]{new Vector2(-1, 1), new Vector2(-1, -1), new Vector2(1, 1), new Vector2(1, -1)};
		this.textureCoordinates = new Vector2[]{new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 0), new Vector2(1, 1)};
	}
	
	public TextureMesh(Vector2[] position) {
		this.postions = position;
		this.textureCoordinates = new Vector2[]{new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 0), new Vector2(1, 1)};
		
	}
	
	public TextureMesh(Vector2[] position, Vector2[] texCoords) {
		this.postions = position;
		this.textureCoordinates = texCoords;
	}
		
	public void setTexCoords(Vector2[] texCoords) {
		textureCoordinates = texCoords;
		if(tbo != 0) { //check whether the old cords were already set if yes the will be renewed otherwise they will be set in the next create() call;
			//Texture Buffer
			FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(8);		
			float[] textureData = new float[8];
			for (int i = 0; i < textureCoordinates.length; i++) {
				textureData[i * 2] = textureCoordinates[i].getX();
				textureData[i * 2 +1] = textureCoordinates[i].getY();
			}
			textureBuffer.put(textureData).flip();		
			tbo = storeData(textureBuffer, 1, 2);	
		}
	}
		
		
	public void create() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
					
		//PositionBuffer
		// Ersetze memAllocFloat durch:
		FloatBuffer verteciesBuffer = BufferUtils.createFloatBuffer(postions.length * 2);
		for (Vector2 pos : postions) {
			verteciesBuffer.put(pos.getX());
			verteciesBuffer.put(pos.getY());
		}
		verteciesBuffer.flip();
		vbo = storeData(verteciesBuffer, 0, 2);	
		
		//Texture Buffer
		FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(8);
		float[] textureData = new float[8];
		for (int i = 0; i < textureCoordinates.length; i++) {
			textureData[i * 2] = textureCoordinates[i].getX();
			textureData[i * 2 +1] = textureCoordinates[i].getY();
		}
		textureBuffer.put(textureData).flip();		
		tbo = storeData(textureBuffer, 1, 2);
		MemoryUtil.memFree(textureBuffer);
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
		glDeleteBuffers(tbo);			
		glDeleteVertexArrays(vao);
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
		public int getTBO() {
		return tbo;
	}
}

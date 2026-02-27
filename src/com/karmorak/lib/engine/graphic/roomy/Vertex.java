package com.karmorak.lib.engine.graphic.roomy;

import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;

public class Vertex {
	

	private Vertex duplicate_Vertex;
	
	private Vector3 position, color;
	private Vector2 textureCoord;
	private Vector3 normal;
	
	private int Index = -1;


	public Vertex(int index, Vector3 pos) {
		this.Index = index;
		this.position = pos;
	}
	
	public Vertex(int index, Vector3 position, Vector2 textureCoord, Vector3 normal) {
		this.Index = index;
		this.position = position;
		this.textureCoord = textureCoord;
		this.normal = normal;
	}
	
	public Vertex(Vector3 position, Vector2 textureCoord) {
		this.position = position;
		this.textureCoord = textureCoord;
	}
	
	public Vertex(Vector3 position, Vector2 textureCoord, Vector3 normal) {
		this.position = position;
		this.textureCoord = textureCoord;
		this.normal = normal;
	}
	

	
	
//	public Vertex(Vector3 position, Vector3 color, Vector2 textureCoord) {
//		this.position = position;
//		this.color = color;
//		this.textureCoord = textureCoord;
//	}
//	
//	public Vertex(Vector3 position, Vector3 color, Vector2 textureCoord, Vector3 normal) {
//		this.position = position;
//		this.color = color;
//		this.textureCoord = textureCoord;
//		this.normal = normal;
//	}
//	
	
	public boolean isSimilar(Vector2 textureCoord, Vector3 normal) {
		return this.textureCoord.getX() == textureCoord.getX() && this.textureCoord.getY() == textureCoord.getY() && this.normal.getX() == normal.getX() && this.normal.getY() == normal.getY() && this.normal.getZ() == normal.getZ(); 
	}

	
	public static boolean isSimilar(Vertex one, Vertex two) {
		return one.textureCoord.getX() == two.textureCoord.getX() && one.textureCoord.getY() == two.textureCoord.getY() && one.normal.getX() == two.normal.getX() && one.normal.getY() == two.normal.getY() && one.normal.getZ() == two.normal.getZ(); 
	}

	public int getIndex() {
		return Index;
	}
	
	public float getLength() {
		return Vector3.length(position);
	}

	public void setIndex(int index) {
		Index = index;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setDublicate(Vertex v) {
		duplicate_Vertex = v;
	}
	
	public Vertex getDublicate() {
		return duplicate_Vertex;		
	}

	public Vector3 getColor() {
		return color;
	}

	public Vector2 getTextureCoord() {
		return textureCoord;
	}
	
	public Vector3 getNormal() {
		return normal;
	}
	
	public void setTextureCoord(Vector2 textureCoord) {
		this.textureCoord = textureCoord;
	}


	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}


	public boolean isSet() {
		return textureCoord != null && normal != null;
	}

}

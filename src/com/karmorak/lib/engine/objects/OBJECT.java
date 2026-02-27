package com.karmorak.lib.engine.objects;

import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;

public abstract class OBJECT {
	
	Vector3 position, rotation, scale;
	Mesh mesh;
	Light light;
	
	int textureIndex = 0;
	

	public OBJECT(Mesh mesh, Vector3 position, Vector3 rotation, Vector3 scale, int index, Light light) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.light = light;
		this.mesh = mesh;
		this.textureIndex = index;
	}
	
	public float getTextureXOffset() {
		return mesh.getMaterial().getTextureXOffset(textureIndex);
	}
	
	public float getTextureYOffset() {
		return mesh.getMaterial().getTextureYOffset(textureIndex);
	}
	
	public Vector2 getTextureOffset() {
		return new Vector2(getTextureXOffset(), getTextureYOffset());
	}
	
	public Vector3 getPosition() {
		return position;
	}

	public Vector3 getRotation() {
		return rotation;
	}

	public Vector3 getScale() {
		return scale;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}

	public void setScale(Vector3 scale) {
		this.scale = scale;
	}

	public Light getLight() {
		return light;
	}
	
	

//	public void setMesh(Mesh mesh) {
//		this.mesh = mesh;
//	}
	
}

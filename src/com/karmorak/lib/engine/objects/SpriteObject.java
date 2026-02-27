package com.karmorak.lib.engine.objects;

import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;


public class SpriteObject extends OBJECT {

	public SpriteObject(Vector2 position, float rotation, Vector2 scale, Mesh mesh) {		
		super(mesh, new Vector3(position.getX(), position.getY(), 0f), new Vector3(0, 0, rotation), new Vector3(scale.getX(), scale.getY(), 0), 0, null);
		mesh.create();
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

	@Override
	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}

	public void setScale(Vector3 scale) {
		this.scale = scale;
	}
	

}

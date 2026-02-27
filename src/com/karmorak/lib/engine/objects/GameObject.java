package com.karmorak.lib.engine.objects;

import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.math.Vector3;

public class GameObject extends OBJECT {

	public GameObject(Vector3 position, Vector3 rotation, Vector3 scale, Mesh mesh) {
		super(mesh, position, rotation, scale, 0, null);
	}
	
	public GameObject(Mesh mesh, Vector3 position, Vector3 rotation, Vector3 scale, int textureIndex) {
		super(mesh, position, rotation, scale, textureIndex, null);
	}
	
	public GameObject(Vector3 position, Vector3 rotation, Vector3 scale, Light l, Mesh mesh) {
		super(mesh, position, rotation, scale, 0, l);
	}
	



//	private double temp;
//	public void update() {
//		temp += 0.02f;
//		position.setX((float) Math.sin(temp));
//		rotation.set((float) Math.sin(temp) * 360, (float) Math.sin(temp) * 360, (float) Math.sin(temp) * 360);
//		scale.set((float) Math.sin(temp), (float) Math.sin(temp), (float) Math.sin(temp) );
//	}
	
	
	
	
	

}

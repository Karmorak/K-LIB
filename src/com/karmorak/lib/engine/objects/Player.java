package com.karmorak.lib.engine.objects;

import static org.lwjgl.glfw.GLFW.*;

import com.karmorak.lib.Input;
import com.karmorak.lib.Window;
import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.engine.terrain.Terrain;
import com.karmorak.lib.math.Vector3;

public class Player extends OBJECT {

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardSpeed = 0;
	
	private boolean isInAir = false;
	
	
	public Player(Mesh mesh, Vector3 position) {
		super(mesh, position, new Vector3(0, 0, 0), new Vector3(1, 1, 1), 0, null);
	}
	
	public Player(Mesh mesh, Vector3 position, Vector3 rotation, Vector3 scale) {
		super(mesh, position, rotation, scale, 0, null);
	}
	
	public Player(Mesh mesh, Vector3 position, int textureIndex) {
		super(mesh, position, new Vector3(0, 0, 0), new Vector3(1, 1, 1), textureIndex, null);
	}
	
	public Player(Mesh mesh, Vector3 position, Vector3 rotation, Vector3 scale, int textureIndex) {
		super(mesh, position, rotation, scale, textureIndex, null);
	}
	
	public Player(Mesh mesh, Vector3 position, Vector3 rotation, Vector3 scale, Light light) {
		super(mesh, position, rotation, scale, 0, light);
	}
	
	public void move(Terrain t) {
		checkInputs();
		

		float delta = (float) Window.getDelta();
		
		rotation = new Vector3(0, rotation.getY() + currentTurnSpeed * delta, 0);		
		
		float distance = currentSpeed  * delta;
		
		float dx = (float) (distance * Math.sin(Math.toRadians(-rotation.getY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(rotation.getY())));		
		
		upwardSpeed += GRAVITY * delta;
		
		position = new Vector3(position.getX() + dx, position.getY() + upwardSpeed * delta, position.getZ() + dz);
		
		
		float terrainHeight = t.getHeights(position.getX(), position.getZ());
		if(position.getY() < terrainHeight) {
			upwardSpeed = 0;
			position.setY(terrainHeight);
			isInAir = false;
		}
	}
	
	private void jump() {
		upwardSpeed = JUMP_POWER;
		isInAir = true;
	}
	

	private void checkInputs() {
		
		if(Input.keys[GLFW_KEY_W])  {
			currentSpeed = RUN_SPEED;
		} else if(Input.keys[GLFW_KEY_S]) {
			currentSpeed = -RUN_SPEED;
		} else {
			currentSpeed = 0;
		}
		
		
		if(Input.keys[GLFW_KEY_D]) {
			currentTurnSpeed = TURN_SPEED;
		} else if(Input.keys[GLFW_KEY_A]) {
			currentTurnSpeed = -TURN_SPEED;
		} else {
			currentTurnSpeed = 0;
		}
		
		if(Input.keys[GLFW_KEY_SPACE] && !isInAir) jump();

//		
//		if(Input.keys[GLFW_KEY_SPACE]) position = Vector3.add(position, new Vector3(0, moveSpeed, 0));
//		if(Input.keys[GLFW_KEY_LEFT_SHIFT]) position = Vector3.add(position, new Vector3(0, -moveSpeed, 0));
		
		
	}
	

}

package com.karmorak.lib.engine.objects;


import com.karmorak.lib.Input;
import com.karmorak.lib.math.Vector3;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {
	
	private Vector3 position, rotation;
	
	private float moveSpeed = 0.05f, mouseSensitivity = 0.15f, distance = 20.0f, horizontal_angle = 45f, vertical_angle = 0f;	
	@SuppressWarnings("unused")
	private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;

	public Camera(Vector3 position, Vector3 rotation) {		
		this.position = position;
		this.rotation = rotation;
		
		oldMouseX = Input.mouse.getX();
		oldMouseY = Input.mouse.getY();
	}
	
	
	public void setOldMouseX(double oldMouseX) {
		this.oldMouseX = oldMouseX;
	}

	public void setOldMouseY(double oldMouseY) {
		this.oldMouseY = oldMouseY;
	}


	public void update() {
		float x = (float) Math.sin(Math.toRadians(rotation.getY())) * moveSpeed;
		float z = (float) Math.cos(Math.toRadians(rotation.getY()))* moveSpeed;
		
		if(Input.keys[GLFW_KEY_A]) position = Vector3.add(position, new Vector3(-z, 0, x));
		if(Input.keys[GLFW_KEY_D])  position = Vector3.add(position, new Vector3( z, 0, -x));;
		if(Input.keys[GLFW_KEY_W])  position = Vector3.add(position, new Vector3(-x , 0, -z));
		if(Input.keys[GLFW_KEY_S]) position = Vector3.add(position, new Vector3(x , 0, z));
		if(Input.keys[GLFW_KEY_SPACE]) position = Vector3.add(position, new Vector3(0, moveSpeed, 0));
		if(Input.keys[GLFW_KEY_LEFT_SHIFT]) position = Vector3.add(position, new Vector3(0, -moveSpeed, 0));
		
		if(Input.mouseLocked) {
			if(Input.mouseMovedX) {
				newMouseX = Input.mouse.getX();		
				float dx = (float) (+newMouseX - oldMouseX);	
				
				rotation.setY(rotation.getY() + (-dx * mouseSensitivity));
				
				oldMouseX = newMouseX;
				Input.mouseMovedX = false;
			}
			if (Input.mouseMovedY) {
				newMouseY = Input.mouse.getY();
				float dy = (float) (+newMouseY - oldMouseY);
				
				rotation.setX(rotation.getX() + (-dy * mouseSensitivity));		
				
				oldMouseY = newMouseY;
				Input.mouseMovedY = false;
			}
		}

	}
	
	public void update(OBJECT object) {
		
		
		newMouseX = Input.mouse.getX();		
		float dx = (float) (newMouseX - oldMouseX);	
		
		newMouseY = Input.mouse.getY();
		float dy = (float) (newMouseY - oldMouseY);
		
		if(Input.buttons[GLFW_MOUSE_BUTTON_LEFT]) {
			vertical_angle -= dy * mouseSensitivity / 4f;
			horizontal_angle += dx * mouseSensitivity / 4f;
		} 
		if(Input.buttons[GLFW_MOUSE_BUTTON_RIGHT]) {
			if (distance > 0 ) {
				distance += dy * mouseSensitivity / 16;
			} else {
				distance = 0.2f;
			}
			
		}
		
		float horizontal_dist = (float) (distance * Math.cos(Math.toRadians(-vertical_angle))); 
		float vertical_dist = (float) (distance * Math.sin(Math.toRadians(vertical_angle))); 
		
		float xOffset = (float) (horizontal_dist * Math.sin(Math.toRadians(-horizontal_angle))); 
		float zOffset = (float) (horizontal_dist * Math.cos(Math.toRadians(-horizontal_angle))); 
		
		position.set(object.getPosition().getX()+ xOffset, object.getPosition().getY() - vertical_dist, object.getPosition().getZ() + zOffset);
		
		rotation.set(vertical_angle, -horizontal_angle, rotation.getZ());
		
		oldMouseX = newMouseX;
		Input.mouseMovedX = false;
		
		oldMouseY = newMouseY;
		Input.mouseMovedY = false;
	}
	
	public void update(Vector3 o_position) {	
		
		newMouseX = Input.mouse.getX();		
		float dx = (float) (newMouseX - oldMouseX);	
		
		newMouseY = Input.mouse.getY();
		float dy = (float) (newMouseY - oldMouseY);
		
		if(Input.buttons[GLFW_MOUSE_BUTTON_LEFT]) {
			vertical_angle -= dy * mouseSensitivity / 4f;
			horizontal_angle += dx * mouseSensitivity / 4f;
		} 
		if(Input.buttons[GLFW_MOUSE_BUTTON_RIGHT]) {
			if (distance > 0 ) {
				distance += dy * mouseSensitivity / 16;
			} else {
				distance = 0.2f;
			}
			
		}
		
		float horizontal_dist = (float) (distance * Math.cos(Math.toRadians(-vertical_angle))); 
		float vertical_dist = (float) (distance * Math.sin(Math.toRadians(vertical_angle))); 
		
		float xOffset = (float) (horizontal_dist * Math.sin(Math.toRadians(-horizontal_angle))); 
		float zOffset = (float) (horizontal_dist * Math.cos(Math.toRadians(-horizontal_angle))); 
		
		position.set(o_position.getX()+ xOffset, o_position.getY() - vertical_dist, o_position.getZ() + zOffset);
		
		rotation.set(vertical_angle, -horizontal_angle, rotation.getZ());
		
		oldMouseX = newMouseX;
		Input.mouseMovedX = false;
		
		oldMouseY = newMouseY;
		Input.mouseMovedY = false;
	}
	
	public void setSpeed(float speed) {
		this.moveSpeed = speed;
	}
	
//	public void setSensivity(float sens) {
//		this.mouseSensitivity = sens;
//	}

	public Vector3 getPosition() {
		return position;
	}

	public Vector3 getRotation() {
		return rotation;
	}
	
	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}
	
}

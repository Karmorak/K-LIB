package com.karmorak.lib.math;

import com.karmorak.lib.math.vector.Vec2;

import java.util.Objects;

public class Vector2i implements Vec2 {
	
	private int x, y;
		
	public Vector2i() {
		this.x = 0;
		this.y = 0;
	}
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(float x, float y) {
		this.x = (int) x;
		this.y = (int) y;
	}
	
	public Vector2i(Vector2 vec) {
		this.x = (int) vec.getX();
		this.y = (int) vec.getY();
	}
	

	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(float x, float y) {
		this.x = (int) x;
		this.y = (int) y;
	}

	@Override
	public void set(Vector2 vec) {
		this.x = (int) vec.getX();
		this.y = (int) vec.getY();
	}

	public void setX(float x) {
		this.x = (int) x;
	}

	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	public void setY(float y) {
		this.y = (int) y;
	}
	
	public void setPosition(Vector2 vec) {
		set(vec);
	}
	
	public void setPosition(int x, int y) {
		set(x, y);
	}
	
	public int getWidth() {
		return x;
	}
	
	public int getHeight() {
		return y;
	}
		
	public void setWidth(int x) {
		this.x = x;
	}
	
	public void setHeight(int y) {
		this.y = y;
	}

	public static Vector2 add(Vector2 vector1, Vector2 vector2) {
		return new Vector2(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
	}
	
	public Vector2 subtract(Vector2 vector) {
		return new Vector2(getX() - vector.getX(), getY() - vector.getY());
	}
	
	public static Vector2 subtract(Vector2 vector1, Vector2 vector2) {
		return new Vector2(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
	}
	
	public static Vector2 multiply(Vector2 vector1, Vector2 vector2) {
		return new Vector2(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY());
	}
	
	public static Vector2 divide(Vector2 vector1, Vector2 vector2) {
		return new Vector2(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY());
	}
	
	public static float length(Vector2 vector) {
		return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
	}
	
	public static Vector2 normalize(Vector2 vector) {
//		float len = Vector2.length(vector);
//		return Vector2.divide(vector, new Vector2(len, len));
		return null;
	}
	
	public static float dot(Vector2 vector1, Vector2 vector2) {
		return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
	}
	@Override
	public int hashCode() {
			return Objects.hash(x, y);
		}
	
	@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vector2i other = (Vector2i) obj;
			return x == other.x && y == other.y;
		}
	@Override
	public String toString() {
		return x +  ":" + y;
	}
}

//v4
package com.karmorak.lib.math;

import com.karmorak.lib.math.vector.Vec2;

import java.util.Objects;

public class Vector2 implements Comparable<Vector2>, Vec2 {
	
	private float x, y;
	public boolean compareX = true;

	public Vector2() { this(0, 0); }

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.compareX = vec.compareX;
	}
	
	public Vector2(Vector2i vec) {
		this.x = vec.getX();
		this.y = vec.getY();
	}

	public Vector2 get(){
		return this;
	}

	@Override
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public void set(Vector2 vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	public void setPosition(Vector2 vec) {
		set(vec);
	}
	
	public void setPosition(float x, float y) {
		set(x, y);
	}

	public float getWidth() {
		return x;
	}
	
	public float getHeight() {
		return y;
	}
		
	public void setWidth(float x) {
		this.x = x;
	}
	
	public void setHeight(float y) {
		this.y = y;
	}
	
	public float getHorizontal() {
		return x;
	}
	
	public float getVertical() {
		return y;
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

	public Vector2 scaleLocal(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}

	public float lengthSquared() {
		return x * x + y * y;
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public Vector2 normalizeLocal() {
		float len = length();
		if (len != 0 && len != 1) {
			scaleLocal(1f / len);
		}
		return this;
	}
	
	public static float dot(Vector2 vector1, Vector2 vector2) {
		return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
	}


	@Override
	public int compareTo(Vector2 o) {
		return compareX ? Float.compare(this.x, o.x) : Float.compare(this.y, o.y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Vector2 other)) return false;
        // Nutzt Float.compare um NaN und -0.0 handling korrekt zu machen
		return Float.compare(x, other.x) == 0 && Float.compare(y, other.y) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return String.format("[%f, %f]", x, y);
	}

	


}

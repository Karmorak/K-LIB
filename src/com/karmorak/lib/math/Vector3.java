package com.karmorak.lib.math;

import com.karmorak.lib.math.vector.Vector;

public class Vector3 implements Vector {
	
	private float x, y, z;
	public static Vector3 EMPTY = new Vector3(0,0,0);
	
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector3 add(Vector3 vector1, Vector3 vector2) {
		return new Vector3(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY(), vector1.getZ() + vector2.getZ());
	}
	
	public static Vector3 subtract(Vector3 vector1, Vector3 vector2) {
		return new Vector3(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
	}
	
	public static Vector3 multiply(Vector3 vector1, Vector3 vector2) {
		return new Vector3(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY(), vector1.getZ() * vector2.getZ());
	}
	
	public static Vector3 divide(Vector3 vector1, Vector3 vector2) {
		return new Vector3(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY(), vector1.getZ() / vector2.getZ());
	}
	
	public static float length(Vector3 vector) {
		return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
	}
	
	public static Vector3 normalize(Vector3 vector) {
		float len = Vector3.length(vector);
		return Vector3.divide(vector, new Vector3(len, len, len));
	}
	
	public static float dot(Vector3 vector1, Vector3 vector2) {
		return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3 other = (Vector3) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return x + ":" + y + ":" + z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	

}

package com.karmorak.lib.math;

import java.util.Arrays;

public class Matrix4 {
	
	public static final int SIZE = 4;
	private float[] elements = new float[SIZE * SIZE];
	
	public Matrix4() {
		elements = new float[SIZE * SIZE];
	}
	
	public static Matrix4 identify() {
		Matrix4 result = new Matrix4();
		
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				result.set(i, j, 0);
			}
		}
		
		result.set(0, 0, 1);
		result.set(1, 1, 1);
		result.set(2, 2, 1);
		result.set(3, 3, 1);
		
		return result;
	}
	
	
	/*
	 * 1 0 0 x
	 * 0 1 0 y
	 * 0 0 1 z
	 * 0 0 0 1
	 */	
	public static Matrix4 translate(Vector3 translate) {
		Matrix4 result = identify();
		
		result.set(3, 0, translate.getX());
		result.set(3, 1, translate.getY());
		result.set(3, 2, translate.getZ());		
		
		return result;		
	}
	
	public static Matrix4 flipY() {
		Matrix4 result = identify();
		
		result.set(0, 0, -1);	
		
		return result;		
	}
	
	
	/**https://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle */
	public static Matrix4 rotate(float angle, Vector3 axis) {
		Matrix4 result = Matrix4.identify();
		
		float x = axis.getX();
		float y = axis.getY();
		float z = axis.getZ();
		
		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));
		float C = 1 - cos;
		
		result.set(0, 0, cos + x * x* C);
		result.set(0, 1, x * y * C - z * sin);
		result.set(0, 2, x * z * C + y * sin);
		result.set(1, 0, y * x * C + z * sin);
		result.set(1, 1, cos + y * y * C);
		result.set(1, 2, y * z * C - x * sin);
		result.set(2, 0, z * x * C - y * sin);
		result.set(2, 1, z * y * C + x * sin);
		result.set(2, 2, cos + z * z * C);
		
		return result;
	}
	
	/** https://en.wikipedia.org/wiki/Transformation_matrix#/media/File:2D_affine_transformation_matrix.svg  */
	/**https://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle */
	public static Matrix4 rotate(Vector3 rotation) {
		
		Matrix4 rotXMatrix = Matrix4.rotate(rotation.getX(), new Vector3(1, 0, 0));
		Matrix4 rotYMatrix = Matrix4.rotate(rotation.getY(), new Vector3(0, 1, 0));
		Matrix4 rotZMatrix = Matrix4.rotate(rotation.getZ(), new Vector3(0, 0, 1));
		Matrix4 rotationMatrix = Matrix4.multiply(rotXMatrix, Matrix4.multiply(rotYMatrix, rotZMatrix));
		
		return rotationMatrix;
	}
	
	
	public static Matrix4 scale(Vector3 scalar) {
		/* x000
		 * 0y00
		 * 00z0
		 * 0001
		 */			
		Matrix4 result = identify();
		
		result.set(0, 0, scalar.getX());
		result.set(1, 1, scalar.getY());
		result.set(2, 2, scalar.getZ());
		
		return result;		
	}
	
	
	
	public static Matrix4 transform(Vector3 position, Vector3 rotation, Vector3 scale) {
		Matrix4 result = identify();
		
		
		Matrix4 translation = Matrix4.translate(position);
		Matrix4 rotXMatrix = Matrix4.rotate(rotation.getX(), new Vector3(1, 0, 0));
		Matrix4 rotYMatrix = Matrix4.rotate(rotation.getY(), new Vector3(0, 1, 0));
		Matrix4 rotZMatrix = Matrix4.rotate(rotation.getZ(), new Vector3(0, 0, 1));
		Matrix4 scaleMatrix = Matrix4.scale(scale);
		
		Matrix4 rotationMatrix = Matrix4.multiply(rotXMatrix, Matrix4.multiply(rotYMatrix, rotZMatrix));
		
		result = Matrix4.multiply(Matrix4.multiply(rotationMatrix, scaleMatrix), translation);		
		
		return result;
	}
	
	public static Matrix4 createTransformationMatrix(Vector2 position, Vector2 scale) {
		Matrix4 translation = Matrix4.translate(new Vector3(position.getX(), position.getY(),position.getY()));
		Matrix4 scaleMatrix = Matrix4.scale(new Vector3(scale.getX(), scale.getY(), 1f));
		
		return multiply(translation, scaleMatrix);
	}
	
	public static Matrix4 createTransformationMatrix2D(Vector2 position, float rx, float ry,float rz, Vector2 scale) {
		Matrix4 matrix = identify();
		
		Matrix4 translation = Matrix4.translate(new Vector3(position.getX(), position.getY(), 0));
		Matrix4.multiply(matrix, translation);		
				
		Matrix4 rotXMatrix = Matrix4.rotate(rx, new Vector3(1, 0, 0));
		Matrix4.multiply(matrix, rotXMatrix);
		Matrix4 rotYMatrix = Matrix4.rotate(ry, new Vector3(0, 1, 0));
		Matrix4.multiply(matrix, rotYMatrix);
		Matrix4 rotZMatrix = Matrix4.rotate(rz, new Vector3(0, 0, 1));
		Matrix4.multiply(matrix, rotZMatrix);
		
		Matrix4 scaleMatrix = Matrix4.scale(new Vector3(scale.getX(), scale.getY(), 0));
		Matrix4.multiply(matrix, scaleMatrix);
		
		return matrix;		
	}
	
	
	public static Matrix4 projection(float fov, float aspect, float near, float far) {
		Matrix4 result = identify();
		
		float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
		
		result.set(0, 0, 1f / (aspect * tanFOV));
		result.set(1, 1, 1f / tanFOV);
		result.set(2, 2, -((far + near) / (far - near)));
		result.set(2, 3, -1f);
		result.set(3, 2, -((2 * far * near) / (far - near)));
		result.set(3, 3, 0f);

		
		return result;	
	}
	
	public static Matrix4 projection(float aspect, float near, float far) {
		Matrix4 result = identify();
		
//		float tanFOV = (float) Math.tan(Math.toRadians(90 / 2));
		
		result.set(0, 0, 1f / (aspect));
		result.set(1, 1, 1f);
		result.set(2, 2, -((far + near) / (far - near)));
		result.set(2, 3, -1f);
		result.set(3, 2, -((2 * far * near) / (far - near)));
		result.set(3, 3, 0f);

		
		return result;	
	}
	
	public static Matrix4 view(Vector3 position, Vector3 rotation) {
		Matrix4 result = identify();
		
		
		Vector3 negative = new Vector3(-position.getX(), -position.getY(), -position.getZ());
		Matrix4 translation = Matrix4.translate(negative);
		Matrix4 rotXMatrix = Matrix4.rotate(rotation.getX(), new Vector3(1, 0, 0));
		Matrix4 rotYMatrix = Matrix4.rotate(rotation.getY(), new Vector3(0, 1, 0));
		Matrix4 rotZMatrix = Matrix4.rotate(rotation.getZ(), new Vector3(0, 0, 1));
		
		Matrix4 rotationMatrix = Matrix4.multiply(rotZMatrix, Matrix4.multiply(rotYMatrix, rotXMatrix));
		
		result = Matrix4.multiply(translation, rotationMatrix);		
		
		return result;	
	}
	
	public static Matrix4 multiply(Matrix4 matrix, Matrix4 other) {
		Matrix4 result = identify();
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				result.set(i, j, matrix.get(i, 0) * other.get(0, j) + 
									  matrix.get(i, 1) * other.get(1, j) + 
									  matrix.get(i, 2) * other.get(2, j) + 
									  matrix.get(i, 3) * other.get(3, j));
			}
		}		
		return result;	
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
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
		Matrix4 other = (Matrix4) obj;
		if (!Arrays.equals(elements, other.elements))
			return false;
		return true;
	}
	
	public float get(int x, int y) {
		return elements[y * SIZE + x];
	}

	public void set(int x, int y,float value) {
		elements[y * SIZE + x] = value;
	}
	
	public float[] getAll() {
		return elements;
	}
}

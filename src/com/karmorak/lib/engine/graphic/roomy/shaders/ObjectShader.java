package com.karmorak.lib.engine.graphic.roomy.shaders;

import static org.lwjgl.opengl.GL46.*;

import java.net.URL;

import com.karmorak.lib.engine.graphic.roomy.Material;
import com.karmorak.lib.engine.objects.Camera;
import com.karmorak.lib.engine.objects.Light;
import com.karmorak.lib.engine.objects.OBJECT;
import com.karmorak.lib.math.Matrix4;
import com.karmorak.lib.math.Vector3;

public class ObjectShader extends ShaderProgramm {
		
	private static final String VERTEX_FILE = "/shaders/shader_src/mainVertex.glsl";
	private static final String FRAGMENT_FILE =  "/shaders/shader_src/mainFragment.glsl";
	
	public ObjectShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public ObjectShader(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}
	
	public ObjectShader(URL vertexPath, URL fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	@Override
	public void create() {
		programID = glCreateProgram();
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		
		glShaderSource(vertexID, vertexFile);
		glCompileShader(vertexID);
		
		if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Vertex Shader: " + glGetShaderInfoLog(vertexID));
			return;
		}
		
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(fragmentID, fragmentFile);
		glCompileShader(fragmentID);
		
		if(glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Fragment Shader: " + glGetShaderInfoLog(fragmentID));
			return;
		}	
	
		
		glAttachShader(programID, vertexID);
		glAttachShader(programID, fragmentID);
		
		glLinkProgram(programID);
		if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Programm Linking:" + glGetProgramInfoLog(programID));
			return;
		}
		
		glValidateProgram(programID);
		if(glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
			System.err.println("Programm Validating:" + glGetProgramInfoLog(programID));
			return;
		}
		
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);		
		created = true;
	}

	

	
	public void loadViewMatrix(Matrix4 matrix) {
		this.setUniform(VIEW_MATRIX, matrix);
	}
	
	public void loadTransformationMatrix(Matrix4 matrix) {
		this.setUniform(MODEL_MATRIX, matrix);
	}
	
	public void loadTransformationMatrix(Vector3 position, Vector3 rotation, Vector3 scale) {
		this.setUniform(MODEL_MATRIX, Matrix4.transform(position, rotation, scale));
	}
	
	public void loadTransformationMatrix(Vector3 position, Vector3 rotation, Vector3 scale, boolean advanced) {
		if (advanced) {
			this.setUniform(TRANSLATION_MATRIX, Matrix4.translate(position));
			this.setUniform(ROTATION_MATRIX+ "x", Matrix4.rotate(rotation.getX(), new Vector3(1, 0, 0)));
			this.setUniform(ROTATION_MATRIX+ "y", Matrix4.rotate(rotation.getY(), new Vector3(0, 1, 0)));
			this.setUniform(ROTATION_MATRIX+ "z", Matrix4.rotate(rotation.getZ(), new Vector3(0, 0, 1)));
			this.setUniform(SCALE_MATRIX, Matrix4.scale(scale));
		} else this.setUniform(MODEL_MATRIX, Matrix4.transform(position, rotation, scale));

	}
	
	public void loadProjectionMatrix(Matrix4 matrix) {
		this.setUniform(PROJECTION_MATRIX, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		this.setUniform(VIEW_MATRIX, Matrix4.view(camera.getPosition(), camera.getRotation()));
	}
	
	public void loadViewMatrix(Vector3 position, Vector3 rotation) {
		this.setUniform(VIEW_MATRIX, Matrix4.view(position, rotation));
	}
	
	public void loadLight(Light light) {
		this.setUniform(LIGHT_POSITION, light.getPosition());
		this.setUniform(LIGHT_COLOUR, light.getColour());
		this.setUniform(MINIMUMLIGHT, light.getMinLight());
	}
	
	public void loadLight(Vector3 position, Vector3 colour, float minlight) {
		this.setUniform(LIGHT_POSITION, position);
		this.setUniform(LIGHT_COLOUR, colour);
		this.setUniform(MINIMUMLIGHT, minlight);
	}
	
	public void loadShine(float damper, float reflectivity) {
		this.setUniform(SHINE_DAMPER, damper);
		this.setUniform(REFLECTIVITY, reflectivity);
	}
	
	public void loadFakeLighting(boolean isFake) {
		this.setUniform(USE_FAKELIGHTING, isFake);
	}
	
	public void loadTextureAtlas(Material mat) {
		setUniform(NUMBER_OF_ROWS, mat.getAtlasRows());		
	}
	
	public void loadOffset(OBJECT mat) {
		setUniform(OFFSET, mat.getTextureOffset());
	}
	
	
	public void setMinLight(float minlight) {
		setUniform(MINIMUMLIGHT, minlight);
	}
	

	

	
}

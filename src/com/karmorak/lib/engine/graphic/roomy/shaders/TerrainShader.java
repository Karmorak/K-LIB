package com.karmorak.lib.engine.graphic.roomy.shaders;

import static org.lwjgl.opengl.GL46.*;
import com.karmorak.lib.engine.objects.Camera;
import com.karmorak.lib.engine.objects.Light;
import com.karmorak.lib.math.Matrix4;
import com.karmorak.lib.math.Vector3;
public class TerrainShader extends ShaderProgramm {
		
	private static final String VERTEX_FILE = "/shaders/shader_src/terrainVertex.glsl";
	private static final String FRAGMENT_FILE = "/shaders/shader_src/terrainFragment.glsl";
	
	
	public static final String BACKGROUND_TEXTURE = "backgroundTex",
										  RED_TEXTURE = "rTex",
										  GREEN_TEXTURE = "gTex",
										  BLUE_TEXTURE = "bTex",
										  BLEND_TEXTURE = "blendMap";
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public TerrainShader(String vertexPath, String fragmentPath) {
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

	
	public void connectTextureUnits() {
		this.setUniform(BACKGROUND_TEXTURE, 0);
		this.setUniform(RED_TEXTURE, 1);
		this.setUniform(GREEN_TEXTURE, 2);
		this.setUniform(BLUE_TEXTURE, 3);
		this.setUniform(BLEND_TEXTURE, 4);
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
	}
	
	public void loadLight(Vector3 position, Vector3 colour) {
		this.setUniform(LIGHT_POSITION, position);
		this.setUniform(LIGHT_COLOUR, colour);
	}
	
	public void loadShine(float damper, float reflectivity) {
		this.setUniform(SHINE_DAMPER, damper);
		this.setUniform(REFLECTIVITY, reflectivity);
	}
	
}

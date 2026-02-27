package com.karmorak.lib.engine.graphic.flat;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import com.karmorak.lib.Color;
import com.karmorak.lib.engine.graphic.roomy.shaders.ShaderProgramm;
import com.karmorak.lib.math.Matrix4;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;
import com.karmorak.lib.math.Vector3;

public class TextureShader extends ShaderProgramm {
	
	private static final String VERTEX_FILE = "/shaders/shader_src/TextureVertexShader.glsl";
	private static final String FRAGMENT_FILE =  "/shaders/shader_src/TextureFragmentShader.glsl";
	
	
	public TextureShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public TextureShader(String vertexPath, String fragmentPath) {
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
	
	public void loadTransformationMatrix(Matrix4 matrix) {
		this.setUniform(MODEL_MATRIX, matrix);
	}
	
	
	public void loadTransformationMatrix(Vector2 position, Vector2 scale, boolean advanced) {
		if (advanced) {
			this.setUniform(TRANSLATION_MATRIX, Matrix4.translate(new Vector3(position.getX(), position.getY(), 0)));
			this.setUniform(SCALE_MATRIX, Matrix4.scale(new Vector3(scale.getX(), scale.getY(), 0)));
		} else {
			//this.setUniform(MODEL_MATRIX, Matrix4.transform(position, rotation, scale));
		}
	}	
		
	@Deprecated
	public void loadTransformationMatrix(Vector2 position, Vector2 scale, Vector3 rotation, boolean advanced) {
		if (advanced) {
			this.setUniform(TRANSLATION_MATRIX, Matrix4.translate(new Vector3(position.getX(), position.getY(), 0)));
			this.setUniform(SCALE_MATRIX, Matrix4.scale(new Vector3(scale.getX(), scale.getY(), 0)));
			this.setUniform(ROTATION_MATRIX+ "x", Matrix4.rotate(rotation.getX(), new Vector3(1, 0, 0)));
			this.setUniform(ROTATION_MATRIX+ "y", Matrix4.rotate(rotation.getY(), new Vector3(0, 1, 0)));
			this.setUniform(ROTATION_MATRIX+ "z", Matrix4.rotate(rotation.getZ(), new Vector3(0, 0, 1)));
			this.setUniform("flipY", 1);
			this.setUniform("flipX", 1);
		} else 
			//this.setUniform(MODEL_MATRIX, Matrix4.transform(position, rotation, scale))
			;

	}	
	
	public void loadTransformationMatrix(Vector2 position, Vector2 scale, Vector3 rotation, boolean flipX, boolean flipY) {
		this.setUniform(TRANSLATION_MATRIX, Matrix4.translate(new Vector3(position.getX(), position.getY(), 0)));
		this.setUniform(SCALE_MATRIX, Matrix4.scale(new Vector3(scale.getX(), scale.getY(), 0)));
		this.setUniform(ROTATION_MATRIX+ "x", Matrix4.rotate(rotation.getX(), new Vector3(1, 0, 0)));
		this.setUniform(ROTATION_MATRIX+ "y", Matrix4.rotate(rotation.getY(), new Vector3(0, 1, 0)));
		this.setUniform(ROTATION_MATRIX+ "z", Matrix4.rotate(rotation.getZ(), new Vector3(0, 0, 1)));
		this.setUniform("flipX", flipX ? -1 : 1);
		this.setUniform("flipY",  flipY ? -1 : 1);		
	}

	public void loadTransformationMatrix(int pos_x, int pos_y, int width, int height, Vector3 rotation, boolean flipX, boolean flipY) {
		this.setUniform(TRANSLATION_MATRIX, Matrix4.translate(new Vector3(pos_x, pos_y, 0)));
		this.setUniform(SCALE_MATRIX, Matrix4.scale(new Vector3(width, height, 0)));
		this.setUniform(ROTATION_MATRIX+ "x", Matrix4.rotate(rotation.getX(), new Vector3(1, 0, 0)));
		this.setUniform(ROTATION_MATRIX+ "y", Matrix4.rotate(rotation.getY(), new Vector3(0, 1, 0)));
		this.setUniform(ROTATION_MATRIX+ "z", Matrix4.rotate(rotation.getZ(), new Vector3(0, 0, 1)));
		this.setUniform("flipX", flipX ? -1 : 1);
		this.setUniform("flipY",  flipY ? -1 : 1);
	}
	
	public void loadTransformationMatrix(Vector2i position, Vector2i scale, Vector3 rotation, boolean flipX, boolean flipY) {
		this.setUniform(TRANSLATION_MATRIX, Matrix4.translate(new Vector3(position.getX(), position.getY(), 0)));
		this.setUniform(SCALE_MATRIX, Matrix4.scale(new Vector3(scale.getX(), scale.getY(), 0)));
		this.setUniform(ROTATION_MATRIX+ "x", Matrix4.rotate(rotation.getX(), new Vector3(1, 0, 0)));
		this.setUniform(ROTATION_MATRIX+ "y", Matrix4.rotate(rotation.getY(), new Vector3(0, 1, 0)));
		this.setUniform(ROTATION_MATRIX+ "z", Matrix4.rotate(rotation.getZ(), new Vector3(0, 0, 1)));
		this.setUniform("flipX", flipX ? -1 : 1);
		this.setUniform("flipY",  flipY ? -1 : 1);		
	}
	
	public void load2DColor(Color color,float intensity) {
		this.setUniform("u_color", color.Vec4());
		this.setUniform("u_color_intensity",  intensity);		
	}

}

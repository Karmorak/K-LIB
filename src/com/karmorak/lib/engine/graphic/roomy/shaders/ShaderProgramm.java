package com.karmorak.lib.engine.graphic.roomy.shaders;

import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.net.URL;
import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import com.karmorak.lib.math.Matrix4;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector3;
import com.karmorak.lib.math.Vector4;
import com.karmorak.lib.utils.file.FileUtils;

public abstract class ShaderProgramm {
	
	protected String vertexFile;
	protected String fragmentFile;
	public int vertexID, fragmentID, programID, lightID;
	
	protected boolean created = false;
	
	public static final String 
			MODEL_MATRIX = "model",
			VIEW_MATRIX = "view",
			PROJECTION_MATRIX = "projection",
			LIGHT_POSITION = "lightPosition",
			LIGHT_COLOUR = "lightColour",
			SHINE_DAMPER = "shineDamper",
			REFLECTIVITY = "reflectivity",
			USE_FAKELIGHTING = "useFakeLighting",
			SKY_COLOR = "skyColour",
			TRANSLATION_MATRIX = "translate",
			ROTATION_MATRIX = "rot_",
			SCALE_MATRIX = "scale",
			NUMBER_OF_ROWS = "numberOfRows",
			OFFSET = "offset",
			MINIMUMLIGHT = "minLight";
	
	
	

	
	
	public ShaderProgramm(String vertexPath, String fragmentPath) {
			vertexFile = FileUtils.loadShader(vertexPath);
			fragmentFile = FileUtils.loadShader(fragmentPath);
	}
	
	
	public ShaderProgramm(URL vertexPath, URL fragmentPath) {
			vertexFile = FileUtils.loadShader(vertexPath);
			fragmentFile = FileUtils.loadShader(fragmentPath);
	}
	
	public abstract void create();
	
	
	public boolean isCreated() {
		return created;
	}
	
	public void loadSkyColor(Vector3 colour) {
		this.setUniform(SKY_COLOR, colour);
	}
	
	
	
	
	public int getUniformLocation(String name) {
		return glGetUniformLocation(programID, name);
	}
	
	public void setUniform(String name, float value) {
		glUniform1f(getUniformLocation(name),(float) value);
	}
	public void setUniform(String name, int value) {
		glUniform1i(getUniformLocation(name), value);
	}
	public void setUniform(String name, boolean value) {
		glUniform1i(getUniformLocation(name), value ? 1 : 0);
	}
	public void setUniform(String name, Vector2 value) {
		glUniform2f(getUniformLocation(name), value.getX(), value.getY());
	}
	public void setUniform(String name, Vector3 value) {
		glUniform3f(getUniformLocation(name), value.getX(), value.getY(), value.getZ());
	}
	public void setUniform(String name, Vector4 value) {
		glUniform4f(getUniformLocation(name), value.getX(), value.getY(), value.getWidth(), value.getHeight());
	}
	public void setUniform(String name, Matrix4 value) {
		FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4.SIZE * Matrix4.SIZE);
		matrix.put(value.getAll()).flip();
		glUniformMatrix4fv(getUniformLocation(name), true, matrix);
	}
	
	public void bind() {
		glUseProgram(programID);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public void destroy() {
		glDetachShader(programID, vertexID);
		glDetachShader(programID, fragmentID);
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
		glDeleteProgram(programID);		
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (created ? 1231 : 1237);
		result = prime * result + ((fragmentFile == null) ? 0 : fragmentFile.hashCode());
		result = prime * result + fragmentID;
		result = prime * result + lightID;
		result = prime * result + programID;
		result = prime * result + ((vertexFile == null) ? 0 : vertexFile.hashCode());
		result = prime * result + vertexID;
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
		ShaderProgramm other = (ShaderProgramm) obj;
		if (created != other.created)
			return false;
		if (fragmentFile == null) {
			if (other.fragmentFile != null)
				return false;
		} else if (!fragmentFile.equals(other.fragmentFile))
			return false;
		if (fragmentID != other.fragmentID)
			return false;
		if (lightID != other.lightID)
			return false;
		if (programID != other.programID)
			return false;
		if (vertexFile == null) {
			if (other.vertexFile != null)
				return false;
		} else if (!vertexFile.equals(other.vertexFile))
			return false;
		if (vertexID != other.vertexID)
			return false;
		return true;
	}	
}

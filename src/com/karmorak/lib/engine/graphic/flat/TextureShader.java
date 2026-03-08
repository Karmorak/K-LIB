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

    private static final String VERTEX_FILE = "/shaders/shader_src/TextureVertexShaderNew.glsl";
    private static final String FRAGMENT_FILE = "/shaders/shader_src/TextureFragmentShaderNew.glsl";
	
	
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

    // In deiner Shader-Klasse
    public void loadTransformation(int x, int y, int width, int height, float rotZ, float scale, boolean flipX, boolean flipY) {
        this.setUniform("pixelPosition", new Vector2(x, y));
        this.setUniform("pixelSize", new Vector2(width * scale, height * scale));

        // In 2D reicht meistens die Rotation um die Z-Achse (im Bogenmaß/Radians)
        this.setUniform("rotationZ", (float) Math.toRadians(rotZ));

        // Flip-Logik als einfache Integers (0 oder 1)
        this.setUniform("flipX", flipX ? -1 : 0);
        this.setUniform("flipY", flipY ? -1 : 0);
    }

    // WICHTIG: Einmal pro Frame (oder bei Window-Resize) aufrufen!
    public void loadProjectionMatrix(int windowWidth, int windowHeight) {
        // left=0, right=w, bottom=0, top=h
        Matrix4 projection = Matrix4.orthographic(0, windowWidth, 0, windowHeight, -1, 10);

        this.setUniform("projectionMatrix", projection);
	}

	
	public void load2DColor(Color color,float intensity) {
        this.setUniform("u_color", color.Vec4f());
		this.setUniform("u_color_intensity",  intensity);		
	}


}

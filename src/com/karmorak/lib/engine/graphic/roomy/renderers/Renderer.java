package com.karmorak.lib.engine.graphic.roomy.renderers;

import static org.lwjgl.opengl.GL46.*;

import java.util.List;
import java.util.Map;

import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.engine.graphic.roomy.Mesh;
import com.karmorak.lib.engine.graphic.roomy.shaders.ObjectShader;
import com.karmorak.lib.engine.objects.OBJECT;


public class Renderer {	
	
	
	
	private ObjectShader shader;

	
	public Renderer() {
		this.shader = new ObjectShader( "/shaders/shader_src/mainVertex.glsl", "/shaders/shader_src/mainFragment.glsl");
		shader.create();
	}
	public Renderer(ObjectShader shader) {
		this.shader = shader;
	}
	
	
	public void render(OBJECT obj) {
		prepareMesh(obj.getMesh());
		prepareInstance(obj);
		glDrawElements(GL_TRIANGLES, obj.getMesh().getIndicies().length, GL_UNSIGNED_INT, 0);
		unbindMesh();			
	}
	

	
	public void render(Map<Mesh, List<OBJECT>> objects) {
		for(Mesh mesh : objects.keySet()) {
			prepareMesh(mesh);
			List<OBJECT> batch = objects.get(mesh);
			for (OBJECT obj : batch) {
				prepareInstance(obj);
				glDrawElements(GL_TRIANGLES, mesh.getIndicies().length, GL_UNSIGNED_INT, 0);
			}
			unbindMesh();			
		}
	}
	
	
	
	private void prepareMesh(Mesh m) {
		glBindVertexArray(m.getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		shader.loadTextureAtlas(m.getMaterial());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.getIBO());
		if(m.getMaterial().hasTransparency()) {
			MasterRenderer.disableCulling();
		}		
		shader.loadFakeLighting(m.getMaterial().isUseFakeLighting());
		shader.loadShine(m.getMaterial().getShine_damper(), m.getMaterial().getReflectivity());
		glActiveTexture(GL_TEXTURE0);
		if(m.getMaterial() != null)glBindTexture(GL_TEXTURE_2D, m.getMaterial().getId());


	}
	
	private void unbindMesh() {
		MasterRenderer.enableCulling();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
//		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
	}
	
	private void prepareInstance(OBJECT obj) {		
		shader.loadTransformationMatrix(obj.getPosition(), obj.getRotation(), obj.getScale(), true);
		shader.loadOffset(obj);
	}

	
	

}

package com.karmorak.lib.engine.graphic.roomy.renderers;

import static org.lwjgl.opengl.GL46.*;

import java.util.List;
import com.karmorak.lib.Running;
import com.karmorak.lib.engine.graphic.roomy.shaders.TerrainShader;
import com.karmorak.lib.engine.terrain.Terrain;
import com.karmorak.lib.engine.terrain.TerrainTexturePack;
import com.karmorak.lib.math.Vector3;

public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader) { 
		this.shader = shader;
	}
	
	public void create() {	
		if(!shader.isCreated()) shader.create();
		shader.bind();
		shader.connectTextureUnits();	
		shader.unbind();	
	}
	
	public void create(Running run) {	
		if(!shader.isCreated()) shader.create();
		shader.bind();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(run.getWindow().getProjectionMatrix());
		shader.unbind();				
	}
	
	public void render(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			glDrawElements(GL_TRIANGLES, terrain.getMesh().getIndicies().length, GL_UNSIGNED_INT, 0);
			unbindTerrain();			
		}
	}	
	
	
	private void prepareTerrain(Terrain terrain) {
		glBindVertexArray(terrain.getMesh().getVAO());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, terrain.getMesh().getIBO());
		bindTextures(terrain);
		shader.loadShine(1, 0);

	}
	
	private void bindTextures(Terrain terrain) {
		TerrainTexturePack pack = terrain.getPack();

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, pack.getBackground().getId());
		
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, pack.getRed_texture().getId());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, pack.getGreen_texture().getId());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, pack.getBlue_texture().getId());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, terrain.getBlendMap().getId());
		
		
	}
	
	private void unbindTerrain() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain) {
		shader.loadTransformationMatrix(new Vector3(terrain.getX(), 0, terrain.getY()), new Vector3(0, 0, 0), new Vector3(1, 1, 1));
	}
}

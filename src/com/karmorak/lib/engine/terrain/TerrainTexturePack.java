package com.karmorak.lib.engine.terrain;

import com.karmorak.lib.engine.graphic.roomy.Material;

public class TerrainTexturePack {
	
	private Material background;
	private Material red_texture;
	private Material green_texture;
	private Material blue_texture;
	
	public TerrainTexturePack(Material background, Material red_texture, Material green_texture,
			Material blue_texture) {
		this.background = background;
		this.red_texture = red_texture;
		this.green_texture = green_texture;
		this.blue_texture = blue_texture;
	}

	public void create() {
		background.create();
		red_texture.create();
		green_texture.create();
		blue_texture.create();
	}
	
	public Material getBackground() {
		return background;
	}

	public Material getRed_texture() {
		return red_texture;
	}

	public Material getGreen_texture() {
		return green_texture;
	}

	public Material getBlue_texture() {
		return blue_texture;
	}
	
	
	

}

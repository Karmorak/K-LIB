#version 460 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

uniform vec4 u_color;
uniform float u_color_intensity;

void main(void){


	vec4 color = texture(guiTexture,textureCoords);
	vec4 b_color = vec4 ((color.rgb * u_color.rgb * u_color_intensity)/255, (color.a * u_color.a)/255);
	out_Color = b_color;

}

#version 460 core

in vec2 position;
in vec2 textureCoord;

out vec2 textureCoords;


uniform mat4 translate;
uniform mat4 scale;

void main(void){

	gl_Position = translate * scale* vec4(position, 0.0, 1.0);
	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
}

#version 460 core

in vec2 position;
in vec2 textureCoord;

out vec2 textureCoords;

uniform mat4 translate;
uniform mat4 scale;
uniform mat4 rot_x;
uniform mat4 rot_y;
uniform mat4 rot_z;

uniform int flipX;
uniform int flipY;

void main(void){

	mat4 rotation = rot_x * rot_y* rot_z;
	gl_Position = translate * scale * rotation * vec4(position, 0.0, 1.0);
	textureCoords = vec2(flipY * textureCoord.x, flipX * textureCoord.y);

}

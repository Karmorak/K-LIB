#version 460 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 color;


out vec2 passTextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;

uniform mat4 view;
uniform mat4 projection;
uniform vec3 lightPosition;
uniform float useFakeLighting;

//uniform mat4 model;
uniform mat4 translate;
uniform mat4 rot_x;
uniform mat4 rot_y;
uniform mat4 rot_z;
uniform mat4 scale;

uniform int numberOfRows;
uniform vec2 offset;

const float density = 0.00003;
const float gradient = 15;

void main() {

	mat4 transMatrix = translate * (rot_x * rot_y * rot_z) *  scale;
	vec4 worldPositon = transMatrix * vec4(position, 1.0);
	vec4 positionRelativetoCam = view * worldPositon;

	gl_Position = projection * positionRelativetoCam;
	passTextureCoord = (textureCoord/numberOfRows) + offset;

	vec3 actualNormal = normal;
	if(useFakeLighting > 0.5f) {
		actualNormal = vec3(0.0, 1.0, 0.0);
	}

	surfaceNormal = (transMatrix * vec4(actualNormal, 0.0)).xyz;
	toLightVector = lightPosition - worldPositon.xyz;
	toCameraVector = (inverse(view)*vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPositon.xyz;

	float distance = length(positionRelativetoCam.xyz);
	visibility = exp(-pow((distance*density),gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}

#version 460 core


in vec3 position;
in vec2 passTextureCoord;


in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D tex;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;
uniform float minLight; 

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectLightDirection = reflect(lightDirection, unitNormal);


	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, minLight);
	vec3 diffuse = brightness * lightColour;

	float specularFactor = dot(reflectLightDirection, unitCameraVector);
	specularFactor = max(specularFactor, 0.0);
	float dampedFaktor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFaktor * reflectivity * lightColour;

	vec4 textureColor = texture(tex, passTextureCoord);
	if(textureColor.a < 0.5) {
		discard;
	}

	outColor = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
	outColor = mix(vec4(skyColour, 1.0), outColor, visibility);

}

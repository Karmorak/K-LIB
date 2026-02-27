#version 460 core

in vec2 passTextureCoord;


in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D backgroundTex;
uniform sampler2D rTex;
uniform sampler2D gTex;
uniform sampler2D bTex;
uniform sampler2D blendMap;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;


void main(void) {

	vec4 blendMapColour = texture(blendMap, passTextureCoord);

	float backTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
	vec2 tiledCoords = passTextureCoord * 40.0;
	vec4 backgroundTextureColour = texture(backgroundTex, tiledCoords) * backTextureAmount;
	vec4 rTextureColour = texture(rTex, tiledCoords) * blendMapColour.r;
	vec4 gTextureColour = texture(gTex, tiledCoords) * blendMapColour.g;
	vec4 bTextureColour = texture(bTex, tiledCoords) * blendMapColour.b;

	vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectLightDirection = reflect(lightDirection, unitNormal);


	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, 0.2);
	vec3 diffuse = brightness * lightColour;

	float specularFactor = dot(reflectLightDirection, unitCameraVector);
	specularFactor = max(specularFactor, 0.0);
	float dampedFaktor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFaktor * reflectivity * lightColour;


	outColor = vec4(diffuse, 1.0) * totalColour + vec4(finalSpecular, 1.0);
	outColor = mix(vec4(skyColour, 1.0), outColor, visibility);

}

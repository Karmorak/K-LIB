#version 460 core

layout (location = 0) in vec2 position;// 0.0 bis 1.0
layout (location = 1) in vec2 textureCoord;

out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform vec2 pixelPosition;
uniform vec2 pixelSize;
uniform float rotationZ;
uniform int flipX;
uniform int flipY;

void main(void) {
    // 1. Rotation (Z-Achse)
    float s = sin(rotationZ);
    float c = cos(rotationZ);
    mat2 rot = mat2(c, s, -s, c);

    // 2. Skalieren und um den Mittelpunkt zentrieren
    // Wir ziehen 0.5 ab, damit (0.5, 0.5) der Nullpunkt ist
    vec2 centeredPos = (position - vec2(0.5)) * pixelSize;

    // 3. Rotieren
    vec2 rotatedPos = rot * centeredPos;

    // 4. Zurückschieben auf die Zielposition (plus halbe Größe, um Mitte zu halten)
    vec2 finalPos = rotatedPos + pixelPosition + (pixelSize * 0.5);

    gl_Position = projectionMatrix * vec4(finalPos, 0.0, 1.0);
    //    gl_Position = projectionMatrix * vec4(pixelPosition, 0.0, 1.0);

    // 4. Texture-Coords
    // Da wir 0,0 unten haben, ist textureCoord.y = 0 auch unten.
    float texX = (flipX == -1) ? 1.0 - textureCoord.x : textureCoord.x;
    float texY = (flipY == -1) ? textureCoord.y : 1.0 - textureCoord.y;

    textureCoords = vec2(texX, texY);
}
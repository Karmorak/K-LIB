#version 460 core

// 1. Standard Quad-Attribute (per Vertex, ändern sich NIE)
layout (location = 0) in vec2 position;
layout (location = 1) in vec2 textureCoord;// 0.0 bis 1.0

// 2. Instanz-Attribute (per Sprite, ändern sich mit jeder Instanz)
layout (location = 2) in vec2 instancePixelPosition;// x, y
layout (location = 3) in vec2 instancePixelSize;// width, height
layout (location = 4) in float instanceRotationZ;// rotationZ
layout (location = 5) in vec2 instanceFlip;// x = flipX, y = flipY
out vec2 textureCoords;

uniform mat4 projectionMatrix;

void main(void) {
    // 1. Rotation (Z-Achse)
    float s = sin(instanceRotationZ);
    float c = cos(instanceRotationZ);
    mat2 rot = mat2(c, s, -s, c);
    // 2. Skalieren und um den Mittelpunkt zentrieren
    vec2 centeredPos = (position - vec2(0.5)) * instancePixelSize;
    // 3. Rotieren
    vec2 rotatedPos = rot * centeredPos;
    // 4. Zurückschieben auf die Zielposition
    vec2 finalPos = rotatedPos + instancePixelPosition + (instancePixelSize * 0.5);

    gl_Position = projectionMatrix * vec4(finalPos, 0.0, 1.0);

    // 5. Texture-Coords mit Flips
    float texX = (instanceFlip.x == -1.0) ? 1.0 - textureCoord.x :
    textureCoord.x;
    float texY = (instanceFlip.y == -1.0) ? textureCoord.y : 1.0 -
    textureCoord.y;

    textureCoords = vec2(texX, texY);
}
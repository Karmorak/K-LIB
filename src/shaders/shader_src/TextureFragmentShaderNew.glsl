#version 460 core

in vec2 textureCoords;
out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec4 u_color;// Sollte in Java bereits 0.0 bis 1.0 sein!
uniform float u_color_intensity;// 0.0 (nur Textur) bis 1.0 (voll u_color)

void main(void){
    vec4 texColor = texture(guiTexture, textureCoords);

    // 1. Einfaches Einfärben (Tinting):
    // Wir mischen die Texturfarbe mit der u_color basierend auf der Intensität
    vec3 mixedRGB = mix(texColor.rgb, texColor.rgb * u_color.rgb, u_color_intensity);

    // 2. Alpha handling
    // Meistens will man das Alpha der Textur beibehalten und nur mit u_color.a modulieren
    float finalAlpha = texColor.a * u_color.a;

    out_Color = vec4(mixedRGB, finalAlpha);
}
#version 130
/* RENDERTARGETS: 2, 15 */
const int RGBA16F = 0;
const int colortex15Format = RGBA16F;
out vec4 outColor1;
out vec4 outColor0;
void main() {
    outColor0 = vec4(0.25, 0.0, 0.0, 1.0);
    outColor1 = vec4(0.0, 0.5, 0.0, 1.0);
}

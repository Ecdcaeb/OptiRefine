#version 130
/* RENDERTARGETS: 5 */
uniform sampler2D colortex9;
uniform sampler2D colortex11;
out vec4 outColor0;
void main() {
    bool correctSize = textureSize(colortex9, 0) == ivec2(32, 24) && textureSize(colortex11, 0) == ivec2(32, 24);
    bool edge = texelFetch(colortex11, ivec2(31, 23), 0).r == 1.0;
    outColor0 = correctSize && edge ? texelFetch(colortex9, ivec2(31, 23), 0) : vec4(1, 0, 0, 1);
}

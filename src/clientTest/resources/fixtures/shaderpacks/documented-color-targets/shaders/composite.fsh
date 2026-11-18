#version 130
/* DRAWBUFFERS:8 */
uniform sampler2D colortex2;
uniform sampler2D colortex15;
void main() {
    gl_FragData[0] = vec4(texelFetch(colortex2, ivec2(1), 0).r, texelFetch(colortex15, ivec2(1), 0).g, 0.75, 1.0);
}

#version 130
/* RENDERTARGETS: 9, 11 */
uniform sampler2D colortex8;
out vec4 outColor0;
out vec4 outColor1;
void main() {
    outColor0 = texelFetch(colortex8, ivec2(1), 0);
    outColor1 = vec4(ivec2(gl_FragCoord.xy) == ivec2(31, 23) ? 1.0 : 0.0);
}

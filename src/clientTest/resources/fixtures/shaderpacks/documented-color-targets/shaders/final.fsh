#version 130
uniform sampler2D colortex5;
uniform float viewWidth;
uniform float viewHeight;
void main() {
    ivec2 size = textureSize(colortex5, 0);
    gl_FragColor = size == ivec2(vec2(viewWidth, viewHeight) * 0.25)
            ? texelFetch(colortex5, size - 1, 0) : vec4(1, 0, 0, 1);
}

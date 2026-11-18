#version 120
attribute vec4 mc_Entity;
varying float blockId;
void main() {
    gl_Position = ftransform();
    blockId = mc_Entity.x;
}

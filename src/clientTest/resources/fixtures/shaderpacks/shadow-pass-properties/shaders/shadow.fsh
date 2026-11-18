#version 120
/* DRAWBUFFERS:0 */
varying float blockId;
const int shadowMapResolution = 128;
const float shadowDistance = 48.0;
const float shadowDistanceRenderMul = 0.25;
void main() {
    if (abs(blockId - 95.0) < 0.1) {
        gl_FragData[0] = vec4(0.0, 1.0, 0.0, 1.0);
    } else if (abs(blockId - 2.0) < 0.1 || abs(blockId - 3.0) < 0.1 || abs(blockId - 7.0) < 0.1) {
        gl_FragData[0] = vec4(1.0, 0.0, 0.0, 1.0);
    } else {
        discard;
    }
}

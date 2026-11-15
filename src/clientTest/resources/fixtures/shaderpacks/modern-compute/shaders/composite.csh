#version 430
#define MODERN_GAIN 0.125 // [0.125 0.25]
#include "/lib/modern_value.glsl"
layout(local_size_x=8,local_size_y=8,local_size_z=1) in;
const vec2 workGroupsRender=vec2(1.0,1.0);
layout(rgba8) uniform writeonly MODERN_IMAGE_TYPE MODERN_OUTPUT_IMAGE;
void main(){ivec2 p=ivec2(gl_GlobalInvocationID.xy);if(any(greaterThanEqual(p,imageSize(colorimg0))))return;imageStore(colorimg0,p,modernValue());}

#version 430
layout(local_size_x=8,local_size_y=8,local_size_z=1) in;
const vec2 workGroupsRender=vec2(1.0,1.0);
layout(rgba8) uniform writeonly image2D colorimg5;
void main(){ivec2 p=ivec2(gl_GlobalInvocationID.xy);if(any(greaterThanEqual(p,imageSize(colorimg5))))return;imageStore(colorimg5,p,vec4(0.125,0.25,0.5,1));}

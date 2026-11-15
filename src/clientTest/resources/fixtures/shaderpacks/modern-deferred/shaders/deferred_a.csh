#version 430
layout(local_size_x=8,local_size_y=8,local_size_z=1) in;
const ivec3 workGroups=ivec3(1,1,1);
layout(rgba8) uniform image2D colorimg0;
void main(){ivec2 p=ivec2(gl_GlobalInvocationID.xy);vec4 v=imageLoad(colorimg0,p);imageStore(colorimg0,p,v+vec4(0.125,0.25,0.375,0));}

#version 430
layout(local_size_x=8,local_size_y=8,local_size_z=1) in;
const ivec3 workGroups=ivec3(0,1,1);
layout(rgba8) uniform writeonly image2D colorimg0;
void main(){imageStore(colorimg0,ivec2(gl_GlobalInvocationID.xy),vec4(1,0,0,1));}

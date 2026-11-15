#version 430 compatibility
layout(rgba8) uniform readonly image2D colorimg5;
void main(){gl_FragColor=imageLoad(colorimg5,ivec2(gl_FragCoord.xy));}

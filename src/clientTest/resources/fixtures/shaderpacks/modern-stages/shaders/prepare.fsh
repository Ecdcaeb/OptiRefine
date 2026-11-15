#version 120
/* DRAWBUFFERS:5 */
const bool colortex5Clear=false;
uniform sampler2D colortex5;
void main(){gl_FragData[0]=texture2D(colortex5,gl_TexCoord[0].xy)+vec4(0.125,0,0,0);}

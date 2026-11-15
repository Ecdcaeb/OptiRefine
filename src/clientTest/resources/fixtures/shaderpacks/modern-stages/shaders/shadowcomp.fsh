#version 120
/* DRAWBUFFERS:0 */
uniform sampler2D shadowcolor0;
void main(){gl_FragData[0]=texture2D(shadowcolor0,gl_TexCoord[0].xy)+vec4(0.125,0,0,0);}

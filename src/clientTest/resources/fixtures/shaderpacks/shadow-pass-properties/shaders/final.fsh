#version 120
uniform sampler2D colortex0;
uniform sampler2D shadowtex0;
uniform sampler2D shadowcolor0;
void main() {
    gl_FragColor = texture2D(colortex0, gl_TexCoord[0].xy);
}

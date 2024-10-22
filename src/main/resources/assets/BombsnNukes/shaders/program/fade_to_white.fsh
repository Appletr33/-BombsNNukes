#version 120

uniform sampler2D texture;
uniform float fadeProgress;

varying vec2 texCoord;

void main() {
    vec4 texColor = texture2D(texture, texCoord);
    vec4 white = vec4(1.0, 1.0, 1.0, 1.0);

    gl_FragColor = mix(texColor, white, fadeProgress);
}
#version 410 core

in vec2 uv;

uniform sampler2D uTex;

void main() {
    vec4 tex = texture( uTex, uv );
    gl_FragColor = vec4( vec3( tex.r + 1, tex.g + 1, tex.b + 1 ), tex.a );
    //gl_FragColor = vec4( 1.0 );
    //gl_FragColor = vec4( tex.rgb, 1.0 );
}

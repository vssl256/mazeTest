#version 410 core

in vec2 uv;

uniform sampler2D uTex;

void main() {
    vec4 tex = texture( uTex, uv );
    float alpha = 1.0f;
    if ( tex.r == 0.0f ) {
        alpha = 0.0f;
    }
    gl_FragColor = vec4( vec3( tex.r, tex.g, tex.b ), alpha );
    //gl_FragColor = vec4( 1.0 );
    //gl_FragColor = vec4( tex.rgb, 1.0 );
}

#version 410 core

in vec2 uv;

uniform sampler2D uTex;

const float radius = 0.2;

float remap( float x, float x1, float x2, float y1, float y2 ) {
    return y1 + ( ( x - x1 ) * ( y2 -y1 ) ) / ( x2 - x1 );
}

void main() {
    vec2 lensUv = vec2( remap( uv.x, 0, 1, 0.25, 0.75 ), remap( uv.y, 0, 1, 0.25, 0.75 ) );
    vec4 tex = texture( uTex, lensUv );

    float distance = distance( uv, lensUv );

    gl_FragColor = texture( uTex, uv );

    if ( distance < radius ) {
        gl_FragColor = vec4( 0.0, 0.0, 0.0, 1.0 );
    }
    if ( distance < radius - 0.01 ) {
        gl_FragColor = texture( uTex, ( lensUv + uv ) / 2.0 );
    }

    //gl_FragColor = vec4( tex.rgb, 1.0 );
}

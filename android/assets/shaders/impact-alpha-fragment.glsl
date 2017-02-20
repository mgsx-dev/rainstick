#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;
varying vec4 v_color;


void main() {

    vec4 color = texture2D(u_texture, v_texCoords);

    float s = length(v_texCoords);
    float t = atan(v_texCoords.y, v_texCoords.x) / 3.1415;

    float d = 1.0 - s;

    gl_FragColor = vec4(v_color.rgb * color.rgb * (sin(t * 100.0)+1.0)*0.5, d * v_color.a*40.0/255.0	);
}

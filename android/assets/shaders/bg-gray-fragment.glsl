#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec2 v_texCoords;
varying vec2 v_world;
uniform sampler2D u_texture;
uniform float u_scale;
uniform float u_time;
uniform vec4 u_color;



void main() {



    vec4 color = texture2D(u_texture, v_texCoords * 10.0 - vec2(-v_world.x, v_world.y) * u_scale);

    float f = sin(color.r * 100.0 /*+ u_time * 0.5*/);
    float c;
    if(f < 0.0) c = 0.0;
    else c = 0.05 + 0.015 * (sin(u_time/3.0)+1.0);

    gl_FragColor = vec4(u_color.rgb * c, 1.0);
}

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



void main() {



    vec4 color = texture2D(u_texture, v_texCoords * 10.0 - vec2(-v_world.x, v_world.y) * u_scale);

    float f = abs(color.r - 0.5 /*+ u_time * 0.5*/)*3.0;
    float c;
    if(f < 0.25) c = 0.0;
    else c = 0.5 + 0.015 * (sin(u_time/3)+1.0);
	c = /*1.0-*/ pow(1.0-f,10.0) + c * 0.1;
    gl_FragColor = vec4(c,c,c	, 1.0);
}
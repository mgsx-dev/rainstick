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

//	vec2 v = vec2(gl_FragCoord.x / u_screen.x, gl_FragCoord.y / u_screen.y);

    vec4 color = texture2D(u_texture, v_texCoords * 10.0 - vec2(-v_world.x, v_world.y) * u_scale);

    float f = sin(color.r * 100.0 /*+ u_time * 0.5*/);
    vec3 c;
    if(f < 0.0) c = vec3(0.0,0.1,0.5);
    else c = vec3(0.0,0.1, 0.6 + 0.1 * (sin(u_time)+1.0));

    gl_FragColor = vec4(u_color.rgb * c, 1.0);
}

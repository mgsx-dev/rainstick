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

    float waveStrength = 0.15;
    float frequency = 30.0;
    float waveSpeed = 3.75;
    vec4 sunlightColor = vec4(1.0,0.91,0.75,0.05);
    float sunlightStrength = 1.0;
    float centerLight = 0.2;
    float oblique = 0.25; 
          
   
    float modifiedTime = u_time * waveSpeed;
    float distance = 1.0 -length(v_texCoords);
    float addend = (sin(frequency*distance-modifiedTime)+centerLight) * waveStrength ;
    
    vec4 colorToAdd = sunlightColor * sunlightStrength * addend;

    gl_FragColor = vec4(color.r,color.g,color.b,distance * v_color.a * 0.5)+colorToAdd;
}

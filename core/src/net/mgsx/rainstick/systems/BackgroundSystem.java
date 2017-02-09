package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableSystem;
import net.mgsx.game.core.annotations.Storable;

@Storable("rainstick.background")
@EditableSystem
public class BackgroundSystem extends EntitySystem
{
	@Editable public float scale = .1f;
	@Editable public float texScale = .1f;
	
	private Sprite sprite;
	private Batch batch;
	private GameScreen game;
	private ShaderProgram shader;
	private OrthographicCamera camera;
	
	public BackgroundSystem(GameScreen game) {
		super(GamePipeline.RENDER);
		this.game = game;
		sprite = new Sprite(new Texture(Gdx.files.internal("perlin.png")));
		sprite.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		batch = new SpriteBatch();
		createShader();
	}
	
	@Editable
	public void createShader(){
		if(shader != null) shader.dispose();
		shader = new ShaderProgram(
				Gdx.files.internal("shaders/bg-vertex.glsl"),
				Gdx.files.internal("shaders/bg-fragment.glsl"));
		shader.begin();
		if(!shader.isCompiled()){
			Gdx.app.error("GLSL", shader.getLog());
		}
		shader.end();
	}
	
	public void update(float deltaTime) 
	{
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		batch.setShader(shader);
		batch.begin();
		// draw unit quad (screen) adapt texture coordinates to map world space
		batch.setProjectionMatrix(game.camera.invProjectionView);
		
		// sprite.setBounds(x, y, width, height);
		
		sprite.setBounds(-1, -1, 2, 2);
		sprite.setRegion(0, 0, texScale, texScale);
		float s = 1f / Gdx.graphics.getHeight();// / 512f;
		
		shader.setUniformf("u_scale", scale);
		shader.setUniformf("u_time", GdxAI.getTimepiece().getTime());
//		shader.setUniformf("u_world", offset.x, offset.y);
		
		sprite.draw(batch);
		batch.end();
	}
}

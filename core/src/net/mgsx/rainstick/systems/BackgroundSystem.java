package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableSystem;
import net.mgsx.game.core.annotations.Storable;
import net.mgsx.game.core.helpers.FilesShaderProgram;

@Storable("rainstick.background")
@EditableSystem
public class BackgroundSystem extends EntitySystem
{
	@Editable public float scale = .1f;
	@Editable public float texScale = .1f;
	
	private Sprite sprite;
	private Batch batch;
	private GameScreen game;
	
	@Editable
	public FilesShaderProgram shader = new FilesShaderProgram(
			Gdx.files.internal("shaders/bg-vertex.glsl"),
			Gdx.files.internal("shaders/bg-fragment.glsl"));
	
	public BackgroundSystem(GameScreen game) {
		super(GamePipeline.RENDER);
		this.game = game;
		sprite = new Sprite(new Texture(Gdx.files.internal("perlin.png")));
		sprite.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		batch = new SpriteBatch();
	}
	
	public void update(float deltaTime) 
	{
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		batch.setShader(shader.program());
		batch.begin();
		// draw unit quad (screen) adapt texture coordinates to map world space
		batch.setProjectionMatrix(game.camera.invProjectionView);
		
		// sprite.setBounds(x, y, width, height);
		
		sprite.setBounds(-1, -1, 2, 2);
		sprite.setRegion(0, 0, texScale, texScale);
		
		shader.program().setUniformf("u_scale", scale);
		shader.program().setUniformf("u_time", GdxAI.getTimepiece().getTime());
//		shader.setUniformf("u_world", offset.x, offset.y);
		
		sprite.draw(batch);
		batch.end();
	}
}

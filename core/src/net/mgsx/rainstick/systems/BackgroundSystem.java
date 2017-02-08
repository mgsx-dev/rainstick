package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;

public class BackgroundSystem extends EntitySystem
{
	private Sprite sprite;
	private Batch batch;
	private GameScreen game;
	public BackgroundSystem(GameScreen game) {
		super(GamePipeline.RENDER);
		this.game = game;
		sprite = new Sprite(new Texture(Gdx.files.internal("perlin.png")));
		batch = new SpriteBatch();
	}
	
	public void update(float deltaTime) 
	{
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		batch.begin();
		batch.setProjectionMatrix(game.camera.combined);
		sprite.draw(batch);
		batch.end();
	}
}

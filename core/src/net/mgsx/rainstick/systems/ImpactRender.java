package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableSystem;
import net.mgsx.game.core.annotations.Storable;
import net.mgsx.game.core.helpers.FilesShaderProgram;
import net.mgsx.rainstick.components.ImpactComponent;

@Storable("rainstick.impact.render")
@EditableSystem
public class ImpactRender extends IteratingSystem
{
	private GameScreen game;
	private SpriteBatch batch;
	private Sprite sprite;
	
	@Editable
	public FilesShaderProgram shader = new FilesShaderProgram(
			Gdx.files.internal("shaders/impact-vertex.glsl"),
			Gdx.files.internal("shaders/impact-fragment.glsl"));
	
	@Editable
	public float scale = 10;
	
	public ImpactRender(GameScreen game) {
		super(Family.all(ImpactComponent.class).get(), GamePipeline.RENDER);
		this.game = game;
		batch = new SpriteBatch();
		sprite = new Sprite(new Texture(Gdx.files.internal("perlin.png"))); // TODO inject asset @Asset
	}
	
	@Override
	public void update(float deltaTime) {
		batch.begin();
		batch.setShader(shader.program());
		batch.setProjectionMatrix(game.camera.combined);
		batch.enableBlending();
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LESS);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		super.update(deltaTime);
		batch.end();
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ImpactComponent impact = ImpactComponent.components.get(entity);
		sprite.setRegion(-1f, -1f, 1f, 1f);
		float size = impact.life * scale * MathUtils.clamp(impact.energy * 10, 0f, 1);
		sprite.setSize(size, size);
		sprite.setCenter(impact.position.x, impact.position.y);
		float mFactor = impact.material == 0 ? 0.5f : 1;
		sprite.setColor(1,1,1,(1-impact.life) * mFactor);
		sprite.draw(batch);
	}
}

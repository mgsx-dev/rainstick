package net.mgsx.rainstick.systems;

import org.puredata.core.PdListener;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableSystem;
import net.mgsx.game.core.annotations.EnumType;
import net.mgsx.game.core.annotations.Storable;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.pd.Pd;
import net.mgsx.pd.utils.PdAdapter;
import net.mgsx.rainstick.components.Ball;

@Storable("rainstick.outline")
@EditableSystem
public class BallOutlineRender extends IteratingSystem {


	private ShapeRenderer batch;
	private GameScreen game;
	private float feedback;
	private PdListener feedbackListener;
	
	@Editable
	public float minRadiusRate = 1.1f;
	@Editable
	public float maxRadiusRate = 2.0f;
	@Editable(type=EnumType.UNIT)
	public float opacity = .5f;
	
	
	public BallOutlineRender(GameScreen game) {
		super(Family.all(Ball.class,Box2DBodyModel.class).get(), GamePipeline.RENDER );
		this.game = game;
		batch = new ShapeRenderer();
		feedbackListener = new PdAdapter(){
			@Override
			public void receiveFloat(String source, float x) {
				feedback = x;
			}
		};
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		Pd.audio.addListener("feedback", feedbackListener);
	}
	
	@Override
	public void removedFromEngine(Engine engine) {
		Pd.audio.removeListener("feedback", feedbackListener);
		super.removedFromEngine(engine);
	}
	
	@Override
	public void update(float deltaTime) {
		// Gdx.gl.glDepthFunc(GL20.GL_GREATER);
		batch.setProjectionMatrix(game.camera.combined);
		batch.begin(ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
		Gdx.gl.glDepthFunc(GL20.GL_LESS	);
		Gdx.gl.glDepthMask(false);
		
		//Gdx.gl.glClearDepthf(0);
		//Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		//Gdx.gl.glColorMask(false, false, false, false); // XXX debug here
		super.update(deltaTime);
		batch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		//Gdx.gl.glClearDepthf(1); // restore to default
		//Gdx.gl.glColorMask(true, true, true, true);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		Ball ball = Ball.components.get(entity);
		
		batch.setColor(1f,1f,1f, opacity);
		batch.circle(ball.position.x, ball.position.y, ball.radius * (minRadiusRate * (1-feedback) + maxRadiusRate * feedback), 16);
	}
}

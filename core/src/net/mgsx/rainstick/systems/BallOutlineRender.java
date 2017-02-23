package net.mgsx.rainstick.systems;

import org.puredata.core.PdListener;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.pd.Pd;
import net.mgsx.pd.events.PdAdapter;
import net.mgsx.rainstick.components.Ball;

public class BallOutlineRender extends IteratingSystem {


	private ShapeRenderer batch;
	private GameScreen game;
	private float feedback;
	private PdListener feedbackListener;
	
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
	protected void processEntity(Entity entity, float deltaTime) {
		Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
		float x = physics.body.getPosition().x;
		float y = physics.body.getPosition().y;
		Ball ball = Ball.components.get(entity);
		
		float min = getEngine().getSystem(ResonatorPhysicSystem.class).velMin;
		float max = getEngine().getSystem(ResonatorPhysicSystem.class).velMax;
		float speed =MathUtils.clamp(( physics.body.getLinearVelocity().len() - min) / (max - min), 0, 1) ;
		float speed2 = .5f + .5f * (float)Math.sin(GdxAI.getTimepiece().getTime() * 0.3f * speed);
		batch.setColor(1f,1f,1f,0.05f+speed * .25f);
		batch.circle(x, y, ball .radius  +(1-speed2)* .55f * feedback, 16);
	}
}

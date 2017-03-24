package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.rainstick.components.InvertMask;
import net.mgsx.rainstick.components.Mask;

public class MaskRender extends IteratingSystem
{
	private ModelBatch batch;
	private GameScreen game;
	
	public MaskRender(GameScreen game) {
		super(Family.all(Mask.class, Box2DBodyModel.class).exclude(InvertMask.class).get(), GamePipeline.RENDER -1);
		this.game = game;
		batch = new ModelBatch();
	}
	
	@Override
	public void update(float deltaTime) {
		batch.begin(game.camera);
		Gdx.gl.glFrontFace(GL20.GL_CW);
		
		Gdx.gl.glClearDepthf(0);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glColorMask(false, false, false, false);
		super.update(deltaTime);
		batch.end();
		
		Gdx.gl.glClearDepthf(1); // restore to default
		Gdx.gl.glColorMask(true, true, true, true);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Mask mask = Mask.components.get(entity);
		Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
		if(mask.modelInstance== null) return;
		// TODO jni calls could be limited by caching positions after physic phase.		mask.modelInstance.transform.idt();
		mask.modelInstance.transform.setTranslation(physics.body.getPosition().x, physics.body.getPosition().y, 0);
		mask.modelInstance.transform.rotate(Vector3.Z, physics.body.getAngle() * MathUtils.radiansToDegrees);
		batch.render(mask.modelInstance);
	}
}

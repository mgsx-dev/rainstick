package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.rainstick.components.Ball;
import net.mgsx.rainstick.components.Mask;

public abstract class AbstractMaskRenderer extends IteratingSystem
{
	protected final ModelBatch batch;
	
	public AbstractMaskRenderer(Family family, int priority) {
		super(family, priority);
		batch = new ModelBatch();
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Mask mask = Mask.components.get(entity);
		if(mask.modelInstance== null) return;
		
		Ball ball = Ball.components.get(entity);
		if(ball != null)
		{
			mask.modelInstance.transform.setToTranslation(ball.position.x, ball.position.y, 0);
			mask.modelInstance.transform.rotate(Vector3.Z, ball.angle * MathUtils.radiansToDegrees);
		}
		else
		{
			// TODO jni calls could be avoided for static bodies.
			Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
			Vector2 position = physics.body.getPosition();
			mask.modelInstance.transform.setToTranslation(position.x, position.y, 0);
			mask.modelInstance.transform.rotate(Vector3.Z, physics.body.getAngle() * MathUtils.radiansToDegrees);
		}
		batch.render(mask.modelInstance);
	}
}
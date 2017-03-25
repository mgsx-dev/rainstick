package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.rainstick.components.Ball;

public class BallPhysicCacheSystem extends IteratingSystem
{
	public BallPhysicCacheSystem() {
		super(Family.all(Ball.class, Box2DBodyModel.class).get(), GamePipeline.AFTER_PHYSICS);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		Ball ball = Ball.components.get(entity);
		Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
		
		if(ball.mass < 0)
		{
			ball.mass = physics.body.getMass();
		}
		
		ball.position.set(physics.body.getPosition());
		ball.angle = physics.body.getAngle();
		ball.velocity.set(physics.body.getLinearVelocity());
	}
}

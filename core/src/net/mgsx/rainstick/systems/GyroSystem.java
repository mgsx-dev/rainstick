package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.plugins.box2d.systems.Box2DWorldSystem;

public class GyroSystem extends EntitySystem
{
	public GyroSystem() {
		super(GamePipeline.BEFORE_PHYSICS);
	}
	
	@Override
	public void update(float deltaTime) {
		float gx = Gdx.input.getAccelerometerY();
		float gy = -Gdx.input.getAccelerometerX();
		
		getEngine().getSystem(Box2DWorldSystem.class).gravity.set(gx, gy);
	}
}

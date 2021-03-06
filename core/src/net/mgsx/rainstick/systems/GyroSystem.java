package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.plugins.box2d.systems.Box2DWorldSystem;

public class GyroSystem extends EntitySystem
{
	public GyroSystem() {
		super(GamePipeline.BEFORE_PHYSICS);
	}
	
	@Override
	public void update(float deltaTime) {
		if(Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
			/*
			float gx = Gdx.input.getAccelerometerY();
			float gy = -Gdx.input.getAccelerometerX();*/
			float gx = -Gdx.input.getAccelerometerX();
			float gy = -Gdx.input.getAccelerometerY();
			
			
			getEngine().getSystem(Box2DWorldSystem.class).gravity.set(gx, gy);
		}
	}
}

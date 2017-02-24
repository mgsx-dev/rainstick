package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableSystem;
import net.mgsx.game.core.annotations.Storable;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;

@Storable("rainstick.calibration")
@EditableSystem
public class CalibrationSystem extends IteratingSystem
{
	@Editable(readonly=true, realtime=true)
	public float velMax, massMax, xMin, xMax;
	
	@Editable
	public boolean run;

	public CalibrationSystem() {
		super(Family.all(Box2DBodyModel.class).get(), GamePipeline.AFTER_PHYSICS);
	}
	
	@Editable
	public void reset(){
		velMax = massMax = xMin = xMax = 0;
	}
	
	@Override
	public void update(float deltaTime) {
		if(run){
			super.update(deltaTime);
		}
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
		velMax = Math.max(physics.body.getLinearVelocity().len(), velMax);
		massMax = Math.max(physics.body.getMass(), massMax);
		xMin = Math.min(physics.body.getPosition().x, xMin);
		xMax = Math.max(physics.body.getPosition().x, xMax);
	}
}

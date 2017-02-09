package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableSystem;
import net.mgsx.rainstick.components.ImpactComponent;

@EditableSystem
public class ImpactUpdate extends IteratingSystem
{
	@Editable
	public float lifeMax = 3;

	public ImpactUpdate() {
		super(Family.all(ImpactComponent.class).get(), GamePipeline.LOGIC);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ImpactComponent impact = ImpactComponent.components.get(entity);
		impact.life += deltaTime / lifeMax;
		if(impact.life > 1 ){
			getEngine().removeEntity(entity);
		}
	}
}
package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.EntitySystem;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableSystem;
import net.mgsx.game.core.annotations.Storable;
import net.mgsx.game.plugins.box2d.systems.Box2DWorldSystem;

@Storable("rainstick.gravity")
@EditableSystem
public class GravityAutoSystem extends EntitySystem
{
	@Editable
	public float rotationSpeed = 20;
	
	@Editable
	public float gravityScale = 10;
	
	private float rotation;
	
	public GravityAutoSystem() {
		super(GamePipeline.BEFORE_PHYSICS);
		setProcessing(false);
	}
	
	@Override
	public void update(float deltaTime) {
		rotation += deltaTime * rotationSpeed;
		getEngine().getSystem(Box2DWorldSystem.class).gravity.set(gravityScale, 0).rotate(rotation);
	}
}

package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.game.plugins.box2d.listeners.Box2DEntityListener;
import net.mgsx.pd.Pd;
import net.mgsx.rainstick.components.Resonator;

public class ResonatorPhysicSystem extends IteratingSystem
{
	private int impacts;
	private float velocity = 0;
	
	public ResonatorPhysicSystem() {
		super(Family.all(Resonator.class, Box2DBodyModel.class).get(), GamePipeline.AFTER_PHYSICS);
	}

	@Override
	public void addedToEngine(Engine engine) 
	{
		super.addedToEngine(engine);
		
		engine.addEntityListener(getFamily(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void entityAdded(Entity entity) {
				
				Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
				physics.setListener(new Box2DEntityListener(){
					@Override
					protected void preSolve(Contact contact, Fixture self, Fixture other, Entity otherEntity,
							Manifold oldManifold) {
						
						
						float v = self.getBody().getLinearVelocity().cpy().sub(other.getBody().getLinearVelocity()).len();
						if(v > 1000){
							velocity += v;
							impacts++;
							
						}
					}
					@Override
					public void beginContact(Contact contact, Fixture self, Fixture other) {
						float v = self.getBody().getLinearVelocity().cpy().sub(other.getBody().getLinearVelocity()).len();
						if(v > 1){
							velocity += v;
							impacts++;
							
						}
					}
				});
			}
		});
	}
	
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		if(impacts > 0)
		{
			float velocity = this.velocity / impacts;
			
			Pd.audio.sendFloat("impact", velocity);
		}
		this.velocity = 0;
		impacts = 0;
	}

	
}

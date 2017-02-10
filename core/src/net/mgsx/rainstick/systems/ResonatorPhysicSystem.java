package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableSystem;
import net.mgsx.game.core.annotations.Storable;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.game.plugins.box2d.listeners.Box2DEntityListener;
import net.mgsx.pd.Pd;
import net.mgsx.rainstick.components.Ball;
import net.mgsx.rainstick.components.ImpactComponent;
import net.mgsx.rainstick.components.Resonator;

@Storable("rainstick.resonator-system")
@EditableSystem
public class ResonatorPhysicSystem extends IteratingSystem
{
	private static class Impact implements Comparable<Impact>{

		public float mass;
		public int material;
		public float velocity;
		public float x;
		public Vector2 position = new Vector2();
		
		@Override
		public int compareTo(Impact i) {
			return Float.compare(i.velocity, velocity);
		}
	}
	
	@Editable
	public int stereoClusteringSteps = 2;
	@Editable
	public float stereoMin = -5;
	@Editable
	public float stereoMax = 5;
	
	@Editable
	public float massMin = 1, massMax = 10;
	@Editable
	public float velMin = 1, velMax = 10;
	
	private Array<Array<Impact>> impacts = new Array<Array<Impact>>();
	private Pool<Impact> pool = new Pool<Impact>(){
		@Override
		protected Impact newObject() {
			return new Impact();
		}
	};
	protected GameScreen game;
	
	public ResonatorPhysicSystem(GameScreen game) {
		super(Family.all(Ball.class, Box2DBodyModel.class).get(), GamePipeline.AFTER_PHYSICS);
		this.game = game;
	}
	
	private ImmutableArray<Entity> balls;

	@Override
	public void addedToEngine(Engine engine) 
	{
		balls = engine.getEntitiesFor(getFamily());
		// TODO dispose when finished !
		Pd.audio.open(Gdx.files.internal("pd/engine2.pd"));
		
		super.addedToEngine(engine);
		
		engine.addEntityListener(getFamily(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void entityAdded(Entity entity) {
				
				final Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
				physics.setListener(new Box2DEntityListener(){
					@Override
					public void beginContact(Contact contact, Fixture self, Fixture other) {
						float v = self.getBody().getLinearVelocity().cpy().sub(other.getBody().getLinearVelocity()).len();
						if(v > velMin){
							Impact i = pool.obtain();
							i.x = MathUtils.clamp((self.getBody().getPosition().x - stereoMin) / (stereoMax - stereoMin), 0, 1);
							i.velocity = MathUtils.clamp((v - velMin) / (velMax - velMin), 0, 1);
							Resonator resonator = Resonator.components.get((Entity)other.getBody().getUserData());
							i.mass = (self.getBody().getMassData().mass - massMin) / (massMax - massMin);
							if(resonator == null){
								i.material = 0;
							}else{
								i.material = resonator.material;
							}
							i.position.set(contact.getWorldManifold().getPoints()[0]);
							int index = MathUtils.clamp(Math.round(i.x * stereoClusteringSteps), 0, stereoClusteringSteps-1);
							while(impacts.size <= index) impacts.add(new Array<Impact>());
							impacts.get(index).add(i);
						}
					}
				});
			}
		});
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		for(Array<Impact> a : impacts){
			if(a.size > 0){
				a.sort();
				for(int j=0 ; j<2 && j<a.size ; j++){
					Impact i = a.get(j);
					
					// TODO sendList doesn't work ...
					Pd.audio.sendMessage("impact", "impact", i.material, i.mass, i.velocity, i.x);
					
					// create particles
					createParticle(i.position, i.mass * i.velocity * i.velocity, i.material);
					
				}
			}
		}
			
		for(Array<Impact> a : impacts){
			pool.freeAll(a);
			a.clear();
		}
		
		// TODO cluster by friction ? mass ... surface
		float totalForce = 0;
		float totalVel = 0;
		int total = 0;
		Vector2 position = new Vector2();
		for(Entity entity : balls)
		{
			Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
			float vel = physics.body.getLinearVelocity().len();
			float force = physics.body.getMass();
			
			totalVel += vel * force;
			totalForce += force;
			position.add(physics.body.getPosition().scl(1));
			total++;
		}
		Pd.audio.sendMessage("ambient", "ambient", 
				(totalVel / total) / totalForce, 
				position.x / total, 
				position.y/total);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		
	}

	private void createParticle(Vector2 position, float energy, int material) 
	{
		Entity entity = getEngine().createEntity();
		
		ImpactComponent ic = getEngine().createComponent(ImpactComponent.class);
		
		ic.position.set(position);
		ic.life = 0;
		ic.energy = energy;
		ic.material = material;
		
		entity.add(ic);
		
		getEngine().addEntity(entity);
	}

	
}

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
import net.mgsx.pd.patch.PdPatch;
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
	
	@Editable
	public float f1 = 359, f2= 426,f3= 1748,f4= 3000 , q=1000, gain = 10 ;
	
	@Editable
	public void sendFormants (){
		Pd.audio.sendList("kit-formants", f1,f2,f3,f4,q,gain);
	}
	
	@Editable
	public float energyMax = 16;
	
	private Array<Array<Impact>> impacts = new Array<Array<Impact>>();
	private Pool<Impact> pool = new Pool<Impact>(){
		@Override
		protected Impact newObject() {
			return new Impact();
		}
	};
	protected GameScreen game;
	
	private transient PdPatch patch;
	
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
		// TODO use assets injection instead ...
		patch = Pd.audio.open(Gdx.files.internal("pd/engine.pd"));
		sendFormants();
		
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
	public void removedFromEngine(Engine engine) {
		patch.dispose();
		super.removedFromEngine(engine);
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
		
		// aggregate total friction on back plane : send energy and position.
		float totalEnergy = 0;
		int total = 0;
		float position = 0;
		for(Entity entity : balls)
		{
			Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
			
			float mass = physics.body.getMass();
			mass = MathUtils.clamp((mass - massMin) / (massMax - massMin), 0, 1);
			
			float vel = physics.body.getLinearVelocity().len();
			vel = MathUtils.clamp((vel - velMin) / (velMax - velMin), 0, 1);
			
			float pos = MathUtils.clamp((physics.body.getPosition().x - stereoMin) / (stereoMax - stereoMin), 0, 1);
			
			float energy = vel * vel * mass;
			totalEnergy += energy;
			position += pos;
			total++;
		}
		Pd.audio.sendMessage("ambient", "ambient", 
				totalEnergy / energyMax, 
				totalEnergy > 0 ? (position / total) : 0, 0); // FIXME add zero to workaround OSC padding bug
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

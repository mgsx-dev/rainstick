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
import net.mgsx.game.core.storage.SystemSettingsListener;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.game.plugins.box2d.listeners.Box2DEntityListener;
import net.mgsx.pd.Pd;
import net.mgsx.pd.patch.PdPatch;
import net.mgsx.rainstick.components.Ball;
import net.mgsx.rainstick.components.ImpactComponent;
import net.mgsx.rainstick.components.Resonator;

@Storable("rainstick.resonator-system")
@EditableSystem
public class ResonatorPhysicSystem extends IteratingSystem implements SystemSettingsListener
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
	public float energyMax = 16;
	
	@Editable
	public float pitch = 48, tone= 0,resonance= 250 ;
	@Editable
	public float mix_ambiant_dry = 20, mix_ambiant_wet= 32,mix_ball_dry= 27 , mix_ball_wet = 70, mix_wall_dry = 25, mix_wall_wet = 80;
	
	// XXX test sendList VS sendFloat performances
	private static boolean sendSeparate = false;
	
	@Override
	public void onSettingsLoaded() {
		sendFormants();
		sendMix();
	}
	
	@Editable
	public void sendFormants ()
	{
		if(sendSeparate){
			Pd.audio.sendFloat("kit-freq", pitch);
			Pd.audio.sendFloat("kit-tone", tone);
			Pd.audio.sendFloat("kit-resonance", resonance);
		}else{
			Pd.audio.sendList("kit-params", pitch, tone, resonance);
		}
	}
	@Editable
	public void sendMix(){
		
		if(sendSeparate){
			Pd.audio.sendFloat("kit-mixa-dry", mix_ambiant_dry);
			Pd.audio.sendFloat("kit-mixa-wet", mix_ambiant_wet);
	
			Pd.audio.sendFloat("kit-mixb-dry", mix_ball_dry);
			Pd.audio.sendFloat("kit-mixb-wet", mix_ball_wet);
	
			Pd.audio.sendFloat("kit-mixw-dry", mix_wall_dry);
			Pd.audio.sendFloat("kit-mixw-wet", mix_wall_wet);
		}else{
			Pd.audio.sendList("kit-mix", mix_ambiant_dry, mix_ambiant_wet, mix_ball_dry, mix_ball_wet, mix_wall_dry, mix_wall_wet);		
		}
	}
	
	
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
		
		super.addedToEngine(engine);
		
		engine.addEntityListener(getFamily(), new EntityListener() {
			
			private final Vector2 velCache = new Vector2();
			
			@Override
			public void entityRemoved(Entity entity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void entityAdded(Entity entity) {
				
				final Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
				final Ball ball = Ball.components.get(entity);
				
				physics.setListener(new Box2DEntityListener(){
					@Override
					public void beginContact(Contact contact, Fixture self, Fixture other) {
						float vel2 = velCache.set(self.getBody().getLinearVelocity()).sub(other.getBody().getLinearVelocity()).len2();
						if(vel2 > velMin * velMin){
							float v = (float)Math.sqrt(vel2);
							Impact i = pool.obtain();
							i.velocity = MathUtils.clamp((v - velMin) / (velMax - velMin), 0, 1);
							Resonator resonator = Resonator.components.get((Entity)other.getBody().getUserData());
							i.mass = (ball.mass - massMin) / (massMax - massMin);
							if(resonator == null){
								i.material = 0;
							}else{
								i.material = resonator.material;
							}
							i.position.set(contact.getWorldManifold().getPoints()[0]);
							i.x = MathUtils.clamp((i.position.x - stereoMin) / (stereoMax - stereoMin), 0, 1);
							
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
	
	private boolean autogravity = false;
	@Override
	public void update(float deltaTime) {
		
		// TODO inject system instead of querying it
		// no comments
		GravityAutoSystem gas = getEngine().getSystem(GravityAutoSystem.class);
		int activeTouch = 0;
		for (int i = 0; i < 20; i++) { // TODO limit to 6
			if (Gdx.input.isTouched(i)) activeTouch++;
		}
		if(activeTouch == 5 ){
			if(autogravity == false){
				autogravity = true;
				gas.setProcessing(!gas.checkProcessing());
			}
		}	
		else{
			autogravity = false;
		}
		// end no comments
		
		// map user input to tone and resonance
		if ( Gdx.input.isTouched()){
			float dynTone = ((float)Gdx.input.getX() / Gdx.graphics.getWidth());
			float dynResonance = ((float)Gdx.input.getY() / Gdx.graphics.getHeight())*1500 + 150;
			Pd.audio.sendFloat("kit-tone", dynTone);
			Pd.audio.sendFloat("kit-resonance", dynResonance);
		}
		
		for(Array<Impact> a : impacts){
			if(a.size > 0){
				a.sort();
				for(int j=0 ; j<2 && j<a.size ; j++){
					Impact i = a.get(j);
					
					Pd.audio.sendList("impact", i.material, i.mass, i.velocity, i.x);
					
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
			Ball ball = Ball.components.get(entity);
			
			float vel = ball.velocity.len();
			if(vel < velMin) continue;
			vel = MathUtils.clamp((vel - velMin) / (velMax - velMin), 0, 1);
			
			
			// TODO mass could be cached to limit jniCalls
			float mass = ball.mass;
			mass = MathUtils.clamp((mass - massMin) / (massMax - massMin), 0, 1);
			
			// TODO position could be cached to avoid jniCalls
			float pos = MathUtils.clamp((ball.position.x - stereoMin) / (stereoMax - stereoMin), 0, 1);
			
			float energy = vel * vel * mass;
			totalEnergy += energy;
			position += pos;
			total++;
		}
		Pd.audio.sendList("ambient", 
				totalEnergy / energyMax, 
				totalEnergy > 0 ? (position / total) : 0, 0);
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

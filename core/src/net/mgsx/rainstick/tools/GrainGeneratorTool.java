package net.mgsx.rainstick.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.GdxRuntimeException;

import net.mgsx.game.core.EditorScreen;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.components.Repository;
import net.mgsx.game.core.tools.RectangleTool;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.game.plugins.box2d.components.Box2DFixtureModel;
import net.mgsx.rainstick.components.Ball;
import net.mgsx.rainstick.components.InvertMask;
import net.mgsx.rainstick.components.Mask;

@Editable
public class GrainGeneratorTool extends RectangleTool
{
	public static enum Type{
		Circle, Triangle, Square, Pentagon, Hexagon, Septagon, Octogon, Random
	}
	
	@Editable
	public boolean persisted = false;
	
	@Editable
	public int count = 2;
	
	@Editable
	public FixtureDef fix = new FixtureDef();
	
	@Editable
	public Type geometry = Type.Circle;
	
	@Editable
	public float radius = 1;
	
	public GrainGeneratorTool(EditorScreen editor) {
		super("Rainstick - Grains", editor);
		fix.density = 1;
	}

	@Override
	protected void create(Vector2 startPoint, Vector2 endPoint) 
	{
		float w = endPoint.x - startPoint.x;
		float h = endPoint.y - startPoint.y;
		for(int i=0 ; i < count ; i++){
			for(int j=0 ; j < count ; j++){
				Vector2 p = new Vector2(w*j/(float)(count), h*i/(float)(count)).add(startPoint);
				createParticle(p);
			}
		}
		
	}

	private void createParticle(Vector2 p) 
	{
		Entity entity = getEngine().createEntity();
		
		Box2DBodyModel physics = getEngine().createComponent(Box2DBodyModel.class);
		physics.def = new BodyDef();
		physics.def.position.set(p);
		physics.def.type = BodyType.DynamicBody;
		physics.def.allowSleep = false;
		
		Shape shape;
		
		if(geometry == Type.Circle)
		{
			CircleShape circle = new CircleShape();
			circle.setRadius(this.radius);
			
			shape = circle;
		}
		else
		{
			int res;
			switch(geometry){
			case Triangle: res = 3;	break;
			case Square: res = 4; break;
			case Pentagon: res = 5; break;
			case Hexagon: res = 6; break;
			case Septagon: res = 7; break;
			case Octogon: res = 8; break;
			case Random: res = MathUtils.random(3,  8); break;
			default:
				// shouldn't occurs
				throw new GdxRuntimeException("resolution not supported");
			}
			
			PolygonShape polygon = new PolygonShape();
			Vector2[] vertices = new Vector2[res];;
			for(int i=0 ; i<res ; i++){
				
				float x = MathUtils.cos(MathUtils.PI2 * i/(float)(res)) * radius;
				float y = MathUtils.sin(MathUtils.PI2 * i/(float)(res)) * radius;
				
				vertices[i] = new Vector2(x, y);
			}
			polygon.set(vertices);
			
			shape = polygon;
		}
		
		Box2DFixtureModel fix = new Box2DFixtureModel();
		fix.def = new FixtureDef();
		fix.def.density = this.fix.density ;
		fix.def.friction = this.fix.friction;
		fix.def.restitution = this.fix.restitution;
		fix.def.shape = shape;
		
		
		physics.fixtures.add(fix );
		
		entity.add(physics);
		
		Ball ball = getEngine().createComponent(Ball.class);
		ball.radius = radius;
		
		entity.add(ball);
	
		Mask mask = getEngine().createComponent(Mask.class);
		entity.add(mask);
		entity.add(getEngine().createComponent(InvertMask.class));
		
		if(persisted){
			entity.add(getEngine().createComponent(Repository.class));
		}
		
		getEngine().addEntity(entity);
	}

}

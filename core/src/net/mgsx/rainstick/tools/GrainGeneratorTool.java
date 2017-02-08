package net.mgsx.rainstick.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.mgsx.game.core.EditorScreen;
import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.tools.RectangleTool;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.game.plugins.box2d.components.Box2DFixtureModel;
import net.mgsx.rainstick.components.Ball;
import net.mgsx.rainstick.components.InvertMask;
import net.mgsx.rainstick.components.Mask;

@Editable
public class GrainGeneratorTool extends RectangleTool
{

	@Editable
	public int count = 10;
	
	@Editable
	public FixtureDef fix = new FixtureDef();

	@Editable
	public int resolution = 6;
	
	@Editable
	public float radius = 1;
	
	public GrainGeneratorTool(EditorScreen editor) {
		super("Rainstick - Grains", editor);
	}

	@Override
	protected void create(Vector2 startPoint, Vector2 endPoint) 
	{
		float w = endPoint.x - startPoint.x;
		float h = endPoint.y - startPoint.y;
		for(int i=0 ; i < count ; i++){
			for(int j=0 ; j < count ; j++){
				Vector2 p = new Vector2(w*j/(float)(count-1), h*i/(float)(count-1)).add(startPoint);
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
		
		PolygonShape shape = new PolygonShape();
		
		int res = MathUtils.random(3,  7);
		Vector2[] vertices = new Vector2[res];;
		for(int i=0 ; i<res ; i++){
			
			float x = MathUtils.cos(MathUtils.PI2 * i/(float)(res)) * radius;
			float y = MathUtils.sin(MathUtils.PI2 * i/(float)(res)) * radius;
			
			vertices[i] = new Vector2(x, y);
		}
		shape.set(vertices );
		
//		CircleShape shape = new CircleShape();
//		shape.setRadius(this.radius);
		
		Box2DFixtureModel fix = new Box2DFixtureModel();
		fix.def = new FixtureDef();
		fix.def.density = this.fix.density ;
		fix.def.friction = this.fix.friction;
		fix.def.restitution = this.fix.restitution;
		fix.def.shape = shape;
		
		
		physics.fixtures.add(fix );
		
		entity.add(physics);
		
		Ball reso = getEngine().createComponent(Ball.class);
		
		entity.add(reso);
	
		// TODO ??
		Mask mask = getEngine().createComponent(Mask.class);
		entity.add(mask);
		entity.add(getEngine().createComponent(InvertMask.class));
		// Transform2DComponent transform = getEngine().createComponent(Transform2DComponent.class);
		//entity.add(transform);
		
		getEngine().addEntity(entity);
	}

}

package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.ShortArray;

import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.game.plugins.box2d.components.Box2DFixtureModel;
import net.mgsx.rainstick.components.InvertMask;
import net.mgsx.rainstick.components.Mask;

public class PolygonMaskRenderer extends EntitySystem
{
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(Family.all(Mask.class, Box2DBodyModel.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
			}
			
			@Override
			public void entityAdded(Entity entity) {
				Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
				Mask mask = Mask.components.get(entity);
				
				ModelBuilder mb = new ModelBuilder();
				mb.begin();
				
				Vector2 v = new Vector2();
				for(Box2DFixtureModel fix : physics.fixtures){
					
					int count = 0;
					float [] delPoints = null;
					if(fix.def.shape instanceof PolygonShape){
						PolygonShape poly = (PolygonShape)fix.def.shape;
						count = poly.getVertexCount();
						delPoints = new float[2 * count];
						for(int i=0 ; i<count ; i++){
							poly.getVertex(i, v);
							delPoints[i*2+0] = v.x;
							delPoints[i*2+1] = v.y;
						}
					}
					else if(fix.def.shape instanceof ChainShape){
						ChainShape chain = (ChainShape)fix.def.shape;
						count = chain.getVertexCount();
						delPoints = new float[2 * count];
						for(int i=0 ; i<count ; i++){
							chain.getVertex(i, v);
							delPoints[i*2+0] = v.x;
							delPoints[i*2+1] = v.y;
						}
					}
					else if(fix.def.shape instanceof CircleShape){
						CircleShape c = (CircleShape)fix.def.shape;
						count = 16;
						delPoints = new float[2 * count];
						for(int i=0 ; i<count ; i++){
							//c.getVertex(i, v);
							float t = i / 16f;
							float x = c.getRadius() * MathUtils.cos(t * MathUtils.PI2);
							float y = c.getRadius() * MathUtils.sin(t * MathUtils.PI2);
							delPoints[i*2+0] = x;
							delPoints[i*2+1] = y;
						}
					}
					if(delPoints != null)
					{
						EarClippingTriangulator ect = new EarClippingTriangulator();
						ShortArray indices = ect.computeTriangles(delPoints);
						
						float [] points = new float[3 * count];
						for(int i=0 ; i<count ; i++){
							points[i*3+0] = delPoints[i*2+0];
							points[i*3+1] = delPoints[i*2+1];
							points[i*3+2] = 0;
						}
						
						Mesh mesh = new Mesh(true, count, indices.size, VertexAttribute.Position());
						mesh.setIndices(indices.shrink());
						mesh.setVertices(points);
						
						float depth = InvertMask.components.has(entity) ? 0 : 1;
						
						mb.part("a", mesh, GL20.GL_TRIANGLES, new Material(
								ColorAttribute.createDiffuse(Color.WHITE),
								new DepthTestAttribute(GL20.GL_ALWAYS, depth, depth, true)
								));
						
					}
				}
				mask.modelInstance = new ModelInstance(mb.end());
				
			}
		});
	}
	
}

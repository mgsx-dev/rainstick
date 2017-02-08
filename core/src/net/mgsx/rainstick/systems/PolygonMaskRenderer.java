package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.ShortArray;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.game.plugins.box2d.components.Box2DFixtureModel;
import net.mgsx.rainstick.components.Mask;

public class PolygonMaskRenderer extends IteratingSystem
{
	private ModelBatch batch;
	private GameScreen game;
	public PolygonMaskRenderer(GameScreen game) {
		super(Family.all(Mask.class, Box2DBodyModel.class).get(), GamePipeline.RENDER);
		this.game = game;
		batch = new ModelBatch();
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(getFamily(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
			}
			
			@Override
			public void entityAdded(Entity entity) {
				Box2DBodyModel physics = Box2DBodyModel.components.get(entity);
				Mask mask = Mask.components.get(entity);
				ModelCache cache = new ModelCache();
				
				
				Vector2 pos = new Vector2(physics.body.getPosition());
				
				mask.mesh = cache;
				cache.begin();
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
							delPoints[i*2+0] = v.x + pos.x;
							delPoints[i*2+1] = v.y + pos.y;
						}
					}
					else if(fix.def.shape instanceof ChainShape){
						ChainShape chain = (ChainShape)fix.def.shape;
						count = chain.getVertexCount();
						delPoints = new float[2 * count];
						for(int i=0 ; i<count ; i++){
							chain.getVertex(i, v);
							delPoints[i*2+0] = v.x + pos.x;
							delPoints[i*2+1] = v.y + pos.y;
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
						
						Renderable r = new Renderable();
						r.meshPart.set("a", mesh, 0, indices.size, GL20.GL_TRIANGLES);
						r.material = new Material(ColorAttribute.createDiffuse(Color.WHITE));
						cache.add(r);
						
						
					}
				}
				cache.end();
				
			}
		});
	}
	
	@Override
	public void update(float deltaTime) {
		batch.begin(game.camera);
		Gdx.gl.glFrontFace(GL20.GL_CW);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		super.update(deltaTime);
		batch.end();
		
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Mask mask = Mask.components.get(entity);
		batch.render(mask.mesh);
	}
}
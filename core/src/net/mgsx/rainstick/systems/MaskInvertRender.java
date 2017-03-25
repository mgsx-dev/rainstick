package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.rainstick.components.InvertMask;
import net.mgsx.rainstick.components.Mask;

public class MaskInvertRender extends AbstractMaskRenderer
{
	private GameScreen game;
	
	public MaskInvertRender(GameScreen game) {
		super(Family.all(Mask.class, Box2DBodyModel.class, InvertMask.class).get(), GamePipeline.RENDER -1);
		this.game = game;
	}
	
	@Override
	public void update(float deltaTime) {
		batch.begin(game.camera);
		Gdx.gl.glFrontFace(GL20.GL_CW);
		
		Gdx.gl.glColorMask(false, false, false, false); // XXX debug here
		super.update(deltaTime);
		batch.end();
		
		Gdx.gl.glColorMask(true, true, true, true);
		
		// restore settings after invert masks having special depth properties.
		Gdx.gl.glDepthRangef(0, 1); 
	}
	
}

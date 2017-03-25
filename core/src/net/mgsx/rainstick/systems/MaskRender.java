package net.mgsx.rainstick.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import net.mgsx.game.core.GamePipeline;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.plugins.box2d.components.Box2DBodyModel;
import net.mgsx.rainstick.components.InvertMask;
import net.mgsx.rainstick.components.Mask;

public class MaskRender extends AbstractMaskRenderer
{
	private GameScreen game;
	
	public MaskRender(GameScreen game) {
		super(Family.all(Mask.class, Box2DBodyModel.class).exclude(InvertMask.class).get(), GamePipeline.RENDER -1);
		this.game = game;
	}
	
	@Override
	public void update(float deltaTime) {
		batch.begin(game.camera);
		Gdx.gl.glFrontFace(GL20.GL_CW);
		
		Gdx.gl.glClearDepthf(0);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glColorMask(false, false, false, false);
		super.update(deltaTime);
		batch.end();
		
		Gdx.gl.glClearDepthf(1); // restore to default
		Gdx.gl.glColorMask(true, true, true, true);
	}
	
}

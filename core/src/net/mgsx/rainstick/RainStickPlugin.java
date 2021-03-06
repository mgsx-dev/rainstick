package net.mgsx.rainstick;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.annotations.PluginDef;
import net.mgsx.game.core.plugins.Plugin;
import net.mgsx.game.plugins.DefaultPlugin;
import net.mgsx.rainstick.components.Ball;
import net.mgsx.rainstick.components.ImpactComponent;
import net.mgsx.rainstick.components.InvertMask;
import net.mgsx.rainstick.components.Mask;
import net.mgsx.rainstick.components.Resonator;
import net.mgsx.rainstick.systems.BackgroundSystem;
import net.mgsx.rainstick.systems.BallOutlineRender;
import net.mgsx.rainstick.systems.BallPhysicCacheSystem;
import net.mgsx.rainstick.systems.GravityAutoSystem;
import net.mgsx.rainstick.systems.GyroSystem;
import net.mgsx.rainstick.systems.ImpactRender;
import net.mgsx.rainstick.systems.ImpactUpdate;
import net.mgsx.rainstick.systems.MaskInvertRender;
import net.mgsx.rainstick.systems.MaskRender;
import net.mgsx.rainstick.systems.PolygonMaskRenderer;
import net.mgsx.rainstick.systems.ResonatorPhysicSystem;

@PluginDef(components={Ball.class, ImpactComponent.class, InvertMask.class, Mask.class, Resonator.class})
public class RainStickPlugin implements Plugin, DefaultPlugin
{

	@Override
	public void initialize(GameScreen engine) 
	{
		engine.assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(engine.assets.getFileHandleResolver()));
		engine.assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(engine.assets.getFileHandleResolver()));
		
		engine.entityEngine.addSystem(new BallPhysicCacheSystem());
		engine.entityEngine.addSystem(new ResonatorPhysicSystem(engine));
		
		engine.entityEngine.addSystem(new ImpactUpdate());
		
		
		engine.entityEngine.addSystem(new MaskRender(engine));
		engine.entityEngine.addSystem(new MaskInvertRender(engine));
		engine.entityEngine.addSystem(new PolygonMaskRenderer());
		engine.entityEngine.addSystem(new BackgroundSystem(engine));
		engine.entityEngine.addSystem(new BallOutlineRender(engine));
		engine.entityEngine.addSystem(new ImpactRender(engine));
		
		engine.entityEngine.addSystem(new GravityAutoSystem());

		engine.entityEngine.addSystem(new GyroSystem());
	}

}

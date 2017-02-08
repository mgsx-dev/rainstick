package net.mgsx.rainstick;

import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.plugins.Plugin;
import net.mgsx.game.plugins.DefaultPlugin;
import net.mgsx.rainstick.systems.BackgroundSystem;
import net.mgsx.rainstick.systems.MaskInvertRender;
import net.mgsx.rainstick.systems.MaskRender;
import net.mgsx.rainstick.systems.PolygonMaskRenderer;
import net.mgsx.rainstick.systems.ResonatorPhysicSystem;

public class RainStickPlugin implements Plugin, DefaultPlugin
{

	@Override
	public void initialize(GameScreen engine) 
	{
		engine.entityEngine.addSystem(new ResonatorPhysicSystem());
		
		engine.entityEngine.addSystem(new MaskRender(engine));
		engine.entityEngine.addSystem(new MaskInvertRender(engine));
		engine.entityEngine.addSystem(new PolygonMaskRenderer(engine));
		engine.entityEngine.addSystem(new BackgroundSystem(engine));
	}

}

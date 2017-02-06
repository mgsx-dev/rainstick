package net.mgsx.rainstick;

import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.plugins.Plugin;
import net.mgsx.game.plugins.DefaultPlugin;
import net.mgsx.rainstick.systems.ResonatorPhysicSystem;

public class RainStickPlugin implements Plugin, DefaultPlugin
{

	@Override
	public void initialize(GameScreen engine) 
	{
		engine.entityEngine.addSystem(new ResonatorPhysicSystem());
	}

}

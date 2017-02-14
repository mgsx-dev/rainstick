package net.mgsx.rainstick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import net.mgsx.game.core.GameApplication;
import net.mgsx.game.core.GameRegistry;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.screen.ScreenClip;
import net.mgsx.game.core.screen.Transitions;

/**
 * Rainstick application is made of 2 screens : 
 * - a loading screen displaying some credits and branding info.
 * - the rainstick screen
 * 
 * @author mgsx
 *
 */
public class RainstickApplication extends GameApplication
{
	@Override
	public void create() 
	{
		super.create();
		
		// create registry based on RainstickPlugin configuration
		GameRegistry registry = new GameRegistry();
		registry.registerPlugin(new RainStickPlugin());
		
		// create default rainstick screen (loading default rainstick data)
		GameScreen rainstickScreen = new GameScreen(assets, registry);
		
		rainstickScreen.loadSettings(Gdx.files.internal("settings.json"));
		rainstickScreen.load(Gdx.files.internal("rainstick-default.json"));
		
		// TODO create a true splash screen with some branding information.
		ScreenClip splashScreen = Transitions.empty(Color.WHITE);
		
		// configure default loading screen with splash screen
		setDefaultLoadingScreen(Transitions.loader(assets, splashScreen));
		
		// add transition (fade) from loading screen to rainstickScreen
		// loading screen is splash screen with minimum timeout of 2 seconds.
		setScreen(Transitions.loader(assets, Transitions.timeout(splashScreen, 2)));
		addTransition(Transitions.fade(rainstickScreen, 0.3f));
	}
}

package net.mgsx.rainstick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import net.mgsx.game.core.GameApplication;
import net.mgsx.game.core.GameRegistry;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.screen.Transitions;
import net.mgsx.rainstick.model.Rainstick;
import net.mgsx.rainstick.screens.RainstickIntroScreen;
import net.mgsx.rainstick.screens.RainstickSelectorScreen;
import net.mgsx.rainstick.screens.RainstickSplashScreen;

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
	private RainstickSplashScreen splashScreen;
	private RainstickSelectorScreen selectorScreen;
	private RainstickIntroScreen introScreen;
	
	@Override
	public void create() 
	{
		super.create();
		
		// TODO register loader where ?
		assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assets.getFileHandleResolver()));
		assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(assets.getFileHandleResolver()));

		
		// We force asset loading for splash screen.
		splashScreen = new RainstickSplashScreen(assets);
		assets.finishLoading();
		
		selectorScreen = new RainstickSelectorScreen(this, assets);
		introScreen = new RainstickIntroScreen(assets);
		
		// configure default loading screen with splash screen
		setDefaultLoadingScreen(Transitions.loader(assets, splashScreen));
		
		// add transition (fade) from loading screen to rainstickScreen
		// loading screen is splash screen with minimum timeout of 2 seconds.
		setScreen(Transitions.loader(assets, Transitions.timeout(splashScreen, 2)));
		addTransition(Transitions.fade(Transitions.timeout(selectorScreen, 2), 2.3f));
	}
	
	public void showRainstick(Rainstick rainstick)
	{
		introScreen.setRainstick(rainstick);
		
		// create registry based on RainstickPlugin configuration
		GameRegistry registry = new GameRegistry();
		registry.registerPlugin(new RainStickPlugin());
		// create default rainstick screen (loading default rainstick data)
		GameScreen rainstickScreen = new GameScreen(this, assets, registry){
			@Override
			public void render(float delta) {
				super.render(delta);
				if(Gdx.input.isKeyPressed(Input.Keys.BACK)) showMenu();
			}
			@Override
			public void hide() {
				// TODO free all (dispose)
				super.hide();
				dispose();
			}
		};
				
		rainstickScreen.load(Gdx.files.internal(rainstick.path));
		
		setTransition(Transitions.fade(Transitions.loader(assets, Transitions.timeout(introScreen, 2)), 1.5f));
		addTransition(Transitions.fade(rainstickScreen, 2.3f));
	}
	
	public void showMenu()
	{
		setTransition(Transitions.fade(Transitions.empty(Color.WHITE), 5.5f));
		addTransition(Transitions.fade(selectorScreen, 1));
	}
}

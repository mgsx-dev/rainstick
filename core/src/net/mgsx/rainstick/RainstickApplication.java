package net.mgsx.rainstick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import net.mgsx.game.core.GameApplication;
import net.mgsx.game.core.GameRegistry;
import net.mgsx.game.core.GameScreen;
import net.mgsx.game.core.screen.Transitions;
import net.mgsx.pd.Pd;
import net.mgsx.pd.PdConfiguration;
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
	public static boolean debugFPS = false;
	
	public static final float ViewportWorldWidth = 480;
	public static final float ViewportWorldHeight = 640;
	
	private RainstickSplashScreen splashScreen;
	private RainstickSelectorScreen selectorScreen;
	private RainstickIntroScreen introScreen;
	
	@Override
	public void create() 
	{
		super.create();
		
		
		// enable libgdx to catch back key
		Gdx.input.setCatchBackKey(true);
		
		Pd.audio.create(new PdConfiguration());
		
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
		setScreen(Transitions.loader(assets, Transitions.timeout(splashScreen, 3)));
		addTransition(Transitions.fade(Transitions.timeout(selectorScreen, 2), 0.85f));
	}
	
	public void showRainstick(Rainstick rainstick)
	{
		introScreen.setRainstick(rainstick);
		
		// create registry based on RainstickPlugin configuration
		GameRegistry registry = new GameRegistry();
		registry.registerPlugin(new RainStickPlugin());
		// create default rainstick screen (loading default rainstick data)
		final FPSLogger logger = new FPSLogger();
		
		GameScreen rainstickScreen = new GameScreen(this, assets, registry){
			private boolean exiting = false;
			private boolean entering = true;
			
			@Override
			public void postShow() {
				super.postShow();
				entering = false;
			}
			
			@Override
			public void render(float delta) {
				super.render(delta);
				if(Gdx.input.isKeyPressed(Input.Keys.BACK) && !exiting && !entering){
					exiting = true;
					showMenu();
				}
				if(debugFPS){
					logger.log();
				}
			}
			@Override
			public void hide() {
				super.hide();
				dispose();
			}
		};
				
		rainstickScreen.load(Gdx.files.internal(rainstick.path));
		
		setTransition(Transitions.fade(Transitions.loader(assets, Transitions.timeout(Transitions.touch(introScreen), 10)), 0.85f));
		addTransition(Transitions.fade(rainstickScreen, 2.0f));
	}
	
	public void showMenu()
	{
		setTransition(Transitions.fade(Transitions.empty(Color.WHITE), 0.3f));
		addTransition(Transitions.fade(selectorScreen, 0.3f));
		
	}
	
	@Override
	public void pause() {
		Pd.audio.pause();
		super.pause();
	}
	
	@Override
	public void resume() {
		super.resume();
		Pd.audio.resume();
	}
}

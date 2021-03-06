package net.mgsx.rainstick.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.rainstick.RainstickApplication;
import net.mgsx.rainstick.model.Rainstick;
import net.mgsx.rainstick.screens.RainstickIntroScreen;

public class RainstickIntroTest {

	public static void main (String[] args) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 640;
		new LwjglApplication(new RainstickApplication(){
			@Override
			public void create() {
				super.create();
				Rainstick rainstick = new Rainstick();
				rainstick.title = "Winter Quadratics";
				rainstick.credits = "by DarkAng3l";
				rainstick.description = "This is a placeholder for a short description.\nOr some hints about how it works...\nOr better something poetic about it.";
				RainstickIntroScreen screen = new RainstickIntroScreen(assets);
				assets.finishLoading();
				screen.setRainstick(rainstick);
				setScreen(screen);
			}
		}, config);
	}
}

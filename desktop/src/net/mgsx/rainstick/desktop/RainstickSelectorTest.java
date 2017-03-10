package net.mgsx.rainstick.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.rainstick.RainstickApplication;
import net.mgsx.rainstick.screens.RainstickSelectorScreen;

public class RainstickSelectorTest {

	public static void main (String[] args) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 640;
		new LwjglApplication(new RainstickApplication(){
			@Override
			public void create() {
				super.create();
				RainstickSelectorScreen screen = new RainstickSelectorScreen(this, assets);
				assets.finishLoading();
				setScreen(screen);
			}
		}, config);
	}
}

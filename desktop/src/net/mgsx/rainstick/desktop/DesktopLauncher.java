package net.mgsx.rainstick.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.gdx.pd.PdAudioOpenAL;
import net.mgsx.pd.Pd;
import net.mgsx.pd.PdConfiguration;
import net.mgsx.pd.midi.DefaultPdMidi;
import net.mgsx.rainstick.RainstickApplication;

public class DesktopLauncher 
{
	public static void main (String[] args) 
	{
		Pd.audio = new PdAudioOpenAL();
		Pd.midi = new DefaultPdMidi();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 640;
		new LwjglApplication(new RainstickApplication(){
			@Override
			public void create() {
				Pd.audio.create(new PdConfiguration());
				super.create();
			}
		}, config);
	}
}

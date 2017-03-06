package net.mgsx.rainstick;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;
import net.mgsx.pd.Pd;
import net.mgsx.pd.PdAudioAndroid;
import net.mgsx.pd.PdConfiguration;
import net.mgsx.pd.midi.DefaultPdMidi;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		Pd.audio = new PdAudioAndroid(this);
		Pd.midi = new DefaultPdMidi();
		
		initialize(new RainstickApplication(){
			@Override
			public void create() {
				super.create();
				Pd.audio.create(new PdConfiguration());
			}
		}, config);
	}
}

package net.mgsx.rainstick;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;
import net.mgsx.game.core.EditorApplication;
import net.mgsx.game.core.EditorConfiguration;
import net.mgsx.pd.Pd;
import net.mgsx.pd.PdAudioAndroid;
import net.mgsx.pd.PdConfiguration;

public class AndroidEditorLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		Pd.audio = new PdAudioAndroid(this);
		
		EditorConfiguration editConfig = new EditorConfiguration();
		editConfig.plugins.add(new RainStickEditorPlugin());
		editConfig.path = "rainstick-default.json";
		editConfig.settingsPath = "settings.json";
		
		initialize(new EditorApplication(editConfig){
			@Override
			public void create() {
				super.create();
				Pd.audio.create(new PdConfiguration());
			}
		}, config);
	}
}

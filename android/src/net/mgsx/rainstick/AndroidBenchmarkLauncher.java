package net.mgsx.rainstick;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.FPSLogger;

import android.os.Bundle;
import android.view.WindowManager;
import net.mgsx.pd.Pd;
import net.mgsx.pd.PdConfiguration;

public class AndroidBenchmarkLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Game(){
			
			private int count;
			private float elapsed;
			private FPSLogger fps;
			
			@Override
			public void create() {
				
				Pd.audio.create(new PdConfiguration());
				Pd.audio.open(Gdx.files.internal("pd/engine.pd"));
				
				fps = new FPSLogger();
			}
			
			@Override
			public void render() {
				super.render();
				
				elapsed += Gdx.graphics.getDeltaTime();
				if(elapsed > count + 1){
					count = (int)elapsed;
					Gdx.app.log("FPSCalls", String.valueOf(count * 10));
				}
				for(int i=0 ; i<count * 10; i++)
				{
					// Android ASUS : 200
					// Asus Laptop : 7k
					// Pd.audio.sendFloat("kit-freq", 1.f);
					
					// Android ASUS : 280
					// Asus Laptop : 9k
					// Pd.audio.sendFloat("kit-tone", 1.f);
					
					// Android ASUS : 400
					// Asus Laptop : 18k
					// Pd.audio.sendFloat("kit-resonance", 1.f);
					
					// Android ASUS : 2000
					// Asus Laptop : 79k
					// Pd.audio.sendFloat("kit-mixa-dry", 1.f);
					
					// Android ASUS : 36 for both "kit-params" and "kit-mix"
					
					// Android ASUS : 60
					// Asus Laptop : 4k
					// Pd.audio.sendList("kit-params", 1.f, 2f, 3f);
					
					// Asus Laptop : 26k
					// Pd.audio.sendList("kit-mix", 1.f, 2f, 3f, 4f, 5f, 6f);
				}
				
				fps.log();
			}
			
		}, config);
		
	}
}

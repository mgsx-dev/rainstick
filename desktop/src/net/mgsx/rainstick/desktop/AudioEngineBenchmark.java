package net.mgsx.rainstick.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.FPSLogger;

import net.mgsx.pd.Pd;
import net.mgsx.pd.PdConfiguration;

public class AudioEngineBenchmark {

	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		new LwjglApplication(new Game(){
			
			private int count;
			private float elapsed;
			private FPSLogger fps;
			
			@Override
			public void create() {
				
				Pd.audio.create(new PdConfiguration());
				Pd.audio.open(Gdx.files.local("../android/assets/pd/engine.pd"));
				
				fps = new FPSLogger();
			}
			
			@Override
			public void render() {
				super.render();
				
				elapsed += Gdx.graphics.getDeltaTime();
				if(elapsed > count + 1){
					count = (int)elapsed;
					Gdx.app.log("Perf", String.valueOf(count) + "k");
				}
				for(int i=0 ; i<count * 1000; i++)
				{
					// Asus Laptop : 7k
					// Pd.audio.sendFloat("kit-freq", 1.f);
					
					// Asus Laptop : 9k
					// Pd.audio.sendFloat("kit-tone", 1.f);
					
					// Asus Laptop : 18k
					// Pd.audio.sendFloat("kit-resonance", 1.f);
					
					// Asus Laptop : 79k
					// Pd.audio.sendFloat("kit-mixa-dry", 1.f);
					
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

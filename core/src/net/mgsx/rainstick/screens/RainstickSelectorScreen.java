package net.mgsx.rainstick.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import net.mgsx.game.core.Kit;
import net.mgsx.game.core.screen.StageScreen;
import net.mgsx.rainstick.RainstickApplication;
import net.mgsx.rainstick.model.Rainstick;

public class RainstickSelectorScreen extends StageScreen
{
	private final AssetManager assets;
	private final Array<Rainstick> rainsticks;
	private final RainstickApplication screenManager;
	private final float padding = 10;
	private Table main;
	
	public RainstickSelectorScreen(RainstickApplication screenManager, AssetManager assets) {
		super(null);
		this.assets = assets;
		this.screenManager = screenManager;
		
		rainsticks = new Json().fromJson(Array.class, Rainstick.class, Gdx.files.internal("rainsticks.json"));
		
		stage.setViewport(new ExtendViewport(1024 + padding*2, (512+padding*2) * rainsticks.size + padding*2 ));
		
		for(Rainstick rainstick : rainsticks){
			assets.load(rainstick.preview, Texture.class);
		}
	}

	@Override
	public void show() {
		
		if(main == null)
		{
			main = new Table(skin);
			main.defaults().pad(padding);
			
			for(final Rainstick rainstick : rainsticks)
			{
				TextureRegion tg = new TextureRegion(assets.get(rainstick.preview, Texture.class));
				tg.setRegionWidth(1024);
				tg.setRegionHeight(512);
				ImageButton bt = new ImageButton(new TextureRegionDrawable(tg));
				
				main.add(bt).row();
				bt.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						Kit.inputs.removeProcessor(stage);
						screenManager.showRainstick(rainstick);
					}
				});
			}
			
			getStage().addActor(main);
			
			main.setFillParent(true);
		}
		super.show();
	}
	
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(deltaTime);
	}
}

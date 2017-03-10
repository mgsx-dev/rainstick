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
import com.badlogic.gdx.utils.viewport.FillViewport;

import net.mgsx.game.core.screen.StageScreen;
import net.mgsx.rainstick.RainstickApplication;
import net.mgsx.rainstick.model.Rainstick;

public class RainstickSelectorScreen extends StageScreen
{
	private final AssetManager assets;
	private final Array<Rainstick> rainsticks;
	private final RainstickApplication screenManager;
	
	public RainstickSelectorScreen(RainstickApplication screenManager, AssetManager assets) {
		super(null);
		stage.setViewport(new FillViewport(1480, 1640));
		this.assets = assets;
		this.screenManager = screenManager;
		
		rainsticks = new Json().fromJson(Array.class, Rainstick.class, Gdx.files.internal("rainsticks.json"));
		
		for(Rainstick rainstick : rainsticks){
			assets.load(rainstick.preview, Texture.class);
		}
	}

	@Override
	public void show() {
		
		Table main = new Table(skin);
		main.defaults().pad(10);
		
		for(final Rainstick rainstick : rainsticks)
		{
			ImageButton bt = new ImageButton(new TextureRegionDrawable(new TextureRegion(assets.get(rainstick.preview, Texture.class))));
			main.add(bt).row();
			bt.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					screenManager.showRainstick(rainstick);
				}
			});
		}
		
		getStage().addActor(main);
		
		main.setFillParent(true);
		
		super.show();
	}
	
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(deltaTime);
	}
}

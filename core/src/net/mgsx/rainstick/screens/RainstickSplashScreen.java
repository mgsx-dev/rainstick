package net.mgsx.rainstick.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import net.mgsx.game.core.screen.StageScreen;
import net.mgsx.rainstick.RainstickApplication;

public class RainstickSplashScreen extends StageScreen
{
	final private AssetManager assets;
	
	public RainstickSplashScreen(AssetManager assets) {
		super(null);
		this.assets = assets;
		stage.setViewport(new ExtendViewport(RainstickApplication.ViewportWorldWidth, RainstickApplication.ViewportWorldHeight));
		
		FreeTypeFontLoaderParameter p = new FreeTypeFontLoaderParameter();
		p.fontParameters.size = 120;
		p.fontFileName = "anchor-steam-nf/AnchorSteamNF.ttf";
		assets.load("font-x120.ttf", BitmapFont.class, p);
		
		p = new FreeTypeFontLoaderParameter();
		p.fontParameters.size = 30;
		p.fontFileName = "anchor-steam-nf/AnchorSteamNF.ttf";
		assets.load("font-x60.ttf", BitmapFont.class, p);
		
		assets.load("libgdx_logo.png", Texture.class);
	}

	@Override
	public void show() {
		BitmapFont font1 = assets.get("font-x120.ttf", BitmapFont.class);
		BitmapFont font2 = assets.get("font-x60.ttf", BitmapFont.class);
		
		Table main = new Table(skin);
		main.defaults();
		
		LabelStyle style = new LabelStyle();
		style.font = font1;
		style.fontColor = Color.BLACK;
		Label label = new Label("Rainstick", style);
		
		main.add(label).expand().row();
		
		style = new LabelStyle();
		style.font = font2;
		style.fontColor = Color.GRAY;
		label = new Label("powered by", style);
		main.add(label).padBottom(10).row();
		
		Image img1 = new Image(assets.get("libgdx_logo.png", Texture.class));
		main.add(img1).padBottom(40).row();
//		Image img2 = new Image(skin.getDrawable("gdxpd.png"));
//		main.add(img2).row();
		
		
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

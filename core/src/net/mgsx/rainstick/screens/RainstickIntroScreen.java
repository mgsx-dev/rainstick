package net.mgsx.rainstick.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.mgsx.game.core.screen.StageScreen;

public class RainstickIntroScreen extends StageScreen
{
	final private AssetManager assets;
	
	public RainstickIntroScreen(AssetManager assets) {
		super(null);
		this.assets = assets;
		
		FreeTypeFontLoaderParameter p = new FreeTypeFontLoaderParameter();
		p.fontParameters.size = 60;
		p.fontFileName = "anchor-steam-nf/AnchorSteamNF.ttf";
		assets.load("font-x60.ttf", BitmapFont.class, p);
	}

	@Override
	public void show() {
		BitmapFont font = assets.get("font-x60.ttf", BitmapFont.class);
		
		Table main = new Table(skin);
		
		LabelStyle style = new LabelStyle();
		style.font = font;
		style.fontColor = Color.WHITE;
		Label label = new Label("Impedence Corolair", style);
		
		main.add(label).expand().row();
		
		style = new LabelStyle();
		style.font = font;
		style.fontColor = Color.GRAY;
		label = new Label("by B2renger", style);
		main.add().expand();
		main.add(label).expand();
		
		getStage().addActor(main);
		
		main.setFillParent(true);
		
		super.show();
	}
	
}

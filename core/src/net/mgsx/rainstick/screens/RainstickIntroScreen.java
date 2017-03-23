package net.mgsx.rainstick.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import net.mgsx.game.core.screen.StageScreen;
import net.mgsx.rainstick.RainstickApplication;
import net.mgsx.rainstick.model.Rainstick;

public class RainstickIntroScreen extends StageScreen
{
	final private AssetManager assets;
	private Rainstick rainstick;
	
	public RainstickIntroScreen(AssetManager assets) {
		super(null);
		this.assets = assets;
		this.stage.setViewport(new ExtendViewport(RainstickApplication.ViewportWorldWidth, RainstickApplication.ViewportWorldHeight));
		FreeTypeFontLoaderParameter p = new FreeTypeFontLoaderParameter();
		p.fontParameters.size = 60;
		p.fontFileName = "anchor-steam-nf/AnchorSteamNF.ttf";
		assets.load("font-x60.ttf", BitmapFont.class, p);
		
		assets.load("blank.png", Texture.class);
	}
	
	public void setRainstick(Rainstick rainstick) {
		this.rainstick = rainstick;
	}

	@Override
	public void show() {
		
		getStage().getRoot().clearChildren();
		
		BitmapFont font = assets.get("font-x60.ttf", BitmapFont.class);
		Texture texture = assets.get("blank.png", Texture.class);
		
		Table main = new Table(skin);
		
		LabelStyle style = new LabelStyle();
		style.font = font;
		style.fontColor = Color.BLACK;
		Label label = new Label(" " + rainstick.title, style);
		label.setAlignment(Align.left);
		Table g = new Table();
		g.setBackground(new TextureRegionDrawable(new TextureRegion(texture)));
		g.add(label).pad(10).expand().fill();
		main.add(g).expand().fillX().row();
		
		style = new LabelStyle();
		style.font = font;
		style.fontColor = Color.WHITE;
		label = new Label(rainstick.description, style);
		label.setAlignment(Align.center);
		g = new Table();
		g.add(label).pad(30).expand().fill();
		main.add(g).row();
		
		style = new LabelStyle();
		style.font = font;
		style.fontColor = Color.GRAY;
		label = new Label(rainstick.credits, style);
		g = new Table();
		g.add(label).pad(30).expand().fill();
		main.add(g).expand().right().row();
		
		getStage().addActor(main);
		
		main.setFillParent(true);
		
		super.show();
	}
	
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(deltaTime);
	}

	
	
}

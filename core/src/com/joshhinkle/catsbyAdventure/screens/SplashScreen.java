package com.joshhinkle.catsbyAdventure.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.tools.InfoRunnable;

/**
 * First screen to be displayed when the game is started
 * Allows assets to load before the user is given option to continue 
 * 
 * @author Josh
 *
 */
public class SplashScreen implements Screen {

	public SplashScreen(GreatCatsby game) {
		this.game = game;
		manager = game.getManager();
		assetsDoneLoading = false;
		splashPreLoad = manager.get("splash_screen_preload.png");
		splashPostLoad = manager.get("splash_screen_postload.png", Texture.class);

		sb = game.getBatch();
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(GreatCatsby.V_WIDTH / GreatCatsby.CORRECTION,
				GreatCatsby.V_HEIGHT / GreatCatsby.CORRECTION, gameCam);
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

	}


	@Override
	public void render(float delta) {


		handleInput();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.setProjectionMatrix(gameCam.combined);
		sb.begin();
		if (!assetsDoneLoading)
			sb.draw(splashPreLoad, 0, 0, GreatCatsby.V_WIDTH / GreatCatsby.CORRECTION,
					GreatCatsby.V_HEIGHT / GreatCatsby.CORRECTION);
		else {
			sb.draw(splashPostLoad, 0, 0, GreatCatsby.V_WIDTH / GreatCatsby.CORRECTION,
					GreatCatsby.V_HEIGHT / GreatCatsby.CORRECTION);
		}
		sb.end();

	}

	public void handleInput() {
		if((Gdx.input.isKeyJustPressed(Input.Keys.I))){
			Runnable infoRunnable = new InfoRunnable();
			Thread infoThread = new Thread(infoRunnable);
			infoThread.start();
		}
		if (Gdx.input.isTouched()&& assetsDoneLoading) {
				game.setScreen(new PlayScreen(game));
		}
	}

	public void assetsDoneLoading() {
		assetsDoneLoading = true;
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
	}
	@Override
	public void resume() {
	}
	@Override
	public void hide() {
	}
	@Override
	public void dispose() {
	}
	@Override
	public void show() {
	}
	
	private GreatCatsby game;
	private AssetManager manager;
	private Texture splashPreLoad;
	private Texture splashPostLoad;
	private SpriteBatch sb;
	private Camera gameCam;
	private Viewport gamePort;
	private boolean assetsDoneLoading;

}

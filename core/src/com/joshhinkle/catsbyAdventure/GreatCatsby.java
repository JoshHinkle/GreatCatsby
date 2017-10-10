package com.joshhinkle.catsbyAdventure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.joshhinkle.catsbyAdventure.screens.SplashScreen;
import com.joshhinkle.catsbyAdventure.tools.AssetLoader;
import com.joshhinkle.catsbyAdventure.tools.AssetLoaderListener;

/**
 * Begin a Game here by starting the first game screen and setting constants
 * used throughout the game Creates one SpriteBatch to be used throughout the
 * whole game.
 * 
 * @author Josh
 *
 */
public class GreatCatsby extends Game // Interface Game delegates the functions to a screen 
									
{

	/**
	 * Constants used for bitwise or, used in collision detection
	 */
	public static final short DEFAULT_BIT = 1;
	public static final short WALLS_BIT = 2;
	public static final short CATSBY_BIT = 4;
	public static final short THROWOUTABLE_BIT = 8;
	public static final short CATSBY_HEAD_BIT = 16;
	public static final short CATSBY_MOUTH_BIT = 32;
	public static final short GROUND_BIT = 64;
	public static final short OBSTACLE_BIT = 128;
	public static final short HARMFULL_OBSTACLE_BIT = 256;
	public static final short NOTHING_BIT = 512;
	public static final short FEET_BIT = 1024;

	/**
	 * Represents the proportions of the game window size and how it scales to the size of the graphics 
	 */
	public static final int V_HEIGHT = 208;
	public static final int V_WIDTH = 400;
	public static final float CORRECTION = 100;

	/**
	 * Method invoked by Application when creating this game 
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		
		// First load the splash screen Texture
		manager.load("splash_screen_preload.png", Texture.class);
		manager.load("splash_screen_postload.png", Texture.class);
		manager.finishLoading();
		
		// Delegate all other asset loading to a runnable thread that will load while splash screen is up
		final SplashScreen splash = new SplashScreen(this);
		AssetLoaderListener listener = new AssetLoaderListener(){

			@Override
			public void doneLoading() {
				splash.assetsDoneLoading();
			}};
			

		Runnable loaderThreadRunnable = new AssetLoader(this);
		((AssetLoader)loaderThreadRunnable).addListener(listener);
		//LibGDX's way of starting a new thread that is synchronized with the Render thread.
		Gdx.app.postRunnable(loaderThreadRunnable);

		
		//Start the splash screen.
		setScreen(splash);
	}

	/**
	 * Render thread cycles through this method continuously while the game is active 
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Dispose of all textures, Batch, etc. to prevent memory leaks
	 */
	@Override
	public void dispose() {
		batch.dispose();
		super.dispose();
		manager.dispose();
	}

	/**
	 * Used to be able to use the same SpriteBatch throughout the game
	 * @return the SpriteBatch
	 */
	public SpriteBatch getBatch() {
		return batch;
	}

	/**
	 * Gets the AssetManager
	 * @return the AssetManager 
	 */
	public AssetManager getManager() {
		return manager;
	}
	

	private SpriteBatch batch;
	private AssetManager manager;


}

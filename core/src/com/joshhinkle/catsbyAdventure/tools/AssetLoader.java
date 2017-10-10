package com.joshhinkle.catsbyAdventure.tools;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.joshhinkle.catsbyAdventure.GreatCatsby;

/**
 * The AssetLoader concurrently loads all the games major assets during the splash
 * Tells the SplashScreen when the loading is done via an AssetLoaderListener
 * @author Josh
 *
 */
public class AssetLoader implements Runnable{

	/**
	 * Creates an instance of the AssetLoader class 
	 * @param game
	 */
	public AssetLoader(GreatCatsby game){
		this.manager = game.getManager();
		listeners = new ArrayList<AssetLoaderListener>();
	}
	
	@Override
	public void run() {
		manager.load("game_over.png", Texture.class);
		manager.load("music/theme_song.mp3", Music.class);
		manager.load("obstacles/spikes.png", Texture.class);
		manager.load("catsby/dead.png",Texture.class);
		manager.load("throwoutables/fish.png",Texture.class);
		manager.load("throwoutables/kibble_1.png",Texture.class);
		manager.load("catsby/standing.png",Texture.class);
		manager.load("catsby/catsbywalking/walking_1.png",Texture.class);
		manager.load("catsby/catsbywalking/walking_2.png",Texture.class);
		manager.load("catsby/catsbywalking/walking_3.png",Texture.class);
		manager.load("catsby/jumping_1.png",Texture.class);
		manager.load("catsby/catsby_open_mouths/jumping_half_open2.png", Texture.class);
		manager.load("catsby/catsby_open_mouths/jumping_full_open.png" , Texture.class);
		manager.load("catsby/catsby_open_mouths/standing_half_open.png", Texture.class);
		manager.load("catsby/catsby_open_mouths/running_full_open2.png", Texture.class);
		manager.load("catsby/catsby_open_mouths/running_full_open3.png", Texture.class);
		manager.load("catsby/catsby_open_mouths/standing_half_open.png", Texture.class);
		manager.load("catsby/catsby_open_mouths/standing_full_open.png", Texture.class);
		manager.load("catsby/catsby_open_mouths/jumping_half_open2.png",Texture.class);
		manager.load("misc_textures/cloud_1.png",Texture.class);
		manager.load("throwoutables/tomato.png", Texture.class);
		manager.load("obstacles/cactus.png", Texture.class);
		manager.load("obstacles/ottoman.png", Texture.class);
		manager.load("obstacles/dog.png", Texture.class);
		manager.load("obstacles/trashcan.png", Texture.class);
		manager.load("catsby/sick.png",Texture.class);
		manager.load("city.png",Texture.class);
		manager.load("leaderboard.png", Texture.class);
		manager.finishLoading();
		tellDone();
	}
	
	/**
	 * Tells all AssetLoaderListeners that the loading is done
	 */
	public void tellDone(){
		for(AssetLoaderListener listener : listeners){
			listener.doneLoading();
		}
	}
	
	/**
	 * Adds an AssetLoaderListener 
	 * @param listener to be added
	 */
	public void addListener(AssetLoaderListener listener){
		listeners.add(listener);
	}
	

	private AssetManager manager;
	private ArrayList<AssetLoaderListener> listeners;
}

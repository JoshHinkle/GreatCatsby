package com.joshhinkle.catsbyAdventure.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.scenes.Hud;
import com.joshhinkle.catsbyAdventure.sprites.Buildings;
import com.joshhinkle.catsbyAdventure.sprites.Catsby;
import com.joshhinkle.catsbyAdventure.sprites.Cloud;
import com.joshhinkle.catsbyAdventure.sprites.Renderable;
import com.joshhinkle.catsbyAdventure.sprites.Updateable;
import com.joshhinkle.catsbyAdventure.sprites.throwoutables.Throwoutable;
import com.joshhinkle.catsbyAdventure.tools.ObstacleGenerator;
import com.joshhinkle.catsbyAdventure.tools.ThrowoutableGenerator;
import com.joshhinkle.catsbyAdventure.tools.WorldContactListener;
import com.joshhinkle.catsbyAdventure.tools.WorldObjectCreator;

/**
 * The main play screen for the game
 * @author Josh
 *
 */
public class PlayScreen implements Screen {
	
	/**
	 * Represent the speed of the ground and the space between clouds when being rendered 
	 */
	public static float SPEED;
	public static final int CLOUD_SPACING = 50;

	
	/**
	 * Play screen for game. Contains the world that the game is played in and
	 * the physics along with that. Updates all sprites. 
	 * 
	 * @param game the GreatCatsby game that is being played
	 */
	public PlayScreen(GreatCatsby game) {

		// initialize all instance variables
		world = new World(new Vector2(0, -10), true);
		throwoutableGenerator = new ThrowoutableGenerator(this);
		obstacleGenerator = new ObstacleGenerator(this);
		this.game = game;
		manager = game.getManager();
		batch = game.getBatch();
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(GreatCatsby.V_WIDTH / GreatCatsby.CORRECTION,
				GreatCatsby.V_HEIGHT / GreatCatsby.CORRECTION, gameCam);
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("backdrop.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / GreatCatsby.CORRECTION);
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		throwoutables = new ArrayList<Throwoutable>();
		renderables = new ArrayList<Renderable>();
		updateables = new ArrayList<Updateable>();
		updateablesToRemove = new ArrayList<Updateable>();
		updateablesToAdd = new ArrayList<Updateable>();
		SPEED = -.3f;

		// Set up contact listener for collisions 
		world.setContactListener(new WorldContactListener());
		
		// add HUD
		hud = new Hud(game);
		addUpdateable(hud);
		
		// load and play music
		music = manager.get("music/theme_song.mp3", Music.class);
		music.setLooping(true);
		music.setVolume(.15f);
		music.play();

		// Add and define Ground and wall Objects
		new WorldObjectCreator(this).createWorldObjects();
		addUpdateable(throwoutableGenerator);
		addUpdateable(obstacleGenerator);
		
		// Add background buildings
		buildings = new ArrayList<Buildings>();
		building1 = new Buildings(this,0);
		building2 = new Buildings(this,Buildings.WIDTH);
		buildings.add(building1);
		buildings.add(building2);
		
		// add initial clouds
		for (int i = 0; i < 10; i++) {
			Cloud c = new Cloud(this, i * CLOUD_SPACING);
			addUpdateable(c);
			addRenderable(c);
		}

		// Create main character
		catsby = new Catsby(this);
		addUpdateable(catsby);
		addRenderable(catsby);
		
		

		
	}

	/**
	 * render() is called over and over again by associated GreatCatsby. Delegates all the updating and input handling to
	 * another method, and is in charge of drawing everything
	 * @param delta the time between renders
	 */
	@Override
	public void render(float delta) {
		// first, update properties all objects in the game
		update(delta);

		// next clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// render the map
		mapRenderer.render();

		// render the rest of the objects
		batch.setProjectionMatrix(gameCam.combined);
		batch.begin();
		//Buildings need to be in back so render separately first
		for(Buildings b : buildings){
			b.render(batch);
		}
		for (Renderable r : renderables) {
			r.render(batch);
		}

		batch.end();

		// render the Hud as the last layer to be on top of everything else
		game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
		hud.getStage().draw();
	}

	/**
	 * Called by render to update all of the objects in the game. Contains all
	 * logic for updating the game
	 * 
	 * @param dt the time since the last update
	 */
	public void update(float dt) {

		handleInput(dt);
		changeSpeed();
		
		// update the world for physics
		world.step(1 / 60f, 6, 2);
		
		//updates buildings in background
		building1.update(dt);
		if(building1.getX()<=-(Buildings.WIDTH-1)/GreatCatsby.CORRECTION){
			building1.setPosition((building2.getX()+Buildings.WIDTH)/GreatCatsby.CORRECTION, Buildings.Y/GreatCatsby.CORRECTION);
		}
		building2.update(dt);
		if(building2.getX()<=-(Buildings.WIDTH-1)/GreatCatsby.CORRECTION){
			building2.setPosition((building1.getX()+Buildings.WIDTH)/GreatCatsby.CORRECTION, Buildings.Y/GreatCatsby.CORRECTION);
		}

		// update all Updateable sprites
		for (Updateable u : updateables) {
			u.update(dt);
		}

		// Handle removing/adding objects to the updateables after done iterating through updateables 
		for (Updateable u : updateablesToRemove) {
			updateables.remove(u);
		}
		updateablesToRemove.clear();
		for (Updateable u : updateablesToAdd) {
			updateables.add(u);
		}
		updateablesToAdd.clear();

		mapRenderer.setView(gameCam);
	}

	/**
	 * Called by update() to handle all inputs from user
	 * @param dt the time in between updates 
	 */
	public void handleInput(float dt) {
		Body body = catsby.getBody();
		float groundSpeed = .1f;
		if(catsby.isSick()){
			Vector2 velocity = body.getLinearVelocity();
			body.setLinearVelocity(new Vector2(SPEED,velocity.y));
			return;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)&& (catsby.isTouchingGround())) {
			body.applyLinearImpulse(new Vector2(0, 4), body.getWorldCenter(), true);
		}
		if (Gdx.input.isKeyPressed( Input.Keys.RIGHT)) {
			body.applyLinearImpulse(new Vector2(groundSpeed, 0), body.getWorldCenter(), true);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) ) {
			body.applyLinearImpulse(new Vector2(-groundSpeed, 0), body.getWorldCenter(), true);
		}
		if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)
				&& body.getLinearVelocity().y == 0) {
			Vector2 velocity = body.getLinearVelocity();
			body.setLinearVelocity(new Vector2(SPEED, velocity.y));
		}

	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);

	}
	
	@Override
	public void dispose() {
		catsby.getTexture().dispose();


	}
	
	/**
	 * Adds an Updateable to the updateables. 
	 */
	public void addUpdateable(Updateable u) {
		updateablesToAdd.add(u);
	}

	public void removeUpdateable(Updateable u) {
		updateablesToRemove.add((Updateable) u);
	}

	public void addThrowoutable(Throwoutable t) {
		throwoutables.add(t);
	}

	public void addRenderable(Renderable r) {
		renderables.add(r);
	}
	public void addRenderable(int index,Renderable r) {
		renderables.add(index,r);
	}

	public void removeThrowoutable(Throwoutable t) {
		throwoutables.remove(t);
	}

	public void removeRenderable(Renderable r) {
		renderables.remove((Renderable) r);
	}

	public World getWorld() {
		return world;
	}

	public TiledMap getMap() {
		return map;
	}
	
	@Override
	public void show() {
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
	
	public Hud getHud(){
		return hud;
	}
	
	public AssetManager getManager() {
		return manager;
	}
	
	public Catsby getCatsby() {
		return catsby;
	}
	
	public static void changeSpeed(){
		SPEED -= .0001;
	}
	
	public GreatCatsby getGame() {
		return game;
		
	}

	private Buildings building1;
	private Buildings building2;
	private ArrayList<Buildings> buildings;
	private ObstacleGenerator obstacleGenerator;
	private Music music;
	private AssetManager manager;
	private OrthographicCamera gameCam;
	private GreatCatsby game;
	private Viewport gamePort;
	private Hud hud;
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private World world;
	private Catsby catsby;
	private ThrowoutableGenerator throwoutableGenerator;
	private SpriteBatch batch;
	private ArrayList<Throwoutable> throwoutables;
	private ArrayList<Renderable> renderables;
	private ArrayList<Updateable> updateables;
	private ArrayList<Updateable> updateablesToRemove;
	private ArrayList<Updateable> updateablesToAdd;



}

package com.joshhinkle.catsbyAdventure.sprites;


import java.lang.reflect.Method;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.GameOverScreen;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;
import com.joshhinkle.catsbyAdventure.tools.Score;

/**
 * Main Character Sprite of the game 
 * @author Josh
 *
 */
public class Catsby extends Sprite implements Updateable, Renderable {

	/**
	 * Represents what the Catsby is doing at that moment
	 */
	public enum State {
		RUNNING, STANDING, JUMPING, DEAD, SICK
	}
	
	/**
	 * Represents which way Catsby is facing
	 */
	public enum Facing {
		LEFT, RIGHT
	}

	/**
	 * Constructs an instance of the Catsby class
	 * @param playScreen the PlayScreen that the object will appear on
	 */
	public Catsby(PlayScreen playScreen) {
		// Initialize instance variables 
		stateTimer = 0;
		sickTimer =0;
		screen = playScreen;
		this.world = playScreen.getWorld();
		width = 48;
		height = 3 * width / 4;
		isEating = false;
		isDead = false;
		defineCatsby();
		setBounds(0, 0, width / GreatCatsby.CORRECTION, height / GreatCatsby.CORRECTION);
		super.setPosition(body.getPosition().x - super.getWidth() / 2, body.getPosition().y - super.getHeight() / 2);
		deathTimer =0;
		

		// Create Animations
		standingTexture = new TextureRegion(screen.getManager().get("catsby/standing.png", Texture.class));
		Array<TextureRegion> regions = new Array<TextureRegion>();
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsbywalking/walking_1.png",Texture.class)));
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsbywalking/walking_2.png",Texture.class)));
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsbywalking/walking_3.png",Texture.class)));
		runningAnimation = new Animation<TextureRegion>(.1f, regions);
		jumpingTexture = new TextureRegion(screen.getManager().get("catsby/jumping_1.png",Texture.class));
		regions.clear();
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsby_open_mouths/jumping_half_open2.png", Texture.class)));
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsby_open_mouths/jumping_full_open.png" , Texture.class)));
		jumpingOpeningMouthAnimation = new Animation<TextureRegion>(.4f, regions);
		regions.clear();
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsby_open_mouths/standing_half_open.png", Texture.class)));
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsby_open_mouths/running_full_open2.png", Texture.class)));
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsby_open_mouths/running_full_open3.png", Texture.class)));
		runningOpeningMouthAnimation = new Animation<TextureRegion>(.4f, regions);
		regions.clear();
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsby_open_mouths/standing_half_open.png", Texture.class)));
		regions.add(new TextureRegion(screen.getManager().get("catsby/catsby_open_mouths/standing_full_open.png", Texture.class)));
		standingOpeningMouthAnimation = new Animation<TextureRegion>(.4f, regions);
		deadTexture = new TextureRegion(screen.getManager().get("catsby/dead.png", Texture.class));
		sickTexture = new TextureRegion(screen.getManager().get("catsby/sick.png",Texture.class));
		touchingGround = false;
		
		setRegion(jumpingTexture);
	}
	

	/**
	 * Called by render() in PlayScreen 
	 * @param dt the time since the last update
	 */
	public void update(float dt) {
		if(isDead){
			deathTimer +=dt;
			if(deathTimer>3){
				Object finalScore = new Score(screen.getHud().getScore());
				try {
					Method m = finalScore.getClass().getDeclaredMethod("serialize", null);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				//finalScore.serialize();
				screen.getGame().setScreen(new GameOverScreen(screen.getGame(),(Score)finalScore));
			}
		}
		if(isSick){
			sickTimer += dt;
			if(sickTimer>=5){
				isSick=false;
			}
		}
		super.setPosition(body.getPosition().x - super.getWidth() / 2, body.getPosition().y - super.getHeight() / 2);
		setRegion(getFrame(dt));
	}

	/**
	 * Gets the current texture that should be displayed by the sprite
	 * @param dt the time since the last update
	 * @return the Texture that should be shown at this time.
	 */
	public TextureRegion getFrame(float dt) {
		currentState = getState();
		TextureRegion texture;
		switch (currentState) {
		case DEAD:
			texture = deadTexture;
			break;
		case SICK:
			texture = sickTexture;
			break;
		case STANDING:
			if (isEating) {
				texture = standingOpeningMouthAnimation.getKeyFrame(stateTimer, true);
				if (standingOpeningMouthAnimation.isAnimationFinished(stateTimer)) {
					isEating = false;
				}
			} else
				texture = standingTexture;
			break;
		case JUMPING:
	
			if (body.getPosition().x < 350 / GreatCatsby.CORRECTION && body.getLinearVelocity().y < 0)
				texture = standingTexture;
			else {
				if (isEating) {
					texture = jumpingOpeningMouthAnimation.getKeyFrame(stateTimer, true);
					if (jumpingOpeningMouthAnimation.isAnimationFinished(stateTimer)) {
						isEating = false;
					}
				} else
					texture = jumpingTexture;
			}
			break;
		case RUNNING:
			if (isEating) {
				texture = runningOpeningMouthAnimation.getKeyFrame(stateTimer, true);
				if (runningOpeningMouthAnimation.isAnimationFinished(stateTimer)) {
					isEating = false;
				}
			} else {
				texture = runningAnimation.getKeyFrame(stateTimer, true);
			}
			break;
		default:
			texture = standingTexture;
			break;
		}
	
		// determine which direction traveling, make fixtures for heads and tail
		if ((body.getLinearVelocity().x < PlayScreen.SPEED || !runningRight) && !texture.isFlipX()) {
			runningRight = false;
			body.destroyFixture(headFixture);
			body.destroyFixture(mouthFixture);
			addHeadFixture();
			texture.flip(true, false);
		}
		if ((body.getLinearVelocity().x > PlayScreen.SPEED || runningRight) && texture.isFlipX()) {
			runningRight = true;
			body.destroyFixture(headFixture);
			body.destroyFixture(mouthFixture);
			addHeadFixture();
			texture.flip(true, false);
		}
		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return texture;
	}


	/**
	 * gets the state that catsby is in at this moment
	 * @return the State Catsby is in
	 */
	public State getState() {
		if(isDead){
			return State.DEAD;
		}
		if(isSick){
			return State.SICK;
		}
		if (!touchingGround) {
			return State.JUMPING;
		} 
		if (touchingGround && (body.getLinearVelocity().x > PlayScreen.SPEED + .05
				|| body.getLinearVelocity().x < PlayScreen.SPEED - .05)) {
			return State.RUNNING;
		} 
		return State.STANDING;
		
	}


	/**
	 * makes new fixtures for Catsby's head every time he turns around
	 */
	public void addHeadFixture() {
		FixtureDef headFDef = new FixtureDef();
		FixtureDef mouthDef = new FixtureDef();
		CircleShape headShape = new CircleShape();
		CircleShape mouthShape = new CircleShape();
		headShape.setRadius(9 / GreatCatsby.CORRECTION);
		mouthShape.setRadius(9 / GreatCatsby.CORRECTION);
		headFDef.shape = headShape;
		mouthDef.shape = mouthShape;
		mouthDef.isSensor = true;
		headFDef.filter.categoryBits = GreatCatsby.CATSBY_HEAD_BIT;
		headFDef.filter.maskBits = GreatCatsby.WALLS_BIT;
		mouthDef.filter.categoryBits = GreatCatsby.CATSBY_MOUTH_BIT;
		mouthDef.filter.maskBits = GreatCatsby.THROWOUTABLE_BIT;
		// mouthDef.isSensor = true;
		if (runningRight) {
			headShape.setPosition(
					new Vector2((width / 2 - 12) / GreatCatsby.CORRECTION, (height / 4 - 4) / GreatCatsby.CORRECTION));
			mouthShape.setPosition(
					new Vector2((width / 2 - 12) / GreatCatsby.CORRECTION, (height / 4 - 4) / GreatCatsby.CORRECTION));
		} else {
			headShape.setPosition(
					new Vector2(-(width / 2 - 12) / GreatCatsby.CORRECTION, (height / 4 - 4) / GreatCatsby.CORRECTION));
			mouthShape.setPosition(
					new Vector2(-(width / 2 - 12) / GreatCatsby.CORRECTION, (height / 4 - 4) / GreatCatsby.CORRECTION));
		}
		headFixture = body.createFixture(headFDef);
		headFixture.setUserData(this);
		mouthFixture = body.createFixture(mouthDef);
		mouthFixture.setUserData(this);
	
	}


	/**
	 * Defines the Box2D features (physics) of Catsby 
	 */
	public void defineCatsby() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(new Vector2(32 / GreatCatsby.CORRECTION, 150 / GreatCatsby.CORRECTION));
		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		Vector2[] verticies = new Vector2[6];
		verticies[0] = new Vector2((width / 2 - 9) / GreatCatsby.CORRECTION,(0)/GreatCatsby.CORRECTION);
		verticies[1] = new Vector2((width / 2 - 9) / GreatCatsby.CORRECTION,-(height/3)/GreatCatsby.CORRECTION);
		verticies[2] = new Vector2((width / 2 - 24)/GreatCatsby.CORRECTION,-(height / 2 )/GreatCatsby.CORRECTION);
		verticies[3] = new Vector2(-(width / 2 - 24)/GreatCatsby.CORRECTION,-(height / 2 )/GreatCatsby.CORRECTION);
		verticies[4] = new Vector2(-(width / 2 - 9) / GreatCatsby.CORRECTION,-(height/3)/GreatCatsby.CORRECTION);
		verticies[5] = new Vector2(-(width / 2 - 9) / GreatCatsby.CORRECTION,(0)/GreatCatsby.CORRECTION);
		shape.set(verticies);
		fdef.shape = shape;
		fdef.restitution = 0;
		fdef.filter.categoryBits = GreatCatsby.CATSBY_BIT;
		body.createFixture(fdef).setUserData(this);
		
		EdgeShape feet = new EdgeShape();
		feet.set(new Vector2(-(width/4-2)/GreatCatsby.CORRECTION,-(height/2+1)/GreatCatsby.CORRECTION),new Vector2((width/4-2)/GreatCatsby.CORRECTION,-(height/2+1)/GreatCatsby.CORRECTION) );
		fdef.shape = feet;
		fdef.isSensor = true;
		fdef.filter.categoryBits= GreatCatsby.FEET_BIT;
		fdef.filter.maskBits = GreatCatsby.GROUND_BIT | GreatCatsby.OBSTACLE_BIT;
		body.createFixture(fdef).setUserData(this);
		addHeadFixture();
	}


	/**
	 * Makes Catsby Sick
	 */
	public void makeSick(){
		sickTimer = 0;
		isSick = true;
	}


	/**
	 * Kills the Catsby
	 */
	public void killCatsby() {
		isDead = true;
		Filter filter = new Filter();
		filter.maskBits = GreatCatsby.NOTHING_BIT;
		for(Fixture fixture : body.getFixtureList()){
			fixture.setFilterData(filter);
		}
		body.applyLinearImpulse(new Vector2(0,6f), body.getWorldCenter(), true);
		
	}


	public void isEating(boolean isEating) {
		this.isEating = isEating;
	}


	public boolean isSick(){
		return isSick;
	}


	@Override
	public void render(SpriteBatch sb) {
		super.draw(sb);
	
	}


	public Body getBody() {
		return body;
	}
	
	public State getPreviousState() {
		return previousState;
	}

	public void touchingWalls(boolean isTouching){
		this.touchingWalls = isTouching;
	}
	
	public boolean isTouchingWalls(){
		return touchingWalls;
	}
	
	public void touchingGround(boolean isTouching){
		this.touchingGround = isTouching;
	}
	
	public boolean isTouchingGround(){
		return touchingGround;
	}
	
	private float deathTimer;
	private float sickTimer;
	private boolean isSick;
	private boolean touchingWalls;
	private boolean touchingGround;
	private TextureRegion deadTexture;
	private boolean isDead;
	private boolean isEating;
	private World world;
	private Body body;
	private float width;
	private float height;
	private State currentState;
	private State previousState;
	private TextureRegion sickTexture;
	private TextureRegion standingTexture;
	private Animation<TextureRegion> runningAnimation;
	private Animation<TextureRegion> runningOpeningMouthAnimation;
	private Animation<TextureRegion> jumpingOpeningMouthAnimation;
	private Animation<TextureRegion> standingOpeningMouthAnimation;
	private float stateTimer;
	private boolean runningRight;
	private TextureRegion jumpingTexture;
	private Fixture headFixture;
	private Fixture mouthFixture;
	private PlayScreen screen;

}

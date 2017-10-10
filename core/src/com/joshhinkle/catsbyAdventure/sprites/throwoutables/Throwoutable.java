package com.joshhinkle.catsbyAdventure.sprites.throwoutables;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;
import com.joshhinkle.catsbyAdventure.sprites.Renderable;
import com.joshhinkle.catsbyAdventure.sprites.Updateable;

/**
 * A Throwoutable is a sprite that can be thrown out and Catsby can catch it
 * It affects catsby in some way when he swallows it
 * @author Josh
 *
 */
public abstract class Throwoutable extends Sprite implements Renderable, Updateable {
	

	/**
	 * creates an instance of the Throwoutable class
	 * @param screen the PlayScreen it will be thrown out on
	 * @param y the height of the initial throw
	 */
	public Throwoutable(PlayScreen screen, float y){
		this.y = y;
		this.screen = screen;
		this.world = screen.getWorld();
		setToDestroy = false;
		destroyed = false;
		define();
	}
	
	/**
	 * How the Throwoutable affects Catsby when he swallows it
	 */
	public abstract void changeScore();
	
	/**
	 * Defines the Box2D features of this Sprite
	 */
	public void define(){
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(width/2);
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(new Vector2(400/GreatCatsby.CORRECTION, y/GreatCatsby.CORRECTION)); 
		body = world.createBody(bdef);
		shape.setRadius(width/2/GreatCatsby.CORRECTION);
		fdef.shape = shape;
		fdef.filter.categoryBits = GreatCatsby.THROWOUTABLE_BIT;
		fdef.filter.maskBits = GreatCatsby.THROWOUTABLE_BIT | GreatCatsby.CATSBY_BIT | GreatCatsby.CATSBY_MOUTH_BIT;
		body.createFixture(fdef).setUserData(this);
		body.setGravityScale(.06f);
	}


	/**
	 * Throw the throwoutable into the playscreen for Catsby to catch
	 * @param projection the projection the throwoutable is thrown at
	 */
	public void throwOut(Vector2 projection) {
		body.applyLinearImpulse(projection, new Vector2(body.getWorldCenter().x, body.getWorldCenter().y), true);
		screen.addRenderable(this);
		screen.addThrowoutable(this);
		screen.addUpdateable(this);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		super.draw(sb);
	}
	
	@Override
	public void update(float dt){
		if(setToDestroy&&!destroyed){
			destroy();
		}
		else if(!destroyed){
			super.setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
			super.rotate(4);
			if(super.getY() <-20/GreatCatsby.CORRECTION){
				setToDestroy = true;
			}
		}
	}
	
	/**
	 * disposes of the resources needed for this sprite
	 */
	public void destroy(){
		world.destroyBody(body);
		destroyed = true;
		screen.removeUpdateable(this);
		screen.removeRenderable(this);
		screen.removeThrowoutable(this);
	}
	

	protected float width;
	protected boolean setToDestroy;
	protected boolean destroyed;
	protected Body body;
	protected PlayScreen screen;
	protected World world;
	protected float y;
	

}

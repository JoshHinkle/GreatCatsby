package com.joshhinkle.catsbyAdventure.sprites.obstacles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;

/**
 * An obstacle that does not hurt Catsby unless Catsby is smushed between the obstacle and the trailing wall
 * @author Josh
 *
 */
public class Dog extends Obstacle{

	/**
	 * Creates an instance of the Dog class
	 * @param screen the PlayScreen that the Dog will appear on
	 */
	public Dog(PlayScreen screen) {
		super(screen);
		manager = screen.getManager();
		width = 25;
		height = 2*width;
		

		defineObstacle();

		setBounds(0, 0, width/GreatCatsby.CORRECTION,height/GreatCatsby.CORRECTION);
		super.setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
		setRegion(manager.get("obstacles/dog.png", Texture.class));
	}

	@Override
	public void onTouching() {
		
	}

	@Override
	public void defineObstacle() {
		BodyDef bdef2 = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width/2)/GreatCatsby.CORRECTION, height/2/GreatCatsby.CORRECTION);
		bdef2.type = BodyDef.BodyType.KinematicBody;
		bdef2.gravityScale = 1;
		bdef2.position.set(new Vector2(450/GreatCatsby.CORRECTION,64/GreatCatsby.CORRECTION)); 
		body = world.createBody(bdef2);
		fdef.shape = shape;
		fdef.filter.categoryBits = GreatCatsby.OBSTACLE_BIT;
		fdef.filter.maskBits = GreatCatsby.CATSBY_BIT | GreatCatsby.CATSBY_HEAD_BIT | GreatCatsby.GROUND_BIT | GreatCatsby.FEET_BIT;
		body.createFixture(fdef).setUserData(this);
		body.setLinearVelocity(PlayScreen.SPEED, 0);
		
	}
	

	private float height;
	private float width;
	private AssetManager manager;
	

}

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
 * An obstacle that kills Catsby on impact 
 * @author Josh
 *
 */
public class Cactus extends Obstacle{

	/**
	 * Creates an instance of the Cactus class
	 * @param screen the PlayScreen that the Cactus will appear on 
	 */
	public Cactus(PlayScreen screen) {
		super(screen);
		manager = screen.getManager();
		width = 30;
		defineObstacle();
		setBounds(0, 0, width/GreatCatsby.CORRECTION, 4*width/3/GreatCatsby.CORRECTION);
		super.setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
		setRegion(manager.get("obstacles/cactus.png", Texture.class));
	}

	@Override
	public void onTouching() {
		screen.getCatsby().killCatsby();
		
	}

	@Override
	public void defineObstacle() {
		BodyDef bdef2 = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width/2)/GreatCatsby.CORRECTION, 4*width/6/GreatCatsby.CORRECTION);
		bdef2.type = BodyDef.BodyType.DynamicBody;
		bdef2.gravityScale = 0;
		bdef2.position.set(new Vector2(450/GreatCatsby.CORRECTION, 60/GreatCatsby.CORRECTION)); 
		body = world.createBody(bdef2);
		fdef.shape = shape;
		fdef.filter.categoryBits = GreatCatsby.HARMFULL_OBSTACLE_BIT;
		fdef.filter.maskBits = GreatCatsby.CATSBY_BIT;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData(this);
		body.setLinearVelocity(PlayScreen.SPEED, 0);
		
	}
	

	private float width;
	private AssetManager manager;
	

}

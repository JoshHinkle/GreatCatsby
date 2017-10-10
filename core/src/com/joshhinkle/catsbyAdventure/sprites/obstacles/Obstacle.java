package com.joshhinkle.catsbyAdventure.sprites.obstacles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;
import com.joshhinkle.catsbyAdventure.sprites.Renderable;
import com.joshhinkle.catsbyAdventure.sprites.Updateable;

/**
 * An obstacle is anything that Catsby must dodge in order to avoid being killed
 * @author Josh
 *
 */
public abstract class Obstacle extends Sprite implements Renderable, Updateable{
	
	/**
	 * Creates an instance of the abstract class Obstacle
	 * @param screen the PlayScreen the subclass will appear on
	 */
	public Obstacle(PlayScreen screen){
		this.screen = screen;
		this.world = screen.getWorld();
		setToDestroy = false;
		destroyed = false;
	}
	
	/**
	 * Defines what the subclass will do when Catsby touches it 
	 */
	public abstract void onTouching();
	
	/**
	 * Defines the Body and Fixtures to be used with the Object
	 */
	public abstract void defineObstacle();
	
	/**
	 * Adds the Obstacle to the list of Updateables and Renderables on the associated PlayScreen
	 */
	public void setObstacle(){
		screen.addUpdateable(this);
		screen.addRenderable(this);
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
			if(super.getX() <-300/GreatCatsby.CORRECTION){
				setToDestroy = true;
			}
		}
	}
	
	/**
	 * Destroys the B2Body and removes it from being updated or rendered. 
	 */
	public void destroy(){
		world.destroyBody(body);
		destroyed = true;
		screen.removeUpdateable(this);
		screen.removeRenderable(this);
	}
	


	
	protected boolean setToDestroy;
	protected boolean destroyed;
	protected Body body;
	protected PlayScreen screen;
	protected World world;
	
}

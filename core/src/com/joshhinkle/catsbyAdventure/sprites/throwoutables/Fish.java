package com.joshhinkle.catsbyAdventure.sprites.throwoutables;

import com.badlogic.gdx.graphics.Texture;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;

/**
 * A Throwoutable that causes the score to have a 3x Multiplier for 20 seconds
 * @author Josh
 *
 */
public class Fish extends Throwoutable{
	

	/**
	 * Constructs an instance of the Fish class
	 * @param screen the PlayScreen that the fish will appear on
	 * @param y the height of the fish when it is thrown
	 */
	public Fish(PlayScreen screen, float y){
		super(screen,y);
		width = 10f;
		setBounds(getX(),getY(),width/GreatCatsby.CORRECTION,width/GreatCatsby.CORRECTION);
		super.setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
		super.setOriginCenter();
		setRegion(screen.getManager().get("throwoutables/fish.png",Texture.class));	
		
	}
	
	
	@Override
	public void changeScore() {
		screen.getHud().turnOnMultiplier();
		setToDestroy = true;
		
	}

	
	

}

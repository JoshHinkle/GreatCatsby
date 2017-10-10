package com.joshhinkle.catsbyAdventure.sprites.throwoutables;

import com.badlogic.gdx.graphics.Texture;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;

public class Kibble extends Throwoutable{
	
	private static final int POINTS = 100;
	
	/**
	 * Creates an instance of the Kibble class
	 * @param screen the PlayScreen it will be thrown out on
	 * @param y the height of the initial throw
	 */
	public Kibble(PlayScreen screen, float y){
		super(screen,y);
		width = 10f;
		setBounds(getX(),getY(),width/GreatCatsby.CORRECTION,width/GreatCatsby.CORRECTION);
		super.setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
		setRegion(screen.getManager().get("throwoutables/kibble_1.png", Texture.class));	
		super.setOriginCenter();
	}
	


	@Override
	public void changeScore() {
		screen.getHud().addToScore(POINTS);
		setToDestroy = true;
	}
		
}



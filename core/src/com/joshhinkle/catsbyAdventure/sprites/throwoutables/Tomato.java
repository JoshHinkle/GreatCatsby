package com.joshhinkle.catsbyAdventure.sprites.throwoutables;

import com.badlogic.gdx.graphics.Texture;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;

public class Tomato extends Throwoutable{

	/**
	 * Creates an instance of the Tomato class
	 * @param screen the PlayScreen it will be thrown out on
	 * @param y the height of the initial throw
	 */
	public Tomato(PlayScreen screen, float y) {
		super(screen,y);
		width = 10f;
		setBounds(getX(),getY(),width/GreatCatsby.CORRECTION,width/GreatCatsby.CORRECTION);
		super.setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
		setRegion(screen.getManager().get("throwoutables/tomato.png", Texture.class));	
		super.setOriginCenter();

	}
	
	@Override
	public void changeScore() {
		screen.getCatsby().makeSick();
		setToDestroy = true;
		
	}

}

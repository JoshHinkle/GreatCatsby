package com.joshhinkle.catsbyAdventure.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;

public class Buildings extends Sprite implements Updateable, Renderable {
	
	/**
	 * Creates an instance of Buildings
	 * @param screen the PlayScreen the Buildings will render on
	 * @param xCor the starting coordinate of the Buildings
	 */
	public Buildings(PlayScreen screen,float xCor){
		height = 70;
		setBounds(0, 0, WIDTH/GreatCatsby.CORRECTION, height/GreatCatsby.CORRECTION);
		setRegion(screen.getManager().get("city.png",Texture.class)); 
		setPosition(xCor/GreatCatsby.CORRECTION, Y/GreatCatsby.CORRECTION);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		super.draw(sb);
		
	}
	
	@Override
	public void update(float dt) {
		translate(PlayScreen.SPEED/2/GreatCatsby.CORRECTION,0);
	}
	
	private float height;
	public static final float WIDTH = 5*GreatCatsby.V_WIDTH/4;
	public static final float Y = 52.5f;
	

}

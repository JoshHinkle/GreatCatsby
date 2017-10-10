package com.joshhinkle.catsbyAdventure.sprites;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;

public class Cloud extends Sprite implements Renderable, Updateable{

	/**
	 * Creates an instance of the Cloud class
	 * @param playScreen the PlayScreen the cloud will be rendered on
	 * @param location the starting x and y location of the cloud 
	 */
	public Cloud(PlayScreen playScreen, float location){
		this.playScreen = playScreen;
		float originalWidth = 344;
		float originalHeight = 236;
		r = new Random();
		// "randomly" size the cloud
		scalar = r.nextFloat();
		float cloudScalar = .05f + scalar*.15f;
		width = originalWidth * cloudScalar;
		height =  originalHeight * cloudScalar;
		
		setBounds(0,0,width/GreatCatsby.CORRECTION,height/GreatCatsby.CORRECTION);
		setRegion(playScreen.getManager().get("misc_textures/cloud_1.png",Texture.class));
		
		// "randomly" place the cloud 
		setPosition((location)/GreatCatsby.CORRECTION, (130+r.nextFloat()*70)/GreatCatsby.CORRECTION);
	}
	
	@Override
	public void update(float dt){
		translate((.07f+.5f*scalar)*PlayScreen.SPEED/GreatCatsby.CORRECTION, 0);
		if(this.getX()<-100/GreatCatsby.CORRECTION){
			Cloud nextC = new Cloud(playScreen, 9*PlayScreen.CLOUD_SPACING);
			playScreen.addRenderable(10,nextC);
			playScreen.addUpdateable(nextC);
			playScreen.removeRenderable(this);
			playScreen.removeUpdateable(this);
		}
	}
	
	@Override
	public void render(SpriteBatch batch){
		
		if(this.getX()<500/GreatCatsby.CORRECTION || this.getX()>-100/GreatCatsby.CORRECTION)
			this.draw(batch);
	}
	
	
	
	private float width;
	private float height;
	private Random r;
	private float scalar;
	private PlayScreen playScreen;
	
	

}

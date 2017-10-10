package com.joshhinkle.catsbyAdventure.tools;

import java.util.Random;

import com.joshhinkle.catsbyAdventure.screens.PlayScreen;
import com.joshhinkle.catsbyAdventure.sprites.Updateable;
import com.joshhinkle.catsbyAdventure.sprites.obstacles.Cactus;
import com.joshhinkle.catsbyAdventure.sprites.obstacles.Dog;
import com.joshhinkle.catsbyAdventure.sprites.obstacles.Obstacle;
import com.joshhinkle.catsbyAdventure.sprites.obstacles.Ottoman;
import com.joshhinkle.catsbyAdventure.sprites.obstacles.SpikePit;
import com.joshhinkle.catsbyAdventure.sprites.obstacles.TrashCan;

/**
 * Factory that generates random Obstacles for Catsby to deal with 
 * @author Josh
 *
 */
public class ObstacleGenerator implements Updateable {

	/**
	 * Creates an instance of the ObstacleGenerator Class
	 * @param screen The PlayScreen the obstacles will be fed to
	 */
	public ObstacleGenerator(PlayScreen screen) {
		this.screen = screen;
		timer = 0;
		r = new Random();
		nextObstacleTimeLimit = 12;
	}

	@Override
	public void update(float dt) {
		timer += dt;

		if (timer >= (nextObstacleTimeLimit)) {
			
			float nextFloat = r.nextFloat();
			Obstacle next;
			if (nextFloat > .9) {
				next = new Cactus(screen);
			} else if(nextFloat>.8) {
				next = new SpikePit(screen);
			} else if(nextFloat>.5){
				next = new Ottoman(screen);
			} else if(nextFloat>.2){
				next = new TrashCan(screen);
			} else{
				next = new Dog(screen);
			}
			next.setObstacle();
			timer = 0;
			nextObstacleTimeLimit = 1.5f + r.nextFloat()*(12 + 5*PlayScreen.SPEED);
		}
	}
	
	private float timer;
	private Random r;
	private PlayScreen screen;
	private float nextObstacleTimeLimit;

}

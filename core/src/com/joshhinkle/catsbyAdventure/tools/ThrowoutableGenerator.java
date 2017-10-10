package com.joshhinkle.catsbyAdventure.tools;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;
import com.joshhinkle.catsbyAdventure.sprites.Updateable;
import com.joshhinkle.catsbyAdventure.sprites.throwoutables.Fish;
import com.joshhinkle.catsbyAdventure.sprites.throwoutables.Kibble;
import com.joshhinkle.catsbyAdventure.sprites.throwoutables.Throwoutable;
import com.joshhinkle.catsbyAdventure.sprites.throwoutables.Tomato;

/**
 * This class throws out Throwoutables at an increasing time rate in random
 * projectiles
 * 
 * @author Josh
 *
 */
public class ThrowoutableGenerator implements Updateable {

	/**
	 * Constructs an instance of the ThrowoutableGenerator class
	 * 
	 * @param screen
	 *            the PlayScreen associated with this instance
	 */
	public ThrowoutableGenerator(PlayScreen screen) {
		this.screen = screen;
		timer = 0;
		r = new Random();
	}

	/**
	 * Called by PlayScreen render(). Keeps track of how often it should be
	 * throwing out 
	 */
	public void update(float dt) {
		timer += dt;
		// if enough time has passed
		if (timer >= 1.5) {
			throwOutThrowoutable();

		}
	}

	
	/**
	 * Called by update(), picks which type of Throwoutable to throw out, and randomly designs the projectile path
	 * @return
	 */
	public Vector2 throwOutThrowoutable() {
		// "randomly" choose a height to throw out food from
		float y = GreatCatsby.V_HEIGHT / 2 + r.nextFloat() * GreatCatsby.V_HEIGHT / 2;
		Throwoutable next;

		// "randomly" select the next type of food to throw out
		float nextFloat = r.nextFloat();
		if (nextFloat > .9) {
			next = new Fish(screen, y);
		} else if (nextFloat > .8) {
			next = new Tomato(screen, y);
		} else {
			next = new Kibble(screen, y);
		}

		// "randomly" calculate a projectile for the food
		float xProj = -.8f + .4f * r.nextFloat();
		float yProj = 2 * r.nextFloat();

		// throw out the food
		next.throwOut(new Vector2(xProj, yProj));

		// reset the timer
		timer = 0;
		return new Vector2(xProj, yProj);
	}

	private float timer;
	private Random r;
	private PlayScreen screen;
}

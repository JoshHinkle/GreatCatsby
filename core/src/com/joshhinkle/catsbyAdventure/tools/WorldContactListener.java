package com.joshhinkle.catsbyAdventure.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.sprites.Catsby;
import com.joshhinkle.catsbyAdventure.sprites.obstacles.Obstacle;
import com.joshhinkle.catsbyAdventure.sprites.throwoutables.Throwoutable;

/**
 * Listens for contact throughout the Box2D world
 * @author Josh
 *
 */
public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		// collision definition is the binary or of the two fixtures
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		// figure out which collision happened
		switch (cDef) {
		// case: Catsby's head hits a throwable
		case GreatCatsby.CATSBY_MOUTH_BIT | GreatCatsby.THROWOUTABLE_BIT:
			// find which fixture is the ScoreChanger
			if (fixA.getFilterData().categoryBits == GreatCatsby.THROWOUTABLE_BIT) {
				((Catsby) fixB.getUserData()).isEating(true);
				((Throwoutable) fixA.getUserData()).changeScore();
			} else {
				((Catsby) fixA.getUserData()).isEating(true);
				((Throwoutable) fixB.getUserData()).changeScore();
			}
			break;
		// case: Catsby and a harmful Obstacle
		case GreatCatsby.CATSBY_BIT | GreatCatsby.HARMFULL_OBSTACLE_BIT:
			if (fixA.getFilterData().categoryBits == GreatCatsby.HARMFULL_OBSTACLE_BIT) {
				((Obstacle) fixA.getUserData()).onTouching();
			} else {
				((Obstacle) fixB.getUserData()).onTouching();
			}
			break;
		// Case: Catsby is standing on the ground or an Obstacle 
		case GreatCatsby.FEET_BIT | GreatCatsby.OBSTACLE_BIT:
		case GreatCatsby.FEET_BIT | GreatCatsby.GROUND_BIT:
			if (fixA.getFilterData().categoryBits == GreatCatsby.FEET_BIT) {
				((Catsby) fixA.getUserData()).touchingGround(true);
			} else {
				((Catsby)fixB.getUserData()).touchingGround(true);
			}
			break;
		//Case: Catsby is touching one of the walls 
		case GreatCatsby.CATSBY_BIT | GreatCatsby.WALLS_BIT:
		case GreatCatsby.CATSBY_HEAD_BIT | GreatCatsby.WALLS_BIT:
			if (fixA.getFilterData().categoryBits == GreatCatsby.CATSBY_BIT) {
				((Catsby) fixA.getUserData()).touchingWalls(true);
			} else {
				((Catsby) fixB.getUserData()).touchingWalls(true);
			}
			break;
		// Case: Catsby is touching an obstacle 
		case GreatCatsby.CATSBY_BIT | GreatCatsby.OBSTACLE_BIT:
		case GreatCatsby.CATSBY_HEAD_BIT | GreatCatsby.OBSTACLE_BIT:
			if(fixA.getFilterData().categoryBits == GreatCatsby.OBSTACLE_BIT){
				if(((Catsby)fixB.getUserData()).isTouchingWalls()){
					((Catsby)fixB.getUserData()).killCatsby();
				}
			} else {
				if(((Catsby)fixA.getUserData()).isTouchingWalls()){
					((Catsby)fixA.getUserData()).killCatsby();
				}
			}
			break;
		
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
		case GreatCatsby.FEET_BIT | GreatCatsby.OBSTACLE_BIT:
		case GreatCatsby.FEET_BIT | GreatCatsby.GROUND_BIT:
			if (fixA.getFilterData().categoryBits == GreatCatsby.FEET_BIT) {
				((Catsby) fixA.getUserData()).touchingGround(false);
			} else {
				((Catsby) fixB.getUserData()).touchingGround(false);
			}
			break;
		case GreatCatsby.CATSBY_BIT | GreatCatsby.WALLS_BIT:
		case GreatCatsby.CATSBY_HEAD_BIT | GreatCatsby.WALLS_BIT:
			if (fixA.getFilterData().categoryBits == GreatCatsby.CATSBY_BIT) {
				((Catsby) fixA.getUserData()).touchingWalls(false);
			} else {
				((Catsby) fixB.getUserData()).touchingWalls(false);
			}
			
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}

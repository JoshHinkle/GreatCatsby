package com.joshhinkle.catsbyAdventure.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.PlayScreen;

/**
 * Tool to create the walls and floor of the world. 
 * @author Josh
 *
 */
public class WorldObjectCreator {
	
	private World world;
	private TiledMap map;
	
	public WorldObjectCreator(PlayScreen screen){
		world = screen.getWorld();
		map = screen.getMap();
	}
	
	/**
	 * Creates the objects
	 */
	public void createWorldObjects() {
		// Add and define Ground and wall Objects
		// These are permanent fixtures that will not be updated
		// used to enclose the scene the game is played on.
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		for (int i = 1; i < 3; i++) {
			for (MapObject obj : map.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) obj).getRectangle();
				bdef.type = BodyDef.BodyType.StaticBody;
				bdef.position.set(new Vector2((rect.getX() + rect.getWidth() / 2) / GreatCatsby.CORRECTION,
						(rect.getY() + rect.getHeight() / 2) / GreatCatsby.CORRECTION));
				body = world.createBody(bdef);
				shape.setAsBox(rect.getWidth() / 2 / GreatCatsby.CORRECTION,
						rect.getHeight() / 2 / GreatCatsby.CORRECTION);
				fdef.shape = shape;
				// Put no friction on the walls
				if (i == 2) {
					fdef.friction = 0;
					fdef.filter.categoryBits = GreatCatsby.WALLS_BIT;
				} else {
					fdef.restitution = 0;
					fdef.filter.categoryBits = GreatCatsby.GROUND_BIT;
				}

				body.createFixture(fdef);
			}
		}
	}

}

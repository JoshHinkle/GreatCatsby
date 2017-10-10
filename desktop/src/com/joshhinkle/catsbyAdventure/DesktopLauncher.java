package com.joshhinkle.catsbyAdventure;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 3 * GreatCatsby.V_HEIGHT;
		config.width = 3 * GreatCatsby.V_WIDTH;
		config.title = "THE GREAT CATSBY'S KIBBLE QUEST";
		//config.fullscreen = true;
		new LwjglApplication(new GreatCatsby(), config);
	}
}

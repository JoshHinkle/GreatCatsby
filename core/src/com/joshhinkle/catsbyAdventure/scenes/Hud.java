package com.joshhinkle.catsbyAdventure.scenes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.screens.GameOverScreen;
import com.joshhinkle.catsbyAdventure.sprites.Updateable;
import com.joshhinkle.catsbyAdventure.tools.Score;

/**
 * Heads up display. Has its own camera and viewport so that it doesn't move with the game. 
 * @author Josh
 *
 */
public class Hud implements Updateable{
	
	/**
	 * Creates an instance of the Hud class
	 * @param sb the SpriteBatch used to render all graphics
	 */
	public Hud(GreatCatsby game){
		this.game = game;
		SpriteBatch sb = game.getBatch();
		worldTimer = 300;
		timeCount = 0;
		score = 0;
		viewport = new FitViewport(GreatCatsby.V_WIDTH, GreatCatsby.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport,sb);
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		BitmapFont hudFont = new BitmapFont(Gdx.files.internal("game_font.fnt"));
		hudFont.getData().setScale(.2f);
		Label.LabelStyle labelStyle = new Label.LabelStyle(hudFont, Color.DARK_GRAY);
		scoreLabel= new Label("SCORE:  "+String.format("%06d", score),labelStyle);
		timeLabel = new Label("TIME LEFT:  " + String.format("%03d", worldTimer) ,labelStyle);
		scoreLabel.pack();
		timeLabel.pack();
		
		table.defaults().height(17);
		table.add(scoreLabel).expandX();
		table.add(timeLabel).expandX();
		table.row();
		
		stage.addActor(table);
		multiplyer = false;
		multiplyerLabel = new Label("", labelStyle);
		table.add(multiplyerLabel).expandX();
		stage.setDebugAll(false);
	}
	
	/**
	 * Called by PlayScreen every render cycle.
	 * Updates the score, timer,etc. 
	 * @param dt the time between updates
	 */
	public void update(float dt){
		timeCount+=dt; 
		if(multiplyer){
			multiplyerLabel.setText("3X MULTIPLYER ON!");
			multiplyerTimer += dt;
			if(multiplyerTimer>=20){
				multiplyer = false;
			}
		}
		else{
			multiplyerLabel.setText("");
		}
 
			
		if(timeCount>=1){
			worldTimer--;
			if(worldTimer<=0){
				game.setScreen(new GameOverScreen(game,new Score(score)));
			}
			timeLabel.setText(String.format("Time Left:  " +"%03d", worldTimer));
			timeCount =0;
		}
	}

	public Stage getStage() {
		return stage;
	}
	
	/**
	 * Adds specified points to the score board
	 * @param points the amount of points to be added
	 */
	public void addToScore(Integer points){
		if(multiplyer){
			score += 3*points;
		}
		else{
			score += points;
		}
		scoreLabel.setText("SCORE:  " + String.format("%06d", score));
	}
	
	/**
	 * Turns on the 3x multiplier sign for 20 seconds
	 */
	public void turnOnMultiplier(){
		multiplyer = true;
		multiplyerTimer = 0;
	}
	
	/**
	 * @return the score of this game
	 */
	public Integer getScore(){
		return score;
	}

	private GreatCatsby game;
	private Stage stage;
	private Viewport viewport;
	private Integer worldTimer;
	private float timeCount;
	private static Integer score;
	private static Label scoreLabel;
	private static Label timeLabel;
	private boolean multiplyer;
	private double multiplyerTimer;
	private Label multiplyerLabel;
}

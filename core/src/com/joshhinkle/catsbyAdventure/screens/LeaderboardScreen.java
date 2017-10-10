package com.joshhinkle.catsbyAdventure.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.tools.Score;

public class LeaderboardScreen implements Screen{

	
	/**
	 * Creates and instance of the LeaderboardScreen Class
	 * Shows the top 5 scores of the history of the game 
	 * @param game
	 */
	public LeaderboardScreen(final GreatCatsby game){
		
		// instantiate variables 
		this.background = game.getManager().get("leaderboard.png",Texture.class);
		sb = game.getBatch();
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(GreatCatsby.V_WIDTH,
					GreatCatsby.V_HEIGHT, gameCam);
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		stage = new Stage(gamePort,sb);
		
		
		// Make UI elements and add the top 5 scores to the screen 
		Table table = new Table();
		table.center().padBottom(20);
		table.setFillParent(true);
		BitmapFont gameFont = new BitmapFont(Gdx.files.internal("game_font.fnt"));
		gameFont.getData().setScale(.25f);
		LabelStyle labelStyle = new LabelStyle(gameFont, Color.WHITE);
		Label[] highScores = new Label[5];
		scores = Score.getAllScoresSorted();
		for(int i=0;i<5;i++){
			Score thisScore = scores.get(i);
			highScores[i] = new Label(Integer.toString(thisScore.getScore()), labelStyle);
		}
		Label[] scoreRanks = new Label[5];
		for(int i=0;i<5;i++){
			scoreRanks[i] = new Label((i+1) +".    ", labelStyle);
			table.add(scoreRanks[i]).padBottom(0);
			table.add(highScores[i]).padBottom(0);
			table.row();	
		}
		BitmapFont font = new BitmapFont();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		TextButton newGameButton = new TextButton("", textButtonStyle);
		newGameButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new PlayScreen(game));
			}
		});
		
		newGameButton.setSize(74, 18);
		newGameButton.setPosition((GreatCatsby.V_WIDTH-newGameButton.getWidth())/2, 13);
		stage.addActor(table);
		stage.addActor(newGameButton);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		sb.setProjectionMatrix(gameCam.combined);
		sb.begin();
		sb.draw(background, 0, 0, GreatCatsby.V_WIDTH, GreatCatsby.V_HEIGHT);
		sb.end();
		
		sb.setProjectionMatrix(stage.getCamera().combined);
		stage.draw();
		stage.setDebugAll(false);
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}
	
	@Override
	public void pause() {
	}
	@Override
	public void resume() {
	}
	@Override
	public void hide() {
	}
	@Override
	public void dispose() {
	}
	
	
	private Texture background;
	private Viewport gamePort;
	private Camera gameCam;
	private Stage stage;
	private ArrayList<Score> scores;
	private SpriteBatch sb;
	
	

}

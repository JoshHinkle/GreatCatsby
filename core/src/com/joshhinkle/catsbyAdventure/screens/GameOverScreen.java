package com.joshhinkle.catsbyAdventure.screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.joshhinkle.catsbyAdventure.GreatCatsby;
import com.joshhinkle.catsbyAdventure.tools.Score;

public class GameOverScreen implements Screen{
		
	/**
	 * Game Over Screen to signal timer ran out or Catsby Died. 
	 * @param game the GreatCatsby game 
	 * @param finalScore the score the user had when the game ended 
	 */
	public GameOverScreen(final GreatCatsby game, Score finalScore){
		
		// instantiate variables
		gameOverTexture = game.getManager().get("game_over.png", Texture.class);
		sb = game.getBatch();
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(GreatCatsby.V_WIDTH,
					GreatCatsby.V_HEIGHT, gameCam);
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		stage = new Stage(gamePort,sb);
		
		// create UI buttons 
		BitmapFont font = new BitmapFont();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		
		//display score 
		TextButton buttonNewGame = new TextButton("", textButtonStyle);
		BitmapFont scoreFont = new BitmapFont(Gdx.files.internal("game_font.fnt"));
		scoreFont.getData().setScale(.2f);
		Label.LabelStyle labelStyle = new Label.LabelStyle(scoreFont, Color.WHITE);
		Label finalScoreLabel = new Label("Final Score: " + Integer.toString(finalScore.getScore()), labelStyle);
		finalScoreLabel.setSize(90, 20);
		finalScoreLabel.setAlignment(Align.center);
		finalScoreLabel.setPosition((GreatCatsby.V_WIDTH-finalScoreLabel.getWidth())/2, 112);
		
		// add UI Components 
		buttonNewGame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new PlayScreen(game));
			}
		});
		TextButton buttonLeaderboard = new TextButton("", textButtonStyle);
		
		buttonLeaderboard.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new LeaderboardScreen(game));
			}
		});
		buttonNewGame.setSize(120, 18);
		buttonLeaderboard.setSize(122, 18);
		buttonNewGame.setPosition(GreatCatsby.V_WIDTH/2-buttonNewGame.getWidth()/2+2, 89 );
		buttonLeaderboard.setPosition(GreatCatsby.V_WIDTH/2-buttonNewGame.getWidth()/2+2, 65 );
		stage.addActor(buttonNewGame);
		stage.addActor(buttonLeaderboard);
		stage.addActor(finalScoreLabel);
		Gdx.input.setInputProcessor(stage);
		

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		sb.setProjectionMatrix(gameCam.combined);
		sb.begin();
		sb.draw(gameOverTexture, 0, 0, GreatCatsby.V_WIDTH, GreatCatsby.V_HEIGHT);
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
	public void dispose() {
	}
	@Override
	public void show() {
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
	

	private Texture gameOverTexture;
	private SpriteBatch sb;
	private Viewport gamePort;
	private Camera gameCam;
	private Stage stage;

}

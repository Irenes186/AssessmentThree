package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

// Class imports
import com.kroy.Kroy;


/**
 * Displays the main menu screen with selection buttons.
 * 
 * @author Archie
 * @author Josh
 * @since 23/11/2019
 */
public class MainMenuScreen extends BasicScreen {
	
	protected Texture texture;

	/**
	 * The constructor for the main menu screen. All game logic for the main
	 * menu screen is contained.
	 *
	 * @param gam The game object.
	 */
	public MainMenuScreen(final Kroy game) {
		super(game);
	}

	/**
	 * Create the button stage.
	 */
	@Override
	public void show() {
		// Allow stage to control screen inputs.
		Gdx.input.setInputProcessor(stage);

		// Create table to arrange buttons.
		final Table buttonTable = new Table();
		buttonTable.setFillParent(true);
		buttonTable.center();

		// Create buttons
		final TextButton playButton = new TextButton("Play", skin);
		final TextButton tutorialButton = new TextButton("Tutorial", skin);
		final TextButton leaderboardButton = new TextButton("Leaderboard", skin);
		final TextButton storylineButton = new TextButton("Story Line", skin);
		final TextButton quitButton = new TextButton("Quit", skin);

		// Increase size of text
		playButton.setTransform(true);
		playButton.scaleBy(0.25f);
		tutorialButton.setTransform(true);
		tutorialButton.scaleBy(0.25f);
		leaderboardButton.setTransform(true);
		leaderboardButton.scaleBy(0.25f);
		storylineButton.setTransform(true);
		storylineButton.scaleBy(0.25f);
		quitButton.setTransform(true);
		quitButton.scaleBy(0.25f);

		// Add listeners
		playButton.addListener(new ClickListener(){
			@Override
			//transition to game screen
			public void clicked(final InputEvent event, final float x, final float y){
				game.setScreen(new GameScreen(game));
				dispose();
			}
		});
		tutorialButton.addListener(new ClickListener(){
		@Override
		//transition to tutorial screen
		public void clicked(final InputEvent event, final float x, final float y){
			game.setScreen(new TutorialScreen(game));
			dispose();
		}
	});

		leaderboardButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				// Transition to leaderboard screen
				//
				// TO IMPLEMENT
				//
				// Currently main game screen
				game.setScreen(new LeaderboardScreen(game));
				dispose();
			}
		});

		storylineButton.addListener(new ClickListener(){
			@Override
			//transition to storyline screen
			public void clicked(final InputEvent event, final float x, final float y){
				game.setScreen(new StoryLineScreen(game));
				dispose();
			}
		});

		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				Gdx.app.exit();
			}
		});
		
		// Add buttons to table and style them
		buttonTable.add(playButton).padBottom(40).padRight(40).width(150).height(40);
		buttonTable.row();
		buttonTable.add(storylineButton).padBottom(40).padRight(40).width(150).height(40);
		buttonTable.row();
		buttonTable.add(leaderboardButton).padBottom(40).padRight(40).width(150).height(40);
		buttonTable.row();
		buttonTable.add(tutorialButton).padBottom(40).padRight(40).width(150).height(40);
		buttonTable.row();
		buttonTable.add(quitButton).padBottom(40).padRight(40).width(150).height(40);

		// Add table to stage
		stage.addActor(buttonTable);

	}

}
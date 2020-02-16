package com.screens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
//Class imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.classes.LeaderboardPair;
import com.kroy.Kroy;


/**
 * Screen displays the results of the game and prompts for new highscore.
 */
public class ResultScreen extends BasicScreen {

    protected Texture texture;
    protected Skin skin2;
    private String displayText;
    public int score;
    public ArrayList<LeaderboardPair> leaderboard;
    public TextField field;
    public String name;

    /**
     * Constructor 
     * 
     * @param game The game object that the screen will be displayed on.
     * @param score The score from the end of the game.
     */
    public ResultScreen(final Kroy game, final int score) {
        super(game);
        skin2 = new Skin(Gdx.files.internal("skin/uiskin2.json"), atlas);
        if (GameScreen.gameWon && !GameScreen.gameLost){
            displayText = "YOU WIN! Your score is " + GameScreen.score;
        }
        else if(!GameScreen.gameWon && GameScreen.gameLost ){
            displayText = "YOU LOSE";
        }
        this.score = score;
        this.leaderboard = new ArrayList<LeaderboardPair>();
        try{
            this.leaderboard = readLeaderboardFile();
        }
        catch(final Exception e){
        }
    }

    /**
     * Given the new name, saves the new highscore to the internal leaderboard.
     * 
     * @param leaderboard The internal leaderboard to be changed.
     * @param name The name for the new highscore.
     * 
     * @return The list of pairs that form the leaderboard.
     */
    public ArrayList<LeaderboardPair> updateInternalLeaderboard(final ArrayList<LeaderboardPair> leaderboard, final String name){
        final ArrayList<LeaderboardPair> outputPairs = new ArrayList<>();
        boolean notWritten = true;
        for (final LeaderboardPair pair: leaderboard ){
            if (pair.score < this.score && notWritten){
                outputPairs.add(new LeaderboardPair(name, this.score));
                notWritten = false;
            }
            else {
                outputPairs.add(pair);
            }
        }
        return outputPairs;
    }

    /**
     * Check if the score large enough to be included in the leaderboard.
     * 
     * @param leaderboard
     * 
     * @return boolean true if score is greater than last leaderboard score, otherwise false.
     */
    public boolean wantNickname(final ArrayList<LeaderboardPair> leaderboard){
        return leaderboard.get(leaderboard.size()-1).score < this.score;
    }

    /**
     * Reads the leaderboard file into memory and saves the values in an arraylist data structure.
     * 
     * @return An arraylist of pairs that contain the information in the leaderboard file.
     */
    public ArrayList<LeaderboardPair> readLeaderboardFile() throws IOException {
        //read names and scores, add to
        BufferedReader reader;
        final File leaderboardFile = new File("leaderboard.txt");
        reader = new BufferedReader(new FileReader(leaderboardFile));
        final ArrayList<LeaderboardPair> pairs = new ArrayList<LeaderboardPair>();
        String line;
        String[] pair;
        line = reader.readLine();
        while (line != null){
            pair = line.split(",");
            pairs.add(new LeaderboardPair(pair[0], Integer.parseInt(pair[1])));
            line = reader.readLine();
        }
        reader.close();
        return pairs;
    }

    /**
     * Stores the given leaderboard in the leaderboard file.
     * 
     * @param outputList The leaderboard data structure to be saved.
     */
    public void writeLeaderboardFile(final ArrayList<LeaderboardPair> outputList) throws IOException{
        final File leaderboardFile = new File("leaderboard.txt");
        final FileWriter fileWriter = new FileWriter(leaderboardFile);
        final BufferedWriter writer = new BufferedWriter(fileWriter);

        String outputString = "";
        for (final LeaderboardPair pair : outputList) {
            outputString = outputString + pair.name + "," + pair.score + "\n";
        }
        writer.write(outputString);
        writer.close();
    }

    /**
     * Displays all the buttons and text on the screen.
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
        final TextButton menuButton = new TextButton("Menu", skin);
        TextButton enterNameButton = null;
        if (wantNickname(leaderboard)) {
            enterNameButton = new TextButton("Enter", skin);
            enterNameButton.addListener(new ClickListener(){
                @Override
                //transition to game screen
                public void clicked(final InputEvent event, final float x, final float y){
                    final String input = field.getText();
                    name = input;
                    leaderboard = updateInternalLeaderboard(leaderboard,name);
                    try {
                        writeLeaderboardFile(leaderboard);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    game.setScreen(new LeaderboardScreen(game));
                    dispose();
                }
    
            });
        }

        //Create labels
        final Label winLabel = new Label(displayText, skin2);
        winLabel.setFontScale(2,2);
        final Label newHighscoreLabel = new Label("New Highscore! Enter your nickname to add to leaderboard",skin2);
        newHighscoreLabel.setFontScale(1.5f,1.5f);

        winLabel.setAlignment (Align.center);
        newHighscoreLabel.setAlignment (Align.center);

        // Increase size of text
        menuButton.setTransform(true);
        menuButton.scaleBy(0.25f);
        // Add listeners
        menuButton.addListener(new ClickListener(){
            @Override
            //transition to game screen
            public void clicked(final InputEvent event, final float x, final float y){
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        // Add buttons to table and style them
        buttonTable.add(winLabel).padBottom(40).padRight(40).width(150).height(150);
        buttonTable.row();

        //add text field if nickname wanted
        if (wantNickname(leaderboard)) {
            field = new TextField("", skin );;
            field.setPosition(24,73);
            field.setSize(88, 14);
            buttonTable.add(newHighscoreLabel).padBottom(40).padRight(40).width(150).height(40);
            buttonTable.row();
            buttonTable.add(field).padBottom(40).padRight(40).width(150).height(40);
            buttonTable.row();
            buttonTable.add(enterNameButton).padBottom(40).padRight(40).width(150).height(40);
            buttonTable.row();
        }
        buttonTable.add(menuButton).padBottom(40).padRight(40).width(150).height(40);
        buttonTable.row();

        // Add table to stage
        stage.addActor(buttonTable);
    }

    /**
     * Implements a click confirmer.
     */
    class ConfirmClick extends ClickListener{

        /**
         * Constructor
         * 
         * @param event The event to be handled.
         * @param x The x value.
         * @param y The y value.
         * @param field The field to be read.
         * @param name The new name.
         */
        public void clicked(final InputEvent event, final float x, final float y, final TextField field, final String name) {
            Gdx.app.log("button","clicked");
        }
    }
}

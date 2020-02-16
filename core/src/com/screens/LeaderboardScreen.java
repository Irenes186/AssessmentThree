package com.screens;

import com.badlogic.gdx.Gdx;

//Class imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kroy.Kroy;
//import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LeaderboardScreen extends BasicScreen {

    protected Texture texture;
    protected Skin skin2;
    /**
     * Constructor initialises key features of screen
     * @param gam
     */
    public LeaderboardScreen (final Kroy game){
        super(game);
        skin2 = new Skin(Gdx.files.internal("skin/uiskin2.json"), atlas);

    }
    /**
     * Reads leaderboard file and formats the name and score pai
     * @return array of pairs containing the string name and integer score
     * @throws IOException
     */
    public String[] readLeaderboardFile() throws IOException{
        //read names and scores
        BufferedReader reader;
        final File leaderboardFile = new File("leaderboard.txt");
        reader = new BufferedReader(new FileReader(leaderboardFile));
        final String [] pairs = new String[5];
        String line;
        line = reader.readLine();
        int index=0;
        while (line != null){
            pairs[index] = line.replace(","," ");
            line = reader.readLine();
            index++;
        }
        reader.close();
        return pairs;
    }

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

        String[] pairs = new String[5];
        try{
            pairs = readLeaderboardFile();
        }
        catch(final Exception e){
        }

        //Create label
        final Label titleLabel = new Label("LEADERBOARD",skin2);
        titleLabel.setFontScale(2,2);
        final Label firstLabel = new Label(pairs[0],skin);
        final Label secondLabel = new Label(pairs[1],skin);
        final Label thirdLabel = new Label(pairs[2],skin);
        final Label fourthLabel = new Label(pairs[3],skin);
        final Label fifthLabel = new Label(pairs[4],skin);

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
        buttonTable.add(titleLabel).padBottom(40).padRight(40).width(150).height(50);
        buttonTable.row();
        buttonTable.add(firstLabel).padBottom(40).padRight(40).padLeft(20).width(150).height(30);
        buttonTable.row();
        buttonTable.add(secondLabel).padBottom(40).padRight(40).padLeft(20).width(150).height(30);
        buttonTable.row();
        buttonTable.add(thirdLabel).padBottom(40).padRight(40).padLeft(20).width(150).height(30);
        buttonTable.row();
        buttonTable.add(fourthLabel).padBottom(40).padRight(40).padLeft(20).width(150).height(30);
        buttonTable.row();
        buttonTable.add(fifthLabel).padBottom(40).padRight(40).padLeft(20).width(150).height(30);
        buttonTable.row();
        buttonTable.add(menuButton).padBottom(40).padRight(40).width(150).height(40);
        buttonTable.row();
        // Add table to stage
        stage.addActor(buttonTable);
    }

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height);
        camera.update();
    }

    @Override
    public void dispose() {
        skin.dispose();
        skin2.dispose();
    }
}

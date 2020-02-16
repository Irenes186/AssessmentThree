package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

//Class imports
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.classes.LeaderboardPair;
import com.kroy.Kroy;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;


public class ResultScreen implements Screen {

    // A constant variable to store the game
    final Kroy game;

    // Private camera to see the screen
    private OrthographicCamera camera;

    protected Stage stage;
    protected Texture texture;
    protected Skin skin;
    protected Skin skin2;
    protected TextureAtlas atlas;
    private SpriteBatch batch;
    private Viewport viewport;
    private String displayText;
    public int score;
    public ArrayList<LeaderboardPair> leaderboard;
    public TextField field;
    public String name;



    //Constructor
    public ResultScreen(Kroy gam, int score) {
        this.game = gam;

        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        skin2 = new Skin(Gdx.files.internal("skin/uiskin2.json"), atlas);
        //skin.add("default", new Texture("button.png"));


        // Create new sprite batch
        batch = new SpriteBatch();

        // Create an orthographic camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // Set font scale
        game.getFont().getData().setScale(1.5f);

        // Create a viewport
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        viewport.apply();

        // Set camera to centre of viewport
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // Create a stage for buttons
        stage = new Stage(viewport, batch);

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
        catch(Exception e){
        }



    }

    public ArrayList<LeaderboardPair> updateInternalLeaderboard(ArrayList<LeaderboardPair> leaderboard, String name){
        ArrayList<LeaderboardPair> outputPairs = new ArrayList<>();
        boolean notWritten = true;
        for (LeaderboardPair pair: leaderboard ){
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
     * @param leaderboard
     * @return boolean true if score is greater than last leaderboard score, otherwise false.
     */
    public boolean wantNickname(ArrayList<LeaderboardPair> leaderboard){
        System.out.println(leaderboard);
        return leaderboard.get(leaderboard.size()-1).score < this.score;
    }


    public ArrayList<LeaderboardPair> readLeaderboardFile() throws IOException {
        //read names and scores, add to
        BufferedReader reader;
        File leaderboardFile = new File("leaderboard.txt");
        reader = new BufferedReader(new FileReader(leaderboardFile));


        ArrayList<LeaderboardPair> pairs = new ArrayList<LeaderboardPair>();
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

    public void writeLeaderboardFile(ArrayList<LeaderboardPair> outputList) throws IOException{
        File leaderboardFile = new File("leaderboard.txt");
        FileWriter dave = new FileWriter(leaderboardFile);
        BufferedWriter writer = new BufferedWriter(dave);

        String outputString = "";
        for (LeaderboardPair pair : outputList) {
            outputString = outputString + pair.name + "," + pair.score + "\n";
        }
        writer.write(outputString);
        writer.close();
    }

    @Override
    public void show() {
        // Allow stage to control screen inputs.
        Gdx.input.setInputProcessor(stage);

        // Create table to arrange buttons.
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.center();

        // Create buttons
        TextButton menuButton = new TextButton("Menu", skin);
        TextButton enterNameButton = null;
        if (wantNickname(leaderboard)) {
            enterNameButton = new TextButton("Enter", skin);
        }

        //Create labels
        Label winLabel = new Label(displayText, skin2);
        winLabel.setFontScale(2,2);
        Label newHighscoreLabel = new Label("New Highscore! Enter your nickname to add to leaderboard",skin2);
        newHighscoreLabel.setFontScale(1.5f,1.5f);




        // Increase size of text
        menuButton.setTransform(true);
        menuButton.scaleBy(0.25f);
        // Add listeners
        menuButton.addListener(new ClickListener(){
            @Override
            //transition to game screen
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });


        enterNameButton.addListener(new ClickListener(){
            @Override
            //transition to game screen
            public void clicked(InputEvent event, float x, float y){
                String input = field.getText();
                name = input;
                leaderboard = updateInternalLeaderboard(leaderboard,name);
                try {
                    writeLeaderboardFile(leaderboard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                game.setScreen(new LeaderboardScreen(game));
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

    @Override
    public void render(float delta) {
        // MUST BE FIRST: Clear the screen each frame to stop textures blurring
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the button stage
        //stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
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
        skin.dispose();

    }

    class ConfirmClick extends ClickListener{
        public void clicked(InputEvent event, float x, float y, TextField field, String name) {
            Gdx.app.log("button","clicked");

        };
    }
}

package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kroy.Kroy;

import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;

//Class imports


public class TutorialScreen implements Screen {

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



    //Constructor
    public TutorialScreen(Kroy gam) {
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
            displayText = "Keyboard buttons “W”, “A”, “S”, and “D”, as well as the arrow keys, are used to move the truck up, left, down, and right respectively. The truck will rotate as you change direction.\n" +
                    "There are two fire trucks available, these can be switched between by using the keyboard button “TAB”. \n Using the keyboard button “E”, you are able to toggle your hose water on and off. The water will aim in the direction of your mouse cursor." +
                    "You must go back to the fire station in order to repair and refill.";
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

        //Create label
        Label winLabel = new Label(displayText,skin2);
        winLabel.setFontScale(2,2);

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

        // Add buttons to table and style them
        buttonTable.add(winLabel).padBottom(40).padRight(40).width(150).height(150);
        buttonTable.row();
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
}

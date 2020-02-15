package com.miniGame;

import com.sprites.SimpleSprite;
import com.miniGame.MiniFireEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Align;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;

import com.kroy.Kroy;
import com.screens.GameScreen;

public class MiniGameScreen implements Screen {
    final Kroy game;
    GameScreen gameScreen;

    private OrthographicCamera camera;

    protected Stage stage;
    protected Texture texture;
    protected Skin skin;
    protected TextureAtlas atlas;
    private SpriteBatch batch;
    private Viewport viewport;
    private SimpleSprite roadSprite;
    private MiniFireEngine engine; 

    private Queue <Obstacle> obstacles;
    private Random rand;
    private int speed;
    private int lives;
    private int obstacleCount;
    private boolean endGame;

    private Label livesLabel;
    private Label obstacleLabel;
    private Label endGameLabel;

    public MiniGameScreen (final Kroy game, GameScreen gameScreen, int focusID) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.endGame = false;

        this.batch = new SpriteBatch ();

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

        //stage.aj

        roadSprite = new SimpleSprite (new Texture ("MiniGame/road.png"));
        engine = new MiniFireEngine (50, 175);


        roadSprite.setSize (75, SCREEN_WIDTH * 2);
        roadSprite.setPosition (SCREEN_WIDTH - 180, -1100);

        speed = 10;
        lives = 5;
        obstacleCount = 15;
        obstacles = new LinkedList <>();
        rand = new Random ();

        obstacles.add (new Obstacle (SCREEN_WIDTH, engine.getY(), speed));
        obstacles.add (new Obstacle (SCREEN_WIDTH * 4/3, engine.getY(), speed));
        obstacles.add (new Obstacle (SCREEN_WIDTH * 5/3, engine.getY(), speed));

        initLabels();
    }

    private void initLabels () {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont font = new BitmapFont ();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("truetypefont/Franchise-Free-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 30;

        font = generator.generateFont (parameter);
        generator.dispose();

        labelStyle.font = font;

        livesLabel = new Label ("temp", labelStyle);
        obstacleLabel = new Label ("temp", labelStyle);
        endGameLabel = new Label ("temp", labelStyle);

        livesLabel.setPosition (50, 100);
        obstacleLabel.setPosition (50, 50);
        endGameLabel.setPosition (Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        endGameLabel.setAlignment(Align.center);

        stage.addActor (livesLabel);
        stage.addActor (obstacleLabel);
    }


    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin ();

        if (endGame) {
            batch.end ();
            stage.draw();

            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                this.game.setScreen (this.gameScreen);
            }

            return;

        }

        if (this.lives <= 0) {
            //game.setScreen (gameScreen);
            endGameLabel.setText ("You have won the mini game, press enter to continue!");
            transitionToEndGame ();

        } else if (this.obstacleCount == 0) {
            endGameLabel.setText ("You have won the mini game, press enter to continue!");
            transitionToEndGame ();

        } else {
            livesLabel.setText ("Number of Lives left: " + this.lives);
            obstacleLabel.setText ("Number of obstacles left: " + this.obstacleCount);

            if (obstacles.peek().getBoundingRectangle().overlaps (engine.getBoundingRectangle())) {
                obstacles.remove();

                this.lives--;

            }

            if (obstacles.peek().getX() <= 0) {
                obstacles.remove();
            }

            if (obstacles.size() < 3) {
                this.obstacleCount--;
                if (rand.nextBoolean ()) {
                    obstacles.add (new Obstacle (SCREEN_WIDTH, engine.getOriginalY(), speed));
                } else {
                    obstacles.add (new Obstacle (SCREEN_WIDTH, engine.getOriginalY(), speed, this.getRandomNumberInRange (100, 300)));
                }
            }

            // Draw the button stage
            roadSprite.update(batch);
            engine.update(batch);

            roadSprite.translateX (-speed);

            if (roadSprite.getX() <=  180) {
                roadSprite.setPosition (SCREEN_WIDTH - 180, -1100);
            }

            for (Obstacle obstacle : obstacles) {
                obstacle.translateX (-speed);
                obstacle.update(batch);
            }
        }
        batch.end ();
        stage.draw();
    }

    private int getRandomNumberInRange (int min, int max) {
        return rand.nextInt ((max - min) + 1) + min;
    }

    private void transitionToEndGame () {
        for (Actor actor : stage.getActors()) {
            actor.remove();
        }
        stage.addActor (endGameLabel);
        endGame = true;
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}

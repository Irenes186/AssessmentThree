package com.MiniGame;

import com.sprites.SimpleSprite;
import com.MiniGame.MiniFireEngine;

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

import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;

import com.kroy.Kroy;

public class MiniGameScreen implements Screen {
    final Kroy game;

    private OrthographicCamera camera;

    protected Stage stage;
    protected Texture texture;
    protected Skin skin;
    protected TextureAtlas atlas;
    private SpriteBatch batch;
    private Viewport viewport;
    private SimpleSprite roadSprite;
    private MiniFireEngine engine; 

    public MiniGameScreen (final Kroy game) {
        this.game = game;

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
        engine = new MiniFireEngine ();


        System.out.println (SCREEN_WIDTH);
        roadSprite.setSize (75, SCREEN_WIDTH * 2);
        //roadSprite.rotate (90);
        roadSprite.setPosition (SCREEN_WIDTH - 180, -1100);

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

        // Draw the button stage
        roadSprite.update(batch);
        engine.update(batch);

        //roadSprite.rotate (15);

        roadSprite.setPosition(roadSprite.getX() - 10, roadSprite.getY());

        if (roadSprite.getX() <=  180) {
            roadSprite.setPosition (SCREEN_WIDTH - 180, -1100);
        }

        batch.end ();
        stage.draw();
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

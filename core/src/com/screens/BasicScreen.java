package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Constants import
import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;

import com.kroy.Kroy;

/**
 * The Basic Screen Implementation, a basic screen template.
 */
public class BasicScreen implements Screen {

    final protected Kroy game;

    protected Stage stage;
	protected TextureAtlas atlas;
    protected Skin skin;
    protected final OrthographicCamera camera;
    protected final Viewport viewport;
    protected final SpriteBatch batch;

    /**
     * Constructor that takes the game object for the screen to be assigned to.
     * 
     * @param game The game object that this screen will be displayed for.
     */
    protected BasicScreen(final Kroy game) {
        this.game = game;

        // Set font scale
        game.getFont().getData().setScale(1.5f);

		// Create an orthographic camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Set camera to centre of viewport
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

		viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        viewport.apply();
        
        // Create new sprite batch
        batch = new SpriteBatch();

        //SpriteBatch to render in the coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);
		atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        // Create a stage for buttons
        stage = new Stage(viewport, batch);
    }


    /**
     * Clears the screen buffer so that new objects can be rendered.
     * 
     * @param delta Not used by this class.
     */
    @Override
    public void render(float delta) {
        // MUST BE FIRST: Clear the screen each frame to stop textures blurring
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the button stage
        stage.draw();
    }

    /**
     * Changes the size of the window for the application.
     * 
     * @param width The new width in pixels of the screen.
     * @param height The new height in pixels of the screen.
     */
    @Override
    public void resize(int width, int height) {
		viewport.update(width, height);
        camera.update();
    }

    // TODO Auto-generated methods stub
    /**
     * Auto generated method.
     */
    @Override
    public void pause() {}

    /**
     * Auto generated method.
     */
    @Override
    public void resume() {}

    /**
     * Auto generated method.
     */
    @Override
    public void hide() {}

    /**
     * Auto generated method.
     */
    @Override
    public void show() {}

    /**
     * Removes the objects on the screen so that it doesn't leave artifacts.
     */
    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

}
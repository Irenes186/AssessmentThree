package com.miniGame;

//importing of constants
import static com.config.Constants.SCREEN_WIDTH;

//java import statements
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.List;
import java.util.Arrays;

//GDX import statements
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
//importing custom classes
import com.kroy.Kroy;
import com.screens.BasicScreen;
import com.screens.GameScreen;
import com.sprites.SimpleSprite;

/**
 * This is the main class of the minigame, we switch to this screen
 * every time we collide with alien patrols
 */
public class MiniGameScreen extends BasicScreen {
    GameScreen gameScreen;

    protected Texture texture;
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

    /**
     * This is the main constructor for minigame screen
     * 
     * @param game This is the running instance of the game used to switch screens.
     * @param gameScreen This is the instance of the game screen to switch back too.
     * @param focusID This is the id of the fire engine currently in focus.
     */
    public MiniGameScreen (final Kroy game, GameScreen gameScreen, int focusID) {
        super(game);
        this.gameScreen = gameScreen;
        this.endGame = false;

        initSprites(focusID);
        initLabels();
    }

    private void initSprites (int focusID) {
        List<String> engineTextures = Arrays.asList(
            "Fire_Engine_2D_blue.png",
            "Fire_Engine_2D_red.png",
            "Fire_Engine_2D_yellow.png",
            "Fire_Engine_2D_green.png");

        System.out.println(focusID);
        roadSprite = new SimpleSprite (new Texture ("MiniGame/road.png"));
        engine = new MiniFireEngine (50f, 175f, "MiniGame/" + engineTextures.get(focusID - 1));


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
            endGameLabel.setText ("You have failed the mini game, press enter to continue!");
            gameScreen.getFiretruckInFocus().getHealthBar().setResourcePercentage(0);
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
}

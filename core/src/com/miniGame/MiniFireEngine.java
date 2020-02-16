package com.miniGame;

// LibGDX imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Class imports
import com.sprites.MovementSprite;

/**
 * This is the class that represents the fire engine in the minigame.
 */
public class MiniFireEngine extends MovementSprite {

    private static float originalY;

    private int jumpSpeed;
    private boolean jumped;
    private boolean ducked;
    private float originalWidth;

    /**
     * This is the constructor to create the fire engine.
     * 
     * @param x This is the x position to draw the fire engine too.
     * @param y This is the y position to draw the fire engine too.
     * @param textureName The filename of the texture to use for the fire engine.
     */
    public MiniFireEngine (float x, float y, String textureName) {
        super (new Texture (textureName));

        this.setSize (100, 100);
        this.setPosition (x, y);
        this.setAccelerationRate (0f);
        this.setSpeed (new Vector2 (0, 0));

        originalY = y;
        jumpSpeed = 0;
        jumped = false;
        ducked = false;
        originalWidth = this.getWidth();
    }

    /**
     * This is called every frame, it is mainly used to handle the input from the user
     * to control the fireengine.
     * 
     * @param batch This is the batch from minigame screen to draw the fireengine.
     */
    public void update (SpriteBatch batch) {
        super.update(batch);


	if ((Gdx.input.isKeyJustPressed(Keys.W) || Gdx.input.isKeyJustPressed (Keys.UP)) && !jumped && !ducked) {
            jumpSpeed = 20;
            jumped = true;
        }

	if ((Gdx.input.isKeyJustPressed(Keys.S) || Gdx.input.isKeyJustPressed (Keys.DOWN)) && !ducked && !jumped) {
            ducked = true;
            this.setSize (this.getWidth() / 2, this.getHeight());
            this.translateY (-this.getWidth() / 2);
            this.rotate (90);

        } else if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed (Keys.DOWN)) {

        } else if (ducked) {
            this.setSize (originalWidth, this.getHeight());
            this.setPosition (this.getX(), originalY);
            this.rotate (90);
            ducked = false;
        }

        this.translateY (jumpSpeed);
        jumpSpeed -= 1;

        if (this.getY() <= MiniFireEngine.originalY) {
            jumpSpeed = 0;
            jumped = false;
        }
    }

    /**
     * This is to retrive the resting y value of the fire engine
     * 
     * @return The resting y value
     */
    public float getOriginalY() {
        return originalY;
    }
}

package com.miniGame;


// LibGDX imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.sprites.MovementSprite;

public class MiniFireEngine extends MovementSprite {

    private static float originalY;

    private int jumpSpeed;
    private boolean jumped;
    private boolean ducked;
    private float originalWidth;

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

    public float getOriginalY() {
        return originalY;
    }
}

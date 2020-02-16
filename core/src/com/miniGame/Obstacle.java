package com.miniGame;

import com.sprites.MovementSprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class Obstacle extends MovementSprite {

    public Obstacle (float x, float y, int speed) {
        super (new Texture ("MiniGame/Alien.png"));
        create (x, y, speed, 0);

    }

    public Obstacle (float x, float y, int speed, float offset) {
        super (new Texture ("MiniGame/UFO.png"));
        System.out.println ("new flying");
        create (x, y, speed, offset);
    }

    private void create(float x, float y, int speed, float offset) {
        this.setSize (64, 64);
        this.setPosition (x, y + offset);
        this.setAccelerationRate (0f);
        this.setSpeed (new Vector2(speed, 0));
    }


    public void update (SpriteBatch batch) {
        super.update (batch);
    }
}

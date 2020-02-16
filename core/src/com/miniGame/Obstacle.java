package com.miniGame;

//GDX imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

//Class imports
import com.sprites.MovementSprite;

/**
 * This class is used to represents the obstacles in the minigame.
 */
public class Obstacle extends MovementSprite {

    /**
     * This is the constructor for the ground obstacles.
     * 
     * @param x The x position on the screen.
     * @param y The y position on the screen.
     * @param speed How fast to approach the fireengine.
     */
    public Obstacle (float x, float y, int speed) {
        super (new Texture ("MiniGame/Alien.png"));
        create (x, y, speed, 0);
    }

    /**
     * This is the constructor for the air obstacles
     * 
     * @param x The x position on the screen.
     * @param y The y position on the screen.
     * @param speed How fast to approach the fireengine.
     * @param offset How high up the obstacle is from the minimum height.
     */
    public Obstacle (float x, float y, int speed, float offset) {
        super (new Texture ("MiniGame/UFO.png"));
        create (x, y, speed, offset);
    }

    /**
     * This is used to set up the basic properties of the class
     * 
     * @param x The x position of the obstacle on the screen
     * @param y The y position of the obstacle on the screen
     * @param speed How fast to approach the fireengine.
     * @param offset How high up the obstacle is from the minimum height.
     */
    private void create(float x, float y, int speed, float offset) {
        this.setSize (64, 64);
        this.setPosition (x, y + offset);
        this.setAccelerationRate (0f);
        this.setSpeed (new Vector2(speed, 0));
    }
}

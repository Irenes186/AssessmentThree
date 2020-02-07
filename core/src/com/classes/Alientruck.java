package com.classes;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

// Custom class import
import com.sprites.MovementSprite;
import com.classes.ResourceBar;

// Constants imports
import static com.config.Constants.Direction;
import static com.config.Constants.FIRETRUCK_HEIGHT;
import static com.config.Constants.FIRETRUCK_WIDTH;

// Java util import
import java.util.ArrayList;

/**
 * The Alientruck implementation. A sprite capable of moving and colliding with other sprites.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public class Alientruck extends MovementSprite {

    // Private values to be used in this class only
    private ArrayList<Texture> alientruckSlices;
    private int[] alientruckProperties;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a Alientruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param textureSlices  The array of textures used to draw the Alientruck with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the Alientruck collides with.
     * @param xPos           The x-coordinate for the Alientruck.
     * @param yPos           The y-coordinate for the Alientruck.
     */
    public Alientruck(ArrayList<Texture> textureSlices, ArrayList<Texture> frames, float[] properties, TiledMapTileLayer collisionLayer, float xPos, float yPos) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.alientruckSlices = textureSlices;
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Simplfied constructor for the Alientruck, that doesn't require a position.
     * Creates a Alientruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at (0,0).
     * 
     * @param textureSlices  The array of textures used to draw the Alientruck with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the Alientruck collides with.
     */
    public Alientruck(ArrayList<Texture> textureSlices, float[] properties, TiledMapTileLayer collisionLayer) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.alientruckSlices = textureSlices;
        this.create();
    }

    /**
     * Sets the health of the Alientruck and its size provided in CONSTANTS.
     * Also initialises any properties needed by the Alientruck.
     */
    private void create() {
        this.setSize(FIRETRUCK_WIDTH, FIRETRUCK_HEIGHT);
        this.getHealthBar().setMaxResource((int) this.alientruckProperties[0]);
        this.setAccelerationRate(this.alientruckProperties[1]);
        this.setDecelerationRate(this.alientruckProperties[1] * 0.6f);
        this.setMaxSpeed(this.alientruckProperties[2]);
        this.setRestitution(this.alientruckProperties[3]);
        // Start the Alientruck facing left
        this.rotate(-90);
    }

    /**
     * Update the position and direction of the Alientruck every frame.
     * @param batch  The batch to draw onto.
     * @param camera Used to get the centre of the screen.
     */
    public void update(Batch batch, Camera camera, Firetruck targetTruck) {
        super.update(batch);
        drawVoxelImage(batch);
        
        
    }

    /**
     * Draws the voxel representation of the firetruck. Incrementally builds the firetruck
     * from layers of images with each image slightly higher than the last
     */
    private void drawVoxelImage(Batch batch) {
        // Length of array containing image slices
        int slicesLength = this.alientruckSlices.size() - 1;
        float x = getX(), y = getY(), angle = this.getRotation();
        float width = this.getWidth(), height = this.getHeight();
        for (int i = 0; i < slicesLength; i++) {
            Texture texture = animateLights(i);
            batch.draw(new TextureRegion(texture), x, (y - slicesLength / 3) + i, width / 2, height / 2, width, height, 1, 1, angle, true);
        }
    }

    /**
     * Alternates between showing the red and blue light on the truck.
     * Returns the texture at the given index offset to the correct index.
     * 
     * @param index The index of the next texture to draw the sprite with.
     * @return      The next texture to draw the sprite with.
     */
    private Texture animateLights(int index) {
        if (index == 14) { // The index of the texture containing the first light colour
            Texture texture = this.getInternalTime() / 5 > 15 ? this.alientruckSlices.get(index + 1) : this.alientruckSlices.get(index);
            return texture;
        } else if (index > 14) { // Offset remaining in order to not repeat a texture
            return this.alientruckSlices.get(index + 1);
        }
        return this.alientruckSlices.get(index);
    }

    /**
     * Gets whether the Alientruck is damaged.
     * 
     * @return Whether the Alientruck is damaged.
     */
    public boolean isDamaged() {
        return this.getHealthBar().getCurrentAmount() < this.alientruckProperties[0];
    }

    /**
     * Overloaded method for drawing debug information. Draws the hitbox as well
     * as the hose range indicator.
     * 
     * @param renderer  The renderer used to draw the hitbox and range indicator with.
     */
    @Override
    public void drawDebug(ShapeRenderer renderer) {
        super.drawDebug(renderer);
    }

    /**
     * Dispose of all textures used by this class and its parents.
     */
    @Override
    public void dispose() {
        super.dispose();
        for (Texture texture : this.alientruckSlices) {
            texture.dispose();
        }
    }
}
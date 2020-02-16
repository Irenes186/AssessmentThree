package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

// Custom class import
import com.sprites.MovementSprite;

// Constants imports
import static com.config.Constants.FIRETRUCK_HEIGHT;
import static com.config.Constants.FIRETRUCK_WIDTH;

// Java util import
import java.util.ArrayList;

/**
 * The truck implementation. A sprite capable of moving and colliding with other sprites.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public abstract class Truck extends MovementSprite {

    // Private values to be used in this class only
    protected ArrayList<Texture> textureSlices;
    protected float[] truckProperties;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a truck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param textureSlices  The array of textures used to draw the truck with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the truck collides with.
     * @param xPos           The x-coordinate for the truck.
     * @param yPos           The y-coordinate for the truck.
     */
    public Truck (ArrayList<Texture> textureSlices, float[] properties, TiledMapTileLayer collisionLayer, float xPos, float yPos) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.textureSlices = textureSlices;
        this.setPosition(xPos, yPos);
        this.truckProperties = properties;
        this.create();
    }

    /**
     * Simplfied constructor for the truck, that doesn't require a position.
     * Creates a truck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at (0,0).
     * 
     * @param textureSlices  The array of textures used to draw the truck with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the truck collides with.
     */
    public Truck(ArrayList<Texture> textureSlices, float[] properties, TiledMapTileLayer collisionLayer) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.textureSlices = textureSlices;
        this.create();
    }

    /**
     * Sets the health of the truck and its size provided in CONSTANTS.
     * Also initialises any properties needed by the truck.
     */
    private void create() {
        this.setSize(FIRETRUCK_WIDTH, FIRETRUCK_HEIGHT);
        this.getHealthBar().setMaxResource((int) this.truckProperties[0]);
        this.setAccelerationRate(this.truckProperties[1]);
        this.setDecelerationRate(this.truckProperties[1] * 0.6f);
        this.setMaxSpeed(this.truckProperties[2]);
        this.setRestitution(this.truckProperties[3]);
        // Start the truck facing left
        this.rotate(-90);
    }

    /**
     * Update the position and direction of the truck every frame.
     * TODO: change direction vector2 to Constants.Direction
     * 
     * @param batch  The batch to draw onto.
     * @param camera Used to get the centre of the screen.
     */
    public void update(Batch batch) {
        super.update(batch);
        drawVoxelImage(batch);
    }

    /**
     * Draws the voxel representation of the truck. Incrementally builds the truck
     * from layers of images with each image slightly higher than the last
     */
    protected void drawVoxelImage(Batch batch) {
        // Length of array containing image slices
        int slicesLength = this.textureSlices.size() - 1;
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
            Texture texture = this.getInternalTime() / 5 > 15 ? this.textureSlices.get(index + 1) : this.textureSlices.get(index);
            return texture;
        } else if (index > 14) { // Offset remaining in order to not repeat a texture
            return this.textureSlices.get(index + 1);
        }
        return this.textureSlices.get(index);
    }

    /**
     * Gets whether the truck is damaged.
     * 
     * @return Whether the truck is damaged.
     */
    public boolean isDamaged() {
        return this.getHealthBar().getCurrentAmount() < this.truckProperties[0];
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
        for (Texture texture : this.textureSlices) {
            texture.dispose();
        }
    }
}

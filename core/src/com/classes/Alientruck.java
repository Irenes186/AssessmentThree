package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

// Custom class import
import com.sprites.MovementSprite;

// Constants imports
import static com.config.Constants.Direction;
import static com.config.Constants.FIRETRUCK_HEIGHT;
import static com.config.Constants.FIRETRUCK_WIDTH;
import static com.config.Constants.TILE_DIMS;

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
    private float[] alientruckProperties;
    private Vector2[] patrolPoints;
    private Direction[] patrolDirections;
    private int patrolIndex;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a Alientruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * Patrol points must be colinear and adjacent coordinates non-equal
     * 
     * @param textureSlices  The array of textures used to draw the Alientruck with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the Alientruck collides with.
     * @param xPos           The x-coordinate for the Alientruck.
     * @param yPos           The y-coordinate for the Alientruck.
     */
    public Alientruck(ArrayList<Texture> textureSlices, float[] properties, TiledMapTileLayer collisionLayer, float xPos, float yPos, Vector2[] patrolPoints) {
        super(textureSlices.get(textureSlices.size() - 1), collisionLayer);
        this.alientruckSlices = textureSlices;
        this.setPosition(xPos, yPos);
        this.alientruckProperties = properties;
        this.patrolPoints = patrolPoints;
        this.patrolIndex = 0;
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
        
        // Generate path directions
        this.patrolDirections = new Direction[patrolPoints.length];
        if (patrolPoints.length > 0) {
            if (patrolPoints[0].x == patrolPoints[Math.abs((patrolPoints.length - 1) % patrolPoints.length)].x) {
                if (patrolPoints[0].y < patrolPoints[Math.abs((patrolPoints.length - 1) % patrolPoints.length)].y) {
                    patrolDirections[0] = Direction.DOWN;
                } else {
                    patrolDirections[0] = Direction.UP;
                }
            } else {
                if (patrolPoints[0].x < patrolPoints[Math.abs((patrolPoints.length - 1) % patrolPoints.length)].x) {
                    patrolDirections[0] = Direction.LEFT;
                } else {
                    patrolDirections[0] = Direction.RIGHT;
                }
            }
        }
        for (int currentPoint = 1; currentPoint < patrolPoints.length; currentPoint++) {
            if (patrolPoints[currentPoint].x == patrolPoints[Math.abs((currentPoint - 1) % patrolPoints.length)].x) {
                if (patrolPoints[currentPoint].y < patrolPoints[Math.abs((currentPoint - 1) % patrolPoints.length)].y) {
                    patrolDirections[currentPoint] = Direction.DOWN;
                } else {
                    patrolDirections[currentPoint] = Direction.UP;
                }
            } else {
                if (patrolPoints[currentPoint].x < patrolPoints[Math.abs((currentPoint - 1) % patrolPoints.length)].x) {
                    patrolDirections[currentPoint] = Direction.LEFT;
                } else {
                    patrolDirections[currentPoint] = Direction.RIGHT;
                }
            }
        }
    }

    /**
     * Update the position and direction of the Alientruck every frame.
     * TODO: change direction vector2 to Constants.Direction
     * 
     * @param batch  The batch to draw onto.
     * @param camera Used to get the centre of the screen.
     */
    public void update(Batch batch, Firetruck targetTruck) { //ShapeRenderer renderer
        super.update(batch);
        drawVoxelImage(batch);
        
//        // TODO: change direction vector2 to Constants.Direction
//        pathFindTo(new Vector2((int) targetTruck.getCentreX() / TILE_DIMS, (int) targetTruck.getCentreY() / TILE_DIMS), renderer);
//        if (currentPathFindDirection.equals(new Vector2(1, 0))) {
//            applyAcceleration(Direction.RIGHT);
//        } else if (currentPathFindDirection.equals(new Vector2(-1, 0))) {
//            applyAcceleration(Direction.LEFT);
//        } else if (currentPathFindDirection.equals(new Vector2(0, 1))) {
//            applyAcceleration(Direction.UP);
//        } else if (currentPathFindDirection.equals(new Vector2(0, -1))) {
//            applyAcceleration(Direction.DOWN);
//        }
//        System.out.println(currentPathFindDirection);
        
        if (patrolPoints[patrolIndex].equals(new Vector2((int) getX() / TILE_DIMS, (int) getY() / TILE_DIMS))) {
            patrolIndex = (patrolIndex + 1) % patrolDirections.length;
        }
        applyAcceleration(patrolDirections[patrolIndex]);
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
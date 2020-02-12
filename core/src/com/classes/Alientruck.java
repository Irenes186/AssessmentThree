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
public class Alientruck extends Truck {

    // Private values to be used in this class only
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
        super(textureSlices, properties, collisionLayer, xPos, yPos);
        this.setPosition(xPos, yPos);
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
        super(textureSlices, properties, collisionLayer, 0, 0);
        this.create();
    }

    /**
     * Sets the health of the Alientruck and its size provided in CONSTANTS.
     * Also initialises any properties needed by the Alientruck.
     */
    private void create() {
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
    public void update(Batch batch) { //, Firetruck targetTruck, ShapeRenderer renderer
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
}
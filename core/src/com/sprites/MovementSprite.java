package com.sprites;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.config.Constants.Direction;
import com.pathfinding.AStarNode;
import com.pathfinding.AStarNodeComparator;
import com.pathfinding.PathFindUtil;

// Constants import
import static com.config.Constants.COLLISION_TILE;
import static com.config.Constants.TILE_DIMS;

// Java imports
import java.util.PriorityQueue;

/**
 * MovementSprite adds movement facilities to a sprite.
 * @author Archie
 * @since 08/12/2019
 */
public class MovementSprite extends SimpleSprite {

    // Private values to be used in this class only
    private float accelerationRate, decelerationRate, maxSpeed, restitution, rotationLockTime;
    private Vector2 speed;
    private TiledMapTileLayer collisionLayer;
    // The coordinate the sprite is currently pathfinding to. null => no target.
    private Vector2 currentTargetPos;
    // The direction in which the sprite should move in order to reach currentTargetPos.
    protected Vector2 currentPathFindDirection;
    // Comparator implementation that decides a total ordering over AStarNodes. Only used during pathfinding.
    private AStarNodeComparator nodeComp;
    private int deliveryRate;

    /**
     * Creates a sprite capable of moving and colliding with the tiledMap and other sprites.
     * 
     * @param spriteTexture  The texture the sprite should use.
     * @param collisionLayer The layer of the map the sprite will collide with.
     */
    public MovementSprite(Texture spriteTexture, TiledMapTileLayer collisionLayer) {
        super(spriteTexture);
        this.collisionLayer = collisionLayer;
        this.create();
    }

    /**
     * Creates a sprite capable of moving and but only colliding with other sprites.
     * 
     * @param spriteTexture  The texture the sprite should use.
     */
    public MovementSprite(Texture spriteTexture) {
        super(spriteTexture);
        this.create();
    }

    /**
     * Sets the inital values for all properties needed by the sprite.
     */
    private void create() {
        this.speed = new Vector2(0,0);
        this.accelerationRate = 10;
        this.decelerationRate = 6;
        this.rotationLockTime = 0;
        this.restitution = 0.8f;
        this.maxSpeed = 200;
        this.currentTargetPos = null;
        this.currentPathFindDirection = new Vector2(0, 0);
        this.nodeComp = new AStarNodeComparator();
        this.deliveryRate = 0;
    }

    /**
     * Update the sprite position and direction based on acceleration and
     * boundaries. This is called every game frame.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
        // Calculate the acceleration on the sprite and apply it
        accelerate();
        // Rotate sprite to face the direction its moving in
        updateRotation();
        // Update rotationLockout if set
        if (this.rotationLockTime >= 0) this.rotationLockTime -= 1; 
    }

    /**
     * Increases the speed of the sprite in the given direction.
     * @param direction The direction to accelerate in.
     */
    public void applyAcceleration(Direction direction) {
        if (this.speed.y < this.maxSpeed && direction == Direction.UP) {
            this.speed.y += this.accelerationRate;
        }
        if (this.speed.y > -this.maxSpeed && direction == Direction.DOWN) {
            this.speed.y -= this.accelerationRate;
        }
        if (this.speed.x < this.maxSpeed && direction == Direction.RIGHT) {
            this.speed.x += this.accelerationRate;
        }
        if (this.speed.x > -this.maxSpeed && direction == Direction.LEFT) {
            this.speed.x -= this.accelerationRate;
        }
    }

    /**
     * Calculate the angle the sprite needs to rotate from it's current rotation to the new rotation.
     */
    private void updateRotation() {
        float currentRotation = this.getRotation();
        float desiredRotation = this.speed.angle();
        float angle = desiredRotation - currentRotation;
        if (this.speed.len() >= this.accelerationRate && this.rotationLockTime <= 0) {
            // Use the shortest angle
            angle = (angle + 180) % 360 - 180;
            float rotationSpeed = 0.05f * this.speed.len();
            this.rotate(angle * rotationSpeed * Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Apply acceleration to the sprite, based on collision boundaries and
     * existing acceleration.
     */
    private void accelerate() {
        // Calculate whether it hits any boundaries
        boolean collides = this.collisionLayer != null && collidesWithBlockedTile(this.getX(), this.getY());
        // Check if it collides with any tiles, then move the sprite
        if (!collides) {
            this.setX(this.getX() + this.speed.x * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() + this.speed.y * Gdx.graphics.getDeltaTime());
            if (this.decelerationRate != 0) decelerate();
        } else {
            // Seperate the sprite from the tile and stop sprite movement
            collisionOccurred(this.speed.rotate(180).scl(0.05f));
            this.speed = new Vector2(0, 0);
        }
    }

    /**
     * Decreases the speed of the sprite (direction irrelevant). Deceleration rate
     * is based upon the sprite's properties.
     */
    private void decelerate() {
        // Stops it bouncing from decelerating in one direction and then another etc..
        if (this.speed.y < this.decelerationRate && this.speed.y > -this.decelerationRate) {
            this.speed.y = 0f;
        } else {
            this.speed.y -= this.speed.y > 0 ? this.decelerationRate : -this.decelerationRate;
        }
        if (this.speed.x < this.decelerationRate && this.speed.x > -this.decelerationRate) {
            this.speed.x = 0f;
        } else {
            this.speed.x -= this.speed.x > 0 ? this.decelerationRate : -this.decelerationRate;
        }
    }

    /**
     * Checks what direction the sprite is facing and bounces it the opposite way.
     * @param seperationVector Vector containing the minimum distance needed to travel to seperate two sprites.
     */
    public void collisionOccurred(Vector2 seperationVector) {
        // Calculate how far to push the sprite back
        float pushBackX = seperationVector.x;
        float pushBackY = seperationVector.y;
        // For each direction, reverse the speed and set the sprite back a few coordinates out of the collision
        this.speed.y *= -this.restitution;
        this.speed.x *= -this.restitution;
        this.setRotationLock(0.5f);
        this.setY(this.getY() + pushBackY);
        this.setX(this.getX() + pushBackX);
    }

    /**
     * Checks if the tile at a location is a "blocked" tile or not.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return Whether the sprite can enter the tile (true) or not (false).
     */
    private boolean collidesWithBlockedTile(float x, float y) {
        // Vector to calculate rotated width and height
        Vector2 size = new Vector2(this.getWidth(), this.getHeight()).rotate(360-this.getRotation());
        // Coordinates to check with a bit of leniance
        int leftX =   (int) x;
        int rightX =  (int) (x + Math.abs(size.x));
        int bottomY = (int) y;
        int topY =    (int) (y + Math.abs(size.y));
        // Check all corners of the firetruck to see if they're in a blocked tile
        for ( int xPos = leftX; xPos < rightX; xPos+= TILE_DIMS / 8) {
            for ( int yPos = bottomY; yPos < topY; yPos+= TILE_DIMS / 8) {
                Cell cell = collisionLayer.getCell(xPos / TILE_DIMS, yPos / TILE_DIMS);
		        if (cell != null && cell.getTile() != null) return cell.getTile().getProperties().containsKey(COLLISION_TILE);
            }
        }
        return false;
	}
    
    /**
     * Sets the amount of time the sprite cannot rotate for.
     * @param duration The duration the sprite cannot rotate in.
     */
    public void setRotationLock(float duration) {
        if (duration > 0 && this.rotationLockTime <= 0) {
            this.rotationLockTime = duration * 100;
        }
    }

    /**
     * Sets the amount the sprite will bounce upon collisions.
     * @param bounce The restitution of the sprite.
     */
    public void setRestitution(float bounce) {
        this.restitution = bounce;
    }


    public void setDeliveryRate(int deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    /**
     * Sets the rate at which the sprite will decelerate.
     * @param rate The deceleration rate for the sprite.
     */
    public void setDecelerationRate(float rate) {
        this.decelerationRate = rate;
    }
    /**
     * Sets the rate at which the sprite will accelerate.
     * @param rate The acceleration rate for the sprite.
     */

    public void setAccelerationRate(float rate) {
        this.accelerationRate = rate;
    }

    /**
     * Sets the max speed the sprite can accelerate to.
     * @param amount The max speed value for the sprite.
     */
    public void setMaxSpeed(float amount) {
        this.maxSpeed = amount;
    }

     /**
     * Returns the max speed the sprite can accelerate to.
     * @return  The max speed value for the sprite.
     */
    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    public int getDeliveryRate() {
        return this.deliveryRate;
    }

    /**
     * Sets the current speed of the sprite.
     * @param speed The speed the sprite should travel.
     */
    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    /**
     * Gets the current speed of the sprite.
     * @return The current speed of the sprite.
     */
    public Vector2 getSpeed() {
        return this.speed;
    }
    
    /**
     * Calculate the next turning point along the road to travel to in order to reach a certain point
     * TODO: change direction vector2 to Constants.Direction
     * 
     * @param targetPos  The coordinate, in terms of cells, to pathfind to (must be on a road)
     * @return a Vector2 representing the next road turning point to travel to. null if at destination or no path found
     */
    private Vector2 nextAStar(Vector2 targetPos, ShapeRenderer renderer) {
        System.out.println("Generate new intermediary destination...");
        // Find the position of the sprite in terms of cells
        Vector2 currentCellPos = new Vector2((int) (this.getCentreX() / TILE_DIMS), (int) (this.getCentreY() / TILE_DIMS));
        // Check whether the target position has been reached
        if (currentCellPos.equals(targetPos)) {
            System.out.println("Final destination reached.");
            return null;
            
        // if not, execute A*
        } else {
            // The list of unexpanded nodes
            PriorityQueue<AStarNode> open = new PriorityQueue<AStarNode>(4, nodeComp);
            // The list of expanded nodes
            PriorityQueue<AStarNode> closed = new PriorityQueue<AStarNode>(10, nodeComp);
            // Initialise open to the sprite's current position
            open.add(new AStarNode(currentCellPos, currentCellPos, targetPos, null, 0));
            System.out.println("Initial node: " + open.peek());
//            System.out.println("position: " + currentCellPos);
            // Track whether or not targetPos has been found
            boolean goalFound = false;
            // The node with the current maximum value of f
            AStarNode bestNode = null;
            
            // While there are still nodes to expand and targetPos has not been found
            while (!open.isEmpty() && !goalFound) {
                System.out.println("A* ITERATION HERE ==========");
                // Get the unexpanded node with the highest value of f, and move it to the closed list
                bestNode = open.peek();
                open.remove();
                closed.add(bestNode);
                // If this is the target node, stop searching
                if (bestNode.getPos().equals(targetPos)) {
                    System.out.println("goal found!");
                    goalFound = true;
                    break;
                }
                
                if (PathFindUtil.getChildNodes(bestNode.getPos(), collisionLayer).isEmpty()) {
                    System.out.println("No children for node: " + bestNode.getPos());
                }
                
                // Add the node's children to the open list
                for (Vector2 currentChild: PathFindUtil.getChildNodes(bestNode.getPos(), collisionLayer)) {
                    // TODO: If child is already in closed list, or in open list with lower g, ignore
                    open.add(new AStarNode(currentCellPos, currentChild, targetPos, bestNode, bestNode.getPos().dst(currentChild)));
                    renderer.rect(currentChild.x, currentChild.y, TILE_DIMS, TILE_DIMS, Color.RED, Color.RED, Color.RED, Color.RED);
                    System.out.println("Check child: " + currentChild);
//                    renderer.rect(currentChild.x, currentChild.y, TILE_DIMS, TILE_DIMS);
                }
            }
            
            // If a goal was not found, return null
            if (!goalFound || bestNode == null) {
                System.out.println("A* failed, no goal found.");
                return null;
            // Otherwise, backtrack along each node's parents, until the sprite's position is found.
            } else {
                while (bestNode != null) {
                    if (bestNode.getParent().getPos().equals(currentCellPos)) {
                        System.out.println("Next intermediary position generated at: " + bestNode.getPos());
                        // If the sprite's position was found, return the next road corner
                        return bestNode.getPos();
                    } else if (bestNode.getParent() != null) {
                        bestNode = bestNode.getParent();
                    // If the sprite's position was not found, return null
                    } else {
                        System.out.println("Path extraction failed");
                        return null;
                    }
                }
            }
            
            // Return null if some error occurred
            return null;
        }
    }
    
    /**
     * Return a unit Vector2 representing the direction in which the sprite should travel
     * in order to reach this.currentTargetPos. Currently only supports four directions.
     * TODO: change direction vector2 to Constants.Direction
     * 
     * @param targetPos  The coordinate, in terms of cells, the sprite should pathfind to.
     * @return the direction in which the sprite should travel. Can be: (0, 0), (1, 0), (-1, 0), (0, 1), (0, -1) or null (use previous)
     */
    public void pathFindTo (Vector2 targetPos, ShapeRenderer renderer) {
        System.out.println("Path findto: " + targetPos);
        Cell targetCell = collisionLayer.getCell((int) targetPos.x, (int) targetPos.y);
        // Make sure the requested location is not blocked
        if ((targetCell == null || targetCell.getTile() == null) || !targetCell.getTile().getProperties().containsKey(COLLISION_TILE)) {
            // If we don't have a pathfinding location yet
            if (this.currentTargetPos == null) {
                System.out.println("No pathfinding location.");
                // Get the next intermediary location
                this.currentTargetPos = nextAStar(targetPos, renderer);
                
                // If a location could not be generated, just don't do anything
                if (this.currentTargetPos == null) {
                    return;
                }
                
                // Set the movement direction
                // Is the next location along the Y axis?
                if (this.currentTargetPos.x == this.getCentreX()) {
                    System.out.println("Move along X axis");
                    if (this.currentTargetPos.y > this.getCentreY()) {
                        this.currentPathFindDirection.set(0, -1);
                    } else {
                        this.currentPathFindDirection.set(0, 1);
                    }
                // Is the next location along the X axis?
                } else {
                    System.out.println("Move along Y axis");
                    if (this.currentTargetPos.x > this.getCentreX()) {
                        this.currentPathFindDirection.set(-1, 0); 
                    } else {
                        this.currentPathFindDirection.set(1, 0);
                    }
                }
            }
            
            // Has the intermediary location been reached?
            else if (this.getCentreX() == this.currentTargetPos.x && this.getCentreY() == this.currentTargetPos.y) {
                // Find the position of the sprite in terms of cells
                Vector2 currentCellPos = new Vector2((int) (this.getCentreX() / TILE_DIMS), (int) (targetPos.y / TILE_DIMS));
                // Has the final destination been reached?
                if (currentCellPos.equals(targetPos)) {
                    // Zero out the movement direction
                    this.currentPathFindDirection.setZero();
                    System.out.println("At final destination.");
                // Is the Sprite at an intermediary location?
                } else {
                    System.out.println("Intermediary destination reached.");
                    // Get the next intermediary location
                    this.currentTargetPos = nextAStar(targetPos, renderer);
                    // Set the movement direction
                    // Is the next location along the Y axis?
                    if (this.currentTargetPos.x == this.getCentreX()) {
                        System.out.println("Move along X axis");
                        if (this.currentTargetPos.y > this.getCentreY()) {
                            this.currentPathFindDirection.set(0, -1);
                        } else {
                            this.currentPathFindDirection.set(0, 1);
                        }
                    // Is the next location along the X axis?
                    } else {
                        System.out.println("Move along Y axis");
                        if (this.currentTargetPos.x > this.getCentreX()) {
                            this.currentPathFindDirection.set(-1, 0); 
                        } else {
                            this.currentPathFindDirection.set(1, 0);
                        }
                    }
                }
            }
        } else {
            if (targetCell == null) {
                if (targetPos.x < 0 || targetPos.x >= collisionLayer.getWidth() || targetPos.y < 0 || targetPos.y >= collisionLayer.getHeight()) {
                    System.out.println("Requested grid cell out of range! width=" + collisionLayer.getWidth() + " height=" + collisionLayer.getHeight());
                } else {
                    System.out.println("Requested grid cell is in range, but null!");
//                    System.out.println("cell: " + collisionLayer.getCell((int) targetPos.x, (int) targetPos.y));
                    System.out.println("cell: " + collisionLayer.getCell(1, 1));
                }
            } else if (targetCell.getTile() == null) {
                System.out.println("Requested cell's tile is null!");
            } else {
                System.out.println("Target location blocked!");
            }
        }
    }
}
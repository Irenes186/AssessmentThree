package com.classes;

// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

// Custom class import
import com.sprites.SimpleSprite;

// Constants import
import static com.config.Constants.FIRESTATION_HEALTH;
import static com.config.Constants.FIRESTATION_HEIGHT;
import static com.config.Constants.FIRESTATION_WIDTH;
import static com.config.Constants.FIRETRUCK_REPAIR_SPEED;

/**
 * The Firestation implementation, a static sprite in the game.
 * 
 * @author Archie
 * @since 17/12/2019
 */
public class Firestation extends SimpleSprite {

    // Private values for this class to use
	private Rectangle repairRange;

    /**
     * Overloaded constructor containing all possible parameters.
     * Drawn with the given texture at the given position.
     * 
     * @param texture  The texture used to draw the Firestation with.
     * @param xPos     The x-coordinate for the Firestation.
     * @param yPos     The y-coordinate for the Firestation.
     */
    public Firestation(Texture texture, float xPos, float yPos) {
        super(texture);
        this.setPosition(xPos, yPos);
        this.create();
    }

    /**
     * Simplified constructor for the Firestation, that doesn't require a position.
     * Drawn with the given texture at (0,0).
     * 
     * @param texture  The texture used to draw the Firestation with.
     */
    public Firestation(Texture texture) {
        super(texture);
        this.create();
    }

    /**
     * Sets the health of the Firestation and its size provided in CONSTANTS.
     * Also creates a circle to indicate the range firetrucks should be within
     * in order to be repaired by the firestation.
     */
    private void create() {
        this.getHealthBar().setMaxResource(FIRESTATION_HEALTH);
        this.setSize(FIRESTATION_WIDTH, FIRESTATION_HEIGHT);
        this.repairRange = new Rectangle(this.getCentreX() - 1.9f * com.config.Constants.TILE_DIMS + 10, this.getY() - 9 * com.config.Constants.TILE_DIMS, 4 * com.config.Constants.TILE_DIMS, 7 * com.config.Constants.TILE_DIMS);
    }

    /**
     * Updates the firestation so that it is drawn every frame.
     * Also reduces the time before next repair can occur.
     * @param batch  The batch to draw onto.
     */
    public void update(Batch batch) {
        super.update(batch);
    }

    /**
     * Repair a firetruck over time.
     * 
     * @param firetruck  The firetruck that will be repaired.
     */
    public void repair(Firetruck firetruck) {
        if (this.getInternalTime() % 10 == 0) {
            firetruck.getHealthBar().addResourceAmount((int) firetruck.getHealthBar().getMaxAmount() / FIRETRUCK_REPAIR_SPEED);
            firetruck.getWaterBar().addResourceAmount((int) firetruck.getWaterBar().getMaxAmount() / FIRETRUCK_REPAIR_SPEED);
        }
    }

    /**
     * Checks if a polygon is within the range of the firestation.
     * Usually used to see if a firetruck is close enough to be repaired.
     * 
     * [!] Repair range originally handled intersecting a circular radius around the Firestation.
     * This is now handled with a rectangle covering the car park area outside of the Firestation.
     * The positioning of this rectangle is currently hard coded.
     * 
     * @param polygon  The polygon that needs to be checked.
     * @return         Whether the given polygon is in the radius of the Firestation
     */
    public boolean isInRadius(Polygon polygon) {
        float []vertices = polygon.getTransformedVertices();
        for (int i = 0; i < vertices.length; i+=2){
            if (i == 0){
                if (Intersector.intersectSegmentRectangle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), this.repairRange))
                	return true;
            } else {
                if (Intersector.intersectSegmentRectangle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), this.repairRange))
                	return true;
            }
        }
        return polygon.contains(this.repairRange.x, this.repairRange.y);
    }

    /**
     * Overloaded method for drawing debug information. Draws the hitbox as well
     * as the range circle.
     * 
     * @param renderer  The renderer used to draw the hitbox and range indicator with.
     */
    @Override
    public void drawDebug(ShapeRenderer renderer) {
        super.drawDebug(renderer);
        renderer.rect(this.repairRange.x, this.repairRange.y, this.repairRange.width, this.repairRange.height);
    }
}
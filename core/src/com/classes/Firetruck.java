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
import com.classes.ResourceBar;

// Constants imports
import static com.config.Constants.Direction;

// Java util import
import java.util.ArrayList;

/**
 * The Firetruck implementation. A sprite capable of moving and colliding with other sprites.
 * 
 * @author Archie
 * @since 16/12/2019
 */
public class Firetruck extends Truck {

    // Private values to be used in this class only
    private Boolean isFocused, isSpraying;
    private int focusID, toggleDelay;
    private float hoseWidth, hoseHeight;
    private ArrayList<Texture> waterFrames;
    private Polygon hoseRange;
    private ResourceBar waterBar;

    /**
     * Overloaded constructor containing all possible parameters.
     * Creates a firetruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at the given position.
     * 
     * @param textureSlices  The array of textures used to draw the firetruck with.
     * @param frames         The texture used to draw the water with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     * @param xPos           The x-coordinate for the firetruck.
     * @param yPos           The y-coordinate for the firetruck.
     */
    public Firetruck(ArrayList<Texture> textureSlices, ArrayList<Texture> frames, float[] properties, TiledMapTileLayer collisionLayer, int ID, float xPos, float yPos) {
        super(textureSlices, properties, collisionLayer, xPos, yPos);
        this.focusID = ID;
        this.waterFrames = frames;
        this.create();
        this.setPosition(xPos, yPos);
    }

    /**
     * Simplfied constructor for the firetruck, that doesn't require a position.
     * Creates a firetruck capable of moving and colliding with the tiledMap and other sprites.
     * It also requires an ID so that it can be focused with the camera. Drawn with the given
     * texture at (0,0).
     * 
     * @param textureSlices  The array of textures used to draw the firetruck with.
     * @param frames         The texture used to draw the water with.
     * @param properties     The properties of the truck inherited from Constants.
     * @param collisionLayer The layer of the map the firetruck collides with.
     * @param ID             The ID of the truck (for object focus).
     */
    public Firetruck(ArrayList<Texture> textureSlices, ArrayList<Texture> frames, float[] properties, TiledMapTileLayer collisionLayer, int ID) {
        super(textureSlices, properties, collisionLayer, 0, 0);
        this.focusID = ID;
        this.waterFrames = frames;
        this.create();
    }

    /**
     * Sets the health of the firetruck and its size provided in CONSTANTS.
     * Also initialises any properties needed by the firetruck.
     */
    private void create() {
        this.isSpraying = true;
        this.createWaterHose();
        this.setDeliveryRate((int) this.truckProperties[6]);
    }

    /**
     * Update the position and direction of the firetruck every frame.
     * @param batch  The batch to draw onto.
     * @param camera Used to get the centre of the screen.
     */
    public void update(Batch batch, Camera camera) {
        super.update(batch);
        drawVoxelImage(batch);
        if (this.isFocused) {
            // Look for key press input, then accelerate the firetruck in that direction
            if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
                super.applyAcceleration(Direction.LEFT);
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
                super.applyAcceleration(Direction.RIGHT);
            }          
            if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
                super.applyAcceleration(Direction.DOWN);
            } 
            if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
                super.applyAcceleration(Direction.UP);
            }
        } else if (this.isSpraying) {
            // If not driving the truck, turn the hose off
            this.toggleHose();
        }
      
        // Deplete water if spraying, toggle off when depleted
        if (this.isSpraying && this.waterBar.getCurrentAmount() > 0) {
            this.waterBar.subtractResourceAmount(this.getDeliveryRate());
        } else if (this.isSpraying) {
            this.toggleHose();
        }

        // Update the water bar position
        this.waterBar.setPosition(this.getX(), this.getCentreY());
        this.waterBar.update(batch);

        // Get the mouse input and get the angle from the truck to it. Get vector, normalise then get angle
        Vector2 hoseVector = new Vector2((this.getCentreX() - (camera.viewportWidth / 2) + Gdx.input.getX()), (this.getCentreY() + (camera.viewportHeight / 2) - Gdx.input.getY())); 
        Vector2 centreVector = new Vector2(this.getCentreX(),this.getCentreY()); 

        // Work out the vector between them
        hoseVector = hoseVector.sub(centreVector);
        hoseVector.nor();

        // Update the hose size and position. Angle it towards the mouse
        float scale = this.isSpraying && this.hoseRange.getScaleX() < this.truckProperties[4] ?
            0.05f : !this.isSpraying && this.hoseRange.getScaleX() > 0 ? -0.05f : 0;
        this.hoseRange.setScale(this.hoseRange.getScaleX() + scale, this.hoseRange.getScaleY() + scale);
        this.hoseRange.setPosition(this.getCentreX(), this.getCentreY());
        this.hoseRange.setRotation(hoseVector.angle());

        // Change batch aplha to match bar to fade hose in and out
        batch.setColor(1.0f, 1.0f, 1.0f, this.waterBar.getFade() * 0.9f);
        batch.draw(new TextureRegion(this.waterFrames.get(Math.round(this.getInternalTime() / 10) % 3)), this.hoseRange.getX(), this.hoseRange.getY() - this.hoseHeight / 2,
            0, this.hoseHeight / 2, this.hoseWidth, this.hoseHeight, this.hoseRange.getScaleX(), this.hoseRange.getScaleY(), hoseVector.angle(), true);
        // Return the batch to its original colours
        batch.setColor(1.0f, 1.0f, 1.0f, 1f);

        // Decrease timeout, used for keeping track of time between toggle presses
        if (this.toggleDelay > 0) this.toggleDelay -= 1;
    }

    /**
     * Creates the polygon for the hose and the water bar to store the firetruck's
     * water level.
     */
    private void createWaterHose() {
        // Get the scale of the hose and create its shape
        float rangeScale = this.truckProperties[4];
        this.hoseWidth = this.getHeight() * 4.5f * rangeScale;
        this.hoseHeight =  this.getWidth() * 0.65f * rangeScale;
        float[] hoseVertices = { // Starts facing right
            0, 0,
            (hoseWidth * 0.5f),  (hoseHeight / 2),
            (hoseWidth * 0.9f),  (hoseHeight / 2),
            (hoseWidth),  (hoseHeight / 2.25f),
            (hoseWidth), -(hoseHeight / 2.25f),
            (hoseWidth * 0.9f), -(hoseHeight / 2),
            (hoseWidth * 0.5f), -(hoseHeight / 2)
        }; 
        this.hoseRange = new Polygon(hoseVertices);
        // Create the water bar
        this.waterBar = new ResourceBar(Math.max(this.getWidth(), this.getHeight()), Math.min(this.getWidth(), this.getHeight()));
        this.waterBar.setColourRange(new Color[] { Color.BLUE });
        this.waterBar.setMaxResource((int) this.truckProperties[5]);
        // Start with the hose off
        this.toggleHose();
    } 

    /**
     * Checks if a polygon is within the range of the firetrucks hose.
     * @param polygon  The polygon that needs to be checked.
     * 
     * @return Whether the polygon is in the hose's range
     */
    public boolean isInHoseRange(Polygon polygon) {
        if (this.getInternalTime() % 10 != 0) return false;
        return Intersector.overlapConvexPolygons(polygon, this.hoseRange);
    }

    /**
     * Gets whether the firetruck is spraying water.
     * 
     * @return Whether the firetruck is spraying water.
     */
    public boolean isSpraying() {
        return this.isSpraying;
    }

    /**
     * Gets whether the firetruck has used any water.
     * 
     * @return Whether the firetruck has used any water.
     */
    public boolean isLowOnWater() {
        return this.waterBar.getCurrentAmount() < this.truckProperties[5];
    }

    /**
     * Toggles the fireturck's hose to spray if off and stop if on.
     */
    public void toggleHose() {
        if (this.toggleDelay <= 0) {
            this.toggleDelay = 20;
            this.isSpraying = !this.isSpraying && this.waterBar.getCurrentAmount() > 0;
            this.waterBar.setFade(false, !this.isSpraying);
        }
    }

    /**
     * Gets whether the firetruck is in focus.
     * 
     * @return Whether the firetruck is in focus (true) or not (false).
     */
    public boolean isFocused() {
        return this.isFocused;
    }

    /**
     * Gets the firetruck's focus ID
     * 
     * @return The firetruck's focus ID
     */
    public int getFocusID() {
        return this.focusID;
    }

    /**
     * Sets the firetruck in focus if its ID matches the one to focus.
     * @param focus The ID of the firetruck to focus on.
     */
    public void setFocus(int focus) {
        if (focus == focusID) {
            this.isFocused = true;
        } else {
            this.isFocused = false;
        }
    }

    /**
     * Gets the firetruck's water bar so it can be manipulated.
     * 
     * @return The firetruck's water bar.
     */
    public ResourceBar getWaterBar() {
        return this.waterBar;
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
        renderer.polygon(this.hoseRange.getTransformedVertices());
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
        for (Texture texture : this.waterFrames) {
            texture.dispose();
        }
    }
}
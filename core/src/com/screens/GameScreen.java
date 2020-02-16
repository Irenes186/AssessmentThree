package com.screens;

// LibGDX imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

// Tiled map imports for LibGDX
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

// Java util imports
import java.util.ArrayList;
import java.util.HashMap;

// Class imports
import com.kroy.Kroy;
import com.classes.Firetruck;
import com.classes.Projectile;
import com.classes.Firestation;
import com.classes.Alientruck;
import com.classes.ETFortress;
import com.miniGame.MiniGameScreen;

// Config imports
import com.config.ETFortressFactory;
import com.config.ETFortressType;

// Constants import
import static com.config.Constants.FONT_Y;
import static com.config.Constants.SCORE_X;
import static com.config.Constants.TIME_X;
import static com.config.Constants.LERP;
import static com.config.Constants.MIN_ZOOM;
import static com.config.Constants.MAX_ZOOM;
import static com.config.Constants.MAP_SCALE;
import static com.config.Constants.TILE_DIMS;
import static com.config.Constants.DEBUG_ENABLED;
import static com.config.Constants.FiretruckOneProperties;
import static com.config.Constants.FiretruckTwoProperties;
import static com.config.Constants.FiretruckThreeProperties;
import static com.config.Constants.FiretruckFourProperties;
import static com.config.Constants.AlientruckProperties;
import static com.config.Constants.FIRETRUCK_DAMAGE;

/**
 * Display the main game.
 * 
 * @author Archie
 * @since 23/11/2019
 */
public class GameScreen extends BasicScreen {

	// Private values for game screen logic
	private ShapeRenderer shapeRenderer;
	private Batch batch;

	// Private values for tiled map
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private int[] foregroundLayers;
    private int[] backgroundLayers;

	// Private values for the game

	public static int score;
	private int time, startTime, focusedID;
	private float zoomDelay;
	private Texture projectileTexture;
	private boolean upgraded;
	private boolean baseDestroyed;
	private HashMap<ETFortressType, Boolean> hasSpawned;
	
	// Private arrays to group sprites
	private ArrayList<Firetruck> firetrucks;
	private HashMap<ETFortressType, Alientruck> alienTrucksToAdd;
	private ArrayList<Alientruck> alientrucks;
	private ArrayList<Firetruck> firetrucksToRemove;
	private ArrayList<Alientruck> alientrucksToRemove;
	private ArrayList<ETFortress> ETFortresses;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Projectile> projectilesToRemove;
	private Firestation firestation;

	//Win and lose variables
	public static boolean gameWon = true, gameLost = true;

	/**
	 * The constructor for the main game screen. All main game logic is
	 * contained.
	 * 
	 * @param gam The game object.
	 */
	public GameScreen(final Kroy game) {
		super(game);

		GameScreen.score = 0;
	    this.baseDestroyed = false;
		this.upgraded = true;
		// ---- 1) Create new instance for all the objects needed for the game ---- //

		// Load the map, set the unit scale
		this.map = new TmxMapLoader().load("MapAssets/York_galletcity.tmx");
		this.renderer = new OrthogonalTiledMapRenderer(map, MAP_SCALE);
		this.shapeRenderer = new ShapeRenderer();

		// Create an array to store all projectiles in motion
		this.projectiles = new ArrayList<Projectile>();

		// Decrease time every second, starting at 5 minutes.
		this.time = 3 * 60;
		this.startTime = this.time;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				if (decreaseTime()) {
			        firestation.removeSprite(new Texture("MapAssets/UniqueBuildings/firestation_destroyed.png"));
			    }
			}
		}, 1, 1 );
		Timer.instance().start();
		// ---- 2) Initialise and set game properties ----------------------------- //

		// Initialise map renderer as batch to draw textures to
		this.batch = renderer.getBatch();

		// Set the game batch
		this.game.setBatch(this.batch);
		
		// ---- 3) Construct all textures to be used in the game here, ONCE ------ //

		// Select background and foreground map layers, order matters
        MapLayers mapLayers = map.getLayers();
        this.foregroundLayers = new int[] {
			mapLayers.getIndex("Buildings"),
			mapLayers.getIndex("Trees"),
        };  
        this.backgroundLayers = new int[] {
			mapLayers.getIndex("River"),
			mapLayers.getIndex("Road")
        };

		// Initialise textures to use for spites
		Texture firestationTexture = new Texture("MapAssets/UniqueBuildings/firestation.png");
		this.projectileTexture = new Texture("alienProjectile.png");
		
		// Create arrays of textures for animations
		ArrayList<Texture> waterFrames = new ArrayList<Texture>();
		ArrayList<Texture> firetruckBlue = new ArrayList<Texture>();
		ArrayList<Texture> firetruckRed = new ArrayList<Texture>();
		ArrayList<Texture> firetruckYellow = new ArrayList<Texture>();
		ArrayList<Texture> firetruckGreen = new ArrayList<Texture>();
		
		ArrayList<Texture> alientruckPink = new ArrayList<Texture>();

		for (int i = 1; i <= 3; i++) {
			Texture texture = new Texture("waterSplash" + i + ".png");
			waterFrames.add(texture);
		}
		for (int i = 20; i > 0; i--) {
			if (i == 6) { // Texture 5 contains identical slices except the lights are different
				Texture blue = new Texture("FiretruckBlue/FiretruckBLUE (6) A.png");
				Texture red = new Texture("FiretruckRed/FiretruckRED (6) A.png");
				Texture yellow = new Texture("FiretruckYellow/FiretruckYELLOW (6) A.png");
				Texture green = new Texture("FiretruckGreen/FiretruckGREEN (6) A.png");
				firetruckBlue.add(blue);
				firetruckRed.add(red);
				firetruckYellow.add(yellow);
				firetruckGreen.add(green);
			} else {
				Texture blue = new Texture("FiretruckBlue/FiretruckBLUE (" + i + ").png");
				Texture red = new Texture("FiretruckRed/FiretruckRED (" + i + ").png");
				Texture yellow = new Texture("FiretruckYellow/FiretruckYELLOW (" + i + ").png");
				Texture green = new Texture("FiretruckGreen/FiretruckGREEN (" + i + ").png");
				Texture alienPink = new Texture("AlienTruckPink/AlientruckPINK (" + i + ").png");
				firetruckBlue.add(blue);
				firetruckRed.add(red);
				firetruckYellow.add(yellow);
				firetruckGreen.add(green);
				alientruckPink.add(alienPink);
			}
		} 

		// ---- 4) Create entities that will be around for entire game duration - //

		// Create a new firestation 
		this.firestation = new Firestation(firestationTexture, 77 * TILE_DIMS, 36 * TILE_DIMS);

		// Initialise firetrucks array and add firetrucks to it
		this.firetrucks = new ArrayList<Firetruck>();
		this.firetrucks.add(new Firetruck(firetruckBlue, waterFrames, FiretruckOneProperties, (TiledMapTileLayer) map.getLayers().get("Collision"), 1, 80 * TILE_DIMS, 30 * TILE_DIMS));
		this.firetrucks.add(new Firetruck(firetruckRed, waterFrames, FiretruckTwoProperties, (TiledMapTileLayer) map.getLayers().get("Collision"), 2, 80 * TILE_DIMS, 32 * TILE_DIMS));
		this.firetrucks.add(new Firetruck(firetruckYellow, waterFrames, FiretruckThreeProperties, (TiledMapTileLayer) map.getLayers().get("Collision"), 3, 80 * TILE_DIMS, 28 * TILE_DIMS));
		this.firetrucks.add(new Firetruck(firetruckGreen, waterFrames, FiretruckFourProperties, (TiledMapTileLayer) map.getLayers().get("Collision"), 4, 80 * TILE_DIMS, 26 * TILE_DIMS));
		
		// Initialise alientrucks array and add alientrucks to it
		Vector2[] alientruckPath1 = new Vector2[] {new Vector2(80, 57), new Vector2(80, 43), new Vector2(85, 43), new Vector2(85, 39), new Vector2(70, 39), new Vector2(70, 45), new Vector2(66, 45), new Vector2(66, 57)};
	    Vector2[] alientruckPath2 = new Vector2[] {new Vector2(14, 83), new Vector2(14, 65), new Vector2(23, 65), new Vector2(23, 88), new Vector2(23, 65), new Vector2(14, 65)};
	    Vector2[] alientruckPath3 = new Vector2[] {new Vector2(62, 100), new Vector2(64, 100), new Vector2(64, 102), new Vector2(88, 102), new Vector2(88, 77), new Vector2(62, 77)};
	    
	    
	    this.alientrucks = new ArrayList<Alientruck>();
	    this.alienTrucksToAdd = new HashMap<ETFortressType, Alientruck>();
	    // truck spawning moved to render
		this.alienTrucksToAdd.put(ETFortressType.CLIFFORDS_TOWER, new Alientruck(alientruckPink, AlientruckProperties, (TiledMapTileLayer) map.getLayers().get("Collision"),
		        80 * TILE_DIMS, 57 * TILE_DIMS, 
		        alientruckPath1));
		this.alienTrucksToAdd.put(ETFortressType.RAIL_STATION, new Alientruck(alientruckPink, AlientruckProperties, (TiledMapTileLayer) map.getLayers().get("Collision"),
                14 * TILE_DIMS, 83 * TILE_DIMS, 
                alientruckPath2));
		this.alienTrucksToAdd.put(ETFortressType.YORK_MINSTER, new Alientruck(alientruckPink, AlientruckProperties, (TiledMapTileLayer) map.getLayers().get("Collision"),
                62 * TILE_DIMS, 100 * TILE_DIMS, 
                alientruckPath3));
		
//		        new Direction[] {Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP, Direction.LEFT, Direction.UP}));
		
//		// Initialise ETFortresses array and add ETFortresses to it
		this.ETFortresses = new ArrayList<ETFortress>();
		
		ETFortressFactory factory = new ETFortressFactory();
		this.ETFortresses.add(factory.createETFortress((ETFortressType.CLIFFORDS_TOWER)));
		this.ETFortresses.add(factory.createETFortress((ETFortressType.YORK_MINSTER)));
		this.ETFortresses.add(factory.createETFortress((ETFortressType.RAIL_STATION)));
		this.ETFortresses.add(factory.createETFortress((ETFortressType.STADIUM)));
		this.ETFortresses.add(factory.createETFortress((ETFortressType.FIBBERS)));
		this.ETFortresses.add(factory.createETFortress((ETFortressType.WINDMILL)));
		
		hasSpawned = new HashMap<ETFortressType, Boolean>();
		for (ETFortress fortress: ETFortresses) {
            hasSpawned.put(fortress.type, false);
        }
	}

	/**
	 * Actions to perform on first render cycle of the game
	 */
	@Override
	public void show() {
		// Set inital firetruck to focus the camera on
		this.focusedID = 1;
		setFiretruckFocus(this.focusedID);

		// Zoom delay is the time before the camera zooms out
		this.zoomDelay = 0;

		// Start the camera near the firestation
		this.camera.position.x = 80 * TILE_DIMS;
		this.camera.position.y = 30 * TILE_DIMS;

		// Create array to collect entities that are no longer used
		this.projectilesToRemove = new ArrayList<Projectile>();
		this.firetrucksToRemove = new ArrayList<Firetruck>();
		this.alientrucksToRemove = new ArrayList<Alientruck>();
	}

	/**
	 * Render function to display all elements in the main game.
	 * 
	 * @param delta The delta time of the game, updated every game second rather than frame.
	 */
	@Override
	public void render(float delta) {
		// MUST BE FIRST: Clear the screen each frame to stop textures blurring
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		// spawn alien trucks
		for (ETFortress fort: ETFortresses) {
		    if (!hasSpawned.get(fort.type)) {
		        if (fort.getHealthBar().getCurrentAmount() < fort.getHealthBar().getMaxAmount() &&
		                alienTrucksToAdd.get(fort.type) != null) {
		            alientrucks.add(alienTrucksToAdd.get(fort.type));
//		            alienTrucksToAdd.remove(fort.type);
		            hasSpawned.put(fort.type, true);
		        }
		    } else {
		        if (alienTrucksToAdd.get(fort.type).getInternalTime() % 1000 == 0) {
//		            System.out.println("update truck " + fort.type + 
//		                    " accel " + alienTrucksToAdd.get(fort.type).getAccelerationRate() + " to " + alienTrucksToAdd.get(fort.type).getAccelerationRate() * 1.2f
//		                    + ", max speed " + alienTrucksToAdd.get(fort.type).getMaxSpeed() + " to " + alienTrucksToAdd.get(fort.type).getMaxSpeed() * 1.1f);
//		            alienTrucksToAdd.get(fort.type).setAccelerationRate(alienTrucksToAdd.get(fort.type).getAccelerationRate() * 1.2f);
//		            alienTrucksToAdd.get(fort.type).setMaxSpeed(alienTrucksToAdd.get(fort.type).getMaxSpeed() * 1.1f);
//		            float oldHealth = alienTrucksToAdd.get(fort.type).getHealthBar().getCurrentAmount();
//		            float oldMax = alienTrucksToAdd.get(fort.type).getHealthBar().getMaxAmount();
//		            alienTrucksToAdd.get(fort.type).getHealthBar().setMaxResource((int)
//		                    (alienTrucksToAdd.get(fort.type).getHealthBar().getMaxAmount() * 1.1));
//		            alienTrucksToAdd.get(fort.type).getHealthBar().setCurrentAmount((int)
//                            (alienTrucksToAdd.get(fort.type).getHealthBar().getCurrentAmount() * 1.1));
		            alienTrucksToAdd.get(fort.type).upgrade(1.1f);
//		            if (oldHealth < oldMax) {
//		                alienTrucksToAdd.get(fort.type).getHealthBar().setCurrentAmount((int) oldHealth);
//		            } else {
//		                alienTrucksToAdd.get(fort.type).getHealthBar().setCurrentAmount((int) oldMax);
//		            }
		            System.out.println("update truck " + fort.type + " health " + alienTrucksToAdd.get(fort.type).getHealthBar().getMaxAmount() + " to "
		            + alienTrucksToAdd.get(fort.type).getHealthBar().getMaxAmount() * 1.1f);
		        }
		    }
		}

		// Check if the game should end
		checkIfGameOver();
		// ---- 1) Update camera and map properties each iteration -------- //
		
		// Set the TiledMapRenderer view based on what the camera sees
		renderer.setView(this.camera);

		// Align the debug view with the camera
		shapeRenderer.setProjectionMatrix(this.camera.combined);

		// Get the firetruck thats being driven so that the camera can follow it
		Firetruck focusedTruck = getFiretruckInFocus();
		// Tell the camera to update to the sprites position with a delay based on lerp and game time
		Vector3 cameraPosition = this.camera.position;
		float xDifference = focusedTruck.getCentreX() - cameraPosition.x;
		float yDifference = focusedTruck.getCentreY() - cameraPosition.y;
		cameraPosition.x += xDifference * LERP * delta;
		cameraPosition.y += yDifference * LERP * delta;
		
		// Zoom the camera out when firetruck moves
		float maxZoomHoldTime = MAX_ZOOM * 4, zoomSpeed = MIN_ZOOM * 0.01f, timeIncrement = MIN_ZOOM * 0.1f; 
		double speed = Math.max(Math.abs(focusedTruck.getSpeed().x), Math.abs(focusedTruck.getSpeed().y));
		boolean isMovingOrSpraying = speed > focusedTruck.getMaxSpeed() / 2  || focusedTruck.isSpraying();
		// If moving, increase delay before zooming out up until the limit
		if (isMovingOrSpraying && this.zoomDelay < maxZoomHoldTime) {
			this.zoomDelay += timeIncrement;
		} else if (this.zoomDelay > MIN_ZOOM) {
			this.zoomDelay -= 0.1f;
		}
		// If delay has been reached, zoom out, then hold until stationary
		if (this.zoomDelay > maxZoomHoldTime / 4) {
			this.camera.zoom = this.camera.zoom + zoomSpeed > MAX_ZOOM ? MAX_ZOOM : this.camera.zoom + zoomSpeed;
		} else if (this.camera.zoom > MIN_ZOOM) {
			this.camera.zoom -= zoomSpeed * 2;
		}
		this.camera.update();

		// ---- 2) Perform any checks for user input ---------------------- //

		// Check for user input to see if the focused truck should change
		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			focusedTruck.toggleHose();
		}
		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			this.focusedID += 1;
			if (this.focusedID > this.firetrucks.size()) {
				this.focusedID = 1;
			}
			setFiretruckFocus(this.focusedID); 
		}

		// ---- 3) Draw background, firetruck then foreground layers ----- //

		// Render background map layers
		renderer.render(this.backgroundLayers);

		// Render the firetrucks on top of the background
		if (DEBUG_ENABLED) shapeRenderer.begin(ShapeType.Line);
		batch.begin();

		// Call the update function of the sprites to draw and update them
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.update(batch, this.camera);
			if (firetruck.getHealthBar().getCurrentAmount() <= 0 && firetrucks.size() > 1) this.firetrucksToRemove.add(firetruck);
			if (DEBUG_ENABLED) firetruck.drawDebug(shapeRenderer);
		}
		
		for (Alientruck alientruck : this.alientrucks) {
		    alientruck.update(batch);
		    if (alientruck.getHealthBar().getCurrentAmount() <= 0) this.alientrucksToRemove.add(alientruck);
        if (DEBUG_ENABLED) alientruck.drawDebug(shapeRenderer);
		}

		// Close layer 
		batch.end();
		if (DEBUG_ENABLED) shapeRenderer.end();

		// Render map foreground layers
		renderer.render(foregroundLayers);

		// Render the remaining sprites, font last to be on top of all
		if (DEBUG_ENABLED) shapeRenderer.begin(ShapeType.Line);
		batch.begin();

		// Render sprites
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.update(batch);
			int destroyedETFortresses =0 ;
			if (ETFortress.getHealthBar().getCurrentAmount() <= 0) {
				destroyedETFortresses++;
			}
			//time and number of destroyed et fortresses can change over time
			if (destroyedETFortresses == 1){
			    this.baseDestroyed = true;
			}
			if (DEBUG_ENABLED) ETFortress.drawDebug(shapeRenderer);
		}
		for (Projectile projectile : this.projectiles) {
			projectile.update(batch);
			if (DEBUG_ENABLED) projectile.drawDebug(shapeRenderer);
			if (projectile.isOutOfMap()) this.projectilesToRemove.add(projectile);
		}
		this.firestation.update(batch);
		if (DEBUG_ENABLED) firestation.drawDebug(shapeRenderer);

		// Draw the score, time and FPS to the screen at given co-ordinates
		game.drawFont("Score: " + GameScreen.score,
			cameraPosition.x - this.camera.viewportWidth * SCORE_X * camera.zoom,
			cameraPosition.y + this.camera.viewportHeight * FONT_Y * camera.zoom);
		game.drawFont("Time: " + this.time, 
			cameraPosition.x + this.camera.viewportWidth * TIME_X * camera.zoom,
			cameraPosition.y + this.camera.viewportHeight * FONT_Y * camera.zoom);
		if (DEBUG_ENABLED) game.drawFont("FPS: "
			+ Gdx.graphics.getFramesPerSecond(),
			cameraPosition.x + this.camera.viewportWidth * TIME_X * camera.zoom,
			cameraPosition.y + this.camera.viewportHeight * FONT_Y * camera.zoom - 30
		);

		// Finish rendering
		batch.end();
		if (DEBUG_ENABLED) shapeRenderer.end();

		// ---- 4) Perform any calulcation needed after sprites are drawn - //

		// Remove projectiles that are off the screen and firetrucks that are dead
		this.projectiles.removeAll(this.projectilesToRemove);
		this.firetrucks.removeAll(this.firetrucksToRemove);
		this.alientrucks.removeAll(this.alientrucksToRemove);

		// Check for any collisions
		checkForCollisions();
		detectPatrolCollision ();

		//Check if fortress has to be upgraded and if so upgrade it.
		checkForUpgrade();

		//debugging
			if (Gdx.input.isKeyJustPressed(Keys.P)){
				for (ETFortress a: ETFortresses){
					a.getHealthBar().subtractResourceAmount(5);
				}
		}

	}

	/**
	 * Checks if any patrol is overlapping with a fireengine.
	 * If is is it removes the patrol from the game and starts the minigame.
	 */
    private void detectPatrolCollision () {
        Alientruck toRemove = null;
        for (Firetruck truck : firetrucks) {
            for (Alientruck alien : alientrucks) {
                if (truck.getHitBox().getBoundingRectangle().overlaps(alien.getHitBox().getBoundingRectangle())) {
                    toRemove = alien;
                    game.setScreen (new MiniGameScreen (game, this, this.focusedID));
                }
            }
        }
        alientrucks.remove (toRemove);
    }

	/**
     * Checks to see if the player has won or lost the game. Navigates back to the main menu
	 * if they won or lost.
     */
	private void checkIfGameOver() {
		gameLost = true;
		gameWon = true;
		// Check if any firetrucks are still alive
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.getHealthBar().getCurrentAmount() > 0) gameLost = false;
		}
		// Check if any fortresses are still alive
		for (ETFortress ETFortress : this.ETFortresses) {
			if (ETFortress.getHealthBar().getCurrentAmount() > 0) gameWon = false;
		}
		if (gameWon || gameLost) {
			dispose();
			this.game.setScreen(new ResultScreen(this.game, GameScreen.score));
		}
	}

	/**
     * Checks to see if any collisions have occurred
     */
	private void checkForCollisions() {
		// Vector to store the minimum movement to seperate two sprites
		Intersector.MinimumTranslationVector seperationVector = new Intersector.MinimumTranslationVector();
		// Check each firetruck to see if it has collided with anything
		for (Firetruck firetruckA : this.firetrucks) {
			for (Firetruck firetruckB : this.firetrucks) {
				// Check if the firetruck overlaps another firetruck, but not itself
				if (!firetruckA.equals(firetruckB) && Intersector.overlapConvexPolygons(firetruckA.getHitBox(), firetruckB.getHitBox(), seperationVector)) {
					firetruckA.collisionOccurred(seperationVector.normal);
				}
			}
			// Check if it overlaps with an ETFortress
			for (ETFortress ETFortress : this.ETFortresses) {
				if (ETFortress.getHealthBar().getCurrentAmount() > 0 && firetruckA.isInHoseRange(ETFortress.getHitBox())) {
					ETFortress.getHealthBar().subtractResourceAmount(FIRETRUCK_DAMAGE);
					GameScreen.score += 10;
				}
				if (ETFortress.isInRadius(firetruckA.getHitBox()) && ETFortress.canShootProjectile()) {
					Projectile projectile = new Projectile(this.projectileTexture, ETFortress.getCentreX(), ETFortress.getCentreY(), ETFortress.getProjectileDamage());
					projectile.calculateTrajectory(firetruckA.getHitBox());
					this.projectiles.add(projectile);
				}
			}
			// Check if it overlaps with an Alientruck
            for (Alientruck alientruck : this.alientrucks) {
                if (alientruck.getHealthBar().getCurrentAmount() > 0 && firetruckA.isInHoseRange(alientruck.getHitBox())) {
                    alientruck.getHealthBar().subtractResourceAmount(FIRETRUCK_DAMAGE);
                    GameScreen.score += 10;
                }
            }
			// Check if firetruck is hit with a projectile
			for (Projectile projectile : this.projectiles) {
				if (Intersector.overlapConvexPolygons(firetruckA.getHitBox(), projectile.getHitBox())) {
					firetruckA.getHealthBar().subtractResourceAmount(projectile.getDamage());
					if (GameScreen.score > 10) GameScreen.score -= 10;
					projectilesToRemove.add(projectile);
				}
			}
			// Check if it is in the firestation's radius. Only repair the truck if it needs repairing.
			// Allows multiple trucks to be in the radius and be repaired or refilled every second.
			if (this.time > 0 && (firetruckA.isDamaged() || firetruckA.isLowOnWater()) && this.firestation.isInRadius(firetruckA.getHitBox())) {
				this.firestation.repair(firetruckA);
			}
		}
	}

	/**
	 * Decreases time by 1, called every second by the timer
	 * 
	 * @return boolean  true when time has run out
	 */
	private boolean decreaseTime() {
	    if (this.baseDestroyed) {
	        if (this.time > 0) {
	            this.time -= 1;
	            return false;
	        }
	        return true;
	    }
		return false;
	}
	
	/*This method will:
	check if it is time for ET fortresses to be upgraded.
	If it is time: upgrade() method will be called in the class ETFortress.
	*/
	private void checkForUpgrade() {
		if((this.time % 60 == 0) && (this.upgraded == false)) {
			int fortressAmount = this.ETFortresses.size();
			for(int i = 0; i < fortressAmount; i++) {
				this.ETFortresses.get(i).upgrade();
			}
			
			this.upgraded = true;
		} else if((this.time % 5 == 0) && (this.time % 60 != 0) && (this.upgraded == true) && (this.time < this.startTime)) {
			this.upgraded = false;
		}
	}

	/**
	 * Get the firetruck the user is currently controlling.
	 * 
	 * @return The firetruck with user's focus.
	 */
	public Firetruck getFiretruckInFocus() {
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.isFocused() && firetruck.getHealthBar().getCurrentAmount() > 0) {
				return firetruck;
			}
		}
		// If no firetruck alive focus next one
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.getHealthBar().getCurrentAmount() > 0) {
				firetruck.setFocus(firetruck.getFocusID());
				return firetruck;
			}
		}
		return this.firetrucks.get(0);
	}

	/**
	 * Set which firetruck the user is currently controlling.
	 * 
	 * @param focusID The ID of the firetruck to focus on.
	 */
	private void setFiretruckFocus(int focusID) {
		for (Firetruck firetruck : this.firetrucks) {
			if (firetruck.getHealthBar().getCurrentAmount() > 0) {
				firetruck.setFocus(focusID);
			}
		}
	}

	/**
	 * Resize the screen.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	@Override
	public void resize(int width, int height) {
		this.camera.viewportHeight = height;
		this.camera.viewportWidth = width;
        this.camera.update();
	}

	/**
	 * Dispose of main game assets upon completion.
	 */
	@Override
	public void dispose() {
		this.projectileTexture.dispose();
		this.firestation.dispose();
		for (Firetruck firetruck : this.firetrucks) {
			firetruck.dispose();
		}
		for (ETFortress ETFortress : this.ETFortresses) {
			ETFortress.dispose();
		}
		for (Alientruck alientruck : this.alientrucks) {
		    alientruck.dispose();
		}
	}

}

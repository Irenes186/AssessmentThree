package com.config;

/**
 * Game constants for use by Kroy.
 */
public final class Constants {

    private Constants() {
        // Any constants that need instantiation go here
    }

    // Firetruck One properties.
    public static final float[] FiretruckOneProperties = {
        100,  // HEALTH
        10f,  // ACCELERATION
        300f, // MAX_SPEED
        0.8f, // RESTITUTION
        1.2f, // RANGE
        10000000,  // WATER MAX
        10000,    //DELIVERY RATE
    };

    // Firetruck Two properties
    public static final float[] FiretruckTwoProperties = {
        110,  // HEALTH
        15f,  // ACCELERATION
        400f, // MAX_SPEED
        0.6f, // RESTITUTION
        1.1f,// RANGE
        200,  // WATER MAX
        3,    //DELIVERY RATE
    };
    // Firetruck Three properties
    public static final float[] FiretruckThreeProperties = {
            120,  // HEALTH
            15f,  // ACCELERATION
            500f, // MAX_SPEED
            0.6f, // RESTITUTION
            1.5f,// RANGE
            500,  // WATER MAX
            2,    //DELIVERY RATE
    };
    // Firetruck Four properties
    public static final float[] FiretruckFourProperties = {
            150,  // HEALTH
            15f,  // ACCELERATION
            100f, // MAX_SPEED
            0.6f, // RESTITUTION
            1.3f,// RANGE
            400,  // WATER MAX
            1,    //DELIVERY RATE
    };
    // Alientruck properties
    public static final float[] AlientruckProperties = {
            20,  // HEALTH
            15f,  // ACCELERATION
            100f, // MAX_SPEED
            0.6f, // RESTITUTION
    };

     // Enums
     public static enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    // Debug mode
    public static final boolean DEBUG_ENABLED = false;

    // Game settings
    public static final String GAME_NAME = "Kroy";
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final float MAP_SCALE = 6f;
    public static final float MAP_WIDTH = 117 * (8 * MAP_SCALE);
    public static final float MAP_HEIGHT = 10000 * (8 * MAP_SCALE);
    public static final int TILE_DIMS = (int) (8 * MAP_SCALE);
    public static final String COLLISION_TILE = "BLOCKED";

    // Time durations
    public static final float BAR_FADE_DURATION = 3;
    public static final int FIRETRUCK_REPAIR_SPEED = 75;

    // Camera settings
    public static final float LERP = 1.5f;
    public static final float MIN_ZOOM = 1f;
    public static final float MAX_ZOOM = 1.75f;

    // Screen elements sizing
    public static final float FONT_Y = 0.45f;
    public static final float SCORE_X = 0.47f;
    public static final float TIME_X = 0.4f;

    // Sprite properties
    // Damage
    public static final int FIRETRUCK_DAMAGE = 2;
    // Health
    public static final int ETFORTRESS_HEALING = 1;
    public static final int FIRESTATION_HEALTH = 1000;
    // Speed
    public static final float PROJECTILE_SPEED = 400;
    // Size
    public static final float FIRETRUCK_WIDTH =  2*TILE_DIMS;
    public static final float FIRETRUCK_HEIGHT = 1*TILE_DIMS;
    public static final float FIRESTATION_WIDTH = 9.5f*TILE_DIMS;
    public static final float FIRESTATION_HEIGHT=5.5f*TILE_DIMS;
    public static final float ETFORTRESS_WIDTH = 5*TILE_DIMS;
    public static final float ETFORTRESS_HEIGHT =5*TILE_DIMS;
    public static final float PROJECTILE_WIDTH = 1*TILE_DIMS;
    public static final float PROJECTILE_HEIGHT =0.5f*TILE_DIMS;

}
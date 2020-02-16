package com.config;

import com.badlogic.gdx.graphics.Texture;
import com.classes.ETFortress;
import com.config.Constants;

/* Data class for use when providing unique parameters to the spawning of a new ETFortress.
 * 
 */
public class ETFortressFactory {
	public Texture texture;
	public Texture destroyedTexture;
	public int projectileDamage;
    public int maxHealth;
    private float x;
    private float y;
    private float scaleX;
    private float scaleY;

	// Fortress-specific values
	public ETFortress createETFortress (ETFortressType type) {
		switch (type) {
		    case CLIFFORDS_TOWER:
		        texture = new Texture("MapAssets/UniqueBuildings/cliffordstower.png");
		        destroyedTexture = new Texture("MapAssets/UniqueBuildings/cliffordstower_wet.png");
		        projectileDamage = 10;
		        maxHealth = 100;
		        x = 69;
		        y = 51;
		        scaleX = 1;
		        scaleY = 1;
		        break;
		    case RAIL_STATION:
		        texture = new Texture("MapAssets/UniqueBuildings/railstation.png");
		        destroyedTexture = new Texture("MapAssets/UniqueBuildings/railstation_wet.png");
		        projectileDamage = 15;
		        maxHealth = 50;
		        x = 1;
                y = 72.75f;
                scaleX = 3;
                scaleY = 2.5f;
		        break;
		    case YORK_MINSTER:
		        texture = new Texture("MapAssets/UniqueBuildings/Yorkminster.png");
                destroyedTexture = new Texture("MapAssets/UniqueBuildings/Yorkminster_wet.png");
                projectileDamage = 5;
                maxHealth = 150;
                x = 68.25f;
                y = 82.25f;
                scaleX = 2;
                scaleY = 3.25f;
		        break;
		    case STADIUM:
		        texture = new Texture("MapAssets/UniqueBuildings/YorkStadium.png");
                destroyedTexture = new Texture("MapAssets/UniqueBuildings/YorkStadium_wet.png");
                projectileDamage = 7;
                maxHealth = 125;
                x = 36;
                y = 69;
                scaleX = 1;
                scaleY = 1;
		        break;
		    case FIBBERS:
		        texture = new Texture("MapAssets/UniqueBuildings/fibbers.png");
                destroyedTexture = new Texture("MapAssets/UniqueBuildings/fibbers_wet.png");
                projectileDamage = 13;
                maxHealth = 75;
                x = 91;
                y = 70;
                scaleX = 1;
                scaleY = 1;
		        break;
		    case WINDMILL:
		        texture = new Texture("MapAssets/UniqueBuildings/windmill.png");
                destroyedTexture = new Texture("MapAssets/UniqueBuildings/windmill_wet.png");
                projectileDamage = 9;
                maxHealth = 110;
                x = 25;
                y = 48;
                scaleX = 1;
                scaleY = 1;
		        break;
	        default:
	            throw new RuntimeException("Requested ETFortressType " + type + " does not exist.");
		}
		return new ETFortress(texture, destroyedTexture, projectileDamage, maxHealth, scaleX, scaleY, x * Constants.TILE_DIMS, y * Constants.TILE_DIMS);
	}
}

package com.config;

import com.badlogic.gdx.graphics.Texture;

/* Data class for use when providing unique parameters to the spawning of a new ETFortress.
 * 
 */
public class ETFortressParameters {
	public Texture texture;
	public Texture destroyedTexture;
	public int projectileDamage;
    public int maxHealth;
	
	// default parameters
	public ETFortressParameters() {
		texture = new Texture("MapAssets/UniqueBuildings/cliffordstower.png");
		destroyedTexture = new Texture("MapAssets/UniqueBuildings/cliffordstower_wet.png");
		projectileDamage = 10;
		maxHealth = 100;
	}
	
	// providing values on creation
	public ETFortressParameters (Texture texture, Texture destroyedTexture, int projectileDamage, int maxHealth) {
		this.texture = texture;
		this.destroyedTexture = destroyedTexture;
		this.projectileDamage = projectileDamage;
		this.maxHealth = maxHealth;
	}
	
	// Fortress-specific values
	public ETFortressParameters (ETFortressType type) {
		switch (type) {
		    case CLIFFORDS_TOWER:
		        texture = new Texture("MapAssets/UniqueBuildings/cliffordstower.png");
		        destroyedTexture = new Texture("MapAssets/UniqueBuildings/cliffordstower_wet.png");
		        projectileDamage = 10;
		        maxHealth = 100;
		        break;
		    case RAIL_STATION:
		        texture = new Texture("MapAssets/UniqueBuildings/railstation.png");
		        destroyedTexture = new Texture("MapAssets/UniqueBuildings/railstation_wet.png");
		        projectileDamage = 15;
		        maxHealth = 50;
		        break;
		    case YORK_MINSTER:
		        texture = new Texture("MapAssets/UniqueBuildings/Yorkminster.png");
                destroyedTexture = new Texture("MapAssets/UniqueBuildings/Yorkminster_wet.png");
                projectileDamage = 5;
                maxHealth = 150;
		        break;
	        default:
	            throw new RuntimeException("Requested ETFortresType " + type + " does not exist.");
		}
	}
}
package com.classes;

import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.FiretruckOneProperties;
import static com.config.Constants.TILE_DIMS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

//Import Kroy game
import com.kroy.Kroy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Polygon;
import com.classes.Projectile;
import com.classes.Firetruck;

// Import test runner
import com.testrunner.GdxTestRunner;


/**
 * @author Jand, Rammmmmmesss jand
 *
 */
@RunWith(GdxTestRunner.class)
class ProjectileTest {

	SpriteBatch batch;

	@BeforeAll
	void setUp () throws Exception {

		ArrayList<Texture> fireEngineTexture = new ArrayList<Texture>();
		ArrayList<Texture> waterFrames = new ArrayList<Texture>();


		for (int i = 1; i <= 3; i++) {
			waterFrames.add(new Texture("waterSplash" + i + ".png"));
		}

		for (int i = 20; i > 0; i--) {
			if (i != 6) {
				fireEngineTexture.add (new Texture("firetruckBlue/FiretruckBLUE (" + i + ").png"));
			}
		}
		fireEngineTexture.add (new Texture("firetruckBlue/FiretruckBLUE (6).png"));

		Firetruck firetruck = new Firetruck(fireEngineTexture, waterFrames, FiretruckOneProperties, new TiledMapTileLayer(10, 10, 2, 2), 1, 1, 1); 
		batch = new SpriteBatch();
	}
	/**
	 * Test method for {@link com.classes.Projectile#update(com.badlogic.gdx.graphics.g2d.Batch)}.
	 */
	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.Projectile#Projectile(com.badlogic.gdx.graphics.Texture, float, float)}.
	 */
	@Test
	void testProjectile() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.Projectile#calculateTrajectory(com.badlogic.gdx.math.Polygon)}.
	 */
	@Test
	void testCalculateTrajectory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for testing a Projectile would be removed when out of view.
	 * {@link com.classes.Projectile#isOutOfView(com.badlogic.gdx.math.Vector3)}.
	 * 
	 * @author Joshua
	 */
	@Test
	void testProjectileIsOutOfView() {
		// Create camera position to test.
		Vector3 testCameraPosition = new Vector3(80 * TILE_DIMS, 30 * TILE_DIMS, 0);
		
		// Create test projectile.
		// TEXTURE NOT WORKING
		assertTrue(Gdx.files.internal("../core/assets/button.png").exists());
		Texture testTexture = new Texture("../core/assets/button.png");
		Projectile testProjectile = new Projectile(testTexture, 0, 0, 1);
		
		// Test projectile is out of view.
		assertTrue(testProjectile.isOutOfView(testCameraPosition));
	}

	/**
	 * Test method for {@link com.classes.Projectile#isOutOfMap()}.
			*/
	@Test
	void testIsOutOfMap() {

		Projectile projectile =  new Projectile(new Texture("alienProjectile.png"),0f, 0f, 1);
		Polygon polygon = new Polygon();
		polygon.setPosition(-10, -10);
		projectile.calculateTrajectory(polygon);

		projectile.update(batch);

		assertFalse(projectile.isOutOfMap());
	}
}

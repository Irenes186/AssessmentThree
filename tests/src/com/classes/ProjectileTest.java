package com.classes;

import static com.config.Constants.TILE_DIMS;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
// Import test runner
import com.testrunner.GdxTestRunner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * @author Jand, Rammmmmmesss jand
 *
 */
//@TestInstance(Lifecycle.PER_CLASS)
@RunWith(GdxTestRunner.class)
class ProjectileTest {

	SpriteBatch batch;

	@Mock
	Texture tex;


	//@BeforeAll
	void setUp () throws Exception {

		//ArrayList<Texture> fireEngineTexture = new ArrayList<Texture>();
		//ArrayList<Texture> waterFrames = new ArrayList<Texture>();


		//for (int i = 1; i <= 3; i++) {
		//	waterFrames.add(new Texture("waterSplash" + i + ".png"));
		//}

		//for (int i = 20; i > 0; i--) {
		//	if (i != 6) {
		//		fireEngineTexture.add (new Texture("firetruckBlue/FiretruckBLUE (" + i + ").png"));
		//	}
		//}
		//fireEngineTexture.add (new Texture("firetruckBlue/FiretruckBLUE (6).png"));

		//Firetruck firetruck = new Firetruck(fireEngineTexture, waterFrames, FiretruckOneProperties, new TiledMapTileLayer(10, 10, 2, 2), 1, 1, 1); 
		Gdx.gl = mock(GL20.class);
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

		batch = mock(SpriteBatch.class);
		tex = mock(Texture.class); // Texture("alienProjectile.png");
		//Projectile projectile = mock(Projectile.class);
		Projectile projectile =  new Projectile(tex,0f, 0f, 1);
		//Polygon polygon = new Polygon();
		Polygon polygon = mock(Polygon.class);
		polygon.setPosition(-10, -10);
		projectile.calculateTrajectory(polygon);

		projectile.update(batch);

		//System.out.println(projectile.isOutOfMap());
		assertTrue(projectile.isOutOfMap());
		//assertTrue(false);
	}
}

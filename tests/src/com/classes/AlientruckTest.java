package com.classes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

// Import Java File type
import java.io.File;
import java.util.ArrayList;

//LibGDX imports
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

//Import Kroy game
import com.kroy.Kroy;
import com.classes.Alientruck;
import com.config.ETFortressFactory;
import com.config.ETFortressType;
import com.config.Constants;

import com.testrunner.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class AlientruckTest {    
//    @Test
//    void testTruckFollowPath() {
//        // truck going from (0, 0) to (0, 1) to (1, 1)
//        Alientruck testTruck = new Alientruck(new ArrayList<Texture>(), Constants.AlientruckProperties, new TiledMapTileLayer(5, 5, 1, 1), 0, 0, new Vector2[] {new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1)});
//        if (!new Vector2(testTruck.getCentreX(), testTruck.getCentreY()).isZero()) {
//            fail("truck did not start at (0, 0)");
//        }
//        
//        if () {
//            
//        }
//    }
}

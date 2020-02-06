package com.MiniGame;


// LibGDX imports
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;

import com.sprites.SimpleSprite;

public class MiniFireEngine extends SimpleSprite {

    private Texture texture;

    public MiniFireEngine () {
        super (new Texture ("MiniGame/fireEngine.png"));

        this.setSize (100, 100);
        this.setPosition (50, 175);

    }
}

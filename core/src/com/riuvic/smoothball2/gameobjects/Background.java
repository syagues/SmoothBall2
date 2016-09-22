package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.riuvic.smoothball2.configuration.Configuration;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.ColorManager;


public class Background extends GameObject {
    private ColorManager colorManager;

    public Background(GameWorld world, float x, float y, float width, float height,
                      TextureRegion texture) {
        super(world, x, y, width, height, texture,
                world.parseColor(Configuration.COLOR_BACKGROUND_COLOR, 1f), Shape.RECTANGLE);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super.render(batch, shapeRenderer);
    }
}

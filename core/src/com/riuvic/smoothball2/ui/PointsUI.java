package com.riuvic.smoothball2.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.configuration.Settings;
import com.riuvic.smoothball2.gameobjects.Coin;
import com.riuvic.smoothball2.gameobjects.GameObject;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.helpers.FlatColors;


public class PointsUI extends GameObject {

    private Text text;

    public PointsUI(GameWorld world, float x, float y, float width, float height,
                    TextureRegion texture,
                    Color color, Shape shape, int align) {
        super(world, x, y, width, height, texture, color, shape);

        text = new Text(world, -100, y, width - x, 90, AssetLoader.square,
                FlatColors.WHITE, AssetLoader.getCoinNumber() + "", AssetLoader.fontS,
                FlatColors.WHITE, 3,
                Align.center);
        start();
    }

    @Override
    public void update(float delta) {

        text.update(delta);
        text.setPosition(position.x + getSprite().getWidth() / 2 + 50, position.y - 30);
        super.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontshader) {
        super.render(batch, shapeRenderer);
        text.render(batch, shapeRenderer, fontshader);
    }

    public void setText(String string) {
        text.setText(string);
    }

    public void start() {
        effectX(position.x - world.gameWidth, position.x, 1f, 0f);
    }

}

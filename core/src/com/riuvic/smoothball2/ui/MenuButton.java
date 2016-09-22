package com.riuvic.smoothball2.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.configuration.Settings;
import com.riuvic.smoothball2.gameobjects.GameObject;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.tweens.Value;


public class MenuButton extends GameObject {

    private Color color;
    private Value time = new Value();
    private Sprite icon, iconShadow, back;

    public MenuButton(final GameWorld world, float x, float y, float width, float height,
                      TextureRegion texture, Color color, Shape shape, TextureRegion buttonIcon) {
        super(world, x, y, width, height, texture, color, shape);
        this.color = color;

        if (Settings.SHADOWS)
            setShadow(true);
        icon = new Sprite(buttonIcon);
        icon.setPosition(getPosition().x, getPosition().y);
        icon.setSize(width, height);

        icon.setScale(0.8f, 0.8f);
        icon.setOriginCenter();

        if (Settings.SHADOWS) {
            iconShadow = new Sprite(buttonIcon);
            iconShadow.setPosition(getPosition().x, getPosition().y);
            iconShadow.setSize(width, height);
            iconShadow.setScale(0.8f, 0.8f);
            iconShadow.setOriginCenter();
            iconShadow.setColor(getShadowSprite().getColor());
            iconShadow.setAlpha(getShadowSprite().getColor().a);
        }

        back = new Sprite(AssetLoader.dot);
        back.setPosition(getPosition().x, getPosition().y);
        back.setSize(width, height);
        back.setColor(color);
        sprite.setColor(Color.WHITE);

    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super.render(batch, shapeRenderer);
        if (Settings.SHADOWS) iconShadow.setAlpha(getShadowSprite().getColor().a);
        if (isPressed) {
            //icon.setAlpha(.5f);
            getSprite().setAlpha(0.7f);
        } else {
            // icon.setAlpha(1f);
            getSprite().setAlpha(1f);
        }
        if (Settings.SHADOWS) iconShadow.draw(batch);
        back.draw(batch);
        icon.draw(batch);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        icon.setPosition(getPosition().x, getPosition().y);
        icon.setScale(getSprite().getScaleX());
        back.setScale(.85f);
        back.setOriginCenter();
        back.setPosition(getPosition().x, getPosition().y);
        if (Settings.SHADOWS) iconShadow.setPosition(getPosition().x + 5, getPosition().y - 5);
    }


    public void setIcon(TextureRegion icon1) {
        icon.setRegion(icon1);
    }
}
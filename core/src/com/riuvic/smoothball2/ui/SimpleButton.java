package com.riuvic.smoothball2.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.tweens.Value;
import com.riuvic.smoothball2.tweens.ValueAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;


public class SimpleButton {

    private float x, y, width, height;

    private TextureRegion buttonUp;
    private TextureRegion buttonDown;

    private Rectangle bounds;
    private Sprite sprite;

    private GameWorld world;
    public boolean isPressed = false;
    private TweenManager manager;
    private String text;

    public SimpleButton(final GameWorld world, final float x, float y, float width, float height,
                        TextureRegion buttonUp, TextureRegion buttonDown, Color color,
                        String text) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonUp = buttonUp;
        this.buttonDown = buttonDown;
        this.text = text;
        bounds = new Rectangle(x, y, width, height);
        sprite = new Sprite(buttonUp);
        sprite.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        sprite.setColor(color);
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
    }

    public boolean isClicked(int screenX, int screenY) {
        return bounds.contains(screenX, screenY);
    }

    public void draw(SpriteBatch batcher, ShaderProgram fontShader) {
        if (isPressed) {
            sprite.setAlpha(.5f);
            sprite.draw(batcher);
        } else {
            sprite.setAlpha(1f);
            sprite.draw(batcher);
        }
        batcher.setShader(fontShader);
        AssetLoader.fontS.draw(batcher, text, x, y, width, Align.center, true);

        batcher.setShader(null);
    }

    public boolean isTouchDown(int screenX, int screenY) {
        if (bounds.contains(screenX, screenY)) {
            isPressed = true;
            return true;
        }
        return false;
    }

    public boolean isTouchUp(int screenX, int screenY) {
        if (bounds.contains(screenX, screenY) && isPressed) {
            isPressed = false;
            return true;
        }
        isPressed = false;
        return false;
    }

    public void update(float delta) {
        manager.update(delta);
    }
}
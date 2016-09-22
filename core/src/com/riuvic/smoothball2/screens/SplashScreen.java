package com.riuvic.smoothball2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.riuvic.smoothball2.configuration.Configuration;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.helpers.FlatColors;
import com.riuvic.smoothball2.maingame.ActionResolver;
import com.riuvic.smoothball2.maingame.Radical;
import com.riuvic.smoothball2.tweens.SpriteAccessor;
import com.riuvic.smoothball2.tweens.Value;
import com.riuvic.smoothball2.tweens.ValueAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class SplashScreen implements Screen {

    float height;
    private TweenManager manager;
    private SpriteBatch batcher;
    private Sprite sprite;
    private Radical game;
    private ActionResolver actionResolver;
    private Sprite spriteBack;

    public SplashScreen(Radical game, ActionResolver actionResolver) {
        this.game = game;
        this.actionResolver = actionResolver;
        spriteBack = new Sprite(AssetLoader.background);
        spriteBack.setColor(FlatColors.WHITE);
        spriteBack.setPosition(0, 0);
        spriteBack.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        sprite = new Sprite(AssetLoader.logo);
        sprite.setColor(1, 1, 1, 0);

        float width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        float desiredWidth = width * .39f;
        float scale = desiredWidth / sprite.getWidth();

        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        sprite.setPosition((width / 2) - (sprite.getWidth() / 2), (height / 2)
                - (sprite.getHeight() / 2));
        sprite.setAlpha(0);
        setupTween();
        batcher = new SpriteBatch();
    }

    private void setupTween() {
        AssetLoader.load();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();

        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new GameScreen(game, actionResolver));
            }
        };

        if (!Configuration.SPLASHSCREEN) {
            //AssetLoader.load();
            game.setScreen(new GameScreen(game, actionResolver));
        }
        Tween.to(sprite, SpriteAccessor.ALPHA, 1.5f).target(1).delay(1f)
                .ease(TweenEquations.easeInOutQuad).repeatYoyo(1, .2f)
                .setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);
        // Tween.to(r1, -1, 1f).target(0 - sprite.getHeight())
        // .ease(TweenEquations.easeInOutQuad).delay(1.6f).start(manager);
    }

    @Override
    public void render(float delta) {
        manager.update(delta);
        Gdx.gl.glClearColor(92.5f, 94.1f, 94.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        spriteBack.draw(batcher);
        sprite.draw(batcher);
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
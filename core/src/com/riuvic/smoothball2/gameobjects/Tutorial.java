package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.riuvic.smoothball2.configuration.Configuration;
import com.riuvic.smoothball2.configuration.Settings;
import com.riuvic.smoothball2.gameworld.GameRenderer;
import com.riuvic.smoothball2.gameworld.GameState;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.helpers.FlatColors;
import com.riuvic.smoothball2.tweens.Value;
import com.riuvic.smoothball2.ui.Text;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;


public class Tutorial extends GameObject {

    private GameObject finger;
    private Value timer = new Value();
    private Text text, textLife, textProt, textSlow;
    private Coin coinLife, coinProt, coinSlow;
    private GameObject circle;

    public Tutorial(GameWorld world, float x, float y, float width, float height,
                    TextureRegion texture,
                    Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        finger = new GameObject(world, world.gameWidth / 2 - 120, 200, 240, 240,
                AssetLoader.finger,
                FlatColors.WHITE, GameObject.Shape.RECTANGLE);
        textLife = new Text(world, -world.gameWidth, world.gameHeight / 2 + 350, world.gameWidth, 100,
                AssetLoader.square, Color.WHITE,
                Configuration.TUTORIAL_LIFE, AssetLoader.fontXS, FlatColors.WHITE, 0, Align.center);
        textProt = new Text(world, -world.gameWidth, world.gameHeight / 2 + 250, world.gameWidth, 100,
                AssetLoader.square, Color.WHITE,
                Configuration.TUTORIAL_PROT, AssetLoader.fontXS, FlatColors.WHITE, 0, Align.center);
        textSlow = new Text(world, -world.gameWidth, world.gameHeight / 2 + 150, world.gameWidth, 100,
                AssetLoader.square, Color.WHITE,
                Configuration.TUTORIAL_SLOW, AssetLoader.fontXS, FlatColors.WHITE, 0, Align.center);
        coinLife = new Coin(world, world.gameWidth / 2, world.gameHeight / 2 + 350 + Settings.COIN_SIZE + 15,
                Settings.COIN_SIZE, Settings.COIN_SIZE, AssetLoader.coin,
                Color.WHITE, Shape.RECTANGLE, 1);
        coinProt = new Coin(world, world.gameWidth / 2, world.gameHeight / 2 + 250 + Settings.COIN_SIZE + 15,
                Settings.COIN_SIZE, Settings.COIN_SIZE, AssetLoader.coin2,
                Color.WHITE, Shape.RECTANGLE, 2);
        coinSlow = new Coin(world, world.gameWidth / 2, world.gameHeight / 2 + 150 + Settings.COIN_SIZE + 15,
                Settings.COIN_SIZE, Settings.COIN_SIZE, AssetLoader.coin3,
                Color.WHITE, Shape.RECTANGLE, 3);
        text = new Text(world, -world.gameWidth, world.gameHeight / 2 - 50, world.gameWidth,100,
                AssetLoader.square, Color.WHITE,
                Configuration.TUTORIAL_TEXT, AssetLoader.font, FlatColors.WHITE, 0, Align.center);

        circle = new GameObject(world, world.gameWidth / 2 - 60, (world.gameHeight / 4) - 100, 100, 100, AssetLoader.dot, Color.WHITE, Shape.CIRCLE);
        circle.getSprite().setAlpha(0f);
    }

    @Override
    public void update(float delta) {
        finger.update(delta);
        text.update(delta);
        textLife.update(delta);
        textProt.update(delta);
        textSlow.update(delta);
        coinLife.update(delta);
        coinProt.update(delta);
        coinSlow.update(delta);
        super.update(delta);

        //CIRCLE EFFECT
        circle.update(delta);
        circle.setPosition((world.gameWidth / 2) - 60, (world.gameHeight / 4) - 100);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        //super.render(batch, shapeRenderer);
        finger.render(batch, shapeRenderer);
        text.render(batch, shapeRenderer, GameRenderer.fontShader);
        textLife.render(batch, shapeRenderer, GameRenderer.fontShader);
        textProt.render(batch, shapeRenderer, GameRenderer.fontShader);
        textSlow.render(batch, shapeRenderer, GameRenderer.fontShader);
        coinLife.render(batch, shapeRenderer);
        coinProt.render(batch, shapeRenderer);
        coinSlow.render(batch, shapeRenderer);
        circle.render(batch, shapeRenderer);
    }

    public void start() {

        finger.fadeIn(.4f, 0f);
        fadeInFromTo(0, .5f, .4f, 0f);
        world.setGameState(GameState.TUTORIAL);
        text.effectX(-world.gameWidth, 0, .4f, 0f);
        textLife.effectX(-world.gameWidth, 0, .4f, 0f);
        textProt.effectX(-world.gameWidth, 0, .4f, 0f);
        textSlow.effectX(-world.gameWidth, 0, .4f, 0f);
        coinLife.effectX(-world.gameWidth, world.gameWidth / 2 - 125, .4f, 0f);
        coinProt.effectX(-world.gameWidth, world.gameWidth / 2 - 230, .4f, 0f);
        coinSlow.effectX(-world.gameWidth, world.gameWidth / 2 - 215, .4f, 0f);
        finger.effectY(finger.getPosition().y, finger.getPosition().y + 10, 1f, 0f);
//        finger.effectX(finger.getPosition().x, world.gameWidth - (finger.getSprite().getWidth()), 2f, 1f);
//        finger.effectX(finger.getPosition().x, world.gameWidth / 2 - (finger.getSprite().getWidth() / 2), 1f, 3f);

        world.getHero().heroState = Hero.HeroState.IDLE;
        world.getHero().startT();

//        circle.scale(0,5,.2f,.0f);
//        circle.fadeOut(.4f, .0f);
    }

    public void finish() {
        finger.fadeOut(.4f, 0f);
        fadeOutFrom(0.5f, .4f, 0f);
        timer.setValue(0);
        text.effectX(text.getPosition().x, world.gameWidth, .4f, 0f);
        textLife.effectX(textLife.getPosition().x, world.gameWidth, .4f, 0f);
        textProt.effectX(textProt.getPosition().x, world.gameWidth, .4f, 0f);
        textSlow.effectX(textSlow.getPosition().x, world.gameWidth, .4f, 0f);
        coinLife.effectX(coinLife.getPosition().x, world.gameWidth, .4f, 0f);
        coinProt.effectX(coinProt.getPosition().x, world.gameWidth, .4f, 0f);
        coinSlow.effectX(coinSlow.getPosition().x, world.gameWidth, .4f, 0f);

        Tween.to(timer, -1, .4f).target(1).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        world.setGameState(GameState.RUNNING);
                        //world.resetGame();
                        // world.getHero().start();
                        world.getPlatformManager().start();
                        world.setCoins(100);
                    }
                }).start(getManager());
    }

    public GameObject getFinger() {
        return finger;
    }
}

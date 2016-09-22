package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.riuvic.smoothball2.configuration.Configuration;
import com.riuvic.smoothball2.gameworld.GameState;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.helpers.FlatColors;
import com.riuvic.smoothball2.tweens.SpriteAccessor;
import com.riuvic.smoothball2.tweens.Value;
import com.riuvic.smoothball2.ui.Text;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;


public class GameOver extends GameObject {

    private GameObject bigBanner, xButton, homeButton, replayButton;
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private Text continueText, scoreText;
    private ArrayList<GameObject> arrows = new ArrayList<GameObject>();
    private Coin coin;
    private Value timer = new Value();

    private int numOfCircles = 7;

    public GameOver(GameWorld world, float x, float y, float width, float height,
                    TextureRegion texture,
                    Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);

        //OBJECTS

        setBigBanner();
        xButton = new GameObject(world, bigBanner.getPosition().x,
                bigBanner.getPosition().y + bigBanner.getSprite().getHeight() + 30,
                AssetLoader.xButton.getRegionWidth(), AssetLoader.xButton.getRegionHeight(),
                AssetLoader.xButton, FlatColors.WHITE, Shape.RECTANGLE);
        xButton.isButton = true;
        homeButton = new GameObject(world,
                world.gameWidth / 2 - (AssetLoader.replayButton.getRegionWidth() / 2) - 250,
                bigBanner.getPosition().y + 80,
                AssetLoader.homeButton.getRegionWidth(), AssetLoader.homeButton.getRegionHeight(),
                AssetLoader.homeButton, FlatColors.WHITE, Shape.RECTANGLE);
        homeButton.isButton = true;
        replayButton = new GameObject(world,
                world.gameWidth / 2 - (AssetLoader.homeButton.getRegionWidth() / 2) + 250,
                bigBanner.getPosition().y + 80,
                AssetLoader.replayButton.getRegionWidth(), AssetLoader.replayButton.getRegionHeight(),
                AssetLoader.replayButton, FlatColors.WHITE, Shape.RECTANGLE);
        replayButton.isButton = true;

        scoreText = new Text(world, bigBanner.getPosition().x,
                bigBanner.getPosition().y + bigBanner.getSprite().getHeight() - 200,
                bigBanner.getSprite().getWidth(), 100, AssetLoader.square, FlatColors.WHITE,
                "", AssetLoader.fontScore, Color.WHITE, 15,
                Align.center);

        //objects.add(xButton);
        objects.add(bigBanner);
        objects.add(replayButton);
        objects.add(homeButton);
        //objects.add(coin);

        for (int i = 0; i < numOfCircles; i++) {
            arrows.add(new GameObject(world, world.gameWidth / 2 - ((3 * 50) + 25) + (50 * i),
                    bigBanner.getPosition().y - 30 - 50, 50,
                    50, AssetLoader.dot, FlatColors.WHITE, Shape.RECTANGLE));
            arrows.get(i).getSprite().setAlpha(1);
            arrows.get(i).getSprite().setScale(0.7f);
            Tween.to(arrows.get(i).getSprite(), SpriteAccessor.SCALE, .8f).target(0.1f)
                    .delay(i * 0.1f)
                    .repeatYoyo(10000, 0f)
                    .ease(TweenEquations.easeInOutSine).start(arrows.get(i).getManager());
            objects.add(arrows.get(i));
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(delta);

        }

        continueText.setPosition(continueText.position.x,
                bigBanner.getPosition().y + bigBanner.getSprite().getHeight() - 180);
        scoreText.setPosition(scoreText.position.x,
                bigBanner.getPosition().y + bigBanner.getSprite().getHeight() - 420);

        continueText.update(delta);
        scoreText.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontShader) {
        for (int i = 0; i < objects.size(); i++) {

            objects.get(i).render(batch, shapeRenderer);
        }
        continueText.render(batch, shapeRenderer, fontShader);
        scoreText.render(batch, shapeRenderer, fontShader);
    }

    public void setScore() {
        scoreText.setText(Configuration.SCORE_TEXT + world.getScore() + "");
    }

    public void start() {
        setScore();
        for (int i = 0; i < objects.size() - numOfCircles - 1; i++) {
            objects.get(i).effectY(objects.get(i).getPosition().y - world.gameHeight,
                    objects.get(i).getPosition().y, 0.7f, 0.03f * i);
        }
        // Animacio
        homeButton.effectY(homeButton.getPosition().y - world.gameHeight,
                homeButton.getPosition().y, 0.7f, 0.03f * 3);
        for (int i = objects.size() - numOfCircles; i < objects.size(); i++) {
            objects.get(i).effectY(objects.get(i).getPosition().y - world.gameHeight,
                    objects.get(i).getPosition().y, 0.7f, 0.12f);
        }
        world.changeSprites();
    }

    public void finish() {
        for (int i = 0; i < objects.size() - numOfCircles - 1; i++) {
            objects.get(i).effectY(objects.get(i).getPosition().y,
                    objects.get(i).getPosition().y + world.gameHeight, 0.7f, 0.03f * i);
        }
        // Animacio
        homeButton.effectY(homeButton.getPosition().y, homeButton.getPosition().y + world.gameHeight, 0.7f,
                0.03f * 3);
        for (int i = objects.size() - numOfCircles; i < objects.size(); i++) {
            objects.get(i).effectY(objects.get(i).getPosition().y,
                    objects.get(i).getPosition().y + world.gameHeight, 0.7f, 0.12f);
        }
    }

    public GameObject getxButton() {
        return xButton;
    }

    public GameObject getReplayButton() {
        return replayButton;
    }

    public GameObject getHomeButton() {
        return homeButton;
    }

    public void startTimer() {
        timer.setValue(0);
        Tween.to(timer, -1, 4).target(1).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        world.getMenu().goToHome();
                    }
                }).start(getManager());
    }

    public void startAgain() {
        int score = world.getScore();
        finish();
        world.resetGame();
        world.getHero().start();
        world.getPlatformManager().start();
        world.setScore(0);
        world.setScoreText(0);
        timer.setValue(0);
        world.setCoins(100);
        world.getMenu().getBackground().fadeOut(.8f, .2f);
        Tween.to(timer, -1, .82f).target(1).setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(
                        new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                world.setGameState(GameState.RUNNING);
                            }
                        }).start(getManager());
    }

    public void setBigBanner(){
        if(world.getScore() > world.getLastHighScore()) {
            bigBanner = new GameObject(world,
                    world.gameWidth / 2 - AssetLoader.bigBannerRecord.getRegionWidth() / 2,
                    world.gameHeight / 2 - AssetLoader.bigBannerRecord.getRegionHeight() / 2 + 0,
                    AssetLoader.bigBannerRecord.getRegionWidth(), AssetLoader.bigBannerRecord.getRegionHeight(),
                    AssetLoader.bigBannerRecord,
                    FlatColors.WHITE, Shape.RECTANGLE);
            continueText = new Text(world, bigBanner.getPosition().x,
                    bigBanner.getPosition().y + bigBanner.getSprite().getHeight() - 100,
                    bigBanner.getSprite().getWidth(), 100, AssetLoader.square, FlatColors.WHITE,
                    Configuration.RECORD_TEXT, AssetLoader.font, Color.WHITE, 30,
                    Align.center);
        } else {
            bigBanner = new GameObject(world,
                    world.gameWidth / 2 - AssetLoader.bigBannerGameOver.getRegionWidth() / 2,
                    world.gameHeight / 2 - AssetLoader.bigBannerGameOver.getRegionHeight() / 2 + 0,
                    AssetLoader.bigBannerGameOver.getRegionWidth(), AssetLoader.bigBannerGameOver.getRegionHeight(),
                    AssetLoader.bigBannerGameOver,
                    FlatColors.WHITE, Shape.RECTANGLE);
            continueText = new Text(world, bigBanner.getPosition().x,
                    bigBanner.getPosition().y + bigBanner.getSprite().getHeight() - 100,
                    bigBanner.getSprite().getWidth(), 100, AssetLoader.square, FlatColors.WHITE,
                    Configuration.GAME_OVER_TEXT, AssetLoader.font, Color.WHITE, 30,
                    Align.center);
        }
    }
}

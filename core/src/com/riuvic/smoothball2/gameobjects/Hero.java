package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.immersion.content.Log;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.tweens.VectorAccessor;
import com.riuvic.smoothball2.configuration.Settings;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;


public class Hero extends GameObject {

    private static final String TAG = "Hero";
    private Vector2 positionPoint;
    float lerp = Settings.HERO_LERP_SPEED;
    private Vector2 position;
    private ParticleEffect effect, divideEffect;
    public boolean notCollided = true;

    public enum HeroState {
        DEAD, IDLE
    }

    public enum HeroGameState {
        NORMAL, PROTECTED, TIMELESS
    }

    public HeroState heroState;
    public HeroGameState heroGameState;
    private GameObject circle, circleLife, circleProtection, circleTime;

    public Hero(GameWorld world, float x, float y, float width, float height, TextureRegion texture, Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        positionPoint = new Vector2(x, y);
        position = new Vector2(x, y);
        position.set(getPosition());
        getRectangle().set(x, y, width - 10, height - 10);
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("misc/jetpack.p"), Gdx.files.internal(""));
        effect.setPosition(-100, -100);
        notCollided = true;
        heroState = HeroState.IDLE;
        heroGameState = HeroGameState.NORMAL;

        // CIRCLES
        circle = new GameObject(world, x, y, 100, 100, AssetLoader.dot, Color.WHITE, Shape.CIRCLE);
        circle.getSprite().setAlpha(0f);
        circleLife = new GameObject(world, x, y, 100, 100, AssetLoader.dot2, Color.WHITE, Shape.CIRCLE);
        circleLife.getSprite().setAlpha(0f);
        circleProtection = new GameObject(world, x, y, 100, 100, AssetLoader.dot3, Color.WHITE, Shape.CIRCLE);
        circleProtection.getSprite().setAlpha(0f);
        circleTime = new GameObject(world, x, y, 100, 100, AssetLoader.dot4, Color.WHITE, Shape.CIRCLE);
        circleTime.getSprite().setAlpha(0f);



    }

    public void start() {
        effectY(-100, Settings.HERO_INITIAL_Y, 1, 0.5f);
        notCollided = true;
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("misc/jetpack.p"), Gdx.files.internal(""));
        effect.setPosition(-100, -100);
    }

    public void startT() {
        notCollided = true;

    }

    @Override
    public void update(float delta) {

        getManager().update(delta);
        if (!isDead()) {

            position.x = positionPoint.x;
            getRectangle().setPosition(position.x - (getSprite().getWidth() / 2) + 5, position.y);
            getSprite().setPosition(position.x - (getSprite().getWidth() / 2), position.y);
            getSprite().setOriginCenter();
            updateEffects();
            effect.update(delta);
            effect.setPosition(position.x, position.y - 5);

        }
        if(world.isTutorial()){
           setPositionPointX(world.tutorial.getFinger().getPosition().x + world.tutorial.getFinger().getSprite().getWidth()/2);
        }

        //CIRCLE EFFECT
        circle.update(delta);
        circle.setPosition(position.x - (circle.getSprite().getWidth() / 2) - (getSprite().getRotation() / 2), position.y - 15);
        circleLife.update(delta);
        circleLife.setPosition(position.x - (circleLife.getSprite().getWidth() / 2) - (getSprite().getRotation() / 2), position.y - 15);
        circleProtection.update(delta);
        circleProtection.setPosition(position.x - (circleProtection.getSprite().getWidth() / 2) - (getSprite().getRotation() / 2), position.y - 15);
        circleTime.update(delta);
        circleTime.setPosition(position.x - (circleTime.getSprite().getWidth() / 2) - (getSprite().getRotation() / 2), position.y - 15);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        effect.draw(batch);
        circle.render(batch, shapeRenderer);
        circleLife.render(batch, shapeRenderer);
        circleProtection.render(batch, shapeRenderer);
        circleTime.render(batch, shapeRenderer);
        super.render(batch, shapeRenderer);
    }

    public void setPositionPointX(float p) {
        this.positionPoint.x = p;
    }

    public float getPositionPointX() {
        return positionPoint.x;
    }

    public float getPositionPointY() {
        return positionPoint.y;
    }

    @Override
    public void effectY(float from, float to, float duration, float delay) {
        position.y = from;
        Tween.to(position, VectorAccessor.VERTICAL, duration).target(to).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(getManager());
    }

    public void coinLifeEffect() {
        circleLife.scale(0, 2f, .3f, 0f);
        circleLife.fadeOut(.3f, 0f);
    }

    public void coinProtectionEffect() {
        circleProtection.scale(0, 2f, .3f, 0f);
        circleProtection.fadeOut(.3f, 0f);
    }

    public void coinTimeEffect() {
        circleTime.scale(0, 2f, .3f, 0f);
        circleTime.fadeOut(.3f, 0f);
    }

    public void finish() {
        if(AssetLoader.getVolume()) AssetLoader.end.play();
        heroState = HeroState.DEAD;
        circle.scale(0,50,.5f,.0f);
        circle.fadeOut(.4f, .0f);
    }

    public boolean isDead() {
        return heroState == HeroState.DEAD;
    }

    public void setNormalState() {
        heroGameState = HeroGameState.NORMAL;
    }

    public boolean isNormalState() {
        if(heroGameState == HeroGameState.NORMAL) return true;
        else return false;
    }

    public void setProtectedState() {
        heroGameState = HeroGameState.PROTECTED;

    }

    public boolean isProtectedState() {
        if(heroGameState == HeroGameState.PROTECTED) return true;
        else return false;
    }

    public void setTimelessState() {
        heroGameState = HeroGameState.TIMELESS;
    }

    public boolean isTimelessState() {
        if(heroGameState == HeroGameState.TIMELESS) return true;
        else return false;
    }
}

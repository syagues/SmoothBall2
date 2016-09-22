package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.immersion.content.Log;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.tweens.SpriteAccessor;
import com.riuvic.smoothball2.tweens.Value;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;


public class Coin extends GameObject {

    private static final String TAG = "Coin Class";

    private Value angle = new Value();
    private float angleInc;
    public boolean isScored = false;
    private int coinType = 0;

    public Coin(GameWorld world, float x, float y, float width, float height, TextureRegion texture, Color color, Shape shape, int type) {
        super(world, x, y, width, height, texture, color, shape);
        Tween.to(getSprite(), SpriteAccessor.SCALE, .3f).target(0.8f).repeatYoyo(100000, 0f)
                .ease(TweenEquations.easeInOutSine).start(getManager());
        getSprite().setRotation(45);
        angleInc = Math.random() < 0.5f ? -MathUtils.random(2, 4) : MathUtils.random(2, 4);
        coinType = type;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        angle.setValue(angle.getValue() + angleInc);
        getSprite().setRotation(angle.getValue());
        collisions();

    }

    private void collisions() {
        if (!isScored && ((Intersector.overlaps(getRectangle(), world.getHero().getRectangle()) && world.isJoined()) ||
                Intersector.overlaps(getRectangle(), world.getHeroL().getRectangle()) ||
                Intersector.overlaps(getRectangle(), world.getHeroR().getRectangle()))) {

            if(isCoinLife()) {
                isScored = true;
                world.addCoins(5);
                playSound();
                world.getHero().coinLifeEffect();
                world.getHeroL().coinLifeEffect();
                world.getHeroR().coinLifeEffect();
                //Log.i(TAG, "Moneda de VIDA agafada");
            }

            if(isCoinProtection()) {
                isScored = true;
                playSound();
                world.getHero().coinProtectionEffect();
                world.getHeroL().coinProtectionEffect();
                world.getHeroR().coinProtectionEffect();
                world.getHero().setProtectedState();
                world.getHeroL().setProtectedState();
                world.getHeroR().setProtectedState();
                world.changeHeroSprites(AssetLoader.rocket2);
                //Log.i(TAG, "Moneda de PROTECCIO agafada");
            }

            if(isCoinTime()) {
                isScored = true;
                playSound();
                world.getHero().coinTimeEffect();
                world.getHeroL().coinTimeEffect();
                world.getHeroR().coinTimeEffect();
                world.getHero().setTimelessState();
                world.getHeroL().setTimelessState();
                world.getHeroR().setTimelessState();
                world.changeHeroSprites(AssetLoader.rocket3);
                //Log.i(TAG, "Moneda de TEMPS agafada");
            }
        }
    }

    public int getCoinType() {
        return coinType;
    }

    public boolean isCoinLife() {
        if (coinType == 1) return true;
        else return false;
    }

    public boolean isCoinProtection() {
        if (coinType == 2) return true;
        else return false;
    }

    public boolean isCoinTime() {
        if (coinType == 3) return true;
        else return false;
    }

    public void playSound() {
        if(AssetLoader.getVolume()) AssetLoader.coinS.play();
        fadeOut(.3f, 0f);
        scaleZero(.3f, 0f);
    }
}

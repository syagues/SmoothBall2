package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.tweens.VectorAccessor;
import com.riuvic.smoothball2.configuration.Settings;
import com.riuvic.smoothball2.helpers.AssetLoader;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;


public class Platform extends GameObject {

    private static final String TAG = "Platform";

    private GameObject platform1, platform2, platform3;
    private float centerPoint, centerPointLeft, centerPointRight, platform1Width, platform2Width, platform3Width;
    private float gap;
    private Coin coinLife, coinProtection, coinTime;
    private boolean isCoinLife, isCoinProtection, isCoinTime;
    private boolean isScored = false;
    private TextureRegion textR;
    private float extradistance;

    public Platform(GameWorld world, float x, float y, float width, float height,
                    TextureRegion texture,
                    Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        if (Math.random() < 0.1f) isCoinLife = true;
        else isCoinLife = false;
        reset();

    }

    public void reset() {
        /*textR = Math.random() < 0.3f ? (AssetLoader.platform1) : (Math
                .random() < 0.5f ? AssetLoader.platform2 : AssetLoader.platform3);*/
        platform1 = platform2 = platform3 = null;
        textR = AssetLoader.platform1;
        isScored = false;
        this.gap = getGap();
        // Center points
        centerPoint = MathUtils.random(gap / 2 + 50, world.gameWidth - (gap / 2) - 50);
        centerPointLeft = centerPoint / 2;
        centerPointRight = world.gameWidth - centerPointLeft;
        // Platforms widths
        platform1Width = centerPointLeft - (gap / 2);
        platform2Width = centerPointRight - centerPointLeft - gap;
        platform3Width = world.gameWidth - (centerPointRight + (gap / 2));
        // Platforms
        platform1 = new GameObject(world, centerPointLeft - (gap / 2) - world.gameWidth, getPosition().y, world.gameWidth, 50, textR, Color.WHITE, Shape.RECTANGLE);
        platform2 = new GameObject(world, centerPointLeft + (gap / 2), getPosition().y, centerPointRight - gap - centerPointLeft, 50, textR, Color.WHITE, Shape.RECTANGLE);
        platform3 = new GameObject(world, centerPointRight + (gap / 2), getPosition().y, world.gameWidth, 50, textR, Color.WHITE, Shape.RECTANGLE);

        getSprite().setSize(world.gameWidth * 2 + gap, 70);
        setPosition(centerPoint - (gap / 2) - world.gameWidth, getPosition().y);

        // COINS RATE
        if (Math.random() < Settings.COIN_LIFE) isCoinLife = true;
        else isCoinLife = false;
        if (Math.random() < Settings.COIN_PROTECTION) isCoinProtection = true;
        else isCoinProtection = false;
        if (Math.random() < Settings.COIN_TIME) isCoinTime = true;
        else isCoinTime = false;

        coinLife = new Coin(world, MathUtils.random(150, world.gameWidth - 150),
                position.y + (70 / 2) - (Settings.COIN_SIZE / 2), Settings.COIN_SIZE,
                Settings.COIN_SIZE, AssetLoader.coin,
                Color.WHITE, Shape.RECTANGLE, 1);
        coinProtection = new Coin(world, MathUtils.random(150, world.gameWidth - 150),
                position.y + (70 / 2) - (Settings.COIN_SIZE / 2), Settings.COIN_SIZE,
                Settings.COIN_SIZE, AssetLoader.coin2,
                Color.WHITE, Shape.RECTANGLE, 2);
        coinTime = new Coin(world, MathUtils.random(150, world.gameWidth - 150),
                position.y + (70 / 2) - (Settings.COIN_SIZE / 2), Settings.COIN_SIZE,
                Settings.COIN_SIZE, AssetLoader.coin3,
                Color.WHITE, Shape.RECTANGLE, 3);

        Tween.to(position, VectorAccessor.HORIZONTAL, 1).target(position.x + 100).delay(0)
                .repeatYoyo(10000, 0)
                .ease(TweenEquations.easeInOutSine).start(getManager());

        extradistance = MathUtils.random(120, (Settings.MIN_DISTANCE_BETWEEN_PLATFORMS / 1.5f));
    }

    @Override
    public void update(float delta) {
        getManager().update(delta);
        // LEVELS
        setLevel(delta);

        platform1.setYPosition(getPosition().y);
        platform1.setVelocity(getVelocity());
        platform1.update(delta);

        platform2.setYPosition(getPosition().y);
        platform2.setVelocity(getVelocity());
        platform2.update(delta);

        platform3.setYPosition(getPosition().y);
        platform3.setVelocity(getVelocity());
        platform3.update(delta);

        if (isCoinLife) {
            coinLife.update(delta);
            coinLife.setYPosition(platform1.position.y + (getSprite()
                    .getHeight() / 2) - (Settings.COIN_SIZE / 2) - 6 + extradistance);
            coinLife.setVelocity(getVelocity());
        }

        if (isCoinProtection) {
            coinProtection.update(delta);
            coinProtection.setYPosition(platform1.position.y + (getSprite()
                    .getHeight() / 2) - (Settings.COIN_SIZE / 2) - 6 + extradistance);
            coinProtection.setVelocity(getVelocity());
        }

        if (isCoinTime) {
            coinTime.update(delta);
            coinTime.setYPosition(platform1.position.y + (getSprite()
                    .getHeight() / 2) - (Settings.COIN_SIZE / 2) - 6 + extradistance);
            coinTime.setVelocity(getVelocity());
        }

        checkScored();
    }

    private void checkScored() {
        if (!isScored && position.y < world.getHero().getPosition().y) {
            world.addScore(1);
            if(AssetLoader.getVolume()) AssetLoader.success.play();
            isScored = true;
        }
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if(isPlatform1Visible())
            platform1.render(batch, shapeRenderer);
        if(isPlatform2Visible())
            platform2.render(batch, shapeRenderer);
        if(isPlatform3Visible())
            platform3.render(batch, shapeRenderer);

        if (isCoinLife) coinLife.render(batch, shapeRenderer);
        if (isCoinProtection) coinProtection.render(batch, shapeRenderer);
        if (isCoinTime) coinTime.render(batch, shapeRenderer);

    }

    public boolean collision() {
        if(isPlatform1Visible() && isPlatform2Visible() && isPlatform3Visible()){
            if (((Intersector.overlaps(world.getHeroL().getRectangle(), platform3.getRectangle()) || Intersector.overlaps(world.getHeroR().getRectangle(), platform3.getRectangle()) || Intersector.overlaps(world.getHeroL().getRectangle(), platform2.getRectangle()) || Intersector.overlaps(world.getHeroR().getRectangle(), platform2.getRectangle()) || Intersector.overlaps(world.getHeroL().getRectangle(), platform1.getRectangle()) || Intersector.overlaps(world.getHeroR().getRectangle(), platform1.getRectangle()))
                    && !world.isJoined()) ||
                    ((Intersector.overlaps(world.getHero().getRectangle(), platform3.getRectangle()) || Intersector.overlaps(world.getHero().getRectangle(), platform2.getRectangle()) || Intersector.overlaps(world.getHero().getRectangle(), platform1.getRectangle()))
                            && world.isJoined())) {

                return checkProtectedState();
            }
        } else if(isPlatform1Visible() && isPlatform3Visible() && !isPlatform2Visible()) {
            if (((Intersector.overlaps(world.getHeroL().getRectangle(), platform3.getRectangle()) || Intersector.overlaps(world.getHeroR().getRectangle(), platform3.getRectangle()) || Intersector.overlaps(world.getHeroL().getRectangle(), platform1.getRectangle()) || Intersector.overlaps(world.getHeroR().getRectangle(), platform1.getRectangle()))
                    && !world.isJoined()) ||
                    ((Intersector.overlaps(world.getHero().getRectangle(), platform3.getRectangle()) || Intersector.overlaps(world.getHero().getRectangle(), platform1.getRectangle()))
                            && world.isJoined())) {

                return checkProtectedState();
            }
        } else if(isPlatform2Visible() && !isPlatform1Visible() && !isPlatform3Visible()) {
            if (((Intersector.overlaps(world.getHeroL().getRectangle(), platform2.getRectangle()) || Intersector.overlaps(world.getHeroR().getRectangle(), platform2.getRectangle()))
                    && !world.isJoined()) ||
                    ((Intersector.overlaps(world.getHero().getRectangle(), platform2.getRectangle()))
                            && world.isJoined())) {

                return checkProtectedState();
            }
        }
        return false;
    }

    public void changeSprite(TextureRegion region) {
        if(isPlatform1Visible())
            platform1.getSprite().setRegion(region);
        if(isPlatform2Visible())
            platform2.getSprite().setRegion(region);
        if(isPlatform3Visible())
            platform3.getSprite().setRegion(region);
    }

    public boolean isPlatform1Visible(){
        if(platform1Width > Settings.MIN_PLAT_WIDTH)
            return true;
        else
            return false;
    }

    public boolean isPlatform2Visible(){
        if(platform2Width > Settings.MIN_PLAT_WIDTH)
            return true;
        else
            return false;
    }

    public boolean isPlatform3Visible(){
        if(platform3Width > Settings.MIN_PLAT_WIDTH)
            return true;
        else
            return false;
    }

    public void setLevel(float delta){

        if(world.getHero().isTimelessState() && world.getHeroL().isTimelessState() && world.getHeroR().isTimelessState()) {
            setSpeed(delta, getSpeed()/2);
        } else {
            setSpeed(delta, getSpeed());
        }
    }

    public void setSpeed(float delta, float speed){
        if (velocity.y > -speed) {
            velocity.add(getAcceleration().cpy().scl(delta));
        } else {
            velocity.y = -speed;
        }
        position.add(velocity.cpy().scl(delta));
    }

    public float getSpeed() {
        if(world.getScore() >= 0 && world.getScore() < 25){
            return Settings.PLAT_INITAL_VELOCITY;
        } else if(world.getScore() >= 25 && world.getScore() < 50) {
            return Settings.PLAT_LEVEL2_VELOCITY;
        } else if(world.getScore() >= 50 && world.getScore() < 75){
            return Settings.PLAT_LEVEL3_VELOCITY;
        } else if(world.getScore() >= 75 && world.getScore() < 100){
            return Settings.PLAT_LEVEL4_VELOCITY;
        } else if(world.getScore() >= 100 && world.getScore() < 125){
            return Settings.PLAT_LEVEL5_VELOCITY;
        } else if(world.getScore() >= 125 && world.getScore() < 150){
            return Settings.PLAT_LEVEL6_VELOCITY;
        } else if(world.getScore() >= 150 && world.getScore() < 175){
            return Settings.PLAT_LEVEL7_VELOCITY;
        } else if(world.getScore() >= 175 && world.getScore() < 200){
            return Settings.PLAT_LEVEL8_VELOCITY;
        } else if(world.getScore() >= 200 && world.getScore() < 225){
            return Settings.PLAT_LEVEL9_VELOCITY;
        } else if(world.getScore() >= 225 && world.getScore() < 250){
            return Settings.PLAT_LEVEL10_VELOCITY;
        } else if(world.getScore() >= 250 && world.getScore() < 275){
            return Settings.PLAT_LEVEL11_VELOCITY;
        } else if(world.getScore() >= 275 && world.getScore() < 300){
            return Settings.PLAT_LEVEL12_VELOCITY;
        } else if(world.getScore() >= 300 && world.getScore() < 325){
            return Settings.PLAT_LEVEL13_VELOCITY;
        } else if(world.getScore() >= 325 && world.getScore() < 350){
            return Settings.PLAT_LEVEL14_VELOCITY;
        } else if(world.getScore() >= 350 && world.getScore() < 375){
            return Settings.PLAT_LEVEL15_VELOCITY;
        } else if(world.getScore() >= 375 && world.getScore() < 400){
            return Settings.PLAT_LEVEL16_VELOCITY;
        } else if(world.getScore() >= 400 && world.getScore() < 425){
            return Settings.PLAT_LEVEL17_VELOCITY;
        } else if(world.getScore() >= 425 && world.getScore() < 450){
            return Settings.PLAT_LEVEL18_VELOCITY;
        } else if(world.getScore() >= 450 && world.getScore() < 475){
            return Settings.PLAT_LEVEL19_VELOCITY;
        } else if(world.getScore() >= 475 && world.getScore() < 500){
            return Settings.PLAT_LEVEL20_VELOCITY;
        } else {
            return Settings.PLAT_FINAL_VELOCITY;
        }
    }

    public float getGap(){
        float diff = 0;
        if(world.getScore()>=25 && world.getScore()<50) {
            diff = 2.5f;
        } else if(world.getScore()>=50 && world.getScore()<75) {
            diff = 5f;
        } else if(world.getScore()>=75 && world.getScore()<100) {
            diff = 7.5f;
        } else if(world.getScore()>=100 && world.getScore()<125) {
            diff = 10f;
        } else if(world.getScore()>=125 && world.getScore()<150) {
            diff = 12.5f;
        } else if(world.getScore()>=150 && world.getScore()<175) {
            diff = 15f;
        } else if(world.getScore()>=175 && world.getScore()<200) {
            diff = 17.5f;
        } else if(world.getScore()>=200 && world.getScore()<225) {
            diff = 20f;
        } else if(world.getScore()>=225 && world.getScore()<250) {
            diff = 22.5f;
        } else if(world.getScore()>=250 && world.getScore()<275) {
            diff = 25f;
        } else if(world.getScore()>=275 && world.getScore()<300) {
            diff = 27.5f;
        } else if(world.getScore()>=300 && world.getScore()<325) {
            diff = 30f;
        } else if(world.getScore()>=325 && world.getScore()<350) {
            diff = 32.5f;
        } else if(world.getScore()>=350 && world.getScore()<375) {
            diff = 35f;
        } else if(world.getScore()>=375 && world.getScore()<400) {
            diff = 37.5f;
        } else if(world.getScore()>=400 && world.getScore()<425) {
            diff = 40f;
        } else if(world.getScore()>=425 && world.getScore()<450) {
            diff = 42.5f;
        } else if(world.getScore()>=450 && world.getScore()<475) {
            diff = 45f;
        } else if(world.getScore()>=475 && world.getScore()<500) {
            diff = 47.5f;
        } else if(world.getScore()>=500) {
            diff = 50f;
        }

        return MathUtils.random(Settings.MIN_GAP_SIZE, Settings.MAX_GAP_SIZE) - diff;
    }

    private boolean checkProtectedState() {
        if(world.getHero().isProtectedState() || world.getHeroL().isProtectedState() || world.getHeroR().isProtectedState()) {
            return false;
        } else {
            return true;
        }
    }
}

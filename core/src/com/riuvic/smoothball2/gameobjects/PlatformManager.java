package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.configuration.Settings;

import java.util.ArrayList;


public class PlatformManager {
    private GameWorld world;
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private int numOfPlatforms = 6;

    public PlatformManager(GameWorld world) {
        this.world = world;
        for (int i = 0; i < numOfPlatforms; i++) {
            platforms.add(new Platform(world, 0,
                    world.gameHeight + (MathUtils.random(Settings.MIN_DISTANCE_BETWEEN_PLATFORMS,
                            Settings.MAX_DISTANCE_BETWEEN_PLATFORMS) * i), world.gameWidth,
                    70, AssetLoader.square,
                    Color.WHITE, GameObject.Shape.RECTANGLE));
        }
    }

    public void update(float delta) {
        for (int i = 0; i < platforms.size(); i++) {
            platforms.get(i).update(delta);
        }

        checkIfOutside();
        checkCollisions();
    }

    private void checkCollisions() {
        for (int i = 0; i < platforms.size(); i++) {
            if (world.getHeroL().notCollided && world.getHeroR().notCollided) {
                if (platforms.get(i).collision()) {
                    finish();
                    world.getHero().notCollided = false;
                    world.getHeroL().notCollided = false;
                    world.getHeroR().notCollided = false;
                }
            }
        }
    }

    public void finish() {
        world.finishGame();
        world.getHero().finish();
        world.getHeroL().finish();
        world.getHeroR().finish();
        for (int i = 0; i < platforms.size(); i++) {
            platforms.get(i).setAcceleration(0, 0);
            platforms.get(i).setVelocity(0, 0);
        }

    }

    private void checkIfOutside() {
        for (int i = 0; i < platforms.size(); i++) {
            if (platforms.get(i).getPosition().y < 0 - platforms.get(i).getSprite()
                    .getHeight() - (Settings.MIN_DISTANCE_BETWEEN_PLATFORMS / 1.5f) && platforms
                    .get(i).getVelocity().y < 0) {
                putOnTop(i);
            } else if (platforms.get(i).getPosition().y > world.gameHeight && platforms.get(i)
                    .getVelocity().y > 0) {
                putOnBottom(i);
            }
        }
    }

    private void putOnTop(int i) {
        platforms.get(i).reset();
        if (i == 0) {
            platforms.get(i).setPosition(platforms.get(platforms.size() - 1).getPosition().x,
                    platforms.get(platforms.size() - 1)
                            .getPosition().y + MathUtils
                            .random(Settings.MIN_DISTANCE_BETWEEN_PLATFORMS,
                                    Settings.MAX_DISTANCE_BETWEEN_PLATFORMS));
        } else {
            platforms.get(i).setPosition(platforms.get(i - 1).getPosition().x,
                    platforms.get(i - 1)
                            .getPosition().y + MathUtils
                            .random(Settings.MIN_DISTANCE_BETWEEN_PLATFORMS,
                                    Settings.MAX_DISTANCE_BETWEEN_PLATFORMS));
        }
    }

    private void putOnBottom(int i) {
        platforms.get(i).reset();
        if (i == 0) {
            platforms.get(i).setPosition(platforms.get(platforms.size() - 1).getPosition().x,
                    platforms.get(platforms.size() - 1)
                            .getPosition().y - MathUtils
                            .random(Settings.MIN_DISTANCE_BETWEEN_PLATFORMS,
                                    Settings.MAX_DISTANCE_BETWEEN_PLATFORMS));
        } else {
            platforms.get(i).setPosition(platforms.get(i - 1).getPosition().x,
                    platforms.get(i - 1)
                            .getPosition().y - MathUtils
                            .random(Settings.MIN_DISTANCE_BETWEEN_PLATFORMS,
                                    Settings.MAX_DISTANCE_BETWEEN_PLATFORMS));
        }
    }

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer) {
        for (int i = 0; i < platforms.size(); i++) {
            platforms.get(i).render(batcher, shapeRenderer);
        }
    }

    public void start() {
        for (int i = 0; i < platforms.size(); i++) {
            platforms.get(i).setAcceleration(0, -120);
        }
    }

    public void rewind() {
        for (int i = 0; i < platforms.size(); i++) {
            platforms.get(i).setVelocity(new Vector2());
            platforms.get(i).effectY(platforms.get(i).position.y,
                    platforms.get(i).position.y + world.gameHeight - 70, 1f, 0f);
        }
    }

    public void changeSprites(TextureRegion region) {
        for (int i = 0; i < platforms.size(); i++) {
            platforms.get(i).changeSprite(region);
        }
    }
}

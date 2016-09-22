package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.helpers.FlatColors;
import com.riuvic.smoothball2.tweens.SpriteAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;


public class Star {
    private Vector2 position, velocity;
    private Sprite sprite;

    private GameWorld world;
    private int type;
    private float angle, angleInc;
    private TweenManager manager;

    public Star(GameWorld world) {
        this.world = world;
        sprite = new Sprite(Math.random() < 0.5f ? AssetLoader.particle1 : AssetLoader.particle2);
        sprite.setRotation((float) (Math.random() * 360));
        float size = (float) Math.random() * 40 + 10;
        sprite.setSize(size, size);
        sprite.setPosition((float) Math.random() * world.worldWidth,
                (float) Math.random() * (world.worldHeight));
        position = new Vector2(sprite.getX(), sprite.getY());
        velocity = new Vector2(MathUtils.random(50, 100), MathUtils.random(50, 100));
        if (Math.random() < 0.5f) {
            velocity.x = velocity.x * -1;
        }
        if (Math.random() < 0.5f) {
            velocity.y = velocity.y * -1;
        }
        type = Math.random() < 0.5f ? 1 : 0;
       /*sprite.setColor(Math.random() < 0.5f ? Math
                .random() < 0.5f ? FlatColors.RED : FlatColors.GREEN : Math
                .random() < 0.5f ? FlatColors.BLUE : FlatColors.ORANGE);*/
        sprite.setColor(FlatColors.WHITE);
        sprite.setAlpha((float) (Math.random() * 0.3f + 0.1f));
        manager = new TweenManager();
        angleInc = MathUtils.random(-100, 100);

    }

    public void update(float delta) {
        angle += angleInc * delta;

        manager.update(delta);
        position.add(velocity.cpy().scl(delta));
        sprite.setPosition(position.x, position.y);
        if (sprite.getX() < -100 || sprite.getX() > world.worldWidth + 100) {
            velocity.x = velocity.x * -1;
        }
        if (sprite.getY() < -100 || sprite.getY() > world.worldHeight + 100) {
            velocity.y = velocity.y * -1;
        }
        sprite.setRotation(angle);

        sprite.setOriginCenter();
        // }
    }

    private void resetPosition() {
       /* position.set((float) Math.random() * world.worldWidth,
                world.gameHeight + 30);*/
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        /*if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {*/
        //sprite.setColor(world.parseColor("#FFFFFF", 0.5f));
        sprite.draw(batch);
        // }
    }

    public void scale(float from, float duration, float delay) {
        sprite.setScale(from);
        Tween.to(sprite, SpriteAccessor.SCALE, duration).target(1).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }
}

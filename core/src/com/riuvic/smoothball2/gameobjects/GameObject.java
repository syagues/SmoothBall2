package com.riuvic.smoothball2.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.helpers.FlatColors;
import com.riuvic.smoothball2.tweens.SpriteAccessor;
import com.riuvic.smoothball2.tweens.VectorAccessor;
import com.riuvic.smoothball2.configuration.Configuration;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class GameObject {
    public GameWorld world;
    private float x, y;
    private float width, height;
    private Rectangle rectangle;
    private Circle circle;
    private TextureRegion texture;
    public Vector2 position, velocity, acceleration;
    public Sprite sprite;
    private Sprite flashSprite;
    private Sprite shadowSprite;
    private Color color;
    public boolean isPressed = false;
    private TweenManager manager;
    public Vector2 shadowD = new Vector2(5, -5);
    public boolean isButton = false;

    public enum Shape {RECTANGLE, CIRCLE}

    public Shape shape;
    public boolean shadow = false;

    public GameObject(final GameWorld world, float x, float y, float width, float height,
                      TextureRegion texture, Color color, Shape shape) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.shape = shape;

        if (shape == Shape.CIRCLE) {
            this.circle = new Circle(x, y, width / 2);
        } else if (shape == Shape.RECTANGLE) {
            this.rectangle = new Rectangle(x, y, width, height);
        }

        position = new Vector2(x, y);
        velocity = new Vector2();
        acceleration = new Vector2();

        sprite = new Sprite(texture);
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);
        this.color = color;
        sprite.setColor(color);

        flashSprite = new Sprite(texture);
        flashSprite.setPosition(position.x, position.y);
        flashSprite.setSize(width, height);
        flashSprite.setAlpha(0);

        //TWEEN STUFF
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Vector2.class, new VectorAccessor());
        manager = new TweenManager();

    }

    public void update(float delta) {
        manager.update(delta);
        velocity.add(acceleration.cpy().scl(delta));
        position.add(velocity.cpy().scl(delta));

        if (shape == Shape.RECTANGLE) rectangle.setPosition(position);
        else if (shape == Shape.CIRCLE)
            circle.setPosition(position.x + circle.radius, position.y + circle.radius);

        sprite.setPosition(position.x, position.y);
        sprite.setOriginCenter();

        updateEffects();
    }

    public void updateEffects() {
        if (flashSprite.getColor().a != 0) {
            flashSprite.setRotation(getSprite().getRotation());
            flashSprite.setPosition(getPosition().x, getPosition().y);
            flashSprite.setOriginCenter();
        }

        if (shadow) {
            shadowSprite.setScale(sprite.getScaleX(), sprite.getScaleY());
            shadowSprite.setSize(sprite.getWidth(), sprite.getHeight());
            shadowSprite.setPosition(sprite.getX() + shadowD.x,
                    sprite.getY() + shadowD.y);
            shadowSprite.setOriginCenter();
            shadowSprite.setRotation(sprite.getRotation());
        }

    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (isInside() && sprite.getColor().a != 0) {
            if (shadow) {
                shadowSprite.draw(batch);
            }

            if (isButton)
                if (isPressed) {
                    getSprite().setAlpha(0.7f);
                } else {
                    getSprite().setAlpha(1f);
                }

            sprite.draw(batch);
            if (flashSprite.getColor().a != 0) {
                flashSprite.draw(batch);
            }

            if (Configuration.DEBUG) {
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(FlatColors.DARK_GREEN);

                if (shape == Shape.RECTANGLE)
                    shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                else if (shape == Shape.CIRCLE)
                    shapeRenderer.circle(circle.x, circle.y, circle.radius);

                shapeRenderer.end();
                batch.begin();
            }
        }
    }


    //CLICK
    public boolean isTouchDown(int screenX, int screenY) {
        if (rectangle.contains(screenX, screenY)) {
            isPressed = true;
            return true;
        }
        return false;
    }

    public boolean isTouchUp(int screenX, int screenY) {
        if (rectangle.contains(screenX, screenY) && isPressed) {
            isPressed = false;
            if(AssetLoader.getVolume()) AssetLoader.click.play();
            return true;
        }
        isPressed = false;
        return false;
    }


    //EFFECTS
    public void fadeIn(float duration, float delay) {
        sprite.setAlpha(0);
        Tween.to(getSprite(), SpriteAccessor.ALPHA, duration).target(1).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void fadeOutFrom(float from, float duration, float delay) {
        sprite.setAlpha(from);
        Tween.to(getSprite(), SpriteAccessor.ALPHA, duration).target(0).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void fadeInFromTo(float from, float to, float duration, float delay) {
        sprite.setAlpha(from);
        Tween.to(getSprite(), SpriteAccessor.ALPHA, duration).target(to).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }


    public void fadeOut(float duration, float delay) {
        sprite.setAlpha(1);
        Tween.to(getSprite(), SpriteAccessor.ALPHA, duration).target(0).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void scale(float from, float duration, float delay) {
        sprite.setScale(from);
        Tween.to(getSprite(), SpriteAccessor.SCALE, duration).target(1).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void scale(float from, float to, float duration, float delay) {
        sprite.setScale(from);
        Tween.to(getSprite(), SpriteAccessor.SCALE, duration).target(to).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void scaleZero(float duration, float delay) {
        Tween.to(getSprite(), SpriteAccessor.SCALE, duration).target(0).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void flash(float duration, float delay) {
        flashSprite.setAlpha(1);
        Tween.to(flashSprite, SpriteAccessor.ALPHA, duration).target(0).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void effectY(float from, float to, float duration, float delay) {
        position.y = from;
        Tween.to(position, VectorAccessor.VERTICAL, duration).target(to).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void effectX(float from, float to, float duration, float delay) {
        position.x = from;
        Tween.to(position, VectorAccessor.HORIZONTAL, duration).target(to).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }


    public void setShadow(boolean t) {
        shadow = t;
        shadowSprite = new Sprite(sprite.getTexture());
        shadowSprite.setSize(sprite.getWidth(), sprite.getHeight());
        shadowSprite.setPosition(sprite.getX(), sprite.getY());
        shadowSprite.setColor(Color.BLACK);
        shadowSprite.setAlpha(0.3f);
    }

    public boolean isInside() {
        if (getPosition().x > 0 - getSprite()
                .getWidth() && getPosition().x < world.gameWidth + getSprite().getWidth()
                && getPosition().y > 0 - getSprite()
                .getHeight() && getPosition().y < world.gameHeight + getSprite().getHeight()) {
            return true;
        }
        return false;
    }

    public void setShadowD(float i, float y) {
        shadowD.set(i, y);
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Sprite getFlashSprite() {
        return flashSprite;
    }

    public Sprite getShadowSprite() {
        return shadowSprite;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setColor(Color color) {
        this.color = color;
        sprite.setColor(color);
    }

    public TweenManager getManager() {
        return manager;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public void setVelocity(Vector2 vec) {
        this.velocity = new Vector2(vec.cpy());
    }

    public void setVelocity(float x1, float y1) {
        this.velocity = new Vector2(x1, y1);
    }

    public void setAcceleration(int i, int i1) {
        this.acceleration = new Vector2(i, i1);
    }

    public void setPosition(float x1, float y1) {
        this.position = new Vector2(x1, y1);
    }

    public void setPosition(Vector2 position1) {
        this.position = new Vector2(position1.cpy());
    }

    public void setScale(float scale) {
        getSprite().setScale(scale);
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public void setYPosition(float YPosition) {
        this.position.y = YPosition;
    }

}

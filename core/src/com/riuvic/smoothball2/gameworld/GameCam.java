package com.riuvic.smoothball2.gameworld;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameCam {

    private OrthographicCamera camera;
    private GameWorld world;
    private Vector3 point;
    private Rectangle bounds;

    public GameCam(GameWorld world, float x, float y, float width, float height) {
        this.world  = world;
        camera = new OrthographicCamera(world.gameWidth, world.gameHeight);
        point = new Vector3(world.worldWidth / 2, world.worldHeight / 2, 0);
        camera.position.set(point);
        camera.update();
        bounds = new Rectangle(point.x - world.gameWidth / 2, point.y - world.gameHeight / 2,
                world.gameWidth, world.gameHeight);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        bounds.setPosition(point.x - world.gameWidth / 2,
                point.x - world.gameHeight / 2);

    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setPoint(Vector2 vec) {
        point = new Vector3(vec.x, vec.y, 0);
    }

    public Rectangle getBounds() {
        return bounds;
    }


    public Vector3 getPoint() {
        return point;
    }
}

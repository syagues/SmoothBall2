package com.riuvic.smoothball2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.riuvic.smoothball2.gameworld.GameWorld;
import com.riuvic.smoothball2.helpers.InputHandler;
import com.riuvic.smoothball2.maingame.ActionResolver;
import com.riuvic.smoothball2.gameworld.GameRenderer;
import com.riuvic.smoothball2.maingame.Radical;


public class GameScreen implements Screen {

    private static GameWorld world;
    private GameRenderer renderer;
    private float runTime;
    public float sW = Gdx.graphics.getWidth();
    public float sH = Gdx.graphics.getHeight();
    public float gameWidth = 1080;
    public float gameHeight = sH / (sW / gameWidth);

    public float worldWidth = gameWidth * 1;
    public float worldHeight = gameHeight * 1;


    public GameScreen(Radical game, ActionResolver actionResolver) {
        Gdx.app.log("GameScreen", "Attached");
        Gdx.app.log("GameWidth " + gameWidth, "GameHeight " + gameHeight);
        world = new GameWorld(game, actionResolver, gameWidth, gameHeight, worldWidth, worldHeight);
        Gdx.input.setInputProcessor(new InputHandler(world, sW / gameWidth, sH
                / gameHeight));
        renderer = new GameRenderer(world, (int) gameWidth, (int) gameHeight);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize");
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide called");
        world.setPauseMode();
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause called");
        //world.setPauseMode();
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");
    }

    @Override
    public void dispose() {

    }
}

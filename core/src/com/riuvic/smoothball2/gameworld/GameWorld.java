package com.riuvic.smoothball2.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.riuvic.smoothball2.configuration.Configuration;
import com.riuvic.smoothball2.configuration.Settings;
import com.riuvic.smoothball2.gameobjects.Background;
import com.riuvic.smoothball2.gameobjects.GameObject;
import com.riuvic.smoothball2.gameobjects.Hero;
import com.riuvic.smoothball2.gameobjects.Menu;
import com.riuvic.smoothball2.gameobjects.PlatformManager;
import com.riuvic.smoothball2.gameobjects.Star;
import com.riuvic.smoothball2.gameobjects.Tutorial;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.helpers.FlatColors;
import com.riuvic.smoothball2.helpers.InputHandler;
import com.riuvic.smoothball2.maingame.ActionResolver;
import com.riuvic.smoothball2.maingame.Radical;
import com.riuvic.smoothball2.ui.PointsUI;
import com.riuvic.smoothball2.ui.Text;

import java.util.ArrayList;



public class GameWorld {

    public final float w;
    //GENERAL VARIABLES
    public float gameWidth;
    public float gameHeight;
    public float worldWidth;
    public float worldHeight;

    public ActionResolver actionResolver;
    //TODO:CHANGE THIS!
    public Radical game;
    public GameWorld world = this;

    //GAME CAMERA
    private GameCam camera;

    //VARIABLES
    private GameState gameState;
    private int score;
    private boolean joined = true;
    private float updateDelay;
    private int lastHighScore;
    private boolean soundActive = true;
    private int coins = 100;
    private float totalTime = 10;

    //GAMEOBJECTS
    private Background background;
    private static Menu menu;
    private GameObject top, scoreCircle;
    private Hero hero, heroL, heroR;
    private PlatformManager platformManager;
    private Text scoreText;
    private PointsUI coinsUI, timeProtectionUI, timeLessVelUI;
    private ParticleEffect divideEffect;

    public int tries = 1;
    private int numberOfStars = 25;
    public final ArrayList<Star> stars = new ArrayList<Star>();

    public Tutorial tutorial;

    public GameWorld(Radical game, ActionResolver actionResolver, float gameWidth,
                     float gameHeight, float worldWidth, float worldHeight) {

        this.gameWidth = gameWidth;
        this.w = gameHeight / 100;
        this.gameHeight = gameHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.game = game;
        this.actionResolver = actionResolver;

        gameState = GameState.MENU;
        camera = new GameCam(this, 0, 0, gameWidth, gameHeight);

        background = new Background(world, 0, 0, gameWidth, gameHeight, AssetLoader.background);

        top = new GameObject(world, -5, -5, gameWidth + 5, gameHeight + 5, AssetLoader.square, FlatColors.SKY_BLUE, GameObject.Shape.RECTANGLE);
        top.fadeOut(.8f, .1f);
        //scoreCircle = new GameObject(world, world.gameWidth / 2 - (600 / 2), (world.gameHeight * 7) / 8 - (600 / 2) - 50, 600, 600, AssetLoader.score_circle, FlatColors.WHITE, GameObject.Shape.CIRCLE);
        scoreText = new Text(world, world.gameWidth / 2 - (900 / 2), (world.gameHeight * 7) / 8 - 160, 900, 220, AssetLoader.square, FlatColors.SKY_BLUE, "200", AssetLoader.fontScore, world.parseColor(Settings.SCORE_TEXT_COLOR), 0, Align.center);
        coinsUI = new PointsUI(world, world.gameWidth - 250, world.gameHeight - 200, AssetLoader.uiback.getRegionWidth(), 75, AssetLoader.uiback, FlatColors.WHITE, GameObject.Shape.RECTANGLE, Align.left);
        timeProtectionUI = new PointsUI(world, world.gameWidth - 200, world.gameHeight - 300, AssetLoader.uiback3.getRegionWidth(), 75, AssetLoader.uiback3, FlatColors.WHITE, GameObject.Shape.RECTANGLE, Align.left);
        timeLessVelUI = new PointsUI(world, world.gameWidth - 200, world.gameHeight - 300, AssetLoader.uiback3.getRegionWidth(), 75, AssetLoader.uiback2, FlatColors.WHITE, GameObject.Shape.RECTANGLE, Align.left);

        checkIfMusicWasPlaying();

        resetTutorial();
        resetGame();
        resetMenu();
        menu.start();
        menu.getBackground().getSprite().setAlpha(1);

        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new Star(world));
        }

        updateDelay = Settings.DIRECTION_DELAY;

        // DIVIDE EFFECT
        divideEffect = new ParticleEffect();
        divideEffect.load(Gdx.files.internal("misc/divideEf.p"), Gdx.files.internal(""));
        divideEffect.setPosition(-100, -100);
    }

    private void checkIfMusicWasPlaying() {
        if (AssetLoader.getVolume()) {
            AssetLoader.music.setLooping(true);
            AssetLoader.music.play();
            AssetLoader.music.setVolume(Settings.MUSIC_VOLUME);
            AssetLoader.setVolume(true);
            setSoundActive(true);
        } else {
            setSoundActive(false);
        }
    }

    public void update(float delta) {
        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).update(delta);
        }

        background.update(delta);
        platformManager.update(delta);
        hero.update(delta);
        heroL.update(delta);
        heroR.update(delta);
        top.update(delta);
        tutorial.update(delta);
        //UI
        //scoreCircle.update(delta);
        scoreText.update(delta);
        coinsUI.update(delta); // Marcador
        timeProtectionUI.update(delta); // Marcador
        timeLessVelUI.update(delta); // Marcador
        collisions();
        menu.update(delta);

        divideEffect.update(delta);
        divideEffect.setPosition(getHero().getPositionPointX(), getHero().getPositionPointY() - 5);
    }

    private void collisions() {

    }

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer, ShaderProgram fontShader,
                       ShaderProgram fontShaderA) {
        background.render(batcher, shapeRenderer);

        //scoreCircle.render(batcher, shapeRenderer);
        scoreText.render(batcher, shapeRenderer, fontShader);
        if (isRunning() || isTutorial()) {
            for (int i = 0; i < stars.size(); i++) {
                stars.get(i).render(batcher, shapeRenderer);
            }
        }
        platformManager.render(batcher, shapeRenderer);

        if(InputHandler.isTouching() && !isTutorial()){
            heroL.render(batcher, shapeRenderer);
            heroR.render(batcher, shapeRenderer);
            divideEffect.draw(batcher);
            divideHero();
            joined = false;
            updateDelay = Settings.DIRECTION_DELAY;
        } else {
            joinHero();
            if(joined || isTutorial()) {
                hero.render(batcher, shapeRenderer);
            } else {
                heroL.render(batcher, shapeRenderer);
                heroR.render(batcher, shapeRenderer);
            }
        }

        if (isTutorial()) {
            tutorial.render(batcher, shapeRenderer);
        }

        if (isMenu() || isGameOver()) {
            menu.render(batcher, shapeRenderer, fontShader, fontShaderA);
        }
        //gradient.render(batcher,shapeRenderer);
        top.render(batcher, shapeRenderer);

        if (Configuration.DEBUG) {
            batcher.setShader(fontShader);
            batcher.setShader(null);
        }

        if(isRunning())
            coinsUI.render(batcher, shapeRenderer, fontShader); // Marcador coins (%)

        if(hero.isProtectedState() && heroL.isProtectedState() && heroR.isProtectedState() && isRunning()) {
            timeProtectionUI.render(batcher, shapeRenderer, fontShader); // Marcador temps restant protection (:)
            startTimerCountdown(timeProtectionUI);
        }

        if(hero.isTimelessState() && heroL.isTimelessState() && heroR.isTimelessState() && isRunning()) {
            timeLessVelUI.render(batcher, shapeRenderer, fontShader);
            startTimerCountdown(timeLessVelUI);
        }
    }

    public void finishGame() {
        saveScoreLogic();
        gameState = GameState.MENU;
        resetMenu();
        menu.startGameOver();
        world.actionResolver.showBanner(true);
    }

    public void saveScoreLogic() {
        AssetLoader.addGamesPlayed();
        int gamesPlayed = AssetLoader.getGamesPlayed();

        // GAMES PLAYED ACHIEVEMENTS!
        actionResolver.submitScore(score);
        setLastHighScore(AssetLoader.getHighScore());
        if (score > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(score);
        }
        checkAchievements();
    }

    private void checkAchievements() {
        if (actionResolver.isSignedIn()) {
            if (score >= 25) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_25_P);
            if (score >= 50) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_50_P);
            if (score >= 75) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_75_P);
            if (score >= 100) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_100_P);
            if (score >= 150) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_150_P);
            if (score >= 200) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_200_P);
            if (score >= 250) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_250_P);
            if (score >= 300) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_300_P);
            if (score >= 400) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_400_P);
            if (score >= 500) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_500_P);
            if (score >= 600) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_600_P);
            if (score >= 700) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_700_P);
            if (score >= 800) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_800_P);
            if (score >= 1000) actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_1000_P);

            // GAMES PLAYED
//            int gamesPlayed = AssetLoader.getGamesPlayed();
//            if (gamesPlayed >= 10)
//                actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_10_GP);
//            if (gamesPlayed >= 25)
//                actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_25_GP);
//            if (gamesPlayed >= 50)
//                actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_50_GP);
//            if (gamesPlayed >= 100)
//                actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_100_GP);
//            if (gamesPlayed >= 200)
//                actionResolver.unlockAchievementGPGS(Configuration.ACHIEVEMENT_200_GP);
        }
    }

    public void startGame() {
        score = 0;
        scoreText.setText(score + "");
        actionResolver.setTrackerScreenName("Game");
        timeProtectionUI.setText("0:10");
    }

    public GameCam getCamera() {
        return camera;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int i) {
        score += i;
        scoreText.setText(score + "");
        restCoin();
        changeSprites();
    }

    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
        }
        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }

    public static Color parseColor(String hex) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
        }
        Color color = Color.valueOf(hex1);
        color.a = 1f;
        return color;
    }

    public boolean isRunning() {
        return gameState == GameState.RUNNING;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAMEOVER;
    }

    public boolean isMenu() {
        return gameState == GameState.MENU;
    }

    public boolean isPaused() {
        return gameState == GameState.PAUSE;
    }

    public boolean isTutorial() {
        return gameState == GameState.TUTORIAL;
    }

    public Radical getGame() {
        return game;
    }

    public void resetGame() {
        score = 0;
        hero = heroL = heroR = null;
        platformManager = null;
        heroL = new Hero(world, this.gameWidth / 2, Settings.HERO_INITIAL_Y, (Settings.HERO_WIDTH * 3) / 4,
                (Settings.HERO_HEIGHT * 3) / 4, AssetLoader.rocket, Color.WHITE, GameObject.Shape.RECTANGLE);
        heroR = new Hero(world, this.gameWidth / 2, Settings.HERO_INITIAL_Y, (Settings.HERO_WIDTH * 3) / 4,
                (Settings.HERO_HEIGHT * 3) / 4, AssetLoader.rocket, Color.WHITE, GameObject.Shape.RECTANGLE);
        hero = new Hero(world, this.gameWidth / 2, Settings.HERO_INITIAL_Y, Settings.HERO_WIDTH,
                Settings.HERO_HEIGHT, AssetLoader.rocket, Color.WHITE, GameObject.Shape.RECTANGLE);
        resetPlatformManager();
        actionResolver.setTrackerScreenName("Reset Game");
    }


    public void resetPlatformManager() {
        platformManager = new PlatformManager(world);
    }

    public void resetMenu() {
        menu = null;
        menu = new Menu(world, 0, 0, gameWidth, gameHeight, AssetLoader.square, FlatColors.WHITE,
                GameObject.Shape.RECTANGLE);
    }


    public void resetTutorial() {
        tutorial = new Tutorial(world, 0, 0, gameWidth, gameHeight, AssetLoader.square,
                FlatColors.DARK_BLACK,
                GameObject.Shape.RECTANGLE);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public static Menu getMenu() {
        return menu;
    }

    public Hero getHero() {
        return hero;
    }

    public Hero getHeroL() {
        return heroL;
    }

    public Hero getHeroR() {
        return heroR;
    }

    public void divideHero(){
        float divideSpeed = getDivideSpeed();
        if(joined) {
            startDivideEffect();
            startDivideEffect();
        }
        if(world.getHeroL().getPositionPointX() > (Settings.HERO_WIDTH * 3) / 8 && world.getHeroR().getPositionPointX() < gameWidth - (Settings.HERO_WIDTH * 3) / 8){
            if(world.getHero().isTimelessState() && world.getHeroL().isTimelessState() && world.getHeroR().isTimelessState())
                divideSpeed = divideSpeed * 3 / 4;

            world.getHeroL().setPositionPointX(world.getHeroL().getPositionPointX() - divideSpeed);
            world.getHeroR().setPositionPointX(world.getHeroR().getPositionPointX() + divideSpeed);
        }
    }

    public void joinHero(){
        float joinSpeed = getDivideSpeed();
        if(world.getHeroL().getPositionPointX() < (gameWidth / 2) - (Settings.HERO_WIDTH * 3) / 8 && world.getHeroR().getPositionPointX() > (gameWidth / 2) - (Settings.HERO_WIDTH * 3) / 8){
            if(world.getHero().isTimelessState() && world.getHeroL().isTimelessState() && world.getHeroR().isTimelessState())
                joinSpeed = joinSpeed * 3 / 4;

            if(updateDelay >= 0){
                world.getHeroL().setPositionPointX(world.getHeroL().getPositionPointX() + ((joinSpeed * (Settings.DIRECTION_DELAY - updateDelay)) / Settings.DIRECTION_DELAY));
                world.getHeroR().setPositionPointX(world.getHeroR().getPositionPointX() - ((joinSpeed * (Settings.DIRECTION_DELAY - updateDelay)) / Settings.DIRECTION_DELAY));
                updateDelay--;
            } else {
                world.getHeroL().setPositionPointX(world.getHeroL().getPositionPointX() + joinSpeed);
                world.getHeroR().setPositionPointX(world.getHeroR().getPositionPointX() - joinSpeed);
            }
        } else {
            joined = true;
        }
    }

    public boolean isJoined(){
        return joined;
    }

    public void startDivideEffect() {
        divideEffect = new ParticleEffect();
        divideEffect.load(Gdx.files.internal("misc/divideEf.p"), Gdx.files.internal(""));
        divideEffect.setPosition(getHero().getPositionPointX(), getHero().getPositionPointY());
        divideEffect.start();
        divideEffect.reset();
        divideEffect.setDuration(1);
    }

    public float getDivideSpeed() {
        float speed = Settings.HERO_DIVIDE_SPEED;
        if(score >= 25 && score < 50) {
            speed += 1;
        } else if(score >= 50 && score < 75) {
            speed += 2;
        } else if(score >= 75 && score < 100) {
            speed += 3;
        } else if(score >= 100 && score < 150) {
            speed += 4;
        } else if(score >= 150 && score < 200) {
            speed += 5;
        } else if(score >= 200 && score < 300) {
            speed += 6;
        } else if(score >= 300 && score < 400) {
            speed += 7;
        } else if(score >= 400) {
            speed += 8;
        }

        return speed;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPauseMode() {
        gameState = GameState.PAUSE;
    }

    public void setToRunning() {
        gameState = GameState.RUNNING;
    }

    public PlatformManager getPlatformManager() {
        return platformManager;
    }

    public PointsUI getCoinsUI() {
        return coinsUI;
    }

    public Text getScoreText() {
        return scoreText;
    }

    public int getCoins(){
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
        getCoinsUI().setText(coins + "%");
    }

    public void addCoins(int i) {
        if(getCoins() + i > 100)
            setCoins(100);
        else
            setCoins(getCoins() + i);
    }

    public void restCoin() {
        int coinsToRest = 0;
        if(score >= 0 && score < 50) {
            coinsToRest = 1;
        } else if(score >= 50 && score < 100) {
            coinsToRest = 2;
        } else if(score >= 100 && score < 150) {
            coinsToRest = 3;
        } else if(score >= 150 && score < 300) {
            coinsToRest = 4;
        } else if(score >= 300) {
            coinsToRest = 5;
        }

        if(getCoins() - coinsToRest > 0) {
            setCoins(getCoins() - coinsToRest);
        } else {
            // Death
            getHeroL().notCollided = false;
            getHeroR().notCollided = false;
            getHero().notCollided = false;
            platformManager.finish();
        }
    }

    public void setLastHighScore(int score){
        lastHighScore = score;
    }

    public int getLastHighScore() {
        return lastHighScore;
    }

    public void setScoreText(int score) {
        scoreText.setText(score + "");
        changeSprites();
    }

    public boolean isSoundActive() {
        return soundActive;
    }

    public void setSoundActive(boolean active) {
        if(active) {
            soundActive = true;
            AssetLoader.music.setLooping(true);
            AssetLoader.music.play();
            AssetLoader.music.setVolume(Settings.MUSIC_VOLUME);
            AssetLoader.setVolume(true);
        } else {
            soundActive = false;
            AssetLoader.music.pause();
            AssetLoader.setVolume(false);
        }
    }

    public void changeSprites() {
        if (score >= 25) {
            platformManager.changeSprites(AssetLoader.platform2);
        }
        if (score >= 50) {
            platformManager.changeSprites(AssetLoader.platform3);
        }
        if (score >= 75) {
            platformManager.changeSprites(AssetLoader.platform4);
        }
        if (score >= 100) {
            platformManager.changeSprites(AssetLoader.platform5);
        }
        if (score >= 125) {
            platformManager.changeSprites(AssetLoader.platform6);
        }
        if (score >= 150) {
            platformManager.changeSprites(AssetLoader.platform7);
        }
        if (score >= 175) {
            platformManager.changeSprites(AssetLoader.platform8);
        }
        if (score >= 200) {
            platformManager.changeSprites(AssetLoader.platform9);
        }
        if (score >= 225) {
            platformManager.changeSprites(AssetLoader.platform10);
        }
        if (score >= 250) {
            platformManager.changeSprites(AssetLoader.platform1);
        }
        if (score >= 275) {
            platformManager.changeSprites(AssetLoader.platform2);
        }
        if (score >= 300) {
            platformManager.changeSprites(AssetLoader.platform3);
        }
        if (score >= 325) {
            platformManager.changeSprites(AssetLoader.platform4);
        }
        if (score >= 350) {
            platformManager.changeSprites(AssetLoader.platform5);
        }
        if (score >= 375) {
            platformManager.changeSprites(AssetLoader.platform6);
        }
        if (score >= 400) {
            platformManager.changeSprites(AssetLoader.platform7);
        }
        if (score >= 425) {
            platformManager.changeSprites(AssetLoader.platform8);
        }
        if (score >= 450) {
            platformManager.changeSprites(AssetLoader.platform9);
        }
        if (score >= 475) {
            platformManager.changeSprites(AssetLoader.platform10);
        }
        if (score >= 500) {
            platformManager.changeSprites(AssetLoader.platform1);
        }
    }

    public void changeHeroSprites(TextureRegion texture){
        world.getHero().getSprite().setRegion(texture);
        world.getHeroL().getSprite().setRegion(texture);
        world.getHeroR().getSprite().setRegion(texture);
        totalTime = 10;
    }

    public void startTimerCountdown(PointsUI timeUI) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        totalTime -= deltaTime;
        if(Integer.toString((int)totalTime % 60).length() > 1)
            timeUI.setText("0:" + Integer.toString((int)totalTime % 60));
        else
            timeUI.setText("0:0" + Integer.toString((int)totalTime % 60));
        if(totalTime <= 0) {
            hero.setNormalState();
            heroL.setNormalState();
            heroR.setNormalState();
            world.changeHeroSprites(AssetLoader.rocket);
        }
    }
}

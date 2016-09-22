package com.riuvic.smoothball2.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.riuvic.smoothball2.configuration.Configuration;
import com.riuvic.smoothball2.configuration.Settings;


public class AssetLoader {

    public static Texture logoTexture, dotT, dotT2, dotT3, dotT4,
            buttonsT, buttonBackT, buttonBackT2, backgroundT, titleT, rocketT, rocketT2, rocketT3, coinT, coinT2, coinT3, scoreBackT, platformT, score_circleT, uibackT, uibackT2, uibackT3,
            xButtonT, homeButtonT, replayButtonT, bigBannerRecordT, bigBannerGameOverT, particlesT, fingerT;
    public static TextureRegion logo, square, dot, dot2, dot3, dot4, playButtonUp, rankButtonUp, background, uiback, uiback2, uiback3,
            title, pauseButton, rocket, rocket2, rocket3, rateButtonUp, soundOffButtonUp, coin, coin2, coin3, particle1, particle2, soundOnButtonUp, shareButtonUp, buttonBack,
            buttonBack2, arrow, scoreBack, score_circle, platform1, platform2, platform3, platform4, platform5, platform6, platform7,
            platform8, platform9, platform10, xButton, homeButton, replayButton, bigBannerRecord, bigBannerGameOver, finger;

    //BUTTONS
    public static BitmapFont font, fontS, fontXS, fontB, fontXL, fontScore;
    private static Preferences prefs;

    //MUSIC
    public static Music music;
    public static Sound click, success, end, coinS;


    public static void load1() {
        logoTexture = new Texture(Gdx.files.internal("logo4.png"));
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        logo = new TextureRegion(logoTexture, 0, 0, logoTexture.getWidth(), logoTexture.getHeight());
        backgroundT = new Texture(Gdx.files.internal("background2.png"));
        backgroundT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        background = new TextureRegion(backgroundT, 0, 0, backgroundT.getWidth(), backgroundT.getHeight());
    }

    public static void load() {
        //LOGO TEXTURE "logo.png"

        square = new TextureRegion(new Texture(Gdx.files.internal("square.png")), 0, 0, 10, 10);

        // DOTS
        dotT = new Texture(Gdx.files.internal("dot.png"));
        dotT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        dot = new TextureRegion(dotT, 0, 0, dotT.getWidth(), dotT.getHeight());

        dotT2 = new Texture(Gdx.files.internal("dot2.png"));
        dotT2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        dot2 = new TextureRegion(dotT2, 0, 0, dotT2.getWidth(), dotT2.getHeight());

        dotT3 = new Texture(Gdx.files.internal("dot3.png"));
        dotT3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        dot3 = new TextureRegion(dotT3, 0, 0, dotT3.getWidth(), dotT3.getHeight());

        dotT4 = new Texture(Gdx.files.internal("dot4.png"));
        dotT4.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        dot4 = new TextureRegion(dotT4, 0, 0, dotT4.getWidth(), dotT4.getHeight());

        // COINS
        coinT = new Texture(Gdx.files.internal("coin2.png"));
        coinT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        coin = new TextureRegion(coinT, 0, 0, coinT.getWidth(), coinT.getHeight());

        coinT2 = new Texture(Gdx.files.internal("coin3.png"));
        coinT2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        coin2 = new TextureRegion(coinT2, 0, 0, coinT2.getWidth(), coinT2.getHeight());

        coinT3 = new Texture(Gdx.files.internal("coin4.png"));
        coinT3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        coin3 = new TextureRegion(coinT3, 0, 0, coinT3.getWidth(), coinT3.getHeight());

        // Hero
        rocketT = new Texture(Gdx.files.internal("rocket4.png"));
        rocketT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        rocket = new TextureRegion(rocketT, 0, 0, rocketT.getWidth(), rocketT.getHeight());

        rocketT2 = new Texture(Gdx.files.internal("rocket5.png"));
        rocketT2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        rocket2 = new TextureRegion(rocketT2, 0, 0, rocketT2.getWidth(), rocketT2.getHeight());

        rocketT3 = new Texture(Gdx.files.internal("rocket6.png"));
        rocketT3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        rocket3 = new TextureRegion(rocketT3, 0, 0, rocketT3.getWidth(), rocketT3.getHeight());

        // UIBACK
        uibackT = new Texture(Gdx.files.internal("uiback5.png"));
        uibackT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        uiback = new TextureRegion(uibackT, 0, 0, uibackT.getWidth(), uibackT.getHeight());

        uibackT2 = new Texture(Gdx.files.internal("uiback6.png"));
        uibackT2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        uiback2 = new TextureRegion(uibackT2, 0, 0, uibackT2.getWidth(), uibackT2.getHeight());

        uibackT3 = new Texture(Gdx.files.internal("uiback7.png"));
        uibackT3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        uiback3 = new TextureRegion(uibackT3, 0, 0, uibackT3.getWidth(), uibackT3.getHeight());

        // Amb cercle blanc
        buttonBackT = new Texture(Gdx.files.internal("button_back.png"));
        buttonBackT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        buttonBack = new TextureRegion(buttonBackT, 0, 0, buttonBackT.getWidth(), buttonBackT.getHeight());

        // Sense cercle blanc
        buttonBackT2 = new Texture(Gdx.files.internal("button_back2.png"));
        buttonBackT2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        buttonBack2 = new TextureRegion(buttonBackT2, 0, 0, buttonBackT2.getWidth(), buttonBackT2.getHeight());

        titleT = new Texture(Gdx.files.internal("title3.png"));
        titleT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        title = new TextureRegion(titleT, 0, 0, titleT.getWidth(), titleT.getHeight());

        score_circleT = new Texture(Gdx.files.internal("score_circle2.png"));
        score_circleT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        score_circle = new TextureRegion(score_circleT, 0, 0, score_circleT.getWidth(),
                score_circleT.getHeight());

        fingerT = new Texture(Gdx.files.internal("finger.png"));
        fingerT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        finger = new TextureRegion(fingerT, 0, 0, fingerT.getWidth(),
                fingerT.getHeight());

        // PLATFORMS
        platformT = new Texture(Gdx.files.internal("platform1.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform1 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform2.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform2 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform3.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform3 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform4.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform4 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform5.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform5 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform6.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform6 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform7.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform7 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform8.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform8 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform9.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform9 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        platformT = new Texture(Gdx.files.internal("platform10.png"));
        platformT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        platform10 = new TextureRegion(platformT, 0, 0, platformT.getWidth(), platformT.getHeight());

        //MENU
        xButtonT = new Texture(Gdx.files.internal("menu/xButton.png"));
        xButtonT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        xButton = new TextureRegion(xButtonT, 0, 0, xButtonT.getWidth(),
                xButtonT.getHeight());

        homeButtonT = new Texture(Gdx.files.internal("menu/homeButton.png"));
        homeButtonT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        homeButton = new TextureRegion(homeButtonT, 0, 0, homeButtonT.getWidth(), homeButtonT.getHeight());

        replayButtonT = new Texture(Gdx.files.internal("menu/replayButton.png"));
        replayButtonT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        replayButton = new TextureRegion(replayButtonT, 0, 0, replayButtonT.getWidth(), replayButtonT.getHeight());

        bigBannerRecordT = new Texture(Gdx.files.internal("menu/bigBanner5.png"));
        bigBannerRecordT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        bigBannerRecord = new TextureRegion(bigBannerRecordT, 0, 0, bigBannerRecordT.getWidth(),
                bigBannerRecordT.getHeight());

        bigBannerGameOverT = new Texture(Gdx.files.internal("menu/bigBanner6.png"));
        bigBannerGameOverT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        bigBannerGameOver = new TextureRegion(bigBannerGameOverT, 0, 0, bigBannerGameOverT.getWidth(),
                bigBannerGameOverT.getHeight());

        //LOADING FONT
        Texture tfont = new Texture(Gdx.files.internal("misc/font.png"), true);
        tfont.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("misc/font2.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        //fontXL = new BitmapFont(Gdx.files.internal("misc/font.ttf"), new TextureRegion(tfont), true);
        parameter.size = 250;
        fontXL = generator.generateFont(parameter);
        fontXL.getData().setScale(1f, 1f);
        fontXL.setColor(FlatColors.WHITE);

        //font = new BitmapFont(Gdx.files.internal("misc/font.ttf"), new TextureRegion(tfont), true);
        FreeTypeFontParameter parameter1 = new FreeTypeFontParameter();
        parameter1.size = 100;
        font = generator.generateFont(parameter1);
        font.getData().setScale(1f, 1f);
        font.setColor(FlatColors.WHITE);

        //fontB = new BitmapFont(Gdx.files.internal("misc/font.tff"), new TextureRegion(tfont), true);
        FreeTypeFontParameter parameter2 = new FreeTypeFontParameter();
        parameter2.size = 60;
        fontB = generator.generateFont(parameter2);
        fontB.getData().setScale(1.4f, 1.4f);
        fontB.setColor(FlatColors.WHITE);

        //fontS = new BitmapFont(Gdx.files.internal("misc/font.tff"), new TextureRegion(tfont), true);
//        FreeTypeFontParameter parameter3 = new FreeTypeFontParameter();
//        parameter3.size = 50;
        Texture tfontScore = new Texture(Gdx.files.internal("misc/fontScore.png"), true);
        tfont.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
        fontS = new BitmapFont(Gdx.files.internal("misc/fontScore.fnt"),
                new TextureRegion(tfontScore),
                false);
        fontS.getData().setScale(0.2f, 0.2f);
        fontS.setColor(FlatColors.WHITE);

        //fontXS = new BitmapFont(Gdx.files.internal("misc/font.ttf"), new TextureRegion(tfont), true);
        FreeTypeFontParameter parameter4 = new FreeTypeFontParameter();
        parameter4.size = 60;
        fontXS = generator.generateFont(parameter4);
        fontXS.getData().setScale(1f, 1f);
        fontXS.setColor(FlatColors.WHITE);

        fontScore = new BitmapFont(Gdx.files.internal("misc/fontScore.fnt"),
                new TextureRegion(tfontScore),
                false);
        fontScore.getData().setScale(1f, 1f);
        fontScore.setColor(FlatColors.WHITE);

        //BUTTONS
        buttonsT = new Texture(Gdx.files.internal("buttons2.png"));
        buttonsT.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //CROP BUTTONS
        playButtonUp = new TextureRegion(buttonsT, 0, 0, 240, 240);
        rankButtonUp = new TextureRegion(buttonsT, 240, 0, 240, 240);
        soundOnButtonUp = new TextureRegion(buttonsT, 720, 0, 240, 240);
        rateButtonUp = new TextureRegion(buttonsT, 960, 0, 240, 240);
        shareButtonUp = new TextureRegion(buttonsT, 1200, 0, 240, 240);
        soundOffButtonUp = new TextureRegion(buttonsT, 480, 0, 240, 240);
        arrow = new TextureRegion(buttonsT, 1200 + 240, 0, 240, 240);
        pauseButton = new TextureRegion(buttonsT, 1200 + 480, 0, 240, 240);


        //PREFERENCES - SAVE DATA IN FILE
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        if (!prefs.contains("games")) {
            prefs.putInteger("games", 0);
        }

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.ogg"));
        click = Gdx.audio.newSound(Gdx.files.internal("sounds/blip_click.wav"));
        success = Gdx.audio.newSound(Gdx.files.internal("sounds/blip_success.wav"));
        end = Gdx.audio.newSound(Gdx.files.internal("sounds/blip_end.wav"));
        coinS = Gdx.audio.newSound(Gdx.files.internal("sounds/blip_coin.wav"));

        //PARTICLES
        particlesT = new Texture(Gdx.files.internal("particles3.png"));
        particlesT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        particle1 = new TextureRegion(particlesT, 0, 0, particlesT.getWidth() / 2,
                particlesT.getHeight());
        particle2 = new TextureRegion(particlesT, 120, 0, particlesT.getWidth() / 2,
                particlesT.getHeight());
    }

    public static void dispose() {
        font.dispose();
        fontS.dispose();
        fontXS.dispose();
        fontB.dispose();
        fontScore.dispose();
        fontXL.dispose();
        click.dispose();
        success.dispose();
        end.dispose();
        dotT.dispose();
        logoTexture.dispose();

    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static void setButtonsClicked(int val) {
        prefs.putInteger("buttonsClicked", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void addGamesPlayed() {
        prefs.putInteger("games", prefs.getInteger("games") + 1);
        prefs.flush();
    }

    public static int getGamesPlayed() {
        return prefs.getInteger("games");
    }

    public static void setAds(boolean removeAdsVersion) {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
        prefs.putBoolean("ads", removeAdsVersion);
        prefs.flush();
    }

    public static boolean getAds() {
        Gdx.app.log("ADS", prefs.getBoolean("ads") + "");
        return prefs.getBoolean("ads", false);
    }


    public static int getCoinNumber() {
        return prefs.getInteger("bonus");
    }

    public static void addCoinNumber(int number) {
        prefs.putInteger("bonus", prefs.getInteger("bonus") + number);
        prefs.flush();
    }

    public static void prefs() {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
    }

    public static void setVolume(boolean val) {
        prefs.putBoolean("volume", val);
        prefs.flush();
    }

    public static boolean getVolume() {
        return prefs.getBoolean("volume", true);
    }

}

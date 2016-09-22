package com.riuvic.smoothball2.maingame;


public interface ActionResolver {
    public void showOrLoadInterstital();

    public void signIn();

    public void signOut();

    public void rateGame();

    public void submitScore(long score);

    public void showScores();

    public boolean isSignedIn();

    public boolean shareGame(String msg);

    public void unlockAchievementGPGS(String string);

    public void showAchievement();

    //public void iapClick();

    public void toast(String string);

    public void showBanner(boolean show);

    public void setTrackerScreenName(String path);
}

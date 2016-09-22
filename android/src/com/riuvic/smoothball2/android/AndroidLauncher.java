package com.riuvic.smoothball2.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.riuvic.smoothball2.android.util.IabHelper;
import com.riuvic.smoothball2.android.util.IabResult;
import com.riuvic.smoothball2.android.util.Inventory;
import com.riuvic.smoothball2.android.util.Purchase;
import com.riuvic.smoothball2.configuration.Configuration;
import com.riuvic.smoothball2.helpers.AssetLoader;
import com.riuvic.smoothball2.maingame.ActionResolver;
import com.riuvic.smoothball2.maingame.Radical;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AndroidLauncher extends AndroidApplication implements
		ActionResolver, GameHelper.GameHelperListener {

	private static final String AD_UNIT_ID_BANNER = Configuration.AD_UNIT_ID_BANNER;
	private static final String AD_UNIT_ID_INTERSTITIAL = Configuration.AD_UNIT_ID_INTERSTITIAL;
	private final static int REQUEST_CODE_UNUSED = 9002;

	private static String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
	protected AdView adView;
	protected View gameView;
	AdView admobView;
	private InterstitialAd interstitialAd;
	private GameHelper _gameHelper;
	private AnalyticsApplication analyticsApplication;
	private Tracker tracker, globalTracker;

	//IAP
	private static final String TAG = "IAP";
	boolean mIsPremium = false;
	static final String SKU_PREMIUM = Configuration.PRODUCT_ID;
	static final int RC_REQUEST = 10001;
	IabHelper mHelper;
	private boolean removeAdsVersion = false;
	SharedPreferences prefs;
	private Radical game;

	// Show ADS
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	protected Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case SHOW_ADS:
				{
					adView.setVisibility(View.VISIBLE);
					break;
				}
				case HIDE_ADS:
				{
					adView.setVisibility(View.GONE);
					break;
				}
			}
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		prefs = getSharedPreferences(Configuration.GAME_NAME, Context.MODE_PRIVATE);
//		loadIAPstuff();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		cfg.useImmersiveMode = true;

		// Analytics
		analyticsApplication = (AnalyticsApplication) getApplication();
		tracker = analyticsApplication.getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
		globalTracker = analyticsApplication.getTracker(AnalyticsApplication.TrackerName.GLOBAL_TRACKER);

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=" + getPackageName();

		FrameLayout layout = new FrameLayout(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		admobView = createAdView();

		View gameView = createGameView(cfg);
		layout.addView(gameView);
		layout.addView(admobView); // ACTIVAR BANNER

		_gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		_gameHelper.enableDebugLog(false);
		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInSucceeded() {
			}

			@Override
			public void onSignInFailed() {
			}
		};
		_gameHelper.setup(gameHelperListener);


		setContentView(layout);
		startAdvertising(admobView);

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {

			}

			@Override
			public void onAdClosed() {
			}
		});
		showOrLoadInterstital();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent
			data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);
		if (mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
			_gameHelper.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	void complain(String message) {
		Log.e(TAG, "**** Ultra Square Error: " + message);
		alert("Error: " + message);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
	}

	void saveData() {
		SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
		spe.putBoolean("ads", removeAdsVersion);
		spe.commit();
		AssetLoader.setAds(removeAdsVersion);
		Log.d(TAG, "Saved data: tank = " + String.valueOf(removeAdsVersion));
	}

	void loadData() {
		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		removeAdsVersion = sp.getBoolean("ads", false);
		removeAdsVersion = AssetLoader.getAds();
		Log.d(TAG, "Loaded data: tank = " + String.valueOf(removeAdsVersion));
	}


	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID_BANNER);
		//adView.setId(1); // this is an arbitrary id, allows for relative
		// positioning in createGameView()
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		params.gravity = Gravity.BOTTOM;

		adView.setLayoutParams(params);
		if (!removeAdsVersion) {
			adView.setVisibility(View.VISIBLE);
		} else {
			adView.setVisibility(View.GONE);
		}
		adView.setBackgroundColor(Color.TRANSPARENT);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new Radical(this), cfg);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		gameView.setLayoutParams(params);
		return gameView;
	}

	private void startAdvertising(AdView adView) {
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Configuration.LEADERBOARDS)
			_gameHelper.onStart(this);
		if (adView != null)
			adView.resume();
	}

	@Override
	public void onPause() {
		if (adView != null)
			adView.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (Configuration.LEADERBOARDS)
			_gameHelper.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}


	@Override
	public void showOrLoadInterstital() {
		if (!removeAdsVersion) {
			try {
				runOnUiThread(new Runnable() {
					public void run() {
						// ACTIVAR INTERSTITIAL
						if (interstitialAd.isLoaded()) {
							interstitialAd.show();

						} else {
							AdRequest interstitialRequest = new AdRequest.Builder().build();
							interstitialAd.loadAd(interstitialRequest);

						}
					}
				});
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {
				// @Override
				public void run() {
					_gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage()
					+ ".");
		}
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				// @Override
				public void run() {
					_gameHelper.signOut();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage()
					+ ".");
		}
	}

	@Override
	public void rateGame() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL)));
	}

	@Override
	public boolean isSignedIn() {
		return _gameHelper.isSignedIn();
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}

	@Override
	public void submitScore(long score) {
		if (isSignedIn() == true) {
			Games.Leaderboards.submitScore(_gameHelper.getApiClient(),
					Configuration.LEADERBOARD_HIGHSCORE, score);
		} else {
			signIn();
		}
	}

	@Override
	public void showScores() {
		if (isSignedIn())
			startActivityForResult(
					Games.Leaderboards.getAllLeaderboardsIntent(_gameHelper
							.getApiClient()), REQUEST_CODE_UNUSED);
			// Games.Leaderboards.getLeaderboardIntent( _gameHelper.getApiClient(),
			// C.LEADERBOARD_ID),REQUEST_CODE_UNUSED)
		else {
			signIn();
		}
	}

	@Override
	public void showAchievement() {
		if (isSignedIn())
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(_gameHelper
							.getApiClient()), REQUEST_CODE_UNUSED);
		else {
			signIn();
		}
	}

	@Override
	public boolean shareGame(String msg) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, msg + GOOGLE_PLAY_URL);
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Share..."));
		return true;

	}

	@Override
	public void unlockAchievementGPGS(String string) {
		if (isSignedIn()) {
			Games.Achievements.unlock(_gameHelper.getApiClient(), string);
		}
	}




	@Override
	public void toast(final String text) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void showBanner(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	@Override
	public void setTrackerScreenName(String path) {
		// Set screen name.
		// Where path is a String representing the screen name.
		globalTracker.setScreenName(path);
		globalTracker.send(new HitBuilders.AppViewBuilder().build());
		Log.i("AndroidLauncher", "Tracker sended");
	}
}

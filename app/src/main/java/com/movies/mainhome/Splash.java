package com.movies.mainhome;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.movies.setting.JsonUtils;
import com.onesignal.OneSignal;
import com.movies.setting.config;
import com.statailo.cinemaxhd.R;
import com.movies.setting.itemjson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Splash extends AppCompatActivity {

    public static String ads_splash = "";
    public static String ads_show = "";
    public static String ads_content = "";
    public static String statususer = "";
    public static String statusopup = "";
    public static String titlepopup = "";
    public static String iconpopup = "";
    public static String bannerpup = "";
    public static String messagepopup = "";
    public static String rrlpageck = "";

    public static String pub_id = "";
    public static String ads_inter = "";
    public static String banner_id = "";

    public static String inter_content = "";
    public static String banner_content = "";

    ImageView sphs_mLogo;
    Button click_mplay, click_mrate;
    TextView load_ads;
    RelativeLayout textpolicy;
    public InterstitialAd mInterstitialAd;
    public com.facebook.ads.InterstitialAd interstitialFb;
    private AVLoadingIndicatorView playprogress;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splahs);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        playprogress = (AVLoadingIndicatorView) findViewById(R.id.playprogress);
        playprogress.setIndicator("BallSpinFadeLoaderIndicator");

        sphs_mLogo = (ImageView) findViewById(R.id.iconmsphs);
        click_mplay = findViewById(R.id.play_mapp);
        click_mrate = findViewById(R.id.rate_mapp);
        textpolicy = findViewById(R.id.privacypolicy_btn);

        load_ads = findViewById(R.id.laod_ads);
        click_mrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApps();
            }
        });

        textpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Splash.this, Privacy.class);
                startActivity(i);
            }
        });

        if (JsonUtils.isNetworkAvailable(Splash.this)) {
            new getSetting().execute();
        }

        SystemClock.sleep(2500);
        startApp();
    }

    public void loadInter() {
        final ProgressDialog progressInter = new ProgressDialog(this);
//        progressInter.setIndeterminate(false);
//        progressInter.setCancelable(false);
//        progressInter.setMessage("Loading...");
        playprogress.show();

        if (ads_splash.equals("admob")) {
            mInterstitialAd = new InterstitialAd(getApplicationContext());
            mInterstitialAd.setAdUnitId(Splash.ads_inter);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    playprogress.hide();
                    load_ads.setVisibility(View.INVISIBLE);
                    mInterstitialAd.show();

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    playprogress.hide();
                    load_ads.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(Splash.this, MianActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    Intent i = new Intent(Splash.this, MianActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        } else {
            AudienceNetworkAds.initialize(this);
            interstitialFb = new com.facebook.ads.InterstitialAd(this, ads_inter);
            interstitialFb.setAdListener(new InterstitialAdExtendedListener() {
                @Override
                public void onRewardedAdCompleted() {

                }

                @Override
                public void onRewardedAdServerSucceeded() {

                }

                @Override
                public void onRewardedAdServerFailed() {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    playprogress.hide();
                    load_ads.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(Splash.this, MianActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    interstitialFb.isAdLoaded();
                    playprogress.hide();
                    load_ads.setVisibility(View.INVISIBLE);
                    interstitialFb.show();
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }

                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Intent i = new Intent(Splash.this, MianActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onInterstitialActivityDestroyed() {

                }

            });
            interstitialFb.loadAd();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class getSetting extends AsyncTask<Void, Void, Void> {

        String data = "";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(config.APP_DET);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }

                try {
                    JSONObject jsonObject = new JSONObject(data);

                    JSONArray jsonArray = jsonObject.getJSONArray("MOV");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        itemjson itemChannel = new itemjson();

                        pub_id = itemChannel.setPubId(object.getString(config.APPPUB));
                        ads_splash = itemChannel.setSplhsAds(object.getString(config.ADSSPLAHS));
                        ads_show = itemChannel.setAdShow(object.getString(config.ADS_SHOW));
                        ads_content = itemChannel.setContentAds(object.getString(config.ADSCONTENT));

                        statususer = itemChannel.setStatusAds(object.getString(config.STATUSUSER));
                        statusopup = itemChannel.setPopUpStatus(object.getString(config.STATUSPOPUP));
                        titlepopup = itemChannel.setPopUpTlite(object.getString(config.TITLEPOPUP));
                        iconpopup = itemChannel.setPopUpIcon(object.getString(config.ICONPOPUP));
                        bannerpup = itemChannel.setPopUpBanner(object.getString(config.BANNERPOPUP));
                        messagepopup = itemChannel.setPopUpMaesed(object.getString(config.MESSAGEPopup));
                        rrlpageck = itemChannel.setAppPageck(object.getString(config.URLPAGECK));

                        ads_inter = itemChannel.setInterSplahs(object.getString(config.INTERAD));
                        banner_id = itemChannel.setBannerPlahs(object.getString(config.BANNERAD));

                        inter_content = itemChannel.setInterContent(object.getString(config.INTERFB));
                        banner_content = itemChannel.setBannerContent(object.getString(config.BANNERFB));

                        if (ads_splash.equals("admob")) {
                            ads_inter = itemChannel.setInterSplahs(object.getString(config.INTERAD));
                            banner_id = itemChannel.setBannerPlahs(object.getString(config.BANNERAD));
                        } else {
                            ads_inter = itemChannel.setInterContent(object.getString(config.INTERFB));
                            banner_id = itemChannel.setBannerContent(object.getString(config.BANNERFB));
                        }
                        if (ads_content.equals("admob")) {
                            inter_content = itemChannel.setInterContent(object.getString(config.INTERAD));
                            banner_content = itemChannel.setBannerContent(object.getString(config.BANNERAD));
                        } else {
                            inter_content = itemChannel.setInterContent(object.getString(config.INTERFB));
                            banner_content = itemChannel.setBannerContent(object.getString(config.BANNERFB));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public void startApp() {
        if (statususer != null) {
            if (statususer.equals("no")) {
                click_mplay.setVisibility(View.VISIBLE);
                click_mrate.setVisibility(View.VISIBLE);
                click_mplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        click_mplay.setVisibility(View.INVISIBLE);
                        click_mrate.setVisibility(View.INVISIBLE);
                        load_ads.setVisibility(View.VISIBLE);
                        loadInter();
                    }
                });

            } else {
                sphs_mLogo.setVisibility(View.GONE);
                MoveApps();
            }

        } else {
            WarningBox();
        }
    }

    public void rateApps() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getPackageName())));
        }
    }


    public void MoveApps() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(Splash.this)
                        .setTitle("Apps Maintenance")
                        .setMessage("This App is on maintenance,\nPlease go to our new apps with new feature and new experience.")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id="
                                                + rrlpageck)));
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    public void WarningBox() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(Splash.this)
                        .setTitle("No Connection")
                        .setMessage("Please check your internet connection\nThis app running well with good connection")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(1);
                                finish();
                            }
                        })
                        .show();
            }
        });
    }
}


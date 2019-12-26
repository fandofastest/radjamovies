package com.movies.mainhome;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.movies.adapter.ApMore;
import com.movies.setting.config;
import com.movies.setting.item_mov;
import com.statailo.cinemaxhd.R;
import com.movies.setting.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityMore extends AppCompatActivity {
    ArrayList<item_mov> itemMovies;
    public RecyclerView recyclerView;
    ApMore movieAdapter;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    String Id, Name;
    private LinearLayout lyt_not_found;
    public com.google.android.gms.ads.InterstitialAd mInterstitialAd;
    public com.facebook.ads.InterstitialAd interstitialFb;
    public com.google.android.gms.ads.AdView adView;
    public com.facebook.ads.AdView fbView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        Id = intent.getStringExtra("Id");
        Name = intent.getStringExtra("name");

        setTitle(Name);
        itemMovies = new ArrayList<>();

        RelativeLayout banner = findViewById(R.id.adView);

        if (Splash.ads_content.equals("admob")){
            adView = new com.google.android.gms.ads.AdView(this);
            adView.setAdUnitId(Splash.banner_id);
            adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
            banner.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        } else {
            fbView = new com.facebook.ads.AdView(this, Splash.banner_id, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            banner.addView(fbView);
            fbView.loadAd();
        }

        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ActivityMore.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);

        if (JsonUtils.isNetworkAvailable(ActivityMore.this)) {
            new getCategory().execute(config.CATEGORY_ITEM_URL + Id);
        }
    }

    private class getCategory extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            return JsonUtils.getJSONString(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(config.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        item_mov objItem = new item_mov();
                        objItem.setId(objJson.getInt(config.MOVIE_ID));
                        objItem.setMovieTitle(objJson.getString(config.MOVIE_TITLE));
                        objItem.setQuality(objJson.getString(config.MOVIE_QUALITY));
                        objItem.setImage(objJson.getString(config.MOVIE_IMAGE));
                        objItem.setRating(objJson.getString(config.MOVIE_RATE));
                        objItem.setMovieUrl(objJson.getString(config.MOVIE_URL));
                        objItem.setSubtitle(objJson.getString(config.MOVIE_SUBTITLE));
                        objItem.setDescription(objJson.getString(config.MOVIE_DESC));
                        objItem.setDownload(objJson.getString(config.MOVIE_DOWNLOAD));
                        objItem.setTrailer(objJson.getString(config.MOVIE_TRAILER));
                        objItem.setCatid(objJson.getString(config.CAT_CID));
                        objItem.setCatname(objJson.getString(config.CAT_NAME));
                        itemMovies.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }

    private void displayData() {
        movieAdapter = new ApMore(ActivityMore.this, itemMovies);
        recyclerView.setAdapter(movieAdapter);

        if (movieAdapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            avLoadingIndicatorView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}

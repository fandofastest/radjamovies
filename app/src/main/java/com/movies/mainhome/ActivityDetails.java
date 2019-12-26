package com.movies.mainhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.movies.fragment.ObjectsFragment;
import com.movies.setting.config;
import com.movies.setting.item_mov;
import com.squareup.picasso.Picasso;
import com.statailo.cinemaxhd.R;
import com.movies.setting.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class ActivityDetails extends AppCompatActivity {
    ImageView imgChannel,imgChannel2,  btnTrailer, btnDownload, imgFavourite, imgBackground;
    Button btnReport, imgPlay, sharebtn;
    TextView txtChannelName, txtDescription, quality, catname, txtChannelRating,cser1,cser2,cser3,cser4,cser5;
    ArrayList<item_mov> mListItem, mListItemRelated;
    ScrollView mScrollView;
    //    ProgressDialog mProgressDialog;
    ProgressBar mProgressBar;
    item_mov objBean;
    String app="com.google.android.youtube";
    String drive="https://drive.google.com/uc?export=download&id=";
    private FragmentManager fragmentManager;
    LinearLayout lyt_may_you;
    private AVLoadingIndicatorView playprogress, reldetailprogres;
    private ConstraintLayout mMovieTabLayout;
    private int mPosterHeight;
    private int mPosterWidth;
    private int mBackdropHeight;
    private int mBackdropWidth;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    LinearLayout hideownload,ChoseServ;
    RelativeLayout btnsever;
    String videoId, subtitle, gdriveUrl;
    String Id,Title,Image,URL,Subtitle,Quality,Description, Rating, Download, Trailer, CatName, CatId;
    public TextView bgQuality;
    public static ArrayList<HttpCookie> cookgd = new ArrayList();
    public com.google.android.gms.ads.AdView adView;
    public com.facebook.ads.AdView fbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Id = i.getStringExtra("Id");
        Title = i.getStringExtra("Title");
        Image = i.getStringExtra("Image");
        URL = i.getStringExtra("URL");
        Subtitle = i.getStringExtra("Subtitle");
        Quality = i.getStringExtra("Quality");
        Description = i.getStringExtra("Description");
        Rating = i.getStringExtra("Rating");
        Download = i.getStringExtra("Download");
        Trailer = i.getStringExtra("Trailer");
        CatName = i.getStringExtra("Catname");
        CatId = i.getStringExtra("CatId");

        setContentView(R.layout.activity_details);

        fragmentManager = getSupportFragmentManager();
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
        setTitle(Title);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playprogress= (AVLoadingIndicatorView) findViewById(R.id.playprogress);
        reldetailprogres = (AVLoadingIndicatorView) findViewById(R.id.relprogress);
        playprogress.setIndicator("BallSpinFadeLoaderIndicator");
        reldetailprogres.setIndicator("BallSpinFadeLoaderIndicator");
        reldetailprogres.show();
        reldetailprogres.setVisibility(View.VISIBLE);

        RelativeLayout banner = (RelativeLayout) findViewById(R.id.adView);
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

        //databaseHelper = new dbHelper(getApplicationContext());

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        mBackdropWidth = getResources().getDisplayMetrics().widthPixels;
        mBackdropHeight = (int) (mBackdropWidth / 1.95);

        mPosterWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.25);
        mPosterHeight = (int) (mPosterWidth / 0.66);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        quality = (TextView) findViewById(R.id.quality);
        imgChannel = (ImageView) findViewById(R.id.img_channel);
        imgChannel2 = (ImageView) findViewById(R.id.img_channel2);
        catname = (TextView) findViewById(R.id.category);

       /* imgChannel.getLayoutParams().width = mPosterWidth;
        imgChannel.getLayoutParams().height = mPosterHeight;*/
        bgQuality = (TextView) findViewById(R.id.quality);

        //imgBackground.getLayoutParams().width = mBackdropWidth;
        //imgBackground.getLayoutParams().height = mBackdropHeight;
        btnsever = (RelativeLayout) findViewById(R.id.btn_server);
        imgPlay = (Button) findViewById(R.id.play);
        txtChannelRating = (TextView) findViewById(R.id.rate);
//        sharebtn = (Button) findViewById(R.id.share_btn);

        txtChannelName = (TextView) findViewById(R.id.txt_tlite);
        txtDescription = (TextView) findViewById(R.id.txt_details);
        mMovieTabLayout = (ConstraintLayout) findViewById(R.id.layout_toolbar_movie);

        ChoseServ = (LinearLayout) findViewById(R.id.Lay_server);

        txtChannelName = (TextView) findViewById(R.id.txt_tlite);
        cser1 = (TextView) findViewById(R.id.serv1);
        cser2 = (TextView) findViewById(R.id.serv2);
        cser3 = (TextView) findViewById(R.id.serv3);
        cser4 = (TextView) findViewById(R.id.serv4);
        cser5 = (TextView) findViewById(R.id.serv5);

        hideownload = (LinearLayout) findViewById(R.id.hidedownload);
        btnDownload = (ImageView) findViewById(R.id.download);
        btnTrailer = (ImageView) findViewById(R.id.trailer);

        mListItem = new ArrayList<>();
        mListItemRelated = new ArrayList<>();

        if (JsonUtils.isNetworkAvailable(ActivityDetails.this)) {
            new getMovid().execute(config.SINGLE_CHANNEL_URL + Id);
        } else {
            // showToast(getString(R.string.conne_msg1));
        }
        txtChannelName.setText(Title);
        txtDescription.setText(Html.fromHtml(Description));
        txtChannelRating.setText(Rating);
        quality.setText(Quality);
        catname.setText(CatName);

        if(quality.getText().equals("HD")){
            bgQuality.setBackgroundColor(Color.parseColor("#00701a"));
        } else if (quality.getText().equals("CAM")){
            bgQuality.setBackgroundColor(Color.parseColor("#e53935"));
        } else {
            bgQuality.setBackgroundColor(Color.parseColor("#2962ff"));
        }

        Picasso.with(ActivityDetails.this).load(config.P_IMAGE + Image).transform(new BlurTransformation(this,13,1)).into(imgChannel2);

        if(MianActivity.dISP.substring(0,6).toLowerCase().equals("google")){
            imgPlay.setVisibility(View.GONE);
            hideownload.setVisibility(View.GONE);
            imgChannel.setVisibility(View.GONE);
            txtDescription.setText(Html.fromHtml("This application does not provide streaming content or movie downloads. You can only see movie trailers from various categories. All trailer categories are available, such as drama movies, thriller movies, sci-movies, adventure movies, horror movies, crime movies, comedy movies, etc."));
        }else if(MianActivity.dISP.substring(0,9).toLowerCase().equals("incapsula")){
            imgPlay.setVisibility(View.GONE);
            hideownload.setVisibility(View.GONE);
            imgChannel.setVisibility(View.GONE);
            txtDescription.setText(Html.fromHtml("This application does not provide streaming content or movie downloads. You can only see movie trailers from various categories. All trailer categories are available, such as drama movies, thriller movies, sci-movies, adventure movies, horror movies, crime movies, comedy movies, etc."));
        }else{
            imgPlay.setVisibility(View.VISIBLE);
            Picasso.with(ActivityDetails.this).load(config.P_IMAGE + Image).into(imgChannel);
            txtDescription.setText(Html.fromHtml(Description));
        }

//        Picasso.with(ActivityDetails.this).load(config.P_IMAGE + Image).into(imgChannel);
//        Picasso.with(ActivityDetails.this).load(config.P_IMAGE + Image)
//                .transform(new BlurTransformation(this, 8, 1))
//                .into(imgBackground);


        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW, Uri.parse(Trailer)
                ));
            }
        });

        catname.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMore.class);
                intent.putExtra("Id", String.valueOf(CatId));
                intent.putExtra("name", CatName);
                startActivity(intent);
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW, Uri.parse(Download)
                ));
            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CServer = URL;
                final String[] mystringserv = CServer.split("~");

                // cek server dulu
                if(mystringserv.length<=1){
                    String exurl = mystringserv[0];
                    String[] servurl = exurl.split("~");
                    // jika server cuma satu
                    videoId = servurl[0];
                    subtitle = "https://pastebin.com/raw/";
                    new driveGetUrl().execute();
                    ChoseServ.setVisibility(View.GONE);

                }else {
                    ChoseServ.setVisibility(View.VISIBLE);

                    for (int ss = 0; ss < mystringserv.length; ss++) {
                        String exurl = mystringserv[ss];
                        final String[] servurl = exurl.split("~");


                        if (ss == 0) {
                            cser1.setVisibility(View.VISIBLE);
                            cser1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
//                                    videoId = drive+servurl[0];
                                    videoId = servurl[0];
                                    subtitle = Subtitle;
                                    new driveGetUrl().execute();
                                    ChoseServ.setVisibility(View.GONE);
                                }
                            });

                        }
                        if (ss == 1) {
                            cser2.setVisibility(View.VISIBLE);
                            cser2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
//                                    videoId = drive+servurl[0];
                                    subtitle = Subtitle;
                                    videoId = servurl[0];
                                    new driveGetUrl().execute();
                                    ChoseServ.setVisibility(View.GONE);
                                }
                            });

                        }
                        if (ss == 2) {
                            cser3.setVisibility(View.VISIBLE);
                            cser3.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
//                                    videoId = drive+servurl[0];
                                    videoId = servurl[0];
                                    subtitle = Subtitle;
                                    new driveGetUrl().execute();
                                    ChoseServ.setVisibility(View.GONE);
                                }
                            });

                        }
                        if (ss == 3) {
                            cser4.setVisibility(View.VISIBLE);
                            cser4.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
//                                    videoId = drive+servurl[0];
                                    videoId = servurl[0];
                                    subtitle = Subtitle;
                                    new driveGetUrl().execute();
                                    ChoseServ.setVisibility(View.GONE);
                                }
                            });

                        }
                        if (ss == 4) {
                            cser5.setVisibility(View.VISIBLE);
                            cser5.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
//                                    videoId = drive+servurl[0];
                                    videoId = servurl[0];
                                    subtitle = Subtitle;
                                    new driveGetUrl().execute();
                                    ChoseServ.setVisibility(View.GONE);
                                }
                            });

                        }



                    }
                }

                ChoseServ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChoseServ.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    private class getMovid extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            if (null == result || result.length() == 0) {
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(config.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        JSONArray jsonArrayChild = objJson.getJSONArray(config.RELATED_ITEM_ARRAY_NAME);
                        if (jsonArrayChild.length() != 0) {
                            for (int j = 0; j < 9; j++) {
                                JSONObject objChild = jsonArrayChild.getJSONObject(j);
                                item_mov item = new item_mov();
                                item.setId(objChild.getInt(config.RELATED_ITEM_CHANNEL_ID));
                                item.setMovieTitle(objChild.getString(config.RELATED_ITEM_CHANNEL_NAME));
                                item.setMovieUrl(objChild.getString(config.RELATED_ITEM_CHANNEL_URL));
                                item.setImage(objChild.getString(config.RELATED_ITEM_CHANNEL_THUMB));
                                item.setCatid(CatId);
                                item.setCatname(CatName);
                                item.setRating(objChild.getString(config.RELATED_ITEM_CHANNEL_RATING));
                                item.setDescription(objChild.getString(config.RELATED_ITEM_CHANNEL_DESC));
                                item.setQuality(objChild.getString(config.RELATED_ITEM_CHANNEL_QUALITY));
                                item.setTrailer(objChild.getString(config.RELATED_ITEM_TRAILER));
                                item.setDownload(objChild.getString(config.RELATED_ITEM_DOWNLOAD));

                                mListItemRelated.add(item);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }
        }
    }

    private void setResult() {
        if (!mListItemRelated.isEmpty()) {
            ObjectsFragment storeFragment = ObjectsFragment.newInstance(mListItemRelated);
            fragmentManager.beginTransaction().replace(R.id.Container, storeFragment).commitAllowingStateLoss();
            reldetailprogres.hide();
            reldetailprogres.setVisibility(View.GONE);
        } else {
            lyt_may_you.setVisibility(View.GONE);
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

    private class driveGetUrl extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final ProgressDialog progressInter = new ProgressDialog(getApplicationContext());
//            mProgressDialog = new ProgressDialog(ActivityDetails.this, R.style.MyAlertDialogStyle);
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setMessage("Preparing to download...");
//            mProgressDialog.show();
            playprogress.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
//            com.movies.setting.drive sh = new drive();
//
//            String url = videoId;
//            String confirmCode = sh.makeServiceCall(url);
//
//            gdriveUrl = url + "&confirm=" + confirmCode;
            gdriveUrl = "http://fando.id/gdrive.php?id=" + videoId;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            Intent intent = new Intent(ActivityDetails.this, PlayerVideos.class);
            intent.putExtra("subtitle", subtitle);
            intent.putExtra("url", gdriveUrl);
            intent.putExtra("Title", Title);
            intent.putExtra("fsub", subtitle);
            startActivity(intent);
//            mProgressDialog.dismiss();
            playprogress.hide();

        }
    }

}

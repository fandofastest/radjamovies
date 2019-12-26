package com.movies.mainhome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.statailo.cinemaxhd.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.ArrayList;

public class PlayerSubtitle extends Activity implements PlaybackControlView.VisibilityListener, View.OnClickListener {
    private String subtitle,subfirst,Id,Title,Image,Director,Description,firsturl;
    private String url,furl;
    private AVLoadingIndicatorView load;
    private RelativeLayout layoutSub;
    private TextView empty, Moviename;
    private long exitTime = 0;
    private LinearLayout debugRootView;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ImageView btnSubtitle;
    private Button btnEnd;
    private DataSource.Factory mediaDataSourceFactory;
    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    private static final CookieManager DEFAULT_COOKIE_MANAGER;
    private boolean startAutoPlay;
    private TrackGroupArray lastSeenTrackGroupArray;
    private int startWindow;
    private long startPosition;
    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.player);

        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            //trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }

        Moviename = (TextView) findViewById(R.id.textMovies);

        Moviename.setText(getIntent().getStringExtra("Title"));

        url = getIntent().getStringExtra("url");
        Id = getIntent().getStringExtra("Id");
        Title = getIntent().getStringExtra("Title");
        firsturl = getIntent().getStringExtra("FIRSTURL");
        Image = getIntent().getStringExtra("Image");
        Director = getIntent().getStringExtra("Director");
        Description = getIntent().getStringExtra("Description");
        subfirst = getIntent().getStringExtra("fsub");

        debugRootView = findViewById(R.id.controls_root);

        url = getIntent().getStringExtra("url");
        subtitle ="https://pastebin.com/raw/";

        debugRootView = findViewById(R.id.controls_root);
        layoutSub = (RelativeLayout) findViewById(R.id.lay_sub);
        btnEnd = (Button) findViewById(R.id.button);
        btnSubtitle = (ImageView) findViewById(R.id.sub);
        ListView listView = (ListView) findViewById(R.id.list_sub);

        layoutSub.setVisibility(View.GONE);

        init();

        final ArrayList subtitleList = new ArrayList<>();
        final  String[] mystring = subfirst.split("~");
        for (int j = 0; j < mystring.length; j++) {
            String explocek = mystring[j];
            String[] subfrom = explocek.split("-");
            String Subtit = subfrom[0];
            subtitleList.add(Subtit);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.textview, subtitleList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                String sexplocek = mystring[position];
                String[] subfroms = sexplocek.split("-");
                String Subtitle = subfroms[1];
                player.stop();

                subtitle = Subtitle;
                init();
                layoutSub.setVisibility(View.GONE);
                // Toast.makeText(getApplicationContext(), "Subtitle Selected : "+ Subtitle,   Toast.LENGTH_LONG).show();

            }
        });



        btnSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSub.setVisibility(View.VISIBLE);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSub.setVisibility(View.GONE);
            }
        });
    }





    public void init() {
        load = (AVLoadingIndicatorView) this.findViewById(R.id.load);
        empty = (TextView) this.findViewById(R.id.empty);
        // mVideoView = (VideoView) this.findViewById(R.id.surface_view);

        simpleExoPlayerView = findViewById(R.id.surface_view);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Uri myUri = Uri.parse(url);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
//        DefaultHttpDataSourceFactory dataSourceFactory = new
//                DefaultHttpDataSourceFactory(Util.getUserAgent(this,
//                "exoplayer2example"));

        String userAgent = Util.getUserAgent(this, "exoplayer");

// Default parameters, except allowCrossProtocolRedirects is true
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent,
                null /* listener */,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true /* allowCrossProtocolRedirects */
        );

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                this,
                null /* listener */,
                httpDataSourceFactory
        );

        CookieManager msCookieManager = new java.net.CookieManager();
        for (HttpCookie cookie : ActivityDetails.cookgd) {
            msCookieManager.getCookieStore().add(null,cookie);
        }

        String incookie = TextUtils.join(";",  msCookieManager.getCookieStore().getCookies());
        Log.d("cookie",incookie);

        httpDataSourceFactory.setDefaultRequestProperty("Cookie", incookie);
//        dataSourceFactory.setDefaultRequestProperty("Cookie", incookie);
        //DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(player);
        simpleExoPlayerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.YELLOW,
                Color.BLACK, Color.TRANSPARENT, CaptionStyleCompat.EDGE_TYPE_NONE, Color.BLACK,
                null));

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(myUri,
                dataSourceFactory, extractorsFactory, null, null);



        Uri subtitleUri = Uri.parse(subtitle);

        Format subtitleFormat = Format.createTextSampleFormat(
                null, // An identifier for the track. May be null.
                MimeTypes.APPLICATION_SUBRIP, // The mime type. Must be set correctly.
                Format.NO_VALUE, // Selection flags for the track.
                "en"); // The subtitle language. May be null.


        MediaSource subtitleSource = new SingleSampleMediaSource(subtitleUri, dataSourceFactory, subtitleFormat, C.TIME_UNSET);

        MergingMediaSource mergedSource = new MergingMediaSource(mediaSource, subtitleSource);

        player.prepare(mergedSource);
        player.setPlayWhenReady(startAutoPlay);
        load.setVisibility(View.GONE);
        loading();

        player.addListener(new Player.EventListener() {


            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_BUFFERING){
                    load.setVisibility(View.VISIBLE);
                } else {
                    load.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }


            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

        });

    }


    private DataSource.Factory getDataSourceFactory() {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                getHttpDataSourceFactory());
    }

    private DataSource.Factory getHttpDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(this,
                "ExoPlayerDemo"), bandwidthMeter);
    }

    private void loading() {
        empty.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
        player.getPlaybackState();

    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            init();
        }
    }



    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - this.exitTime > 2000) {
            Toast.makeText(this, "Press again to close video", 0).show();
            this.exitTime = System.currentTimeMillis();
            return;
        }
        player.stop();
        player.release();
        finish();
    }

    @Override
    public void onVisibilityChange(int visibility) {
        debugRootView.setVisibility(visibility);
    }

    @Override
    public void onClick(View view) {

    }


}
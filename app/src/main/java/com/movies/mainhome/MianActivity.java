package com.movies.mainhome;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.movies.fragment.HomeFragment;
import com.movies.setting.JsonUtils;
import com.movies.setting.config;
import com.statailo.cinemaxhd.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.movies.mainhome.Splash.ads_show;
import static com.movies.mainhome.Splash.rrlpageck;

public class MianActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 212;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String statailo;
    public static int INTER_COUNT, INTER_STAT = 0, INTER_INT;
    public com.google.android.gms.ads.AdView adView;
    public com.facebook.ads.AdView fbView;
    private FragmentManager fm;
    FrameLayout home,latestframe,popularframe,genre;
    HomeFragment homeFragment = new HomeFragment();
    public static String dISP ;
    public static String dIP ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        statailo = getApplicationContext().getPackageName();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (JsonUtils.isNetworkAvailable(MianActivity.this)) {
            new HomeCek().execute(config.configip);
        } else {

        }

        INTER_INT = Integer.valueOf(ads_show);

        home = (FrameLayout) findViewById(R.id.RootLayout);
        latestframe = (FrameLayout) findViewById(R.id.RootLatest);
        popularframe = (FrameLayout) findViewById(R.id.ROotPopular);
        genre = (FrameLayout) findViewById(R.id.RootGenre);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Splash.statusopup.equals("yes")) {
            setPop_up();
        }

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

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.RootLayout, homeFragment).commit();
    }
    private class HomeCek extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null == result || result.length() == 0) {

            } else {

                try {
                    JSONObject obj = new JSONObject(result);
                    dISP = obj.getString("isp");
                    dIP = obj.getString("query");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void setPop_up() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        View dialogView = getLayoutInflater().inflate(R.layout.popup_dialog, null);
        dialogBuilder.setView(dialogView);

        ImageView icon_up = (ImageView) dialogView.findViewById(R.id.ic_icon);
        Glide.with(this).load(Splash.iconpopup).into(icon_up);

        ImageView icon_banner = (ImageView) dialogView.findViewById(R.id.imgbanner);
        Glide.with(this).load(Splash.bannerpup).into(icon_banner);

        TextView title_up = (TextView) dialogView.findViewById(R.id.txttitle);
        title_up.setText(Splash.titlepopup);

        TextView desc_up = (TextView) dialogView.findViewById(R.id.textdesc);
        desc_up.setText(Splash.messagepopup);

        TextView imgInstal = (TextView) dialogView.findViewById(R.id.text_innstal);
        final android.app.AlertDialog playDialog = dialogBuilder.create();
        imgInstal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent rate = new Intent(Intent.ACTION_VIEW);
                rate.setData(Uri.parse("market://details?id=" + rrlpageck));
                startActivity(rate);
                playDialog.dismiss();
            }
        });
        playDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    MenuItemCompat.collapseActionView(searchMenuItem);
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MianActivity.this, ActivitySearch.class);
                intent.putExtra("search_query", arg0);
                startActivity(intent);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        ExitApp();
    }

    private void ExitApp() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        View dialogView = getLayoutInflater().inflate(R.layout.exitdialog, null);
        dialogBuilder.setView(dialogView);
        ImageView imgExit = (ImageView) dialogView.findViewById(R.id.img_exit);
        ImageButton imgRare = (ImageButton) dialogView.findViewById(R.id.img_rate);
        ImageButton imgShare = (ImageButton) dialogView.findViewById(R.id.img_share);
        final android.app.AlertDialog playDialog = dialogBuilder.create();
        imgRare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + getPackageName())));

//                        playDialog.dismiss();
                }
            }
        });
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction( Intent.ACTION_SEND );
                sendIntent.putExtra( Intent.EXTRA_TEXT, getResources().getString( R.string.share_msg ) + getPackageName() );
                sendIntent.setType( "text/plain" );
                startActivity( sendIntent );
            }
        });
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
                finish();
            }
        });
        playDialog.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_rate) {
            RateApp();
        } else if (id == R.id.nav_share) {
            ShareApp();
        } else if (id == R.id.nav_dmca) {
            Intent intent = new Intent(this, Dmca.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void RateApp() {
        final String appName = getPackageName();//your application package name textview.MianActivity play store application url
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="
                            + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + appName)));
        }
    }

    private void ShareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String sAux = "\n" + getResources().getString(R.string.share_msg) + "\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {

        }
    }
}

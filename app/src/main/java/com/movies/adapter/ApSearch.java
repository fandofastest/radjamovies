package com.movies.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.movies.setting.config;
import com.movies.setting.item_mov;
import com.squareup.picasso.Picasso;
import com.movies.mainhome.ActivityDetails;
import com.statailo.cinemaxhd.R;
import com.movies.mainhome.Splash;

import java.util.ArrayList;

import static com.movies.mainhome.MianActivity.INTER_COUNT;
import static com.movies.mainhome.MianActivity.INTER_INT;
import static com.movies.mainhome.MianActivity.INTER_STAT;
import static com.movies.mainhome.MianActivity.dISP;
import static com.movies.mainhome.Splash.ads_content;
import static com.movies.mainhome.Splash.ads_show;
import static com.movies.mainhome.Splash.inter_content;

public class ApSearch extends RecyclerView.Adapter<ApSearch.ItemRowHolder>{
    private ArrayList<item_mov> Listdata;
    private Context context;

    com.google.android.gms.ads.InterstitialAd interstitialAd;
    com.facebook.ads.InterstitialAd interstitialFb;

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ItemRowHolder(v);
    }

    public ApSearch(Context context, ArrayList<item_mov> dataList) {
        this.Listdata = dataList;
        this.context = context;


    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {

        final item_mov singleItem = Listdata.get(position);

        if(dISP.substring(0,6).toLowerCase().equals("google")){
            holder.context.setText(singleItem.getMovieTitle().substring(0,2).toUpperCase());
        }else if(dISP.substring(0,9).toLowerCase().equals("incapsula")){
            holder.context.setText(singleItem.getMovieTitle().substring(0,2).toUpperCase());
        }else{
            Picasso.with(context).load(config.P_IMAGE + singleItem.getImage()).placeholder(R.drawable.iconitem).into(holder.image);
        }

        holder.quality.setText(singleItem.getQuality());
        holder.text.setText(singleItem.getMovieTitle());
        holder.rating.setText(singleItem.getRating());


        if(holder.quality.getText().equals("HD")){
            holder.bgQuality.setBackgroundColor(Color.parseColor("#00701a"));
        } else if (holder.quality.getText().equals("CAM")){
            holder.bgQuality.setBackgroundColor(Color.parseColor("#e53935"));
        } else {
            holder.bgQuality.setBackgroundColor(Color.parseColor("#2962ff"));
        }

        final Activity activity = (Activity)context;

        Animation animation = AnimationUtils.loadAnimation(activity,
                R.anim.animation);
        holder.itemView.startAnimation(animation);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                INTER_COUNT +=1;

                if (INTER_STAT == 0) {
                    if (INTER_COUNT >= INTER_INT ) {
                        LoadInter(singleItem);
                        INTER_STAT = 1;
                        INTER_COUNT = 0;
                    } else {
                        action(singleItem);
                    }
                } else {
                    action(singleItem);
                }

            }
        });
    }

    public void action(item_mov data){
        Intent intent = new Intent(context, ActivityDetails.class);
        intent.putExtra("Id", String.valueOf(data.getId()));
        intent.putExtra("Title", String.valueOf(data.getMovieTitle()));
        intent.putExtra("Image", String.valueOf(data.getImage()));
        intent.putExtra("URL", String.valueOf(data.getMovieUrl()));
        intent.putExtra("Subtitle", String.valueOf(data.getSubtitle()));
        intent.putExtra("Quality", String.valueOf(data.getQuality()));
        intent.putExtra("Rating", String.valueOf(data.getRating()));
        intent.putExtra("Description", String.valueOf(data.getDescription()));
        intent.putExtra("Download", String.valueOf(data.getDownload()));
        intent.putExtra("Trailer", String.valueOf(data.getTrailer()));
        intent.putExtra("Catname", String.valueOf(data.getCatname()));
        intent.putExtra("CatId", String.valueOf(data.getCatid()));
        context.startActivity(intent);
    }

    public void LoadInter(final item_mov datalist) {
        final ProgressDialog interads = new ProgressDialog(context);
        interads.setIndeterminate(false);
        interads.setCancelable(false);
        interads.setMessage("Loading Ads.\nPlease wait...");
        interads.show();

        if (ads_content.equals("admob")) {

            MobileAds.initialize(context);
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(Splash.inter_content);
            interstitialAd.loadAd(new AdRequest.Builder().build());
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    interads.dismiss();
                    interstitialAd.show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    interads.dismiss();
                    action(datalist);
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
                    action(datalist);
                }
            });

        } else {
            AudienceNetworkAds.initialize(context);
            interstitialFb = new com.facebook.ads.InterstitialAd(context, inter_content);
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
                    interads.dismiss();
                    action(datalist);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    interads.dismiss();
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
                    action(datalist);

                }

                @Override
                public void onInterstitialActivityDestroyed() {

                }
            });
            interstitialFb.loadAd();
        }
    }

    @Override
    public int getItemCount() {
        return (null != Listdata ? Listdata.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text, quality, rating, context;
        public LinearLayout lyt_parent;
        public RelativeLayout bgQuality;
        public ItemRowHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.namamovie);
            quality = (TextView) itemView.findViewById(R.id.quality);
            context = (TextView) itemView.findViewById(R.id.context);
            rating = (TextView) itemView.findViewById(R.id.rate);


            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            bgQuality = (RelativeLayout) itemView.findViewById(R.id.bgQuality);

            image.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.32);
            image.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().widthPixels * 0.34) / 0.75);

        }
    }
}


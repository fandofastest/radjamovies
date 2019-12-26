package com.movies.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.gms.ads.MobileAds;
import com.movies.mainhome.Splash;
import com.movies.setting.item_ctgory;
import com.movies.mainhome.ActivityMore;
import com.statailo.cinemaxhd.R;

import java.util.ArrayList;

import static com.movies.mainhome.MianActivity.INTER_COUNT;
import static com.movies.mainhome.MianActivity.INTER_INT;
import static com.movies.mainhome.MianActivity.INTER_STAT;
import static com.movies.mainhome.Splash.ads_content;
import static com.movies.mainhome.Splash.ads_show;
import static com.movies.mainhome.Splash.inter_content;

public class ApCategory extends RecyclerView.Adapter<ApCategory.ItemRowHolder> {
    private ArrayList<item_ctgory> dataList;
    private Context mContext;
    private int lastPosition = -1;

    com.google.android.gms.ads.InterstitialAd interstitialAd;
    com.facebook.ads.InterstitialAd interstitialFb;

    public ApCategory(Context context, ArrayList<item_ctgory> dataList){
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ctgry, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final item_ctgory singleItem = dataList.get(position);
        holder.text.setText(singleItem.getCatName());
        //Picasso.with(mContext).load(confiAPP.P_IMAGE + singleItem.getCatImage()).placeholder(R.drawable.bg).into(holder.image);
        final Activity activity = (Activity)mContext;
        if (position > lastPosition) {
            if (position % 2 == 0) {
                Animation animation = AnimationUtils.loadAnimation(activity,
                        R.anim.animlagi);
                holder.itemView.startAnimation(animation);
                lastPosition = position;
            } else {
                Animation animation = AnimationUtils.loadAnimation(activity,
                        R.anim.animlagi);
                holder.itemView.startAnimation(animation);
                lastPosition = position;
            }
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                INTER_COUNT +=1;

                if (INTER_STAT == 0) {
                    if (INTER_COUNT >= INTER_INT ) {
                        LoadAds(singleItem);
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

    public void action (item_ctgory data) {
        Intent intent = new Intent(mContext, ActivityMore.class);
        intent.putExtra("Id", String.valueOf(data.getCategoryId()));
        intent.putExtra("name", data.getCatName());
        mContext.startActivity(intent);
    }

    public void LoadAds(final item_ctgory data) {
        final ProgressDialog interads = new ProgressDialog(mContext);
        interads.setIndeterminate(false);
        interads.setCancelable(false);
        interads.setMessage("Loading Ads.\nPlease wait...");
        interads.show();

        if (ads_content.equals("admob")) {
            MobileAds.initialize(mContext);
            interstitialAd = new InterstitialAd(mContext);
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
                    action(data);
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
                    action(data);
                }
            });

        } else {
            AudienceNetworkAds.initialize(mContext);
            interstitialFb = new com.facebook.ads.InterstitialAd(mContext, inter_content);
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
                    action(data);
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
                    action(data);

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
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        //public ImageView image;
        public TextView text;
        public ImageView image;
        private RelativeLayout relativeLayout;

        public ItemRowHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.textViewName_category_adapter);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout_category_adapter);
        }
    }
}

package com.movies.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.movies.adapter.ApCategory;
import com.movies.adapter.ApdterPL;
import com.movies.mainhome.ActivityMore2;
import com.movies.setting.config;
import com.movies.setting.item_ctgory;
import com.movies.setting.item_mov;
import com.statailo.cinemaxhd.R;
import com.movies.setting.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    NestedScrollView scrollView;
    ArrayList<item_ctgory> mListItem;
    public RecyclerView recyclerView;
    ApCategory adapter;

    AVLoadingIndicatorView pb;
    ArrayList<item_mov>  latestItem, popularItem, CategoryItem, AnimItem;
    ApdterPL latestadapter, popularadapter, categoryadapter, animadapter;
    RecyclerView latestview, popularview, categoryview, animview;
    Button latestmore, popularmore, actionmore, romancemore, categorymore, animemore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        scrollView = (NestedScrollView) v.findViewById(R.id.scroll);
        pb = (AVLoadingIndicatorView) v.findViewById(R.id.loadanim);

        latestItem = new ArrayList<>();
        popularItem = new ArrayList<>();
        CategoryItem = new ArrayList<>();
        mListItem = new ArrayList<>();
        AnimItem = new ArrayList<>();
        ArrayList<item_ctgory> mListItem;
        latestmore = (Button) v.findViewById(R.id.latestmore);
        popularmore = (Button) v.findViewById(R.id.popularmore);
//        categorymore = (TextView) v.findViewById(R.id.scifimore);
        animemore = (Button) v.findViewById(R.id.animemore);

        recyclerView = (RecyclerView) v.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setFocusable(false);

        latestview = (RecyclerView) v.findViewById(R.id.latestView);
        popularview = (RecyclerView) v.findViewById(R.id.popularView);
        animview = (RecyclerView) v.findViewById(R.id.animRecy);

        latestview.setHasFixedSize(false);
        latestview.setNestedScrollingEnabled(false);
        latestview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        latestview.setFocusable(false);

        popularview.setHasFixedSize(false);
        popularview.setNestedScrollingEnabled(false);
        popularview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popularview.setFocusable(false);

        animview.setHasFixedSize(false);
        animview.setNestedScrollingEnabled(false);
        animview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        animview.setFocusable(false);

        latestmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityMore2.class);
                intent.putExtra("Id", String.valueOf(1));
                intent.putExtra("name", "Latest Movies");
                startActivity(intent);
            }
        });

        popularmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityMore2.class);
                intent.putExtra("Id", String.valueOf(2));
                intent.putExtra("name", "Popular Movies");
                startActivity(intent);
            }
        });

        animemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityMore2.class);
                intent.putExtra("Id", String.valueOf(3));
                intent.putExtra("name", "Action Movies");
                startActivity(intent);
            }
        });

        if (JsonUtils.isNetworkAvailable(getActivity())){
            new taskHome().execute(config.HOME_URL);
            new getCategory().execute(config.CATEGORY_LINK);

        } else {
            //  tampilToast(getString(R.string.msg_no));
        }


        return v;


    }

    private class taskHome extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return JsonUtils.getJSONString(strings[0]);
        }

        @Override
        protected void onPostExecute(String hasil){
            super.onPostExecute(hasil);
            pb.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            if (null == hasil || hasil.length() == 0){
                scrollView.setVisibility(View.GONE);

            }else {
                scrollView.setVisibility(View.VISIBLE);
                try {
                    JSONObject Jsonmain = new JSONObject(hasil);
                    JSONObject arrayJson = Jsonmain.getJSONObject(config.ARRAY_NAME);


                    JSONArray jsonAction = arrayJson.getJSONArray(config.HOME_LATEST_ARRAY);
                    JSONObject jsonObjectAction;
                    for (int i = 0; i < jsonAction.length(); i++){
                        jsonObjectAction = jsonAction.getJSONObject(i);
                        item_mov itemMovie = new item_mov();
                        itemMovie.setId(jsonObjectAction.getInt(config.MOVIE_ID));
                        itemMovie.setMovieTitle(jsonObjectAction.getString(config.MOVIE_TITLE));
                        itemMovie.setImage(jsonObjectAction.getString(config.MOVIE_IMAGE));
                        itemMovie.setMovieUrl(jsonObjectAction.getString(config.MOVIE_URL));
                        itemMovie.setDescription(jsonObjectAction.getString(config.MOVIE_DESC));
                        itemMovie.setSubtitle(jsonObjectAction.getString(config.MOVIE_SUBTITLE));
                        itemMovie.setQuality(jsonObjectAction.getString(config.MOVIE_QUALITY));
                        itemMovie.setTrailer(jsonObjectAction.getString(config.MOVIE_TRAILER));
                        itemMovie.setDownload(jsonObjectAction.getString(config.MOVIE_DOWNLOAD));
                        itemMovie.setCatname(jsonObjectAction.getString(config.CAT_NAME));
                        itemMovie.setCatid(jsonObjectAction.getString(config.CAT_CID));
                        itemMovie.setRating(jsonObjectAction.getString(config.MOVIE_RATE));
                        latestItem.add(itemMovie);

                    }

                    JSONArray jsonRoman = arrayJson.getJSONArray(config.HOME_FEATURED_ARRAY);
                    JSONObject jsonObjectRoman;
                    for (int i = 0; i < jsonAction.length(); i++){
                        jsonObjectRoman = jsonRoman.getJSONObject(i);
                        item_mov itemMovie = new item_mov();
                        itemMovie.setId(jsonObjectRoman.getInt(config.MOVIE_ID));
                        itemMovie.setMovieTitle(jsonObjectRoman.getString(config.MOVIE_TITLE));
                        itemMovie.setImage(jsonObjectRoman.getString(config.MOVIE_IMAGE));
                        itemMovie.setMovieUrl(jsonObjectRoman.getString(config.MOVIE_URL));
                        itemMovie.setDescription(jsonObjectRoman.getString(config.MOVIE_DESC));
                        itemMovie.setSubtitle(jsonObjectRoman.getString(config.MOVIE_SUBTITLE));
                        itemMovie.setQuality(jsonObjectRoman.getString(config.MOVIE_QUALITY));
                        itemMovie.setTrailer(jsonObjectRoman.getString(config.MOVIE_TRAILER));
                        itemMovie.setDownload(jsonObjectRoman.getString(config.MOVIE_DOWNLOAD));
                        itemMovie.setCatname(jsonObjectRoman.getString(config.CAT_NAME));
                        itemMovie.setCatid(jsonObjectRoman.getString(config.CAT_CID));
                        itemMovie.setRating(jsonObjectRoman.getString(config.MOVIE_RATE));
                        popularItem.add(itemMovie);

                    }

                    JSONArray jsonAnim = arrayJson.getJSONArray(config.HOME_ACTION_ARRAY);
                    JSONObject jsonObjectAnim;
                    for (int i = 0; i < jsonAnim.length(); i++){
                        jsonObjectAnim = jsonAnim.getJSONObject(i);
                        item_mov itemMovie = new item_mov();
                        itemMovie.setId(jsonObjectAnim.getInt(config.MOVIE_ID));
                        itemMovie.setMovieTitle(jsonObjectAnim.getString(config.MOVIE_TITLE));
                        itemMovie.setImage(jsonObjectAnim.getString(config.MOVIE_IMAGE));
                        itemMovie.setMovieUrl(jsonObjectAnim.getString(config.MOVIE_URL));
                        itemMovie.setDescription(jsonObjectAnim.getString(config.MOVIE_DESC));
                        itemMovie.setSubtitle(jsonObjectAnim.getString(config.MOVIE_SUBTITLE));
                        itemMovie.setQuality(jsonObjectAnim.getString(config.MOVIE_QUALITY));
                        itemMovie.setTrailer(jsonObjectAnim.getString(config.MOVIE_TRAILER));
                        itemMovie.setDownload(jsonObjectAnim.getString(config.MOVIE_DOWNLOAD));
                        itemMovie.setCatname(jsonObjectAnim.getString(config.CAT_NAME));
                        itemMovie.setCatid(jsonObjectAnim.getString(config.CAT_CID));
                        itemMovie.setRating(jsonObjectAnim.getString(config.MOVIE_RATE));
                        AnimItem.add(itemMovie);

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                resultHome();
            }
        }

    }

    private class getCategory extends AsyncTask<String, Void, String> {

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
//                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(config.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        item_ctgory objItem = new item_ctgory();
                        objItem.setCategoryId(objJson.getInt(config.CAT_CID));
                        objItem.setCatName(objJson.getString(config.CAT_NAME));
                        objItem.setCatImage(objJson.getString(config.CAT_IMAGE));
                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ApCategory(getActivity(), mListItem);
                recyclerView.setAdapter(adapter);

            }
        }
    }

    private void resultHome() {
        latestadapter = new ApdterPL(getActivity(),latestItem);
        latestview.setAdapter(latestadapter);

        popularadapter = new ApdterPL(getActivity(),popularItem);
        popularview.setAdapter(popularadapter);

        animadapter = new ApdterPL(getActivity(),AnimItem);
        animview.setAdapter(animadapter);


    }
}

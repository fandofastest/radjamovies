package com.movies.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.movies.adapter.ApdterPL;
import com.movies.setting.config;
import com.movies.setting.item_mov;
import com.statailo.cinemaxhd.R;
import com.movies.setting.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularFragment extends Fragment {

    ArrayList<item_mov> mListItem;
    public RecyclerView recyclerView;
    ApdterPL adapter;
    private AVLoadingIndicatorView progressBar;
    private LinearLayout lyt_not_found;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_more, container, false);
        mListItem = new ArrayList<>();
        lyt_not_found = (LinearLayout) rootView.findViewById(R.id.lyt_not_found);
        progressBar = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new PopularFragment.getPopular().execute(config.FEATURED_URL);
        }
        return rootView;
    }
    private class getPopular extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
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
                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }
    private void displayData() {
        adapter = new ApdterPL(getActivity(), mListItem);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }


    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}

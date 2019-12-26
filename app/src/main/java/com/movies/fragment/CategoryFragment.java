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

import com.movies.adapter.ApCategory;
import com.movies.setting.config;
import com.movies.setting.item_ctgory;
import com.statailo.cinemaxhd.R;
import com.movies.setting.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    ArrayList<item_ctgory> mListItem;
    public RecyclerView recyclerView;
    ApCategory adapter;
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
        int columns = getResources().getInteger(R.integer.col_cat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columns));
        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new getCategory().execute(config.CATEGORY_LINK);
        }
        return rootView;
    }

    private class getCategory extends AsyncTask<String, Void, String> {

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
                        item_ctgory objItem = new item_ctgory();
                        objItem.setCategoryId(objJson.getInt(config.CAT_CID));
                        objItem.setCatName(objJson.getString(config.CAT_NAME));
                        objItem.setCatImage(objJson.getString(config.CAT_IMAGE));
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
        adapter = new ApCategory(getActivity(), mListItem);
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

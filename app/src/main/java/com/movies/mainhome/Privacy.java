package com.movies.mainhome;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.movies.setting.config;
import com.statailo.cinemaxhd.R;
import com.movies.setting.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Privacy extends AppCompatActivity {
    ProgressBar mProgressBar;
    WebView webView;
    String dmcaHtml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webiew);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Privacy Police");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (JsonUtils.isNetworkAvailable(Privacy.this)) {
            new Privacy.privacyTask().execute(config.APP_DET);
        }
    }

    private class privacyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            if (null == result || result.length() == 0) {
                showToast("No Data");
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(config.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        dmcaHtml=objJson.getString(config.APP_PRIVACY_POLICY);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dmcaResul();
            }
        }
    }

    private void dmcaResul() {

        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";
        String htmlText = dmcaHtml;

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.ttf\")}body{font-family: MyFont;color: #000;text-align:justify}"
                + "</style></head>"
                + "<body bgcolor=\"#fff\">"
                +  htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
    }

    public void showToast(String msg) {
        Toast.makeText(Privacy.this, msg, Toast.LENGTH_LONG).show();
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

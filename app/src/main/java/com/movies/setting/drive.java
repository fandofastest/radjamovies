package com.movies.setting;

import android.util.Log;

import com.movies.mainhome.ActivityDetails;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class drive {
    private static final String TAG = drive.class.getSimpleName();

    public drive() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        String code = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            final String COOKIES_HEADER = "Set-Cookie";

            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            CookieManager msCookieManager = new java.net.CookieManager();
            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {

                    ActivityDetails.cookgd.add(HttpCookie.parse(cookie).get(0));
                    msCookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                }
            }

            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

            String s1 = "confirm=";
            Integer pos = response.indexOf(s1);
            code = response.substring(pos+s1.length(), pos+s1.length() + 4); // 4 digit code
            Log.d("code", code);

            //Download file
            /*URL url2 = new URL(reqUrl + "&" + s1 + code);
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();

            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                conn2.setRequestProperty("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }
            conn2.setRequestMethod("GET");
            in = new BufferedInputStream(conn2.getInputStream());
            response = convertStreamToString(in);
            Log.ObjectsFragment("resp", response);*/

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return code;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

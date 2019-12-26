package com.movies.setting;

import com.statailo.cinemaxhd.BuildConfig;

import java.io.Serializable;

import static com.movies.mainhome.MianActivity.statailo;
import static com.movies.mainhome.Splash.ads_show;


public class config implements Serializable {
    private static final long serialVersionUID = 1L;
    public static String AppURL = BuildConfig.config;

    public static final String P_IMAGE = AppURL + "images/";
    public static final String configip = "http://www.ip-api.com/json";
    public static final String CATEGORY_LINK = AppURL + "api.php?kod="+statailo;

    public static final String CATEGORY_ITEM_URL = AppURL + "api.php?kod="+statailo+"&cat_id=";

    public static final String SEARCH_URL = AppURL + "api.php?kod="+statailo+"&search=";

    public static final String SINGLE_CHANNEL_URL = AppURL + "api.php?kod="+statailo+"&channel_id=";

    public static final String REPORT_CHANNEL_URL = AppURL + "api.php?kod="+statailo+"&name=";

    public static final String REQUEST_CHANNEL_URL = AppURL + "api.php?kod="+statailo+"&name=";

    public static final String HOME_URL = AppURL + "api.php?kod="+statailo+"&home";

    public static final String FEATURED_URL = AppURL + "api_featured.php?kod="+statailo+"";

    public static final String LATEST_URL = AppURL + "api.php?kod="+statailo+"&latest";

    public static final String YOUTUBE_IMAGE_FRONT="http://img.youtube.com/vi/";
    public static final String YOUTUBE_SMALL_IMAGE_BACK="/hqdefault.jpg";
    public static final String DAILYMOTION_IMAGE_PATH="http://www.dailymotion.com/thumbnail/video/";

    public static final String ARRAY_NAME="MOV";

    public static final String CAT_NAME="category_name";
    public static final String CAT_IMAGE="category_image";
    public static final String CAT_CID="cid";

    public static String CATEGORY_TITLE;
    public static int CATEGORY_ID;
    public static String VIDEO_IDD;

    public static final String MOVIE_ID="id";

    public static final String MOVIE_TITLE="movie_title";
    public static final String MOVIE_QUALITY="quality";
    public static final String MOVIE_URL="movie_url";
    public static final String MOVIE_IMAGE="movie_thumbnail";
    public static final String MOVIE_RATE="movie_rating";
    public static final String MOVIE_DESC="movie_desc";
    public static final String MOVIE_DOWNLOAD="download";
    public static final String MOVIE_TRAILER="movie_trailer";
    public static final String MOVIE_SUBTITLE="movie_subtitle";

    public static final String HOME_LATEST_ARRAY="movieupdate";
    public static final String HOME_FEATURED_ARRAY="recommovie";
    public static final String HOME_ACTION_ARRAY="actmovie";
    public static final String HOME_SCIFI_ARRAY="scifimovie";
    public static final String HOME_ANIMATION_ARRAY="animovie";
    public static final String HOME_ROMAN_ARRAY="romanmovie";
    public static final String HOME_CATEGORY_ARRAY="category_list";

    public static final String RELATED_ITEM_ARRAY_NAME="related";
    public static final String RELATED_ITEM_CHANNEL_ID="rel_id";
    public static final String RELATED_ITEM_CHANNEL_NAME="rel_channel_title";
    public static final String RELATED_ITEM_CHANNEL_THUMB="rel_channel_thumbnail";
    public static final String RELATED_ITEM_CHANNEL_URL="rel_channel_url";
    public static final String RELATED_ITEM_CHANNEL_RATING="rel_channel_rating";
    public static final String RELATED_ITEM_CHANNEL_QUALITY="rel_quality";
    public static final String RELATED_ITEM_CHANNEL_DESC="rel_description";
    public static final String RELATED_ITEM_TRAILER="rel_trailer";
    public static final String RELATED_ITEM_DOWNLOAD="rel_download";


    public static final String SLIDER_ARRAY="slider_list";
    public static final String SLIDER_NAME="home_title";
    public static final String SLIDER_SUBTITLE="home_subtitle";
    public static final String SLIDER_IMAGE="home_banner";
    public static final String SLIDER_LINK="home_url";
    public static final String MSG="msg";

    public static final String APP_DET = AppURL + "api.php?kod="+statailo+"&app_details";
    public static final String APP_PRIVACY_POLICY="app_privacy_policy";
    public static final String DMCA="dmca_report";
    public static final String REQ = AppURL + "api.php?";

    public static final String APPPUB = "apppub_id";
    public static final String ADS_SHOW = "ads_show";
    public static final String ADSSPLAHS= "ads_splash";
    public static final String ADSCONTENT= "ads_content";
    public static final String BANNERFB = "banner_fb";
    public static final String INTERFB = "inter_fb";
    public static final String BANNERAD = "banner_ad";
    public static final String INTERAD = "inter_ad";
    public static final String STATUSUSER = "status_user";
    public static final String STATUSPOPUP = "status_popup";
    public static final String TITLEPOPUP = "title_popup";
    public static final String ICONPOPUP = "icon_popup";
    public static final String BANNERPOPUP = "banner_popup";
    public static final String MESSAGEPopup = "message_popup";
    public static final String URLPAGECK = "pack_name";

    public static int AD_COUNT=0;
    public static int AD_COUNT_SHOW=3;
}

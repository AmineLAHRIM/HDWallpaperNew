package com.vpapps.utils;

import com.vpapps.hdwallpaper.BuildConfig;
import com.vpapps.items.ItemAbout;
import com.vpapps.items.ItemGIF;
import com.vpapps.items.ItemWallpaper;

import java.io.Serializable;
import java.util.ArrayList;

public class Constant implements Serializable {

    private static final long serialVersionUID = 1L;

    //server url
    private static String SERVER_URL = BuildConfig.SERVER_URL;

    public static final String URL_HOME = SERVER_URL + "api.php?home";
    //latest wallpaper
    public static final String URL_LATEST = SERVER_URL + "api.php?latest&page=";
    public static final String URL_LATEST_GIF = SERVER_URL + "api.php?latest_gif&page=";
    public static final String URL_MOST_VIEWED = SERVER_URL + "api.php?get_wallpaper_most_viewed&page=";
    public static final String URL_MOST_VIEWED_GIF = SERVER_URL + "api.php?get_gif_wallpaper_most_viewed&page=";
    public static final String URL_MOST_RATED = SERVER_URL + "api.php?get_wallpaper_most_rated&page=";
    public static final String URL_MOST_RATED_GIF = SERVER_URL + "api.php?get_gif_wallpaper_most_rated&page=";
    public static final String URL_WALLPAPER_BY_CAT = SERVER_URL + "api.php?cat_id=";
    public static final String URL_WALLPAPER_DOWNLOAD = SERVER_URL + "api.php?wallpaper_download_id=";
    public static final String URL_GIF_DOWNLOAD = SERVER_URL + "api.php?gif_download_id=";
    public static final String URL_SEARCH_WALL = SERVER_URL + "api.php?search_text=";
    public static final String URL_SEARCH_GIF = SERVER_URL + "api.php?gif_search_text=";

    //category
    public static final String URL_CATEGORY = SERVER_URL + "api.php?cat_list";

    public static final String URL_RATING_1 = SERVER_URL + "api.php?wallpaper_rate&post_id=";
    public static final String URL_RATING_2 = "&device_id=";

    public static final String URL_RATING_GIF_1 = SERVER_URL + "api.php?gif_rate&post_id=";
    public static final String URL_RATING_GIF_2 = "&device_id=";
    public static final String URL_RATING_GIF_3 = "&rate=";

    public static final String URL_RATING_3 = "&rate=";

    public static final String URL_GET_WALL_RATING_1 = SERVER_URL + "api.php?get_wallpaper_rate&post_id=";
    public static final String URL_GET_WALL_RATING_2 = "&device_id=";

    public static final String URL_GET_GIF_RATING_1 = SERVER_URL + "api.php?get_gif_rate&post_id=";
    public static final String URL_GET_GIF_RATING_2 = "&device_id=";

    public static final String URL_ABOUT = SERVER_URL + "api.php";

    public static final String URL_WALLPAPER = SERVER_URL + "api.php?wallpaper_id=";
    public static final String URL_GIF = SERVER_URL + "api.php?gif_id=";
    public static final String URL_ABOUT_US_LOGO = SERVER_URL + "images/";

    public static final String TAG_ROOT = "HD_WALLPAPER";
    public static final String TAG_MSG = "MSG";
    public static final String TAG_LATEST_WALL = "latest_wallpaper";
    public static final String TAG_MOST_VIEWED_WALL = "most_viewed_wallpaper";
    public static final String TAG_MOST_RATED_WALL = "most_rated_wallpaper";

    public static final String TAG_CAT_ID = "cid";
    public static final String TAG_CAT_NAME = "category_name";
    public static final String TAG_CAT_IMAGE = "category_image";
    public static final String  TAG_TOTAL_WALL = "category_total_wall";

    public static final String TAG_WALL_ID = "id";
    public static final String TAG_WALL_IMAGE = "wallpaper_image";
    public static final String TAG_WALL_IMAGE_THUMB = "wallpaper_image_thumb";

    public static final String TAG_GIF_ID = "id";
    public static final String TAG_GIF_IMAGE = "gif_image";
    public static final String TAG_GIF_TAGS = "gif_tags";
    public static final String TAG_GIF_VIEWS = "total_views";
    public static final String TAG_GIF_TOTAL_RATE = "total_rate";
    public static final String TAG_GIF_AVG_RATE = "rate_avg";

    public static final String TAG_WALL_VIEWS = "total_views";
    public static final String TAG_WALL_AVG_RATE = "rate_avg";
    public static final String TAG_WALL_TOTAL_RATE = "total_rate";
    public static final String TAG_WALL_DOWNLOADS = "total_download";
    public static final String TAG_WALL_TAGS = "wall_tags";

    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 2;

    // Gridview image padding
    public static final int GRID_PADDING = 3; // in dp

    public static ArrayList<ItemWallpaper> arrayList = new ArrayList<>();
    public static ArrayList<ItemGIF> arrayListGIF = new ArrayList<>();
    public static ItemAbout itemAbout;
    public static int columnWidth = 0;
    public static int columnHeight = 0;

    public static Boolean isFav = false;
    public static String search_item = "";

    public static Boolean isBannerAd = true, isInterAd = true;
    public static String ad_publisher_id = "";
    public static String url_privacy_policy = "";
    public static String ad_banner_id = "";
    public static String ad_inter_id = "";

    public static int adShow = 5;
    public static int adCount = 0;
}
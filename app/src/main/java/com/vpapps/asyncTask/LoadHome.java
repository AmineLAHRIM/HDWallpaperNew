package com.vpapps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.vpapps.interfaces.HomeListener;
import com.vpapps.items.ItemWallpaper;
import com.vpapps.utils.Constant;
import com.vpapps.utils.DBHelper;
import com.vpapps.utils.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadHome extends AsyncTask<String,String,Boolean> {

    private HomeListener homeListener;
    private DBHelper dbHelper;
    private ArrayList<ItemWallpaper> arrayList_latest, arrayList_mostview, arrayList_rated;

    public LoadHome(Context context, HomeListener homeListener) {
        dbHelper = new DBHelper(context);
        this.homeListener = homeListener;
        arrayList_latest = new ArrayList<>();
        arrayList_rated = new ArrayList<>();
        arrayList_mostview = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        dbHelper.removeAllWallpaper("latest");
        homeListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = Methods.getJSONString(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONObject jsonObj = jOb.getJSONObject(Constant.TAG_ROOT);

            JSONArray jsonArray_latest = jsonObj.getJSONArray(Constant.TAG_LATEST_WALL);
            for (int i = 0; i < jsonArray_latest.length(); i++) {
                JSONObject objJson = jsonArray_latest.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_WALL_ID);
                String cid = objJson.getString(Constant.TAG_CAT_ID);
                String cat_name = objJson.getString(Constant.TAG_CAT_NAME);
                String img = objJson.getString(Constant.TAG_WALL_IMAGE).replace(" ","%20");
                String img_thumb = objJson.getString(Constant.TAG_WALL_IMAGE_THUMB).replace(" ","%20");
                String totalviews = objJson.getString(Constant.TAG_WALL_VIEWS);
                String totalrate = objJson.getString(Constant.TAG_WALL_TOTAL_RATE);
                String averagerate = objJson.getString(Constant.TAG_WALL_AVG_RATE);
                String tags = objJson.getString(Constant.TAG_WALL_TAGS);

                ItemWallpaper itemWallpaper = new ItemWallpaper(id, cid, cat_name, img, img_thumb, totalviews, totalrate, averagerate,"", tags);
                arrayList_latest.add(itemWallpaper);
                dbHelper.addWallpaper(itemWallpaper, "latest");
            }

            JSONArray jsonArray_mostviewed = jsonObj.getJSONArray(Constant.TAG_MOST_VIEWED_WALL);
            for (int i = 0; i < jsonArray_mostviewed.length(); i++) {
                JSONObject objJson = jsonArray_mostviewed.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_WALL_ID);
                String cid = objJson.getString(Constant.TAG_CAT_ID);
                String cat_name = objJson.getString(Constant.TAG_CAT_NAME);
                String img = objJson.getString(Constant.TAG_WALL_IMAGE).replace(" ","%20");
                String img_thumb = objJson.getString(Constant.TAG_WALL_IMAGE_THUMB).replace(" ","%20");
                String totalviews = objJson.getString(Constant.TAG_WALL_VIEWS);
                String totalrate = objJson.getString(Constant.TAG_WALL_TOTAL_RATE);
                String averagerate = objJson.getString(Constant.TAG_WALL_AVG_RATE);
                String tags = objJson.getString(Constant.TAG_WALL_TAGS);

                ItemWallpaper itemWallpaper = new ItemWallpaper(id, cid, cat_name, img, img_thumb, totalviews, totalrate, averagerate,"", tags);
                arrayList_mostview.add(itemWallpaper);
            }

            JSONArray jsonArray_rated = jsonObj.getJSONArray(Constant.TAG_MOST_RATED_WALL);
            for (int i = 0; i < jsonArray_rated.length(); i++) {
                JSONObject objJson = jsonArray_latest.getJSONObject(i);

                String id = objJson.getString(Constant.TAG_WALL_ID);
                String cid = objJson.getString(Constant.TAG_CAT_ID);
                String cat_name = objJson.getString(Constant.TAG_CAT_NAME);
                String img = objJson.getString(Constant.TAG_WALL_IMAGE).replace(" ","%20");
                String img_thumb = objJson.getString(Constant.TAG_WALL_IMAGE_THUMB).replace(" ","%20");
                String totalviews = objJson.getString(Constant.TAG_WALL_VIEWS);
                String totalrate = objJson.getString(Constant.TAG_WALL_TOTAL_RATE);
                String averagerate = objJson.getString(Constant.TAG_WALL_AVG_RATE);
                String tags = objJson.getString(Constant.TAG_WALL_TAGS);

                ItemWallpaper itemWallpaper = new ItemWallpaper(id, cid, cat_name, img, img_thumb, totalviews, totalrate, averagerate,"", tags);
                arrayList_rated.add(itemWallpaper);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        homeListener.onEnd(String.valueOf(s), arrayList_latest,  arrayList_mostview, arrayList_rated);
        super.onPostExecute(s);
    }
}

package com.vpapps.asyncTask;

import android.os.AsyncTask;

import com.vpapps.interfaces.LatestWallListener;
import com.vpapps.items.ItemWallpaper;
import com.vpapps.utils.Constant;
import com.vpapps.utils.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadLatestWall extends AsyncTask<String,String,Boolean> {

    private LatestWallListener latestWallListener;
    private ArrayList<ItemWallpaper> arrayList;

    public LoadLatestWall(LatestWallListener latestWallListener) {
        this.latestWallListener = latestWallListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        latestWallListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = Methods.getJSONString(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

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
                arrayList.add(itemWallpaper);
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
        latestWallListener.onEnd(String.valueOf(s),arrayList);
        super.onPostExecute(s);
    }
}
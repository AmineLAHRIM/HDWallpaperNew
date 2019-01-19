package com.vpapps.asyncTask;

import android.os.AsyncTask;

import com.vpapps.interfaces.LatestGIFListener;
import com.vpapps.items.ItemGIF;
import com.vpapps.utils.Constant;
import com.vpapps.utils.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadLatestGIF extends AsyncTask<String, String, Boolean> {

    private LatestGIFListener latestGIFListener;
    private ArrayList<ItemGIF> arrayList;

    public LoadLatestGIF(LatestGIFListener latestGIFListener) {
        this.latestGIFListener = latestGIFListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        latestGIFListener.onStart();
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

                String id = objJson.getString(Constant.TAG_GIF_ID);
                String img = objJson.getString(Constant.TAG_GIF_IMAGE).replace(" ", "%20");
                String totalviews = objJson.getString(Constant.TAG_GIF_VIEWS);
                String totalRate = objJson.getString(Constant.TAG_GIF_TOTAL_RATE);
                String avj_rate = objJson.getString(Constant.TAG_GIF_AVG_RATE);
                String tags= objJson.getString(Constant.TAG_GIF_TAGS);

                ItemGIF itemGIF = new ItemGIF(id, img, totalviews, totalRate, avj_rate,"", tags);
                arrayList.add(itemGIF);
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
        latestGIFListener.onEnd(String.valueOf(s), arrayList);
        super.onPostExecute(s);
    }
}
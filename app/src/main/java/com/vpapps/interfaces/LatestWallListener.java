package com.vpapps.interfaces;

import com.vpapps.items.ItemWallpaper;

import java.util.ArrayList;

public interface LatestWallListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemWallpaper> arrayListCat);
}

package com.vpapps.interfaces;

import com.vpapps.items.ItemGIF;

import java.util.ArrayList;

public interface LatestGIFListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemGIF> arrayListCat);
}
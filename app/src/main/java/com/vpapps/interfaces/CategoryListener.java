package com.vpapps.interfaces;

import com.vpapps.items.ItemCat;

import java.util.ArrayList;

public interface CategoryListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemCat> arrayListCat);
}

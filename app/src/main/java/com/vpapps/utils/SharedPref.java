package com.vpapps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.vpapps.hdwallpaper.R;

public class SharedPref {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Boolean getIsNotification() {
        return sharedPreferences.getBoolean("noti", true);
    }

    public void setIsNotification(Boolean isNotification) {
        editor.putBoolean("noti", isNotification);
        editor.apply();
    }
}

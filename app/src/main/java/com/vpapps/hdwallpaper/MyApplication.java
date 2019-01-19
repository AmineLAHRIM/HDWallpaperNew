package com.vpapps.hdwallpaper;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vpapps.utils.DBHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.onesignal.OneSignal;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/poppins_med.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        FirebaseAnalytics.getInstance(getApplicationContext());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        OneSignal.setRequiresUserPrivacyConsent(true);
        OneSignal.startInit(this)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        Fresco.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

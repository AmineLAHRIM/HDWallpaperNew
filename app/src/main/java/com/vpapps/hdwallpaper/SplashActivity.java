package com.vpapps.hdwallpaper;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.vpapps.hdwallpaper.R;
import com.vpapps.utils.Constant;
import com.vpapps.utils.Methods;

public class SplashActivity extends AppCompatActivity {

    Boolean isCancelled = false;
    String cid = "0", cname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Methods methods;
        methods = new Methods(SplashActivity.this);

        if(getIntent().hasExtra("cid")) {
            cid = getIntent().getStringExtra("cid");
            cname = getIntent().getStringExtra("cname");
        }

        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constant.GRID_PADDING, r.getDisplayMetrics());
        Constant.columnWidth = (int) ((methods.getScreenWidth() - ((Constant.NUM_OF_COLUMNS + 1) * padding)) / Constant.NUM_OF_COLUMNS);
        Constant.columnHeight = (int) (Constant.columnWidth*1.44);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isCancelled) {
                    if(cid.equals("0")) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, WallpaperByCatActivity.class);
                        intent.putExtra("cid", cid);
                        intent.putExtra("cname", cname);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        isCancelled = true;
        super.onDestroy();
    }
}

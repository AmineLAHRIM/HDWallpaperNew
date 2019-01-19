package com.vpapps.hdwallpaper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vpapps.asyncTask.LoadAbout;
import com.vpapps.interfaces.AboutListener;
import com.vpapps.interfaces.InterAdListener;
import com.vpapps.utils.*;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Methods methods;
    DBHelper dbHelper;
    FragmentManager fm;
    LoadAbout loadAbout;
    LinearLayout ll_ad;
    Toolbar toolbar;
    AdConsent adConsent;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
    private NavigationView navigationView;
    private PrefManager prefManager;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);
        prefManager.setFirstTimeLaunch(false);

        dbHelper = new DBHelper(this);
        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                clickNav(position);
            }
        });
        methods.setStatusColor(getWindow());
        methods.forceRTLIfSupported(getWindow());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ll_ad = findViewById(R.id.ll_ad_main);

        fm = getSupportFragmentManager();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        changeHeaderFont();

        FragmentHome f1 = new FragmentHome();
        loadFrag(f1, getResources().getString(R.string.home), fm);
        getSupportActionBar().setTitle(getResources().getString(R.string.home));

        adConsent = new AdConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate() {
                methods.showBannerAd(ll_ad);
            }
        });

        if (methods.isNetworkAvailable()) {
            loadAbout = new LoadAbout(new AboutListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(Boolean success) {
                    adConsent.checkForConsent();
                    dbHelper.addtoAbout();
                }
            });
            loadAbout.execute();
        } else {
            adConsent.checkForConsent();
            if (!dbHelper.getAbout()) {
//                Toast.makeText(MainActivity.this, getResources().getString(R.string.first_load_internet), Toast.LENGTH_SHORT).show();
            }
        }

        checkPer();

    }

    private void changeHeaderFont() {
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/aclonica.ttf");
        TextView header_txt = navigationView.getHeaderView(0).findViewById(R.id.header_txt);
        header_txt.setTypeface(myTypeface);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        methods.showInter(item.getItemId(), "");
        return true;
    }

    private void clickNav(int item) {
        switch (item) {
            case R.id.nav_home:
                FragmentHome fhome = new FragmentHome();
                loadFrag(fhome, getResources().getString(R.string.home), fm);
                getSupportActionBar().setTitle(getResources().getString(R.string.home));
                break;
            case R.id.nav_Latest:
                Intent intent_cat = new Intent(MainActivity.this, WallpaperActivity.class);
                intent_cat.putExtra("pos", 0);
                startActivity(intent_cat);
                break;
            case R.id.nav_gif:
                Intent intent_gif = new Intent(MainActivity.this, GIFActivity.class);
                startActivity(intent_gif);
                break;
            case R.id.nav_category:
                FragmentCategories fcat = new FragmentCategories();
                loadFrag(fcat, getResources().getString(R.string.categories), fm);
                toolbar.setTitle(getResources().getString(R.string.categories));
                break;
            case R.id.nav_fav:
                Intent intent_fav = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(intent_fav);
                break;
            case R.id.nav_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_rate:
                final String appName = getPackageName();//your application package name i.e play store application url
                Log.e("package:", appName);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + appName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + appName)));
                }
                break;
            case R.id.nav_shareapp:
                Intent ishare = new Intent(Intent.ACTION_SEND);
                ishare.setType("text/plain");
                ishare.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(ishare);
                break;
            case R.id.nav_moreapp:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_more_apps))));
                break;
            case R.id.nav_priacy:
                openPrivacyDialog();
                break;
            case R.id.nav_setting:
                Intent intent_set = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_set);
                break;
        }
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.frame_layout, f1, name);
        ft.commit();
    }

    public void openPrivacyDialog() {
        Dialog dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(MainActivity.this);
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_privacy);

        WebView webview = dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
//		webview.loadUrl("file:///android_asset/privacy.html");
        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";

        if (Constant.itemAbout != null) {
            String text = "<html><head>"
                    + "<style> body{color: #000 !important;text-align:left}"
                    + "</style></head>"
                    + "<body>"
                    + Constant.itemAbout.getPrivacy()
                    + "</body></html>";

            webview.loadData(text, mimeType, encoding);
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void checkPer() {
        if ((ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }
    }
}

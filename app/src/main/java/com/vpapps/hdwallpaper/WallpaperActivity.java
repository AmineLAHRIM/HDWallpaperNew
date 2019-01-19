package com.vpapps.hdwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.vpapps.utils.Constant;
import com.vpapps.utils.Methods;
import com.vpapps.utils.ZoomOutSlideTransformer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WallpaperActivity extends AppCompatActivity {

    Methods methods;
    LinearLayout ll_ad;
    int viewpager_pos;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        methods = new Methods(this);
        methods.setStatusColor(getWindow());
        methods.forceRTLIfSupported(getWindow());

        viewpager_pos = getIntent().getIntExtra("pos", 0);

        ll_ad = findViewById(R.id.ll_ad_wall);
        methods.showBannerAd(ll_ad);

        Toolbar toolbar = findViewById(R.id.toolbar_wallpaper);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(viewpager_pos);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Constant.search_item = s;
            Intent intent = new Intent(WallpaperActivity.this, SearchWallActivity.class);
            startActivity(intent);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentLatest.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

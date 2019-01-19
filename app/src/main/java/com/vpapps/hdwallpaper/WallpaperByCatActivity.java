package com.vpapps.hdwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vpapps.adapter.AdapterWallpaper;
import com.vpapps.asyncTask.LoadLatestWall;
import com.vpapps.interfaces.InterAdListener;
import com.vpapps.interfaces.LatestWallListener;
import com.vpapps.interfaces.RecyclerViewClickListener;
import com.vpapps.items.ItemWallpaper;
import com.vpapps.utils.Constant;
import com.vpapps.utils.DBHelper;
import com.vpapps.utils.EndlessRecyclerViewScrollListener;
import com.vpapps.utils.Methods;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WallpaperByCatActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterWallpaper adapter;
    ArrayList<ItemWallpaper> arrayList;
    ProgressBar progressBar;
    Methods methods;
    InterAdListener interAdListener;
    Boolean isOver = false, isScroll = false;
    TextView textView_empty;
    LoadLatestWall loadWallpaper;
    int page = 1;
    GridLayoutManager grid;
    String cid, cname;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_by_cat);

        interAdListener = new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Intent intent = new Intent(WallpaperByCatActivity.this, WallPaperDetailsActivity.class);
                intent.putExtra("pos", position);
                Constant.arrayList.clear();
                Constant.arrayList.addAll(arrayList);
                startActivity(intent);
            }
        };

        dbHelper = new DBHelper(this);
        methods = new Methods(this, interAdListener);
        methods.setStatusColor(getWindow());
        methods.forceRTLIfSupported(getWindow());

        LinearLayout ll_ad = findViewById(R.id.ll_ad_search);
        dbHelper.getAbout();
        methods.showBannerAd(ll_ad);

        cid = getIntent().getStringExtra("cid");
        cname = getIntent().getStringExtra("cname");

        toolbar = this.findViewById(R.id.toolbar_wall_by_cat);
        toolbar.setTitle(cname);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.pb_wallcat);
        textView_empty = findViewById(R.id.tv_empty_wallcat);

        recyclerView = findViewById(R.id.rv_wall_by_cat);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(WallpaperByCatActivity.this, 2);
        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? grid.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(grid);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(grid) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if(!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            getWallpaperData();
                        }
                    }, 0);
                } else {
                    adapter.hideHeader();
                }
            }
        });

        getWallpaperData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
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
            Intent intent = new Intent(WallpaperByCatActivity.this, SearchWallActivity.class);
            startActivity(intent);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private void getWallpaperData() {
        if (methods.isNetworkAvailable()) {
            loadWallpaper = new LoadLatestWall(new LatestWallListener() {
                @Override
                public void onStart() {
                    if(arrayList.size() == 0) {
                        dbHelper.removeWallByCat("catlist", cid);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onEnd(String success, ArrayList<ItemWallpaper> arrayListWall) {
                    if(arrayListWall.size() == 0) {
                        isOver = true;
                        try {
                            adapter.hideHeader();
                        }catch (Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            setEmptTextView();
                            e.printStackTrace();
                        }
                    } else {
                        for(int i=0; i< arrayListWall.size() ; i++) {
                            dbHelper.addWallpaper(arrayListWall.get(i),"catlist");
                        }
                        page = page + 1;
                        arrayList.addAll(arrayListWall);
                        progressBar.setVisibility(View.INVISIBLE);
                        setAdapter();
                    }
                }
            });
            loadWallpaper.execute(Constant.URL_WALLPAPER_BY_CAT + cid + "&page=" + page);
        } else {
            arrayList = dbHelper.getWallByCat("catlist", cid);
            setAdapter();
            isOver = true;
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void setAdapter() {
        if(!isScroll) {
            adapter = new AdapterWallpaper(WallpaperByCatActivity.this, arrayList , new RecyclerViewClickListener(){
                @Override
                public void onClick(int position) {
                    methods.showInter(position,"");
                }
            });
            AnimationAdapter adapterAnim = new ScaleInAnimationAdapter(adapter);
            adapterAnim.setFirstOnly(true);
            adapterAnim.setDuration(500);
            adapterAnim.setInterpolator(new OvershootInterpolator(.5f));
            recyclerView.setAdapter(adapterAnim);
            setEmptTextView();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void setEmptTextView() {
        if (arrayList.size() == 0) {
            textView_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textView_empty.setVisibility(View.GONE);
        }
    }
}

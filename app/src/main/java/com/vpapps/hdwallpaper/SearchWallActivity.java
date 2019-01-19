package com.vpapps.hdwallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.vpapps.adapter.AdapterWallpaperFav;
import com.vpapps.asyncTask.LoadLatestWall;
import com.vpapps.hdwallpaper.R;
import com.vpapps.interfaces.InterAdListener;
import com.vpapps.interfaces.LatestWallListener;
import com.vpapps.interfaces.RecyclerViewClickListener;
import com.vpapps.items.ItemWallpaper;
import com.vpapps.utils.Constant;
import com.vpapps.utils.Methods;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchWallActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterWallpaperFav adapter;
    ArrayList<ItemWallpaper> arrayList;
    ProgressBar progressBar;
    Methods methods;
    InterAdListener interAdListener;
    Boolean isOver = false;
    TextView textView_empty;
    LoadLatestWall loadWallpaper;
    GridLayoutManager grid;

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
                Intent intent = new Intent(SearchWallActivity.this, WallPaperDetailsActivity.class);
                intent.putExtra("pos", position);
                Constant.arrayList.clear();
                Constant.arrayList.addAll(arrayList);
                startActivity(intent);
            }
        };

        methods = new Methods(this, interAdListener);
        methods.setStatusColor(getWindow());
        methods.forceRTLIfSupported(getWindow());

        toolbar = this.findViewById(R.id.toolbar_wall_by_cat);
        toolbar.setTitle(Constant.search_item);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll_ad = findViewById(R.id.ll_ad_search);
        methods.showBannerAd(ll_ad);

        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.pb_wallcat);
        textView_empty = findViewById(R.id.tv_empty_wallcat);

        recyclerView = findViewById(R.id.rv_wall_by_cat);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(SearchWallActivity.this, 2);
        recyclerView.setLayoutManager(grid);

        getWallpaperData();
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
            getWallpaperData();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

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

    private void getWallpaperData() {
        if (methods.isNetworkAvailable()) {
            loadWallpaper = new LoadLatestWall(new LatestWallListener() {
                @Override
                public void onStart() {
                    arrayList.clear();
                    recyclerView.setVisibility(View.GONE);
                    textView_empty.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd(String success, ArrayList<ItemWallpaper> arrayListWall) {
                    arrayList.addAll(arrayListWall);
                    progressBar.setVisibility(View.INVISIBLE);
                    setAdapter();
                }
            });
            loadWallpaper.execute(Constant.URL_SEARCH_WALL + Constant.search_item.replace(" ", "%20"));
        } else {
            setAdapter();
            isOver = true;
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void setAdapter() {
        adapter = new AdapterWallpaperFav(SearchWallActivity.this, arrayList, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInter(position,"");
            }
        });
        AnimationAdapter adapterAnim = new ScaleInAnimationAdapter(adapter);
        adapterAnim.setFirstOnly(true);
        adapterAnim.setDuration(500);
        adapterAnim.setInterpolator(new OvershootInterpolator(.9f));
        recyclerView.setAdapter(adapterAnim);
        setExmptTextView();

    }

    private void setExmptTextView() {
        if (arrayList.size() == 0) {
            textView_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textView_empty.setVisibility(View.GONE);
        }
    }
}

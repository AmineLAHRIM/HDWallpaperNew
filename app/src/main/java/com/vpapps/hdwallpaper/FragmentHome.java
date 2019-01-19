package com.vpapps.hdwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vpapps.adapter.AdapterImageHome;
import com.vpapps.asyncTask.LoadHome;
import com.vpapps.interfaces.HomeListener;
import com.vpapps.interfaces.InterAdListener;
import com.vpapps.interfaces.RecyclerViewClickListener;
import com.vpapps.items.ItemWallpaper;
import com.vpapps.utils.Constant;
import com.vpapps.utils.DBHelper;
import com.vpapps.utils.Methods;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

public class FragmentHome extends Fragment {

    DBHelper dbHelper;
    Methods methods;
    RecyclerView recyclerView_latest, recyclerView_mostview, recyclerView_rated;
    AdapterImageHome adapter_latest, adapter_mostview, adapter_rated;
    ArrayList<ItemWallpaper> arrayList_latest, arrayList_mostview, arrayList_rated;
    Button button_latest, button_view, button_rate;
    TextView textView_latest_empty, textView_mostview_empty, textView_rated_empty;
    LoadHome loadHome;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DBHelper(getActivity());
        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                switch (type) {
                    case "latest":
                        Intent intent_lat = new Intent(getActivity(), WallPaperDetailsActivity.class);
                        Constant.arrayList.clear();
                        Constant.arrayList.addAll(arrayList_latest);
                        intent_lat.putExtra("pos", position);
                        startActivity(intent_lat);
                        break;
                    case "viewed":
                        Intent intent_view = new Intent(getActivity(), WallPaperDetailsActivity.class);
                        Constant.arrayList.clear();
                        Constant.arrayList.addAll(arrayList_mostview);
                        intent_view.putExtra("pos", position);
                        startActivity(intent_view);
                        break;
                    case "rated":
                        Intent intent = new Intent(getActivity(), WallPaperDetailsActivity.class);
                        Constant.arrayList.clear();
                        Constant.arrayList.addAll(arrayList_rated);
                        intent.putExtra("pos", position);
                        startActivity(intent);
                        break;
                }
            }
        });
        arrayList_latest = new ArrayList<>();
        arrayList_mostview = new ArrayList<>();
        arrayList_rated = new ArrayList<>();

        button_latest = rootView.findViewById(R.id.button_latest_all);
        button_view = rootView.findViewById(R.id.button_mostview_all);
        button_rate = rootView.findViewById(R.id.button_rated_all);
        textView_latest_empty = rootView.findViewById(R.id.tv_empty_home_latest);
        textView_mostview_empty = rootView.findViewById(R.id.tv_empty_home_mostview);
        textView_rated_empty = rootView.findViewById(R.id.tv_empty_home_rated);

        recyclerView_latest = rootView.findViewById(R.id.rv_home_latest);
        recyclerView_latest.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_latest.setLayoutManager(llm);

        recyclerView_mostview = rootView.findViewById(R.id.rv_home_mostview);
        recyclerView_mostview.setHasFixedSize(true);
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_mostview.setLayoutManager(llm2);

        recyclerView_rated = rootView.findViewById(R.id.rv_home_rated);
        recyclerView_rated.setHasFixedSize(true);
        LinearLayoutManager llm3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_rated.setLayoutManager(llm3);

        linearLayout = rootView.findViewById(R.id.ll_main_home);
        progressBar = rootView.findViewById(R.id.pb_home);

        Constant.isFav = false;

        button_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_cat = new Intent(getActivity(), WallpaperActivity.class);
                intent_cat.putExtra("pos", 0);
                startActivity(intent_cat);
            }
        });

        button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_cat = new Intent(getActivity(), WallpaperActivity.class);
                intent_cat.putExtra("pos", 1);
                startActivity(intent_cat);
            }
        });

        button_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_cat = new Intent(getActivity(), WallpaperActivity.class);
                intent_cat.putExtra("pos", 2);
                startActivity(intent_cat);
            }
        });

        getWallpapers();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);

        super.onCreateOptionsMenu(menu, inflater);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Constant.search_item = s;
            Intent intent = new Intent(getActivity(), SearchWallActivity.class);
            startActivity(intent);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private void getWallpapers() {
        if (methods.isNetworkAvailable()) {
            loadHome = new LoadHome(getActivity(), new HomeListener() {
                @Override
                public void onStart() {
                    linearLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd(String success, ArrayList<ItemWallpaper> arrayListLatest, ArrayList<ItemWallpaper> arrayListMostView, ArrayList<ItemWallpaper> arrayListMostRated) {
                    if (getActivity() != null) {
                        arrayList_latest.addAll(arrayListLatest);
                        arrayList_mostview.addAll(arrayListMostView);
                        arrayList_rated.addAll(arrayListMostRated);
                        setAdapterToListview();
                    }
                }
            });
            loadHome.execute(Constant.URL_HOME);
        } else {
            arrayList_latest = dbHelper.getWallpapers("latest", "");
            arrayList_mostview = dbHelper.getWallpapers("latest", "views");
            arrayList_rated = dbHelper.getWallpapers("latest", "rate");
            setAdapterToListview();
        }
    }

    public void setAdapterToListview() {
        adapter_latest = new AdapterImageHome(getActivity(), arrayList_latest, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInter(position, "latest");
            }
        });
        AnimationAdapter adapterAnim = new SlideInRightAnimationAdapter(adapter_latest);
        adapterAnim.setFirstOnly(true);
        adapterAnim.setDuration(500);
        adapterAnim.setInterpolator(new OvershootInterpolator(.9f));
        recyclerView_latest.setAdapter(adapterAnim);

        adapter_mostview = new AdapterImageHome(getActivity(), arrayList_mostview, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInter(position, "viewed");
            }
        });
        AnimationAdapter adapterAnim2 = new SlideInRightAnimationAdapter(adapter_mostview);
        adapterAnim2.setFirstOnly(true);
        adapterAnim2.setDuration(500);
        adapterAnim2.setInterpolator(new OvershootInterpolator(.9f));
        recyclerView_mostview.setAdapter(adapterAnim2);

        adapter_rated = new AdapterImageHome(getActivity(), arrayList_rated, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInter(position, "rated");
            }
        });
        AnimationAdapter adapterAnim3 = new SlideInRightAnimationAdapter(adapter_rated);
        adapterAnim3.setFirstOnly(true);
        adapterAnim3.setDuration(500);
        adapterAnim3.setInterpolator(new OvershootInterpolator(.9f));
        recyclerView_rated.setAdapter(adapterAnim3);

        setExmptTextView();
    }

    private void setExmptTextView() {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        if (arrayList_latest.size() == 0) {
            textView_latest_empty.setVisibility(View.VISIBLE);
            recyclerView_latest.setVisibility(View.GONE);
        } else {
            recyclerView_latest.setVisibility(View.VISIBLE);
            textView_latest_empty.setVisibility(View.GONE);
        }

        if (arrayList_mostview.size() == 0) {
            textView_mostview_empty.setVisibility(View.VISIBLE);
            recyclerView_mostview.setVisibility(View.GONE);
        } else {
            recyclerView_mostview.setVisibility(View.VISIBLE);
            textView_mostview_empty.setVisibility(View.GONE);
        }

        if (arrayList_rated.size() == 0) {
            textView_rated_empty.setVisibility(View.VISIBLE);
            recyclerView_rated.setVisibility(View.GONE);
        } else {
            recyclerView_rated.setVisibility(View.VISIBLE);
            textView_rated_empty.setVisibility(View.GONE);
        }
    }
}
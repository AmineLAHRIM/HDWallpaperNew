package com.vpapps.hdwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
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

public class FragmentLatest extends Fragment {

    DBHelper dbHelper;
    Methods methods;
    RecyclerView recyclerView;
    AdapterWallpaper adapter;
    ArrayList<ItemWallpaper> arrayList;
    ProgressBar progressBar;
    TextView textView_empty;
    LoadLatestWall loadLatestWall;
    GridLayoutManager grid;
    InterAdListener interAdListener;
    AnimationAdapter adapterAnim;
    int pos, page = 1;
    Boolean isOver = false, isScroll = false, isLoaded = false, isVisible = false;

    public static FragmentLatest newInstance(int position) {
        FragmentLatest fragment = new FragmentLatest();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wallpaper, container, false);

        if (getArguments() != null) {
            pos = getArguments().getInt("pos");
        }

        interAdListener = new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Intent intent = new Intent(getActivity(), WallPaperDetailsActivity.class);
                intent.putExtra("pos", position);
                Constant.arrayList.clear();
                Constant.arrayList.addAll(arrayList);
                startActivity(intent);
            }
        };

        dbHelper = new DBHelper(getActivity());
        methods = new Methods(getActivity(), interAdListener);
        arrayList = new ArrayList<>();

        progressBar = rootView.findViewById(R.id.pb_wall);
        textView_empty = rootView.findViewById(R.id.tv_empty_wall);

        recyclerView = rootView.findViewById(R.id.rv_wall);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(getActivity(), 2);
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
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            getLatestData();
                        }
                    }, 0);
                } else {
                    adapter.hideHeader();
                }
            }
        });

        if (isVisible && !isLoaded) {
            getLatestData();
            isLoaded = true;
        }

        return rootView;
    }

    private void getLatestData() {
        if (methods.isNetworkAvailable()) {
            loadLatestWall = new LoadLatestWall(new LatestWallListener() {
                @Override
                public void onStart() {
                    if (arrayList.size() == 0) {
                        if (pos == 0) {
                            dbHelper.removeAllWallpaper("latest");
                        }
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onEnd(String success, ArrayList<ItemWallpaper> arrayListWall) {
//                    dbHelper.addtoFavorite(objItem, "latest");
                    if (getActivity() != null) {
                        if (arrayListWall.size() == 0) {
                            isOver = true;
                            try {
                                adapter.hideHeader();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
//                            if (pos == 0) {
                            for (int i = 0; i < arrayListWall.size(); i++) {
                                dbHelper.addWallpaper(arrayListWall.get(i), "latest");
                            }
//                            }
                            page = page + 1;
                            arrayList.addAll(arrayListWall);
                            progressBar.setVisibility(View.INVISIBLE);
                            setAdapter();
                        }
                    }
                }
            });
            if (pos == 0) {
                loadLatestWall.execute(Constant.URL_LATEST + page);
            } else if (pos == 1) {
                loadLatestWall.execute(Constant.URL_MOST_VIEWED + page);
            } else if (pos == 2) {
                loadLatestWall.execute(Constant.URL_MOST_RATED + page);
            }
        } else {
            if (pos == 0) {
                arrayList = dbHelper.getWallpapers("latest", "");
            } else if (pos == 1) {
                arrayList = dbHelper.getWallpapers("latest", "views");
            } else if (pos == 2) {
                arrayList = dbHelper.getWallpapers("latest", "rate");
            }
            setAdapter();
            isOver = true;
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void setAdapter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isScroll) {
                    adapter = new AdapterWallpaper(getActivity(), arrayList, new RecyclerViewClickListener() {
                        @Override
                        public void onClick(int position) {
                            methods.showInter(position,"");
                        }
                    });

                    adapterAnim = new ScaleInAnimationAdapter(adapter);
                    adapterAnim.setFirstOnly(true);
                    adapterAnim.setDuration(500);
                    adapterAnim.setInterpolator(new OvershootInterpolator(.9f));
                    recyclerView.setAdapter(adapterAnim);
                    setExmptTextView();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        }, 0);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;
        if (isVisibleToUser && isAdded() && !isLoaded) {
            getLatestData();
            isLoaded = true;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}

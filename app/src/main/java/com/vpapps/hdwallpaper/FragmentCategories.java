package com.vpapps.hdwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vpapps.adapter.AdapterCategories;
import com.vpapps.asyncTask.LoadCat;
import com.vpapps.hdwallpaper.R;
import com.vpapps.interfaces.CategoryListener;
import com.vpapps.interfaces.InterAdListener;
import com.vpapps.items.ItemCat;
import com.vpapps.utils.Constant;
import com.vpapps.utils.DBHelper;
import com.vpapps.utils.Methods;
import com.vpapps.utils.RecyclerItemClickListener;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class FragmentCategories extends Fragment {

    DBHelper dbHelper;
    Methods methods;
    RecyclerView recyclerView;
    AdapterCategories adapterCategories;
    ArrayList<ItemCat> arrayList;
    ProgressBar progressBar;
    TextView textView_empty;
    LoadCat loadCat;
    InterAdListener interAdListener;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        interAdListener = new InterAdListener() {
            @Override
            public void onClick(int pos, String type) {
                int position = getPosition(adapterCategories.getID(pos));
                Intent intent = new Intent(getActivity(), WallpaperByCatActivity.class);
                intent.putExtra("cid", arrayList.get(position).getId());
                intent.putExtra("cname", arrayList.get(position).getName());
                startActivity(intent);
            }
        };

        dbHelper = new DBHelper(getActivity());
        methods = new Methods(getActivity(), interAdListener);

        arrayList = new ArrayList<>();

        progressBar = rootView.findViewById(R.id.pb_cat);
        textView_empty = rootView.findViewById(R.id.tv_empty_cat);
        recyclerView = rootView.findViewById(R.id.rv_cat);
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(grid);

        if (methods.isNetworkAvailable()) {
            loadCat = new LoadCat(getActivity(), new CategoryListener() {
                @Override
                public void onStart() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd(String success, ArrayList<ItemCat> arrayListCat) {
                    if(getActivity() != null) {
                        arrayList.addAll(arrayListCat);
                        progressBar.setVisibility(View.INVISIBLE);

                        setAdapter();
                    }
                }
            });
            loadCat.execute(Constant.URL_CATEGORY);
        } else {
			arrayList = dbHelper.getCat();
			if(arrayList!=null) {
                setAdapter();
			}
            progressBar.setVisibility(View.GONE);
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                methods.showInter(position, "");
            }
        }));

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);

        super.onCreateOptionsMenu(menu, inflater);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {

            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (!searchView.isIconified()) {
                adapterCategories.getFilter().filter(s);
                adapterCategories.notifyDataSetChanged();
            }
            return false;
        }
    };

    public void setAdapter() {
        adapterCategories = new AdapterCategories(arrayList);
        AnimationAdapter adapterAnim = new ScaleInAnimationAdapter(adapterCategories);
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

    private int getPosition(String id) {
        int count=0;
        for(int i=0;i<arrayList.size();i++) {
            if(id.equals(arrayList.get(i).getId())) {
                count = i;
                break;
            }
        }
        return count;
    }
}

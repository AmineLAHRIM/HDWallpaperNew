package com.vpapps.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vpapps.hdwallpaper.R;
import com.vpapps.interfaces.RecyclerViewClickListener;
import com.vpapps.items.ItemWallpaper;
import com.vpapps.utils.Constant;
import com.vpapps.utils.DBHelper;
import com.vpapps.utils.Methods;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterWallpaperFav extends RecyclerView.Adapter {

    private ArrayList<ItemWallpaper> arrayList;
    private Context context;
    private RecyclerViewClickListener recyclerViewClickListener;
    private DBHelper dbHelper;
    private Methods methods;

    private final int VIEW_ITEM = 1;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rootlayout;
        private LikeButton likeButton;
        private RoundedImageView imageView;
        //        private ImageView imageView_fav;
        private TextView textView_cat;
        private View vieww;

        private MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_wall);
//            imageView_fav = view.findViewById(R.id.iv_wall_fav);
            textView_cat = view.findViewById(R.id.tv_wall_cat);
            vieww = view.findViewById(R.id.view_wall);
            rootlayout = view.findViewById(R.id.rootlayout);
            likeButton = view.findViewById(R.id.button_wall_fav);
        }
    }

    public AdapterWallpaperFav(Context context, ArrayList<ItemWallpaper> arrayList, RecyclerViewClickListener recyclerViewClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        dbHelper = new DBHelper(context);
        methods = new Methods(context);
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_wall, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).likeButton.setLiked(dbHelper.isFav(arrayList.get(position).getId()));
//        if (dbHelper.isFav(arrayList.get(position).getId())) {
//            ((MyViewHolder) holder).imageView_fav.setImageResource(R.mipmap.fav_hover);
//        } else {
//            ((MyViewHolder) holder).imageView_fav.setImageResource(R.mipmap.fav);
//        }
        ((MyViewHolder) holder).textView_cat.setText(arrayList.get(position).getCName());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Constant.columnWidth, (int)(Constant.columnHeight*0.3));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((MyViewHolder) holder).vieww.setLayoutParams(params);
        ((MyViewHolder) holder).imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ((MyViewHolder) holder).imageView.setLayoutParams(new RelativeLayout.LayoutParams(Constant.columnWidth, Constant.columnHeight));

        Picasso.get()
                .load(arrayList.get(position).getImageThumb())
                .placeholder(R.drawable.placeholder_wall)
                .into(((MyViewHolder) holder).imageView);

        ((MyViewHolder) holder).likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbHelper.addtoFavorite(arrayList.get(holder.getAdapterPosition()));
                methods.showSnackBar(((MyViewHolder) holder).rootlayout, context.getString(R.string.added_to_fav));
//                Toast.makeText(context, context.getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbHelper.removeFav(arrayList.get(holder.getAdapterPosition()).getId());
                methods.showSnackBar(((MyViewHolder) holder).rootlayout, context.getString(R.string.removed_from_fav));
//                Toast.makeText(context, context.getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show();
            }
        });

//        ((MyViewHolder) holder).imageView_fav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!dbHelper.isFav(arrayList.get(holder.getAdapterPosition()).getId())) {
//                    dbHelper.addtoFavorite(arrayList.get(holder.getAdapterPosition()));
//                    Toast.makeText(context, context.getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show();
//                    ((MyViewHolder) holder).imageView_fav.setImageResource(R.mipmap.fav_hover);
//                } else {
//                    dbHelper.removeFav(arrayList.get(holder.getAdapterPosition()).getId());
//                    Toast.makeText(context, context.getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show();
//                    ((MyViewHolder) holder).imageView_fav.setImageResource(R.mipmap.fav);
//                }
//            }
//        });

        ((MyViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_ITEM;
    }
}
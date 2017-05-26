package com.shmily.tjz.swap.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.shmily.tjz.swap.ShowPhotoResultActivity;
import com.shmily.tjz.swap.Utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/21.
 */

public class PhotoShowAdapter extends RecyclerView.Adapter<PhotoShowAdapter.ViewHolder> {

    private static final String TAG = "RecommendAdapter";

    private Context mContext;

    private List<String> mShoesList=new ArrayList<>();


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ShoesImage;
        ImageView delete;

        public ViewHolder(View view) {
            super(view);
            ShoesImage = (ImageView) view.findViewById(R.id.activity_item_ivImage);
            delete = (ImageView) view.findViewById(R.id.activity_item_ivDelete);

        }
    }

    public PhotoShowAdapter(List<String> shoesList) {
        mShoesList = shoesList;
    }
    @Override
    public void onBindViewHolder(PhotoShowAdapter.ViewHolder holder, int position) {
        String photo = mShoesList.get(position);
        Glide.with(mContext).load(photo)
                .into(holder.ShoesImage);

    }
    @Override
    public PhotoShowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.show_photo_item, parent, false);


        final PhotoShowAdapter.ViewHolder holder = new PhotoShowAdapter.ViewHolder(view);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                 mShoesList.remove(position);
                notifyDataSetChanged();

            }
        });
        holder.ShoesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                Intent intent=new Intent(MyApplication.getContext(), ShowPhotoResultActivity.class);
                intent.putExtra("url",mShoesList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);

            }
        });
        return holder;
    }



    @Override
    public int getItemCount() {
        return mShoesList.size();
    }

}

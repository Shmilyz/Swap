package com.shmily.tjz.swap.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shmily.tjz.swap.Db.ShoesSpecial;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.shmily.tjz.swap.ShowPhotoResultActivity;
import com.shmily.tjz.swap.Utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/6/5.
 */

public class ShoesShowAdapter extends RecyclerView.Adapter<ShoesShowAdapter.ViewHolder>{


    private Context mContext;

    private List<ShoesSpecial> mShoesList=new ArrayList<>();


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView show_image;

        public ViewHolder(View view) {
            super(view);
            show_image = (ImageView) view.findViewById(R.id.show_image);
        }
    }

    public ShoesShowAdapter(List<ShoesSpecial> shoesList) {
        mShoesList = shoesList;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShoesSpecial shoes = mShoesList.get(position);
        Glide.with(mContext).load(shoes.getImageUrl()).into(holder.show_image);
//. skipMemoryCache( true ).diskCacheStrategy(DiskCacheStrategy.NONE)

        /*
        *  Glide.with(GlideActivity.this)
                        .load("http://www.qqzhi.com/uploadpic/2014-06-05/231505771.jpg")
                        .asGif()
                        //获取后存储，断网后仍能获取
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        //加载失败返回小机器人
                        .error(R.mipmap.ic_launcher)
                        .into(img);


        * */
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.shoes_show_item, parent, false);


        final ViewHolder holder = new ViewHolder(view);
        holder.show_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                Intent intent=new Intent(MyApplication.getContext(), ShowPhotoResultActivity.class);
                intent.putExtra("url",mShoesList.get(position).getImageUrl());
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
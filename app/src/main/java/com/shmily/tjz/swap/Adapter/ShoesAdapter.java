package com.shmily.tjz.swap.Adapter;

/**
 * Created by Shmily_Z on 2017/4/2.
 */

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
import com.shmily.tjz.swap.Db.ShoesDb;
import com.shmily.tjz.swap.FruitActivity;
import com.shmily.tjz.swap.R;

import java.util.List;

public class ShoesAdapter extends RecyclerView.Adapter<ShoesAdapter.ViewHolder>{

    private static final String TAG = "ShoesAdapter";

    private Context mContext;

    private List<ShoesDb> mShoesDbList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }
    }

    public ShoesAdapter(List<ShoesDb> shoesDbList) {
        mShoesDbList = shoesDbList;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShoesDb shoesDb = mShoesDbList.get(position);
        holder.fruitName.setText(shoesDb.getName());
        Glide.with(mContext).load(shoesDb.getImageId()).into(holder.fruitImage);

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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
         View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item, parent, false);


        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ShoesDb shoesDb = mShoesDbList.get(position);
                Intent intent = new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME, shoesDb.getName());
               intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, shoesDb.getImageId());
                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());

            }
        });
        return holder;
    }



    @Override
    public int getItemCount() {
        return mShoesDbList.size();
    }

}
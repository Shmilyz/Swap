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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/16.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private static final String TAG = "RecommendAdapter";

    private Context mContext;

    private List<Shoes> mShoesList=new ArrayList<>();


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ShoesImage;

        public ViewHolder(View view) {
            super(view);
            ShoesImage = (ImageView) view.findViewById(R.id.recommend_card_image);
        }
    }

    public RecommendAdapter(List<Shoes> shoesList) {
        mShoesList = shoesList;
    }
    @Override
    public void onBindViewHolder(RecommendAdapter.ViewHolder holder, int position) {
        Shoes shoes = mShoesList.get(position);
        Glide.with(mContext).load(shoes.getPicture())
        .into(holder.ShoesImage);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.recommend_card_item, parent, false);


        final RecommendAdapter.ViewHolder holder = new RecommendAdapter.ViewHolder(view);
        holder.ShoesImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Shoes shoes = mShoesList.get(position);
                Intent intent = new Intent(mContext, ShoesActivity.class);
                intent.putExtra(ShoesActivity.SHOES_ID, String.valueOf(shoes.getId()));
                intent.putExtra(ShoesActivity.SHOES_IMAGE_URL, shoes.getPicture());
                intent.putExtra(ShoesActivity.SHOES_BIAOTI,shoes.getBiaoti());
                intent.putExtra(ShoesActivity.SHOES_BRAND,shoes.getBrand());
                intent.putExtra(ShoesActivity.SHOES_STYLE,shoes.getStyle());
                intent.putExtra(ShoesActivity.SHOES_USERNAME,shoes.getUsername());
                intent.putExtra(ShoesActivity.SHOES_POSITION,shoes.getPosition());
                intent.putExtra(ShoesActivity.SHOES_SIZE,shoes.getSize());
                intent.putExtra(ShoesActivity.SHOES_DATE,shoes.getDate());
                intent.putExtra(ShoesActivity.SHOES_MIAOSHU,shoes.getMiaoshu());
                intent.putExtra(ShoesActivity.SHOES_PRICE,String.valueOf(shoes.getPrice()));

                intent.putExtra(ShoesActivity.SHOES_PICTUREAMOUNT,String.valueOf(shoes.getPictureamount()));
                intent.putExtra(ShoesActivity.SHOES_FILE,shoes.getFile());
                intent.putExtra(ShoesActivity.SHOES_PICTURENAME,shoes.getPicturename());



                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                ((Activity) mContext).finish();
            }
        });
        return holder;
    }



    @Override
    public int getItemCount() {
        return mShoesList.size();
    }

}

package com.shmily.tjz.swap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shmily.tjz.swap.Gson.AlreadBuy;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.shmily.tjz.swap.ShowBuyInfoActivity;
import com.shmily.tjz.swap.Utils.Xutils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/6/26.
 */

public class AlreadBuyAdapter extends SwipeMenuAdapter<AlreadBuyAdapter.ViewHolder> {


    private Context mContext;

    private List<AlreadBuy> mShoesList = new ArrayList<>();
    String username;
    private String loveurl = "http://www.shmilyz.com/ForAndroidHttp/love.action";
    private Xutils xutils;

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.collect_item, parent, false);

        return view;
    }

    @Override
    public AlreadBuyAdapter.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {


        return new AlreadBuyAdapter.ViewHolder(realContentView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView collect_image;
        TextView collect_shoesname,coolect_coast;
        RelativeLayout collect_item;
        public ViewHolder(View view) {

            super(view);
            collect_image= (ImageView) view.findViewById(R.id.collect_image);
            collect_shoesname= (TextView) view.findViewById(R.id.collect_shoesname);
            coolect_coast= (TextView) view.findViewById(R.id.coolect_coast);
            collect_item= (RelativeLayout) view.findViewById(R.id.collect_item);

        }
    }

    public AlreadBuyAdapter(List<AlreadBuy> shoesList) {
        mShoesList = shoesList;
    }


    @Override
    public void onBindViewHolder(final AlreadBuyAdapter.ViewHolder holder, int position) {

        final AlreadBuy number = mShoesList.get(position);
        holder.collect_shoesname.setText(number.getBiaoti());
        holder.coolect_coast.setText("¥"+String.valueOf(number.getPrice()));
        Glide.with(mContext).load(number.getPicture()).into(holder.collect_image);
        holder.collect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                AlreadBuy shoes = mShoesList.get(position);
                Intent intent = new Intent(mContext, ShowBuyInfoActivity.class);

                intent.putExtra(ShoesActivity.SHOES_ID, String.valueOf(shoes.getId()));
                intent.putExtra(ShoesActivity.SHOES_IMAGE_URL, shoes.getPicture());
                intent.putExtra(ShoesActivity.SHOES_BIAOTI,shoes.getBiaoti());
                intent.putExtra(ShoesActivity.SHOES_USERNAME,shoes.getUsername());
                intent.putExtra(ShoesActivity.SHOES_SIZE,shoes.getSize());
                intent.putExtra("buy_position",shoes.getBuyposition());
                intent.putExtra("buy_say",shoes.getSay());
                intent.putExtra("buy_buyid",String.valueOf(shoes.getBuyid()));
                intent.putExtra("buy_date",shoes.getDate().replace("T"," "));

                intent.putExtra(ShoesActivity.SHOES_PRICE,String.valueOf(shoes.getPrice()));

                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mShoesList.size();
    }

}

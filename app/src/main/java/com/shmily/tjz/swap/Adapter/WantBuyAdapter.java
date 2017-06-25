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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shmily.tjz.swap.BuyActivity;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.shmily.tjz.swap.Utils.Xutils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shmily_Z on 2017/6/26.
 */

public class WantBuyAdapter extends SwipeMenuAdapter<WantBuyAdapter.ViewHolder> {


    private Context mContext;

    private List<Shoes> mShoesList = new ArrayList<>();
    String username;
    private String loveurl = "http://www.shmilyz.com/ForAndroidHttp/love.action";
    private Xutils xutils;

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.gobuy_item, parent, false);

        return view;
    }

    @Override
    public WantBuyAdapter.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {


        return new WantBuyAdapter.ViewHolder(realContentView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView buy_info_shoesname,buy_info_shoesprice,buy_info_togo;
        ImageView buy_info_shoesimage;

        public ViewHolder(View view) {

            super(view);
            buy_info_shoesname= (TextView) view.findViewById(R.id.buy_info_shoesname);
            buy_info_shoesprice= (TextView) view.findViewById(R.id.buy_info_shoesprice);
            buy_info_togo= (TextView) view.findViewById(R.id.buy_info_togo);
            buy_info_shoesimage= (ImageView) view.findViewById(R.id.buy_info_shoesimage);
        }
    }

    public WantBuyAdapter(List<Shoes> shoesList) {
        mShoesList = shoesList;
    }


    @Override
    public void onBindViewHolder(final WantBuyAdapter.ViewHolder holder, int position) {

        final Shoes number = mShoesList.get(position);
        Glide.with(mContext)
                .load( number.getPicture())
                .into(holder.buy_info_shoesimage);
        String url="http://www.shmilyz.com/headimage/"+number.getUsername()+".jpg";

        holder.buy_info_shoesname.setText(number.getUsername());
        holder.buy_info_shoesprice.setText("¥"+number.getPrice());
        holder.buy_info_togo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent =new Intent(mContext,BuyActivity.class);
                intent.putExtra(ShoesActivity.SHOES_ID,number.getId());
                intent.putExtra(ShoesActivity.SHOES_IMAGE_URL,number.getPicture());
                intent.putExtra(ShoesActivity.SHOES_BIAOTI,number.getMiaoshu());
                intent.putExtra(ShoesActivity.SHOES_USERNAME,number.getUsername());
                intent.putExtra(ShoesActivity.SHOES_SIZE,number.getSize());
                intent.putExtra(ShoesActivity.SHOES_PRICE,String.valueOf(number.getPrice()));

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mShoesList.size();
    }

}

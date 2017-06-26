package com.shmily.tjz.swap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.shmily.tjz.swap.Utils.Xutils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shmily_Z on 2017/6/26.
 */

public class ReleaseShowAdapter extends SwipeMenuAdapter<ReleaseShowAdapter.ViewHolder> {



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
        View view = LayoutInflater.from(mContext).inflate(R.layout.release_show_item, parent, false);
        xutils=Xutils.getInstance();
        return view;
    }

    @Override
    public ReleaseShowAdapter.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {


        return new ReleaseShowAdapter.ViewHolder(realContentView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView collect_image;
        TextView collect_shoesname,coolect_coast,release_show_cs;
        RelativeLayout collect_item;
        public ViewHolder(View view) {

            super(view);
            collect_image= (ImageView) view.findViewById(R.id.collect_image);
            collect_shoesname= (TextView) view.findViewById(R.id.collect_shoesname);
            coolect_coast= (TextView) view.findViewById(R.id.coolect_coast);
            collect_item= (RelativeLayout) view.findViewById(R.id.collect_item);
            release_show_cs= (TextView) view.findViewById(R.id.release_show_cs);
        }
    }

    public ReleaseShowAdapter(List<Shoes> shoesList) {
        mShoesList = shoesList;
    }


    @Override
    public void onBindViewHolder(final ReleaseShowAdapter.ViewHolder holder, int position) {

        final Shoes number = mShoesList.get(position);
        holder.collect_shoesname.setText(number.getBiaoti());
        holder.coolect_coast.setText("¥"+String.valueOf(number.getPrice()));
        holder.release_show_cs.setText("已浏览"+number.getIid()+"次");
        Glide.with(mContext).load(number.getPicture()).into(holder.collect_image);
        holder.collect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();

                Shoes shoes = mShoesList.get(position);
                int cs=Integer.parseInt(shoes.getIid())+1;
                    String zcs="'"+String.valueOf(cs)+"'";
                String url="http://www.shmilyz.com/ForAndroidHttp/update.action";
                Map<String, String> map=new HashMap<String, String>();
                String  setcollect="update shoes set iid="+zcs+ " where id="+shoes.getId()+";";
                Log.i("setcollecta",setcollect);
                map.put("uname",setcollect);
                xutils.post(url, map, new Xutils.XCallBack() {
                    @Override
                    public void onResponse(String result) {

                    }
                });
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
                intent.putExtra(ShoesActivity.SHOES_PRICE,String.valueOf(shoes.getPrice()));
                intent.putExtra(ShoesActivity.SHOES_PRICE,String.valueOf(shoes.getPrice()));
                intent.putExtra(ShoesActivity.SHOES_PRICE,String.valueOf(shoes.getPrice()));
                intent.putExtra(ShoesActivity.SHOES_PICTUREAMOUNT,String.valueOf(shoes.getPictureamount()));
                intent.putExtra(ShoesActivity.SHOES_FILE,shoes.getFile());
                intent.putExtra(ShoesActivity.SHOES_PICTURENAME,shoes.getPicturename());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mShoesList.size();
    }


}

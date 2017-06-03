package com.shmily.tjz.swap.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.shmily.tjz.swap.Gson.Discuss;
import com.shmily.tjz.swap.Gson.DiscussLove;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.LitePal.DiscussAllLite;
import com.shmily.tjz.swap.LitePal.DiscussLite;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Rubbish.Xutils;
import com.shmily.tjz.swap.ShoesActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shmily_Z on 2017/6/2.
 */

public class DiscussAdapter extends RecyclerView.Adapter<DiscussAdapter.ViewHolder>{

    private static final String TAG = "ShoesAdapter";

    private Context mContext;

    private List<DiscussAllLite> mShoesList=new ArrayList<>();
String username;
        private String loveurl="http://www.shmilyz.com/ForAndroidHttp/love.action";
            private  Xutils xutils;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headview;
        TextView username,date,content,like;
        LikeButton  likeButton;
        public ViewHolder(View view) {
            super(view);
            username= (TextView) view.findViewById(R.id.username);
            date= (TextView) view.findViewById(R.id.date);
            content= (TextView) view.findViewById(R.id.content);
            like= (TextView) view.findViewById(R.id.like);
            headview= (CircleImageView) view.findViewById(R.id.icon_image);
            likeButton= (LikeButton) view.findViewById(R.id.star_button);


        }
    }

    public DiscussAdapter(List<DiscussAllLite> shoesList) {
        mShoesList = shoesList;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DiscussAllLite discuss = mShoesList.get(position);
        holder.like.setText(String.valueOf(discuss.getLove()));
        List<DiscussLite> discussLites= DataSupport.where("discussid = ?",String.valueOf(discuss.getDiscussid())).find(DiscussLite.class);
        if (discussLites.size()>0){
            holder.likeButton.setLiked(true);
            holder.like.setTextColor(Color.parseColor("#e2302e"));

        }
        SharedPreferences prefs=mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        username=prefs.getString("username",null);
        holder.username.setText(discuss.getUsername());
        holder.date.setText(discuss.getDate());
        holder.content.setText(discuss.getContent());
        String url="http://www.shmilyz.com/headimage/"+discuss.getUsername()+".jpg";
        Glide.with(mContext).load(url).into(holder.headview);
        xutils=Xutils.getInstance();


        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                int like =Integer.parseInt(holder.like.getText().toString());
                holder.like.setText(String.valueOf(like+1));
                holder.like.setTextColor(Color.parseColor("#e2302e"));
                Map<String, String> maps=new HashMap<String, String>();
                maps.put("uname","UPDATE discuss SET love=love + 1 WHERE id ="+String.valueOf(discuss.getDiscussid()));
                Log.i("liked","UPDATE discuss SET love=love + 1 WHERE id ="+String.valueOf(discuss.getDiscussid()));
                maps.put("upass","INSERT INTO discuss_love(shoesid,username,discussid) VALUES("+String.valueOf(discuss.getShoesid())+","+"'"+username+"'"+","+String.valueOf(discuss.getDiscussid())+")");
                Log.i("liked","INSERT INTO discuss_love(shoesid,username,discussid) VALUES("+String.valueOf(discuss.getShoesid())+","+"'"+username+"'"+","+String.valueOf(discuss.getDiscussid())+")");

                xutils.post(loveurl, maps, new Xutils.XCallBack() {
                    @Override
                    public void onResponse(String result) {

                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int like =Integer.parseInt(holder.like.getText().toString());
                holder.like.setText(String.valueOf(like-1));
                holder.like.setTextColor(Color.parseColor("#000000"));

                Map<String, String> maps=new HashMap<String, String>();
                maps.put("uname","UPDATE discuss SET love=love - 1 WHERE id ="+String.valueOf(discuss.getDiscussid()));
                Log.i("unliked","UPDATE discuss SET love=love - 1 WHERE id ="+String.valueOf(discuss.getDiscussid()));
                maps.put("upass","DELETE FROM discuss_love WHERE shoesid="+String.valueOf(discuss.getShoesid())+" and username='"+username+"'"+" and discussid="+discuss.getDiscussid());
                Log.i("unliked","DELETE FROM discuss_love WHERE shoesid="+String.valueOf(discuss.getShoesid())+" and username='"+username+"'"+" and discussid="+discuss.getDiscussid());

                xutils.post(loveurl, maps, new Xutils.XCallBack() {
                    @Override
                    public void onResponse(String result) {

                    }
                });



            }
        });
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.discuss_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }



    @Override
    public int getItemCount() {
        return mShoesList.size();
    }

}
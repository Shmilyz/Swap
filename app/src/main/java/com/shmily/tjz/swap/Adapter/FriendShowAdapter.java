package com.shmily.tjz.swap.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.shmily.tjz.swap.Db.Contacts;
import com.shmily.tjz.swap.Gson.Discuss;
import com.shmily.tjz.swap.Gson.NumberResult;
import com.shmily.tjz.swap.LitePal.DiscussLite;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.Xutils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shmily_Z on 2017/6/9.
 */

public class FriendShowAdapter extends RecyclerView.Adapter<FriendShowAdapter.ViewHolder>{

    private static final String TAG = "ShoesAdapter";

    private Context mContext;

    private List<NumberResult> mShoesList=new ArrayList<>();
    String username;
    private String loveurl="http://www.shmilyz.com/ForAndroidHttp/love.action";
    private Xutils xutils;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friend_headview;
        TextView friend_name;
        public ViewHolder(View view) {
            super(view);
            friend_name= (TextView) view.findViewById(R.id.friend_name);
            friend_headview= (CircleImageView) view.findViewById(R.id.friend_headview);


        }
    }

    public FriendShowAdapter(List<NumberResult> shoesList) {
        mShoesList = shoesList;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final NumberResult number = mShoesList.get(position);
        holder.friend_name.setText(number.getName());
        String urls="http://www.shmilyz.com/headimage/"+number.getUsername()+".jpg";
        Glide.with(mContext).load(urls).into(holder.friend_headview);

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.collect_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }



    @Override
    public int getItemCount() {
        return mShoesList.size();
    }

}
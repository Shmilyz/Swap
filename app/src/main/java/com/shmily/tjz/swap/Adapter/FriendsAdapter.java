package com.shmily.tjz.swap.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.shmily.tjz.swap.Gson.Friends;
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
 * Created by Shmily_Z on 2017/6/7.
 */

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int FRIENDS_LOVE =1;
    public static final int FRIENDS_DISCUSS =2;
    public static final int FRIENDS_RELEASE =3;



    private Context mContext;

    private List<Friends> mShoesList=new ArrayList<>();
    String username;
    private String loveurl="http://www.shmilyz.com/ForAndroidHttp/love.action";
    private Xutils xutils;

    public class LoveHolder extends RecyclerView.ViewHolder {
            TextView friends_love_username,friends_love_shoesname,friends_love_date;
        ImageView friends_love_image;
        public LoveHolder(View view) {
            super(view);
            friends_love_username= (TextView) view.findViewById(R.id.friends_love_username);
            friends_love_shoesname= (TextView) view.findViewById(R.id.friends_love_shoesname);
            friends_love_date= (TextView) view.findViewById(R.id.friends_love_date);
            friends_love_image= (ImageView) view.findViewById(R.id.friends_love_image);

        }
    }
    public class DiscussHolder extends RecyclerView.ViewHolder {
                TextView firends_discuss_username,firends_discuss_discuss,firends_discuss_date;
        ImageView friends_discuss_headview;
        public DiscussHolder(View view) {

            super(view);
            firends_discuss_username= (TextView) view.findViewById(R.id.firends_discuss_username);
            firends_discuss_discuss= (TextView) view.findViewById(R.id.firends_discuss_discuss);
            firends_discuss_date= (TextView) view.findViewById(R.id.firends_discuss_date);
            friends_discuss_headview= (ImageView) view.findViewById(R.id.friends_discuss_headview);

        }
    }
    public class ReleaseHolder extends RecyclerView.ViewHolder {
            TextView friends_release_username,friends_release_name,friends_release_date;
        ImageView friends_release_headview;
        public ReleaseHolder(View view) {
            super(view);
            friends_release_username= (TextView) view.findViewById(R.id.friends_release_username);
            friends_release_name= (TextView) view.findViewById(R.id.friends_release_name);
            friends_release_date= (TextView) view.findViewById(R.id.friends_release_date);
            friends_release_headview= (ImageView) view.findViewById(R.id.friends_release_headview);




        }
    }

    public FriendsAdapter(List<Friends> shoesList) {
        mShoesList = shoesList;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoveHolder){

            ((LoveHolder)holder).friends_love_username.setText(mShoesList.get(position).getUsername());
            ((LoveHolder)holder).friends_love_shoesname.setText(mShoesList.get(position).getShoesname());
            ((LoveHolder)holder).friends_love_date.setText(mShoesList.get(position).getUserdate());
            String url="http://www.shmilyz.com/picture/"+String.valueOf(mShoesList.get(position).getShoesid())+".jpg";
            Log.i("pictureurl",url);
            Glide.with(mContext).load(url).into(((LoveHolder)holder).friends_love_image);

        }
        else if (holder instanceof DiscussHolder){

            ((DiscussHolder)holder).firends_discuss_username.setText(mShoesList.get(position).getUsername());
            ((DiscussHolder)holder).firends_discuss_discuss.setText(mShoesList.get(position).getDiscuss());
            ((DiscussHolder)holder).firends_discuss_date.setText(mShoesList.get(position).getUserdate());
            String url="http://www.shmilyz.com/picture/"+String.valueOf(mShoesList.get(position).getShoesid())+".jpg";
            Glide.with(mContext).load(url).into(((DiscussHolder)holder).friends_discuss_headview);


        }
        else if (holder instanceof ReleaseHolder){
            ((ReleaseHolder)holder).friends_release_username.setText(mShoesList.get(position).getUsername());
            ((ReleaseHolder)holder).friends_release_name.setText(mShoesList.get(position).getShoesname());
            ((ReleaseHolder)holder).friends_release_date.setText(mShoesList.get(position).getUserdate());

            String url="http://www.shmilyz.com/picture/"+String.valueOf(mShoesList.get(position).getShoesid())+".jpg";
            Glide.with(mContext).load(url).into(((ReleaseHolder)holder).friends_release_headview);

        }




    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType==FRIENDS_LOVE){
            view = LayoutInflater.from(mContext).inflate(R.layout.friends_love_item, parent, false);
            LoveHolder loveholder = new LoveHolder(view);

            return new LoveHolder(view);
        }
        else if (viewType==FRIENDS_DISCUSS){
            view = LayoutInflater.from(mContext).inflate(R.layout.friends_discuss_item, parent, false);
            DiscussHolder discussholder = new DiscussHolder(view);

            return new DiscussHolder(view);
        }
        else if (viewType==FRIENDS_RELEASE){
            view = LayoutInflater.from(mContext).inflate(R.layout.friends_release_item, parent, false);
            ReleaseHolder releaseholder = new ReleaseHolder(view);
            return new ReleaseHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mShoesList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mShoesList.size();
    }

}

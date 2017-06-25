package com.shmily.tjz.swap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shmily.tjz.swap.Gson.Positions;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.shmily.tjz.swap.ShowPositionsActivity;
import com.shmily.tjz.swap.Utils.MyApplication;
import com.shmily.tjz.swap.Utils.Xutils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/6/25.
 */

public class PositionsAdapter  extends SwipeMenuAdapter<PositionsAdapter.ViewHolder> {


    private Context mContext;

    private List<Positions> mShoesList = new ArrayList<>();
    String username;
    private Xutils xutils;

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.show_positions_item, parent, false);

        return view;
    }

    @Override
    public PositionsAdapter.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {


        return new PositionsAdapter.ViewHolder(realContentView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView buy_user_shr,buy_user_number,buy_user_position;
        LinearLayout show_positions_item_lin;
        public ViewHolder(View view) {

            super(view);
            buy_user_shr= (TextView) view.findViewById(R.id.buy_user_shr);
            buy_user_number= (TextView) view.findViewById(R.id.buy_user_number);
            buy_user_position= (TextView) view.findViewById(R.id.buy_user_position);
            show_positions_item_lin= (LinearLayout) view.findViewById(R.id.show_positions_item_lin);
        }
    }

    public PositionsAdapter(List<Positions> shoesList) {
        mShoesList = shoesList;
    }


    @Override
    public void onBindViewHolder(final PositionsAdapter.ViewHolder holder, int position) {

         final Positions positions = mShoesList.get(position);
        holder.buy_user_number.setText(positions.getNumber());
        holder.buy_user_position.setText(positions.getPositionss());
        holder.buy_user_shr.setText(positions.getName());
        holder.show_positions_item_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor=mContext.getSharedPreferences("buy_positions_show",mContext.MODE_PRIVATE).edit();
                editor.clear();
                editor.putBoolean("boolean",true);
                editor.putString("name",positions.getName());
                editor.putString("position",positions.getPositionss());
                editor.putString("number",positions.getNumber());
                editor.apply();

                ((ShowPositionsActivity)mContext).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mShoesList.size();
    }

}

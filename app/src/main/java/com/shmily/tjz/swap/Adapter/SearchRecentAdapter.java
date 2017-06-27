package com.shmily.tjz.swap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shmily.tjz.swap.Fragment.ResultFragment;
import com.shmily.tjz.swap.LitePal.Data;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.SearchActivity;
import com.shmily.tjz.swap.ShowSearchItemActivity;
import com.shmily.tjz.swap.Utils.MyApplication;

import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/7.
 */


public class SearchRecentAdapter extends ArrayAdapter<Data> {
    private int resourceId;
    private Context mcontext;
    public SearchRecentAdapter(Context context, int textViewResourceId,
                            List<Data> objects) {
        super(context, textViewResourceId, objects);

        resourceId = textViewResourceId;
        mcontext=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Data data = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.HotName = (TextView) view.findViewById (R.id.hot_name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.HotName.setText(data.getName());
        viewHolder.HotName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mcontext, ShowSearchItemActivity.class);
                intent.putExtra("search_item_get",data.getName());
                mcontext.startActivity(intent);


            }
        });
        return view;
    }

    class ViewHolder {


        TextView HotName;

    }
}

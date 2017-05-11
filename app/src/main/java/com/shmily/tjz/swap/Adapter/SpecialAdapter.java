package com.shmily.tjz.swap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import com.jcodecraeer.imageloader.ImageLoader;
import com.shmily.tjz.swap.Db.ShoesSpecial;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.ScaleImageView;

import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/3.
 */

public class SpecialAdapter extends BaseAdapter {
    private static final String TAG = "ImageGridAdapter";
    private static final boolean DEBUG = true;
    private ImageLoader mLoader;
    private List<ShoesSpecial> mImageList;
    private LayoutInflater mLayoutInflater;
    public SpecialAdapter(Context context,
                          List<ShoesSpecial> list) {
        mLoader = new ImageLoader(context);
        mLoader.setIsUseMediaStoreThumbnails(false);
        mImageList = list;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        mLoader.setRequiredSize(width / 2); //3表示列数
        mLayoutInflater = LayoutInflater.from(context);


    }

    public int getCount() {
        return mImageList.size();
    }
    public Object getItem(int arg0) {
        return null;
    }
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.search_item,
                    null);
            holder = new ViewHolder();
            holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        mLoader.DisplayImage(mImageList.get(position).getImageId(), holder.imageView);
        return convertView;
    }

    static class ViewHolder {
        ScaleImageView imageView;
    }
}

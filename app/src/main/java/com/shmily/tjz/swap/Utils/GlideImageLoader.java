package com.shmily.tjz.swap.Utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.imageloader.ImageLoader;
import com.youth.banner.loader.ImageLoaderInterface;

/**
 * Created by Shmily_Z on 2017/5/4.
 */

public class GlideImageLoader extends ImageLoader implements ImageLoaderInterface {
    Context mContext;
    public GlideImageLoader(Context context) {
        super(context);
        mContext=context;
    }

    @Override
    public void DisplayImage(String url, ImageView imageView) {
        super.DisplayImage(url, imageView);
        Glide.with(mContext).load(url)
                .into(imageView);

    }

    @Override
    public void displayImage(Context context, Object path, View imageView) {
        Glide.with(context).load(path)
                .into((ImageView) imageView);

    }

    @Override
    public View createImageView(Context context) {
        return null;
    }
}

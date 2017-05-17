package com.shmily.tjz.swap.Rubbish;

/**
 * Created by Shmily_Z on 2017/5/17.
 */

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.imnjh.imagepicker.ImageLoader;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.MyApplication;


/**
 * Created by Martin on 2017/1/18.
 */

public class ForSelectGlideImageLoader implements ImageLoader {
    @Override
    public void bindImage(ImageView imageView, Uri uri, int width, int height) {
        Glide.with(MyApplication.getContext()).load(uri).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).override(width, height).dontAnimate().into(imageView);
    }

    @Override
    public void bindImage(ImageView imageView, Uri uri) {
        Glide.with(MyApplication.getContext()).load(uri).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).dontAnimate().into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public ImageView createFakeImageView(Context context) {
        return new ImageView(context);
    }
}

package com.shmily.tjz.swap.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.jaiky.imagespickers.ImageLoader;
import com.shmily.tjz.swap.R;

/**
 * Created by Shmily_Z on 2017/5/17.
 */

public class ImageConfigGlideLoader implements ImageLoader {
    private static final long serialVersionUID = 1L;

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.global_img_default)
                .priority(Priority.HIGH)
                .into(imageView);
    }
}

package com.shmily.tjz.swap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wingsofts.dragphotoview.DragPhotoView;

public class ShowPhotoResultActivity extends AppCompatActivity {
        private DragPhotoView dragPhotoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo_result);
        dragPhotoView= (DragPhotoView) findViewById(R.id.Drag);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        Glide.with(ShowPhotoResultActivity.this).load(url)
                .centerCrop()
                .placeholder(R.drawable.black)
                .into(dragPhotoView);
        dragPhotoView.setOnExitListener(new DragPhotoView.OnExitListener() {
            @Override
            public void onExit(DragPhotoView dragPhotoView, float v, float v1, float v2, float v3) {
                ShowPhotoResultActivity.this.finish();
            }
        });
        dragPhotoView.setOnTapListener(new DragPhotoView.OnTapListener() {
            @Override
            public void onTap(DragPhotoView dragPhotoView) {
                ShowPhotoResultActivity.this.finish();

            }
        });

    }
}

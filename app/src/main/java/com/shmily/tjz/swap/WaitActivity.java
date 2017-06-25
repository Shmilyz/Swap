package com.shmily.tjz.swap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.shmily.tjz.swap.Utils.HttpUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WaitActivity extends AppCompatActivity {
    ImageView wait_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        wait_imageView= (ImageView) findViewById(R.id.wait_imageView);
        updateBingPic();
    }




    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                Log.i("bingPic",bingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WaitActivity.this).load(bingPic)
                                .diskCacheStrategy( DiskCacheStrategy.NONE )
                                .skipMemoryCache( true )
                                .into(new GlideDrawableImageViewTarget(wait_imageView) {
                                          @Override
                                          public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {

                                              Timer timer=new Timer();
                                              TimerTask task=new TimerTask(){
                                                  public void run(){
                                                      Intent intent =new Intent(WaitActivity.this,MainActivity.class);
                                                      startActivity(intent);
                                                      WaitActivity.this.finish();
                                                      overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                                  }
                                              };
                                              timer.schedule(task, 500);
                                              super.onResourceReady(drawable, anim);

                                              //在这里添加一些图片加载完成的操作
                                          }
                                      });

                    /*    try {
                            Thread.sleep(5000);
                            Toast.makeText(WaitActivity.this, "AAA", Toast.LENGTH_SHORT).show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

}

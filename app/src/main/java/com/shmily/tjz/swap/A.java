package com.shmily.tjz.swap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weavey.loading.lib.LoadingLayout;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import android.net.Uri;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class A extends AppCompatActivity {


    ImageView imageView;
    @BindView(R.id.po)
    Button po;
    private static final int REQUEST_CODE_CHOOSE = 23;
    List<Uri> mSelected;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Toast.makeText(this, mSelected.get(0).toString(), Toast.LENGTH_SHORT).show();
            Glide.with(this).load(mSelected.get(0).toString()).into(imageView);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        Button button= (Button) findViewById(R.id.select);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(A.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(9)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .theme(R.style.Matisse_Dracula)
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });



        LoadingLayout  loading = (LoadingLayout) findViewById(R.id.load_layout);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //获取系统的连接服务。
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //获取网络的连接情况。
        if (networkInfo != null && networkInfo.isAvailable()) {
          /*  if (networkInfo.getType()==connectivityManager.TYPE_WIFI){
                Toast.makeText(A.this,"网络已启动啦(WIFI)",Toast.LENGTH_SHORT).show();
            }else if (networkInfo.getType()==connectivityManager.TYPE_MOBILE) {
                Toast.makeText(A.this,"网络已启动啦(3G)",Toast.LENGTH_SHORT).show();

            }*/
            Toast.makeText(A.this, "网络已启动啦(WIFI)", Toast.LENGTH_SHORT).show();
            loading.setStatus(LoadingLayout.Success);//加载成功


        } else {
            Toast.makeText(A.this, "网络已关闭啦", Toast.LENGTH_SHORT).show();
            loading.setStatus(LoadingLayout.No_Network);//无网络

        }
            loading.setOnReloadListener(new LoadingLayout.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    Toast.makeText(A.this, "AAAA", Toast.LENGTH_SHORT).show();
                }
            });
        imageView = (ImageView) findViewById(R.id.image);
        Intent intent = getIntent();

        String b = (String) intent.getSerializableExtra("ss");
//        a.setText(b);

               /* Position Position = new Position();
                Position.getLocation(A.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        A.this.runOnUiThread(new Runnable() {
                            //                    runOnUiThread()方法的将线程切换回主线程，因为我们知道我们无法再副线程里面进行ui的变化，所以我们用这个方法。
                            @Override
                            public void run() {
                                SharedPreferences prefs = getSharedPreferences("location", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = prefs.edit();
                                String aa = prefs.getString("City", null);
                                editor.commit();
                                a.setText(aa);
//                        告诉它刷新结束，收回进度条。
                            }
                        });
                    }
                }).start();*/
        SharedPreferences pref = getSharedPreferences("shoes_result_service", MODE_PRIVATE);
        String result = pref.getString("shoes_result_service", "");


        int i = 1;
        StringBuilder url = new StringBuilder();
        url.append("http://www.shmilyz.com/search/").append(i).append(".png");
        String urls = String.valueOf(url);
//        Glide.with(this).load(urls).into(imageView);

    }
}

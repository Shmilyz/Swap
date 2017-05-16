package com.shmily.tjz.swap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.shmily.tjz.swap.Adapter.RecommendAdapter;
import com.shmily.tjz.swap.Adapter.ShoesAdapter;
import com.shmily.tjz.swap.Adapter.SpecialAdapter;
import com.shmily.tjz.swap.Db.ShoesSpecial;
import com.shmily.tjz.swap.Gson.Shoes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShoesActivity extends AppCompatActivity {
    public static final String SHOES_NAME ="shoes_biaoti";
    public static final String SHOES_IMAGE_ID ="fruit_image_id";
    FloatingActionButton fab;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    private SpecialAdapter adapter;
    private List<ShoesSpecial> shoessearchList = new ArrayList<>();
    private List<Shoes> recommendList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
       /* Explode explode = new Explode();
        explode.setDuration(700L);
        getWindow().setEnterTransition(explode);*/

        SwipeBackHelper.onCreate(this);
        Intent intent=getIntent();
        String fruitName=intent.getStringExtra(SHOES_NAME);
        String fruitImageId=intent.getStringExtra(SHOES_IMAGE_ID);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView= (ImageView) findViewById(R.id.fruit_image_view);
        TextView fruitContentText= (TextView) findViewById(R.id.fruit_content_text);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        initview();
        recyview();
        collapsingToolbar.setTitle(fruitName);
      Glide.with(this).load(fruitImageId).into(fruitImageView);
//        fruitContentText.setText(fruitName);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"确认收藏？？",Snackbar.LENGTH_LONG)
                        .setAction("确认", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ShoesActivity.this, "收藏成功！！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    private void recyview() {


        try {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 负责接收Handler消息，并执行UI更新
                    // 判断消息的来源：通过消息的类型 what
                    switch (msg.what) {
                        case WHAT_NEWS:

                            RecyclerView recyclerView= (RecyclerView) findViewById(R.id.show_src_recycler_view);
                            LinearLayoutManager layoutManger =new LinearLayoutManager(ShoesActivity.this);
                            layoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
                            recyclerView.setLayoutManager(layoutManger);
                            RecommendAdapter adapter=new RecommendAdapter(recommendList);
                            recyclerView.setAdapter(adapter);


                            break;

                    }

                }
            } ;

            SharedPreferences pref=getSharedPreferences("shoes_result_service",MODE_PRIVATE);
            String result=pref.getString("all_result"," ");
            JSONObject jsonobject=new JSONObject(result);
            JSONArray shoesArray=jsonobject.getJSONArray("result");
            Gson gson=new Gson();
            List<Shoes> shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());
        /*    for(Shoes shoes : shoesList)
            {

                recommendList.add(shoes);

            }*/
            for (int i=0;i<=6;i++){
                Random random=new Random();
                int index=random.nextInt(shoesList.size());
                recommendList.add(shoesList.get(index));
            }
            Message msg = handler.obtainMessage() ;
            // 设置消息内容（可选）
            // 设置消息类型
            msg.what = WHAT_NEWS;
            // 发送消息
            handler.sendMessage(msg) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void initview() {
        for (int i = 1; i <= 4; i++) {
            StringBuilder url = new StringBuilder();
            url.append("http://www.shmilyz.com/search/").append(i).append(".png");
            String urls = String.valueOf(url);
            ShoesSpecial shoessearch = new ShoesSpecial(urls);
            shoessearchList.add(shoessearch);

        }
        MultiColumnListView multicolumn = (MultiColumnListView) findViewById(R.id.show_sec_list);
        adapter = new SpecialAdapter(ShoesActivity.this, shoessearchList);
        multicolumn.setAdapter(adapter);
        multicolumn.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                String pos= String.valueOf(position);
                Toast.makeText(ShoesActivity.this, pos, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

   /* private String generateFruitContent(String fruitName) {
        StringBuilder fruitContent=new StringBuilder();
        for(int i=0;i<500;i++){
            fruitContent.append(fruitName);
        }
        return fruitContent.toString();
    }*/


}

package com.shmily.tjz.swap;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.shmily.tjz.swap.Adapter.SpecialAdapter;
import com.shmily.tjz.swap.Db.ShoesSpecial;

import java.util.ArrayList;
import java.util.List;

public class ShoesActivity extends AppCompatActivity {
    public static final String SHOES_NAME ="shoes_biaoti";
    public static final String SHOES_IMAGE_ID ="fruit_image_id";
    FloatingActionButton fab;
    private SpecialAdapter adapter;
    private List<ShoesSpecial> shoessearchList = new ArrayList<>();
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
//        initview();
    }
    private void initview() {
        for (int i = 1; i <= 10; i++) {
            StringBuilder url = new StringBuilder();
            url.append("http://www.shmilyz.com/search/").append(i).append(".png");
            String urls = String.valueOf(url);
            ShoesSpecial shoessearch = new ShoesSpecial(urls);
            shoessearchList.add(shoessearch);

        }
        MultiColumnListView multicolumn = (MultiColumnListView)findViewById(R.id.list);
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

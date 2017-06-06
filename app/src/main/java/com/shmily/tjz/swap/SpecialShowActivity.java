package com.shmily.tjz.swap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.DiscussAdapter;
import com.shmily.tjz.swap.Adapter.ShoesAdapter;
import com.shmily.tjz.swap.Gson.Discuss;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.Utils.Xutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialShowActivity extends AppCompatActivity {
    private String special_Name,special_Specialcontent,special_Specialname,special_Url;
    private TextView specoal_show_title,specoal_show_detail;
    private RecyclerView specoal_show_recy;
    private ImageView specoal_show_image;
    private List<Shoes> shoesList =new ArrayList<>();
    public ShoesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_show);
        Xutils xutils=Xutils.getInstance();
        specoal_show_title= (TextView) findViewById(R.id.specoal_show_title);
        specoal_show_detail= (TextView) findViewById(R.id.specoal_show_detail);
        specoal_show_recy= (RecyclerView) findViewById(R.id.specoal_show_recy);
        specoal_show_image= (ImageView) findViewById(R.id.specoal_show_image);
        Intent intent=getIntent();
        special_Name=intent.getStringExtra("special_Name");
        special_Specialcontent=intent.getStringExtra("special_Specialcontent");
        special_Specialname=intent.getStringExtra("special_Specialname");
        special_Url=intent.getStringExtra("special_Url");
        Glide.with(this).load(special_Url).placeholder(R.mipmap.blackback)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .crossFade().into(specoal_show_image);
        specoal_show_title.setText(special_Name);
        specoal_show_detail.setText(special_Specialcontent);

        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        Map<String, String> maps=new HashMap<String, String>();
        maps.put("uname","select * from discuss where special="+special_Specialname );
        Log.i("select_special","select * from discuss where special="+special_Specialname);
        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());
                    LinearLayoutManager layoutManager=new LinearLayoutManager(SpecialShowActivity.this);
                    specoal_show_recy.setLayoutManager(layoutManager);
                    adapter=new ShoesAdapter(shoesList);
                    specoal_show_recy.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}

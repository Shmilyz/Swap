package com.shmily.tjz.swap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.DiscussAdapter;
import com.shmily.tjz.swap.Gson.Discuss;
import com.shmily.tjz.swap.Gson.DiscussLove;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.LitePal.DiscussLite;
import com.shmily.tjz.swap.Rubbish.Xutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xutils.x;

public class DiscussActivity extends AppCompatActivity {
private String shoes_id;
    private List<Discuss> discussList = new ArrayList<>();
    private List<DiscussLove> discussloveList = new ArrayList<>();

    private  String username;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        Fade fade = new Fade();
        fade.setDuration(100L);
        getWindow().setEnterTransition(fade);
        Intent intent=getIntent();

      shoes_id=intent.getStringExtra("discuss_shoes_id");
/*        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
        username=prefs.getString("username",null);*/
        Xutils xutils=Xutils.getInstance();
        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        Map<String, String> maps=new HashMap<String, String>();
        maps.put("uname","select * from discuss where shoesid="+shoes_id );


      /*  Map<String, String> map=new HashMap<String, String>();
        map.put("uname","select * from discuss_love where shoesid="+shoes_id+" and username="+"'张梦'");

        xutils.post(url, map, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {
                try {
                    Toast.makeText(DiscussActivity.this, result, Toast.LENGTH_SHORT).show();
                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    discussloveList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<DiscussLove>>(){}.getType());
                    for (DiscussLove discusslove:discussloveList){
                        DiscussLite dis=new DiscussLite();
                        dis.setDiscussid(discusslove.getDiscussid());
                        dis.save();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/


        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {


                try {
                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    discussList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Discuss>>(){}.getType());
                    RecyclerView recyclerView= (RecyclerView) findViewById(R.id.discuss_recy);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(DiscussActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    DiscussAdapter adapter=new DiscussAdapter(discussList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}

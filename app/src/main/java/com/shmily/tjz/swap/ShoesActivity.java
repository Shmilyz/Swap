package com.shmily.tjz.swap;

import android.app.ActivityOptions;
import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.shmily.tjz.swap.Adapter.RecommendAdapter;
import com.shmily.tjz.swap.Adapter.ShoesShowAdapter;
import com.shmily.tjz.swap.Adapter.SpecialAdapter;
import com.shmily.tjz.swap.Db.ShoesSpecial;
import com.shmily.tjz.swap.Gson.Discuss;
import com.shmily.tjz.swap.Gson.DiscussLove;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.LitePal.DiscussAllLite;
import com.shmily.tjz.swap.LitePal.DiscussLite;
import com.shmily.tjz.swap.Utils.Xutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import ezy.ui.view.RoundButton;

public class ShoesActivity extends AppCompatActivity {
    public static final String SHOES_IMAGE_URL ="shoes_url";
    public static final String SHOES_ID ="shoes_id";
    FloatingActionButton fab;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    final int WHAT_NEWSS = 2;
    private ShoesShowAdapter adapter;
    private List<ShoesSpecial> shoessearchList = new ArrayList<>();
    private List<Shoes> recommendList = new ArrayList<>();
    private List<Shoes> shoesList = new ArrayList<>();
    private  List<DiscussLove> discussloveList = new ArrayList<>();

    private int shoesid_int;
    private String shoesid;
    private TextView shoesContentText,model,type;
    private TextView username,position;
    private CircleImageView  headimage,discuss_headimage;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView info_name,info_size,info_position,info_date,info_desc,discuss_username,discuss_discuss;
    private RoundButton discuss;
    private String username_get;
    private List<Discuss> discussList = new ArrayList<>();
    private Xutils xutil;
    private String shoesimageurl;
    private TextView activty_shoes_price;
    private boolean collect=true;
    private String setcollect;
    @Override
    protected void onResume() {
        super.onResume();
        lovelite();
        Alldiscuss();
//        getcollect();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes);

        xutil=Xutils.getInstance();
        SwipeBackHelper.onCreate(this);
        Intent intent=getIntent();
        shoesid=intent.getStringExtra(SHOES_ID);
        shoesimageurl=intent.getStringExtra(SHOES_IMAGE_URL);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView ShoesImageView= (ImageView) findViewById(R.id.fruit_image_view);
        Glide.with(this).load(shoesimageurl).into(ShoesImageView);

        discuss= (RoundButton) findViewById(R.id.discuss);

        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
        username_get=prefs.getString("username",null);
        fab= (FloatingActionButton) findViewById(R.id.fab);




        discuss_headimage= (CircleImageView) findViewById(R.id.discuss_headimage);
        discuss_username= (TextView) findViewById(R.id.discuss_username);
        discuss_discuss= (TextView) findViewById(R.id.discuss_discuss);

        shoesContentText= (TextView) findViewById(R.id.shoes_content_text);
        model= (TextView) findViewById(R.id.model);
        type= (TextView) findViewById(R.id.type);
        username= (TextView) findViewById(R.id.username);
        headimage= (CircleImageView) findViewById(R.id.icon_image);
        position= (TextView) findViewById(R.id.position);
        info_name= (TextView) findViewById(R.id.info_name);
        info_size= (TextView) findViewById(R.id.info_size);
        info_position= (TextView) findViewById(R.id.info_position);
        info_date= (TextView) findViewById(R.id.info_date);
        info_desc= (TextView) findViewById(R.id.info_desc);
        activty_shoes_price= (TextView) findViewById(R.id.activty_shoes_price);
        setSupportActionBar(toolbar);
        finid();

//        fruitContentText.setText(fruitName);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (collect){

                    fab.setImageResource(R.mipmap.love_love);
                    collect=false;
                    setlove(true);


                }
                else {
                    fab.setImageResource(R.mipmap.love);
                    collect=true;
                    setlove(false);

                }

            }
        });
    }

    private void setlove(boolean type) {
        String url="http://www.shmilyz.com/ForAndroidHttp/update.action";
        Map<String, String> map=new HashMap<String, String>();
        if (type){
            setcollect="insert into collect(username,shoesid) value ('"+username_get+"',"+shoesid+")";
            Log.i("setcollect",setcollect);
        }else {
            setcollect="DELETE FROM collect WHERE username='"+username_get+"' and shoesid="+shoesid;
            Log.i("setcollects",setcollect);
        }

        map.put("uname",setcollect);
        xutil.post(url, map, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {

            }
        });



    }

    private void getcollect() {

        String url="http://www.shmilyz.com/ForAndroidHttp/exist.action";
        Map<String, String> maps=new HashMap<String, String>();
        String select="select id from collect where username='"+username_get+"' and shoesid="+shoesid;
        maps.put("uname",select);
        xutil.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject json = new JSONObject(result);
                    String results = json.getString("result");
                    if (results.equals("1")){
                        fab.setImageResource(R.mipmap.love_love);
                        collect=false;
                    }
                    else {
                        fab.setImageResource(R.mipmap.love);
                        collect=true;
                    }







                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });



    }

    private void finid() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWSS:

                        collapsingToolbar.setTitle(shoesList.get(0).getBiaoti());
                        shoesContentText.setText(shoesList.get(0).getBiaoti());
                        model.setText(shoesList.get(0).getBrand());
                        type.setText(shoesList.get(0).getStyle());

                        username.setText(shoesList.get(0).getUsername());
                        position.setText(shoesList.get(0).getPosition());
                        String headimage_url="http://www.shmilyz.com/headimage/"+shoesList.get(0).getUsername()+".jpg";
                        Glide.with(ShoesActivity.this).load(headimage_url).into(headimage);

                        info_name.setText(shoesList.get(0).getBiaoti());
                        info_size.setText(shoesList.get(0).getSize());
                        info_date.setText(shoesList.get(0).getDate());
                        info_position.setText(shoesList.get(0).getPosition());
                        info_desc.setText(shoesList.get(0).getMiaoshu());
                        activty_shoes_price.setText("¥"+shoesList.get(0).getPrice());
                        discuss.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View view) {

                                Intent intent=new Intent(ShoesActivity.this,DiscussActivity.class);
                                intent.putExtra("discuss_shoes_id",String.valueOf(shoesList.get(0).getId()));
                                intent.putExtra("discuss_showimage",shoesimageurl);
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ShoesActivity.this).toBundle());

                            }
                        });
                        initview();
                        recyview();

                        break;

                }

            }
        } ;


        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        String results= "select * from shoes where id="+shoesid;
        params.addBodyParameter("uname",results);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
              /*  JSONObject jsonobject=new JSONObject(result);
                JSONArray shoesArray=jsonobject.getJSONArray("result");*/

                JSONObject jsonobject= null;
                try {
                    jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());




                    Message msg = handler.obtainMessage() ;
                    // 设置消息内容（可选）
                    // 设置消息类型
                    msg.what = WHAT_NEWSS;
                    // 发送消息
                    handler.sendMessage(msg) ;




                } catch (JSONException e) {
                    e.printStackTrace();
                }






            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });



    }

    public void Alldiscuss() {
//        DataSupport.deleteAll(DiscussAllLite.class);

        Xutils xutils=Xutils.getInstance();
        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        Map<String, String> maps=new HashMap<String, String>();
        maps.put("uname","select * from discuss where shoesid="+shoesid );
        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");

                    Gson gson=new Gson();
                    discussList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Discuss>>(){}.getType());
                     /*   for (Discuss discuss:discussList){
                            DiscussAllLite dis=new DiscussAllLite();
                            dis.setContent(discuss.getContent());
                            dis.setDate(discuss.getDate());
                            dis.setDiscussid(discuss.getId());
                            dis.setShoesid(discuss.getShoesid());
                            dis.setLove(discuss.getLove());
                            dis.setUsername(discuss.getUsername());
                            dis.save();
                        }*/
                    String dis_user=discussList.get(0).getUsername();
                    String url="http://www.shmilyz.com/headimage/"+dis_user+".jpg";
                    Glide.with(ShoesActivity.this).load(url).into(discuss_headimage);
                    discuss_username.setText(dis_user);
                    discuss_discuss.setText(discussList.get(0).getContent());





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void lovelite() {

        DataSupport.deleteAll(DiscussLite.class);

        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";

        Map<String, String> map=new HashMap<String, String>();
        map.put("uname","select * from discuss_love where shoesid="+shoesid+" and username="+"'"+username_get+"'");
        Log.i("CSS","select * from discuss_love where shoesid="+shoesid+" and username="+"'"+username_get+"'");
        Xutils xutils=Xutils.getInstance();
        xutils.post(url, map, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {
                try {

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
        for (int i = 1; i <= shoesList.get(0).getPictureamount(); i++) {
            StringBuilder url = new StringBuilder();
            url.append("http://www.shmilyz.com/").append( shoesList.get(0).getFile()).append("/").append(shoesList.get(0).getPicturename()).append("_").append(String.valueOf(i)).append(".jpg");
            String urls = String.valueOf(url);

            ShoesSpecial shoessearch = new ShoesSpecial(urls);
            shoessearchList.add(shoessearch);

        }
   /*     MultiColumnListView multicolumn = (MultiColumnListView) findViewById(R.id.show_sec_list);
        adapter = new SpecialAdapter(ShoesActivity.this, shoessearchList);
        multicolumn.setAdapter(adapter);
        multicolumn.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                String pos= String.valueOf(position);
                Toast.makeText(ShoesActivity.this, pos, Toast.LENGTH_SHORT).show();
            }
        });*/
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.show_sec_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(ShoesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter= new ShoesShowAdapter(shoessearchList);
        recyclerView.setAdapter(adapter);

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

package com.shmily.tjz.swap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.DiscussAdapter;
import com.shmily.tjz.swap.Gson.Discuss;
import com.shmily.tjz.swap.Gson.DiscussLove;
import com.shmily.tjz.swap.LitePal.DiscussAllLite;
import com.shmily.tjz.swap.LitePal.DiscussLite;
import com.shmily.tjz.swap.Utils.Xutils;
import com.shmily.tjz.swap.Utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscussActivity extends AppCompatActivity {
    private String shoes_id;
    private List<Discuss> discussList = new ArrayList<>();
    private List<DiscussLove> discussloveList = new ArrayList<>();
    private ImageView discuss_showimage;
    private  String username;
    private CircleImageView discuss_write_head;
    private EditText discuss_write_edit;
    private Button discuss_write_button;
    private  RecyclerView recyclerView;
    private  DiscussAdapter adapter;
    DateUtil dateUtil=new DateUtil();
    Xutils xutils=Xutils.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        Fade fade = new Fade();
        fade.setDuration(100L);
        getWindow().setEnterTransition(fade);
        discuss_showimage= (ImageView) findViewById(R.id.discuss_showimage);
        discuss_write_head= (CircleImageView) findViewById(R.id.discuss_write_head);
        discuss_write_edit= (EditText) findViewById(R.id.discuss_write_edit);
        discuss_write_button= (Button) findViewById(R.id.discuss_write_button);



        Intent intent=getIntent();

        shoes_id=intent.getStringExtra("discuss_shoes_id");
        String  discuss_showimage_url=intent.getStringExtra("discuss_showimage");
        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
        username=prefs.getString("username",null);
        String headimage_url="http://www.shmilyz.com/headimage/"+username+".jpg";
        Glide.with(DiscussActivity.this).load(headimage_url).into(discuss_write_head);
        Glide.with(DiscussActivity.this).load(discuss_showimage_url).into(discuss_showimage);



        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        Map<String, String> maps=new HashMap<String, String>();
        maps.put("uname","select * from discuss where shoesid="+shoes_id );



        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {


                try {
                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    discussList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Discuss>>(){}.getType());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Discuss discuss=new Discuss();
                    discuss.setContent("为该商品发表第一条留言吧!");
                    discuss.setUsername("S小助手");
                    discuss.setDate(dateUtil.getCurrentTime(DateUtil.DateFormat.YYYY_MM_DD));
                    discussList.add(discuss);

                }
                recyclerView= (RecyclerView) findViewById(R.id.discuss_recy);
                LinearLayoutManager layoutManager=new LinearLayoutManager(DiscussActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                adapter=new DiscussAdapter(discussList);
                recyclerView.setAdapter(adapter);

            }
        });





        discuss_write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result=discuss_write_edit.getText().toString().trim().replace(" ","");
                if (!TextUtils.isEmpty(result)){




                    String loveurl="http://www.shmilyz.com/ForAndroidHttp/update.action";
                    Map<String, String> maps=new HashMap<String, String>();
                    maps.put("uname","INSERT INTO discuss(shoesid,username,date,love,content) VALUES ("+shoes_id+","+"'"+username+"'"+","+"'"+dateUtil.getCurrentTime(DateUtil.DateFormat.YYYY_MM_DD)+"'"+","+"0"+","+"'"+discuss_write_edit.getText().toString().trim()+"'"+")");
                    Log.i("discussup","INSERT INTO discuss(shoesid,username,date,love,content) VALUES ("+shoes_id+","+"'"+username+"'"+","+"'"+dateUtil.getCurrentTime(DateUtil.DateFormat.YYYY_MM_DD)+"'"+","+"0"+","+"'"+discuss_write_edit.getText().toString().trim()+"'"+")");

                    discuss_write_edit.setText(null);

                    xutils.post(loveurl, maps, new Xutils.XCallBack() {
                        @Override
                        public void onResponse(String result) {
                            try {
                                JSONObject json = new JSONObject(result);
                                String return_result = json.getString("result");
                                if (return_result.equals("1")){
                                    Toast.makeText(DiscussActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                    String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
                                    Map<String, String> maps=new HashMap<String, String>();
                                    maps.put("uname","select * from discuss where shoesid="+shoes_id );
                                    xutils.post(url, maps, new Xutils.XCallBack() {
                                        @Override
                                        public void onResponse(String result) {

                                            JSONArray shoesArray= null;
                                            try {

                                                JSONObject jsonobject = new JSONObject(result);
                                                shoesArray = jsonobject.getJSONArray("result");
                                                Gson gson=new Gson();
                                                List<Discuss> discussLists = new ArrayList<>();
                                                discussLists=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Discuss>>(){}.getType());
                                                discussList.clear();
                                                Log.i("info",String.valueOf(discussList.size()));
                                                discussList.addAll(discussLists);
                                                Log.i("info",String.valueOf(discussList.size()));
                                                adapter.notifyDataSetChanged();
                                                recyclerView.scrollToPosition(discussList.size()-1);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });




                }else {
                    Toast.makeText(DiscussActivity.this, "请填写评论后在发送。", Toast.LENGTH_SHORT).show();
                }


                hind();
            }
        });
        
        
        




    }

    private void hind() {

        //点击发送后，小键盘无法收回。
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }
}
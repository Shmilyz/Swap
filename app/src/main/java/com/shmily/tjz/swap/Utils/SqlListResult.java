package com.shmily.tjz.swap.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.ShoesAdapter;
import com.shmily.tjz.swap.Gson.Shoes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/10.
 */

public class SqlListResult {

    /*
    * 提供方法，由于会令开线程导致返回的list值为空，所以要做线程短暂休眠处理，影响用户体验，造成卡顿，暂不用。想到新方法后在解决。
    *
    *
    *
    * */
    private List<Shoes> shoesDbList =new ArrayList<>();
    List<Shoes> shoesDbListResult =new ArrayList<>();
String resultss;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    public boolean getList(int start,int end,String sql){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:
                        SharedPreferences prefs=MyApplication.getContext().getSharedPreferences("SqlListResult", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=prefs.edit();
                        editor.remove("sqllistresult");
                        editor.putString("sqllistresult", resultss);
                        editor.apply();

                }
            }
        };

        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        String results= sql+" LIMIT "+start+","+end;
        params.addBodyParameter("uname",results);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                   /* JSONObject jsonobject=new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    List<Shoes> shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());
                    for(Shoes shoes : shoesList)
                    {

                        shoesDbList.add(shoes);

                    }*/
                    resultss=result;
                    Message msg = handler.obtainMessage() ;
                    // 设置消息内容（可选）
                    // 设置消息类型
                    msg.what = WHAT_NEWS;
                    // 发送消息
                    handler.sendMessage(msg) ;


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



        return true;
    }
}

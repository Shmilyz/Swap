package com.shmily.tjz.swap.Srevice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.SearchHotAdapter;
import com.shmily.tjz.swap.LitePal.Hot;
import com.shmily.tjz.swap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class SearchService extends Service {
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    List<Hot> hot=new ArrayList<>();

    public SearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        update();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 20* 60*1000; // 这是8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, SearchService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void update() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:
                if (hot.size()>0){
                    DataSupport.deleteAll(Hot.class);
                    for (Hot hots : hot) {
                        Hot data = new Hot();
                            data.setName(hots.getName());
                            data.save();
                        }
                        SharedPreferences.Editor editor =getSharedPreferences("hot",MODE_PRIVATE).edit();
                        editor.putBoolean("sql", true);
                        editor.apply();
                        break;
                                }
                }

            }
        };

        RequestParams params = new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        String results = "select * from search";
        params.addBodyParameter("uname", results);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray = jsonobject.getJSONArray("result");
                    Gson gson = new Gson();
                    hot = gson.fromJson(String.valueOf(shoesArray), new TypeToken<List<Hot>>() {
                    }.getType());

                    Message msg = handler.obtainMessage();
                    // 设置消息内容（可选）
                    // 设置消息类型
                    msg.what = WHAT_NEWS;
                    // 发送消息
                    handler.sendMessage(msg);
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
}

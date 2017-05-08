package com.shmily.tjz.swap.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.SearchHotAdapter;
import com.shmily.tjz.swap.Adapter.SearchRecentAdapter;
import com.shmily.tjz.swap.LitePal.Data;
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

/**
 * Created by Shmily_Z on 2017/5/4.
 */

public class SearchFragment extends Fragment{

    private View rootView;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    List<Hot> hot=new ArrayList<>();
    List<Data> recent=new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_fragment, container, false);
        initview();
        initv();
        return rootView;

    }

    private void initv() {

        List<Data> count=DataSupport.select().find(Data.class);
        int counts=count.size();
        if (counts>0){
            Data lastData= DataSupport.findLast(Data.class);
            int last=lastData.getId();
            if (last>5){
                recent=DataSupport.limit(5).offset(last-5).find(Data.class);
            }
            else {
                recent=DataSupport.findAll(Data.class);
            }
        }

        final SearchRecentAdapter adapter=new SearchRecentAdapter(getActivity(),R.layout.hot_item,recent);
        ListView listView= (ListView) rootView.findViewById(R.id.recent_view);
        listView.setDivider(null);
        listView.setAdapter(adapter);

    }

    private void initview() {
        SharedPreferences pref=getActivity().getSharedPreferences("hot", getActivity().MODE_PRIVATE);
        boolean sql=pref.getBoolean("sql",false);
        if(sql) {
             hot=DataSupport.findAll(Hot.class);

            SearchHotAdapter adapter = new SearchHotAdapter(getActivity(), R.layout.hot_item, hot);
            ListView listView = (ListView) rootView.findViewById(R.id.list_view);
            listView.setDivider(null);
            listView.setAdapter(adapter);
        }
        else {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 负责接收Handler消息，并执行UI更新
                    // 判断消息的来源：通过消息的类型 what
                    switch (msg.what) {
                        case WHAT_NEWS:

                            SearchHotAdapter adapter = new SearchHotAdapter(getActivity(), R.layout.hot_item, hot);
                            ListView listView = (ListView) rootView.findViewById(R.id.list_view);
                            listView.setDivider(null);
                            listView.setAdapter(adapter);
                            break;

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
                        for (Hot hots : hot) {
                            Hot data = new Hot();
                            data.setName(hots.getName());
                            data.save();
                        }
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("hot", getActivity().MODE_PRIVATE).edit();
                        editor.putBoolean("sql", true);
                        editor.apply();
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



}

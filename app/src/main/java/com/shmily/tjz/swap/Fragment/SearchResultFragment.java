package com.shmily.tjz.swap.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.ShoesAdapter;
import com.shmily.tjz.swap.Db.ShoesDb;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/8.
 */

public class SearchResultFragment extends Fragment {

    private View rootView;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    String result;
    private List<Shoes> shoesDbList =new ArrayList<>();
    private ShoesAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.search_result_fragment, container, false);
        initview();
        return rootView;
    }

    private void initview() {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            result= bundle.getString("search_results");
            Toast.makeText(getActivity(), bundle.getString("results"), Toast.LENGTH_SHORT).show();
        }
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:
                        RecyclerView recyclerView= (RecyclerView) rootView.findViewById(R.id.recycler_view);
                        GridLayoutManager layoutManger=new GridLayoutManager(getActivity(),2);
//        StaggeredGridLayoutManager layoutManger=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
//        瀑布。
                        recyclerView.setLayoutManager(layoutManger);
                        adapter=new ShoesAdapter(shoesDbList);
                        recyclerView.setAdapter(adapter);
                        break;

                }

            }
        } ;

    }
    private void init() {
       /* shoesDbList.clear();
        for (int i=0;i<50;i++){
//这个Random表达的是随机数，index等于fruits的数组的长度，这里random从0开始的，
// 然后index等于random的随机数，然后fruitList加入fruit【】的随机。for语句则表示来50个。
            Random random=new Random();
            int index=random.nextInt(fruits.length);
            shoesDbList.add(fruits[index]);
        }*/
        shoesDbList.clear();
        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        String results= "select * from shoes where miaoshu like '%"+result+"%'" +
                "";
        params.addBodyParameter("uname",results);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject jsonobject=new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    List<Shoes> shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());
                    for(Shoes shoes : shoesList)
                    {
                        shoesDbList.add(shoes);

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

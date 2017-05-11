package com.shmily.tjz.swap.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.shmily.tjz.swap.Db.ShoesSpecial;
import com.shmily.tjz.swap.Gson.Special;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Adapter.SpecialAdapter;
import com.shmily.tjz.swap.Utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/3.
 */

public class SpecialFragment extends Fragment {
    private View rootView;
    private List<ShoesSpecial> shoessearchList = new ArrayList<>();

    private SpecialAdapter adapter;
    List<String> list =new ArrayList<>();
    List<Object> arrayList=new ArrayList<>();
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.special_fragment, container, false);
        initroll();
        initview();
        return rootView;
    }

    private void initroll() {
        final Banner banner = (Banner) rootView.findViewById(R.id.banner);
       /* for (int i = 1; i <= 4; i++) {
            StringBuilder url = new StringBuilder();
            url.append("http://www.shmilyz.com/search/").append(i).append(".png");
            String urls = String.valueOf(url);
            ShoesSpecial shoessearch = new ShoesSpecial(urls);
            images.add(shoessearch);
        }*/
        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        String results= "select * from special";
        params.addBodyParameter("uname",results);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject jsonobject=new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    List<Special> shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Special>>(){}.getType());
                    for(Special special : shoesList)
                    {
                            list.add(special.getName());
                        arrayList.add(special.getUrl());

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

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:
                        //设置banner样式
                        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
                        //设置图片加载器
                        banner.setImageLoader(new GlideImageLoader(getActivity()));
                        //设置图片集合
                        banner.setImages(arrayList);
                        //设置banner动画效果
                        banner.setBannerAnimation(Transformer.ZoomOut);
                        //设置标题集合（当banner样式有显示title时）
                        banner.setBannerTitles(list);
                        //设置自动轮播，默认为true
                        banner.isAutoPlay(true);
                        //设置轮播时间
                        banner.setDelayTime(2880);
                        //设置指示器位置（当banner模式中有指示器时）
                        banner.setIndicatorGravity(BannerConfig.CENTER);

                        //banner设置方法全部调用完毕时最后调用
                        banner.start();
                        break;

                }

            }
        } ;








    }

    private void initview() {
        for (int i = 1; i <= 24; i++) {
            StringBuilder url = new StringBuilder();
            url.append("http://www.shmilyz.com/search/").append(i).append(".png");
            String urls = String.valueOf(url);
            ShoesSpecial shoessearch = new ShoesSpecial(urls);
            shoessearchList.add(shoessearch);

        }
        MultiColumnListView multicolumn = (MultiColumnListView) rootView.findViewById(R.id.list);
        adapter = new SpecialAdapter(getActivity(), shoessearchList);
        multicolumn.setAdapter(adapter);
        multicolumn.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                String pos= String.valueOf(position);
                Toast.makeText(getActivity(), pos, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.shmily.tjz.swap.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.shmily.tjz.swap.SpecialShowActivity;
import com.shmily.tjz.swap.Utils.GlideImageLoader;
import com.weavey.loading.lib.LoadingLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

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

    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManger;
    private LoadingLayout loadingLayout;
    private List<Special> shoesList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.special_fragment, container, false);
        localBroadcastManger=LocalBroadcastManager.getInstance(getActivity());
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.shmily.tjz.swap.LOCAL_SPECIAL");
        localReceiver=new LocalReceiver();
        localBroadcastManger.registerReceiver(localReceiver,intentFilter);
        loadnet();
        initroll();
        initview();
        return rootView;
    }
    private void loadnet() {
        loadingLayout=(LoadingLayout)rootView.findViewById(R.id.special_fragment_load_layout);
        loadingLayout.setLoadingPage(R.layout.define_loading_page);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        //获取系统的连接服务。
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //获取网络的连接情况。
        if (networkInfo != null && networkInfo.isAvailable()) {
          /*  if (networkInfo.getType()==connectivityManager.TYPE_WIFI){
                Toast.makeText(A.this,"网络已启动啦(WIFI)",Toast.LENGTH_SHORT).show();
            }else if (networkInfo.getType()==connectivityManager.TYPE_MOBILE) {
                Toast.makeText(A.this,"网络已启动啦(3G)",Toast.LENGTH_SHORT).show();

            }*/
            loadingLayout.setStatus(LoadingLayout.Success);//加载成功


        } else {
            loadingLayout.setStatus(LoadingLayout.Loading);
        }

    }

    private void initroll() {
        final Banner banner = (Banner) rootView.findViewById(R.id.banner);

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getActivity(),String.valueOf(position), Toast.LENGTH_SHORT).show();
                Special special=shoesList.get(position);
                Intent intent=new Intent(getActivity(), SpecialShowActivity.class);
                intent.putExtra("special_Name",special.getName());
                intent.putExtra("special_Specialcontent",special.getSpecialcontent());
                intent.putExtra("special_Specialname",special.getSpecialname());
                intent.putExtra("special_Url",special.getUrl());
                getActivity().startActivity(intent);
            }
        });

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
                    shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Special>>(){}.getType());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManger.unregisterReceiver(localReceiver);
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

     class LocalReceiver extends BroadcastReceiver{
         @Override
         public void onReceive(Context context, Intent intent) {
             loadnet();
             initroll();
             initview();
                    }
     }
}
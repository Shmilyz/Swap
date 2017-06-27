package com.shmily.tjz.swap.Utils;

/**
 * Created by Shmily_Z on 2017/3/31.
 */

import android.app.Application;
import android.content.Context;
import android.util.Log;


import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.shmily.tjz.swap.MainActivity;
import com.shmily.tjz.swap.R;
import com.weavey.loading.lib.LoadingLayout;

import org.litepal.LitePal;
import org.xutils.x;

/**
 * Created by OO on 2017/3/31.
 */

public class MyApplication extends Application {
    private  static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        initCloudChannel(this);

        x.Ext.setDebug(true);
        context=getApplicationContext();
        LitePal.initialize(context);
        LoadingLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setErrorImage(R.mipmap.error)
                .setEmptyImage(R.mipmap.empty)
                .setNoNetworkImage(R.mipmap.no_network)
                .setAllTipTextColor(R.color.black)
                .setAllTipTextSize(14)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.black)
                .setReloadButtonWidthAndHeight(150,40);

    }
    /*
    *创建方法 为所有脱离activity的类提供全局context
    *
    * */
    public  static Context getContext(){
        return  context;
    }
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("TAG", "init cloudchannel success");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d("TAG", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

}
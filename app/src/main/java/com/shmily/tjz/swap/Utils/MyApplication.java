package com.shmily.tjz.swap.Utils;

/**
 * Created by Shmily_Z on 2017/3/31.
 */

import android.app.Application;
import android.content.Context;

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
        x.Ext.setDebug(true);
        context=getApplicationContext();
        LitePal.initialize(context);
    }
    /*
    *创建方法 为所有脱离activity的类提供全局context
    *
    * */
    public  static Context getContext(){
        return  context;
    }
}
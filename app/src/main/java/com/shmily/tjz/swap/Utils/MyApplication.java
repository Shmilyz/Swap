package com.shmily.tjz.swap.Utils;

/**
 * Created by Shmily_Z on 2017/3/31.
 */

import android.app.Application;

import org.xutils.x;

/**
 * Created by OO on 2017/3/31.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
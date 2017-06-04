package com.shmily.tjz.swap.Utils;

import android.os.Handler;
import android.os.Looper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * Created by Shmily_Z on 2017/6/1.
 */

public class Xutils {
    private Handler handler;
    private volatile static Xutils instance;

    private Xutils() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static Xutils getInstance() {
        if (instance == null) {
            synchronized (Xutils.class) {
                if (instance == null) {
                    instance = new Xutils();
                }
            }
        }
        return instance;
    }

    public void post(String url,Map<String, String> maps,final XCallBack callback){
    RequestParams params = new RequestParams(url);
    for (Map.Entry<String, String> entry : maps.entrySet()) {
        params.addQueryStringParameter(entry.getKey(), entry.getValue());
    }
    x.http().post(params, new Callback.CacheCallback<String>() {
        @Override
        public void onSuccess(String result) {
            onSuccessResponse(result, callback);


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

    private void onSuccessResponse(final String result, final XCallBack callback) {


        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(result);
                }
            }
        });
    }

    public interface XCallBack {
        void onResponse(String result);
    }
}

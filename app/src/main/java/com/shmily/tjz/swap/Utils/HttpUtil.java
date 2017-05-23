package com.shmily.tjz.swap.Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Shmily_Z on 2017/4/5.
 */

public class HttpUtil {
    public static  void sendOKHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
/*
* 利用我们的开源包进行HTTP请求，然后在别的地方进行调用这个方法。
* */

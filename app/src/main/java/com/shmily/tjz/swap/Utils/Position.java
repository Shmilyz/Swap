package com.shmily.tjz.swap.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Shmily_Z on 2017/5/2.
 */

public class Position {
    public LocationClient mLocationClient;
    StringBuilder currentPosition = new StringBuilder();
    private Handler handler;
    final int WHAT_NEWS = 1 ;

    public boolean getLocation(final Context context){

        mLocationClient = new LocationClient(context);

        mLocationClient.registerLocationListener(new MyLocationListener());

        requestLocation();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:
                        SharedPreferences prefs=context.getSharedPreferences("location", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=prefs.edit();
                        editor.remove("City");
                        editor.clear();
                        editor.putString("City", String.valueOf(currentPosition));
                        editor.apply();
                        editor.commit();

                        break;

                }

            }

        } ;
        return true;

    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            try {
                currentPosition.append(location.getCity());
                currentPosition.append(" ").append(location.getDistrict());
                Message msg = handler.obtainMessage() ;
                // 设置消息内容（可选）
                // 设置消息类型
                msg.what = WHAT_NEWS;
                // 发送消息
                handler.sendMessage(msg) ;
            } catch (Exception e) {
/*
                Toast.makeText(Wel.this, "点击尝试再次定位。", Toast.LENGTH_SHORT).show();
*/
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }
}

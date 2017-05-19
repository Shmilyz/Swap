package com.shmily.tjz.swap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.shmily.tjz.swap.Utils.DrivingRouteOverlay;
import com.shmily.tjz.swap.Utils.MyApplication;

public class MapActivity extends AppCompatActivity {

    private MapView baiduMap;
        private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(MyApplication.getContext());
        setContentView(R.layout.activity_map);
        baiduMap= (MapView) findViewById(R.id.bmapbiew);
       mBaiduMap = baiduMap.getMap();


        final RoutePlanSearch routePlanSearch = RoutePlanSearch.newInstance();



        OnGetRoutePlanResultListener listener=new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {



            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {


            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {


                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    // result.getSuggestAddrInfo()

                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {

                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    String distant= String.valueOf(result.getRouteLines().get(0).getDistance()/1000);
;
                    Toast.makeText(MapActivity.this, distant+"公里", Toast.LENGTH_SHORT).show();
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();

                    }



            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };

      routePlanSearch.setOnGetRoutePlanResultListener(listener);
        final PlanNode stNode = PlanNode.withCityNameAndPlaceName("天津", "西青");
        final PlanNode enNode = PlanNode.withCityNameAndPlaceName("河南", "驻马店");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MapActivity.this.runOnUiThread(new Runnable() {
                    //                    runOnUiThread()方法的将线程切换回主线程，因为我们知道我们无法再副线程里面进行ui的变化，所以我们用这个方法。
                    @Override
                    public void run() {

                        routePlanSearch.drivingSearch((new DrivingRoutePlanOption())
                                .from(stNode)
                                .to(enNode));

                    }
                });
            }
        }).start();


    }

    @Override
    protected void onResume() {
        super.onResume();
        baiduMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baiduMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.onDestroy();
    }

}

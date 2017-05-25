package com.shmily.tjz.swap.Fragment;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import com.liuguangqiang.cookie.CookieBar;
import com.liuguangqiang.cookie.OnActionClickListener;
import com.shmily.tjz.swap.Adapter.ShoesAdapter;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.SearchActivity;
import com.shmily.tjz.swap.SelectActivity;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.weavey.loading.lib.LoadingLayout;

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
 * Created by Shmily_Z on 2017/5/2.
 */

public class MainFragment extends Fragment {
    private LoadingLayout loadingLayout;
    private View rootView;
    private XRecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    FloatingToolbar mFloatingToolbar;
    private List<Shoes> shoesDbList =new ArrayList<>();
    public ShoesAdapter adapter;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    final int WHAT_NEWSS = 2 ;
    private LocalBroadcastManager localBroadcastManger;

    int start=10;
    int return_start=10;
    boolean load=true;
    boolean return_load=false;
    private ProgressDialog pDialog ;
    String username;

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==getActivity().RESULT_OK)
                {
                    return_start=10;
                    load=false;
                    return_load=true;
                    init();
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.main_fragment, container, false);

        //        过场动画
        Fade fade = new Fade();
        fade.setDuration(700L);
        getActivity().getWindow().setEnterTransition(fade);
        setHasOptionsMenu(true);
        initView();
        new CookieBar.Builder(getActivity())
                .setTitle("欢迎您")
                .setMessage(username)
                .setBackgroundColor(R.color.colorPrimary)
                .setAction("确认", new OnActionClickListener() {
                    @Override
                    public void onClick() {

                    }
                })
                .show();
        loadnet();

        return rootView;
    }

    private void loadnet() {
        loadingLayout=(LoadingLayout)rootView.findViewById(R.id.main_fragment_load_layout);
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
            loadingLayout.setStatus(LoadingLayout.No_Network);//无网络

        }
        loadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
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
                    init();
                    Intent intent=new Intent("com.shmily.tjz.swap.LOCAL_SPECIAL");
                    localBroadcastManger=LocalBroadcastManager.getInstance(getActivity());
                    localBroadcastManger.sendBroadcast(intent);

                } else {
                    Snackbar.make(v,"改善网络环境后再试。",Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });
    }

    private void initView() {
        SharedPreferences prefs=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=prefs.edit();
        boolean isGuideLoaded=prefs.getBoolean("denglu",false);
        username=prefs.getString("username",null);



        /*mDrawerLayout= (DrawerLayout) rootView.findViewById(R.id.activity_main);
       NavigationView navView= (NavigationView) rootView.findViewById(R.id.nav_view);
        View headerLayout = navView.inflateHeaderView(R.layout.nav_header);
        TextView name= (TextView) headerLayout.findViewById(R.id.username);
        name.setText(username);*/
        final FloatingActionButton fab= (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFloatingToolbar= (FloatingToolbar) rootView.findViewById(R.id.floatingToolbar);
        mFloatingToolbar.attachFab(fab);
      /*  swipeRefresh= (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshShoes();
            }
        });
*/

        /*swipeRefresh= (TwinklingRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setEnableOverScroll(true);
        swipeRefresh.setHeaderView(new ProgressLayout(getActivity()));
        swipeRefresh.setBottomView(new LoadingView(getActivity()));
        swipeRefresh.setFloatRefresh(true);
        swipeRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.finishRefreshing();
                    }
                },2000);
            }
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.finishLoadmore();
                    }
                },2000);
            }
        });*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           /*     Intent intent=new Intent(MainActivity.this,SelectActivity.class);
                startActivity(intent);*/
                mFloatingToolbar.show();


            }
        });
      /*  ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.gengduo);
            actionBar.setTitle("   ");
        }*/
//        navView.setCheckedItem(R.id.nav_me);
     /*   navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_setting:
                        editor.remove("username");
                        editor.remove("denglu");
                        editor.commit();
                        Intent intent=new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case R.id.nav_theme:
                        Intent intent1=new Intent(getActivity(),LocationActivity.class);
                        startActivity(intent1);
//                    在这里编写逻辑性的东西。
                }
                return true;
            }
        });*/
        init();




        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_shaixuan:
                        Intent intent=new Intent(getActivity(),SelectActivity.class);

                        startActivityForResult(intent, 1,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        break;
                    case R.id.menu_sousuo:
                        Intent intent1=new Intent(getActivity(),SearchActivity.class);
                        getActivity().startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        break;
                    case R.id.menu_xihuan:
                        mFloatingToolbar.hide();
                        break;
                    default:
                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {


            }
        });
    }
/*
    public void refreshShoes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    //                    runOnUiThread()方法的将线程切换回主线程，因为我们知道我们无法再副线程里面进行ui的变化，所以我们用这个方法。
                    @Override
                    public void run() {
                        init();
                        adapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
//                        告诉它刷新结束，收回进度条。
                    }
                });
            }
        }).start();
    }
*/

    public void init() {




        mRecyclerView = (XRecyclerView)rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        init();
                        mRecyclerView.refreshComplete();
                    }

                }, 3000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        if (load) {
                            load(start);
                            start = start + 10;

                        }
                        if (return_load){
                            returnload(return_start);
                            return_start=return_start+10;

                        }
                        mRecyclerView.loadMoreComplete();
                    }

                }, 1000);            //refresh data here

            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:

                        GridLayoutManager layoutManger=new GridLayoutManager(getActivity(),2);
//        StaggeredGridLayoutManager layoutManger=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
//        瀑布。
                        mRecyclerView.setLayoutManager(layoutManger);
                        mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
                        mRecyclerView.setRefreshProgressStyle(2);
                        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);


                        /*SqlListResult sqlListResult=new SqlListResult();
                        sqlListResult.getList(0,5,"select * from shoes");
                         new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(210);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    //                    runOnUiThread()方法的将线程切换回主线程，因为我们知道我们无法再副线程里面进行ui的变化，所以我们用这个方法。
                                    @Override
                                    public void run() {
                                        SharedPreferences prefs = getActivity().getSharedPreferences("SqlListResult", Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = prefs.edit();
                                        String aa = prefs.getString("sqllistresult", null);
                                        editor.commit();
                                        List<Shoes> sql= null;
                                        try {
                                            JSONObject jsonobject=new JSONObject(aa);
                                            JSONArray shoesArray=jsonobject.getJSONArray("result");
                                            Gson gson=new Gson();
                                            sql = gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        adapter=new ShoesAdapter(sql);
                                        mRecyclerView.setAdapter(adapter);
//                        告诉它刷新结束，收回进度条。
                                    }
                                });
                            }
                        }).start();*/

                        adapter=new ShoesAdapter(shoesDbList);
                        mRecyclerView.setAdapter(adapter);

                        break;

                }

            }
        } ;
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
        String results= "select * from shoes"+" LIMIT "+0+","+10;
        params.addBodyParameter("uname",results);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    SharedPreferences pref=getActivity().getSharedPreferences("select_result", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();
                    boolean can=pref.getBoolean("can",false);
                    if (can) {
                        result = pref.getString("result", "");
                        editor.remove("can");
                        editor.putBoolean("can",false);
                        editor.apply();
                    }
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

    private void returnload(int load_returnstart) {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWSS:

                        adapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();

                }
            }
        };



        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        SharedPreferences pref=getActivity().getSharedPreferences("select_result", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        String resultss= pref.getString("sql_result","");
        editor.apply();
        String results=resultss+" LIMIT "+load_returnstart+","+"10";
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
                    msg.what = WHAT_NEWSS;
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

    private void load(int load_start) {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWSS:

                        adapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();

                }
            }
        };


        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        String results= "select * from shoes"+" LIMIT "+load_start+","+"10";
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
                    msg.what = WHAT_NEWSS;
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











/*
package com.shmily.tjz.swap.Fragment;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import com.liuguangqiang.cookie.CookieBar;
import com.liuguangqiang.cookie.OnActionClickListener;
import com.shmily.tjz.swap.Adapter.ShoesAdapter;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.SearchActivity;
import com.shmily.tjz.swap.SelectActivity;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;


*/
/**
 * Created by Shmily_Z on 2017/5/2.
 *//*


public class MainFragment extends Fragment {
    private LoadingLayout loadingLayout;
    private View rootView;
    private XRecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    FloatingToolbar mFloatingToolbar;
    private List<Shoes> shoesList =new ArrayList<>();
    private List<Shoes> ceshi =new ArrayList<>();
    public ShoesAdapter adapter;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    final int WHAT_NEWSS = 2 ;
    private LocalBroadcastManager localBroadcastManger;

    int start=10;
    int return_start=10;
    boolean load=true;
    boolean return_load=false;
    private ProgressDialog pDialog ;
    String username;

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==getActivity().RESULT_OK)
                {
                    return_start=10;
                    load=false;
                    return_load=true;
                    init();
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.main_fragment, container, false);

        //        过场动画
        Fade fade = new Fade();
        fade.setDuration(700L);
        getActivity().getWindow().setEnterTransition(fade);
        setHasOptionsMenu(true);
        initView();
        new CookieBar.Builder(getActivity())
                .setTitle("欢迎您")
                .setMessage(username)
                .setBackgroundColor(R.color.colorPrimary)
                .setAction("确认", new OnActionClickListener() {
                    @Override
                    public void onClick() {

                    }
                })
                .show();
        loadnet();

        return rootView;
    }

    private void loadnet() {
        loadingLayout=(LoadingLayout)rootView.findViewById(R.id.main_fragment_load_layout);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        //获取系统的连接服务。
         NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //获取网络的连接情况。
        if (networkInfo != null && networkInfo.isAvailable()) {
          */
/*  if (networkInfo.getType()==connectivityManager.TYPE_WIFI){
                Toast.makeText(A.this,"网络已启动啦(WIFI)",Toast.LENGTH_SHORT).show();
            }else if (networkInfo.getType()==connectivityManager.TYPE_MOBILE) {
                Toast.makeText(A.this,"网络已启动啦(3G)",Toast.LENGTH_SHORT).show();

            }*//*

            loadingLayout.setStatus(LoadingLayout.Success);//加载成功


        } else {
            loadingLayout.setStatus(LoadingLayout.No_Network);//无网络

        }
        loadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                //获取系统的连接服务。
                 NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                //获取网络的连接情况。
                if (networkInfo != null && networkInfo.isAvailable()) {
          */
/*  if (networkInfo.getType()==connectivityManager.TYPE_WIFI){
                Toast.makeText(A.this,"网络已启动啦(WIFI)",Toast.LENGTH_SHORT).show();
            }else if (networkInfo.getType()==connectivityManager.TYPE_MOBILE) {
                Toast.makeText(A.this,"网络已启动啦(3G)",Toast.LENGTH_SHORT).show();

            }*//*

                    loadingLayout.setStatus(LoadingLayout.Success);//加载成功
                    init();
                    Intent intent=new Intent("com.shmily.tjz.swap.LOCAL_SPECIAL");
                    localBroadcastManger=LocalBroadcastManager.getInstance(getActivity());
                    localBroadcastManger.sendBroadcast(intent);

                } else {
                    Snackbar.make(v,"改善网络环境后再试。",Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });
    }

    private void initView() {
        SharedPreferences prefs=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=prefs.edit();
        boolean isGuideLoaded=prefs.getBoolean("denglu",false);
        username=prefs.getString("username",null);



        */
/*mDrawerLayout= (DrawerLayout) rootView.findViewById(R.id.activity_main);
       NavigationView navView= (NavigationView) rootView.findViewById(R.id.nav_view);
        View headerLayout = navView.inflateHeaderView(R.layout.nav_header);
        TextView name= (TextView) headerLayout.findViewById(R.id.username);
        name.setText(username);*//*

        final FloatingActionButton fab= (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFloatingToolbar= (FloatingToolbar) rootView.findViewById(R.id.floatingToolbar);
        mFloatingToolbar.attachFab(fab);
      */
/*  swipeRefresh= (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshShoes();
            }


        });
*//*


        */
/*swipeRefresh= (TwinklingRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setEnableOverScroll(true);
        swipeRefresh.setHeaderView(new ProgressLayout(getActivity()));
        swipeRefresh.setBottomView(new LoadingView(getActivity()));
        swipeRefresh.setFloatRefresh(true);
        swipeRefresh.setOnRefreshListener(new RefreshListenerAdapter() {

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.finishRefreshing();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.finishLoadmore();
                    }
                },2000);
            }
        });*//*


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           */
/*     Intent intent=new Intent(MainActivity.this,SelectActivity.class);
                startActivity(intent);*//*

                mFloatingToolbar.show();


            }
        });
      */
/*  ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.gengduo);
            actionBar.setTitle("   ");
        }*//*

//        navView.setCheckedItem(R.id.nav_me);
     */
/*   navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_setting:
                        editor.remove("username");
                        editor.remove("denglu");
                        editor.commit();
                        Intent intent=new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case R.id.nav_theme:
                        Intent intent1=new Intent(getActivity(),LocationActivity.class);
                        startActivity(intent1);
//                    在这里编写逻辑性的东西。
                }
                return true;
            }
        });*//*

        init();




        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_shaixuan:
                                Intent intent=new Intent(getActivity(),SelectActivity.class);

                                startActivityForResult(intent, 1,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        break;
                    case R.id.menu_sousuo:
                        Intent intent1=new Intent(getActivity(),SearchActivity.class);
                        getActivity().startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        break;
                    case R.id.menu_xihuan:
                        mFloatingToolbar.hide();
                        break;
                    default:
                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {


            }
        });
    }
*/
/*

    public void refreshShoes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    //                    runOnUiThread()方法的将线程切换回主线程，因为我们知道我们无法再副线程里面进行ui的变化，所以我们用这个方法。
                    @Override
                    public void run() {
                        init();
                        adapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
//                        告诉它刷新结束，收回进度条。
                    }
                });
            }
        }).start();

    }
*//*


    public void init() {




        mRecyclerView = (XRecyclerView)rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        init();
                        mRecyclerView.refreshComplete();
                    }

                }, 3000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        if (load) {
                            load(start);
                            start = start + 10;

                        }
                        if (return_load){
                            returnload(return_start);
                            return_start=return_start+10;

                        }
                        mRecyclerView.loadMoreComplete();
                    }

                }, 1000);            //refresh data here

            }
        });
    */
/*    handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:

                     *//*
*/
/*   GridLayoutManager layoutManger=new GridLayoutManager(getActivity(),2);
//        StaggeredGridLayoutManager layoutManger=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
//        瀑布。
                        mRecyclerView.setLayoutManager(layoutManger);
                        mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
                        mRecyclerView.setRefreshProgressStyle(2);
                        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
*//*
*/
/*

                        *//*
*/
/*SqlListResult sqlListResult=new SqlListResult();
                        sqlListResult.getList(0,5,"select * from shoes");
                         new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(210);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    //                    runOnUiThread()方法的将线程切换回主线程，因为我们知道我们无法再副线程里面进行ui的变化，所以我们用这个方法。
                                    @Override
                                    public void run() {

                                        SharedPreferences prefs = getActivity().getSharedPreferences("SqlListResult", Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = prefs.edit();
                                        String Aa = prefs.getString("sqllistresult", null);
                                        editor.commit();
                                        List<Shoes> sql= null;
                                        try {
                                            JSONObject jsonobject=new JSONObject(Aa);
                                            JSONArray shoesArray=jsonobject.getJSONArray("result");
                                            Gson gson=new Gson();
                                            sql = gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        adapter=new ShoesAdapter(sql);
                                        mRecyclerView.setAdapter(adapter);

//                        告诉它刷新结束，收回进度条。
                                    }
                                });
                            }
                        }).start();*//*
*/
/*

                      *//*
*/
/*  adapter=new ShoesAdapter(shoesDbList);
                        mRecyclerView.setAdapter(adapter);
*//*
*/
/*
                        break;

                }

            }
        } ;*//*

       */
/* shoesDbList.clear();
        for (int i=0;i<50;i++){
//这个Random表达的是随机数，index等于fruits的数组的长度，这里random从0开始的，
// 然后index等于random的随机数，然后fruitList加入fruit【】的随机。for语句则表示来50个。
            Random random=new Random();
            int index=random.nextInt(fruits.length);
            shoesDbList.add(fruits[index]);
        }*//*

        shoesList.clear();
        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        String results= "select * from shoes"+" LIMIT "+0+","+10;
        params.addBodyParameter("uname",results);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    SharedPreferences pref=getActivity().getSharedPreferences("select_result", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();
                    boolean can=pref.getBoolean("can",false);
                    if (can) {
                        result = pref.getString("result", "");
                        editor.remove("can");
                        editor.putBoolean("can",false);
                        editor.apply();
                    }
                    JSONObject jsonobject=new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    List<Shoes> shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());



                    GridLayoutManager layoutManger=new GridLayoutManager(getActivity(),2);
//        StaggeredGridLayoutManager layoutManger=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
//        瀑布。
                    mRecyclerView.setLayoutManager(layoutManger);
                    mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
                    mRecyclerView.setRefreshProgressStyle(2);
                    mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
                    adapter=new ShoesAdapter(shoesList);
                    mRecyclerView.setAdapter(adapter);

                  */
/*  Message msg = handler.obtainMessage() ;
                    // 设置消息内容（可选）
                    // 设置消息类型
                    msg.what = WHAT_NEWS;
                    // 发送消息
                    handler.sendMessage(msg) ;*//*

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

    private void returnload(int load_returnstart) {

   */
/*     handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWSS:

                      *//*
*/
/*  adapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();*//*
*/
/*

                }
            }
        };*//*




        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        SharedPreferences pref=getActivity().getSharedPreferences("select_result", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        String resultss= pref.getString("sql_result","");
        editor.remove("sql_result");
        editor.apply();
        String results=resultss+" LIMIT "+load_returnstart+","+"10";
        params.addBodyParameter("uname",results);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonobject=new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    List<Shoes> shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Shoes>>(){}.getType());

                      adapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();
                */
/*    Message msg = handler.obtainMessage() ;
                    // 设置消息内容（可选）
                    // 设置消息类型
                    msg.what = WHAT_NEWSS;
                    // 发送消息
                    handler.sendMessage(msg) ;*//*

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

    private void load(int load_start) {

      */
/*  handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWSS:

                          *//*
*/
/*  adapter.notifyDataSetChanged();
                            mRecyclerView.loadMoreComplete();*//*
*/
/*

                }
            }
        };
*//*


        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
        String results= "select * from shoes"+" LIMIT "+load_start+","+"10";
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

                        shoesList.add(shoes);

                    }
                    adapter.notifyDataSetChanged();
                    mRecyclerView.loadMoreComplete();
                 */
/*   Message msg = handler.obtainMessage() ;
                    // 设置消息内容（可选）
                    // 设置消息类型
                    msg.what = WHAT_NEWSS;
                    // 发送消息
                    handler.sendMessage(msg) ;*//*

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
*/

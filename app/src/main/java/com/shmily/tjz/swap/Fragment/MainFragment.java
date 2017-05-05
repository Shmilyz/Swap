package com.shmily.tjz.swap.Fragment;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liuguangqiang.cookie.CookieBar;
import com.liuguangqiang.cookie.OnActionClickListener;
import com.shmily.tjz.swap.Adapter.ShoesAdapter;
import com.shmily.tjz.swap.Db.ShoesDb;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.SelectActivity;
import com.shmily.tjz.swap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/2.
 */

public class MainFragment extends Fragment {
    private View rootView;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefresh;
    FloatingToolbar mFloatingToolbar;
    RecyclerView recyclerView;
    private List<ShoesDb> shoesDbList =new ArrayList<>();
    private ShoesAdapter adapter;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    private ProgressDialog pDialog ;

    String username;
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
        return rootView;
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
        swipeRefresh= (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }


        });

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
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:
                        RecyclerView recyclerView= (RecyclerView) rootView.findViewById(R.id.recycler_view);
                        GridLayoutManager layoutManger=new GridLayoutManager(getActivity(),2);
//        StaggeredGridLayoutManager layoutManger=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
//        瀑布。
                        recyclerView.setLayoutManager(layoutManger);
                        adapter=new ShoesAdapter(shoesDbList);
                        recyclerView.setAdapter(adapter);
                        mFloatingToolbar.attachRecyclerView(recyclerView);
                        break;

                }

            }
        } ;



        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_shaixuan:
                                Intent intent=new Intent(getActivity(),SelectActivity.class);
                                getActivity().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        break;
                    default:
                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {


            }
        });
    }

    private void refreshFruits() {
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
                        swipeRefresh.setRefreshing(false);
//                        告诉它刷新结束，收回进度条。
                    }
                });
            }
        }).start();

    }
    private void init() {
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
        String results= "select * from shoes";
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
                            ShoesDb shoesDb =new ShoesDb(shoes.getStyle(),shoes.getPicture());
                        shoesDbList.add(shoesDb);

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
 }

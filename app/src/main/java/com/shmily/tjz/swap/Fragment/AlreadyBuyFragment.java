package com.shmily.tjz.swap.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.AlreadBuyAdapter;
import com.shmily.tjz.swap.Adapter.CollectAdapter;
import com.shmily.tjz.swap.Gson.AlreadBuy;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.Xutils;
import com.weavey.loading.lib.LoadingLayout;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.ListPopupWindow.MATCH_PARENT;

/**
 * Created by Shmily_Z on 2017/6/26.
 */

public class AlreadyBuyFragment extends Fragment {

    private View rootview;
    private SwipeMenuRecyclerView menu_recy_view;
    private String  username;
    private List<AlreadBuy> shoesList =new ArrayList<>();
    private AlreadBuyAdapter adapter;
    private LoadingLayout collect_fragment_load_layout;
    int width;
    private Xutils xutils;
    private CircleImageView collect_headview;
    private TextView collect_username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.collect_fragment, container, false);
        menu_recy_view= (SwipeMenuRecyclerView) rootview.findViewById(R.id.menu_recy_view);
        collect_fragment_load_layout= (LoadingLayout) rootview.findViewById(R.id.collect_fragment_load_layout);
        collect_headview= (CircleImageView) rootview.findViewById(R.id.collect_headview);
        collect_username= (TextView) rootview.findViewById(R.id.collect_username);
        xutils=Xutils.getInstance();

        SharedPreferences prefs=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=prefs.edit();
        username=prefs.getString("username",null);
        collect_fragment_load_layout.setStatus(LoadingLayout.Loading);
        String url="http://www.shmilyz.com/headimage/"+username+".jpg";

        Glide.with(getActivity())
                .load(url)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(collect_headview);
        collect_username.setText(username);
        width = getResources().getDimensionPixelSize(R.dimen.item_height);

        getcollect();
        return rootview;

    }

    private void getcollect() {

        Map<String, String> maps=new HashMap<String, String>();
        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        String sql="select * from shoes,buy where shoes.id=buy.shoesid and buy.username='"+username+"';";
        Log.i("collect_result",sql);
        maps.put("uname",sql);
        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject jsonobject=new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    shoesList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<AlreadBuy>>(){}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                recyadapter();
            }
        });
    }

    private void recyadapter() {


        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        menu_recy_view.setLayoutManager(layoutManager);
        adapter=new AlreadBuyAdapter(shoesList);
        menu_recy_view.setAdapter(adapter);


        collect_fragment_load_layout.setStatus(LoadingLayout.Success);//加载成功




    }


}

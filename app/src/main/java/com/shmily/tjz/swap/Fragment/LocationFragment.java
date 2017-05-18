package com.shmily.tjz.swap.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liuguangqiang.cookie.CookieBar;
import com.liuguangqiang.cookie.OnActionClickListener;
import com.shmily.tjz.swap.Db.ShoesDb;
import com.shmily.tjz.swap.Gson.Shoes;
import com.shmily.tjz.swap.Adapter.LocationAdapter;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.Position;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

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

public class LocationFragment extends Fragment {
    private View rootView;
    private List<ShoesDb> shoesDbList =new ArrayList<>();
    private  TextView text;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.location_fragment, container, false);

         text= (TextView) rootView.findViewById(R.id.texta);
        setHasOptionsMenu(true);
        init();
        Position Position = new Position();
        Position.getLocation(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    //                    runOnUiThread()方法的将线程切换回主线程，因为我们知道我们无法再副线程里面进行ui的变化，所以我们用这个方法。
                    @Override
                    public void run() {
                        SharedPreferences prefs = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = prefs.edit();
                        String location = prefs.getString("City", null);
                        editor.clear();
                        editor.commit();
                        new CookieBar.Builder(getActivity())
                                .setTitle("您的定位是")
                                .setMessage(location)
                                .setBackgroundColor(R.color.colorPrimary)
                                .setAction("确认", new OnActionClickListener() {
                                    @Override
                                    public void onClick() {
                                    }
                                })
                                .show();

                        DiscreteScrollView scrollView = (DiscreteScrollView) rootView.findViewById(R.id.picker);
                        scrollView.setAdapter(new LocationAdapter(shoesDbList));
                        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                                .setMinScale(0.8f)
                                .build());
                        scrollView.setOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
                            @Override
                            public void onCurrentItemChanged(@NonNull RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                                onItemChanged(shoesDbList.get(adapterPosition));
                            }
                        });
                    }
                });
            }
        }).start();

        return rootView;
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



    private void onItemChanged(ShoesDb shoesDb) {
             text.setText(shoesDb.getName());
    }
}

package com.shmily.tjz.swap.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.FriendsAdapter;
import com.shmily.tjz.swap.Db.Contacts;
import com.shmily.tjz.swap.Gson.Friends;
import com.shmily.tjz.swap.Gson.NumberResult;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.ReadContacts;
import com.shmily.tjz.swap.Utils.Xutils;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shmily_Z on 2017/6/7.
 */

public class FriendsFragment extends Fragment {
private RelativeLayout friends_toast;
    private RecyclerView friends_recy;
    private View rootView;
    Xutils xutils=Xutils.getInstance();
    private List<Friends> friendsList = new ArrayList<>();
    private List<Contacts> resultlist =new ArrayList<>();
    private List<NumberResult> return_resultlist = new ArrayList<>();
    private LoadingLayout friends_fragment_load_layout;
    private String username;
    private FriendsAdapter adapter;
    private SwipeRefreshLayout swipRefresh;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.friends_fragment, container, false);
        friends_recy= (RecyclerView) rootView.findViewById(R.id.friends_recy);
        swipRefresh= (SwipeRefreshLayout) rootView.findViewById(R.id.swipRefresh);
        friends_fragment_load_layout= (LoadingLayout) rootView.findViewById(R.id.friends_fragment_load_layout);
        friends_fragment_load_layout.setStatus(LoadingLayout.Loading);
        SharedPreferences prefs=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=prefs.edit();

        username=prefs.getString("username"," ");

        findnumber();


        swipRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findnumber();
                adapter.notifyDataSetChanged();
                swipRefresh.setRefreshing(false);

            }
        });
        return rootView;
    }



    private void findnumber() {



        ReadContacts readcontacts=new ReadContacts();
        resultlist=readcontacts.getContacts();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject tmpObj = null;
        int count = resultlist.size();
        for(int i = 0; i < count; i++)
        {

            try {
                tmpObj = new JSONObject();
                tmpObj.put("name" , resultlist.get(i).getName());

                tmpObj.put("number", resultlist.get(i).getNumber());

                jsonArray.put(tmpObj);
                tmpObj = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        String personInfos = jsonArray.toString();
        Map<String, String> maps=new HashMap<String, String>();
        String url="http://www.shmilyz.com/ForAndroidHttp/contacts.action";
        maps.put("uname",personInfos);

        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {

                try {
                    Log.i("result",result);

                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");

                    Gson gson=new Gson();
                    return_resultlist=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<NumberResult>>(){}.getType());

                } catch (JSONException e) {
                    e.printStackTrace();
                    NumberResult num=new NumberResult();
                    num.setUsername("");
                    num.setId(1);
                    num.setPhone("15522902120");
                    num.setName("自己");
                    return_resultlist.add(num);
                }

                selectfriend();

                friends_fragment_load_layout.setStatus(LoadingLayout.Success);//加载成功

            }
        });



    }

    private void selectfriend() {
        StringBuilder stringbuilder=new StringBuilder();
        stringbuilder.append("select * from friends where ");
        if (return_resultlist.size()>0){
            for (int i=0;i<return_resultlist.size()-1;i++){
                stringbuilder.append("username='").append(return_resultlist.get(i).getUsername()).append("'").append(" or ");
            }
            stringbuilder.append("username='").append(return_resultlist.get(return_resultlist.size()-1).getUsername()).append("'");
        }
        stringbuilder.append(" or username='").append(username).append("'").append("  Order By userdate Desc");
        String result=String.valueOf(stringbuilder);
        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        Map<String, String> maps=new HashMap<String, String>();
        maps.put("uname",result);
        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    friendsList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Friends>>(){}.getType());
                    



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
                friends_recy.setLayoutManager(layoutManager);
                adapter=new FriendsAdapter(friendsList);
                friends_recy.setAdapter(adapter);


            }

        });


    }


}

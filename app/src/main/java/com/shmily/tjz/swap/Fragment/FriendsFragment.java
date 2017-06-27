package com.shmily.tjz.swap.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.FriendsAdapter;
import com.shmily.tjz.swap.Db.Contacts;
import com.shmily.tjz.swap.Gson.Friends;
import com.shmily.tjz.swap.Gson.NumberResult;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.MyApplication;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Shmily_Z on 2017/6/7.
 */

public class FriendsFragment extends Fragment {
    private RelativeLayout friends_toast;
    private RecyclerView friends_recy;
    private View rootView;
    Xutils xutils=Xutils.getInstance();
    private List<Friends> friendsList = new ArrayList<>();
    private List<Friends> friendnamesList = new ArrayList<>();

    private List<Contacts> resultlist =new ArrayList<>();
    private List<NumberResult> return_resultlist = new ArrayList<>();
    private LoadingLayout friends_fragment_load_layout;
    private String username;
    private TextView set_username;
    private FriendsAdapter adapter;
    private SwipeRefreshLayout swipRefresh;
    private CircleImageView friends_headview;

    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManger;
    private  String headimage_url;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.friends_fragment, container, false);
        SharedPreferences prefs=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        username=prefs.getString("username"," ");

        localBroadcastManger=LocalBroadcastManager.getInstance(getActivity());
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.shmily.tjz.swap.LOCAL_SPECIAL");
        localReceiver=new LocalReceiver();
        localBroadcastManger.registerReceiver(localReceiver,intentFilter);
        friends_recy= (RecyclerView) rootView.findViewById(R.id.friends_recy);
        swipRefresh= (SwipeRefreshLayout) rootView.findViewById(R.id.swipRefresh);
        friends_fragment_load_layout= (LoadingLayout) rootView.findViewById(R.id.friends_fragment_load_layout);

        friends_fragment_load_layout.setStatus(LoadingLayout.Loading);
        set_username= (TextView) rootView.findViewById(R.id.set_username);
        friends_headview= (CircleImageView) rootView.findViewById(R.id.friends_headview);


        set_username.setText(username);
        headimage_url="http://www.shmilyz.com/headimage/"+username+".jpg";

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

        Glide.with(MyApplication.getContext()).load(headimage_url).into(friends_headview);

        ReadContacts readcontacts=new ReadContacts();

        resultlist.clear();
        resultlist=readcontacts.getContacts();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject tmpObj = null;
        int count = resultlist.size();
        Log.i("tianjunzhe",String.valueOf(count));
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

                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");

                    Gson gson=new Gson();
                    return_resultlist.clear();
                    return_resultlist=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<NumberResult>>(){}.getType());
                    Log.i("zy",String.valueOf(shoesArray));
                    selectfriend();
                } catch (JSONException e) {

                    e.printStackTrace();

                }



                friends_fragment_load_layout.setStatus(LoadingLayout.Success);//加载成功

            }
        });



    }

    private void selectfriend() {

        friendnamesList.clear();
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
                    for(Friends friends:friendsList){
                        Friends friends1 = new Friends();
                        friends1.setId(friends.getId());
                        friends1.setShoesname(friends.getShoesname());
                        friends1.setDiscuss(friends.getDiscuss());
                        friends1.setShoesid(friends.getShoesid());
                        friends1.setShoesurl(friends.getShoesurl());
                        friends1.setType(friends.getType());
                        friends1.setUserdate(friends.getUserdate());
                        for(NumberResult num:return_resultlist){

                            if (num.getUsername().equals(friends.getUsername())){
                                friends1.setUsername(num.getName());


                                break;
                            }

                        }
                        friendnamesList.add(friends1);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
                friends_recy.setLayoutManager(layoutManager);
                adapter=new FriendsAdapter(friendnamesList);
                friends_recy.setAdapter(adapter);


            }

        });


    }
    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            friends_fragment_load_layout.setStatus(LoadingLayout.Success);//加载成功


            findnumber();
        }
    }


}
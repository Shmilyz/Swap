package com.shmily.tjz.swap.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.DiscussAdapter;
import com.shmily.tjz.swap.Adapter.FriendsAdapter;
import com.shmily.tjz.swap.Gson.Discuss;
import com.shmily.tjz.swap.Gson.Friends;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.Xutils;

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
private FriendsAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.friends_fragment, container, false);
        friends_toast= (RelativeLayout) rootView.findViewById(R.id.friends_toast);
        friends_recy= (RecyclerView) rootView.findViewById(R.id.friends_recy);
        selectfriend();
        return rootView;
    }

    private void selectfriend() {

        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        Map<String, String> maps=new HashMap<String, String>();
        maps.put("uname","select * from friends Order By userdate Desc" );
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

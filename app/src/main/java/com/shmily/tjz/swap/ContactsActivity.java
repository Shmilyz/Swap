package com.shmily.tjz.swap;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.FriendShowAdapter;
import com.shmily.tjz.swap.Db.Contacts;
import com.shmily.tjz.swap.Gson.NumberResult;
import com.shmily.tjz.swap.LitePal.FriendsLite;
import com.shmily.tjz.swap.Utils.GridSpacingItemDecoration;
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

public class ContactsActivity extends AppCompatActivity {
    private List<Contacts> resultlist =new ArrayList<>();
    private RecyclerView activity_contacts_recy;
    private List<NumberResult> return_resultlist = new ArrayList<>();
    private  FriendShowAdapter adapter;
private LoadingLayout activity_contacts_load_layout;
    private Button activity_contacts_button;
    Contacts contacts=new Contacts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        activity_contacts_load_layout= (LoadingLayout) findViewById(R.id.activity_contacts_load_layout);
        activity_contacts_load_layout.setStatus(LoadingLayout.Loading);
        activity_contacts_button= (Button) findViewById(R.id.activity_contacts_button);
        activity_contacts_recy= (RecyclerView) findViewById(R.id.activity_contacts_recy);
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
        Log.i("哈",personInfos);
        Xutils xutils=Xutils.getInstance();
        Map<String, String> maps=new HashMap<String, String>();
        String url="http://www.shmilyz.com/ForAndroidHttp/contacts.action";
        maps.put("uname",personInfos);

        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {

                try {
                    Log.i("a","a");

                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");

                    Gson gson=new Gson();
                    return_resultlist=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<NumberResult>>(){}.getType());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(getWindow().getDecorView(),"您好没有好友在用,快通知好友一起使用吧",Snackbar.LENGTH_SHORT)
                            .show();
                }
                GridLayoutManager layoutManger=new GridLayoutManager(ContactsActivity.this,3);
                activity_contacts_recy.setLayoutManager(layoutManger);
                int spanCount = 3;
                int spacing = 2;
                boolean includeEdge = false;
                activity_contacts_recy.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                adapter=new FriendShowAdapter(return_resultlist);
                activity_contacts_recy.setAdapter(adapter);

                activity_contacts_load_layout.setStatus(LoadingLayout.Success);//加载成功
                activity_contacts_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(ContactsActivity.this,ContactsActivity.class);
                        startActivity(intent);
                        ContactsActivity.this.finish();
                    }
                });


            }
        });



    }


}

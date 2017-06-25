package com.shmily.tjz.swap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.CollectAdapter;
import com.shmily.tjz.swap.Adapter.PositionsAdapter;
import com.shmily.tjz.swap.Gson.Positions;
import com.shmily.tjz.swap.Utils.Xutils;
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

import static android.widget.ListPopupWindow.MATCH_PARENT;

public class ShowPositionsActivity extends AppCompatActivity {
    private SwipeMenuRecyclerView show_positions_menu_recy_view;
    private TextView show_positions_add;
    private List<Positions> positionsList =new ArrayList<>();
    private PositionsAdapter adapter;


    int width;
    private Xutils xutils;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_positions);
        show_positions_add= (TextView) findViewById(R.id.show_positions_add);
        show_positions_menu_recy_view= (SwipeMenuRecyclerView) findViewById(R.id.show_positions_menu_recy_view);
        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
        username=prefs.getString("username",null);
        xutils=Xutils.getInstance();
        width = getResources().getDimensionPixelSize(R.dimen.item_height);
        show_positions_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(ShowPositionsActivity.this,WritePositionActivity.class);
                startActivity(intent);
                ShowPositionsActivity.this.finish();

            }
        });
        getcollect();
    }

    private void getcollect() {

        Map<String, String> maps=new HashMap<String, String>();
        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        String sql="SELECT * FROM positions WHERE username='"+username+"';";
        Log.i("collect_result",sql);
        maps.put("uname",sql);
        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject jsonobject=new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    Gson gson=new Gson();
                    positionsList=gson.fromJson(String.valueOf(shoesArray),new TypeToken<List<Positions>>(){}.getType());

                    recy();
                } catch (JSONException e) {

                    Positions pos=new Positions();
                    pos.setName("S小助手");
                    pos.setNumber(" ");
                    pos.setPositionss("赶紧添加您的第一条地址吧!");
                    pos.setId(1);
                    pos.setUsername("");
                    positionsList.add(pos);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(ShowPositionsActivity.this);
                    show_positions_menu_recy_view.setLayoutManager(layoutManager);
                    adapter=new PositionsAdapter(positionsList);
                    show_positions_menu_recy_view.setAdapter(adapter);
                    e.printStackTrace();

                }


            }
        });
    }

    private void recy() {


        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(ShowPositionsActivity.this)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除") // 文字。
                        .setTextColor(Color.WHITE) // 文字的颜色。
                        .setWidth(width) // 菜单宽度。
                        .setHeight(MATCH_PARENT); // 菜单高度。
                rightMenu.addMenuItem(deleteItem); // 在右侧添加一个菜单。

            }
        };
        show_positions_menu_recy_view.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                closeable.smoothCloseMenu();// 关闭被点击的菜单。

                if (menuPosition == 0) {

                    String url="http://www.shmilyz.com/ForAndroidHttp/update.action";
                    Map<String, String> map=new HashMap<String, String>();
                    String  setcollect="DELETE FROM positions WHERE "+" id="+positionsList.get(adapterPosition).getId();
                    Log.i("setcollecta",setcollect);
                    map.put("uname",setcollect);
                    xutils.post(url, map, new Xutils.XCallBack() {
                        @Override
                        public void onResponse(String result) {

                        }
                    });
                    positionsList.remove(adapterPosition);
                    adapter.notifyItemRemoved(adapterPosition);
                }
            }
        });

        show_positions_menu_recy_view.setSwipeMenuCreator(swipeMenuCreator);

        LinearLayoutManager layoutManager=new LinearLayoutManager(ShowPositionsActivity.this);
        show_positions_menu_recy_view.setLayoutManager(layoutManager);
        adapter=new PositionsAdapter(positionsList);
        show_positions_menu_recy_view.setAdapter(adapter);

    }
}

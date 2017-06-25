package com.shmily.tjz.swap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shmily.tjz.swap.Adapter.PositionsAdapter;
import com.shmily.tjz.swap.Gson.Positions;
import com.shmily.tjz.swap.Utils.Xutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyActivity extends AppCompatActivity {
    private TextView buy_user_shr,buy_user_number,buy_user_position;
    private Xutils xutils;
    private String username;
    private List<Positions> positionsList =new ArrayList<>();
    private LinearLayout buy_case_lin;
    private CircleImageView buy_info_headimage;
    private TextView buy_info_username,buy_info_shoesname,buy_info_price,buy_info_shoesmodle,activity_buy_price;
    private Button activity_buy_sure;
    private EditText buy_info_leave;
    private ImageView buy_info_shoesimage;
    private String shoesid;

    @Override
    protected void onRestart() {

        SharedPreferences.Editor editor=getSharedPreferences("buy_positions_show",MODE_PRIVATE).edit();
        SharedPreferences pref=getSharedPreferences("buy_positions_show",MODE_PRIVATE);
        if (pref.getBoolean("boolean",false)) {
            buy_user_shr.setText(pref.getString("name", "S小助手"));
            buy_user_number.setText(pref.getString("number", ""));
            buy_user_position.setText(pref.getString("position", "选择一个地址作为您的收货地址吧"));
        }
        editor.putBoolean("boolean",false);
        editor.apply();
        editor.clear();
        super.onRestart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        xutils=Xutils.getInstance();
        buy_user_shr= (TextView) findViewById(R.id.buy_user_shr);
        buy_user_number= (TextView) findViewById(R.id.buy_user_number);
        buy_user_position= (TextView) findViewById(R.id.buy_user_position);
        buy_case_lin= (LinearLayout) findViewById(R.id.buy_case_lin);

        buy_info_headimage= (CircleImageView) findViewById(R.id.buy_info_headimage);
        buy_info_username= (TextView) findViewById(R.id.buy_info_username);
        buy_info_shoesname= (TextView) findViewById(R.id.buy_info_shoesname);
        buy_info_price= (TextView) findViewById(R.id.buy_info_price);
        buy_info_shoesmodle= (TextView) findViewById(R.id.buy_info_shoesmodle);
        activity_buy_price= (TextView) findViewById(R.id.activity_buy_price);
        buy_info_shoesimage= (ImageView) findViewById(R.id.buy_info_shoesimage);
        activity_buy_sure= (Button) findViewById(R.id.activity_buy_sure);

        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
        username=prefs.getString("username",null);


        others();
        positions();
    }

    private void others() {

            Intent intent =getIntent();

        shoesid=intent.getStringExtra(ShoesActivity.SHOES_ID);
        Glide.with(BuyActivity.this)
                .load( intent.getStringExtra(ShoesActivity.SHOES_IMAGE_URL))
                .into(buy_info_shoesimage);
        String url="http://www.shmilyz.com/headimage/"+intent.getStringExtra(ShoesActivity.SHOES_USERNAME)+".jpg";
        Glide.with(BuyActivity.this)
                .load(url)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(buy_info_headimage);
        buy_info_username.setText(intent.getStringExtra(ShoesActivity.SHOES_USERNAME));
        buy_info_shoesname.setText(intent.getStringExtra(ShoesActivity.SHOES_BIAOTI));
        buy_info_price.setText("¥"+intent.getStringExtra(ShoesActivity.SHOES_PRICE));
        activity_buy_price.setText("¥"+intent.getStringExtra(ShoesActivity.SHOES_PRICE));
        buy_info_shoesmodle.setText(intent.getStringExtra(ShoesActivity.SHOES_SIZE));


    }

    private void positions() {
        buy_case_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(BuyActivity.this,ShowPositionsActivity.class);
                startActivity(intent);


            }
        });

        Map<String, String> maps=new HashMap<String, String>();
        String url="http://www.shmilyz.com/ForAndroidHttp/select.action";
        String sql="SELECT * FROM position WHERE username='"+username+"';";
        Log.i("collect_result",sql);
        maps.put("uname",sql);
        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject jsonobject=new JSONObject(result);
                    JSONArray shoesArray=jsonobject.getJSONArray("result");
                    JSONObject js=shoesArray.getJSONObject(0);
                    buy_user_shr.setText(js.get("name").toString());
                    buy_user_number.setText(js.get("number").toString());
                    buy_user_position.setText(js.get("positions").toString());
                } catch (JSONException e) {

                    buy_user_shr.setText("S小助手");

                    buy_user_position.setText("选择一个地址作为您的收货地址吧!");

                    e.printStackTrace();

                }


            }
        });

    }
}

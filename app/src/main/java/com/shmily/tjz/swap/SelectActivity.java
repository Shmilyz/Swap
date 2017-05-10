package com.shmily.tjz.swap;
/*
* SelextActivity测试使用全注解方法写。
* */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihidea.multilinechooselib.MultiLineChooseLayout;
import com.shmily.tjz.swap.Adapter.SearchHotAdapter;
import com.shmily.tjz.swap.Gson.Shoes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ezy.ui.view.RoundButton;

public class SelectActivity extends AppCompatActivity {
    @BindView(R.id.checkbox_nike)
    CheckBox checkboxNike;
    @BindView(R.id.checkbox_airjordan)
    CheckBox checkboxAirjordan;
    @BindView(R.id.checkbox_converse)
    CheckBox checkboxConverse;
    @BindView(R.id.checkbox_newbalance)
    CheckBox checkboxNewbalance;
    @BindView(R.id.checkbox_puma)
    CheckBox checkboxPuma;
    @BindView(R.id.checkbox_vans)
    CheckBox checkboxVans;
    @BindView(R.id.checkbox_basketball)
    CheckBox checkboxBasketball;
    @BindView(R.id.checkbox_running)
    CheckBox checkboxRunning;
    @BindView(R.id.checkbox_skateboar)
    CheckBox checkboxSkateboar;
    @BindView(R.id.clear_all)
    TextView clearAll;
    private List<String> brand = new ArrayList<>();
    private List<String> style = new ArrayList<>();
    private List<String> sex = new ArrayList<>();
    private List<String> no = new ArrayList<>();

    private List<String> mColorData = new ArrayList<>();
    private List<String> mColorData1 = new ArrayList<>();
    private MultiLineChooseLayout singleChoose, singleChoose1;
    private RoundButton button;
    private CrystalRangeSeekbar rangeSeekbar;
    private TextView tvMin, tvMax;
    private String select_result;
    final StringBuilder builder = new StringBuilder();
    private boolean select, select1, select2, select3;

    private Handler handler;
    final int WHAT_NEWS = 1 ;
    String results;
  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SelectActivity.this,MainActivity.class);
        startActivity(intent);

    }*/



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select);
        Explode explode = new Explode();
        explode.setDuration(300L);
        getWindow().setEnterTransition(explode);

        ButterKnife.bind(this);
        initView();

        singleChoose = (MultiLineChooseLayout) findViewById(R.id.flowLayout);
        singleChoose1 = (MultiLineChooseLayout) findViewById(R.id.flowLayout1);
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        button = (RoundButton) findViewById(R.id.Position);
        tvMin = (TextView) findViewById(R.id.left);
        tvMax = (TextView) findViewById(R.id.right);
        mColorData.add("男士");
        mColorData.add("女士");
        mColorData.add("中性");
        singleChoose.setList(mColorData);
        mColorData1.add("全新");
        mColorData1.add("二手");
        singleChoose1.setList(mColorData1);
// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {

            }
        });
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChoose.cancelAllSelectedItems();
                singleChoose1.cancelAllSelectedItems();
                checkboxNike.setChecked(false);
                checkboxAirjordan.setChecked(false);
                checkboxConverse.setChecked(false);
                checkboxNewbalance.setChecked(false);
                checkboxPuma.setChecked(false);
                checkboxVans.setChecked(false);
                checkboxBasketball.setChecked(false);
                checkboxRunning.setChecked(false);
                checkboxSkateboar.setChecked(false);

                   }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = false;
                select1 = false;
                select2 = false;
                select3 = false;
                builder.setLength(0);
                brand.clear();
                style.clear();
                builder.append("select * from shoes where ");
                checkbox();

            }
        });
    }

    private void initView() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:

                        SharedPreferences.Editor editor =getSharedPreferences("select_result",MODE_PRIVATE).edit();
                        editor.putString("result",select_result );
                        editor.putBoolean("can",true);
                        editor.putString("sql_result",results);
                        editor.apply();
                        setResult(RESULT_OK);
                        SelectActivity.this.finish();
                        break;

                }

            }
        };
    }

    private void checkbox() {
//        brand
        if (checkboxNike.isChecked() == true) {
            brand.add("nike");
        }
        if (checkboxAirjordan.isChecked() == true) {
            brand.add("aj");
        }
        if (checkboxConverse.isChecked() == true) {
            brand.add("converse");
        }
        if (checkboxNewbalance.isChecked() == true) {
            brand.add("newbalance");
        }
        if (checkboxPuma.isChecked() == true) {
            brand.add("puma");
        }
        if (checkboxVans.isChecked() == true) {
            brand.add("vans");
        }
        if (brand.size() > 0) {
            if (brand.size() == 1) {
                builder.append("brand='" + brand.get(0) + "'");
                select = true;
            } else {
                for (int i = 0; i < brand.size() - 1; i++) {

                    builder.append("brand='" + brand.get(i) + "'").append(" or ");


                }
                builder.append("brand='" + brand.get(brand.size() - 1) + "'");
                select = true;

            }
        }

//style
        if (select) {
            if (checkboxBasketball.isChecked() == true) {
                style.add("basketball");
            }
            if (checkboxRunning.isChecked() == true) {
                style.add("run");
            }
            if (checkboxSkateboar.isChecked() == true) {
                style.add("arder");
            }
            if (style.size() > 0) {
                builder.append(" and ");
                if (style.size() == 1) {
                    builder.append("style='" + style.get(0) + "'");
                    select1 = true;
                } else {
                    for (int i = 0; i < style.size() - 1; i++) {

                        builder.append("style='" + style.get(i) + "'").append(" or ");
                        select1 = true;


                    }
                    builder.append("style='" + style.get(style.size() - 1) + "'");
                }
            }
        } else {
            if (checkboxBasketball.isChecked() == true) {
                style.add("basketball");
            }
            if (checkboxRunning.isChecked() == true) {
                style.add("run");
            }
            if (checkboxSkateboar.isChecked() == true) {
                style.add("arder");
            }
            if (style.size() > 0) {
                if (style.size() == 1) {
                    builder.append("style='" + style.get(0) + "'");
                    select1 = true;

                } else {
                    for (int i = 0; i < style.size() - 1; i++) {

                        builder.append("style='" + style.get(i) + "'").append(" or ");
                        select1 = true;


                    }
                    builder.append("style='" + style.get(style.size() - 1) + "'");
                }
            }
        }
//        sex
        if (select || select1) {
            sex = singleChoose.getAllItemSelectedTextWithListArray();
            if (sex.size() > 0) {
                builder.append(" and ");

                if (sex.size() == 1) {

                    builder.append("sex='" + sex.get(0) + "'");
                    select2 = true;
                } else {

                    for (int i = 0; i < sex.size() - 1; i++) {

                        builder.append("sex='" + sex.get(i) + "'").append(" or ");
                        select2 = true;


                    }
                    builder.append("sex='" + sex.get(sex.size() - 1) + "'");
                }
            }
        } else {
            sex = singleChoose.getAllItemSelectedTextWithListArray();
            if (sex.size() > 0) {
                if (sex.size() == 1) {
                    builder.append("sex='" + sex.get(0) + "'");
                    select2 = true;
                } else {
                    for (int i = 0; i < sex.size() - 1; i++) {

                        builder.append("sex='" + sex.get(i) + "'").append(" or ");
                        select2 = true;


                    }
                    builder.append("sex='" + sex.get(sex.size() - 1) + "'");
                }
            }
        }
//no
        if (select || select1 || select2) {

            no = singleChoose1.getAllItemSelectedTextWithListArray();
            if (no.size() > 0) {
                builder.append(" and ");

                if (no.size() == 1) {

                    builder.append("no='" + no.get(0) + "'");
                    select3 = true;
                } else {

                    for (int i = 0; i < no.size() - 1; i++) {

                        builder.append("no='" + no.get(i) + "'").append(" or ");
                        select3 = true;


                    }
                    builder.append("no='" + no.get(no.size() - 1) + "'");
                }
            }

        } else {

            no = singleChoose1.getAllItemSelectedTextWithListArray();
            if (no.size() > 0) {

                if (no.size() == 1) {

                    builder.append("no='" + no.get(0) + "'");
                    select3 = true;
                } else {

                    for (int i = 0; i < no.size() - 1; i++) {

                        builder.append("no='" + no.get(i) + "'").append(" or ");
                        select3 = true;


                    }
                    builder.append("no='" + no.get(no.size() - 1) + "'");
                }
            }

        }
//        price
        if (select || select1 || select2 || select3) {
            builder.append(" and ");
            String min = tvMin.getText().toString().trim();
            String max = tvMax.getText().toString().trim();

            builder.append("price ").append(" between ").append(min).append(" and ").append(max);


        } else {
            String min = tvMin.getText().toString().trim();
            String max = tvMax.getText().toString().trim();

            builder.append("price ").append(" between ").append(min).append(" and ").append(max);
        }

        RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/select.action");
         results= String.valueOf(builder);
        String last_results=results+" LIMIT 0,10";
        params.addBodyParameter("uname",last_results);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess( String result) {

                select_result=result;
                Message msg = handler.obtainMessage();
                // 设置消息内容（可选）
                // 设置消息类型
                msg.what = WHAT_NEWS;
                // 发送消息
                handler.sendMessage(msg);

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

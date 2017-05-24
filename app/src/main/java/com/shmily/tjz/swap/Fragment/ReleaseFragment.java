package com.shmily.tjz.swap.Fragment;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;

import com.shmily.tjz.swap.A;
import com.shmily.tjz.swap.Adapter.PhotoShowAdapter;
import com.shmily.tjz.swap.MainActivity;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.ImageConfigGlideLoader;
import com.shmily.tjz.swap.Utils.NumberKeyboardView;
import com.shmily.tjz.swap.Utils.Position;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.picker.SinglePicker;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Shmily_Z on 2017/5/20.
 */

public class ReleaseFragment extends Fragment {
    private View rootView;
    private RelativeLayout camera;
    private RelativeLayout re_type,re_size,re_money,re_data,position_re;
    private TextView te_type,te_size,te_data,text_nest_title_limit,text_nest_desc_limit;
    private EditText te_money,text_nest_title,text_nest_desc;
    private int REQUEST_CODE=66;
    private String str = "";
    private TextView result,position;
    private DrawerLayout drawerLayout;

    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManger;
    private LoadingLayout loadingLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.release_fragment, container, false);
        position_re= (RelativeLayout) rootView.findViewById(R.id.position_re);
        drawerLayout= (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        camera= (RelativeLayout) rootView.findViewById(R.id.camera);
        re_type= (RelativeLayout) rootView.findViewById(R.id.type_nest);
        te_type= (TextView) rootView.findViewById(R.id.text_type_nest);
        re_size= (RelativeLayout) rootView.findViewById(R.id.size_nest);
        te_size= (TextView) rootView.findViewById(R.id.text_size_nest);
        te_money= (EditText) rootView.findViewById(R.id.text_money_nest);
        re_data= (RelativeLayout) rootView.findViewById(R.id.data_nest);
        te_data= (TextView) rootView.findViewById(R.id.data_type_nest);
        text_nest_title= (EditText) rootView.findViewById(R.id.text_nest_title);
        text_nest_title_limit= (TextView) rootView.findViewById(R.id.text_nest_title_limit);
        text_nest_desc= (EditText) rootView.findViewById(R.id.text_nest_desc);
        text_nest_desc_limit= (TextView) rootView.findViewById(R.id.text_nest_desc_limit);
        position= (TextView) rootView.findViewById(R.id.position);

        localBroadcastManger=LocalBroadcastManager.getInstance(getActivity());
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.shmily.tjz.swap.GET_LOCATION");
        localReceiver=new LocalReceiver();
        localBroadcastManger.registerReceiver(localReceiver,intentFilter);
        Position Position = new Position();
        Position.getLocation(getActivity());
        autoposition();
        releaseposition();

        releaseselect();
        releaselimit();
        releasepthoto();
        return rootView;

    }

    private void autoposition() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                String autoposition = prefs.getString("City", "自动定位失败");
                editor.clear();
                editor.commit();
                position.setText(autoposition);

            }
        }, 1500);


    }

    public void position(){
        SharedPreferences pref=getActivity().getSharedPreferences("Fragment_get_position",getActivity().MODE_PRIVATE);
        String city=pref.getString("City","");
        String county=pref.getString("County","");

        position.setText(city+ " "+county);
        SharedPreferences.Editor editor=pref.edit();
        editor.remove("City");
        editor.remove("County");
        editor.clear();
        editor.apply();
        editor.commit();
        /*
        *这里总怕实现不了移除。所以都用上了。
        * */
    }

    private void releaseposition() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        position_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);


                MainActivity main= (MainActivity) getActivity();
            }
        });
    }

    private void releaselimit() {

        text_nest_desc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode()==KeyEvent.KEYCODE_ENTER);
            }
        });
        text_nest_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int num=60-s.length();
                text_nest_desc_limit.setText(String.valueOf(num));

            }
        });



        text_nest_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode()==KeyEvent.KEYCODE_ENTER);
            }
        });
        text_nest_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

               int num=15-s.length();
                text_nest_title_limit.setText(String.valueOf(num));

            }
        });

    }

    private void releaseselect() {
        re_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance() ;
                new DatePickerDialog(
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                te_data.setText(
                                        String.format("%04d.%02d" , year , (month+1))
                                );                            }
                        } ,
                        c.get(Calendar.YEAR) ,
                        c.get(Calendar.MONTH)  ,
                        c.get(Calendar.DAY_OF_MONTH)
                ).show() ;

            }
        });



        re_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {

                    @Override
                    public boolean isOnlyTwo() {
                        return true;
                    }

                    @Override
                    public List<String> provideFirstData() {
                        ArrayList<String> firstList = new ArrayList<>();
                        firstList.add("运动");
                        firstList.add("休闲");
                        firstList.add("滑板");
                        firstList.add("篮球");
                        firstList.add("足球");
                        firstList.add("网球");
                        return firstList;
                    }

                    @Override
                    public List<String> provideSecondData(int firstIndex) {
                        ArrayList<String> secondList = new ArrayList<>();
                        secondList.add("运动");
                        secondList.add("休闲");
                        secondList.add("滑板");
                        secondList.add("篮球");
                        secondList.add("足球");
                        secondList.add("网球");
                        return secondList;
                    }

                    @Override
                    public List<String> provideThirdData(int firstIndex, int secondIndex) {
                        return null;
                    }

                };



                LinkagePicker picker = new LinkagePicker(getActivity(), provider);

                picker.setCanLoop(true);//不禁用循环
                picker.setTopBackgroundColor(0xFF384A56);
                picker.setTopHeight(50);
                picker.setTopLineColor(0xFF384A56);
                picker.setTopLineHeight(1);
                picker.setTitleText( "请选择" );
                picker.setTitleTextColor(0xFFFFFFFF);
                picker.setTitleTextSize(12);
                picker.setCancelTextColor(0xFFFFFFFF);
                picker.setCancelTextSize(13);
                picker.setSubmitTextColor(0xFF33B5E5);
                picker.setSubmitTextSize(13);
                picker.setSelectedTextColor(0xFFEE0000);
                picker.setUnSelectedTextColor(0xFF999999);
                LineConfig config = new LineConfig();
                config.setColor(0xFF384A56);//线颜色
                config.setAlpha(140);//线透明度
                config.setRatio((float) (1.0 / 8.0));//线比率
                picker.setLineConfig(config);
                picker.setBackgroundColor(0xFFFFFFFF);
                picker.setLabel("", "");
                picker.setSelectedIndex(0, 0);
                picker.setOnMoreItemPickListener(new OnMoreItemPickListener<String>() {

                    @Override
                    public void onItemPicked(String first, String second, String third) {
                        te_type.setText(first+"/"+second);
                    }
                });
                picker.show();




            }
        });

        re_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
                SinglePicker<String> picker = new SinglePicker<>(getActivity(),
                        isChinese ? new String[]{
                                "35", "35.5", "36", "36.5", "37", "37.5",
                                 "38", "38.5", "39", "39.5", "40","40.5","41","41.5","42","42.5","43","43.5","44","44.5","45","45.5","46","46.5","47"
                        } : new String[]{
                                "35", "35.5", "36", "36.5", "37", "37.5",
                                "38", "38.5", "39", "39.5", "40","40.5","41","41.5","42","42.5","43","43.5","44","44.5","45","45.5","46","46.5","47"
                        });

                picker.setCanLoop(true);//不禁用循环
                picker.setTopBackgroundColor(0xFF384A56);
                picker.setTopHeight(50);
                picker.setTopLineColor(0xFF384A56);
                picker.setTopLineHeight(1);
                picker.setTitleText(isChinese ? "请选择" : "Please pick");
                picker.setTitleTextColor(0xFFFFFFFF);
                picker.setTitleTextSize(12);
                picker.setCancelTextColor(0xFFFFFFFF);
                picker.setCancelTextSize(13);
                picker.setSubmitTextColor(0xFF33B5E5);
                picker.setSubmitTextSize(13);
                picker.setSelectedTextColor(0xFFEE0000);
                picker.setUnSelectedTextColor(0xFF999999);
                LineConfig config = new LineConfig();
                config.setColor(0xFF384A56);//线颜色
                config.setAlpha(140);//线透明度
                config.setRatio((float) (1.0 / 8.0));//线比率
                picker.setLineConfig(config);
                picker.setItemWidth(200);
                picker.setBackgroundColor(0xFFFFFFFF);
                picker.setSelectedIndex(0);
                //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
                picker.setOnItemPickListener(new OnItemPickListener<String>() {
                    @Override
                    public void onItemPicked(int index, String item) {
                        te_size.setText(item);
                    }
                });
                picker.show();





            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 获取选中的图片路径列表 Get Images Path List
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            Toast.makeText(getActivity(), pathList.get(0), Toast.LENGTH_SHORT).show();
            RecyclerView recyclerView= (RecyclerView) rootView.findViewById(R.id.show_photo_recy);
            LinearLayoutManager layoutManger =new LinearLayoutManager(getActivity());
            layoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManger);
            PhotoShowAdapter adapter=new PhotoShowAdapter(pathList);
            recyclerView.setAdapter(adapter);


        }
    }
    private void releasepthoto() {

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageConfig imageConfig
                        = new ImageConfig.Builder(new ImageConfigGlideLoader())
                        // 修改状态栏颜色
                        .steepToolBarColor(getResources().getColor(R.color.colorPrimary))
                        // 标题的背景颜色
                        .titleBgColor(getResources().getColor(R.color.colorPrimary))
                        // 提交按钮字体的颜色
                        .titleSubmitTextColor(getResources().getColor(R.color.white))
                        // 标题颜色
                        .titleTextColor(getResources().getColor(R.color.white))
                        .showCamera()
                        .mutiSelect()
                        .mutiSelectMaxSize(3)
                        .requestCode(REQUEST_CODE)

//                        .setContainer(llContainer, 3, true)
                        .build();
                ReleaseFragment fragment=new ReleaseFragment();
                ImageSelector.open(ReleaseFragment.this, imageConfig);
            }
        });
    }


    public void setTextContent(String textContent) {
        result.setText(textContent);
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            drawerLayout.closeDrawers();
            position();
        }
    }
}

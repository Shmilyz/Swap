package com.shmily.tjz.swap.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.shmily.tjz.swap.Adapter.PhotoShowAdapter;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Utils.ImageConfigGlideLoader;

import java.util.ArrayList;
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
    private RelativeLayout re_type,re_size;
    private TextView te_type,te_size;
    private int REQUEST_CODE=66;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.release_fragment, container, false);
        camera= (RelativeLayout) rootView.findViewById(R.id.camera);

        re_type= (RelativeLayout) rootView.findViewById(R.id.type_nest);
        te_type= (TextView) rootView.findViewById(R.id.text_type_nest);
        re_size= (RelativeLayout) rootView.findViewById(R.id.size_nest);
        te_size= (TextView) rootView.findViewById(R.id.text_size_nest);
        releaseselect();

        releasepthoto();
        return rootView;

    }

    private void releaseselect() {
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
                                "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                                "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
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


}

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
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;

import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.shmily.tjz.swap.A;
import com.shmily.tjz.swap.Adapter.PhotoShowAdapter;
import com.shmily.tjz.swap.MainActivity;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;
import com.shmily.tjz.swap.SignActivity;
import com.shmily.tjz.swap.Utils.DateUtil;
import com.shmily.tjz.swap.Utils.ImageConfigGlideLoader;
import com.shmily.tjz.swap.Utils.Position;
import com.shmily.tjz.swap.Utils.Xutils;
import com.weavey.loading.lib.LoadingLayout;
import com.yalantis.ucrop.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.picker.SinglePicker;
import github.ishaan.buttonprogressbar.ButtonProgressBar;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

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

    private ButtonProgressBar release_button;
    private  List<String> pathList=new ArrayList<>();
    private  List<String> pathList_luban=new ArrayList<>();

    private int path_amount;
    String picture_name,picture_name_title;
    String results,addresult;
    private Handler handler;
    final int WHAT_NEWS = 1 ;
    FloatingActionButton fab;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private  PictureConfig.OnSelectResultCallback resultCallback=new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {


            // 获取选中的图片路径列表 Get Images Path List
            pathList_luban.clear();
            for (int i=0;i<list.size();i++){
                pathList_luban.add(list.get(i).getCompressPath());
            }
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.show_photo_recy);
            LinearLayoutManager layoutManger = new LinearLayoutManager(getActivity());
            layoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManger);
            PhotoShowAdapter adapter = new PhotoShowAdapter(pathList_luban);
            recyclerView.setAdapter(adapter);
            path_amount = pathList_luban.size();





        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {


        }
    };

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
        release_button= (ButtonProgressBar) rootView.findViewById(R.id.releasebutton);
        localBroadcastManger=LocalBroadcastManager.getInstance(getActivity());
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.shmily.tjz.swap.GET_LOCATION");
        localReceiver=new LocalReceiver();

        Position Position = new Position();
        Position.getLocation(getActivity());

        autoposition();
        releaseposition();

        releaseselect();
        releaselimit();
        releasepthoto();
        release();
        return rootView;

    }

    private void release() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 负责接收Handler消息，并执行UI更新
                // 判断消息的来源：通过消息的类型 what
                switch (msg.what) {
                    case WHAT_NEWS:
                            uploadFile();
                        break;

                }

            }
        } ;

        release_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String text_nest_title_result="'"+text_nest_title.getText().toString().trim()+"'";
                String te_data_result="'"+te_data.getText().toString().trim()+"'";
                String te_type_result="'"+te_type.getText().toString().trim()+"'";
                String te_size_result="'"+te_size.getText().toString().trim()+"'";
                String text_nest_desc_result="'"+text_nest_desc.getText().toString().trim()+"'";
                String position_result="'"+position.getText().toString().replace(" ","")+"'";
                String te_money_result=te_money.getText().toString().trim();
                int te_money_int=Integer.parseInt(te_money_result);
                SharedPreferences prefs=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String usernames=prefs.getString("username",null);
                 final String username="'"+prefs.getString("username",null)+"'";
                String file="'userupload'";
//                path_amount
                DateUtil data=new DateUtil();
                picture_name=usernames+data.getCurrentTime(DateUtil.DateFormat.YYYYMMDDHHMMSS);
                picture_name_title="'"+usernames+data.getCurrentTime(DateUtil.DateFormat.YYYYMMDDHHMMSS)+"'";
                final String picture_url="'"+"http://www.shmilyz.com/userupload/"+picture_name+"_1.jpg"+"'";
                StringBuilder builder=new StringBuilder();
                builder.append("insert into shoes(sex,style,brand,no,price,picture,iid,special,miaoshu,biaoti,date,size,username,position,picturename,pictureamount,file) \n" +
                        "VALUES(").append("'男士'").append(",").append(te_type_result).append(",").append("'品牌'").append(",").append("'二手'").append(",")
                        .append(te_money_int).append(",").append(picture_url).append(",").append("'1'").append(",")
                        .append(position_result).append(",").append(text_nest_desc_result).append(",")
                        .append(text_nest_title_result).append(",").append(te_data_result).append(",")
                .append(te_size_result).append(",").append(username).append(",").append(position_result).append(",").append(picture_name_title).append(",").append(path_amount).append(",").append(file)
                .append(")");


                RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/insert.action");
                results= String.valueOf(builder);

                params.addBodyParameter("uname",results);
                x.http().post(params, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess( String result) {

                        try {
                            JSONObject json = new JSONObject(result);
                            int return_result = json.getInt("result");
                            Log.i("return_result",String.valueOf(return_result));
                            Log.i("int",String.valueOf(return_result));
                            if (return_result!=0){
//                                release_button.startLoader();

                                StringBuilder builders=new StringBuilder();
                                builders.append("insert into friends(shoesid,shoesname,shoesurl,username,userdate,type) value(").append(return_result).append(",").append(text_nest_title_result).append(",").append(picture_url).append(",").append(username).append(",").append("NOW()").append(",").append("3").append(");");
                                addresult=String.valueOf(builders);
                                Log.i("addresult",addresult);
                                Map<String, String> maps=new HashMap<String, String>();
                                String url="http://www.shmilyz.com/ForAndroidHttp/update.action";
                                maps.put("uname",addresult);
                                Xutils xutils=Xutils.getInstance();
                                xutils.post(url, maps, new Xutils.XCallBack() {
                                    @Override
                                    public void onResponse(String result) {
                                        try {
                                            Log.i("result",result);

                                            JSONObject json = new JSONObject(result);
                                            String  get=json.getString("result");
                                            if (get.equals("1")){


                                                Message msg = handler.obtainMessage();
                                                // 设置消息内容（可选）
                                                // 设置消息类型
                                                msg.what = WHAT_NEWS;
                                                // 发送消息
                                                handler.sendMessage(msg);
                                            }
                                            else {

                                                Snackbar.make(view,"很抱歉，上传失败!",Snackbar.LENGTH_LONG)
                                                        .show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                            Snackbar.make(view,"很抱歉，上传失败",Snackbar.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });




                            }else{

                                Snackbar.make(view,"很抱歉，上传失败",Snackbar.LENGTH_LONG)
                                        .show();
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
        });

    }
    private void uploadFile() {

        for (int a=1;a<=pathList_luban.size();a++){
            String num=String.valueOf(a);
                String name=picture_name+"_"+num;

                File file=new File(pathList_luban.get(a-1));


            RequestParams params = new RequestParams("http://www.shmilyz.com/ForAndroidUpload/upload.do") ;
            params.setMultipart(true);    // 文件上传必须有该语句
            params.addBodyParameter("file" , "userupload");
            params.addBodyParameter("username" , name);
            params.addBodyParameter("userphoto" , file);

            final int finalA = a;
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {

                    try {
                        JSONObject json = new JSONObject(result);
                        String results = json.getString("result");

                        if (results.equals("1")) {

                            if (finalA == pathList_luban.size()){
                                release_button.startLoader();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                        MainActivity mainactivity= (MainActivity) getActivity();
                                    mainactivity.replaceFragment(new ReleaseShowFragment(),R.id.nav_message);
                                        release_button.stopLoader();


                                }
                            }, 1200);

                            }


                        } else {
                            Toast.makeText(getActivity(), "失败！！！", Toast.LENGTH_SHORT).show();

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
            }) ;


        }

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
        }, 1300);


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
                localBroadcastManger.unregisterReceiver(localReceiver);


            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        position_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
                localBroadcastManger.registerReceiver(localReceiver,intentFilter);
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

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 获取选中的图片路径列表 Get Images Path List
            pathList.clear();
            pathList_luban.clear();
            pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            Log.i("pathlist",String.valueOf(pathList.size()));
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.show_photo_recy);
            LinearLayoutManager layoutManger = new LinearLayoutManager(getActivity());
            layoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManger);
            PhotoShowAdapter adapter = new PhotoShowAdapter(pathList);
            recyclerView.setAdapter(adapter);
            path_amount = pathList.size();

            for (int i=0;i<path_amount;i++) {
                File file1=new File(pathList.get(i).toString());

                Luban.get(getActivity())
                        .load(file1)                     //传人要压缩的图片
                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                        .setCompressListener(new OnCompressListener() { //设置回调

                            @Override
                            public void onStart() {
                                // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                String image_path = file.getPath();

                                pathList_luban.add(image_path);
                                //Toast.makeText(SignActivity.this, file.getPath(), Toast.LENGTH_SHORT).show();
                                Log.i("pathlist","1");

                                // TODO 压缩成功后调用，返回压缩后的图片文件
                            }

                            @Override
                            public void onError(Throwable e) {
                                // TODO 当压缩过去出现问题时调用
                            }
                        }).launch();    //启动压缩


            }
            Log.i("pathlist",String.valueOf(pathList_luban.size()));

        }
    }*/
    private void releasepthoto() {

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FunctionOptions options = new FunctionOptions.Builder()
                        .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                        .setCompress(true) //是否压缩
                        .setMaxSelectNum(3) // 可选择图片的数量
                        .setShowCamera(false) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                        .setVideoS(0)// 查询多少秒内的视频 单位:秒
                        .setEnablePreview(true) // 是否打开预览选项
                        .setCompressFlag(Luban.THIRD_GEAR)
                        .setPreviewVideo(false) // 是否预览视频(播放) mode or 多选有效
                        .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选 FunctionConfig.MODE_SINGLE FunctionConfig.MODE_MULTIPLE
                        .setNumComplete(true) // 0/9 完成  样式
//                        .setCheckNumMode(true) //QQ选择风格
                        .create();

                PictureConfig.getInstance().init(options).openPhoto(getActivity(), resultCallback);
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

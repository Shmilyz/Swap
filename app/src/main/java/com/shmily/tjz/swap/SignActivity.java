package com.shmily.tjz.swap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.shmily.tjz.swap.Fragment.ReleaseFragment;
import com.shmily.tjz.swap.Utils.ImageConfigGlideLoader;
import com.shmily.tjz.swap.Utils.Xutils;
import com.yalantis.ucrop.entity.LocalMedia;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import github.ishaan.buttonprogressbar.ButtonProgressBar;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SignActivity extends AppCompatActivity {
    private CircleImageView imageView;
    private EditText uname,upass;
    private ButtonProgressBar button;
    String name,pass;
    String image_path;
    String phone;
    View views;


    private  PictureConfig.OnSelectResultCallback resultCallback=new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {

        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {

            image_path=localMedia.getCompressPath();
            Glide.with(SignActivity.this)
                    .load(image_path)
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .into(imageView);
/*
            File file1=new File(localMedia.getCutPath());
            Glide.with(SignActivity.this)
                    .load(file1)
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .into(imageView);
            Luban.get(SignActivity.this)
                    .load(file1)                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调

                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        }
                        @Override
                        public void onSuccess(File file) {
                            image_path=file.getPath();
                            //Toast.makeText(SignActivity.this, file.getPath(), Toast.LENGTH_SHORT).show();

                            // TODO 压缩成功后调用，返回压缩后的图片文件
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过去出现问题时调用
                        }
                    }).launch();    //启动压缩*/


        }
    };



    private void uploadFile(File file) {
        RequestParams params = new RequestParams("http://www.shmilyz.com/ForAndroidUpload/upload.do") ;
        params.setMultipart(true);    // 文件上传必须有该语句
        params.addBodyParameter("file" , "headimage");
        params.addBodyParameter("username" , name);
        params.addBodyParameter("userphoto" , file);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject json = new JSONObject(result);
                    String results = json.getString("result");

                    if (results.equals("1")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(getWindow().getDecorView(),"注册成功",Snackbar.LENGTH_SHORT)
                                        .show();
                                SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=prefs.edit();
                                editor.putString("username",name);
                                editor.putBoolean("denglu",true);
                                editor.commit();
                                Intent intent=new Intent(SignActivity.this,ContactsActivity.class);
                                button.stopLoader();
                                startActivity(intent);
                                SignActivity.this.finish();
                            }
                        }, 1200);


                    } else {
                        Toast.makeText(SignActivity.this, "失败！！！", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        Snackbar.make(getWindow().getDecorView(),"手机验证成功,开始注册",Snackbar.LENGTH_SHORT)
                .show();
        uname= (EditText) findViewById(R.id.editText3);
        upass= (EditText) findViewById(R.id.editText4);
        button= (ButtonProgressBar) findViewById(R.id.registerbutton);
        imageView= (CircleImageView) findViewById(R.id.sign_image);

        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        signimage();
        init();
    }

    private void signimage() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                FunctionOptions options = new FunctionOptions.Builder()
                        .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                        .setCompress(true) //是否压缩
                        .setMaxSelectNum(1) // 可选择图片的数量
                        .setVideoS(0)// 查询多少秒内的视频 单位:秒
                        .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                        .setEnablePreview(true) // 是否打开预览选项
                        .setEnableCrop(true) // 是否打开剪切选项
                        .setCircularCut(true)// 是否采用圆形裁剪
                        .setCompressFlag(Luban.THIRD_GEAR)
                        .setPreviewVideo(false) // 是否预览视频(播放) mode or 多选有效
                        .setSelectMode(FunctionConfig.MODE_SINGLE) // 单选 or 多选 FunctionConfig.MODE_SINGLE FunctionConfig.MODE_MULTIPLE
                        .setNumComplete(false) // 0/9 完成  样式
//                        .setCheckNumMode(true) //QQ选择风格
                        .create();

                PictureConfig.getInstance().init(options).openPhoto(SignActivity.this, resultCallback);



            }
        });
    }

    private void init() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View vs) {
                views=vs;
                name=uname.getText().toString().trim().replace(" ","");
                pass=upass.getText().toString().trim().replace(" ","");

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
                    String sql="INSERT INTO login(username,password,phone) VALUES ('"+name+"',"+"'"+pass+"',"+"'"+phone+"')";
                    Log.i("sql",sql);
                    Xutils xutils=Xutils.getInstance();
                    String url="http://www.shmilyz.com/ForAndroidHttp/update.action";
                    Map<String, String> maps=new HashMap<String, String>();
                    maps.put("uname",sql);
                    xutils.post(url, maps, new Xutils.XCallBack() {
                        @Override
                        public void onResponse(String result) {

                            try {
                                JSONObject json = new JSONObject(result);
                                String results = json.getString("result");

                                if (results.equals("1")) {
                                    button.startLoader();
                                    uploadFile(new File(image_path));


            } else {
                Toast.makeText(SignActivity.this, "失败！！！", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

                        }
                    });

                }
                else{
                    Toast.makeText(SignActivity.this, "请输入正确的账号或密码！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

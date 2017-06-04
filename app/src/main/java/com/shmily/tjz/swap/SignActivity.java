package com.shmily.tjz.swap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.shmily.tjz.swap.Fragment.ReleaseFragment;
import com.shmily.tjz.swap.Utils.ImageConfigGlideLoader;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 获取选中的图片路径列表 Get Images Path List
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            File file1=new File(pathList.get(0).toString());
            Glide.with(SignActivity.this)
                    .load(file1)
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .into(imageView);
            Luban.get(this)
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
                    }).launch();    //启动压缩

        }

    }

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
                                SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=prefs.edit();
                                editor.putString("username",name);
                                editor.putBoolean("denglu",true);
                                editor.commit();
                                Intent intent=new Intent(SignActivity.this,MainActivity.class);
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
        uname= (EditText) findViewById(R.id.editText3);
        upass= (EditText) findViewById(R.id.editText4);
        button= (ButtonProgressBar) findViewById(R.id.registerbutton);
        imageView= (CircleImageView) findViewById(R.id.sign_image);
        signimage();
        init();
    }

    private void signimage() {
        imageView.setOnClickListener(new View.OnClickListener() {
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
                     .singleSelect()
                        .crop()
                        .build();
                ImageSelector.open(SignActivity.this, imageConfig);
            }
        });
    }

    private void init() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=uname.getText().toString().trim().replace(" ","");
                pass=upass.getText().toString().trim().replace(" ","");
                RequestParams params=new RequestParams("http://120.25.96.231/ForAndroidHttp/login.action");
                params.addBodyParameter("uname",name);
                params.addBodyParameter("upass",pass);
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {

                    x.http().post(params, new Callback.CacheCallback<String>() {
    @Override
    public void onSuccess(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String results = json.getString("result");

            if (results.equals("1")) {
                button.startLoader();

                uploadFile(new File(image_path));
               /* Toast.makeText(SignActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("username",name);
                editor.putBoolean("denglu",true);
                editor.commit();
                Intent intent=new Intent(SignActivity.this,MainActivity.class);
                startActivity(intent);
                SignActivity.this.finish();*/
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

    @Override
    public boolean onCache(String result) {
        return false;
    }
});
                }else{
                    Toast.makeText(SignActivity.this, "请输入正确的账号或密码！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

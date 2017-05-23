package com.shmily.tjz.swap;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.shmily.tjz.swap.Utils.ImageConfigGlideLoader;
import com.shmily.tjz.swap.Utils.NumberKeyboardView;
import com.weavey.loading.lib.LoadingLayout;

import android.net.Uri;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class A extends AppCompatActivity {


    CircleImageView imageView;
    @BindView(R.id.po)
    Button po;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_IMAGE = 25;

    List<Uri> mSelected;
    private LinearLayout llContainer;
    private View inflate;
    private Button show;
    private Dialog dialog;
    private void uploadFile(File file) {
        RequestParams params = new RequestParams("http://www.shmilyz.com/ForAndroidUpload/upload.do") ;
        params.setMultipart(true);    // 文件上传必须有该语句
        params.addBodyParameter("file" , "headimage");
        params.addBodyParameter("username" , "张三");
        params.addBodyParameter("userphoto" , file);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Toast.makeText(A.this, result, Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     /*   if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
//            Toast.makeText(this, mSelected.get(0).toString(), Toast.LENGTH_SHORT).show();
            CropImage.activity( mSelected.get(0))
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this).load(resultUri).into(imageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/

      /*  if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            final boolean original =
                    data.getBooleanExtra(PhotoPickerActivity.EXTRA_RESULT_ORIGINAL, false);
        }*/
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 获取选中的图片路径列表 Get Images Path List
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            Toast.makeText(this, pathList.get(0).toString(), Toast.LENGTH_SHORT).show();
                 File file1=new File(pathList.get(0).toString());

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
                            uploadFile(file);
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过去出现问题时调用
                        }
                    }).launch();    //启动压缩

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        llContainer= (LinearLayout) findViewById(R.id.llContainer);
        Button button= (Button) findViewById(R.id.select);
        Button show= (Button) findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* SImagePicker
                        .from(A.this)
                        .maxCount(9)
                        .rowCount(3)
                        .showCamera(true)
                        .pickMode(SImagePicker.MODE_IMAGE)
                        .forResult(REQUEST_CODE_IMAGE);*/
          /*      Matisse.from(A.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .theme(R.style.Matisse_Dracula)
                        .forResult(REQUEST_CODE_CHOOSE);*/
                /*两种方式实现加载图片选择器和剪裁 但是第二种的作者实现的图片选择器有细节bug，所以采用这种组合式的图片选择器，先选择后剪裁。*/


                ImageConfig imageConfig
                        = new ImageConfig.Builder(new ImageConfigGlideLoader())
                        // 修改状态栏颜色
                        .steepToolBarColor(getResources().getColor(R.color.blue))
                        // 标题的背景颜色
                        .titleBgColor(getResources().getColor(R.color.blue))
                        // 提交按钮字体的颜色
                        .titleSubmitTextColor(getResources().getColor(R.color.white))
                        // 标题颜色
                        .titleTextColor(getResources().getColor(R.color.white))
                        .showCamera()
                  /*    .singleSelect()
//                        .crop(1, 2, 500, 1000)
                        .crop()*/
                        .setContainer(llContainer, 4, true)
                        .build();
                ImageSelector.open(A.this, imageConfig);
            }
        });


        LoadingLayout  loading = (LoadingLayout) findViewById(R.id.load_layout);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //获取系统的连接服务。
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //获取网络的连接情况。
        if (networkInfo != null && networkInfo.isAvailable()) {
          /*  if (networkInfo.getType()==connectivityManager.TYPE_WIFI){
                Toast.makeText(A.this,"网络已启动啦(WIFI)",Toast.LENGTH_SHORT).show();
            }else if (networkInfo.getType()==connectivityManager.TYPE_MOBILE) {
                Toast.makeText(A.this,"网络已启动啦(3G)",Toast.LENGTH_SHORT).show();

            }*/
            Toast.makeText(A.this, "网络已启动啦(WIFI)", Toast.LENGTH_SHORT).show();
            loading.setStatus(LoadingLayout.Success);//加载成功


        } else {
            Toast.makeText(A.this, "网络已关闭啦", Toast.LENGTH_SHORT).show();
            loading.setStatus(LoadingLayout.No_Network);//无网络

        }
            loading.setOnReloadListener(new LoadingLayout.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    Toast.makeText(A.this, "AAAA", Toast.LENGTH_SHORT).show();
                }
            });
        imageView = (CircleImageView) findViewById(R.id.icon_image);
        Intent intent = getIntent();

        String b = (String) intent.getSerializableExtra("ss");
//        a.setText(b);

               /* Position Position = new Position();
                Position.getLocation(A.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        A.this.runOnUiThread(new Runnable() {
                            //                    runOnUiThread()方法的将线程切换回主线程，因为我们知道我们无法再副线程里面进行ui的变化，所以我们用这个方法。
                            @Override
                            public void run() {
                                SharedPreferences prefs = getSharedPreferences("location", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = prefs.edit();
                                String Aa = prefs.getString("City", null);
                                editor.commit();
                                a.setText(Aa);
//                        告诉它刷新结束，收回进度条。
                            }
                        });
                    }
                }).start();*/
        SharedPreferences pref = getSharedPreferences("shoes_result_service", MODE_PRIVATE);
        String result = pref.getString("shoes_result_service", "");


        int i = 1;
        StringBuilder url = new StringBuilder();
        url.append("http://www.shmilyz.com/search/").append(i).append(".png");
        String urls = String.valueOf(url);
//        Glide.with(this).load(urls).into(imageView);

    }

    private void showPopupWindow() {



        PopupWindow mPopWindow;
        View contentView = LayoutInflater.from(A.this).inflate(R.layout.activity_keyboard, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //设置各个控件的点击响应
        NumberKeyboardView number= (NumberKeyboardView) contentView.findViewById(R.id.numberKeyboardView);
        number.setOnNumberClickListener(new NumberKeyboardView.OnNumberClickListener() {
            @Override
            public void onNumberReturn(String number) {
                Toast.makeText(A.this, number, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNumberDelete() {

            }
        });

        //显示PopupWindow
        View rootview = LayoutInflater.from(A.this).inflate(R.layout.activity_a, null);

        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);


    }
}

package com.shmily.tjz.swap.Rubbish;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.shmily.tjz.swap.MainActivity;
import com.shmily.tjz.swap.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class SignLoginActivity extends AppCompatActivity {
    MaterialEditText email, pass, email2, pass2, confirmPass;
    RelativeLayout relativeLayout, relativeLayout2;
    LinearLayout mainLinear,img;
    TextView signUp,login,forgetPass;
    ImageView logo,back;
    LinearLayout.LayoutParams params, params2;
    FrameLayout.LayoutParams params3;
    FrameLayout mainFrame;
    ObjectAnimator animator2, animator1;
    String uname,upass;
    String uuname,uupass;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_login);

        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params3 = new FrameLayout.LayoutParams(inDp(50), inDp(50));

        signUp = (TextView) findViewById(R.id.signUp);
        login = (TextView) findViewById(R.id.login);
        email = (MaterialEditText) findViewById(R.id.email);
        pass = (MaterialEditText) findViewById(R.id.pass);
        img = (LinearLayout) findViewById(R.id.img);
        email2 = (MaterialEditText) findViewById(R.id.email2);

        pass2 = (MaterialEditText) findViewById(R.id.pass2);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        back = (ImageView) findViewById(R.id.backImg);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relative2);
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);

        logo = new ImageView(this);
        logo.setImageResource(R.drawable.logo);
        logo.setLayoutParams(params3);

        relativeLayout.post(new Runnable() {
            @Override
            public void run() {

                logo.setX((relativeLayout2.getRight() / 2));
                logo.setY(inDp(50));
                mainFrame.addView(logo);
            }
        });

        params.weight = (float) 0.75;
        params2.weight = (float) 4.25;

        mainLinear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mainLinear.getWindowVisibleDisplayFrame(r);
                int screenHeight = mainFrame.getRootView().getHeight();


                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) {
                    // keyboard is opened
                    if (params.weight == 4.25) {

                        animator1 = ObjectAnimator.ofFloat(back, "scaleX", (float) 1.95);
                        animator2 = ObjectAnimator.ofFloat(back, "scaleY", (float) 1.95);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2);
                        set.setDuration(1000);
                        set.start();

                    } else {

                        animator1 = ObjectAnimator.ofFloat(back, "scaleX", (float) 1.75);
                        animator2 = ObjectAnimator.ofFloat(back, "scaleY", (float) 1.75);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2);
                        set.setDuration(500);
                        set.start();
                    }
                } else {
                    // keyboard is closed
                    animator1 = ObjectAnimator.ofFloat(back, "scaleX", 3);
                    animator2 = ObjectAnimator.ofFloat(back, "scaleY", 3);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(animator1, animator2);
                    set.setDuration(500);
                    set.start();
                }
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (params.weight == 4.25) {

                    uname=email2.getText().toString().trim();
                    upass=pass2.getText().toString().trim();
                    RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/login.action");
                    params.addBodyParameter("uname",uname);
                    params.addBodyParameter("upass",upass);
                    if (!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(upass)) {
                        x.http().post(params, new Callback.CacheCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject json = new JSONObject(result);
                                    String results = json.getString("result");

                                    if (results.equals("1")) {
                                        Toast.makeText(SignLoginActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=prefs.edit();
                                        editor.putString("username",uname);
                                        editor.putBoolean("denglu",true);
                                        editor.commit();
                                        Intent intent=new Intent(SignLoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        SignLoginActivity.this.finish();
                                    } else {
                                        Snackbar.make(relativeLayout, "注册失败", Snackbar.LENGTH_SHORT).show();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Snackbar.make(relativeLayout, "网络请求失败", Snackbar.LENGTH_SHORT).show();

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
                        Snackbar.make(relativeLayout, "请输入正确的账号或密码", Snackbar.LENGTH_SHORT).show();

                    }                    return;
                }
                email2.setVisibility(View.VISIBLE);
                pass2.setVisibility(View.VISIBLE);

                final ChangeBounds bounds = new ChangeBounds();
                bounds.setDuration(1500);
                bounds.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {


                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(signUp, "translationX", mainLinear.getWidth() / 2 - relativeLayout2.getWidth() / 2 - signUp.getWidth() / 2);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX", -relativeLayout2.getX());
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(signUp, "rotation", 0);

                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(email, "alpha", 1, 0);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(pass, "alpha", 1, 0);
                        ObjectAnimator animator6 = ObjectAnimator.ofFloat(forgetPass, "alpha", 1, 0);

                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(login, "rotation", 90);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(login, "y", relativeLayout2.getHeight() / 2);
                        ObjectAnimator animator9 = ObjectAnimator.ofFloat(email2, "alpha", 0, 1);

                        ObjectAnimator animator10 = ObjectAnimator.ofFloat(confirmPass, "alpha", 0, 1);
                        ObjectAnimator animator11 = ObjectAnimator.ofFloat(pass2, "alpha", 0, 1);
                        ObjectAnimator animator12 = ObjectAnimator.ofFloat(signUp, "y", login.getY());

                        ObjectAnimator animator13 = ObjectAnimator.ofFloat(back, "translationX", img.getX());
                        ObjectAnimator animator14 = ObjectAnimator.ofFloat(signUp, "scaleX", 2);
                        ObjectAnimator animator15 = ObjectAnimator.ofFloat(signUp, "scaleY", 2);

                        ObjectAnimator animator16 = ObjectAnimator.ofFloat(login, "scaleX", 1);
                        ObjectAnimator animator17 = ObjectAnimator.ofFloat(login, "scaleY", 1);
                        ObjectAnimator animator18 = ObjectAnimator.ofFloat(logo, "x", relativeLayout2.getRight() / 2 - relativeLayout.getRight());

                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2, animator3, animator4, animator5, animator6, animator7,
                                animator8, animator9, animator10, animator11, animator12, animator13, animator14, animator15, animator16, animator17, animator18);
                        set.setDuration(1500).start();


                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {


                        email.setVisibility(View.INVISIBLE);
                        pass.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {


                    }
                });

                TransitionManager.beginDelayedTransition(mainLinear, bounds);

                params.weight = (float) 4.25;
                params2.weight = (float) 0.75;


                relativeLayout.setLayoutParams(params);
                relativeLayout2.setLayoutParams(params2);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (params2.weight == 4.25) {

                    /*Snackbar.make(relativeLayout2, "Login Complete", Snackbar.LENGTH_SHORT).show();*/
                    uuname=email.getText().toString().trim();
                    uupass=pass.getText().toString().trim();
                    RequestParams params=new RequestParams("http://www.shmilyz.com/ForAndroidHttp/sign.action");
                    params.addBodyParameter("uname",uuname);
                    params.addBodyParameter("upass",uupass);

                    if (!TextUtils.isEmpty(uuname) && !TextUtils.isEmpty(uupass)){
                        x.http().post(params, new Callback.CacheCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject json = new JSONObject(result);
                                    String results = json.getString("result");

                                    if (results.equals("1")) {
                                        Toast.makeText(SignLoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=prefs.edit();
                                        editor.putString("username",uuname);
                                        editor.putBoolean("denglu",true);
                                        editor.commit();
                                        Intent intent=new Intent(SignLoginActivity.this,MainActivity.class);
                                        intent.putExtra("username",uuname);
                                        startActivity(intent);
                                        SignLoginActivity.this.finish();
                                    } else {
                                        Snackbar.make(relativeLayout2, "账号或密码错误", Snackbar.LENGTH_SHORT).show();


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Snackbar.make(relativeLayout2, "网络请求失败", Snackbar.LENGTH_SHORT).show();


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
                    }else {
                        Snackbar.make(relativeLayout2, "请输入正确的账号或密码", Snackbar.LENGTH_SHORT).show();

/*
                        Toast.makeText(LoginActivity.this, "请输入正确的账号或密码！", Toast.LENGTH_SHORT).show();
*/
                    }
                    return;
                }

                email.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);


                final ChangeBounds bounds = new ChangeBounds();
                bounds.setDuration(1500);
                bounds.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {


                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(login, "translationX", mainLinear.getWidth() / 2 - relativeLayout.getWidth() / 2 - login.getWidth() / 2);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX", (relativeLayout.getX()));
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(login, "rotation", 0);

                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(email, "alpha", 0, 1);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(pass, "alpha", 0, 1);
                        ObjectAnimator animator6 = ObjectAnimator.ofFloat(forgetPass, "alpha", 0, 1);

                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(signUp, "rotation", 90);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(signUp, "y", relativeLayout.getHeight() / 2);
                        ObjectAnimator animator9 = ObjectAnimator.ofFloat(email2, "alpha", 1, 0);

                        ObjectAnimator animator10 = ObjectAnimator.ofFloat(confirmPass, "alpha", 1, 0);
                        ObjectAnimator animator11 = ObjectAnimator.ofFloat(pass2, "alpha", 1, 0);
                        ObjectAnimator animator12 = ObjectAnimator.ofFloat(login, "y", signUp.getY());

                        ObjectAnimator animator13 = ObjectAnimator.ofFloat(back, "translationX", -img.getX());
                        ObjectAnimator animator14 = ObjectAnimator.ofFloat(login, "scaleX", 2);
                        ObjectAnimator animator15 = ObjectAnimator.ofFloat(login, "scaleY", 2);

                        ObjectAnimator animator16 = ObjectAnimator.ofFloat(signUp, "scaleX", 1);
                        ObjectAnimator animator17 = ObjectAnimator.ofFloat(signUp, "scaleY", 1);
                        ObjectAnimator animator18 = ObjectAnimator.ofFloat(logo, "x", logo.getX()+relativeLayout2.getWidth());


                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2, animator3, animator4, animator5, animator6, animator7,
                                animator8, animator9, animator10, animator11, animator12, animator13, animator14, animator15, animator16, animator17,animator18);
                        set.setDuration(1500).start();

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {

                        email2.setVisibility(View.INVISIBLE);
                        pass2.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });

                TransitionManager.beginDelayedTransition(mainLinear, bounds);

                params.weight = (float) 0.75;
                params2.weight = (float) 4.25;

                relativeLayout.setLayoutParams(params);
                relativeLayout2.setLayoutParams(params2);


            }
        });
    }


    private int inDp(int dp) {

        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}

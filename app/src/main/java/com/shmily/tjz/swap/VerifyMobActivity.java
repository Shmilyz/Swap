package com.shmily.tjz.swap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class VerifyMobActivity extends AppCompatActivity {
      private Button verify_mob_get,verify_mob_next;
    private TextView verify_mob_text;
    private EditText verify_mob_edit,verify_mob_ma_edit;
    private Handler handler;
    int i=30;
    private String phoneNums;
private View v;
    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mob);
        verify_mob_get= (Button) findViewById(R.id.verify_mob_get);
        verify_mob_next= (Button) findViewById(R.id.verify_mob_next);
        verify_mob_text= (TextView) findViewById(R.id.verify_mob_text);
        verify_mob_edit= (EditText) findViewById(R.id.verify_mob_edit);
        verify_mob_ma_edit= (EditText) findViewById(R.id.verify_mob_ma_edit);
        init();
        verify_mob_next.setClickable(false);

        verify_mob_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v=view;
                phoneNums = verify_mob_edit.getText().toString().trim();
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证

                SMSSDK.getVerificationCode("86", phoneNums);
                settext();
                verify_mob_get.setClickable(false);
                verify_mob_next.setClickable(true);

                verify_mob_get.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();


            }
        });
        verify_mob_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v=view;
                SMSSDK.submitVerificationCode("86", phoneNums, verify_mob_ma_edit
                        .getText().toString());
            }
        });


        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == -9) {
                    verify_mob_get.setText("重新发送(" + i + ")");
                } else if (msg.what == -8) {
                    verify_mob_get.setText("获取验证码");
                    verify_mob_get.setClickable(true);
                    i = 30;
                } else {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // 短信注册成功后，返回MainActivity,然后提示
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                            Snackbar.make(getWindow().getDecorView(),"手机验证成功,开始注册",Snackbar.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent(VerifyMobActivity.this,
                                    SignActivity.class);
                            intent.putExtra("phone",phoneNums);
                            startActivity(intent);
                            VerifyMobActivity.this.finish();
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            Snackbar.make(getWindow().getDecorView(),"验证码发送成功",Snackbar.LENGTH_SHORT)
                                    .setAction("确认", new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                        }
                                    })
                                    .show();
                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                }
            }
        };


    }

    private void settext() {

        String str1 = "我们已经给您的手机号码 ";
        String str2 = phoneNums;
        String str3 = " 发送了一条验证短信。";

        SpannableStringBuilder builder = new SpannableStringBuilder(str1 + str2 + str3 );
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffa200")),
                str1.length(), (str1 + str2).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        verify_mob_text.setText(builder);
    }

    private boolean judgePhoneNums(String phoneNums) {

        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }
    public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
    private void init() {
        SMSSDK.initSDK(this, "1e82a8b66b9a6", "23a8ca89c645a13fdc7b27a37fcaafc5");
        EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);



    }
}

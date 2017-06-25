package com.shmily.tjz.swap;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shmily.tjz.swap.Utils.Xutils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WritePositionActivity extends AppCompatActivity {
    @BindView(R.id.activity_write_position_keep)
    TextView activityWritePositionKeep;
    @BindView(R.id.activity_write_position_re)
    RelativeLayout activityWritePositionRe;
    @BindView(R.id.activity_write_position_name)
    EditText activityWritePositionName;
    @BindView(R.id.activity_write_position_number)
    EditText activityWritePositionNumber;
    @BindView(R.id.activity_write_position_pos)
    EditText activityWritePositionPos;
    @BindView(R.id.switch_compat)
    SwitchCompat switchCompat;
    private boolean isChecked=false;
     private String name,number,pos,username;
    Xutils xutils;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_position);
        ButterKnife.bind(this);
        xutils=Xutils.getInstance();
        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
        username="'"+prefs.getString("username",null)+"'";
        getinfo();

    }

    private void getinfo() {

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked=b;

            }
        });

    }

    @OnClick(R.id.activity_write_position_keep)
    public void onViewClicked() {
        progressDialog = new Dialog(WritePositionActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("卖力上传中");
        progressDialog.show();
        StringBuilder builder = new StringBuilder();
        StringBuilder builders = new StringBuilder();

        String sqlresult;
         name="'"+activityWritePositionName.getText().toString().trim()+"'";
        number="'"+activityWritePositionNumber.getText().toString().trim()+"'";
        pos="'"+activityWritePositionPos.getText().toString().trim()+"'";
        if (isChecked){
            builder.append("INSERT into position value(").append(username).append(",").append(name).append(",").append(number).append(",").append(pos)
                    .append(") ON DUPLICATE KEY UPDATE").append(" name=").append(name).append(",number=").append(number).append(",positions=")
                    .append(pos).append(";");
            builders.append("INSERT into positions value(").append("null,").append(username).append(",").append(name).append(",").append(number).append(",").append(pos).append(");");

            sqlresult=String.valueOf(builder);
            String sqlresults=String.valueOf(builders);

            Log.i("builder",sqlresult);
            Log.i("builder",sqlresults);

            send(sqlresult,sqlresults);
        }else{
            builders.append("INSERT into positions value(").append("null,").append(username).append(",").append(name).append(",").append(number).append(",").append(pos).append(");");
            sqlresult=String.valueOf(builders);
            Log.i("builder",sqlresult);
            send(sqlresult);

        }

    }

    private void send(String result) {


        Map<String, String> maps=new HashMap<String, String>();
        String url="http://www.shmilyz.com/ForAndroidHttp/update.action";
        String sql=result;

        maps.put("uname",sql);
        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {


                Snackbar.make(getWindow().getDecorView(),"信息上传成功",Snackbar.LENGTH_SHORT)
                        .setAction("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();

                SharedPreferences.Editor editor=getSharedPreferences("buy_positions_show",MODE_PRIVATE).edit();
                editor.clear();
                editor.putBoolean("boolean",true);
                editor.putString("name",name);
                editor.putString("position",number);
                editor.putString("number",pos);
                editor.apply();
                progressDialog.dismiss();
                WritePositionActivity.this.finish();

            }
        });


    }
    private void send(String result,String results) {


        Map<String, String> maps=new HashMap<String, String>();
        String url="http://www.shmilyz.com/ForAndroidHttp/update.action";
        String sql=result;
        String sqls=results;

        maps.put("uname",sqls);
        maps.put("upass",sql);

        xutils.post(url, maps, new Xutils.XCallBack() {
            @Override
            public void onResponse(String result) {


                Snackbar.make(getWindow().getDecorView(),"信息上传成功",Snackbar.LENGTH_SHORT)
                        .setAction("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();
                SharedPreferences.Editor editor=getSharedPreferences("buy_positions_show",MODE_PRIVATE).edit();
                editor.clear();
                editor.putBoolean("boolean",true);
                editor.putString("name",activityWritePositionName.getText().toString().trim());
                editor.putString("position",activityWritePositionPos.getText().toString().trim());
                editor.putString("number",activityWritePositionNumber.getText().toString().trim());
                editor.apply();
                progressDialog.dismiss();
                WritePositionActivity.this.finish();

            }
        });


    }


}

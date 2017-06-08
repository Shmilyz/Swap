package com.shmily.tjz.swap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VerifyMobActivity extends AppCompatActivity {
      private Button verify_mob_get,verify_mob_next;
    private TextView verify_mob_text;
    private EditText verify_mob_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mob);
        verify_mob_get= (Button) findViewById(R.id.verify_mob_get);
        verify_mob_next= (Button) findViewById(R.id.verify_mob_next);
        verify_mob_text= (TextView) findViewById(R.id.verify_mob_text);
        verify_mob_edit= (EditText) findViewById(R.id.verify_mob_edit);
        


    }
}

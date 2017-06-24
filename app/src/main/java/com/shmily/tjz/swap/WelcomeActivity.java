package com.shmily.tjz.swap;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {
      private  Button count_down;
    private Handler handler;
    int i=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        count_down= (Button) findViewById(R.id.count_down);


    }

}

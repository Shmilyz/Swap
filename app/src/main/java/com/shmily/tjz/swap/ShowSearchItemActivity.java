package com.shmily.tjz.swap;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shmily.tjz.swap.Fragment.ResultFragment;
import com.shmily.tjz.swap.Fragment.SearchFragment;

public class ShowSearchItemActivity extends AppCompatActivity {
private TextView specoal_show_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search_item);
        Intent intent=getIntent();
        String search_item_get=intent.getStringExtra("search_item_get");
        specoal_show_title= (TextView) findViewById(R.id.specoal_show_title);
        specoal_show_title.setText(search_item_get);
        ResultFragment resultFragment=new ResultFragment();
        replaceFragment(resultFragment);
        Bundle bundle=new Bundle();
        bundle.putString("results",search_item_get
        );
        resultFragment.setArguments(bundle);

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.search_layout,fragment);
        transaction.commit();

    }
}

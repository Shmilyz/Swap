package com.shmily.tjz.swap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shmily.tjz.swap.Fragment.MainFragment;
import com.shmily.tjz.swap.Fragment.ResultFragment;
import com.shmily.tjz.swap.Fragment.SearchFragment;
import com.shmily.tjz.swap.LitePal.Data;

public class SearchActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    MaterialSearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);

        replaceFragment(new SearchFragment());
        initview();

    }



    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.search_layout,fragment);
        transaction.commit();

    }

    private void initview() {
        searchBar.setMaxSuggestionCount(0);

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())){
                    ResultFragment resultFragment=new ResultFragment();
                    Bundle bundle=new Bundle();
                    String result=s.toString().replace("","");
                    bundle.putString("results",result);
                    resultFragment.setArguments(bundle);
                    replaceFragment(resultFragment);

                }
                else {
                    replaceFragment(new SearchFragment());

                }
            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                if (!TextUtils.isEmpty(text.toString().trim())){
                    Data data=new Data();
                    data.setName(text.toString());
                    data.save();

                }


                InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                        0);
                }

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }
}
/*
*     if (!TextUtils.isEmpty(s.toString())){
            replaceFragment(new MainFragment());

                }
                else {
                    replaceFragment(new SearchFragment());

                }
*
*
* */
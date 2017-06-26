package com.shmily.tjz.swap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.imageloader.ImageLoader;
import com.shmily.tjz.swap.Fragment.AlreadyBuyFragment;
import com.shmily.tjz.swap.Fragment.BuyCarFragment;
import com.shmily.tjz.swap.Fragment.CollectFragment;
import com.shmily.tjz.swap.Fragment.ReleaseFragment;
import com.shmily.tjz.swap.Fragment.ReleaseShowFragment;
import com.shmily.tjz.swap.Fragment.ViewPagerFragmwnt;
import com.shmily.tjz.swap.Srevice.SearchService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    String username;
    private ImageLoader mLoader;
    SharedPreferences.Editor editor;
    boolean release=true;
    boolean main=false;
    boolean reshow=true;
    boolean wantbuy=true;
    boolean collect=true;
    boolean buy=true;
    int a = 0;
    Toolbar toolbar;
    NavigationView navView;
    @Override
    public void onBackPressed() {
        mLoader = new ImageLoader(MainActivity.this);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent Startservice=new Intent(this, SearchService.class);
        startService(Startservice);


        SharedPreferences prefs=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=prefs.edit();
        boolean isGuideLoaded=prefs.getBoolean("denglu",false);
        username=prefs.getString("username",null);

        if (!isGuideLoaded){

            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }

        navView= (NavigationView)findViewById(R.id.nav_view);
         toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();

        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.gengduo);
            actionBar.setTitle(" ");
        }

        replaceFragment(new ViewPagerFragmwnt());
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_CONTACTS);

        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            init();
        }


    }



    private void init() {

        mDrawerLayout= (DrawerLayout) findViewById(R.id.activity_main);

        View headerLayout = navView.inflateHeaderView(R.layout.nav_header);
        TextView name= (TextView) headerLayout.findViewById(R.id.username);
        name.setText(username);
        navView.setCheckedItem(R.id.nav_me);

        CircleImageView headimage= (CircleImageView) headerLayout.findViewById(R.id.icon_image);

        String url="http://www.shmilyz.com/headimage/"+username+".jpg";

        Glide.with(MainActivity.this)
                .load(url)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(headimage);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_me:


                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        navView.setCheckedItem(R.id.nav_me);

                        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                if (slideOffset == 0) {
                                    if (main) {
                                        Intent stopservice = new Intent(MainActivity.this, SearchService.class);
                                        stopService(stopservice);

                                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                                        startActivity(intent1);
                                        MainActivity.this.finish();
                                    }
                                }
                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {

                            }

                            @Override
                            public void onDrawerClosed(View drawerView) {

                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });


                        break;

                    case R.id.nav_message:

                        mDrawerLayout.closeDrawers();
                        item.setChecked(true);


                        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                if (slideOffset == 0 && item.getItemId()==R.id.nav_message) {

                                    replaceFragment(new ReleaseShowFragment());
                                    if (reshow) {
                                        release = true;
                                        main = true;
                                        reshow = false;
                                        wantbuy = true;
                                        collect = true;
                                        buy = true;
                                    }


                                }
                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {

                            }

                            @Override
                            public void onDrawerClosed(View drawerView) {

                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });

                        break;
                    case R.id.nav_night:

                        mDrawerLayout.closeDrawers();
                        item.setChecked(true);


                        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                if (slideOffset == 0 && item.getItemId()==R.id.nav_night) {
                                    if (wantbuy) {
                                        replaceFragment(new BuyCarFragment());
                                        release = true;
                                        main = true;
                                        reshow = true;
                                        wantbuy = false;
                                        collect = true;
                                        buy = true;
                                    }


                                }
                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {

                            }

                            @Override
                            public void onDrawerClosed(View drawerView) {

                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });

                        break;

                    case R.id.nav_notification:

                        mDrawerLayout.closeDrawers();
                        item.setChecked(true);


                        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                if (slideOffset == 0 && item.getItemId()==R.id.nav_notification) {
                                    if (buy) {
                                        replaceFragment(new AlreadyBuyFragment());
                                        release = true;
                                        main = true;
                                        reshow = true;
                                        wantbuy = true;
                                        collect = true;
                                        buy = false;
                                    }

                                }
                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {

                            }

                            @Override
                            public void onDrawerClosed(View drawerView) {

                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });

                        break;
                    case R.id.nav_setting:
                        editor.apply();
                        editor.remove("username");
                        editor.remove("denglu");
                        editor.commit();
                        Intent intent=new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                        break;
                    case R.id.nav_friend:
                        mDrawerLayout.closeDrawers();
                        item.setChecked(true);


                        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                if (slideOffset == 0 && item.getItemId()==R.id.nav_friend) {
                                    if (collect) {
                                        replaceFragment(new CollectFragment());
                                        release = true;
                                        main = true;
                                        reshow = true;
                                        wantbuy = true;
                                        collect = false;
                                        buy = true;
                                    }


                                }
                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {

                            }

                            @Override
                            public void onDrawerClosed(View drawerView) {

                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });


                        break;
                    case R.id.nav_theme:
                        mDrawerLayout.closeDrawers();
                        item.setChecked(true);

                        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                if (slideOffset == 0 && item.getItemId()==R.id.nav_theme) {
                                    if (release) {
                                        replaceFragment(new ReleaseFragment());
                                        release=false;
                                        main=true;
                                         reshow=true;
                                         wantbuy=true;
                                         collect=true;
                                         buy=true;

                                    }
                                }
                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {

                            }

                            @Override
                            public void onDrawerClosed(View drawerView) {

                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });
                    }


                //                    在这里编写逻辑性的东西。

                return true;
            }
        });
    }

    public void replaceFragment(Fragment fragement) {

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,fragement);
        transaction.commit();
    }
    public void replaceFragment(Fragment fragement,int id) {
        navView.setCheckedItem(id);
        release=true;
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,fragement);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            MainActivity.this.finish();
                            return;
                        }
                    }
                    /*replaceFragment(new LocationFragment());
                    mDrawerLayout.closeDrawers();*/
                    init();


                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }


}
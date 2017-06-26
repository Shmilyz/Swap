package com.shmily.tjz.swap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShowBuyInfoActivity extends AppCompatActivity {

    private TextView show_buy_info_pos,buy_info_username,buy_info_shoesname,buy_info_price
            ,buy_info_shoesmodle,buy_info_leave,buy_info_shoesddbh,buy_info_shoesdate;

    private CircleImageView buy_info_headimage;
    private ImageView buy_info_shoesimage;
        private String shoesid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_buy_info);
        show_buy_info_pos= (TextView) findViewById(R.id.show_buy_info_pos);
        buy_info_username= (TextView) findViewById(R.id.buy_info_username);
        buy_info_shoesname= (TextView) findViewById(R.id.buy_info_shoesname);
        buy_info_price= (TextView) findViewById(R.id.buy_info_price);
        buy_info_shoesmodle= (TextView) findViewById(R.id.buy_info_shoesmodle);
        buy_info_leave= (TextView) findViewById(R.id.buy_info_leave);
        buy_info_shoesddbh= (TextView) findViewById(R.id.buy_info_shoesddbh);
        buy_info_shoesdate= (TextView) findViewById(R.id.buy_info_shoesdate);
        buy_info_headimage= (CircleImageView) findViewById(R.id.buy_info_headimage);
        buy_info_shoesimage= (ImageView) findViewById(R.id.buy_info_shoesimage);
        getinfo();

    }

    private void getinfo() {

        Intent intent =getIntent();

        shoesid=intent.getStringExtra(ShoesActivity.SHOES_ID);
        Glide.with(ShowBuyInfoActivity.this)
                .load( intent.getStringExtra(ShoesActivity.SHOES_IMAGE_URL))
                .into(buy_info_shoesimage);
        String url="http://www.shmilyz.com/headimage/"+intent.getStringExtra(ShoesActivity.SHOES_USERNAME)+".jpg";
        Glide.with(ShowBuyInfoActivity.this)
                .load(url)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(buy_info_headimage);
        buy_info_username.setText(intent.getStringExtra(ShoesActivity.SHOES_USERNAME));
        buy_info_shoesname.setText(intent.getStringExtra(ShoesActivity.SHOES_BIAOTI));
        buy_info_price.setText("¥"+intent.getStringExtra(ShoesActivity.SHOES_PRICE));
        buy_info_shoesmodle.setText(intent.getStringExtra(ShoesActivity.SHOES_SIZE));
        buy_info_leave.setText(intent.getStringExtra("buy_say"));
        buy_info_shoesdate.setText(intent.getStringExtra("buy_date"));
        buy_info_shoesddbh.setText(intent.getStringExtra("buy_buyid"));
        show_buy_info_pos.setText(intent.getStringExtra("buy_position"));


    }
}

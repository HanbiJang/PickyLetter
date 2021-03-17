package com.makeus.pineapple.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.makeus.pineapple.R;

public class PopupSub extends Activity {

    TextView tv_brand;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_subscribe);

        String brand;
        Intent intent = getIntent();

        brand = intent.getExtras().getString("brand");

        tv_brand = findViewById(R.id.tv_brand);
        tv_brand.setText(brand+"(을)를\n아직 구독하지 않고 계시네요!\n구독하여 더 다양한 뉴스레터를 \n받아보시겠어요?");


    }

}

package com.makeus.pineapple.home.filters;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;

public class PopupFilter extends Activity {

    RecyclerView rv_filter_brand;

    public PopupFilter() {
    }

    @Override
    public void finish() {
        super.finish();
        //종료 애니메이션 설정
        overridePendingTransition(R.anim.bottom_down,R.anim.bottom_down);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_filter);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명
            WindowManager.LayoutParams params = window.getAttributes();
            // 화면에 가득 차도록
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
/*
            // 열기&닫기 시 애니메이션 설정
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);*/

            // UI 하단 정렬
            window.setGravity(Gravity.BOTTOM);
        }

        //브랜드 리사이클러뷰
        rv_filter_brand = findViewById(R.id.rv_filter_brand);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rv_filter_brand.setLayoutManager(layoutManager);
        FilterBrandAdapter filterBrandAdapter = new FilterBrandAdapter();

        filterBrandAdapter.addItem(new FilterBrand("디독"));
        filterBrandAdapter.addItem(new FilterBrand("뉴닉"));
        filterBrandAdapter.addItem(new FilterBrand("어피티"));

        rv_filter_brand.setAdapter(filterBrandAdapter); //리사이클러뷰에 어답터 설정


    }
}


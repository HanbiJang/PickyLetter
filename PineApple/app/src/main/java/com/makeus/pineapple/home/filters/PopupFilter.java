package com.makeus.pineapple.home.filters;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;
import com.makeus.pineapple.server_controllers.get.GetSubPlatformFilter;

public class PopupFilter extends Activity {

    public static RecyclerView rv_filter_brand;
    Button btn_check;
    FrameLayout fl_end_date, fl_start_date, fl_ok;


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

            // 열기&닫기 시 애니메이션 설정
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);

            // UI 하단 정렬
            window.setGravity(Gravity.BOTTOM);
        }

        findViewByIdAll();

        //브랜드 리사이클러뷰
        setRv();



    }

    private void setRv() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rv_filter_brand.setLayoutManager(layoutManager);
        FilterBrandAdapter filterBrandAdapter = new FilterBrandAdapter();

        //브랜드 정보 get요청
        GetSubPlatformFilter getSubPlatformFilter = new GetSubPlatformFilter(getApplicationContext());
        getSubPlatformFilter.tryRequest();
    }

    void findViewByIdAll(){
        rv_filter_brand = findViewById(R.id.rv_filter_brand);
        btn_check = findViewById(R.id.btn_check);
        fl_end_date = findViewById(R.id.fl_end_date);
        fl_start_date = findViewById(R.id.fl_start_date);
        fl_ok = findViewById(R.id.fl_ok);
    }
}


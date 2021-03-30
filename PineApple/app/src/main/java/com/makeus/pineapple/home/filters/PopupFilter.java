package com.makeus.pineapple.home.filters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;
import com.makeus.pineapple.server_controllers.get.GetSubPlatformFilter;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;

public class PopupFilter extends Activity {

    public static RecyclerView rv_filter_brand;
    Button btn_date, btn_down1, btn_down2; //기간 버튼
    FrameLayout fl_end_date, fl_start_date, fl_ok;
    public static TextView tv_start_days, tv_start_year_month, tv_start_day,
            tv_end_days, tv_end_year_month, tv_end_day, tv_start, tv_end, tv__;
    private boolean isbtn_dateClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //색처리

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn_down1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyGray)));
            btn_down2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyGray)));
        }

        //브랜드 리사이클러뷰
        setRv();

        //확인 버튼
        fl_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메인 화면의 Filter 버튼 활성화

            }
        });

        //끝 날짜 설정 버튼
        fl_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PopupFilter.this, PopupEndDatePicker.class);
                startActivity(intent);


            }
        });


        //시작 날짜 설정 버튼
        fl_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PopupFilter.this, PopupStartDatePicker.class);
                startActivity(intent);


            }
        });


        btn_date.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                //기간 버튼 클릭 시 전환
                if (isbtn_dateClicked == false) {
                    btn_date.setBackgroundResource(R.drawable.square_check_line);
                    //날짜 선택 불가능하게 만들기
                    fl_end_date.setClickable(false);
                    fl_end_date.setEnabled(false);
                    fl_start_date.setClickable(false);
                    fl_start_date.setEnabled(false);


                    setUnColor();
                    isbtn_dateClicked = true;

                } else {
                    btn_date.setBackgroundResource(R.drawable.square_check_fill);
                    //날짜 선택 가능하게 만들기
                    fl_end_date.setClickable(true);
                    fl_end_date.setEnabled(true);
                    fl_start_date.setClickable(true);
                    fl_start_date.setEnabled(true);

                    setGrayColor();
                    isbtn_dateClicked = false;

                }

            }
        });


    }

    private void setGrayColor() {
        //날짜 색 변경하기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fl_start_date.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyGray)));
            fl_end_date.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyGray)));
            tv_start.setTextColor(Color.parseColor("#333333"));
            tv_end.setTextColor(Color.parseColor("#333333"));
            tv__.setTextColor(Color.parseColor("#333333"));
            tv_start_days.setTextColor(Color.parseColor("#333333"));
            tv_start_year_month.setTextColor(Color.parseColor("#333333"));
            tv_start_day.setTextColor(Color.parseColor("#333333"));
            tv_end_days.setTextColor(Color.parseColor("#333333"));
            tv_end_year_month.setTextColor(Color.parseColor("#333333"));
            tv_end_day.setTextColor(Color.parseColor("#333333"));
            btn_down1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyGray)));
            btn_down2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyGray)));

        }
    }

    private void setUnColor() {
        //날짜 색 변경하기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fl_start_date.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyUnableGray)));
            fl_end_date.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyUnableGray)));
            tv_start.setTextColor(Color.parseColor("#bdbdbd"));
            tv_end.setTextColor(Color.parseColor("#bdbdbd"));
            tv__.setTextColor(Color.parseColor("#bdbdbd"));
            tv_start_days.setTextColor(Color.parseColor("#bdbdbd"));
            tv_start_year_month.setTextColor(Color.parseColor("#bdbdbd"));
            tv_start_day.setTextColor(Color.parseColor("#bdbdbd"));
            tv_end_days.setTextColor(Color.parseColor("#bdbdbd"));
            tv_end_year_month.setTextColor(Color.parseColor("#bdbdbd"));
            tv_end_day.setTextColor(Color.parseColor("#bdbdbd"));
            btn_down1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyUnableGray)));
            btn_down2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyUnableGray)));
        }
    }

    private void setRv() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_filter_brand.setLayoutManager(layoutManager);
        FilterBrandAdapter filterBrandAdapter = new FilterBrandAdapter();

        //구독중인 브랜드 정보 get요청
        GetSubPlatformFilter getSubPlatformFilter = new GetSubPlatformFilter(getApplicationContext());
        getSubPlatformFilter.tryRequest();
    }

    void findViewByIdAll() {
        rv_filter_brand = findViewById(R.id.rv_filter_brand);
        btn_date = findViewById(R.id.btn_date);
        fl_end_date = findViewById(R.id.fl_end_date);
        fl_start_date = findViewById(R.id.fl_start_date);
        fl_ok = findViewById(R.id.fl_ok);

        //
        tv_start_days = findViewById(R.id.tv_start_days);
        tv_start_year_month = findViewById(R.id.tv_start_year_month);
        tv_start_day = findViewById(R.id.tv_start_day);
        tv_end_days = findViewById(R.id.tv_end_days);
        tv_end_year_month = findViewById(R.id.tv_end_year_month);
        tv_end_day = findViewById(R.id.tv_end_day);
        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        tv__ = findViewById(R.id.tv__);

        btn_down1 = findViewById(R.id.btn_down1);
        btn_down2 = findViewById(R.id.btn_down2);


    }
}


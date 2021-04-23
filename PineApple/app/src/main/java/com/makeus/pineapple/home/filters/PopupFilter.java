package com.makeus.pineapple.home.filters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.popup.PopupEndDatePicker;
import com.makeus.pineapple.popup.PopupStartDatePicker;
import com.makeus.pineapple.server_controllers.get.GetSubPlatformFilter;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PopupFilter extends Activity {

    public static RecyclerView rv_filter_brand;
    Button btn_date, btn_down1, btn_down2,  btn_ok; //기간 버튼
    FrameLayout fl_end_date, fl_start_date, fl_ok;
    public static TextView tv_start_days, tv_start_year_month, tv_start_day,
            tv_end_days, tv_end_year_month, tv_end_day, tv_start, tv_end, tv__;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_filter);

        setDialog(); //다이얼로그 UI 설정

        findViewByIdAll(); //UI 설정

        //색처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn_down1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyGray)));
            btn_down2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PopupFilter.this, R.color.pickyGray)));
        }

        setRv(); //브랜드 리사이클러뷰 데이터 세팅, 설정

        setBtn_date_init();//기간 버튼 초기 설정

        setDateinit();  //날짜 초기 설정

        //확인 버튼
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메인 화면의 Filter 버튼 활성화, 비활성화 기준
                //1. 기간 버튼이 활성화 되어있고, endDate, startDate 의 값이 null이 아니거나 (기간 검색)
                //2. 브랜드 네임이 1개라도 선택되어 있다면 필터 적용됨 (브랜드 검색)
                //3. 브랜드와 기간 검색 (둘다 검색)
                //4. 아무것도 선택되지 않았을 때( => 비활성화)

                //요청을 위한 초기화
                //스크롤할때마다 page값 증가하므로 초기화
                Fragment1_Home.pageBottom = -1;
                Fragment1_Home.pageLimitBottom = 0;
                Fragment1_Home.setLoadingPopupOld = false;
                Fragment1_Home.isLoadingBottomRv = false;  //하단 무한 스크롤

                if (Fragment1_Home.isbtn_dateClicked == true && Fragment1_Home.endDate != null && Fragment1_Home.startDate != null
                        && Fragment1_Home.brandNameList.size() == 0) { //1. 기간 검색
                    //필터 버튼 활성화
                    Fragment1_Home.isFilterStart_date = true;
                    Fragment1_Home.isFilterStart_brand = false;
                    Fragment1_Home.isFilterStart_all = false;

                    Fragment1_Home.btn_filter.setBackgroundResource(R.drawable.btn_filter_fill);

                    Fragment1_Home.setBottomRv(Fragment1_Home.view);

                    finish();
                    //애니메이션 설정
                    overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

                }
                else if(Fragment1_Home.brandNameList.size() != 0
                        && Fragment1_Home.isbtn_dateClicked == false){ //2.브랜드 검색
                    //필터 버튼 활성화
                    Fragment1_Home.isFilterStart_date = false;
                    Fragment1_Home.isFilterStart_brand = true;
                    Fragment1_Home.isFilterStart_all = false;

                    Fragment1_Home.btn_filter.setBackgroundResource(R.drawable.btn_filter_fill);

                    Fragment1_Home.setBottomRv(Fragment1_Home.view);

                    finish();

                }
                else if(Fragment1_Home.isbtn_dateClicked == true && Fragment1_Home.endDate != null && Fragment1_Home.startDate != null
                        &&Fragment1_Home.brandNameList.size() != 0
                ){ //3. 모두 검색
                    //필터 버튼 활성화
                    Fragment1_Home.isFilterStart_date = false;
                    Fragment1_Home.isFilterStart_brand = false;
                    Fragment1_Home.isFilterStart_all = true;

                    Fragment1_Home.btn_filter.setBackgroundResource(R.drawable.btn_filter_fill);

                    Fragment1_Home.setBottomRv(Fragment1_Home.view);

                    finish();

                }

                // 기간이 온전하게 선택되지 않았거나, 브랜드가 선택되지 않았을 때 (필터기능 비활성화)
                else {
                    Fragment1_Home.isbtn_dateClicked = false;
                    Fragment1_Home.endDate = null;
                    Fragment1_Home.startDate = null;
                    Fragment1_Home.isFilterStart_date = false;
                    Fragment1_Home.isFilterStart_brand = false;
                    Fragment1_Home.isFilterStart_all = false;
                    Fragment1_Home.btn_filter.setBackgroundResource(R.drawable.btn_filter_line);

                    Fragment1_Home.setBottomRv(Fragment1_Home.view);

                    finish();
                }
            }
        });

        fl_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메인 화면의 Filter 버튼 활성화, 비활성화 기준
                //1. 기간 버튼이 활성화 되어있고, endDate, startDate 의 값이 null이 아니거나 (기간 검색)
                //2. 브랜드 네임이 1개라도 선택되어 있다면 필터 적용됨 (브랜드 검색)
                //3. 브랜드와 기간 검색 (둘다 검색)
                //4. 아무것도 선택되지 않았을 때( => 비활성화)

                //요청을 위한 초기화
                //스크롤할때마다 page값 증가하므로 초기화
                Fragment1_Home.pageBottom = -1;
                Fragment1_Home.pageLimitBottom = 0;
                Fragment1_Home.setLoadingPopupOld = false;
                Fragment1_Home.isLoadingBottomRv = false;  //하단 무한 스크롤

                if (Fragment1_Home.isbtn_dateClicked == true && Fragment1_Home.endDate != null && Fragment1_Home.startDate != null
                && Fragment1_Home.brandNameList.size() == 0) { //1. 기간 검색
                    //필터 버튼 활성화
                    Fragment1_Home.isFilterStart_date = true;
                    Fragment1_Home.isFilterStart_brand = false;
                    Fragment1_Home.isFilterStart_all = false;

                    Fragment1_Home.btn_filter.setBackgroundResource(R.drawable.btn_filter_fill);

                    Fragment1_Home.setBottomRv(Fragment1_Home.view);

                    finish();
                    //애니메이션 설정
                    overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

                }
                else if(Fragment1_Home.brandNameList.size() != 0
                && Fragment1_Home.isbtn_dateClicked == false){ //2.브랜드 검색
                    //필터 버튼 활성화
                    Fragment1_Home.isFilterStart_date = false;
                    Fragment1_Home.isFilterStart_brand = true;
                    Fragment1_Home.isFilterStart_all = false;

                    Fragment1_Home.btn_filter.setBackgroundResource(R.drawable.btn_filter_fill);

                    Fragment1_Home.setBottomRv(Fragment1_Home.view);

                    finish();

                }
                else if(Fragment1_Home.isbtn_dateClicked == true && Fragment1_Home.endDate != null && Fragment1_Home.startDate != null
                &&Fragment1_Home.brandNameList.size() != 0
                ){ //3. 모두 검색
                    //필터 버튼 활성화
                    Fragment1_Home.isFilterStart_date = false;
                    Fragment1_Home.isFilterStart_brand = false;
                    Fragment1_Home.isFilterStart_all = true;

                    Fragment1_Home.btn_filter.setBackgroundResource(R.drawable.btn_filter_fill);

                    Fragment1_Home.setBottomRv(Fragment1_Home.view);

                    finish();

                }

                // 기간이 온전하게 선택되지 않았거나, 브랜드가 선택되지 않았을 때 (필터기능 비활성화)
                else {
                    Fragment1_Home.isbtn_dateClicked = false;
                    Fragment1_Home.endDate = null;
                    Fragment1_Home.startDate = null;
                    Fragment1_Home.isFilterStart_date = false;
                    Fragment1_Home.isFilterStart_brand = false;
                    Fragment1_Home.isFilterStart_all = false;
                    Fragment1_Home.btn_filter.setBackgroundResource(R.drawable.btn_filter_line);

                    Fragment1_Home.setBottomRv(Fragment1_Home.view);

                    finish();
                }
            }
        });

        //끝 날짜 설정 버튼
        fl_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PopupFilter.this, PopupEndDatePicker.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

            }
        });


        //시작 날짜 설정 버튼
        fl_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PopupFilter.this, PopupStartDatePicker.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

            }
        });

        //기간 설정 버튼
        btn_date.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                //기간 버튼 클릭 시 전환
                if (Fragment1_Home.isbtn_dateClicked == false) {
                    Log.e("0", Fragment1_Home.isbtn_dateClicked + "");
                    btn_date.setBackgroundResource(R.drawable.square_check_line);

                    //날짜 선택 가능하게 만들기
                    fl_end_date.setClickable(true);
                    fl_end_date.setEnabled(true);
                    fl_start_date.setClickable(true);
                    fl_start_date.setEnabled(true);

                    setGrayColor();
                    Fragment1_Home.isbtn_dateClicked = true;


                } else {
                    Log.e("0", Fragment1_Home.isbtn_dateClicked + "");
                    btn_date.setBackgroundResource(R.drawable.square_check_fill);

                    //날짜 선택 불가능하게 만들기
                    fl_end_date.setClickable(false);
                    fl_end_date.setEnabled(false);
                    fl_start_date.setClickable(false);
                    fl_start_date.setEnabled(false);

                    setUnColor();
                    Fragment1_Home.isbtn_dateClicked = false;

                    //날짜 데이터 모두 삭제
                    Fragment1_Home.endDate = null;
                    Fragment1_Home.startDate = null;

                }

            }
        });


    }

    private void setDateinit() {
        if (Fragment1_Home.endDate != null && Fragment1_Home.startDate != null) {
            setEndDateinit();
            setStartDateinit();

        } else {
            tv_end_days.setText("00");
            tv_end_day.setText(" ");
            tv_end_year_month.setText("종료날짜");

            tv_start_days.setText("00");
            tv_start_day.setText(" ");
            tv_start_year_month.setText("시작날짜");
        }

    }

    private void setStartDateinit() {
        pickyyyyMMDDStart(Fragment1_Home.startDate);

    }

    private void setEndDateinit() {
        pickyyyyMMDDEnd(Fragment1_Home.endDate);
    }

    //string 에서 년월일, 요일 뽑기
    private void pickyyyyMMDDEnd(String today) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer year = Integer.valueOf(new SimpleDateFormat("yyyy").format(date)); // 년
        Integer month = Integer.valueOf(new SimpleDateFormat("MM").format(date)); // 월
        Integer days = Integer.valueOf(new SimpleDateFormat("dd").format(date)); // 일

        MailboxRequestData mailboxRequestData1 = new MailboxRequestData();
        String day = mailboxRequestData1.calDay(today);

        tv_end_days.setText(days + "");
        tv_end_day.setText(day);
        tv_end_year_month.setText(year + "년 " + month + "월");
    }

    //string 에서 년월일, 요일 뽑기
    private void pickyyyyMMDDStart(String today) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer year = Integer.valueOf(new SimpleDateFormat("yyyy").format(date)); // 년
        Integer month = Integer.valueOf(new SimpleDateFormat("MM").format(date)); // 월
        Integer days = Integer.valueOf(new SimpleDateFormat("dd").format(date)); // 일

        MailboxRequestData mailboxRequestData1 = new MailboxRequestData();
        String day = mailboxRequestData1.calDay(today);

        tv_start_days.setText(days + "");
        tv_start_day.setText(day);
        tv_start_year_month.setText(year + "년 " + month + "월");
    }

    private void setBtn_date_init() {

        if (Fragment1_Home.isbtn_dateClicked == false) {
            btn_date.setBackgroundResource(R.drawable.square_check_fill);
            //날짜 선택 불가능하게 만들기
            fl_end_date.setClickable(false);
            fl_end_date.setEnabled(false);
            fl_start_date.setClickable(false);
            fl_start_date.setEnabled(false);

            setUnColor();
        } else {
            btn_date.setBackgroundResource(R.drawable.square_check_line);
            //날짜 선택 가능하게 만들기
            fl_end_date.setClickable(true);
            fl_end_date.setEnabled(true);
            fl_start_date.setClickable(true);
            fl_start_date.setEnabled(true);

            setGrayColor();

        }
    }

    private void setDialog() {
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
        btn_ok = findViewById(R.id.btn_ok);


    }
}


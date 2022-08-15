package com.makeus.pineapple.popup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.makeus.pineapple.R;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.home.filters.PopupFilter;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PopupEndDatePicker extends Activity {

    Integer year,month,day;
    Button btn_no, btn_yes;

    @Override //바깥영역 터치 막기
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect dialogBoinds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBoinds);

        if (!dialogBoinds.contains((int) ev.getX(), (int) ev.getY())) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_datepicker2);

        //다이얼로그 설정
        setDialog();

        DatePicker datePicker = (DatePicker)findViewById(R.id.dataPicker);
        btn_no = findViewById(R.id.btn_no);
        btn_yes = findViewById(R.id.btn_yes);

        //오늘 날짜 구하기
        if(Fragment1_Home.endDate == null){ //이전에 설정하기 않은 경우
            MailboxRequestData mailboxRequestData = new MailboxRequestData();
            String today =  mailboxRequestData.calToday();
            pickyyyyMMDD(today);        //"yyyy-MM-dd" 형식 String에서 년월일 추출하기
        }
        else{ // 이전에 설정한 경우
            pickyyyyMMDD(Fragment1_Home.endDate);
        }

        //년월일 추출해서 초기화하기
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                monthOfYear+=1;

                String date = year + "-" + monthOfYear + "-" + dayOfMonth;

                //필터UI의 날짜 변경
                //년월
                PopupFilter.tv_end_year_month.setText(year + "년 "+ monthOfYear +"월");
                //일
                PopupFilter.tv_end_days.setText(dayOfMonth+"");
                //요일
                MailboxRequestData mailboxRequestData1 = new MailboxRequestData();
                PopupFilter.tv_end_day.setText(mailboxRequestData1.calDay(date)); //요일 계산하기
                Log.e("요일", mailboxRequestData1.calDay(date));

                //yyyy-MM-dd 형식으로 바꿔서 저장하기
                String month_0 =  String.format("%02d", monthOfYear); //2자리로 나타내기
                String day_0 =  String.format("%02d", dayOfMonth); //2자리로 나타내기
                Fragment1_Home.endDate = year + "-" + month_0 + "-" + day_0; //yyyy-MM-dd 형식으로 저장했음

            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PopupEndDatePicker.this, "날짜가 선택되었습니다", Toast.LENGTH_SHORT).show();

                finish();
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //필터UI의 날짜 변경
                //년월
                PopupFilter.tv_end_year_month.setText("종료날짜");
                //일
                PopupFilter.tv_end_days.setText("00");
                //요일
                PopupFilter.tv_end_day.setText(null); //요일 계산하기

                Fragment1_Home.endDate = null; //yyyy-MM-dd 형식으로 저장했음

                finish();
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
            }
        });




    }

    private void pickyyyyMMDD(String today) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        year = Integer.valueOf(new SimpleDateFormat("yyyy").format(date)); // 년
        month = Integer.valueOf(new SimpleDateFormat("MM").format(date)) - 1; // 월
        day = Integer.valueOf(new SimpleDateFormat("dd").format(date)); // 일

    }

    private void setDialog() {
        //팝업설정
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명
            WindowManager.LayoutParams params = window.getAttributes();

            // 열기&닫기 시 애니메이션 설정
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);

        }
    }

}

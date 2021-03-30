package com.makeus.pineapple.loading;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.makeus.pineapple.HomeMail;
import com.makeus.pineapple.R;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;
import com.makeus.pineapple.search.Fragment2_Search;

import java.util.Timer;
import java.util.TimerTask;

public class PopupLoading extends Activity {

    @Override //바깥영역 터치 막기
    public boolean dispatchTouchEvent(MotionEvent ev){
        Rect dialogBoinds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBoinds);

        if(!dialogBoinds.contains((int)ev.getX(), (int) ev.getY())){
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_loading);

        //팝업설정
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명
            WindowManager.LayoutParams params = window.getAttributes();

        }


        Intent intent = getIntent();
        Integer pastFragmentNum = intent.getExtras().getInt("pastFragmentNum");

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (pastFragmentNum == 1) {
                    Log.e("1", Fragment1_Home.setLoadingPopupNew +   "     "+ Fragment1_Home.setLoadingPopupOld );
                    if (Fragment1_Home.setLoadingPopupNew == true && Fragment1_Home.setLoadingPopupOld == true) {
                        timer.cancel();
                        finish();
                        overridePendingTransition(R.anim.none,R.anim.exit_small);
                    }
                } else if (pastFragmentNum == 2) {
                    if (Fragment2_Search.setLoadingPopup == true) {
                        timer.cancel();
                        finish();
                        overridePendingTransition(R.anim.none,R.anim.exit_small);
                    }
                } else if (pastFragmentNum == 3) {
                    if (Fragment3_MyPage.setLoadingPopup == true) {
                        timer.cancel();
                        finish();
                        overridePendingTransition(R.anim.none,R.anim.exit_small);
                    }
                } /*else if (pastFragmentNum == 4) { //홈뷰
                    if (HomeMail.setLoadingPopup == true) {
                        Log.e(" ","홈뷰 로딩끝");
                        timer.cancel();
                        finish();
                        overridePendingTransition(R.anim.none,R.anim.exit_small);
                    }
                }*/

            }
        };


        timer.schedule(timerTask, 0, 500); //0초후 첫실행, 0.2초마다 실행


    }

}

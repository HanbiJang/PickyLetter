package com.makeus.pineapple.popup;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.makeus.pineapple.R;
import com.makeus.pineapple.server_controllers.delete.DeleteUser;
import com.makeus.pineapple.sign.SignIn;

public class PopupOut extends Activity {

    Button btn_no, btn_yes;

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
        setContentView(R.layout.popup_out);

        //팝업설정
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명
            WindowManager.LayoutParams params = window.getAttributes();

        }

        //
        findViewByIdAll();

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
            }
        });


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //탈퇴 요청
                DeleteUser deleteUser = new DeleteUser(
                  getApplicationContext()
                );

                deleteUser.tryRequest();
                //로그인 화면으로
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

                //로그아웃때처럼 토큰, 사용자 정보 날리기
                //shared Preferences 값 지우기
                SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);    // user 이름의 기본모드 설정
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
                editor.remove("token"); // key,value 형식으로 저장
                editor.remove("userId"); // key,value 형식으로 저장
                editor.remove("nickName"); // key,value 형식으로 저장
                editor.commit();    //최종 커밋. 커밋을 해야 저장이 된다.
            }
        });



    }

    private void findViewByIdAll() {
        btn_no = findViewById(R.id.btn_no);
        btn_yes = findViewById(R.id.btn_yes);
    }

}

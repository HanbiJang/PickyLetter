package com.makeus.pineapple.mypage_settings.settings;

import android.app.Activity;
import android.content.DialogInterface;
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

            }
        });



    }

    private void findViewByIdAll() {
        btn_no = findViewById(R.id.btn_no);
        btn_yes = findViewById(R.id.btn_yes);
    }

}

package com.makeus.pineapple.popup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.makeus.pineapple.R;
import com.makeus.pineapple.server_controllers.delete.DeleteSubPlatform;
import com.makeus.pineapple.server_controllers.post.PostAddPlatform;

public class PopupUnSub extends Activity {

    Button btn_no, btn_yes;
    TextView tv_brand;

    String brand;
    public static Integer platformId;

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
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_unsubscribe);

        //팝업설정
        setDialog();

        findViewByIdAll();

        Intent intent = getIntent();
        brand = intent.getExtras().getString("brand");
        platformId = intent.getExtras().getInt("platformId");

        tv_brand.setText(brand+"을(를) 정말 구독해지하시겠어요?");



        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구독해지 요청
                DeleteSubPlatform deleteSubPlatform = new DeleteSubPlatform(
                        PopupUnSub.this,
                        platformId
                );

                deleteSubPlatform.tryRequest();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();

                        Intent intent1 = new Intent(PopupUnSub.this, PopupEmpty.class);
                        startActivity(intent1);
                    }
                },500);
            }
        });




    }

    private void findViewByIdAll() {
        tv_brand = findViewById(R.id.tv_brand);
        btn_no = findViewById(R.id.btn_no);
        btn_yes = findViewById(R.id.btn_yes);
    }

    private void setDialog() {
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

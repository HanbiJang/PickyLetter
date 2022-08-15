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
import com.makeus.pineapple.search.Fragment2_Search;
import com.makeus.pineapple.server_controllers.post.PostAddPlatform;

public class PopupSub extends Activity {

    Button btn_no, btn_yes;
    TextView tv_brand;

    String brand;
    public static Integer platformId;

    String preView;

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
        setContentView(R.layout.popup_subscribe);

        //팝업설정
        setDialog();

        Intent intent = getIntent();
        brand = intent.getExtras().getString("brand");
        platformId = intent.getExtras().getInt("platformId");
        preView = intent.getExtras().getString("preView");


        findViewByIdAll();



        tv_brand.setText(brand + "(을)를\n아직 구독하지 않고 계시네요!\n구독하여 더 다양한 뉴스레터를 \n받아보시겠어요?");


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구독 요청
                PostAddPlatform postAddPlatform = new PostAddPlatform(
                        PopupSub.this,
                        platformId
                );
                postAddPlatform.tryRequest();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(preView != null){
                            if (preView.equals("search")) {
                                //구독을 하고 돌아왔을 시
                                //랭킹 rv와 검색 rv 새로고침
                                Fragment2_Search.setLoadingPopup = false; //로딩팝업 관련
                                Fragment2_Search.setRankRv(Fragment2_Search.view);
                                Fragment2_Search.sr_layout.setRefreshing(false); //새로고침 멈춤

                                //검색 재검색
                                if (Fragment2_Search.searchKeyword != null) {
                                    Fragment2_Search.lastLetterId = 0;

                                    //검색 초기화
                                    Fragment2_Search.et_search.setText(null);
                                    Fragment2_Search.rv_search_result.setAdapter(null);

                                    Fragment2_Search.tv_search_result.setText("'" + Fragment2_Search.searchKeyword + "'" + " 검색 결과");
                                    Fragment2_Search.ll_search_result.setVisibility(View.VISIBLE);
                                    // 검색 결과 리사이클러뷰 결과 만들기

                                    Fragment2_Search.setResultRv(Fragment2_Search.view);

                                }
                            }
                        }

                        Intent intent1 = new Intent(PopupSub.this, PopupEmpty.class);
                        startActivity(intent1);

                        finish();
                    }
                }, 550);


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

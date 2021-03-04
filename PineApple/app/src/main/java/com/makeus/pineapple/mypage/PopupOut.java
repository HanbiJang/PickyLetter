package com.makeus.pineapple.mypage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.makeus.pineapple.R;

public class PopupOut extends Activity {

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_out);

    }

}

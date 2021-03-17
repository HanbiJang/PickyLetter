package com.makeus.pineapple.sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.makeus.pineapple.R;

public class SignUp_1 extends Activity {

    Button btn_next;
    EditText et_name;
    LinearLayout ll_name;
    //
    String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_1);

        //findViewById
        btn_next = findViewById(R.id.btn_next);
        et_name = findViewById(R.id.et_name);
        ll_name = findViewById(R.id.ll_name);

        //버튼 조건 처리
        btn_next.setClickable(false);
        btn_next.setEnabled(false);
        setBtnNext();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = et_name.getText().toString();

                Intent intent = new Intent(SignUp_1.this, SignUp_2.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right, R.anim.exit_left);

                finish();

            }
        });

    }

    public void setBtnNext() {
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtnClickable();

            }
        });

    }

    public void setBtnClickable() {
        if (et_name.length() > 0) {
            btn_next.setTextColor(getResources().getColor(R.color.pickyGray));
            btn_next.setBackgroundResource(R.drawable.round_squre_coral);
            btn_next.setClickable(true);
            btn_next.setEnabled(true);
            ll_name.setVisibility(View.INVISIBLE);
        } else {
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_next.setBackgroundResource(R.drawable.round_squre_gray);
            btn_next.setClickable(false);
            btn_next.setEnabled(false);
            ll_name.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SignUp_1.this, SignIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_left_pop, R.anim.exit_left_pop);

        finish();
    }
}

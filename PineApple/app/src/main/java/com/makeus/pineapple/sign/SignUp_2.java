package com.makeus.pineapple.sign;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.R;
import com.makeus.pineapple.sign.signUp.EmailCheckResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp_2 extends Activity {
    static String isExistUrl = "http://3.13.65.158/v1/users/exists"; //중복체크 api
    static RequestQueue requestQueue;
    static boolean isEmailRedundant;
    static String email;

    Button btn_x;
    Button btn_next;
    EditText et_mail, et_name;
    LinearLayout ll_mail;

    TextView tv_mail;

    String userName, userMail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup_2);

        //findViewById
        btn_x = findViewById(R.id.btn_x);
        btn_next = findViewById(R.id.btn_next);
        et_mail = findViewById(R.id.et_mail);
        et_name = findViewById(R.id.et_name);
        ll_mail = findViewById(R.id.ll_mail);
        tv_mail = findViewById(R.id.tv_mail);

        //정보 가져오기
        intentDataSetting();

        btn_x.setVisibility(View.INVISIBLE);
        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_mail.setText(null);
                btn_x.setVisibility(View.INVISIBLE);
            }
        });

        //버튼 조건 충족 시 클릭 가능하게 설정
        btn_next.setEnabled(false);
        btn_next.setClickable(false);
        setBtnNext();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userMail = et_mail.getText().toString();

                //받은 email 주소로 이메일 중복 체크하기
                tryEmailCheck();

            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext()); // 큐 객체 생성하기
        }

    }

    private void tryEmailCheck() {
        email = et_mail.getText().toString();
        JSONObject requestData2 = makeJsonObjectForEmailCheck(email);
        makeRequrstPost(requestData2,isExistUrl);
    }

    private JSONObject makeJsonObjectForEmailCheck(String email) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("email", email);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return requestData;
    }

    //이메일 체크 요청 보내기
    public void makeRequrstPost(JSONObject requestData,String urlData) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                urlData,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponseForCheck(response);
                        processEmailCheck();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ll_mail.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUp_2.this, "올바른 이메일을 입력해주세요 ", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            et_mail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#eb5757"))); //레드
                            tv_mail.setTextColor(Color.parseColor("#eb5757"));
                            ll_mail.setVisibility(View.VISIBLE);
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //여기 파라미터 넣는 거 아님
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);

    }

    //이메일 중복 체크 마무리 처리
    private void processEmailCheck() {
        if (isEmailRedundant == true){
            Toast.makeText(this, "이메일 중복입니다", Toast.LENGTH_SHORT).show();
            tv_mail.setTextColor(Color.parseColor("#eb575757"));
            ll_mail.setVisibility(View.VISIBLE);
        }
        else{
            //다음 단계로 넘어가기
            Intent intent = new Intent(SignUp_2.this, SignUp_3.class);
            intent.putExtra("userName",userName);
            intent.putExtra("userMail",userMail);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
            finish();

        }

    }

    //이메일 중복 체크 결과 객체로 받기
    public void processResponseForCheck(JSONObject response){
        Gson gson = new Gson();
        EmailCheckResult emailCheckResult = gson.fromJson(String.valueOf(response), EmailCheckResult.class);
        isEmailRedundant = emailCheckResult.getResult().booleanValue();
    }

    public void intentDataSetting(){
        Intent intent = getIntent();
        userName = intent.getExtras().getString("userName");
        et_name.setText(userName);
    }

    public void setBtnNext() {
        et_mail.addTextChangedListener(new TextWatcher() {
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
        if (et_mail.length() > 0) {
            btn_x.setVisibility(View.VISIBLE);//버튼 보이기

            btn_next.setTextColor(getResources().getColor(R.color.pickyGray));
            btn_next.setBackgroundResource(R.drawable.round_squre_coral);
            btn_next.setClickable(true);
            btn_next.setEnabled(true);
            ll_mail.setVisibility(View.INVISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv_mail.setTextColor(Color.parseColor("#333333"));
                et_mail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#333333"))); //그레이
            }
        } else {
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_next.setBackgroundResource(R.drawable.round_squre_gray);
            btn_next.setClickable(false);
            btn_next.setEnabled(false);
            ll_mail.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv_mail.setTextColor(Color.parseColor("#eb5757"));
                et_mail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#eb5757"))); //레드
            }
        }
    }


    /* 뒤로가기 버튼 막기 */
    @Override
    public void onBackPressed() {

    }
}

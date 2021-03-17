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
import com.makeus.pineapple.sign.users.UserResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp_3 extends Activity {
    static String signUpUrl = "http://3.13.65.158/v1/users"; // 회원가입 url
    static RequestQueue requestQueue;
    String token = null;

    Button btn_next;
    EditText et_pw,et_name,et_mail;
    LinearLayout ll_pw;

    String userName, userMail, userPw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup_3);

        //findViewById
        btn_next = findViewById(R.id.btn_next);
        et_pw = findViewById(R.id.et_pw);
        et_name = findViewById(R.id.et_name);
        et_mail = findViewById(R.id.et_mail);
        ll_pw = findViewById(R.id.ll_pw);


        //데이터 세팅
        intentDataSetting();

        //버튼 조건처리
        btn_next.setEnabled(false);
        btn_next.setClickable(false);
        setBtnNext();

        //회원가입 완료 버튼
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //회원가입 동작
                trySignUp();

                Intent intent = new Intent(SignUp_3.this, SignIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
                finish();

            }
        });


        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext()); // 큐 객체 생성하기
        }

    }

    private void trySignUp() {
        userMail = et_mail.getText().toString();
        userPw = et_pw.getText().toString();
        userName = et_name.getText().toString();

        JSONObject requestData3 = makeJsonObjectForSignUp(userMail,userPw,userName);
        makeRequrstPost(requestData3,signUpUrl);
    }

    private void makeRequrstPost(JSONObject requestData, String signUpUrl) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                signUpUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponseForSignUp(response); //회원가입 결과 데이터 처리
                        processSignUp(); //회원가입 동작 처리
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp_3.this, "회원가입 에러", Toast.LENGTH_SHORT).show();

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

    //회원가입 동작 처리
    private void processSignUp() {
        if(token != null){
            Toast.makeText(this, "회원가입 성공" + token, Toast.LENGTH_SHORT).show();
        }
    }

    //회원가입 요청 결과 데이터 처리
    private void processResponseForSignUp(JSONObject response) {
        Gson gson = new Gson();
        UserResult userResult = gson.fromJson(String.valueOf(response), UserResult.class);
        token = userResult.getToken();
    }

    private JSONObject makeJsonObjectForSignUp(String userMail, String userPw, String userName) {
        JSONObject requestData = new JSONObject();

        try {
            requestData.put("email", userMail);
            requestData.put("password", userPw);
            requestData.put("nickname", userName);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return requestData;
    }

    public void intentDataSetting(){
        Intent intent = getIntent();
        userName = intent.getExtras().getString("userName");
        userMail = intent.getExtras().getString("userMail");
        et_mail.setText(userMail);
        et_name.setText(userName);

    }

    public void setBtnNext() {
        et_pw.addTextChangedListener(new TextWatcher() {
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
        if (et_pw.length() >= 8) {
            btn_next.setTextColor(getResources().getColor(R.color.pickyGray));
            btn_next.setBackgroundResource(R.drawable.round_squre_coral);
            btn_next.setClickable(true);
            btn_next.setEnabled(true);
            ll_pw.setVisibility(View.INVISIBLE);
        } else {
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_next.setBackgroundResource(R.drawable.round_squre_gray);
            btn_next.setClickable(false);
            btn_next.setEnabled(false);
            ll_pw.setVisibility(View.VISIBLE);
        }
    }


    /* 뒤로가기 버튼 막기 */
    @Override
    public void onBackPressed() {

    }
}

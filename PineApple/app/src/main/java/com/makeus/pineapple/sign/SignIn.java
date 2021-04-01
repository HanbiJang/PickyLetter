package com.makeus.pineapple.sign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.sign.users.UserResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignIn extends Activity {

    static String loginUrl = "http://3.13.65.158/v1/users/login"; //로그인 url
    static RequestQueue requestQueue;
    public static String token = null;
    public static Integer userId = null;
    static String userEmail;
    static String userpw;
    public static String userNickName;
    boolean loginResult;

    Button btn_x_pw, btn_x_mail;
    Button btn_login, btn_signup, btn_eye;
    public static EditText et_id, et_pw;
    Boolean isClicked = false;    //비밀번호 보이기 버튼 관련
    FrameLayout fl_pw, fl_mail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //-------------------------------------------------------------

        //자동 로그인
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        String token = sharedPreferences.getString("token","");
        Integer userId = sharedPreferences.getInt("userId",-1);
        String userNickName = sharedPreferences.getString("userNickName","");

        if(token != "" || userId != -1 || userNickName != ""){
            Intent intent = new Intent(SignIn.this, MainActivity.class);
            intent.putExtra("token", token); //인텐트로 token 정보를 넘김
            intent.putExtra("userId", userId); //인텐트로 userId 정보를 넘김
            intent.putExtra("nickName", userNickName); //인텐트로 userId 정보를 넘김
            startActivity(intent);
            overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
            finish();
        }


        //-------------------------------------------------------------

        //findViewById
        btn_x_pw = findViewById(R.id.btn_x_pw);
        btn_x_mail = findViewById(R.id.btn_x_mail);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        btn_eye = findViewById(R.id.btn_eye);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        fl_mail = findViewById(R.id.fl_mail);
        fl_pw = findViewById(R.id.fl_pw);

        //로그인
        //색전환 처리
        setBtnLogin();

        //비번칸 초기화
        et_pw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT ) ;

        btn_x_pw.setVisibility(View.INVISIBLE);
        btn_x_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pw.setText(null);
                btn_x_pw.setVisibility(View.INVISIBLE);
            }
        });

        btn_x_mail.setVisibility(View.INVISIBLE);
        btn_x_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_id.setText(null);
                btn_x_mail.setVisibility(View.INVISIBLE);
            }
        });



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //아이디, 비번 입력 받기
                userpw = et_pw.getText().toString();
                userEmail = et_id.getText().toString();

                //로그인 시도
                tryLogin(userEmail, userpw);

            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext()); // 큐 객체 생성하기
        }

        //비밀번호 보이기 버튼
        btn_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    btn_eye.setBackgroundResource(R.drawable.btn_opened_eyes);
                    et_pw.setInputType(InputType.TYPE_CLASS_TEXT);
                    isClicked = true;
                } else {
                    btn_eye.setBackgroundResource(R.drawable.btn_eye_close);
                    et_pw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT ) ;
                    isClicked = false;
                }

            }
        });

        //회원가입
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this, SignUp_1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
                finish();

            }
        });


    }

    private void tryLogin(String userId, String userpw) {
        //로그인 버튼 막기
        btn_login.setClickable(false);
        btn_login.setEnabled(false);

        JSONObject requestData1 = makeJsonObjectForLogin(userId, userpw);
        makeRequestPost(requestData1, loginUrl);
    }

    private JSONObject makeJsonObjectForLogin(String userId, String password) {
        JSONObject requestData = new JSONObject();

        try {
            requestData.put("email", userId);
            requestData.put("password", password);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return requestData;
    }

    //로그인 요청 함수
    private void makeRequestPost(JSONObject requestData, String urlData) {

        loginResult = true;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                urlData,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loginResult = true;
                        processResponseForLogin(response);
                        processLogin(); //로그인 처리 하게 만들기
                        //로그인 버튼 활성화하기
                        btn_login.setClickable(true);
                        btn_login.setEnabled(true);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //로그인 실패
                        loginResult = false;
                        Toast.makeText(SignIn.this, "올바른 아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        //로그인 버튼 활성화하기
                        btn_login.setClickable(true);
                        btn_login.setEnabled(true);
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

    private void processLogin() {

        if (loginResult == true) {
            //로그인 성공 시
            Intent intent = new Intent(SignIn.this, MainActivity.class);
            intent.putExtra("token", token); //인텐트로 token 정보를 넘김
            intent.putExtra("userId", userId); //인텐트로 userId 정보를 넘김
            intent.putExtra("nickName", userNickName); //인텐트로 userId 정보를 넘김
            startActivity(intent);
            overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
            finish();

            //토큰,userId, nickName 저장하기
            SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);    // user 이름의 기본모드 설정
            SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
            editor.putString("token",token); // key,value 형식으로 저장
            editor.putInt("userId", userId); // key,value 형식으로 저장
            editor.putString("nickName", userNickName); // key,value 형식으로 저장
            editor.commit();    //최종 커밋. 커밋을 해야 저장이 된다.

        }
    }

    public void processResponseForLogin(JSONObject response) {
        Gson gson = new Gson();
        UserResult userResult = gson.fromJson(String.valueOf(response), UserResult.class);
        userId = userResult.getUser().getUserId();
        token = userResult.getToken();
        userNickName = userResult.getUser().getNickname();
    }


    public void setBtnLogin() {
        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_pw.length() > 0){
                    //색변화처리
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fl_pw.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SignIn.this, R.color.pickyGray)));
                    }
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fl_pw.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SignIn.this, R.color.pickyUnableGray)));

                    }
                }

                setBtnClickable();
                if(et_pw.length() > 0){
                    btn_x_pw.setVisibility(View.VISIBLE);
                }
                else{
                    btn_x_pw.setVisibility(View.INVISIBLE);
                }

            }
        });

        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_id.length() > 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fl_mail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SignIn.this, R.color.pickyGray)));
                    }
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fl_mail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SignIn.this, R.color.pickyUnableGray)));

                    }
                }
                setBtnClickable();
                if(et_id.length() > 0){
                    btn_x_mail.setVisibility(View.VISIBLE);
                }
                else{
                    btn_x_mail.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    public void setBtnClickable() {

        if (et_pw.length() > 0 && et_id.length() > 0) {
            btn_login.setTextColor(getResources().getColor(R.color.pickyGray));
            btn_login.setBackgroundResource(R.drawable.round_squre_coral);
            btn_login.setClickable(true);


/*            //색변화처리
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fl_pw.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SignIn.this, R.color.pickyGray)));
                fl_mail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SignIn.this, R.color.pickyGray)));
            }*/

        } else {

            btn_login.setTextColor(getResources().getColor(R.color.white));
            btn_login.setBackgroundResource(R.drawable.round_squre_gray);
            btn_login.setClickable(false);


/*            //색변화처리
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fl_pw.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SignIn.this, R.color.pickyUnableGray)));
                fl_mail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SignIn.this, R.color.pickyUnableGray)));

            }*/

        }
    }
}

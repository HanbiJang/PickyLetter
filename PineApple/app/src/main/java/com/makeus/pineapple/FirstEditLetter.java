package com.makeus.pineapple;

import android.app.Activity;
import android.content.Context;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.makeus.pineapple.server_controllers.get.GetAllPlatform;
import com.makeus.pineapple.server_controllers.get.GetAllPlatform_first;
import com.makeus.pineapple.sign.SignIn;
import com.makeus.pineapple.sign.users.UserResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FirstEditLetter extends Activity {

    Button btn_start;
    FragmentActivity myContext; //화면 전환
    public static RecyclerView rv_edit_letter;

    public static String token = null;
    public static Integer userId = null;
    public static String nickName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_settings_edit_letter);

        //-------------------------------------------------------------
        rv_edit_letter = findViewById(R.id.rv_edit_letter);
        btn_start = findViewById(R.id.btn_start);

        //-------------------------------------------------------------

        //회원 정보 받기
        getIntentData();

        //모든 플랫폼 목록 불러오기 리사이클러뷰
        setRv();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메인 화면으로
                Intent intent = new Intent(FirstEditLetter.this, MainActivity.class);
                intent.putExtra("token", token); //인텐트로 token 정보를 넘김
                intent.putExtra("userId", userId); //인텐트로 userId 정보를 넘김
                intent.putExtra("nickName", nickName); //인텐트로 userId 정보를 넘김
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
                finish();
            }
        });



    }

    private void setRv() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(FirstEditLetter.this, LinearLayoutManager.VERTICAL, false);
        rv_edit_letter.setLayoutManager((layoutManager));

        //모든 플랫폼 가져오기
        GetAllPlatform_first getAllPlatform = new GetAllPlatform_first(
                FirstEditLetter.this
        );

        getAllPlatform.tryRequest();
    }

    private void getIntentData() {
        Intent intent =getIntent();
        userId = intent.getExtras().getInt("userId");
        token = intent.getExtras().getString("token");
        nickName = intent.getExtras().getString("nickName");

    }

    @Override
    public void onResume() {
        super.onResume();

        //리사이클러뷰 재 로딩
        setRv();

    }

}

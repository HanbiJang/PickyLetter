package com.makeus.pineapple;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.mypage.Fragment3_MyPage;
import com.makeus.pineapple.search.Fragment2_Search;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    //사용자 정보
    static String userId = null;
    static String token= null;

    Fragment fragment1_home;
    Fragment fragment2_search;
    Fragment fragment3_mypage;
    BottomNavigationView navigation;

/*    //앱종료시간체크
    long backKeyPressedTime;*/

    public static String getUserId() {
        return userId;
    }

    public static String getToken() {
        return token;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //인텐트에서 userId, token 정보 얻기
        getIntentData();

        //프레그먼트 생성
        fragment1_home = new Fragment1_Home();
        fragment2_search = new Fragment2_Search();
        fragment3_mypage = new Fragment3_MyPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment1_home).commit();

        navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.tab_home:
                                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left).replace(R.id.container_fragment,fragment1_home).commit();
                                return true;

                            case R.id.tab_search:
                                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left).replace(R.id.container_fragment,fragment2_search).commit();
                                return true;

                            case R.id.tab_mypage:
                                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left).replace(R.id.container_fragment,fragment3_mypage).commit();
                                return true;
                        }
                        return false;
                    }
                }
        );


    }

    //인텐트에서 정보 얻기
    private void getIntentData() {
        Intent intent =getIntent();
        userId = intent.getExtras().getString("userId");
        token = intent.getExtras().getString("token");

    }

/*    //뒤로가기 2번하면 앱종료
    @Override
    public void onBackPressed() {
        //1번째 백버튼 클릭
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(MainActivity.this, "'뒤로'버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Log.e(" ","뒤로가기 버튼 누름");
        }
        //2번째 백버튼 클릭 (종료)
        else{
            AppFinish();
        }
    }

    //앱종료
    public void AppFinish(){
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }*/


}
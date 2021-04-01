package com.makeus.pineapple.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.makeus.pineapple.HomeMail;
import com.makeus.pineapple.R;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;
import com.makeus.pineapple.search.Fragment2_Search;

public class MainActivity extends AppCompatActivity {
    public static Activity mainActivity;

    //사용자 정보
    static Integer userId = null;
    static String token= null;
    static String nickName = null;

    static Boolean oneTimeEmpty =false;    //홈화면 empty이미지 띄우는 변수

    public static Fragment fragment1_home;
    public static Fragment fragment2_search;
    public static Fragment fragment3_mypage;
    static BottomNavigationView navigation;

    public static FragmentManager fragmentManager;

    //앱종료시간체크
    long backKeyPressedTime;

    public static Integer getUserId() {
        return userId;
    }

    public static String getToken() {
        Log.e("토큰얻음", token);
        return token;
    }

    public static Boolean getOneTimeEmpty() { return oneTimeEmpty; }

    public static String getNickName() { return nickName; }

    public static void setNickName(String nickName) { MainActivity.nickName = nickName; }

    public static void setOneTimeEmpty(Boolean oneTimeEmpty) { MainActivity.oneTimeEmpty = oneTimeEmpty; }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //메인 액티비티
        mainActivity = MainActivity.this;

        //인텐트에서 userId, token 정보 얻기
        getIntentData();


        fragmentManager = getSupportFragmentManager();//프래그먼트 매니저

        if(fragment1_home == null){
            fragment1_home = new Fragment1_Home();
            fragmentManager.beginTransaction().add(R.id.container_fragment, fragment1_home).commit();
        }

        else{
            if(fragment1_home != null) fragmentManager.beginTransaction().show(fragment1_home).commit();
            if(fragment2_search != null) fragmentManager.beginTransaction().hide(fragment2_search).commit();
            if(fragment3_mypage != null) fragmentManager.beginTransaction().hide(fragment3_mypage).commit();
        }

        navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        toggleNavigationBarItems(false);

                        //네비게이션 디버깅 코드
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.toggleNavigationBarItems(true);
                            }
                        }, 500);

                        if (item.getItemId() == R.id.tab_home){
                            setOneTimeEmpty(false);

                            if(fragment1_home == null){
                                fragment1_home = new Fragment1_Home();
                                fragmentManager.beginTransaction().add(R.id.container_fragment, fragment1_home).commit();
                            }

                            else{
                                if(fragment1_home != null) fragmentManager.beginTransaction().show(fragment1_home).commit();
                                if(fragment2_search != null) fragmentManager.beginTransaction().hide(fragment2_search).commit();
                                if(fragment3_mypage != null) fragmentManager.beginTransaction().hide(fragment3_mypage).commit();
                                if(HomeMail.homeMail != null) fragmentManager.beginTransaction().hide(HomeMail.homeMail).commit();
                            }



                        }
                        else if (item.getItemId() == R.id.tab_search){

                            if(fragment2_search == null){
                                fragment2_search = new Fragment2_Search();
                                fragmentManager.beginTransaction().add(R.id.container_fragment, fragment2_search).commit();
                            }

                            else{
                                if(fragment1_home != null) fragmentManager.beginTransaction().hide(fragment1_home).commit();
                                if(fragment2_search != null) fragmentManager.beginTransaction().show(fragment2_search).commit();
                                if(fragment3_mypage != null) fragmentManager.beginTransaction().hide(fragment3_mypage).commit();
                                if(HomeMail.homeMail != null) fragmentManager.beginTransaction().hide(HomeMail.homeMail).commit();
                            }

                        }
                        else if (item.getItemId() == R.id.tab_mypage){

                            if(fragment3_mypage == null){
                                fragment3_mypage = new Fragment3_MyPage();
                                fragmentManager.beginTransaction().add(R.id.container_fragment, fragment3_mypage).commit();
                            }

                            else{
                                if(fragment1_home != null) fragmentManager.beginTransaction().hide(fragment1_home).commit();
                                if(fragment2_search != null) fragmentManager.beginTransaction().hide(fragment2_search).commit();
                                if(fragment3_mypage != null) fragmentManager.beginTransaction().show(fragment3_mypage).commit();
                                if(HomeMail.homeMail != null) fragmentManager.beginTransaction().hide(HomeMail.homeMail).commit();
                            }
                        }
                        else{
                            return false;
                        }

                        return true;

                    }
                }
        );


    }

    public static void toggleNavigationBarItems(boolean enabled) {
        Menu navMenu = navigation.getMenu();
        for (int i = 0; i < navMenu.size(); ++i) {
            navMenu.getItem(i).setEnabled(enabled);
        }
    }

    //인텐트에서 정보 얻기
    private void getIntentData() {
        Intent intent =getIntent();
        userId = intent.getExtras().getInt("userId");
        token = intent.getExtras().getString("token");
        nickName = intent.getExtras().getString("nickName");

    }

    //뒤로가기 2번하면 앱종료
    @Override
    public void onBackPressed() {

        Log.e(" ","백그라운드 스택수"+ fragmentManager.getBackStackEntryCount());

        //만약 홈메일이 실행 중이라면 그 전 화면으로 가게 만듦
        if(HomeMail.isHomeMailBack != null && HomeMail.isHomeMailBack == 1){
            HomeMail.showPreView();
            HomeMail.isHomeMailBack = null;
        }

        if(fragmentManager.getBackStackEntryCount() == 0){

            //1번째 백버튼 클릭
            if(System.currentTimeMillis()>backKeyPressedTime+2000){
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "'뒤로가기'버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
            //2번째 백버튼 클릭 (종료)
            else{
                AppFinish();
            }
        }

        else{
            fragmentManager.popBackStack();
        }


    }

    //앱종료
    public void AppFinish(){
        finish();
        overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
            }
        },500);

    }


}
package com.makeus.pineapple.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    Fragment fragment1_home;
    Fragment fragment2_search;
    Fragment fragment3_mypage;
    static BottomNavigationView navigation;

/*    //앱종료시간체크
    long backKeyPressedTime;*/

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

                        toggleNavigationBarItems(false);

                        if (item.getItemId() == R.id.tab_home){
                            setOneTimeEmpty(false);
//                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left).replace(R.id.container_fragment,fragment1_home).commit();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment1_home).commit();
                        }
                        else if (item.getItemId() == R.id.tab_search){
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment2_search).commit();
                        }
                        else if (item.getItemId() == R.id.tab_mypage){
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment3_mypage).commit();

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
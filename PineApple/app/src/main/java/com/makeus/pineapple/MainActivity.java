package com.makeus.pineapple;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.mypage.Fragment3_MyPage;
import com.makeus.pineapple.search.Fragment2_Search;

public class MainActivity extends AppCompatActivity {

    Fragment fragment1_home;
    Fragment fragment2_search;
    Fragment fragment3_mypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //프레그먼트 생성
        fragment1_home = new Fragment1_Home();
        fragment2_search = new Fragment2_Search();
        fragment3_mypage = new Fragment3_MyPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment1_home).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.tab_home:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment1_home).commit();
                                return true;

                            case R.id.tab_search:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment2_search).commit();
                                return true;

                            case R.id.tab_mypage:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment3_mypage).commit();
                                return true;
                        }
                        return false;
                    }
                }
        );

    }

}
package com.makeus.pineapple.mypage_settings.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;

public class SettingsMain extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    Button btn_back, btn_profile_edit, btn_letter_edit, btn_out, btn_service_center;
    FrameLayout fl_btn_back, fl_btn_profile_edit, fl_btn_letter_edit, fl_btn_out, fl_btn_service_center;



    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        myContext=(FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_main, container, false);

        //백버튼
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
        btn_back = view.findViewById(R.id.btn_back);
        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_mypage = new Fragment3_MyPage();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left,R.anim.enter_left_pop,R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment,fragment_mypage).commit();//프래그먼트 전환

            }
        });


        //프로필 수정
        fl_btn_profile_edit = view.findViewById(R.id.fl_btn_profile_edit);
        btn_profile_edit = view.findViewById(R.id.btn_profile_edit);
        fl_btn_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_profile_edit = new SettingsProfileEdit();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left,R.anim.enter_left_pop,R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment,fragment_profile_edit).commit();//프래그먼트 전환

            }
        });

        //구독메일 수정
        fl_btn_letter_edit = view.findViewById(R.id.fl_btn_letter_edit);
        btn_letter_edit = view.findViewById(R.id.btn_letter_edit);
        fl_btn_letter_edit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_edit_letter = new SettingsEditLetter();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left,R.anim.enter_left_pop,R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment,fragment_edit_letter).commit();//프래그먼트 전환

            }
        });

        //고객센터
        //탈퇴
        fl_btn_out = view.findViewById(R.id.fl_btn_out);
        btn_out = view.findViewById(R.id.btn_out);
        fl_btn_out.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PopupOut.class);
/*        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);*/
                startActivity(intent);

            }
        });





        return view;
    }
}
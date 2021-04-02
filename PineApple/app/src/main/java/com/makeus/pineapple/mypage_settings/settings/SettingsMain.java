package com.makeus.pineapple.mypage_settings.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.popup.PopupLogout;
import com.makeus.pineapple.popup.PopupOut;
import com.makeus.pineapple.sign.SignIn;

import static com.makeus.pineapple.main.MainActivity.fragmentManager;

public class SettingsMain extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    Button btn_back, btn_profile_edit, btn_letter_edit, btn_out, btn_service_center;
    FrameLayout fl_btn_back, fl_btn_profile_edit, fl_btn_letter_edit, fl_btn_out, fl_btn_service_center, fl_btn_logout;
    LinearLayout ll_profile_edit,ll_letter_edit,ll_out,ll_service_center,ll_logout;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        myContext = (FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_main, container, false);

        //네비게이터 막기
        MainActivity.toggleNavigationBarItems(false);
        //네비게이터 안보이게 만들기
        MainActivity.navigation.setVisibility(View.INVISIBLE);

        findviewByidLl(view);

        //백버튼
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
        btn_back = view.findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });

        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });


        //프로필 수정
        ll_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_profile_edit = new SettingsProfileEdit();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment, fragment_profile_edit).commit();//프래그먼트 전환

            }
        });


        fl_btn_profile_edit = view.findViewById(R.id.fl_btn_profile_edit);
        btn_profile_edit = view.findViewById(R.id.btn_profile_edit);
        fl_btn_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_profile_edit = new SettingsProfileEdit();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment, fragment_profile_edit).commit();//프래그먼트 전환

            }
        });

        //구독메일 수정

        ll_letter_edit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_edit_letter = new SettingsEditLetter();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment, fragment_edit_letter).commit();//프래그먼트 전환

            }
        });

        fl_btn_letter_edit = view.findViewById(R.id.fl_btn_letter_edit);
        btn_letter_edit = view.findViewById(R.id.btn_letter_edit);
        fl_btn_letter_edit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_edit_letter = new SettingsEditLetter();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment, fragment_edit_letter).commit();//프래그먼트 전환

            }
        });

        //고객센터
        ll_service_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                // email setting 배열로 해놔서 복수 발송 가능
//                String[] address = {"email@address.com"};
                String[] address = {"makeus.pineapple@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
//                email.putExtra(Intent.EXTRA_SUBJECT,"보내질 email 제목");
                email.putExtra(Intent.EXTRA_SUBJECT, "[고객센터] 제목");
//                email.putExtra(Intent.EXTRA_TEXT,"보낼 email 내용을 미리 적어 놓을 수 있습니다.\n");
                email.putExtra(Intent.EXTRA_TEXT, "내용");
                startActivity(email);

            }
        });

        fl_btn_service_center = view.findViewById(R.id.fl_btn_service_center);
        fl_btn_service_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                // email setting 배열로 해놔서 복수 발송 가능
//                String[] address = {"email@address.com"};
                String[] address = {"makeus.pineapple@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
//                email.putExtra(Intent.EXTRA_SUBJECT,"보내질 email 제목");
                email.putExtra(Intent.EXTRA_SUBJECT, "[고객센터] 제목");
//                email.putExtra(Intent.EXTRA_TEXT,"보낼 email 내용을 미리 적어 놓을 수 있습니다.\n");
                email.putExtra(Intent.EXTRA_TEXT, "내용");
                startActivity(email);

            }
        });

        //로그아웃
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PopupLogout.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

            }
        });
        fl_btn_logout = view.findViewById(R.id.fl_btn_logout);
        fl_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PopupLogout.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

            }
        });


        //탈퇴
        ll_out.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PopupOut.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

            }
        });
        fl_btn_out = view.findViewById(R.id.fl_btn_out);
        btn_out = view.findViewById(R.id.btn_out);
        fl_btn_out.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PopupOut.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

            }
        });


        return view;
    }

    private void findviewByidLl(View view) {
        ll_profile_edit = view.findViewById(R.id.ll_profile_edit);
        ll_letter_edit = view.findViewById(R.id.ll_letter_edit);
        ll_out = view.findViewById(R.id.ll_out);
        ll_service_center = view.findViewById(R.id.ll_service_center);
        ll_logout = view.findViewById(R.id.ll_logout);

    }
}
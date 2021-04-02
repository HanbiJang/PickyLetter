package com.makeus.pineapple.mypage_settings.settings;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.server_controllers.get.GetAllPlatform;
import com.makeus.pineapple.server_controllers.get.GetSubPlatform;

public class SettingsEditLetter extends Fragment {
    View view;

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    public static RecyclerView rv_edit_letter;
    FrameLayout fl_btn_back;
    //

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        myContext=(FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings_edit_letter, container, false);

        //네비게이터 막기
        MainActivity.toggleNavigationBarItems(false);
        //네비게이터 안보이게 만들기
        MainActivity.navigation.setVisibility(View.INVISIBLE);

        findViewByIdAll(view);

        //모든 플랫폼 목록 불러오기 리사이클러뷰
        setRv(view);

        //백버튼
        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myContext.getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void setRv(View view) {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_edit_letter.setLayoutManager((layoutManager));

        //모든 플랫폼 가져오기
        GetAllPlatform getAllPlatform = new GetAllPlatform(
                myContext
        );

        getAllPlatform.tryRequest();
    }

    private void findViewByIdAll(View view) {
        rv_edit_letter = view.findViewById(R.id.rv_edit_letter);
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
    }

    @Override
    public void onResume() {
        super.onResume();

        //리사이클러뷰 재 로딩
        setRv(view);

    }
}
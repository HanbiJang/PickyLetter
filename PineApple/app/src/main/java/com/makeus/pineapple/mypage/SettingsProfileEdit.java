package com.makeus.pineapple.mypage;

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

import com.makeus.pineapple.MainActivity;
import com.makeus.pineapple.R;

public class SettingsProfileEdit extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    Button btn_back;
    FrameLayout fl_btn_back;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        myContext=(FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_profile_edit, container, false);


        //백버튼
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                myContext.getSupportFragmentManager().popBackStack();

            }
        });


        return view;
    }

}

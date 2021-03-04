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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.MainActivity;
import com.makeus.pineapple.R;

public class SettingsEditLetter extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    Button btn_back;
    RecyclerView rv_edit_letter;
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
        View view = inflater.inflate(R.layout.fragment_settings_edit_letter, container, false);

        //백버튼
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
        btn_back = view.findViewById(R.id.btn_back);
        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_settings_main = new SettingsMain();
                myContext.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment_settings_main).commit();//프래그먼트 전환

            }
        });


        //구독메일 리사이클러뷰
        rv_edit_letter = view.findViewById(R.id.rv_edit_letter);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_edit_letter.setLayoutManager((layoutManager));
        EditLetterAdapter editLetterAdapter = new EditLetterAdapter();

        for(int num =1; num<=5;num++){

            if (num!=5){
                editLetterAdapter.addItem(new EditedLetter(EditLetterViewCode.VIEW_EDIT_LETTER_MIDDLE,"디독"));
            }
            else{
                editLetterAdapter.addItem(new EditedLetter(EditLetterViewCode.VIEW_EDIT_LETTER_END,"뉴닉"));
            }

        }

        rv_edit_letter.setAdapter(editLetterAdapter);



        return view;
    }
}
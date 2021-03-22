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

import com.makeus.pineapple.main.MainActivity;
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
        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                myContext.getSupportFragmentManager().popBackStack();

            }
        });


        //구독메일 리사이클러뷰
        rv_edit_letter = view.findViewById(R.id.rv_edit_letter);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_edit_letter.setLayoutManager((layoutManager));
        EditLetterAdapter editLetterAdapter = new EditLetterAdapter();

        editLetterAdapter.addItem(new EditedLetter(EditLetterViewCode.VIEW_EDIT_LETTER_MIDDLE,"디독",R.drawable.brand_1));
        editLetterAdapter.addItem(new EditedLetter(EditLetterViewCode.VIEW_EDIT_LETTER_END,"어피티",R.drawable.brand_3));


        rv_edit_letter.setAdapter(editLetterAdapter);



        return view;
    }
}
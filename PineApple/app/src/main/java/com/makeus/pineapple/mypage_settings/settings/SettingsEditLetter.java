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
import com.makeus.pineapple.server_controllers.get.GetSubPlatform;

public class SettingsEditLetter extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    Button btn_back;
    public static RecyclerView rv_edit_letter;
    FrameLayout fl_btn_back;
    //
    RequestQueue requestQueue;


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

        findViewByIdAll(view);

        //get 요청 관련
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }

        //구독메일 리사이클러뷰
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

        GetSubPlatform getSubPlatform = new GetSubPlatform(
                requestQueue,
                MainActivity.getUserId()
        );

        getSubPlatform.tryRequest();
    }

    private void findViewByIdAll(View view) {
        rv_edit_letter = view.findViewById(R.id.rv_edit_letter);
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
    }
}
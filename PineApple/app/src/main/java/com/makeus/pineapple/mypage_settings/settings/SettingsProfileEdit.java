package com.makeus.pineapple.mypage_settings.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.server_controllers.get.GetUserData;
import com.makeus.pineapple.server_controllers.patch.PatchNickname;

public class SettingsProfileEdit extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    FrameLayout fl_btn_back;
    Button btn_edit;
    public static EditText et_nickname;

    //
    RequestQueue requestQueue;
    public static String nickname;


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

        //네비게이터 막기
        MainActivity.toggleNavigationBarItems(false);

        et_nickname = view.findViewById(R.id.et_nickname);
        btn_edit = view.findViewById(R.id.btn_edit);

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getContext());
        }

        //userData중 닉네임을 get해와서 et_nickname에 set하기
        GetUserData getUserData = new GetUserData(requestQueue);
        getUserData.tryRequest();


        //백버튼
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myContext.getSupportFragmentManager().popBackStack();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = et_nickname.getText().toString();
                if(nickname.length() > 0){
                    PatchNickname patchNickname = new PatchNickname(
                            requestQueue,
                            nickname
                    );
                    patchNickname.tryRequest();
                    Toast.makeText(getContext(), "닉네임이 성공적으로 바뀌었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }

}

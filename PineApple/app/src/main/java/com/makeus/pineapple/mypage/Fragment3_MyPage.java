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
import com.makeus.pineapple.home.NewLetter;

public class Fragment3_MyPage extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    Button btn_setting;
    FrameLayout fl_btn_setting;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        myContext=(FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment3_mypage,container,false);

        //북마크 리사이클러뷰
        RecyclerView rv_mypage_bookmark = view.findViewById(R.id.rv_mypage_bookmark);
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false);
        rv_mypage_bookmark.setLayoutManager(layoutManager2);
        BookmarkLetterAdapter bookmarkLetterAdapter = new BookmarkLetterAdapter();

        bookmarkLetterAdapter.addItem(new NewLetter("디독", "02/02/2021"));
        bookmarkLetterAdapter.addItem(new NewLetter("디독", "02/02/2021"));
        bookmarkLetterAdapter.addItem(new NewLetter("디독", "02/02/2021"));

        rv_mypage_bookmark.setAdapter(bookmarkLetterAdapter); //리사이클러뷰에 어답터 설정

        //세팅 버튼
        fl_btn_setting = view.findViewById(R.id.fl_btn_setting);
        btn_setting = view.findViewById(R.id.btn_setting);
        fl_btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_settings_main = new SettingsMain();
                myContext.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment_settings_main).commit();//프래그먼트 전환

            }
        });

        return view;

    }


}
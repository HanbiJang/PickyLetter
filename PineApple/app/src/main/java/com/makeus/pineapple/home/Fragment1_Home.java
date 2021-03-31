package com.makeus.pineapple.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeus.pineapple.popup.loading.PopupLoading;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.home.adapters.HomeAdapters;
import com.makeus.pineapple.home.adapters.NewLetterAdapter;
import com.makeus.pineapple.home.adapters.OldLetterAdapter;
import com.makeus.pineapple.home.filters.PopupFilter;
import com.makeus.pineapple.server_controllers.get.GetMailBoxBottom;
import com.makeus.pineapple.server_controllers.get.GetMailBoxTop;

import java.util.HashMap;
import java.util.Map;


public class Fragment1_Home extends Fragment {
    public static View view;
    public static Context myContext;

    //사용자 정보 얻기ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    static Integer userId = null;
    static String token = null;
    static String nickName = null;

    // UIㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    LinearLayout ll_main;
    public static RecyclerView rv_newletter,rv_oldletter;
    static ImageView img_empty;
    public static Button btn_filter;
    FrameLayout fl_btn_filter;
    static TextView tv_nickname, tv_dochack, tv_nim, tv_empty;


    //새로고침ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static SwipeRefreshLayout sr_layout;
    public static boolean setLoadingPopupNew = false;
    public static boolean setLoadingPopupOld = false;

    public static boolean isLoadingTopRv = false; //상단 무한스크롤
    public static boolean isLoadingBottomRv = false;  //하단 무한 스크롤

    //페이지수ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static Integer pageTop = -1;
    public static Integer pageBottom = -1;

    public static Integer pageLimitTop = 0;
    public static Integer pageLimitBottom = 0;

    //어답터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static NewLetterAdapter newLetterAdapter;
    public static OldLetterAdapter oldLetterAdapter;

    //필터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //필터 시작 알림 변수
    public static boolean isFilterStart_date = false;
    public static boolean isFilterStart_brand = false;
    public static boolean isFilterStart_all = false;

    //필터 날짜 변수ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static boolean isbtn_dateClicked = false;
    public static String endDate = null, startDate =null;

    //필터 플랫폼 변수ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static Map<String, String> brandNameList = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //로그인으로 얻은 유저 데이터 가져오기
        getUserData();
        //변수 만들기
        brandNameList = new HashMap<>();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1_home, container, false);
        myContext = getContext();

        //스크롤할때마다 page값 증가하므로 초기화
        pageTop = -1;
        pageBottom = -1;
        pageLimitTop = 0;
        pageLimitBottom = 0;
        isLoadingTopRv = false; //상단 무한스크롤
        isLoadingBottomRv = false;  //하단 무한 스크롤

        //findViewById
        findViewByIdAll(view);

        //닉네임 세팅하기
        tv_nickname.setText(nickName);

        //empty이미지 보여주기 (get요청 하기 전)
        showEmptyImg();

        setAllRv(view);


        //필터 버튼

        if (isFilterStart_date == true || isFilterStart_brand == true || isFilterStart_all == true){ //필터 버튼이 활성화 되어있다면 UI활성화
            btn_filter.setBackgroundResource(R.drawable.btn_filter_fill);
        }
        fl_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PopupFilter.class);
                startActivity(intent);
                //애니메이션 설정
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
            }
        });


        //스와이프 새로고침
        sr_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                setLoadingPopupNew = false;
                setLoadingPopupOld = false;

                //스크롤할때마다 page값 증가하므로 초기화
                pageTop = -1;
                pageBottom = -1;
                pageLimitTop = 0;
                pageLimitBottom = 0;

                //무한스크롤
                isLoadingTopRv = false;
                isLoadingBottomRv = false;

                setAllRv(view); //리사이클러뷰 설정 (get요청 포함)

            }
        });

        return view;
    }

    private void setAllRv(View view) {
        try {
            Intent intent = new Intent(getContext(), PopupLoading.class);
            intent.putExtra("pastFragmentNum", 1);
            getContext().startActivity(intent);
        } catch (Exception e) {
            Log.e("0", "로딩 오류");
        }
        //리사이클러뷰 상단 설정 (get요청 포함)
        setTopRv(view);
        //리사이클러뷰 하단 설정 (get요청 포함)
        setBottomRv(view);
    }


    private void findViewByIdAll(View view) {
        btn_filter = view.findViewById(R.id.btn_filter);
        fl_btn_filter = view.findViewById(R.id.fl_btn_filter);
        img_empty = view.findViewById(R.id.img_empty);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_dochack = view.findViewById(R.id.tv_dochack);
        tv_nim = view.findViewById(R.id.tv_nim);
        tv_empty = view.findViewById(R.id.tv_empty);
        rv_newletter = view.findViewById(R.id.rv_newletter);
        rv_oldletter = view.findViewById(R.id.rv_oldletter);
        sr_layout = view.findViewById(R.id.sr_layout);
        ll_main = view.findViewById(R.id.ll_main);

    }

    //메인함수에서 정보를 받아옴
    private void getUserData() {
        userId = MainActivity.getUserId();
        token = MainActivity.getToken();
        nickName = MainActivity.getNickName();

    }


    //상단 리사이클러뷰 (이번주 메일)
    private void setTopRv(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        rv_newletter.setLayoutManager(layoutManager);
        newLetterAdapter = new NewLetterAdapter();

        //데이터 세팅
        setNewsToRv(newLetterAdapter);
    }

    public static void setBottomRv(View view) {
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_oldletter.setLayoutManager(layoutManager2);
        oldLetterAdapter = new OldLetterAdapter();

        //데이터 세팅
        setNewsToRv(oldLetterAdapter);
    }

    public static void setNewsToRv(HomeAdapters homeAdapters) {
        homeAdapters.removeAll(); //안 쌓이게 하기
        //로딩 팝업
        if (homeAdapters instanceof NewLetterAdapter) {
            setLoadingPopupNew = false;
        } else {
            setLoadingPopupOld = false;
        }

        //서버에서 구독메일을 받아서 리사이클러뷰의 내용을 세팅함
        if (homeAdapters instanceof NewLetterAdapter) {
            GetMailBoxTop getMailBox = new GetMailBoxTop(
                    myContext,
                    rv_newletter,
                    homeAdapters
            );
            getMailBox.tryRequest();
        }
        else {
            GetMailBoxBottom getMailBox = new GetMailBoxBottom(
                    myContext,
                    rv_oldletter,
                    homeAdapters
            );
            getMailBox.tryRequest();
        }

    }

    public static void showEmptyImg() {
        tv_nickname.setVisibility(View.GONE);
        tv_nim.setVisibility(View.GONE);
        tv_dochack.setVisibility(View.GONE);
        tv_empty.setVisibility(View.VISIBLE);
        img_empty.setVisibility(View.VISIBLE);
        rv_newletter.setVisibility(View.GONE);

    }

    public static void showNomalRv() {
        tv_nickname.setVisibility(View.VISIBLE);
        tv_nim.setVisibility(View.VISIBLE);
        tv_dochack.setVisibility(View.VISIBLE);
        tv_empty.setVisibility(View.GONE);
        img_empty.setVisibility(View.GONE);
        rv_newletter.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();

        //로딩뷰 관련 변수 초기화
        isLoadingTopRv = false; //상단 무한스크롤
        isLoadingBottomRv = false;  //하단 무한 스크롤
        Log.e("온리줌" , "온리줌 부름");


    }
}
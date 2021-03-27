package com.makeus.pineapple.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.makeus.pineapple.loading.PopupLoading;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.home.adapters.HomeAdapters;
import com.makeus.pineapple.home.adapters.NewLetterAdapter;
import com.makeus.pineapple.home.adapters.OldLetterAdapter;
import com.makeus.pineapple.home.filters.PopupFilter;
import com.makeus.pineapple.server_controllers.get.GetMailBox;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;


public class Fragment1_Home extends Fragment {
    static View view;

    //사용자 정보 얻기
    static Integer userId = null;
    static String token = null;
    static String nickName = null;

    static RequestQueue requestQueueTop;
    static RequestQueue requestQueueBottom;

    static Integer pageTopRv = 0;
    static Integer pageBottomRv = 0;

    LinearLayout ll_main;
    static RecyclerView rv_newletter,rv_oldletter;
    static ImageView img_empty;
    Button btn_filter;
    FrameLayout fl_btn_filter;
    static TextView tv_nickname, tv_dochack, tv_nim, tv_empty;

    public static boolean isLoadingTopRv = false; //상단 무한스크롤
    public static boolean isLoadingBottomRv = false;  //하단 무한 스크롤

    //새로고침
    public static SwipeRefreshLayout sr_layout;
    public static boolean setLoadingPopupNew = false;
    public static boolean setLoadingPopupOld = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (requestQueueTop == null) {
            requestQueueTop = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }
        if (requestQueueBottom == null) {
            requestQueueBottom = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }

        //로그인으로 얻은 유저 데이터 가져오기
        getUserData();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1_home, container, false);

        pageTopRv = 0; //스크롤할때마다 page값 증가하므로 초기화
        pageBottomRv = 0; //스크롤할때마다 page값 증가하므로 초기화

        //findViewById
        findViewByIdAll(view);

        //닉네임 세팅하기
        tv_nickname.setText(nickName);

        //empty이미지 보여주기 (get요청 하기 전)
        showEmptyImg();

        setAllRv(view);


        //필터 버튼
        fl_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PopupFilter.class);
                startActivity(intent);
                //애니메이션 설정
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
            }
        });

        //네비게이션 디버깅 코드
        Handler handler0 = new Handler();
        handler0.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.toggleNavigationBarItems(true);
            }
        }, 500);

        //스와이프 새로고침
        sr_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLoadingPopupNew = false;
                setLoadingPopupOld = false;

                pageTopRv = 0; //스크롤할때마다 page값 증가하므로 초기화
                pageBottomRv = 0; //스크롤할때마다 page값 증가하므로 초기화

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
        NewLetterAdapter newLetterAdapter = new NewLetterAdapter();

        //데이터 세팅
        setNewsToRv(newLetterAdapter);
    }

    private void setBottomRv(View view) {
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_oldletter.setLayoutManager(layoutManager2);
        OldLetterAdapter oldLetterAdapter = new OldLetterAdapter();

        //데이터 세팅
        setNewsToRv(oldLetterAdapter);
    }

    private void setNewsToRv(HomeAdapters homeAdapters) {
        homeAdapters.removeAll(); //안 쌓이게 하기
        //로딩 팝업
        if (homeAdapters instanceof NewLetterAdapter) {
            setLoadingPopupNew = false;
        } else {
            setLoadingPopupOld = false;
        }

        //서버에서 구독메일을 받아서 리사이클러뷰의 내용을 세팅함
        if (homeAdapters instanceof NewLetterAdapter) {
            GetMailBox getMailBox = new GetMailBox(
                    rv_newletter,
                    rv_oldletter,
                    requestQueueTop,
                    new MailboxRequestData(userId, pageTopRv, -7 ),
                    homeAdapters
            );
            getMailBox.tryRequest();
        }
        else {
            GetMailBox getMailBox = new GetMailBox(
                    rv_newletter,
                    rv_oldletter,
                    requestQueueBottom,
                    new MailboxRequestData(userId, pageTopRv),
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

}
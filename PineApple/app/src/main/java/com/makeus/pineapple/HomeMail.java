package com.makeus.pineapple;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.makeus.pineapple.bookmark.AddOrDelBookmark;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.server_controllers.get.GetLetterInformHomeMail;
import com.makeus.pineapple.server_controllers.server_data.NewsData;
import com.makeus.pineapple.sign.SignIn;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.OVER_SCROLL_ALWAYS;
import static com.makeus.pineapple.main.MainActivity.getToken;

public class HomeMail extends Fragment {

    //뒤로가기 플래그
    public static Integer isHomeMailBack = 0;

    public static Fragment homeMail;

    public static boolean setLoadingPopup = false;

    //메일 정보
    static RequestQueue requestQueueBookmarkId;

    //북마크 정보
    static RequestQueue requestQueueBookmarkAdd, requestQueueBookmarkDel, requestQueueGetLetterInform;

    MainActivity mainActivity;
    public static FragmentActivity myContext; //화면 전환
    FrameLayout fl_btn_back;
    static Button btn_bookmark , btn_back;
    TextView tv_title;
    TextView tv_brand;
    TextView tv_date;
    ImageView img_news;
    CircleImageView cimg_brand;
    WebView wv_news;

    //메일 데이터
    static String newsTitle, newsBrand, newsDate, newsImage, newsBrandImage;
    static Integer letterId;
    static Integer bookmarkId;
    public static NewsData newsData;

    static Integer isClicked = -1;        //북마크 체크 기능

    //이전 화면
    Integer preView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        homeMail = this; //자기자신
        myContext = (FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mail, container, false);

        //네비게이터 막기
        MainActivity.toggleNavigationBarItems(false);
        //네비게이터 안보이게 만들기
        MainActivity.navigation.setVisibility(View.INVISIBLE);

        //어댑터로부터 정보 넘겨받기
        getDataFromAdapter();

        findByViewAll(view);

        isHomeMailBack = 1;

        //정보세팅
        //북마크 정보 Get해오기
        if (requestQueueBookmarkId == null) {
            requestQueueBookmarkId = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }


        //북마크 Id 정보 받아오기 - 북마크 초기 세팅하기
        GetLetterInformHomeMail getLetterInformHomeMail = new GetLetterInformHomeMail(requestQueueBookmarkId, letterId, btn_bookmark);
        getLetterInformHomeMail.tryRequest();

        //UI에 정보 세팅
        newsDataSettingToUi();

        //웹뷰
        MyWebView myWebView = new MyWebView();
        myWebView.wvSetting(wv_news);

        tryGetHtmlAndLoad(letterId);//html 정보 가져오기

        AddOrDelBookmark addOrDelBookmark = new AddOrDelBookmark(btn_bookmark,
                letterId,
                myContext,
                requestQueueBookmarkAdd,
                requestQueueBookmarkDel,
                requestQueueGetLetterInform);

        //북마크 체크 기능
        btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //북마크 등록/해제 함수
                addOrDelBookmark.setBtnFunc(isClicked);
                isClicked *= -1;

            }
        });


        //백버튼
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreView();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreView();
            }
        });


        return view;
    }


    public static void showPreView() {
        myContext.getSupportFragmentManager().popBackStack();
        MainActivity.fragmentManager.beginTransaction().
                setCustomAnimations(R.anim.enter_left_pop, R.anim.exit_left_pop, R.anim.enter_left_pop, R.anim.exit_left_pop).
                hide(homeMail).commit();
        //네비게이터 풀기
        MainActivity.toggleNavigationBarItems(true);
        //네비게이터 보이게 만들기
        MainActivity.navigation.setVisibility(View.VISIBLE);
    }


    private void newsDataSettingToUi() {
        tv_title.setText(newsTitle);
        tv_brand.setText(newsBrand);
        tv_date.setText(CalStringDate.calDate(newsDate));

        //G-glide로 이미지 설정
        String imageUrl = newsBrandImage;
        Glide.with(getContext()).load(imageUrl).into(cimg_brand);

        String imageUrl2 = newsImage;
        Glide.with(getContext()).load(imageUrl2)
                .error(R.color.pickyUnableGray)
                .into(img_news);

    }

    public static void bookmarkFirstSetting(Button btn_bookmark) {
        bookmarkId = newsData.getBookmarkId();
        isClicked = bookmarkId;
        if (isClicked != 0) {
            btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill);
        }
    }

    private void findByViewAll(View view) {
        tv_title = view.findViewById(R.id.tv_title);
        tv_brand = view.findViewById(R.id.tv_brand);
        tv_date = view.findViewById(R.id.tv_date);
        img_news = view.findViewById(R.id.img_news);
        cimg_brand = view.findViewById(R.id.cimg_brand);
        wv_news = view.findViewById(R.id.wv_news);
        btn_bookmark = view.findViewById(R.id.btn_bookmark);
        btn_back = view.findViewById(R.id.btn_back);
    }

    private void getDataFromAdapter() {
        newsTitle = getArguments().getString("newsTitle"); // 전달한 key 값
        newsBrand = getArguments().getString("newsBrand"); // 전달한 key 값
        newsDate = getArguments().getString("newsDate"); // 전달한 key 값
        newsImage = getArguments().getString("newsImage"); // 전달한 key 값
        newsBrandImage = getArguments().getString("newsBrandImage"); // 전달한 key 값
        letterId = getArguments().getInt("letterId"); // 전달한 key 값
        Log.e("letterId 값", letterId + "");

        //이전화면 정보 받기
        preView = getArguments().getInt("preView"); // 전달한 key 값
    }

    public class MyWebView extends WebViewClient {

        void wvSetting(WebView wv_news) {
            wv_news.getSettings().setJavaScriptEnabled(true);
            wv_news.setWebViewClient(new MyWebView());
            wv_news.setOverScrollMode(OVER_SCROLL_ALWAYS);


        }
    }

    private void tryGetHtmlAndLoad(Integer letterId) {
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("x-access-token", getToken());
        wv_news.loadUrl(makeGetHtmlUrl(letterId), extraHeaders);
        setLoadingPopup = true;
    }

    //웹뷰
    private String makeGetHtmlUrl(Integer letterId) {
        String url;
        url = "http://3.13.65.158/v1/letters/" + letterId + "/html";
        return url;
    }


}
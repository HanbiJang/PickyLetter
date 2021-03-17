package com.makeus.pineapple.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeus.pineapple.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage.Fragment3_MyPage;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.OVER_SCROLL_ALWAYS;
import static android.view.View.OVER_SCROLL_IF_CONTENT_SCROLLS;

public class HomeMail extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    FrameLayout fl_btn_back;

    TextView tv_title;
    TextView tv_brand;
    TextView tv_date;
    ImageView img_news;
    CircleImageView cimg_brand;

    WebView wv_news;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        myContext=(FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mail,container,false);

        String newsTitle = getArguments().getString("newsTitle"); // 전달한 key 값
        String newsBrand = getArguments().getString("newsBrand"); // 전달한 key 값
        String newsDate = getArguments().getString("newsDate"); // 전달한 key 값
        Integer newsImage = getArguments().getInt("newsImage"); // 전달한 key 값
        Integer newsBrandImage = getArguments().getInt("newsBrandImage"); // 전달한 key 값

        //정보 세팅
        tv_title = view.findViewById(R.id.tv_title);
        tv_brand = view.findViewById(R.id.tv_brand);
        tv_date = view.findViewById(R.id.tv_date);
        img_news = view.findViewById(R.id.img_news);
        cimg_brand = view.findViewById(R.id.cimg_brand);
        wv_news = view.findViewById(R.id.wv_news);
        //
        tv_title.setText(newsTitle);
        tv_brand.setText(newsBrand);
        tv_date.setText(newsDate);
        img_news.setImageResource(newsImage);
        cimg_brand.setImageResource(newsBrandImage);

        //가라데이터 세팅
        MyWebView myWebView = new MyWebView();
        myWebView.wvSetting(wv_news);


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

    public class MyWebView extends WebViewClient{

        void wvSetting(WebView wv_news){
            wv_news.getSettings().setJavaScriptEnabled(true);
            wv_news.setWebViewClient(new MyWebView());
            wv_news.setOverScrollMode(OVER_SCROLL_ALWAYS);
            wv_news.loadUrl("file:///android_asset/www/sample_news.html");

            wv_news.setOnTouchListener( new View.OnTouchListener() {

                public boolean onTouch( View v, MotionEvent event ) {

                    WebView wv = (WebView)v;

                    wv.requestDisallowInterceptTouchEvent( true );

                    return false;

                }

            });

        }



    }
}
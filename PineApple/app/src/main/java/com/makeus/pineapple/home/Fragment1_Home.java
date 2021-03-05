package com.makeus.pineapple.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.makeus.pineapple.R;
import com.makeus.pineapple.home.filters.PopupFilter;
import com.makeus.pineapple.search.SearchViewCode;
import com.makeus.pineapple.search.SearchedNews;

public class Fragment1_Home extends Fragment {

    RecyclerView rv_newletter;
    Button btn_filter;
    FrameLayout fl_btn_filter;

    //무한 스크롤
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment1_home, container, false);
        //코드 작성
        //필터 버튼
        btn_filter = view.findViewById(R.id.btn_filter);
        fl_btn_filter = view.findViewById(R.id.fl_btn_filter);
        fl_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PopupFilter.class);
/*        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);*/
                startActivity(intent);
                //애니메이션 설정
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_up);

            }
        });


        //리사이클러뷰 상단
        rv_newletter = view.findViewById(R.id.rv_newletter);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_newletter.setLayoutManager(layoutManager);
        NewLetterAdapter newLetterAdapter = new NewLetterAdapter();

        newLetterAdapter.addItem(new NewLetter("개발 실력을 위한 IT기업 기술 블로그 45곳 모음", "오렌지레터", "02/01/2021", R.drawable.news_1, R.drawable.brand_4));
        newLetterAdapter.addItem(new NewLetter("머스크·최태원 한다는데…클럽하우스 안해도 괜찮아요?", "오렌지레터", "02/01/2021", R.drawable.news_2, R.drawable.brand_4));
        newLetterAdapter.addItem(new NewLetter("쇼핑 중독에 걸리다", "오렌지레터", "02/01/2021", R.drawable.news_3, R.drawable.brand_4));
        newLetterAdapter.addItem(new NewLetter("가게 배달지역 관리방식 개편 프로젝트", "디독", "02/01/2021", R.drawable.news_4, R.drawable.brand_1));
        newLetterAdapter.addItem(new NewLetter("5달러 짜리 로고와 250달러 짜리 로고의 차이점", "디독", "02/01/2021", R.drawable.news_5, R.drawable.brand_1));

        rv_newletter.setAdapter(newLetterAdapter); //리사이클러뷰에 어답터 설정

//리사이클러뷰 하단
        RecyclerView rv_oldletter = view.findViewById(R.id.rv_oldletter);
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_oldletter.setLayoutManager(layoutManager2);
        OldLetterAdapter oldLetterAdapter = new OldLetterAdapter();

        oldLetterAdapter.addItem(new OldLetter("개발 실력을 위한 IT기업 기술 블로그 45곳 모음","오렌지레터","02/01/2021",R.drawable.news_1,R.drawable.brand_4));
        oldLetterAdapter.addItem(new OldLetter("머스크·최태원 한다는데…클럽하우스 안해도 괜찮아요?","오렌지레터","02/01/2021",R.drawable.news_2,R.drawable.brand_4));
        oldLetterAdapter.addItem(new OldLetter("쇼핑 중독에 걸리다","오렌지레터","02/01/2021",R.drawable.news_3,R.drawable.brand_4));
        oldLetterAdapter.addItem(new OldLetter("가게 배달지역 관리방식 개편 프로젝트","디독","02/01/2021",R.drawable.news_4,R.drawable.brand_1));
        oldLetterAdapter.addItem(new OldLetter("5달러 짜리 로고와 250달러 짜리 로고의 차이점","디독","02/01/2021",R.drawable.news_5,R.drawable.brand_1));
        oldLetterAdapter.addItem(new OldLetter("페이스북,인스타그램,유튜브의 잘못된 UI","디독","02/01/2021",R.drawable.news_6,R.drawable.brand_1));
        oldLetterAdapter.addItem(new OldLetter("주목받는 재택근무 관련주와 라이프스타일의 변화 \uD83D\uDC69\u200D\uD83D\uDCBB\n","어피티","02/01/2021",R.drawable.news_7,R.drawable.brand_3));
        oldLetterAdapter.addItem(new OldLetter("비상 걸린 콜센터,원격 콜센터 구축 업체는? \uD83D\uDCDE","어피티","02/01/2021",R.drawable.news_8,R.drawable.brand_3));
        oldLetterAdapter.addItem(new OldLetter("금 수요 폭등,거래소에는 호재","어피티","02/01/2021",R.drawable.news_9,R.drawable.brand_3));
        oldLetterAdapter.addItem(new OldLetter("타다 클라이언트 개발기","어피티","02/01/2021",R.drawable.news_10,R.drawable.brand_3));



        rv_oldletter.setAdapter(oldLetterAdapter); //리사이클러뷰에 어답터 설정
        initScrollListener(rv_oldletter, oldLetterAdapter);

        return view;
    }

    private void initScrollListener(RecyclerView rv_oldletter, OldLetterAdapter oldLetterAdapter) {
        rv_oldletter.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == oldLetterAdapter.getItemCount() - 1) {
//bottom of list!
                        loadMore(oldLetterAdapter);
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore(OldLetterAdapter oldLetterAdapter) {
        oldLetterAdapter.addItem(null);

        oldLetterAdapter.notifyItemInserted(oldLetterAdapter.getItems().size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                oldLetterAdapter.removeItems(oldLetterAdapter.getItems().size() - 1);
                int scrollPosition = oldLetterAdapter.getItems().size();
                oldLetterAdapter.notifyItemRemoved(scrollPosition);

                int currentSize = scrollPosition;
                int nextLimit = currentSize + 9;

                while (currentSize - 1 < nextLimit) {
                    oldLetterAdapter.addItem(null); //스크롤 시 추가되는 내용
                    currentSize++;
                }

                oldLetterAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }


}
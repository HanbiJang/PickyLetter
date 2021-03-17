package com.makeus.pineapple.search;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;

public class Fragment2_Search extends Fragment {

    RecyclerView rv_rank;
    RecyclerView rv_search_result;
    Button btn_search;
    EditText et_search;
    Button btn_x;
    TextView tv_search_result;
    LinearLayout ll_search_result;
    FrameLayout fl_btn_x, fl_btn_search;

    //무한 스크롤
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_search,container,false);

        //순위 리사이클러뷰
        rv_rank = view.findViewById(R.id.rv_rank);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_rank.setLayoutManager((layoutManager));
        SearchedNewsRankAdapter searchedNewsAdapter = new SearchedNewsRankAdapter();

        //리사이클러뷰 초기화
        searchedNewsAdapter.removeAll();

        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RANK_BLIND,"개발 실력을 위한 IT기업 기술 블로그 45곳 모음","오렌지레터","02/01/2021",1,R.drawable.news_1,R.drawable.brand_4,false));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RANK_BLIND,"머스크·최태원 한다는데…클럽하우스 안해도 괜찮아요?","오렌지레터","02/01/2021",2,R.drawable.news_2,R.drawable.brand_4,false));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RANK_BLIND,"쇼핑 중독에 걸리다","오렌지레터","02/01/2021",3,R.drawable.news_3,R.drawable.brand_4,false));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_RANK_SEARCH,"가게 배달지역 관리방식 개편 프로젝트","디독","02/01/2021",4,R.drawable.news_4,R.drawable.brand_1,true));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_RANK_SEARCH,"5달러 짜리 로고와 250달러 짜리 로고의 차이점","디독","02/01/2021",5,R.drawable.news_5,R.drawable.brand_1,true));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_RANK_SEARCH,"페이스북,인스타그램,유튜브의 잘못된 UI","디독","02/01/2021",6,R.drawable.news_6,R.drawable.brand_1,true));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_RANK_SEARCH,"주목받는 재택근무 관련주와 라이프스타일의 변화 \uD83D\uDC69\u200D\uD83D\uDCBB\n","어피티","02/01/2021",7,R.drawable.news_7,R.drawable.brand_3,true));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_RANK_SEARCH,"비상 걸린 콜센터,원격 콜센터 구축 업체는? \uD83D\uDCDE","어피티","02/01/2021",8,R.drawable.news_8,R.drawable.brand_3,true));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_RANK_SEARCH,"금 수요 폭등,거래소에는 호재","어피티","02/01/2021",9,R.drawable.news_9,R.drawable.brand_3,true));
        searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_RANK_SEARCH,"타다 클라이언트 개발기","어피티","02/01/2021",10,R.drawable.news_10,R.drawable.brand_3,true));

        rv_rank.setAdapter(searchedNewsAdapter);


        //검색 결과 만들기
        btn_search = view.findViewById(R.id.btn_search);
        et_search = view.findViewById(R.id.et_search);
        btn_x = view.findViewById(R.id.btn_x);
        tv_search_result = view.findViewById(R.id.tv_search_result);
        ll_search_result = view.findViewById(R.id.ll_search_result);
        rv_search_result = view.findViewById(R.id.rv_search_result);

        //x 버튼
        // x버튼 / 검색버튼 색설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn_search.setBackgroundTintList(null);
            btn_x.setBackgroundTintList(null);
        }

        fl_btn_search = view.findViewById(R.id.fl_btn_search);
        //검색 버튼
        fl_btn_search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String searchKeyword = et_search.getText().toString();

                if (searchKeyword != null){
                    tv_search_result.setText("'"+searchKeyword+"'"+" 검색 결과");

                    // 검색 결과 리사이클러뷰 결과 만들기

                    LinearLayoutManager layoutManager2 =
                            new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_search_result.setLayoutManager((layoutManager2));
                    SearchedNewsResultAdapter searchedNewsResultAdapter = new SearchedNewsResultAdapter();


                    searchedNewsResultAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RESULT_BLIND,"개발 실력을 위한 IT기업 기술 블로그 45곳 모음","오렌지레터","02/01/2021",1,R.drawable.news_1,R.drawable.brand_4,false));
                    searchedNewsResultAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RESULT_BLIND,"머스크·최태원 한다는데…클럽하우스 안해도 괜찮아요?","오렌지레터","02/01/2021",2,R.drawable.news_2,R.drawable.brand_4,false));
                    searchedNewsResultAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RESULT_BLIND,"쇼핑 중독에 걸리다","오렌지레터","02/01/2021",3,R.drawable.news_3,R.drawable.brand_4,false));

                    rv_search_result.setAdapter(searchedNewsResultAdapter);
                    ll_search_result.setVisibility(View.VISIBLE);
                    initScrollListener(rv_search_result, searchedNewsResultAdapter);

                }


            }
        });

        //x버튼
        fl_btn_x = view.findViewById(R.id.fl_btn_x);
        fl_btn_x.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                et_search.setText(null);
                ll_search_result.setVisibility(View.GONE);
            }
        });



        return view;
    }

    private void initScrollListener(RecyclerView rv_search_result, SearchedNewsResultAdapter searchedNewsResultAdapter) {
        rv_search_result.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchedNewsResultAdapter.getItemCount() - 1) {//bottom of list!
                        loadMore(searchedNewsResultAdapter);
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore(SearchedNewsResultAdapter searchedNewsResultAdapter) {
        searchedNewsResultAdapter.addItem(null);
        searchedNewsResultAdapter.notifyItemInserted(searchedNewsResultAdapter.getItems().size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                searchedNewsResultAdapter.removeItems(searchedNewsResultAdapter.getItems().size() - 1);
                int scrollPosition = searchedNewsResultAdapter.getItems().size();
                searchedNewsResultAdapter.notifyItemRemoved(scrollPosition);

                int currentSize = scrollPosition;
                int nextLimit = currentSize + 3;

                while (currentSize - 1 < nextLimit) {

                    SearchedNews tmpSearchedNews = searchedNewsResultAdapter.getItem(searchedNewsResultAdapter.getItems().size() - 1); //마지막 요소 킵
                    //마지막 요소가 지워짐
                    searchedNewsResultAdapter.removeItems(searchedNewsResultAdapter.getItems().size() - 1);
                    //마지막 요소를 넣어야 함
                    searchedNewsResultAdapter.addItem(tmpSearchedNews); //스크롤 시 추가되는 내용
                    currentSize++;
                }

                searchedNewsResultAdapter.notifyDataSetChanged();
                isLoading = false;

            }
        }, 2000);


    }
}
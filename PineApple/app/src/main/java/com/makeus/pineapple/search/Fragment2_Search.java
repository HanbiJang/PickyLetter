package com.makeus.pineapple.search;

import android.os.Build;
import android.os.Bundle;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment2_search,container,false);

        //순위 리사이클러뷰
        rv_rank = view.findViewById(R.id.rv_rank);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_rank.setLayoutManager((layoutManager));
        SearchedNewsRankAdapter searchedNewsAdapter = new SearchedNewsRankAdapter();

        for(int num =1; num<=10;num++){

            if (num==2){
                searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RANK_BLIND,"'다크패턴'이라고 부르면 안되는 이유","디독","02/01/2021",num));
            }
            else{
                searchedNewsAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_RANK_SEARCH,"'다크패턴'이라고 부르면 안되는 이유","디독","02/01/2021",num));
            }

        }

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

                    for(int num =1; num<=3;num++){

                        if (num==2){
                            searchedNewsResultAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RESULT_BLIND,"'다크패턴'이라고 부르면 안되는 이유","디독","02/01/2021",-1));
                        }
                        else{
                            searchedNewsResultAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RESULT,"새로운 색상","어피티","02/01/2021",-1));
                        }

                    }

                    rv_search_result.setAdapter(searchedNewsResultAdapter);
                    ll_search_result.setVisibility(View.VISIBLE);

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
}
package com.makeus.pineapple.search.searchViewHolders;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.makeus.pineapple.R;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.search.Fragment2_Search;
import com.makeus.pineapple.search.data.SearchedNews;
import com.makeus.pineapple.server_controllers.get.GetMailBoxBottomAgain;
import com.makeus.pineapple.server_controllers.get.GetSearchResult;
import com.makeus.pineapple.server_controllers.get.GetSearchResultAgain;
import com.makeus.pineapple.server_controllers.server_data.NewsData;


//(2) 로딩뷰 홀더
public class LoadingViewHolder extends SearchViewHolder {

    public static Button btn_more;
    public static ProgressBar progressBar;
    Context myContext;
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        myContext = (FragmentActivity) itemView.getContext(); //context
        btn_more = itemView.findViewById(R.id.btn_more);
        progressBar = itemView.findViewById(R.id.progressBar);
    }


    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //아이템 추가
                Log.e(" ", "버튼 누름");

                //프로그레스바 보여주기
                btn_more.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //다시 검색 결과 불러오기
                        GetSearchResultAgain getSearchResultAgain = new GetSearchResultAgain(
                                myContext,
                                Fragment2_Search.rv_search_result,
                                Fragment2_Search.searchedNewsResultAdapter
                        );
                        getSearchResultAgain.tryRequest();

                    }
                },600);

            }
        });
    }
}
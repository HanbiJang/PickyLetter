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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.search.adapters.SearchedNewsRankAdapter;
import com.makeus.pineapple.search.adapters.SearchedNewsResultAdapter;
import com.makeus.pineapple.search.data.SearchedNews;
import com.makeus.pineapple.search.data.server_data.RankResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment2_Search extends Fragment {

    static String token = null;
    static RequestQueue requestQueue;

    RecyclerView rv_rank;
    RecyclerView rv_search_result;
    Button btn_search;
    EditText et_search;
    Button btn_x;
    TextView tv_search_result;
    LinearLayout ll_search_result;
    FrameLayout fl_btn_x, fl_btn_search;

    //검색 결과 무한 스크롤
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_search, container, false);

        //모든 컴포넌트 findViewById
        findViewByIdAll(view);

        //로그인으로 얻은 유저 데이터 가져오기
        getUserData();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }

        //순위 리사이클러뷰 설정 & 데이터 설정 (무한스크롤 아님)
        setRankRv(view);


        /****검색****/
        //검색 버튼
        fl_btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String searchKeyword = et_search.getText().toString();

                if (searchKeyword != null) {
                    tv_search_result.setText("'" + searchKeyword + "'" + " 검색 결과");

                    // 검색 결과 리사이클러뷰 결과 만들기

                    LinearLayoutManager layoutManager2 =
                            new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_search_result.setLayoutManager((layoutManager2));
                    SearchedNewsResultAdapter searchedNewsResultAdapter = new SearchedNewsResultAdapter();


/*
                    searchedNewsResultAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RESULT_BLIND, "개발 실력을 위한 IT기업 기술 블로그 45곳 모음", "오렌지레터", "02/01/2021", , R.drawable.news_1, 1, false));
                    searchedNewsResultAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RESULT_BLIND, "머스크·최태원 한다는데…클럽하우스 안해도 괜찮아요?", "오렌지레터", "02/01/2021", 2, R.drawable.news_2, R.drawable.brand_4, false));
                    searchedNewsResultAdapter.addItem(new SearchedNews(SearchViewCode.VIEW_SEARCH_RESULT_BLIND, "쇼핑 중독에 걸리다", "오렌지레터", "02/01/2021", 3, R.drawable.news_3, R.drawable.brand_4, false));
*/

                    rv_search_result.setAdapter(searchedNewsResultAdapter);
                    ll_search_result.setVisibility(View.VISIBLE);
                    initScrollListener(rv_search_result, searchedNewsResultAdapter);

                }


            }
        });

        //x버튼
        fl_btn_x.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search.setText(null);
                ll_search_result.setVisibility(View.GONE);
            }
        });


        return view;
    }

    //토큰 가져오기
    private void getUserData() {
        token = MainActivity.getToken();
    }

    //findViewById 모아놓기
    private void findViewByIdAll(View view) {
        rv_rank = view.findViewById(R.id.rv_rank);
        btn_search = view.findViewById(R.id.btn_search);
        et_search = view.findViewById(R.id.et_search);
        btn_x = view.findViewById(R.id.btn_x);
        tv_search_result = view.findViewById(R.id.tv_search_result);
        ll_search_result = view.findViewById(R.id.ll_search_result);
        rv_search_result = view.findViewById(R.id.rv_search_result);
        fl_btn_search = view.findViewById(R.id.fl_btn_search);
        fl_btn_x = view.findViewById(R.id.fl_btn_x);
    }

    private void setRankRv(View view) {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_rank.setLayoutManager((layoutManager));
        SearchedNewsRankAdapter searchedNewsAdapter = new SearchedNewsRankAdapter();

        //데이터 세팅
        setNewsToRv(searchedNewsAdapter);
    }

    private void setNewsToRv(SearchedNewsRankAdapter searchedNewsAdapter) {
        searchedNewsAdapter.removeAll(); //안 쌓이게 하기

        //서버에서 구독메일을 받아서 리사이클러뷰의 내용을 세팅함
        ArrayList<SearchedNews> searchedNewsArrayList = new ArrayList<>();
        tryGetRankLetter(searchedNewsArrayList, searchedNewsAdapter); //get요청
    }

    private void tryGetRankLetter(ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsRankAdapter searchedNewsAdapter) {
        JSONObject requestData1 = makeJsonObject();
        makeGetRequestMailBox(requestData1, makeRankUrl(), searchedNewsArrayList, searchedNewsAdapter);
    }

    private void makeGetRequestMailBox(JSONObject requestData, String rankUrl, ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsRankAdapter searchedNewsAdapter) {
        //서버에 요청을 보내기 위한 StringRequest 객체 생성
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                rankUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponseForRank(response, searchedNewsArrayList, searchedNewsAdapter);
                        setRankLetterListToRv(searchedNewsArrayList, searchedNewsAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "순위 데이터 오류", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return super.getParams();
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("x-access-token", token);

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void setRankLetterListToRv(ArrayList<SearchedNews> searchedNewsArrayList,
                                       SearchedNewsRankAdapter searchedNewsAdapter) {

        for (int i = 0; i < searchedNewsArrayList.size(); i++) {
            searchedNewsAdapter.addItem(searchedNewsArrayList.get(i));
        }
        // 하단 RV : 어답터 설정, 무한 스크롤 설정
        rv_rank.setAdapter(searchedNewsAdapter);

    }

    private void processResponseForRank(JSONObject response, ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsRankAdapter searchedNewsAdapter) {
        Gson gson = new Gson();
        RankResult rankResult = gson.fromJson(String.valueOf(response), RankResult.class);
        for (int i = 0; i < rankResult.getResultList().size(); i++) {
            SearchedNews searchedNews = null;
            searchedNews = new SearchedNews();

            searchedNews.setImg_brand(rankResult.getResultList().get(i).getPlatformImageUrl());
            searchedNews.setImg_news(rankResult.getResultList().get(i).getThumbnailImageUrl());
            searchedNews.setDate(rankResult.getResultList().get(i).getCreatedAt());
            searchedNews.setTitle(rankResult.getResultList().get(i).getTitle());
            searchedNews.setBrand(rankResult.getResultList().get(i).getPlatformName());
            searchedNews.setNumRank(i + 1); //인덱스대로 순위 순서임
            searchedNews.setBookmarkClicked(false);

            //구독 중이 아니면 뷰타입 블라인드로 처리하기
            if (rankResult.getResultList().get(i).getSubscribing() == true) {
                searchedNews.setViewType(SearchViewCode.VIEW_RANK_SEARCH);
            } else {
                searchedNews.setViewType(SearchViewCode.VIEW_SEARCH_RANK_BLIND);
            }

            searchedNewsArrayList.add(searchedNews);
        }
    }

    private String makeRankUrl() {
        String url;
        url = "http://3.13.65.158/v1/letters/bookmark-rank";
        return url;
    }

    private JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("x-access-token", token);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return requestData;
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
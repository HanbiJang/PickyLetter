package com.makeus.pineapple.search;

import android.content.Context;
import android.content.Intent;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.popup.loading.PopupLoading;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.search.adapters.SearchedNewsRankAdapter;
import com.makeus.pineapple.search.adapters.SearchedNewsResultAdapter;
import com.makeus.pineapple.search.data.SearchedNews;
import com.makeus.pineapple.search.data.server_data.SearchResult;
import com.makeus.pineapple.server_controllers.get.GetSearchResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment2_Search extends Fragment {
    static View view;
    static Context myContext;

    static String token = null;
    RequestQueue requestQueue;
    public static Integer lastLetterId = 0;
    public static String searchKeyword = null;

    RecyclerView rv_rank;
    public static RecyclerView rv_search_result;
    public static SearchedNewsResultAdapter searchedNewsResultAdapter;


    Button btn_search;
    EditText et_search;
    Button btn_x;
    TextView tv_search_result;
    LinearLayout ll_search_result;
    FrameLayout fl_btn_x, fl_btn_search;

    //검색 결과 무한 스크롤
    boolean isLoading = false;

    //새로고침
    public static SwipeRefreshLayout sr_layout;
    //로딩 상태변화
    public static boolean setLoadingPopup = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2_search, container, false);
        myContext = getContext();

        setLoadingPopup = false;

        lastLetterId = 0;
        // 모든 컴포넌트 findViewById
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
                searchKeyword = et_search.getText().toString();
                if (searchKeyword.length() >= 2) {
                    lastLetterId = 0;

                    tv_search_result.setText("'" + searchKeyword + "'" + " 검색 결과");
                    ll_search_result.setVisibility(View.VISIBLE);
                    // 검색 결과 리사이클러뷰 결과 만들기

                    setResultRv(view);

                }
                else{
                    Toast.makeText(getContext(), "2글자 이상을 검색하세요", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //x버튼
        fl_btn_x.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search.setText(null);
                rv_search_result.setAdapter(null);
                ll_search_result.setVisibility(View.GONE);
            }
        });

/*        //네비게이션 디버깅 코드
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.toggleNavigationBarItems(true);
            }
        }, 500);*/


        //스와이프 새로고침
        sr_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLoadingPopup = false; //로딩팝업 관련
                setRankRv(view);
                sr_layout.setRefreshing(false); //새로고침 멈춤

            }
        });

        return view;
    }

    private void setResultRv(View view) {
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_search_result.setLayoutManager((layoutManager2));
        searchedNewsResultAdapter = new SearchedNewsResultAdapter();

        GetSearchResult getSearchResult = new GetSearchResult(
                myContext,
                rv_search_result,
                searchedNewsResultAdapter
        );

        getSearchResult.tryRequest();

/*        //데이터 서버에서 가져오기
        searchedNewsResultAdapter.removeAll(); //안 쌓이게 하기

        //서버에서 구독메일을 받아서 리사이클러뷰의 내용을 세팅함
        ArrayList<SearchedNews> searchedNewsArrayList = new ArrayList<>();
        tryGetResultLetter(searchedNewsArrayList, searchedNewsResultAdapter); //get요청*/

    }

    private void tryGetResultLetter(ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsResultAdapter searchedNewsResultAdapter) {
        //시작 로딩 팝업
        setLoadingPopup = false;
        Intent intent = new Intent(getContext(), PopupLoading.class);
        intent.putExtra("pastFragmentNum", 2);
        getContext().startActivity(intent);

        JSONObject requestData1 = makeJsonObject();
        makeGetRequestSearchResult(requestData1, makeSearchResultUrl(), searchedNewsArrayList, searchedNewsResultAdapter);
    }

    private void makeGetRequestSearchResult(JSONObject requestData, String makeSearchResultUrl, ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsResultAdapter searchedNewsResultAdapter) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                makeSearchResultUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponseForResult(response, searchedNewsArrayList, searchedNewsResultAdapter);
                        setResultLetterListToRv(searchedNewsArrayList, searchedNewsResultAdapter);
                        setLoadingPopup = true;
                        sr_layout.setRefreshing(false); //새로고침 멈춤
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

    private void setResultLetterListToRv(ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsResultAdapter searchedNewsResultAdapter) {
        //데이터 세팅
        if (searchedNewsArrayList.size() != 0) {
            for (int i = 0; i < searchedNewsArrayList.size(); i++) {
                searchedNewsResultAdapter.addItem(searchedNewsArrayList.get(i));
            }
            //어답터 설정, 무한 스크롤 설정
            rv_search_result.setAdapter(searchedNewsResultAdapter);
            initScrollListener(rv_search_result, searchedNewsResultAdapter);
        }

    }

    private void processResponseForResult(JSONObject response, ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsResultAdapter searchedNewsResultAdapter) {
        Gson gson = new Gson();
        SearchResult rankResult = gson.fromJson(String.valueOf(response), SearchResult.class);
        for (int i = 0; i < rankResult.getResultList().size(); i++) {
            SearchedNews searchedNews = null;
            searchedNews = new SearchedNews();

            searchedNews.setPlatformId(rankResult.getResultList().get(i).getPlatformId());
            searchedNews.setImg_brand(rankResult.getResultList().get(i).getPlatformImageUrl());
            searchedNews.setImg_news(rankResult.getResultList().get(i).getThumbnailImageUrl());
            searchedNews.setDate(rankResult.getResultList().get(i).getCreatedAt());
            searchedNews.setTitle(rankResult.getResultList().get(i).getTitle());
            searchedNews.setBrand(rankResult.getResultList().get(i).getPlatformName());
            searchedNews.setNumRank(0); //순위 무시
            searchedNews.setLetterId(rankResult.getResultList().get(i).getLetterId());
            searchedNews.setBookmarkId(rankResult.getResultList().get(i).getBookmarkId());
            searchedNews.setBookmarkCount(rankResult.getResultList().get(i).getBookmarkCount());
            searchedNews.setBookmarkClicked(rankResult.getResultList().get(i).getBookmarkId());
            searchedNews.setModifiedAt(rankResult.getResultList().get(i).getModifiedAt());
            searchedNews.setPlatformId(rankResult.getResultList().get(i).getBookmarkId());
            searchedNews.setSubscribing(rankResult.getResultList().get(i).getSubscribing());

            //마지막 letterId 저장
            if (i == rankResult.getResultList().size() - 1) {
                lastLetterId = rankResult.getResultList().get(i).getLetterId();
            }

            //구독 중이 아니면 뷰타입 블라인드로 처리하기
            if (rankResult.getResultList().get(i).getSubscribing() == true) {
                searchedNews.setViewType(SearchViewCode.VIEW_SEARCH_RESULT);
            } else {
                searchedNews.setViewType(SearchViewCode.VIEW_SEARCH_RESULT_BLIND);
            }

            searchedNewsArrayList.add(searchedNews);

        }
    }

    private String makeSearchResultUrl() {
        String url;
        url = "http://3.13.65.158/v1/letters/search?lastLetterId=" + lastLetterId + "&searchKeyword=" + searchKeyword;
        return url;
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
        sr_layout = view.findViewById(R.id.sr_layout);
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
        //시작 로딩 팝업
        setLoadingPopup = false;
        Intent intent = new Intent(getContext(), PopupLoading.class);
        intent.putExtra("pastFragmentNum", 2);
        getContext().startActivity(intent);

        JSONObject requestData1 = makeJsonObject();
        makeGetRequestMailBox(requestData1, makeRankUrl(), searchedNewsArrayList, searchedNewsAdapter);
    }

    private void makeGetRequestMailBox(JSONObject requestData, String rankUrl, ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsRankAdapter searchedNewsAdapter) {
        //서버에 요청을 보내기 위한 객체 생성
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                rankUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponseForRank(response, searchedNewsArrayList, searchedNewsAdapter);
                        setRankLetterListToRv(searchedNewsArrayList, searchedNewsAdapter);
                        setLoadingPopup = true;
                        sr_layout.setRefreshing(false); //새로고침 멈춤
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
        //어답터 설정
        rv_rank.setAdapter(searchedNewsAdapter);

    }

    //순위 리사이클러뷰 데이터 처리
    private void processResponseForRank(JSONObject response, ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsRankAdapter searchedNewsAdapter) {
        Gson gson = new Gson();
        SearchResult rankResult = gson.fromJson(String.valueOf(response), SearchResult.class);
        for (int i = 0; i < rankResult.getResultList().size(); i++) {
            SearchedNews searchedNews = null;
            searchedNews = new SearchedNews();

            searchedNews.setPlatformId(rankResult.getResultList().get(i).getPlatformId());
            searchedNews.setImg_brand(rankResult.getResultList().get(i).getPlatformImageUrl());
            searchedNews.setImg_news(rankResult.getResultList().get(i).getThumbnailImageUrl());
            searchedNews.setDate(rankResult.getResultList().get(i).getCreatedAt());
            searchedNews.setTitle(rankResult.getResultList().get(i).getTitle());
            searchedNews.setBrand(rankResult.getResultList().get(i).getPlatformName());
            searchedNews.setNumRank(i + 1); //인덱스대로 순위 순서임
            searchedNews.setLetterId(rankResult.getResultList().get(i).getLetterId());
            searchedNews.setBookmarkId(rankResult.getResultList().get(i).getBookmarkId());
            searchedNews.setBookmarkCount(rankResult.getResultList().get(i).getBookmarkCount());
            searchedNews.setBookmarkClicked(rankResult.getResultList().get(i).getBookmarkId());

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
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchedNewsResultAdapter.getItemCount()-1) {//bottom of list!
                        loadMore(searchedNewsResultAdapter);
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore(SearchedNewsResultAdapter searchedNewsResultAdapter) {

/*            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchedNewsResultAdapter.addItem(null); //로딩뷰 추가
                    searchedNewsResultAdapter.notifyItemInserted(searchedNewsResultAdapter.getItems().size() - 1);
                }
            }, 300);*/

            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //로딩뷰 제거
//                    searchedNewsResultAdapter.removeItems(searchedNewsResultAdapter.getItems().size() - 1);

                    int scrollPosition = searchedNewsResultAdapter.getItems().size();

                    int pastScrollPosition =  scrollPosition;
                    searchedNewsResultAdapter.notifyItemRemoved(scrollPosition);

                    //get요청
                    ArrayList<SearchedNews> bookmarkLetterArrayListAdded = new ArrayList<>();
                    tryGetResultLetter(bookmarkLetterArrayListAdded, searchedNewsResultAdapter);

                    searchedNewsResultAdapter.notifyDataSetChanged();
                    isLoading = false;
                    rv_search_result.scrollToPosition(pastScrollPosition);

                }
            }, 1200);



    }
}
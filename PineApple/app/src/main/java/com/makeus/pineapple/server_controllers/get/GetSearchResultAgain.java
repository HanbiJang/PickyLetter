package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;
import com.makeus.pineapple.mypage_settings.mypage.adapter.BookmarkLetterAdapter;
import com.makeus.pineapple.search.Fragment2_Search;
import com.makeus.pineapple.search.SearchViewCode;
import com.makeus.pineapple.search.adapters.SearchedNewsResultAdapter;
import com.makeus.pineapple.search.data.SearchedNews;
import com.makeus.pineapple.search.data.server_data.SearchResult;
import com.makeus.pineapple.search.searchViewHolders.LoadingViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;

//레터 정보 불러오는 클래스
public class GetSearchResultAgain implements GetRequestInterface {
    Context myContext;
    RecyclerView rv_result;
    RequestQueue requestQueue;
    SearchResult searchResult;
    public ArrayList<SearchedNews> searchedNewsArrayList;
    SearchedNewsResultAdapter searchedNewsResultAdapter;

    boolean startRequest = true;

    public GetSearchResultAgain(Context myContext, RecyclerView rv_bottom, SearchedNewsResultAdapter searchedNewsResultAdapter) {
        this.myContext = myContext;
        this.rv_result = rv_bottom;
        this.searchedNewsResultAdapter = searchedNewsResultAdapter;

        searchedNewsArrayList = new ArrayList<>(); //한번만 생성
    }

    @Override
    public void tryRequest() {
        //get 요청 관련
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

        JSONObject requestData = makeJsonObject();
        Log.e("get요청 시작: 서치시작", " ");
        startRequest = false;
        makeJsonRequest(requestData, makeRequestUrl(" "), requestQueue);
    }

    @Override
    public String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/letters/search?lastLetterId=" + Fragment2_Search.lastLetterId + "&searchKeyword=" + Fragment2_Search.searchKeyword;
        Log.e("요청 유알엘", url);
        return url;
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        searchResult = gson.fromJson(String.valueOf(response), SearchResult.class);
        for (int i = 0; i < searchResult.getResultList().size(); i++) {
            SearchedNews searchedNews = null;
            searchedNews = new SearchedNews();

            searchedNews.setPlatformId(searchResult.getResultList().get(i).getPlatformId());
            searchedNews.setImg_brand(searchResult.getResultList().get(i).getPlatformImageUrl());
            searchedNews.setImg_news(searchResult.getResultList().get(i).getThumbnailImageUrl());
            searchedNews.setDate(searchResult.getResultList().get(i).getCreatedAt());
            searchedNews.setTitle(searchResult.getResultList().get(i).getTitle());
            searchedNews.setBrand(searchResult.getResultList().get(i).getPlatformName());
            searchedNews.setNumRank(0); //순위 무시
            searchedNews.setLetterId(searchResult.getResultList().get(i).getLetterId());
            searchedNews.setBookmarkId(searchResult.getResultList().get(i).getBookmarkId());
            searchedNews.setBookmarkCount(searchResult.getResultList().get(i).getBookmarkCount());
            searchedNews.setBookmarkClicked(searchResult.getResultList().get(i).getBookmarkId());
            searchedNews.setModifiedAt(searchResult.getResultList().get(i).getModifiedAt());
            searchedNews.setPlatformId(searchResult.getResultList().get(i).getBookmarkId());
            searchedNews.setSubscribing(searchResult.getResultList().get(i).getSubscribing());

            //마지막 letterId 저장
            if (i == searchResult.getResultList().size() - 1) {
                Fragment2_Search.lastLetterId = searchResult.getResultList().get(i).getLetterId();
            }

            //구독 중이 아니면 뷰타입 블라인드로 처리하기
            if (searchResult.getResultList().get(i).getSubscribing() == true) {
                searchedNews.setViewType(SearchViewCode.VIEW_SEARCH_RESULT);
            } else {
                searchedNews.setViewType(SearchViewCode.VIEW_SEARCH_RESULT_BLIND);
            }

            searchedNewsArrayList.add(searchedNews);

        }

        setResultLetterListToRv(searchedNewsArrayList, searchedNewsResultAdapter);
        Fragment2_Search.setLoadingPopup = true;
        Fragment2_Search.sr_layout.setRefreshing(false); //새로고침 멈춤

    }

    private void setResultLetterListToRv(ArrayList<SearchedNews> searchedNewsArrayList, SearchedNewsResultAdapter searchedNewsResultAdapter) {
        //데이터 세팅
        Log.e("1", "setResultLetterListToRv 설정 함수 부름");
        //로딩뷰 없애기
        Fragment2_Search.searchedNewsResultAdapter.removeItems(Fragment2_Search.searchedNewsResultAdapter.getItems().size() - 1);
        Fragment2_Search.searchedNewsResultAdapter.notifyItemRemoved(Fragment2_Search.searchedNewsResultAdapter.getItems().size());

        if (searchedNewsArrayList.size() != 0) {
            for (int i = 0; i < searchedNewsArrayList.size(); i++) {
                searchedNewsResultAdapter.addItem(searchedNewsArrayList.get(i));
            }

            Log.e("요청","로딩뷰넣기");
            searchedNewsResultAdapter.addItem(null); //로딩뷰 넣기
            searchedNewsResultAdapter.notifyItemInserted(Fragment2_Search.searchedNewsResultAdapter.getItems().size());

            //프로그레스바 보이기
            LoadingViewHolder.progressBar.setVisibility(View.GONE);
            LoadingViewHolder.btn_more.setVisibility(View.VISIBLE);
        }
    }


}

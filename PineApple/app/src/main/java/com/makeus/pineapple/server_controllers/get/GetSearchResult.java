package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetterResult;
import com.makeus.pineapple.search.Fragment2_Search;
import com.makeus.pineapple.search.SearchViewCode;
import com.makeus.pineapple.search.adapters.SearchedNewsResultAdapter;
import com.makeus.pineapple.search.data.SearchedNews;
import com.makeus.pineapple.search.data.server_data.SearchResult;

import org.json.JSONObject;

import java.util.ArrayList;

//레터 정보 불러오는 클래스
public class GetSearchResult implements GetRequestInterface {
    Context myContext;
    RecyclerView rv_result;
    RequestQueue requestQueue;
    SearchResult searchResult;
    public ArrayList<SearchedNews> searchedNewsArrayList;
    SearchedNewsResultAdapter searchedNewsResultAdapter;

    boolean startRequest = true;

    public GetSearchResult(Context myContext, RecyclerView rv_bottom, SearchedNewsResultAdapter searchedNewsResultAdapter) {
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
        searchedNewsResultAdapter.removeAll();
        if (searchedNewsArrayList.size() != 0) {
            for (int i = 0; i < searchedNewsArrayList.size(); i++) {
                searchedNewsResultAdapter.addItem(searchedNewsArrayList.get(i));
            }
            //어답터 설정, 무한 스크롤 설정
            Fragment2_Search.rv_search_result.setAdapter(searchedNewsResultAdapter);
            Log.e("요청","로딩뷰넣기");
            searchedNewsResultAdapter.addItem(null); //로딩뷰 넣기
            searchedNewsResultAdapter.notifyItemInserted(Fragment2_Search.searchedNewsResultAdapter.getItems().size());

        }
    }


/*    public void setToAdapter() {
        for (int i = 0; i < searchResult.getResultList().size(); i++) {
            BookmarkLetter bookmarkLetter;
            bookmarkLetter = new BookmarkLetter(
                    searchResult.getResultList().get(i).getLetterId(),
                    searchResult.getResultList().get(i).getPlatformId(),
                    searchResult.getResultList().get(i).getPlatformName(),
                    searchResult.getResultList().get(i).getPlatformImageUrl(),
                    searchResult.getResultList().get(i).getBookmarkId(),
                    searchResult.getResultList().get(i).getTitle(),
                    searchResult.getResultList().get(i).getThumbnailImageUrl(),
                    searchResult.getResultList().get(i).getBookmarkCount(),
                    searchResult.getResultList().get(i).getCreatedAt(),
                    searchResult.getResultList().get(i).getModifiedAt(),
                    searchResult.getResultList().get(i).getSubscribing()
            );
            searchedNewsArrayList.add(bookmarkLetter);

            if(i == searchResult.getResultList().size()-1){
                Log.e("북마크 요청", Fragment3_MyPage.lastLetterId + "마지막 레터아이디" );
                Fragment3_MyPage.lastLetterId = searchedNewsArrayList.get(i).getLetterId();
            }
        }
    }

    //


    private void setRv() {
        Log.e("1", "setNewLetterListToRv 설정 함수 부름");
        searchedNewsResultAdapter.removeAll();
        if (searchedNewsArrayList.size() != 0) {
            for (int i = 0; i < searchedNewsArrayList.size(); i++) {
                searchedNewsResultAdapter.addItem(searchedNewsArrayList.get(i));
            }


            Log.e(" ", "아이템 사이즈" + Fragment3_MyPage.bookmarkLetterAdapter.getItems().size());
            rv_result.setAdapter(searchedNewsResultAdapter);
            Log.e("요청","로딩뷰넣기");
            searchedNewsResultAdapter.addItem(null); //로딩뷰 넣기
            searchedNewsResultAdapter.notifyItemInserted(Fragment3_MyPage.bookmarkLetterAdapter.getItems().size());
        }
    }*/


}

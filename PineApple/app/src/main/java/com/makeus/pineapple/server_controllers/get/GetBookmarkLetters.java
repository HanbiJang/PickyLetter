package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.bookmark.BookmakrResult;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.home.adapters.HomeAdapters;
import com.makeus.pineapple.home.adapters.OldLetterAdapter;
import com.makeus.pineapple.home.data.HomeLetters;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;
import com.makeus.pineapple.mypage_settings.mypage.adapter.BookmarkLetterAdapter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetterResult;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;
import com.makeus.pineapple.server_controllers.server_data.NewsData;
import com.makeus.pineapple.server_controllers.server_data.NewsResult;

import org.json.JSONObject;

import java.util.ArrayList;

//레터 정보 불러오는 클래스
public class GetBookmarkLetters implements GetRequestInterface {
    Context myContext;
    RecyclerView rv_bottom;
    RequestQueue requestQueueBookmark;
    BookmarkLetterResult bookmakrResult;
    public ArrayList<BookmarkLetter> bookmarkLetterArrayList;
    BookmarkLetterAdapter bookmarkLetterAdapter;

    boolean startRequest = true;


    public GetBookmarkLetters(Context myContext, RecyclerView rv_bottom, BookmarkLetterAdapter bookmarkLetterAdapter) {
        this.myContext = myContext;
        this.rv_bottom = rv_bottom;
        this.bookmarkLetterAdapter = bookmarkLetterAdapter;

        bookmarkLetterArrayList = new ArrayList<>(); //한번만 생성
    }

    @Override
    public void tryRequest() {

        //get 요청 관련
        if (requestQueueBookmark == null) {
            requestQueueBookmark = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

//        Fragment3_MyPage.page += 1;

        JSONObject requestData = makeJsonObject();
        Log.e("get요청 시작: 북마크 가져오기", " ");
        startRequest = false;
        makeJsonRequest(requestData, makeRequestUrl(" "), requestQueueBookmark);
    }

    @Override
    public String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/users/current/bookmarks?lastLetterId=" + Fragment3_MyPage.lastLetterId;
        Log.e("요청 유알엘", url);
        return url;
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        bookmakrResult = gson.fromJson(String.valueOf(response), BookmarkLetterResult.class);

        Log.e(" 리스폰스 값 !!! ", response.toString() + "  ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ");

        setLoadingView(); //로딩뷰 변수 설정
        setToAdapter(); //어답터에 데이터 설정

        Log.e("북마크 사이즈", bookmakrResult.getResultList().size() + "");
        startRequest = true;

//        if (bookmakrResult.getResultList().size() != 0 && startRequest == true && Fragment3_MyPage.page <= Fragment3_MyPage.pageLimit) {
//            tryRequest();
//        }

        setRv();

        Fragment3_MyPage.setLoadingPopup = true;
        Fragment3_MyPage.sr_layout.setRefreshing(false); //새로고침 멈춤

    }

    public void setLoadingView() {
        if (bookmakrResult.getResultList().size() == 0) {
            Fragment3_MyPage.setLoadingPopup = true;
        }
    }

    public void setToAdapter() {
        for (int i = 0; i < bookmakrResult.getResultList().size(); i++) {
            BookmarkLetter bookmarkLetter;
            bookmarkLetter = new BookmarkLetter(
                    bookmakrResult.getResultList().get(i).getLetterId(),
                    bookmakrResult.getResultList().get(i).getPlatformId(),
                    bookmakrResult.getResultList().get(i).getPlatformName(),
                    bookmakrResult.getResultList().get(i).getPlatformImageUrl(),
                    bookmakrResult.getResultList().get(i).getBookmarkId(),
                    bookmakrResult.getResultList().get(i).getTitle(),
                    bookmakrResult.getResultList().get(i).getThumbnailImageUrl(),
                    bookmakrResult.getResultList().get(i).getBookmarkCount(),
                    bookmakrResult.getResultList().get(i).getCreatedAt(),
                    bookmakrResult.getResultList().get(i).getModifiedAt(),
                    bookmakrResult.getResultList().get(i).getSubscribing()
            );
            bookmarkLetterArrayList.add(bookmarkLetter);

            if(i == bookmakrResult.getResultList().size()-1){
                Log.e("북마크 요청", Fragment3_MyPage.lastLetterId + "마지막 레터아이디" );
                Fragment3_MyPage.lastLetterId = bookmarkLetterArrayList.get(i).getLetterId();
            }
        }
    }

    //


    private void setRv() {
        Log.e("1", "setNewLetterListToRv 설정 함수 부름");
        bookmarkLetterAdapter.removeAll();
        if (bookmarkLetterArrayList.size() != 0) {
            for (int i = 0; i < bookmarkLetterArrayList.size(); i++) {
                bookmarkLetterAdapter.addItem(bookmarkLetterArrayList.get(i));
            }


            Log.e(" ", "아이템 사이즈" + Fragment3_MyPage.bookmarkLetterAdapter.getItems().size());
            rv_bottom.setAdapter(bookmarkLetterAdapter);
            Log.e("요청","로딩뷰넣기");
//            bookmarkLetterAdapter.addItem(null); //로딩뷰 넣기
            bookmarkLetterAdapter.addItem(null); //로딩뷰 넣기
            bookmarkLetterAdapter.notifyItemInserted(Fragment3_MyPage.bookmarkLetterAdapter.getItems().size());
        }
    }


}

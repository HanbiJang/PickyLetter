package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;
import com.makeus.pineapple.mypage_settings.mypage.adapter.BookmarkLetterAdapter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetterResult;

import org.json.JSONObject;

import java.util.ArrayList;

//레터 정보 불러오는 클래스
public class GetBookmarkLettersAgain implements GetRequestInterface {
    Context myContext;
    RecyclerView rv_bottom;
    RequestQueue requestQueueBookmark;
    BookmarkLetterResult bookmakrResult;
    public ArrayList<BookmarkLetter> bookmarkLetterArrayList;
    BookmarkLetterAdapter bookmarkLetterAdapter;

    boolean startRequest = true;


    public GetBookmarkLettersAgain(Context myContext, RecyclerView rv_bottom, BookmarkLetterAdapter bookmarkLetterAdapter) {
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
        Log.e("get재요청 시작: 북마크 가져오기", " ");
        startRequest = false;
        makeJsonRequest(requestData, makeRequestUrl(" "), requestQueueBookmark);
    }

    @Override
    public String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/users/current/bookmarks?lastLetterId=" + Fragment3_MyPage.lastLetterId;
        Log.e("재요청 유알엘", url);
        return url;
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        bookmakrResult = gson.fromJson(String.valueOf(response), BookmarkLetterResult.class);

        setLoadingView(); //로딩뷰 변수 설정
        setToAdapter(); //어답터에 데이터 설정

        Log.e("북마크 사이즈", bookmakrResult.getResultList().size() + "");
        startRequest = true;

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
                Log.e("북마크 재요청", Fragment3_MyPage.lastLetterId + "마지막 레터아이디" );
                Fragment3_MyPage.lastLetterId = bookmarkLetterArrayList.get(i).getLetterId();
            }
        }
    }

    //


    private void setRv() {
        Log.e("1", "setNewLetterListToRv 재설정 함수 부름");

        //로딩뷰 없애기
        Fragment3_MyPage.bookmarkLetterAdapter.removeItems(Fragment3_MyPage.bookmarkLetterAdapter.getItems().size() - 1);
        Fragment3_MyPage.bookmarkLetterAdapter.notifyItemRemoved(Fragment3_MyPage.bookmarkLetterAdapter.getItems().size());

        if (bookmarkLetterArrayList.size() != 0) {

            for (int i = 0; i < bookmarkLetterArrayList.size(); i++) {
                bookmarkLetterAdapter.addItem(bookmarkLetterArrayList.get(i));
            }
            bookmarkLetterAdapter.addItem(null); //로딩뷰 넣기

            Log.d("11", "북마크 아이템 사이즈" + bookmarkLetterArrayList.size());

            Fragment3_MyPage.bookmarkLetterAdapter.notifyItemInserted(Fragment3_MyPage.bookmarkLetterAdapter.getItems().size());

            Log.d("11", "북마크 마지막 아이템" + bookmarkLetterAdapter.getItemCount());

            //프로그레스바 보이기
            BookmarkLetterAdapter.LoadingViewHolder.progressBar.setVisibility(View.GONE);
            BookmarkLetterAdapter.LoadingViewHolder.btn_more.setVisibility(View.VISIBLE);

        }
    }


}

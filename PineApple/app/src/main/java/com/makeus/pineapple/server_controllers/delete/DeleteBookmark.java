package com.makeus.pineapple.server_controllers.delete;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.R;
import com.makeus.pineapple.bookmark.BookmakrResult;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;
import com.makeus.pineapple.popup.loading.PopupLoading;
import com.makeus.pineapple.server_controllers.get.GetLetterInformBeforeBookmarkDel;

import org.json.JSONObject;

public class DeleteBookmark implements DeleteRequest {
    Context context;
    Button btn_bookmark;
    RequestQueue requestQueueBookmarkDel, requestQueueGetLetterInform;
    public static Integer bookmarkId = null, letterId;

    public DeleteBookmark(Context context, Integer letterId, Button btn_bookmark) {
        this.context = context;
        this.letterId = letterId;
        this.btn_bookmark = btn_bookmark;
    }

    @Override
    public void tryRequest() {
        if (requestQueueBookmarkDel == null) {
            requestQueueBookmarkDel = Volley.newRequestQueue(context); // 큐 객체 생성하기
        }
        if (requestQueueGetLetterInform == null){
            requestQueueGetLetterInform = Volley.newRequestQueue(context);
        }

        //bookmarkID 메일 확인 Get 요청으로 가져오기
        GetLetterInformBeforeBookmarkDel getLetterInform =
                new GetLetterInformBeforeBookmarkDel(
                        requestQueueBookmarkDel,
                        letterId,
                        btn_bookmark);
        getLetterInform.tryRequest();
        
        android.os.Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() { //2초간 실행

            @Override
            public void run() {
                Log.e("북마크 삭제 bookmarkId값", "DeleteBookmark클래스" + bookmarkId + "");
                JSONObject requestData1 = makeJsonObject();
                makeJsonRequest(requestData1, makeRequestUrl(bookmarkId), requestQueueGetLetterInform);
            }
        },500);
    }



    @Override
    public JSONObject makeJsonObject()
    {
        JSONObject requestData = new JSONObject();
        try{
            requestData.put("bookmarkId", bookmarkId);
        } catch (Exception e) {
        }
        return requestData;
    }

    @Override
    public String makeRequestUrl(Object data) {
        String url;
        data = (Integer) bookmarkId;
        url = "http://3.13.65.158/v1/users/current/bookmarks/" + data;
        return url;
    }

    @Override
    public void processResponse(JSONObject response)
    {
        Gson gson = new Gson();
        BookmakrResult bookmakrResult = gson.fromJson(String.valueOf(response), BookmakrResult.class);
        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_line); //이미지 바꿈
        Log.e("0", "북마크 삭제 성공");

        //북마크 버튼 활성화 시키기
        btn_bookmark.setEnabled(true);
        btn_bookmark.setClickable(true);

        //마이페이지 새로고침
        if(MainActivity.fragment3_mypage !=null)
        {
            //서버통신
            Fragment3_MyPage.lastLetterId = 0; //마이페이지 북마크 조회 기능 초기화
            Fragment3_MyPage.page = -1;
            Fragment3_MyPage.pageLimit = 0;

            Fragment3_MyPage.setUserData();
            Fragment3_MyPage.setBookmarkRv(Fragment3_MyPage.view);
        }
    }


}

package com.makeus.pineapple.server_controllers.delete;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.makeus.pineapple.R;
import com.makeus.pineapple.bookmark.BookmakrResult;
import com.makeus.pineapple.server_controllers.get.GetLetterInformBeforeBookmarkDel;

import org.json.JSONObject;

public class DeleteBookmark implements DeleteRequest {
    Button btn_bookmark;
    RequestQueue requestQueueBookmarkDel, requestQueueGetLetterInform;
    public static Integer bookmarkId = null, letterId;

    public DeleteBookmark(RequestQueue requestQueueBookmarkDel, RequestQueue requestQueueGetLetterInform, Integer letterId, Button btn_bookmark) {
        this.requestQueueBookmarkDel = requestQueueBookmarkDel;
        this.requestQueueGetLetterInform = requestQueueGetLetterInform;
        this.letterId = letterId;
        this.btn_bookmark = btn_bookmark;
    }

    @Override
    public void tryRequest() {
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
    public JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        try {
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
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        BookmakrResult bookmakrResult = gson.fromJson(String.valueOf(response), BookmakrResult.class);
        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_line); //이미지 바꿈
        Log.e("0", "북마크 삭제 성공");
    }


}

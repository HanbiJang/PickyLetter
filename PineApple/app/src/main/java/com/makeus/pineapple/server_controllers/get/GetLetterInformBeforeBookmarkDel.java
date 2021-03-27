package com.makeus.pineapple.server_controllers.get;

import android.util.Log;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.makeus.pineapple.server_controllers.delete.DeleteBookmark;
import com.makeus.pineapple.server_controllers.server_data.NewsData;

import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetLetterInformBeforeBookmarkDel implements GetLetterInformInterface {
    RequestQueue requestQueueMailInform;
    NewsData newLetter;
    Button btn_bookmark;
    Integer letterId;

    public GetLetterInformBeforeBookmarkDel(RequestQueue requestQueueMailInform, Integer letterId, Button btn_bookmark){
        this.requestQueueMailInform = requestQueueMailInform;
        this.btn_bookmark = btn_bookmark;
        this.letterId = letterId;
    }


    @Override
    public void tryRequest() {
        JSONObject requestData1 = makeJsonObject();
        Log.e("get요청 시작: 북마크값가져옴", DeleteBookmark.bookmarkId +"");
        makeJsonRequest(requestData1, makeRequestUrl(letterId),requestQueueMailInform);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        newLetter = gson.fromJson(String.valueOf(response), NewsData.class);
        DeleteBookmark.bookmarkId = newLetter.getBookmarkId();
        Log.e("get요청 마무리: 북마크값가져옴", DeleteBookmark.bookmarkId +"");

    }

}

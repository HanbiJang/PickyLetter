package com.makeus.pineapple.server_controllers.post;

import android.util.Log;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.makeus.pineapple.R;
import com.makeus.pineapple.bookmark.BookmakrResult;

import org.json.JSONObject;

public class PostBookmarkAdd implements PostBookmarkAddInterface{
    Button btn_bookmark;
    RequestQueue requestQueueBookmarkAdd;
    Integer letterId;

    public PostBookmarkAdd(RequestQueue requestQueueMailInform, Integer letterId, Button btn_bookmark){
        this.requestQueueBookmarkAdd = requestQueueMailInform;
        this.btn_bookmark = btn_bookmark;
        this.letterId = letterId;
    }

    @Override
    public void tryRequest() {
        JSONObject requestData1 = makeJsonObject();
        makeJsonRequest(requestData1, makeRequestUrl(letterId), requestQueueBookmarkAdd);
    }

    @Override
    public JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        try {
            Log.e("넣는 레터 아이디",  letterId +"");
            requestData.put("letterId", letterId);

        } catch (Exception e) {

        }
        return requestData;
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        BookmakrResult bookmakrResult = gson.fromJson(String.valueOf(response), BookmakrResult.class);
        Log.d("북마크 동작: ", String.valueOf(bookmakrResult.isResult()));
        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill); //이미지 바꿈

    }

}

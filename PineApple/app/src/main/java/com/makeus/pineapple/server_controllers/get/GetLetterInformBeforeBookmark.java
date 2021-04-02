package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.HomeMail;
import com.makeus.pineapple.bookmark.AddOrDelBookmark;
import com.makeus.pineapple.server_controllers.server_data.NewsData;

import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetLetterInformBeforeBookmark implements GetLetterInformInterface {
    Context myContext;
    RequestQueue requestQueueMailInform;
    NewsData newsData;
    Button btn_bookmark;
    static Integer letterId;

    public GetLetterInformBeforeBookmark(Context myContext,  Integer letterId){
        this.myContext = myContext;
        this.letterId = letterId;
    }


    @Override
    public void tryRequest() {
        if (requestQueueMailInform == null){
            requestQueueMailInform = Volley.newRequestQueue(myContext);
        }
        JSONObject requestData1 = makeJsonObject();
        makeJsonRequest(requestData1, makeRequestUrl(letterId),requestQueueMailInform);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        newsData = gson.fromJson(String.valueOf(response), NewsData.class);
        AddOrDelBookmark.isClicked_new = newsData.getBookmarkId();
    }

}

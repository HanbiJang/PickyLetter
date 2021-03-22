package com.makeus.pineapple.server_controllers;

import android.widget.Button;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.makeus.pineapple.HomeMail;
import com.makeus.pineapple.home.data.NewLetter;

import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetLetterInformHomeMail implements GetLetterInformInterface {
    RequestQueue requestQueueMailInform;
    NewLetter newLetter;
    Button btn_bookmark;
    static Integer letterId;

    public GetLetterInformHomeMail(RequestQueue requestQueueMailInform, Integer letterId, Button btn_bookmark){
        this.requestQueueMailInform = requestQueueMailInform;
        this.btn_bookmark = btn_bookmark;
        this.letterId = letterId;
    }


    @Override
    public void tryRequest() {
        JSONObject requestData1 = makeJsonObject();
        makeJsonRequest(requestData1, makeRequestUrl(letterId),requestQueueMailInform);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        newLetter = gson.fromJson(String.valueOf(response), NewLetter.class);
        HomeMail.newLetter = newLetter;
        HomeMail.bookmarkFirstSetting(btn_bookmark);

    }

}

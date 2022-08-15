package com.makeus.pineapple.server_controllers.patch;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.makeus.pineapple.mypage_settings.mypage.data.UserData;

import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class PatchNickname implements PatchNicknameInterface {
    RequestQueue requestQueue;
    UserData userData;
    String nickname;

    public PatchNickname(RequestQueue requestQueue, String nickname) {
        this.requestQueue = requestQueue;
        this.nickname = nickname;
    }

    @Override
    public void tryRequest() {
        JSONObject requestData1 = makeJsonObject();
        Log.e("Patch 시작: ", "tryRequest");
        makeJsonRequest(requestData1, makeRequestUrl(" "), requestQueue);
    }


    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        userData = gson.fromJson(String.valueOf(response), UserData.class);
        Log.e("Post 마무리: ", userData.getNickname() +": 바뀐 닉네임");
    }

    @Override
    public JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        try {
            //데이터 삽입
            requestData.put("nickname", nickname);

        }catch (Exception e){ }
        return requestData;
    }
}

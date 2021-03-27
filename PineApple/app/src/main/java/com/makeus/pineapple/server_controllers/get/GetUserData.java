package com.makeus.pineapple.server_controllers.get;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.makeus.pineapple.mypage_settings.mypage.data.UserData;
import com.makeus.pineapple.mypage_settings.settings.SettingsProfileEdit;
import com.makeus.pineapple.server_controllers.delete.DeleteBookmark;
import com.makeus.pineapple.server_controllers.server_data.NewsData;

import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetUserData implements GetUserDataInterface {
    RequestQueue requestQueueMailInform;
    UserData userData;

    public GetUserData(RequestQueue requestQueueMailInform){
        this.requestQueueMailInform = requestQueueMailInform;
    }

    @Override
    public void tryRequest() {
        JSONObject requestData1 = makeJsonObject();
        Log.e("get요청 시작: GetUserData ", " ");
        makeJsonRequest(requestData1, makeRequestUrl(" "),requestQueueMailInform);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        userData = gson.fromJson(String.valueOf(response), UserData.class);
        Log.e("get요청 마무리: GetUserData", response +" ");

        String nickname = userData.getNickname();
        SettingsProfileEdit.et_nickname.setText(nickname);
    }

}

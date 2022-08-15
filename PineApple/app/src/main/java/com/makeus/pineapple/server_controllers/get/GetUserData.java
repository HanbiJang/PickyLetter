package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.mypage_settings.mypage.data.UserData;
import com.makeus.pineapple.mypage_settings.settings.SettingsProfileEdit;

import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetUserData implements GetUserDataInterface {
    Context myContext;
    RequestQueue requestQueue;
    UserData userData;

    public GetUserData(Context myContext){
        this.myContext = myContext;
    }

    @Override
    public void tryRequest() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

        JSONObject requestData1 = makeJsonObject();
        Log.e("get요청 시작: GetUserData ", " ");
        makeJsonRequest(requestData1, makeRequestUrl(" "), requestQueue);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        userData = gson.fromJson(String.valueOf(response), UserData.class);
        Log.e("get요청 마무리: GetUserData", response +" ");

        String nickname = userData.getNickname();
        MainActivity.setNickName(nickname);
        if(Fragment1_Home.tv_nickname!=null){
            Fragment1_Home.tv_nickname.setText(nickname);
        }
        if(SettingsProfileEdit.et_nickname !=null){
            SettingsProfileEdit.et_nickname.setText(nickname);
        }


    }

}

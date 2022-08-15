package com.makeus.pineapple.server_controllers.delete;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.mypage_settings.mypage.data.UserData;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformData;

import org.json.JSONObject;

public class DeleteUser implements DeleteRequest {
    RequestQueue requestQueue;
    Context myContext;

    public DeleteUser(Context myContext) {
        this.myContext = myContext;
    }

    @Override
    public void tryRequest() {
        Log.e("delete 요청 ", "DeleteSubPlatform ");
        JSONObject requestData1 = makeJsonObject();

        //get 요청 관련
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

        makeJsonRequest(requestData1, makeRequestUrl(" "), requestQueue);
    }


    @Override
    public String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/users";
        return url;
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        UserData userData = gson.fromJson(String.valueOf(response), UserData.class);
        if(userData.getNickname() != null){
            Log.e("0", "유저 삭제 성공:" + userData.getNickname() );
        }
    }

}

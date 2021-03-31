package com.makeus.pineapple.server_controllers.post;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformData;

import org.json.JSONObject;

public class PostAddPlatform implements PostRequest{
    Context myContext;
    RequestQueue requestQueue;
    Integer platformId;

    public PostAddPlatform(Context myContext, Integer platformId){
        this.myContext= myContext;
        this.platformId = platformId;
    }

    @Override
    public void tryRequest() {
        //get 요청 관련
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

        JSONObject requestData1 = makeJsonObject();
        makeJsonRequest(requestData1, makeRequestUrl(" "), requestQueue);
    }


    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        SubscribingPlatformData bookmakrResult = gson.fromJson(String.valueOf(response), SubscribingPlatformData.class);

        if(bookmakrResult.getName() != null){
            Toast.makeText(myContext, bookmakrResult.getName()+ "을(를) 구독하였습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/subscribing-platforms/"+ platformId;
        return url;
    }

}

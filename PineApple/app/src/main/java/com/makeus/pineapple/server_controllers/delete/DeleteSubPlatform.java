package com.makeus.pineapple.server_controllers.delete;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.R;
import com.makeus.pineapple.bookmark.BookmakrResult;
import com.makeus.pineapple.server_controllers.get.GetLetterInformBeforeBookmarkDel;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformData;

import org.json.JSONObject;

public class DeleteSubPlatform implements DeleteRequest {
    RequestQueue requestQueue;
    Context myContext;
    Integer platformId;

    public DeleteSubPlatform( Context myContext, Integer platformId) {
        this.myContext = myContext;
        this.platformId = platformId;
    }

    @Override
    public void tryRequest() {
        Log.e("북마크 삭제 bookmarkId값", "DeleteSubPlatform ");
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
        url = "http://3.13.65.158/v1/subscribing-platforms/" + platformId;
        return url;
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        SubscribingPlatformData subscribingPlatformData = gson.fromJson(String.valueOf(response), SubscribingPlatformData.class);
        if(subscribingPlatformData.getName() != null){
            Log.e("0", "플랫폼 삭제 성공:" + subscribingPlatformData.getName() );
            Toast.makeText(myContext, "구독해지하였어요.", Toast.LENGTH_SHORT).show();
        }

    }


}

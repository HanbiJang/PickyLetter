package com.makeus.pineapple.server_controllers.delete;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.R;
import com.makeus.pineapple.bookmark.BookmakrResult;
import com.makeus.pineapple.server_controllers.get.GetLetterInformBeforeBookmarkDel;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.makeus.pineapple.main.MainActivity.getToken;

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

    //기본으로 get요청 하도록 만듦
    public void makeJsonRequest(JSONObject requestData, String requestUrl, RequestQueue requestQueue) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                requestUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //후처리 코드
                        processResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("에러 발생", error.getMessage());
                        //에러 발생 코드

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return super.getParams();
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");

                //변경부분********************
                SharedPreferences sharedPreferences= myContext.getSharedPreferences("user", Context.MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
                String token = sharedPreferences.getString("token","");
                headers.put("x-access-token", token );

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        request.setShouldCache(false);
        requestQueue.add(request);

    }


}

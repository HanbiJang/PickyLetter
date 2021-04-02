package com.makeus.pineapple.server_controllers.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.makeus.pineapple.main.MainActivity.getToken;

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

    //기본으로 get요청 하도록 만듦
    public void makeJsonRequest(JSONObject requestData, String requestUrl, RequestQueue requestQueue) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
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
                        Log.e("Post 에러 발생", error.getMessage());
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

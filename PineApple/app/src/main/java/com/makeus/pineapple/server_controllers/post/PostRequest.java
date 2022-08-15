package com.makeus.pineapple.server_controllers.post;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.makeus.pineapple.main.MainActivity.getToken;

public interface PostRequest {
    void tryRequest(); //리퀘스트 시작 작업
    void processResponse(JSONObject response); //response 받은 후, 후처리 작업
    String makeRequestUrl(Object data);

    //제이슨 오브젝트(서버로 보내는) 생성 작업
    default JSONObject makeJsonObject(){
        JSONObject requestData = new JSONObject();
        try {
            //데이터 삽입

        }catch (Exception e){ }
        return requestData;
    }

    //기본으로 get요청 하도록 만듦
    default void makeJsonRequest(JSONObject requestData, String requestUrl, RequestQueue requestQueue) {

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
                headers.put("x-access-token", getToken());

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

package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.FirstEditLetter;
import com.makeus.pineapple.mypage_settings.settings.EditLetterAdapter;
import com.makeus.pineapple.mypage_settings.settings.EditLetterViewCode;
import com.makeus.pineapple.mypage_settings.settings.EditedLetter;
import com.makeus.pineapple.mypage_settings.settings.SettingsEditLetter;
import com.makeus.pineapple.server_controllers.server_data.AllPlatformResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.makeus.pineapple.main.MainActivity.getToken;

//레터 정보 불러오는 클래스
public class GetAllPlatform_first implements GetRequestInterface {
    Context myContext;
    RequestQueue requestQueue;
    AllPlatformResult subscribingPlatformResult;


    public GetAllPlatform_first(Context myContext){

        this.myContext = myContext;
    }


    @Override
    public JSONObject makeJsonObject() {
        //get 요청 관련
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

        JSONObject requestData = new JSONObject();
        return requestData;
    }

    @Override
    public String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/platforms" ;
        return url;
    }


    @Override
    public void tryRequest() {
        JSONObject requestData1 = makeJsonObject();
        Log.e("get요청 시작:", " GetAllPlatform");
        makeJsonRequest(requestData1, makeRequestUrl(" "), requestQueue);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        subscribingPlatformResult = gson.fromJson(String.valueOf(response), AllPlatformResult.class);
        Log.e("요청 마무리:GetAllPlatform", response +" ");
        setToRv(); //플랫폼 정보 리사이클러뷰에 넣음
    }

    public void setToRv(){
        EditLetterAdapter editLetterAdapter = new EditLetterAdapter();

        for (int i=0; i<subscribingPlatformResult.getResultList().size(); i++){

            if(i == subscribingPlatformResult.getResultList().size()-1){ // 끝
                editLetterAdapter.addItem(new EditedLetter(EditLetterViewCode.VIEW_EDIT_LETTER_END,
                        subscribingPlatformResult.getResultList().get(i).getImageUrl(),
                        subscribingPlatformResult.getResultList().get(i).getName(),
                        subscribingPlatformResult.getResultList().get(i).getPlatformId(),
                        subscribingPlatformResult.getResultList().get(i).getSubscribing()
                ));
            }
            else{
                editLetterAdapter.addItem(new EditedLetter(EditLetterViewCode.VIEW_EDIT_LETTER_MIDDLE, // 중간
                        subscribingPlatformResult.getResultList().get(i).getImageUrl(),
                        subscribingPlatformResult.getResultList().get(i).getName(),
                        subscribingPlatformResult.getResultList().get(i).getPlatformId(),
                        subscribingPlatformResult.getResultList().get(i).getSubscribing()
                ));
            }
        }

        FirstEditLetter.rv_edit_letter.setAdapter(editLetterAdapter);

    }

    //기본으로 get요청 하도록 만듦
    @Override
    public void makeJsonRequest(JSONObject requestData, String requestUrl, RequestQueue requestQueue) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
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
                        //에러 발생 코드
                        Log.e("에러 발생" , "  ");

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

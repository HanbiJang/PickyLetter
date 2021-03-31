package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.mypage_settings.settings.EditLetterAdapter;
import com.makeus.pineapple.mypage_settings.settings.EditLetterViewCode;
import com.makeus.pineapple.mypage_settings.settings.EditedLetter;
import com.makeus.pineapple.mypage_settings.settings.SettingsEditLetter;
import com.makeus.pineapple.server_controllers.server_data.AllPlatformResult;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformResult;

import org.json.JSONException;
import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetAllPlatform implements GetRequestInterface {
    Context myContext;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    AllPlatformResult subscribingPlatformResult;


    public GetAllPlatform(Context myContext){
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

        SettingsEditLetter.rv_edit_letter.setAdapter(editLetterAdapter);

    }



}

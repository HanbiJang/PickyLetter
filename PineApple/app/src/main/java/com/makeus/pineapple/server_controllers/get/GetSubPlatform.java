package com.makeus.pineapple.server_controllers.get;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage_settings.mypage.data.UserData;
import com.makeus.pineapple.mypage_settings.settings.EditLetterAdapter;
import com.makeus.pineapple.mypage_settings.settings.EditLetterViewCode;
import com.makeus.pineapple.mypage_settings.settings.EditedLetter;
import com.makeus.pineapple.mypage_settings.settings.SettingsEditLetter;
import com.makeus.pineapple.mypage_settings.settings.SettingsProfileEdit;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformData;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformResult;

import org.json.JSONException;
import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetSubPlatform implements GetRequestInterface {
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    SubscribingPlatformResult subscribingPlatformResult;
    Integer userId;


    public GetSubPlatform(RequestQueue requestQueueMailInform, Integer userId){
        this.requestQueue = requestQueueMailInform;
        this.userId = userId;
    }

    @Override
    public JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestData;
    }

    @Override
    public String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/users/" +userId+"/subscribing-platforms";
        return url;
    }


    @Override
    public void tryRequest() {



        JSONObject requestData1 = makeJsonObject();
        Log.e("get요청 시작:", " GetSubPlatform");
        makeJsonRequest(requestData1, makeRequestUrl(" "), requestQueue);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        subscribingPlatformResult = gson.fromJson(String.valueOf(response), SubscribingPlatformResult.class);
        Log.e("get요청 마무리: GetUserData", response +" ");
        setToRv(); //플랫폼 정보 리사이클러뷰에 넣음
    }

    public void setToRv(){
        EditLetterAdapter editLetterAdapter = new EditLetterAdapter();

        for (int i=0; i<subscribingPlatformResult.getResultList().size(); i++){

            if(i == subscribingPlatformResult.getResultList().size()-1){ // 끝
                editLetterAdapter.addItem(new EditedLetter(EditLetterViewCode.VIEW_EDIT_LETTER_END,
                        subscribingPlatformResult.getResultList().get(i).getImageUrl(),
                        subscribingPlatformResult.getResultList().get(i).getName(),
                        subscribingPlatformResult.getResultList().get(i).getPlatformId()
                ));
            }
            else{
                editLetterAdapter.addItem(new EditedLetter(EditLetterViewCode.VIEW_EDIT_LETTER_MIDDLE, // 중간
                        subscribingPlatformResult.getResultList().get(i).getImageUrl(),
                        subscribingPlatformResult.getResultList().get(i).getName(),
                        subscribingPlatformResult.getResultList().get(i).getPlatformId()
                ));
            }
        }

        SettingsEditLetter.rv_edit_letter.setAdapter(editLetterAdapter);

    }



}

package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.home.filters.FilterBrand;
import com.makeus.pineapple.home.filters.FilterBrandAdapter;
import com.makeus.pineapple.home.filters.PopupFilter;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.mypage_settings.settings.EditLetterAdapter;
import com.makeus.pineapple.mypage_settings.settings.EditLetterViewCode;
import com.makeus.pineapple.mypage_settings.settings.EditedLetter;
import com.makeus.pineapple.mypage_settings.settings.SettingsEditLetter;
import com.makeus.pineapple.server_controllers.server_data.SubscribingPlatformResult;

import org.json.JSONException;
import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetSubPlatformFilter implements GetRequestInterface {
    RequestQueue requestQueue;
    SubscribingPlatformResult subscribingPlatformResult;

    Context myContext;


    public GetSubPlatformFilter(Context myContext) {
        this.myContext = myContext;
    }

    @Override
    public JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        return requestData;
    }

    @Override
    public String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/users/" + MainActivity.getUserId() + "/subscribing-platforms";
        return url;
    }


    @Override
    public void tryRequest() {

        //get 요청 관련
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

        JSONObject requestData1 = makeJsonObject();
        Log.e("get요청 시작:", " GetSubPlatform");
        makeJsonRequest(requestData1, makeRequestUrl(" "), requestQueue);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        subscribingPlatformResult = gson.fromJson(String.valueOf(response), SubscribingPlatformResult.class);
        Log.e("get요청 마무리: GetUserData", response + " ");
        setToRv(); //플랫폼 정보 리사이클러뷰에 넣음
    }

    public void setToRv() {
        FilterBrandAdapter filterBrandAdapter = new FilterBrandAdapter();

        for (int i = 0; i < subscribingPlatformResult.getResultList().size(); i++) {
            filterBrandAdapter.addItem( new FilterBrand(subscribingPlatformResult.getResultList().get(i).getName()));
        }

        PopupFilter.rv_filter_brand.setAdapter(filterBrandAdapter);

    }


}

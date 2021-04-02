package com.makeus.pineapple.server_controllers.post;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.R;
import com.makeus.pineapple.bookmark.BookmakrResult;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.mypage_settings.mypage.Fragment3_MyPage;

import org.json.JSONObject;

public class PostBookmarkAdd implements PostBookmarkAddInterface{
    Context context;
    Button btn_bookmark;
    RequestQueue requestQueueBookmarkAdd;
    Integer letterId;

    public PostBookmarkAdd(Context context, Integer letterId, Button btn_bookmark){
        this.context = context;
        this.btn_bookmark = btn_bookmark;
        this.letterId = letterId;
    }

    @Override
    public void tryRequest() {
        if (requestQueueBookmarkAdd == null) {
            requestQueueBookmarkAdd = Volley.newRequestQueue(context); // 큐 객체 생성하기
        }
        JSONObject requestData1 = makeJsonObject();
        makeJsonRequest(requestData1, makeRequestUrl(letterId), requestQueueBookmarkAdd);
    }

    @Override
    public JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        try {
            Log.e("넣는 레터 아이디",  letterId +"");
            requestData.put("letterId", letterId);

        } catch (Exception e) {

        }
        return requestData;
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        BookmakrResult bookmakrResult = gson.fromJson(String.valueOf(response), BookmakrResult.class);
        Log.d("북마크 동작: ", String.valueOf(bookmakrResult.isResult()));
        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill); //이미지 바꿈
        Toast.makeText(context, "북마크를 등록했어요", Toast.LENGTH_SHORT).show();

/*        //마이페이지 새로고침
        if(MainActivity.fragment3_mypage !=null){
            //서버통신
            Fragment3_MyPage.lastLetterId = 0; //마이페이지 북마크 조회 기능 초기화
            Fragment3_MyPage.page = -1;
            Fragment3_MyPage.pageLimit = 0;

            Fragment3_MyPage.setUserData();
            Fragment3_MyPage.setBookmarkRv(Fragment3_MyPage.view);
        }*/

        //페이지 새로고침
        MainActivity.fragment3_mypage = null;

    }

}

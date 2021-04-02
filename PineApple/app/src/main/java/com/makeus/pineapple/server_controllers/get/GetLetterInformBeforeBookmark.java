/*
package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.bookmark.AddOrDelBookmark;
import com.makeus.pineapple.server_controllers.delete.DeleteBookmark;
import com.makeus.pineapple.server_controllers.post.PostBookmarkAdd;
import com.makeus.pineapple.server_controllers.server_data.NewsData;

import org.json.JSONObject;

//레터 정보 불러오는 클래스
public class GetLetterInformBeforeBookmark implements GetLetterInformInterface {
    RequestQueue requestQueueMailInform;
    NewsData newLetter;
    Button btn_bookmark;
    Integer letterId;
    Context context;

    public GetLetterInformBeforeBookmark(Context context, Integer letterId, Button btn_bookmark){
        this.context = context;
        this.btn_bookmark = btn_bookmark;
        this.letterId = letterId;
    }


    @Override
    public void tryRequest() {
        JSONObject requestData1 = makeJsonObject();
        Log.e("get요청 시작: 북마크값가져옴", DeleteBookmark.bookmarkId +"");
        makeJsonRequest(requestData1, makeRequestUrl(letterId),requestQueueMailInform);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        newLetter = gson.fromJson(String.valueOf(response), NewsData.class);
        DeleteBookmark.bookmarkId = newLetter.getBookmarkId();
        Log.e("get요청 마무리: 북마크값가져옴", DeleteBookmark.bookmarkId +"");


        //---------------------------------------------------------------------------
        PostBookmarkAdd postBookmarkAdd;
        DeleteBookmark deleteBookmark;

        if (AddOrDelBookmark.bookmarkId == 0) { //0
            //북마크 등록 요청
            Log.e("북마크 추가 시작",letterId+ "레터아이디값");
            postBookmarkAdd = new PostBookmarkAdd(context,letterId, btn_bookmark);
            postBookmarkAdd.tryRequest();
        }
        else if(AddOrDelBookmark.bookmarkId  > 0) { //양수
            //북마크 해제 요청

            Log.e("북마크 삭제시작", "  ");
            deleteBookmark = new DeleteBookmark(context,letterId,btn_bookmark);
            deleteBookmark.tryRequest();
        }

    }

}
*/

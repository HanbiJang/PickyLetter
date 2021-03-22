package com.makeus.pineapple.bookmark;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.makeus.pineapple.server_controllers.DeleteBookmark;
import com.makeus.pineapple.server_controllers.PostBookmarkAdd;

public class AddOrDelBookmark {

    Button btn_bookmark;
    Integer letterId;
    Context myContext;
    RequestQueue requestQueueBookmarkAdd,  requestQueueBookmarkDel,  requestQueueGetLetterInform;

    public AddOrDelBookmark(Button btn_bookmark,
                            Integer letterId,
                            Context myContext,
                            RequestQueue requestQueueBookmarkAdd,
                            RequestQueue requestQueueBookmarkDel,
                            RequestQueue requestQueueGetLetterInform) {
        this.btn_bookmark = btn_bookmark;
        this.letterId = letterId;
        this.myContext = myContext;
        this.requestQueueBookmarkAdd = requestQueueBookmarkAdd;
        this.requestQueueBookmarkDel = requestQueueBookmarkDel;
        this.requestQueueGetLetterInform = requestQueueGetLetterInform;
    }

    public void setBtnFunc(Integer isClicked) {
        PostBookmarkAdd postBookmarkAdd;
        DeleteBookmark deleteBookmark;

        Log.e("북마크전환 ", isClicked.toString());
        if (isClicked <= 0) {
            //북마크 등록 요청
            Log.e("북마크 등록 요청", "시작");
            if (requestQueueBookmarkAdd == null) {
                requestQueueBookmarkAdd = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
            }
            Log.e("북마크 추가 시작",letterId+ "레터아이디값");
            postBookmarkAdd = new PostBookmarkAdd(requestQueueBookmarkAdd, letterId, btn_bookmark);
            postBookmarkAdd.tryRequest();
        } else {
            //북마크 해제 요청
            Log.e("북마크 해제 요청", "시작");
            if (requestQueueBookmarkDel == null) {
                requestQueueBookmarkDel = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
            }
            if (requestQueueGetLetterInform == null){
                requestQueueGetLetterInform = Volley.newRequestQueue(myContext);
            }
            deleteBookmark = new DeleteBookmark(requestQueueBookmarkDel,requestQueueGetLetterInform, letterId,btn_bookmark);
            deleteBookmark.tryRequest();
        }
    }

}

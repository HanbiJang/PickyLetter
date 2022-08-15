package com.makeus.pineapple.bookmark;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.makeus.pineapple.R;
import com.makeus.pineapple.server_controllers.delete.DeleteBookmark;
import com.makeus.pineapple.server_controllers.get.GetLetterInformBeforeBookmark;
import com.makeus.pineapple.server_controllers.post.PostBookmarkAdd;

import java.security.cert.Extension;

public class AddOrDelBookmark {
    public static Integer isClicked_new;
    Button btn_bookmark;
    Integer letterId;
    Context myContext;
    RequestQueue requestQueueBookmarkAdd,  requestQueueBookmarkDel,  requestQueueGetLetterInform;
    PostBookmarkAdd postBookmarkAdd;
    DeleteBookmark deleteBookmark;

    public AddOrDelBookmark(Button btn_bookmark,
                            Integer letterId,
                            Context myContext,
                            RequestQueue requestQueueBookmarkAdd,
                            RequestQueue requestQueueBookmarkDel,
                            RequestQueue requestQueueGetLetterInform)
    {
        this.btn_bookmark = btn_bookmark;
        this.letterId = letterId;
        this.myContext = myContext;
        this.requestQueueBookmarkAdd = requestQueueBookmarkAdd;
        this.requestQueueBookmarkDel = requestQueueBookmarkDel;
        this.requestQueueGetLetterInform = requestQueueGetLetterInform;
    }

    public void setBtnFunc(Integer isClicked)
    {
        //북마크 버튼 비활성화 시키기
        btn_bookmark.setEnabled(false);

        //북마크 정보 가져오기
        GetLetterInformBeforeBookmark beforeBookmark = new GetLetterInformBeforeBookmark(
                myContext,
                letterId
        );
        beforeBookmark.tryRequest();


        android.os.Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("북마크전환 ", isClicked.toString());
                if (isClicked_new <= 0) {
                    try
                    {
                        //북마크 등록 요청
                        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill);
                        Log.e("북마크 등록 요청", "시작");
                        if (requestQueueBookmarkAdd == null) {
                            requestQueueBookmarkAdd = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
                        }
                        Log.e("북마크 추가 시작",letterId+ "레터아이디값");
                        postBookmarkAdd = new PostBookmarkAdd(myContext,letterId, btn_bookmark);
                        postBookmarkAdd.tryRequest();
                    }catch (Exception e){
                        Toast.makeText(myContext, "북마크 오류 발생", Toast.LENGTH_SHORT).show();
                    }


                } else {
                        try {
                            //북마크 해제 요청
                            btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_line);
                            Log.e("북마크 해제 요청", "시작");
                            if (requestQueueBookmarkDel == null) {
                                requestQueueBookmarkDel = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
                            }
                            if (requestQueueGetLetterInform == null){
                                requestQueueGetLetterInform = Volley.newRequestQueue(myContext);
                            }
                            deleteBookmark = new DeleteBookmark(myContext,letterId,btn_bookmark);
                            deleteBookmark.tryRequest();
                        }catch (Exception e){
                            Toast.makeText(myContext, "북마크 오류 발생", Toast.LENGTH_SHORT).show();
                        }


                    }
                }

        },600);

    }

}
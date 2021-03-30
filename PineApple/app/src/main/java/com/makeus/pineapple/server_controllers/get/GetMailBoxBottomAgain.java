package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.home.adapters.HomeAdapters;
import com.makeus.pineapple.home.adapters.OldLetterAdapter;
import com.makeus.pineapple.home.data.HomeLetters;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;
import com.makeus.pineapple.server_controllers.server_data.NewsData;
import com.makeus.pineapple.server_controllers.server_data.NewsResult;

import org.json.JSONObject;

import java.util.ArrayList;

//레터 정보 불러오는 클래스
public class GetMailBoxBottomAgain implements GetMailboxInterface {
    Context myContext;
    RecyclerView rv_bottom;
    RequestQueue requestQueueTop;
    MailboxRequestData mailboxRequestData;
    NewsResult newsResult;
    public ArrayList<HomeLetters> homeLettersArrayList;
    HomeAdapters homeAdapters;

    boolean startRequest = true ;

    public GetMailBoxBottomAgain(Context myContext, RecyclerView rv_bottom, HomeAdapters homeAdapters) {
        this.myContext = myContext;
        this.rv_bottom = rv_bottom;
        this.homeAdapters = homeAdapters;

        homeLettersArrayList = new ArrayList<>(); //한번만 생성
    }

    @Override
    public void tryRequest() {

        if (requestQueueTop == null) {
            requestQueueTop = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

        Fragment1_Home.pageBottom += 1;

        Log.e(" 값 ", Fragment1_Home.pageBottom + "    "+   Fragment1_Home.pageLimitBottom);

        mailboxRequestData = new MailboxRequestData(
                MainActivity.getUserId(),
                Fragment1_Home.pageBottom
        );

        JSONObject requestData = makeJsonObject();
        Log.e("get 재요청시작: mailbox 가져오기", " bottom ");
        startRequest = false;
        makeJsonRequest(requestData, makeRequestUrl(mailboxRequestData), requestQueueTop);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        newsResult = gson.fromJson(String.valueOf(response), NewsResult.class);

        setToAdapter(); //어답터에 데이터 설정

        Log.e(" 사이즈 값", "mailBox 재요청" + newsResult.getNewNewsResultList().size() +"");
        startRequest = true;

        if (newsResult.getNewNewsResultList().size() != 0 && startRequest == true && Fragment1_Home.pageBottom <= Fragment1_Home.pageLimitBottom) {
            tryRequest();
        }


    }


    public void setToAdapter() {
        //로딩뷰 없애기
        //프로그레스바 보여주기

        Fragment1_Home.oldLetterAdapter.removeItems(Fragment1_Home.oldLetterAdapter.getItems().size() - 1);
        Fragment1_Home.oldLetterAdapter.notifyItemRemoved(Fragment1_Home.oldLetterAdapter.getItems().size());


        for (int i = 0; i < newsResult.getNewNewsResultList().size(); i++) {
            HomeLetters homeLetters;
            homeLetters = new NewsData(
                    newsResult.getNewNewsResultList().get(i).getLetterId(),
                    newsResult.getNewNewsResultList().get(i).getPlatformId(),
                    newsResult.getNewNewsResultList().get(i).getBookmarkId(),
                    newsResult.getNewNewsResultList().get(i).getBookmarkCount(),
                    newsResult.getNewNewsResultList().get(i).getPlatformName(),
                    newsResult.getNewNewsResultList().get(i).getPlatformImageUrl(),
                    newsResult.getNewNewsResultList().get(i).getTitle(),
                    newsResult.getNewNewsResultList().get(i).getContent(),
                    newsResult.getNewNewsResultList().get(i).getThumbnailImageUrl(),
                    newsResult.getNewNewsResultList().get(i).getCreatedAt(),
                    newsResult.getNewNewsResultList().get(i).getModifiedAt()
            );

            if(i == newsResult.getNewNewsResultList().size()-1 ){
                Fragment1_Home.oldLetterAdapter.addItem(null);
            }
            else{
                Fragment1_Home.oldLetterAdapter.addItem(homeLetters);
            }

        }

        Fragment1_Home.oldLetterAdapter.notifyItemInserted(Fragment1_Home.oldLetterAdapter.getItems().size());

        OldLetterAdapter.LoadingViewHolder.progressBar.setVisibility(View.GONE);
        OldLetterAdapter.LoadingViewHolder.btn_more.setVisibility(View.VISIBLE);

    }


}

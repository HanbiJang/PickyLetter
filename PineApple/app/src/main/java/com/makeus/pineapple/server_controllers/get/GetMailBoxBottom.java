package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.home.adapters.HomeAdapters;
import com.makeus.pineapple.home.adapters.NewLetterAdapter;
import com.makeus.pineapple.home.adapters.OldLetterAdapter;
import com.makeus.pineapple.home.data.HomeLetters;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;
import com.makeus.pineapple.server_controllers.server_data.NewsData;
import com.makeus.pineapple.server_controllers.server_data.NewsResult;

import org.json.JSONObject;

import java.util.ArrayList;

//레터 정보 불러오는 클래스
public class GetMailBoxBottom implements GetMailboxInterface {
    Context myContext;
    RecyclerView rv_bottom;
    RequestQueue requestQueueTop;
    MailboxRequestData mailboxRequestData;
    NewsResult newsResult;
    public ArrayList<HomeLetters> homeLettersArrayList;
    HomeAdapters homeAdapters;

    boolean startRequest = true ;


    public GetMailBoxBottom(Context myContext, RecyclerView rv_bottom, HomeAdapters homeAdapters) {
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

        Log.e(" ", " 값"+ Fragment1_Home.pageBottom +   "   "+ Fragment1_Home.pageLimitBottom);

        Fragment1_Home.pageBottom += 1;

        mailboxRequestData = new MailboxRequestData(
                MainActivity.getUserId(),
                Fragment1_Home.pageBottom
        );

        JSONObject requestData = makeJsonObject();
        Log.e("get요청 시작: mailbox 가져오기", " bottom ");
        startRequest = false;
        makeJsonRequest(requestData, makeRequestUrl(mailboxRequestData), requestQueueTop);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        newsResult = gson.fromJson(String.valueOf(response), NewsResult.class);

        setLoadingView(); //로딩뷰 변수 설정
        setToAdapter(); //어답터에 데이터 설정

        Log.e(" 사이즈 값", newsResult.getNewNewsResultList().size() +"");
        startRequest = true;

        if (newsResult.getNewNewsResultList().size() != 0 && startRequest == true && Fragment1_Home.pageBottom <= Fragment1_Home.pageLimitBottom) {
            tryRequest();
        }


        if (Fragment1_Home.isLoadingBottomRv == false) {
            setBottomRv();
            Fragment1_Home.setLoadingPopupOld = true;
        }

        Fragment1_Home.sr_layout.setRefreshing(false); //새로고침 멈춤


    }

    public void setLoadingView() {
        if (newsResult.getNewNewsResultList().size() == 0) {
            Fragment1_Home.setLoadingPopupOld = true;
            Fragment1_Home.isLoadingBottomRv = true;
        }
    }

    public void setToAdapter() {
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

            homeLettersArrayList.add(homeLetters);
        }
    }

    //


    private void setBottomRv(){
        Log.e("1","setNewLetterListToRv 설정 함수 부름");
        homeAdapters.removeAll();
        if (homeLettersArrayList.size() != 0) {
            for (int i = 0; i < homeLettersArrayList.size(); i++) {
                homeAdapters.addItem(homeLettersArrayList.get(i));
            }
        }

        // 하단 RV : 어답터 설정, 더보기 설정
        rv_bottom.setAdapter((OldLetterAdapter) homeAdapters);
        homeAdapters.addItem(null); //로딩뷰 넣기
        homeAdapters.notifyItemInserted(homeAdapters.getItems().size() - 1);
    }





}

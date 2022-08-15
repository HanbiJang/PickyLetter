package com.makeus.pineapple.server_controllers.get;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.makeus.pineapple.server_controllers.server_data.NewsData;
import com.makeus.pineapple.server_controllers.server_data.NewsResult;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;

import org.json.JSONObject;

import java.util.ArrayList;

//레터 정보 불러오는 클래스
public class GetMailBoxTop implements GetMailboxInterface {
    Context myContext;
    RecyclerView rv_top;
    RequestQueue requestQueueTop;
    MailboxRequestData mailboxRequestData;
    NewsResult newsResult;
    public ArrayList<HomeLetters> homeLettersArrayList;
    HomeAdapters homeAdapters;
    //
    boolean startRequest = true;
    boolean isContinue = false;

    public GetMailBoxTop(Context myContext, RecyclerView rv_top, HomeAdapters homeAdapters) {
        this.myContext = myContext;
        this.rv_top = rv_top;
        this.homeAdapters = homeAdapters;

        homeLettersArrayList = new ArrayList<>(); //한번만 생성
    }

    @Override
    public void tryRequest() {

        if (requestQueueTop == null) {
            requestQueueTop = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }

        Fragment1_Home.pageTop += 1;

        mailboxRequestData = new MailboxRequestData(
                MainActivity.getUserId(),
                Fragment1_Home.pageTop,
                -7
        );

        JSONObject requestData = makeJsonObject();
        Log.e("get요청 시작: mailbox 가져오기", " top ");
        startRequest = false;
        makeJsonRequest(requestData, makeRequestUrl(mailboxRequestData), requestQueueTop);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        newsResult = gson.fromJson(String.valueOf(response), NewsResult.class);

        setLoadingView(); //로딩뷰 변수 설정
        setToAdapter(); //어답터에 데이터 설정

        Log.e(" 사이즈 값", newsResult.getNewNewsResultList().size() + "");
/*        startRequest = true;
        if (newsResult.getNewNewsResultList().size() != 0 && startRequest == true) {
            tryRequest();
        }*/

        if (Fragment1_Home.isLoadingTopRv == false) {
            setTopRv();
            Fragment1_Home.setLoadingPopupNew = true;
        }

        Fragment1_Home.sr_layout.setRefreshing(false); //새로고침 멈춤
        MainActivity.setOneTimeEmpty(true);

    }

    public void setLoadingView() {
        if (newsResult.getNewNewsResultList().size() == 0) {
            Fragment1_Home.setLoadingPopupNew = true;
            Fragment1_Home.isLoadingTopRv = true;
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
            Fragment1_Home.newLetterAdapter.addItem(homeLetters);
        }
        Fragment1_Home.newLetterAdapter.notifyItemInserted(Fragment1_Home.newLetterAdapter.getItems().size());
    }

    //

    private void setTopRv() {
        Log.e("1", "setNewLetterListToRv 설정 함수 부름");
//        homeAdapters.removeAll();
        if (homeLettersArrayList.size() == 0) {

            if (MainActivity.getOneTimeEmpty() == false) {
                Fragment1_Home.showEmptyImg();
            }
        }/* else {
            for (int i = 0; i < homeLettersArrayList.size(); i++) {
                homeAdapters.addItem(homeLettersArrayList.get(i));
            }*/

            // 상단 RV : 어답터 설정, 더보기 설정
            Fragment1_Home.showNomalRv();
            rv_top.setAdapter((NewLetterAdapter) homeAdapters);

/*            homeAdapters.addItem(null); //로딩뷰 넣기
            homeAdapters.notifyItemInserted(homeAdapters.getItems().size() - 1);*/

            initScrollListener(rv_top, homeAdapters);
        }
//    }


    private void initScrollListener(RecyclerView rv_home, HomeAdapters homeAdapters) {
        rv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!Fragment1_Home.isLoadingTopRv) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == homeAdapters.getItemCount()) {//bottom of list!
                        Log.e("보이는 위치", linearLayoutManager.findLastCompletelyVisibleItemPosition() + "");
                        loadMoreTopRv(homeAdapters); //데이터를 더 로딩하기
                        Fragment1_Home.isLoadingTopRv = true;
                    }
                }

            }
        });

    }

    private void loadMoreTopRv(HomeAdapters homeAdapters) {

        if (isContinue == false) {
            homeAdapters.addItem(null); //로딩뷰 넣기
            homeAdapters.notifyItemInserted(homeAdapters.getItems().size() - 1);
            isContinue = true;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() { //0.5초간 실행
            @Override
            public void run() {
                if (isContinue == true) {

                    //get요청
                    GetMailBoxTopAgain getMailBoxTopAgain = new GetMailBoxTopAgain(
                            myContext,
                            Fragment1_Home.rv_newletter,
                            homeAdapters
                    );

                    getMailBoxTopAgain.tryRequest();
                    Fragment1_Home.isLoadingTopRv = false;
                }


            }
        }, 500);
    }


}

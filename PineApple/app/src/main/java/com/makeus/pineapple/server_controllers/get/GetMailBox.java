package com.makeus.pineapple.server_controllers.get;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
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
public class GetMailBox implements GetMailboxInterface {
    RecyclerView rv_top;
    RecyclerView rv_bottom;
    RequestQueue requestQueueMailInform;
    MailboxRequestData mailboxRequestData;
    NewsResult newsResult;
    public ArrayList<HomeLetters> homeLettersArrayList;
    HomeAdapters homeAdapters;
    Integer pageTopRv =0, pageBottomRv =0;

    public GetMailBox(RecyclerView rv_top,RecyclerView rv_bottom, RequestQueue requestQueueMailInform, MailboxRequestData mailboxRequestData, HomeAdapters homeAdapters) {
        this.rv_top = rv_top;
        this.rv_bottom = rv_bottom;
        this.requestQueueMailInform = requestQueueMailInform;
        this.mailboxRequestData = mailboxRequestData;
        this.homeAdapters = homeAdapters;
    }

    @Override
    public void tryRequest() {
        JSONObject requestData = makeJsonObject();
        Log.e("get요청 시작: mailbox 가져오기", " ");
        makeJsonRequest(requestData, makeRequestUrl(mailboxRequestData), requestQueueMailInform);
    }

    @Override
    public void processResponse(JSONObject response) {
        Gson gson = new Gson();
        newsResult = gson.fromJson(String.valueOf(response), NewsResult.class);
        Log.e("get요청 끝: mailbox 가져오기", " ");

        setLoadingView(homeAdapters); //로딩뷰 변수 설정
        setToAdapter(homeAdapters); //어답터에 데이터 설정

/*        while(newsResult.getNewNewsResultList().size() != 0){
            Log.e("1",newsResult.getNewNewsResultList().size()+"");
            Log.e("1 - 내용",newsResult.getNewNewsResultList().get(newsResult.getNewNewsResultList().size()-1).getTitle()+"");
            if (homeAdapters instanceof NewLetterAdapter) {
                pageTopRv += 1;
                mailboxRequestData.setPage(pageTopRv);
                GetMailBox getMailBox = new GetMailBox(
                        rv_top,
                        rv_bottom,
                        requestQueueMailInform,
                        new MailboxRequestData(mailboxRequestData.getUserId(), pageTopRv, -7 ),
                        homeAdapters
                );
                getMailBox.tryRequest();
            }
            else {
                pageBottomRv += 1;
                mailboxRequestData.setPage(pageBottomRv);
                GetMailBox getMailBox = new GetMailBox(
                        rv_top,
                        rv_bottom,
                        requestQueueMailInform,
                        new MailboxRequestData(mailboxRequestData.getUserId(), pageBottomRv),
                        homeAdapters
                );
                getMailBox.tryRequest();
            }


        }

        if(Fragment1_Home.isLoadingTopRv == false && homeAdapters instanceof NewLetterAdapter){
            setTopRv();
            Fragment1_Home.setLoadingPopupNew = true;
        }
        else if (Fragment1_Home.isLoadingBottomRv == false && homeAdapters instanceof OldLetterAdapter){
            setBottomRv();
            Fragment1_Home.setLoadingPopupOld = true;
        }

        Fragment1_Home.sr_layout.setRefreshing(false); //새로고침 멈춤
        MainActivity.setOneTimeEmpty(true);*/

    }

    public void setLoadingView(HomeAdapters homeAdapters) {
        if (homeAdapters instanceof NewLetterAdapter /*&& newsResult.getNewNewsResultList().size() == 0*/) {
            Fragment1_Home.setLoadingPopupNew = true;
            Fragment1_Home.isLoadingTopRv = true;
        } else if (homeAdapters instanceof OldLetterAdapter /*&& newsResult.getNewNewsResultList().size() == 0*/) {
            Fragment1_Home.setLoadingPopupOld = true;
            Fragment1_Home.isLoadingBottomRv = true; // 반복 요청 그만하기
        }
    }

    public void setToAdapter(HomeAdapters homeAdapters) {
        homeLettersArrayList = new ArrayList<>();

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

            Log.e("2", "타이틀: : "+newsResult.getNewNewsResultList().get(i).getTitle()+ "");
            Log.e("2", "북마크아이디: "+newsResult.getNewNewsResultList().get(i).getBookmarkId()+ "");

            homeLettersArrayList.add(homeLetters);
        }
    }

    //

    private void setTopRv(){
        Log.e("1","setNewLetterListToRv 설정 함수 부름");
        if (homeLettersArrayList.size() == 0) {

            if (MainActivity.getOneTimeEmpty() == false) {
                Fragment1_Home.showEmptyImg();
            }
        } else {
            for (int i = 0; i < homeLettersArrayList.size(); i++) {
                homeAdapters.addItem(homeLettersArrayList.get(i));
            }

            // 상단 RV : 어답터 설정, 무한 스크롤 설정
            Fragment1_Home.showNomalRv();
            rv_top.setAdapter((NewLetterAdapter) homeAdapters);
//            initScrollListener(rv_top, homeAdapters);
        }
    }

    private void setBottomRv(){
        Log.e("1","setNewLetterListToRv 설정 함수 부름");
        if (homeLettersArrayList.size() != 0) {
            for (int i = 0; i < homeLettersArrayList.size(); i++) {
                homeAdapters.addItem(homeLettersArrayList.get(i));
            }
        }

        // 하단 RV : 어답터 설정, 무한 스크롤 설정
        rv_bottom.setAdapter((OldLetterAdapter) homeAdapters);
//        initScrollListener(rv_bottom, homeAdapters);
    }


/*    private void initScrollListener(RecyclerView rv_home, HomeAdapters homeAdapters) {
        rv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (homeAdapters instanceof NewLetterAdapter) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (!isLoadingTopRv) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == homeAdapters.getItemCount() - 1) {//bottom of list!

                            loadMoreTopRv(homeAdapters); //데이터를 더 로딩하기
                            isLoadingTopRv = true;
                        }
                    }

                } else if (homeAdapters instanceof OldLetterAdapter) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (!isLoadingBottomRv) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == homeAdapters.getItemCount() - 1) {//bottom of list!
                            Log.e("2", linearLayoutManager.findLastCompletelyVisibleItemPosition() + " 사이즈"+ String.valueOf(homeAdapters.getItemCount()) );
                            loadMoreBottomRv(homeAdapters); //데이터를 더 로딩하기
                            isLoadingBottomRv = true;

                        }
                    }
                }


            }
        });


    }

    private void loadMoreTopRv(HomeAdapters homeAdapters) {
        Handler handler1 = new Handler();
        Handler handler2 = new Handler();

        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                homeAdapters.addItem(null); //로딩뷰 넣기
                homeAdapters.notifyItemInserted(homeAdapters.getItems().size() - 1);
            }
        }, 300);


        handler2.postDelayed(new Runnable() { //2초간 실행
            @Override
            public void run() {
                homeAdapters.removeItems(homeAdapters.getItems().size() - 1); //로딩뷰 없애기
                int scrollPosition = homeAdapters.getItems().size();
                int pastScrollPosition = scrollPosition;
                homeAdapters.notifyItemRemoved(scrollPosition);

                pageTopRv += 1; //뉴스를 최대 10개 더 로딩함
                //get요청
                ArrayList<HomeLetters> homeLettersArrayList = new ArrayList<>();
                tryGetLetter(homeLettersArrayList, homeAdapters, endDate, pageTopRv, startDate);

                homeAdapters.notifyDataSetChanged();
                isLoadingTopRv = false;
                rv_newletter.smoothScrollToPosition(pastScrollPosition - 1);

            }
        }, 2000);
    }

    private void loadMoreBottomRv(HomeAdapters homeAdapters) {
        Handler handler1 = new Handler();
        Handler handler2 = new Handler();

        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                homeAdapters.addItem(null); //로딩뷰 넣기
                homeAdapters.notifyItemInserted(homeAdapters.getItems().size() - 1);
            }
        }, 300);


        handler2.postDelayed(new Runnable() { //2초 뒤에 실행

            @Override
            public void run() {
                homeAdapters.removeItems(homeAdapters.getItems().size() - 1); //로딩뷰 없애기

                int pastScrollPosition = homeAdapters.getItems().size();
                homeAdapters.notifyItemRemoved(homeAdapters.getItems().size());

                pageBottomRv += 1;
                Log.e("", "페이지번호-------" + pageBottomRv);

                //get요청
                ArrayList<HomeLetters> homeLettersArrayList = new ArrayList<>();
                tryGetLetter(homeLettersArrayList, homeAdapters, endDate, pageBottomRv, "1000-01-01");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() { //0.3초 뒤에 실행
                    @Override
                    public void run() {
                        homeAdapters.notifyDataSetChanged();
                        isLoadingBottomRv = false;
                        rv_oldletter.smoothScrollToPosition(pastScrollPosition - 1);
                        Log.e("", "아이템사이즈-------" + homeAdapters.getItems().size() + "");
                    }
                }, 1000);

            }
        }, 1000);


    }

    */


}

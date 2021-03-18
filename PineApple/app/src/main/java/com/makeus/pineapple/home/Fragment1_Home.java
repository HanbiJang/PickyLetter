package com.makeus.pineapple.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.home.adapters.HomeAdapters;
import com.makeus.pineapple.home.adapters.NewLetterAdapter;
import com.makeus.pineapple.home.adapters.OldLetterAdapter;
import com.makeus.pineapple.home.data.HomeLetters;
import com.makeus.pineapple.home.data.NewLetter;
import com.makeus.pineapple.home.data.OldLetter;
import com.makeus.pineapple.home.filters.PopupFilter;
import com.makeus.pineapple.home.data.server_data.NewsResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment1_Home extends Fragment {
    //사용자 정보 얻기
    static String userId = null;
    static String token = null;
    static RequestQueue requestQueue;
    static String endDate = "2021-03-17", startDate = "2020-03-17";
    static Integer pageTopRv = 0;
    static Integer pageBottomRv = 0;

    RecyclerView rv_newletter;
    RecyclerView rv_oldletter;
    ImageView img_empty;
    Button btn_filter;
    FrameLayout fl_btn_filter;
    TextView tv_nickname, tv_dochack, tv_nim, tv_empty;

    boolean isLoadingTopRv = false; //상단 무한스크롤
    boolean isLoadingBottomRv = false;  //하단 무한 스크롤


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_home, container, false);
        pageTopRv = 0; //스크롤할때마다 page값 증가하므로 초기화
        pageBottomRv = 0; //스크롤할때마다 page값 증가하므로 초기화

        //로그인으로 얻은 유저 데이터 가져오기
        getUserData();

        //findViewById
        findViewByIdAll(view);

        //필터 버튼
        btn_filter = view.findViewById(R.id.btn_filter);
        fl_btn_filter = view.findViewById(R.id.fl_btn_filter);
        fl_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PopupFilter.class);
                startActivity(intent);
                //애니메이션 설정
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_up);

            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }

        //empty이미지 보여주기 (get요청 하기 전)
        showEmptyImg();

        //리사이클러뷰 상단 설정
        setTopRv(view);
        //리사이클러뷰 하단 설정
        setBottomRv(view);

        return view;
    }

    private void findViewByIdAll(View view) {
        img_empty = view.findViewById(R.id.img_empty);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_dochack = view.findViewById(R.id.tv_dochack);
        tv_nim = view.findViewById(R.id.tv_nim);
        tv_empty = view.findViewById(R.id.tv_empty);
        rv_newletter = view.findViewById(R.id.rv_newletter);
        rv_oldletter = view.findViewById(R.id.rv_oldletter);

    }

    //메인함수에서 정보를 받아옴
    private void getUserData() {
        userId = MainActivity.getUserId();
        token = MainActivity.getToken();
    }


    //상단 리사이클러뷰 (이번주 메일)
    private void setTopRv(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        rv_newletter.setLayoutManager(layoutManager);
        NewLetterAdapter newLetterAdapter = new NewLetterAdapter();

        //데이터 세팅
        setNewsToRv(newLetterAdapter);
    }

    private void setBottomRv(View view) {
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_oldletter.setLayoutManager(layoutManager2);
        OldLetterAdapter oldLetterAdapter = new OldLetterAdapter();

        //데이터 세팅
        setNewsToRv(oldLetterAdapter);
    }

    private void setNewsToRv(HomeAdapters homeAdapters) {
        homeAdapters.removeAll(); //안 쌓이게 하기

        //서버에서 구독메일을 받아서 리사이클러뷰의 내용을 세팅함
        ArrayList<HomeLetters> homeLetterArrayList = new ArrayList<>();
        tryGetLetter(homeLetterArrayList, homeAdapters, endDate, pageTopRv, startDate);

    }

    private void tryGetLetter(
            ArrayList homeLettersArrayList,
            HomeAdapters homeAdapters,
            String endDate,
            Integer page,
            String startDate) {
        JSONObject requestData1 = makeJsonObject();
        makeGetRequestMailBox(requestData1, makeMailBoxUrl(endDate, page, startDate), homeLettersArrayList, homeAdapters);
    }

    private JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        return requestData;
    }

    //메일박스 url 데이터 받아옴
    //파라미터를 여기에 넣어주어야 함
    private String makeMailBoxUrl(String endDate, Integer page, String startDate) {
        String url;
        url = "http://3.13.65.158/v1/users/" + userId + "/mailbox"
                + "?endDate=" + endDate + "&page=" + page + "&startDate=" + startDate;
        return url;
    }

    //서버에 GET 으로 구독메일 리스트를 받아옴
    private void makeGetRequestMailBox(JSONObject requestData, String mailBoxUrl, ArrayList<HomeLetters> homeLettersArrayList, HomeAdapters homeAdapters) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                mailBoxUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponseForNewsData(response, homeLettersArrayList, homeAdapters);
                        setNewLetterListToRv(homeAdapters, homeLettersArrayList);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "메일 오류", Toast.LENGTH_SHORT).show();
                        if(homeAdapters instanceof NewLetterAdapter){
                            showEmptyImg();//상단 리사이클러뷰 : 문구 수정 + 이미지 보이기
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return super.getParams();
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("x-access-token", token);

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        request.setShouldCache(false);
        requestQueue.add(request);

    }

    private void showEmptyImg() {
        tv_nickname.setVisibility(View.GONE);
        tv_nim.setVisibility(View.GONE);
        tv_dochack.setVisibility(View.GONE);
        tv_empty.setVisibility(View.VISIBLE);
        img_empty.setVisibility(View.VISIBLE);
        rv_newletter.setVisibility(View.GONE);

    }

    private void showNomalRv() {
        tv_nickname.setVisibility(View.VISIBLE);
        tv_nim.setVisibility(View.VISIBLE);
        tv_dochack.setVisibility(View.VISIBLE);
        tv_empty.setVisibility(View.GONE);
        img_empty.setVisibility(View.GONE);
        rv_newletter.setVisibility(View.VISIBLE);

    }


    //서버에서 받아온 객체에 저장된 내용을 기존의 클래스로 받아옴
    private void processResponseForNewsData(JSONObject response, ArrayList<HomeLetters> homeLettersArrayList, HomeAdapters homeAdapters) {
        Gson gson = new Gson();
        NewsResult newNewsResult = gson.fromJson(String.valueOf(response), NewsResult.class);
        for (int i = 0; i < newNewsResult.getNewNewsResultList().size(); i++) {
            HomeLetters homeLetters = null;

            if (homeAdapters instanceof NewLetterAdapter) {
                homeLetters = new NewLetter();
            } else if (homeAdapters instanceof OldLetterAdapter) {
                homeLetters = new OldLetter();
            }

            homeLetters.setPlatformImageUrl(newNewsResult.getNewNewsResultList().get(i).getPlatformImageUrl());
            homeLetters.setThumbnailImageUrl(newNewsResult.getNewNewsResultList().get(i).getThumbnailImageUrl());
            homeLetters.setCreatedAt(newNewsResult.getNewNewsResultList().get(i).getCreatedAt());
            homeLetters.setTitle(newNewsResult.getNewNewsResultList().get(i).getTitle());
            homeLetters.setPlatformName(newNewsResult.getNewNewsResultList().get(i).getPlatformName());
            //북마크,홈메일
            homeLetters.setLetterId(newNewsResult.getNewNewsResultList().get(i).getLetterId()); //레터아이디
            homeLetters.setBookmarkId(newNewsResult.getNewNewsResultList().get(i).getBookmarkId()); //북마크아이디
            homeLetters.setBookmarkCount(newNewsResult.getNewNewsResultList().get(i).getBookmarkCount()); //북마크카운트

            homeLettersArrayList.add(homeLetters);
        }
    }

    //서버에서 받아온 리스트 newLetterList으로 리사이클러뷰에 내용을 추가함
    private void setNewLetterListToRv(HomeAdapters homeAdapters, ArrayList<HomeLetters> homeLettersArrayList) {

        //상단 RV : 받아온 리스트 사이즈가 0이면 텅 빈 이미지 보이기
        if (homeAdapters instanceof NewLetterAdapter) {

            if (homeLettersArrayList.size() == 0 ) {
                if(MainActivity.getOneTimeEmpty() == false){
                    showEmptyImg();
                }
            } else {
                for (int i = 0; i < homeLettersArrayList.size(); i++) {
                    homeAdapters.addItem(homeLettersArrayList.get(i));
                }
                // 상단 RV : 어답터 설정, 무한 스크롤 설정
                showNomalRv();
                rv_newletter.setAdapter((NewLetterAdapter) homeAdapters);
                initScrollListenerToRV(rv_newletter, homeAdapters);
            }
        }

        //하단 RV : 데이터 세팅
        else if (homeAdapters instanceof OldLetterAdapter) {
            if (homeLettersArrayList.size() != 0) {
                for (int i = 0; i < homeLettersArrayList.size(); i++) {
                    homeAdapters.addItem(homeLettersArrayList.get(i));
                }
                // 하단 RV : 어답터 설정, 무한 스크롤 설정
                rv_oldletter.setAdapter((OldLetterAdapter) homeAdapters);
                initScrollListenerToRV(rv_oldletter, homeAdapters);
            }
        }

        MainActivity.setOneTimeEmpty(true);

    }

    private void initScrollListenerToRV(RecyclerView rv_home, HomeAdapters homeAdapters) {
        rv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoadingTopRv) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == homeAdapters.getItemCount() - 1) {//bottom of list!
                        loadMore(homeAdapters); //데이터를 더 로딩하기
                        isLoadingTopRv = true;
                    }
                }
            }
        });


    }

    //상단 리사이클러뷰의 데이터를 더 로딩하기
    private void loadMore(HomeAdapters homeAdapters) {
        homeAdapters.addItem(null);
        homeAdapters.notifyItemInserted(homeAdapters.getItems().size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                homeAdapters.removeItems(homeAdapters.getItems().size() - 1); //로딩뷰 없애기
                int scrollPosition = homeAdapters.getItems().size();
                homeAdapters.notifyItemRemoved(scrollPosition);

                pageTopRv += 1; //뉴스를 최대 10개 더 로딩함
                //get요청
                ArrayList<NewLetter> newLetterArrayListAdded = new ArrayList<>();
                tryGetLetter(newLetterArrayListAdded, homeAdapters, endDate, pageTopRv, startDate);

                int currentSize = scrollPosition;
                int nextLimit = currentSize + newLetterArrayListAdded.size(); //get으로 얻은 리스트 크기 만큼 nextLimit


                while (currentSize - 1 < nextLimit) {

                    HomeLetters tmpHomeLetter = homeAdapters.getItem(homeAdapters.getItems().size() - 1); //마지막 요소 킵
                    homeAdapters.removeItems(homeAdapters.getItems().size() - 1);//마지막 요소가 지워짐
                    homeAdapters.addItem(tmpHomeLetter); //스크롤 시 추가되는 내용 = 마지막 요소를 넣어야 함

                    for (int i = 0; i < newLetterArrayListAdded.size(); i++) {
                        homeAdapters.addItem(newLetterArrayListAdded.get(i));
                    }


                    currentSize++;
                }

                homeAdapters.notifyDataSetChanged();
                if (homeAdapters instanceof NewLetterAdapter) { //상단 RV
                    isLoadingTopRv = false;
                } else if (homeAdapters instanceof OldLetterAdapter) { //하단 RV
                    isLoadingBottomRv = false;
                }


            }
        }, 2000);

    }


}
package com.makeus.pineapple.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.home.filters.PopupFilter;
import com.makeus.pineapple.newsletters.NewNewsResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment1_Home extends Fragment {
    //사용자 정보 얻기
    static String userId = null;
    static String token = null;
    static RequestQueue requestQueue;

    RecyclerView rv_newletter;
    Button btn_filter;
    FrameLayout fl_btn_filter;

    //하단 무한 스크롤
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_home, container, false);

        //로그인으로 얻은 유저 데이터 가져오기
        getUserData();

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

        //리사이클러뷰 상단
        setTopRv(view);

        //리사이클러뷰 하단
        setBottomRv(view);



        return view;
    }

    private void getUserData() {
        userId = MainActivity.getUserId();
        token = MainActivity.getToken();
    }

    private void setBottomRv(View view) {
        RecyclerView rv_oldletter = view.findViewById(R.id.rv_oldletter);
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_oldletter.setLayoutManager(layoutManager2);
        OldLetterAdapter oldLetterAdapter = new OldLetterAdapter();

        oldLetterAdapter.addItem(new OldLetter("가게 배달지역 관리방식 개편 프로젝트", "디독", "02/01/2021", R.drawable.news_4, R.drawable.brand_1));
        oldLetterAdapter.addItem(new OldLetter("5달러 짜리 로고와 250달러 짜리 로고의 차이점", "디독", "02/01/2021", R.drawable.news_5, R.drawable.brand_1));
        oldLetterAdapter.addItem(new OldLetter("페이스북,인스타그램,유튜브의 잘못된 UI", "디독", "02/01/2021", R.drawable.news_6, R.drawable.brand_1));
        oldLetterAdapter.addItem(new OldLetter("주목받는 재택근무 관련주와 라이프스타일의 변화 \uD83D\uDC69\u200D\uD83D\uDCBB\n", "어피티", "02/01/2021", R.drawable.news_7, R.drawable.brand_3));
        oldLetterAdapter.addItem(new OldLetter("비상 걸린 콜센터,원격 콜센터 구축 업체는? \uD83D\uDCDE", "어피티", "02/01/2021", R.drawable.news_8, R.drawable.brand_3));
        oldLetterAdapter.addItem(new OldLetter("금 수요 폭등,거래소에는 호재", "어피티", "02/01/2021", R.drawable.news_9, R.drawable.brand_3));
        oldLetterAdapter.addItem(new OldLetter("타다 클라이언트 개발기", "어피티", "02/01/2021", R.drawable.news_10, R.drawable.brand_3));

        rv_oldletter.setAdapter(oldLetterAdapter); //리사이클러뷰에 어답터 설정
        initScrollListener(rv_oldletter, oldLetterAdapter);
    }

    //상단 리사이클러뷰 (이번주 메일)
    private void setTopRv(View view) {
        rv_newletter = view.findViewById(R.id.rv_newletter);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_newletter.setLayoutManager(layoutManager);
        NewLetterAdapter newLetterAdapter = new NewLetterAdapter();

        //데이터 세팅
        setNewsToTopRv(newLetterAdapter);
    }

    private void setNewsToTopRv(NewLetterAdapter newLetterAdapter) {
        newLetterAdapter.removeAll();

        //서버에서 구독메일을 받아서 리사이클러뷰의 내용을 세팅함
        ArrayList<NewLetter> newLetterArrayList = new ArrayList<>();
        tryGetNewLetter(userId, newLetterArrayList, newLetterAdapter);

    }

    private void tryGetNewLetter(String userId, ArrayList<NewLetter> newLetterArrayList, NewLetterAdapter newLetterAdapter) {
        JSONObject requestData1 = makeJsonObjectForNewLetter();
        makeGetRequestMailBox(requestData1, makeMailBoxUrl(), userId, newLetterArrayList,newLetterAdapter);
    }

    private JSONObject makeJsonObjectForNewLetter() {
        JSONObject requestData = new JSONObject();
        return requestData;
    }

    //메일박스 url 데이터 받아옴
    private String makeMailBoxUrl() {
        String url ;
        url = "http://3.13.65.158/v1/users/"+userId +"/mailbox";
        return url;
    }

    //서버에 GET 으로 구독메일 리스트를 받아옴
    private void makeGetRequestMailBox(JSONObject requestData, String mailBoxUrl, String userId, ArrayList<NewLetter> newLetterArrayList, NewLetterAdapter newLetterAdapter) {

        //서버에 요청을 보내기 위한 StringRequest 객체 생성
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                mailBoxUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("메일 결과", response.toString());
                        processResponseForNewsData(response, newLetterArrayList);
                        setNewLetterListToRv(newLetterAdapter, newLetterArrayList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "메일 불러오기 오류", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("endDate", "2021-03-17"); //임의값
                params.put("startDate", "");
                params.put("userId ", userId);

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

    private void processResponseForNewsData(JSONObject response, ArrayList<NewLetter> newLetterArrayList){
        Gson gson = new Gson();
        NewNewsResult newNewsResult = gson.fromJson(String.valueOf(response), NewNewsResult.class);
        for (int i = 0 ; i < newNewsResult.getNewNewsResultList().size(); i++){
            NewLetter newLetter = new NewLetter();
            //이미지 설정
            newLetter.setImg_brand(newNewsResult.getNewNewsResultList().get(i).getPlatformImageUrl());
            newLetter.setImg_news(newNewsResult.getNewNewsResultList().get(i).getThumbnailImageUrl());
            newLetter.setNewsDate(newNewsResult.getNewNewsResultList().get(i).getCreatedAt());
            newLetter.setNewsTitle(newNewsResult.getNewNewsResultList().get(i).getTitle());
            newLetter.setNewsBrand(newNewsResult.getNewNewsResultList().get(i).getPlatformName());

            newLetterArrayList.add(newLetter);
        }
    }

    //서버에서 받아온 리스트 newLetterList으로 리사이클러뷰에 내용을 추가함
    private void setNewLetterListToRv(NewLetterAdapter newLetterAdapter, ArrayList<NewLetter> newLetterList) {

        for (int i = 0; i < newLetterList.size(); i++) {
            newLetterAdapter.addItem(newLetterList.get(i));
            Log.e("내용추가", "내용을 추가하였습니다." + newLetterList.get(i).getNewsBrand());
        }

        rv_newletter.setAdapter(newLetterAdapter);

    }


    private void initScrollListener(RecyclerView rv_oldletter, OldLetterAdapter oldLetterAdapter) {
        rv_oldletter.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == oldLetterAdapter.getItemCount() - 1) {//bottom of list!
                        loadMore(oldLetterAdapter);
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore(OldLetterAdapter oldLetterAdapter) {
        oldLetterAdapter.addItem(null);

        oldLetterAdapter.notifyItemInserted(oldLetterAdapter.getItems().size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                oldLetterAdapter.removeItems(oldLetterAdapter.getItems().size() - 1);
                int scrollPosition = oldLetterAdapter.getItems().size();
                oldLetterAdapter.notifyItemRemoved(scrollPosition);

                int currentSize = scrollPosition;
                int nextLimit = currentSize + 9;

                while (currentSize - 1 < nextLimit) {

                    OldLetter tmpOldLetter = oldLetterAdapter.getItem(oldLetterAdapter.getItems().size() - 1); //마지막 요소 킵
                    //마지막 요소가 지워짐
                    oldLetterAdapter.removeItems(oldLetterAdapter.getItems().size() - 1);
                    //마지막 요소를 넣어야 함
                    oldLetterAdapter.addItem(tmpOldLetter); //스크롤 시 추가되는 내용
                    currentSize++;
                }

                oldLetterAdapter.notifyDataSetChanged();
                isLoading = false;

            }
        }, 2000);


    }


}
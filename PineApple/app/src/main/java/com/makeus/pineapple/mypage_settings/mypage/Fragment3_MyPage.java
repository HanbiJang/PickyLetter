package com.makeus.pineapple.mypage_settings.mypage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage_settings.mypage.adapter.BookmarkLetterAdapter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetterResult;
import com.makeus.pineapple.mypage_settings.mypage.data.UserData;
import com.makeus.pineapple.mypage_settings.settings.SettingsMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment3_MyPage extends Fragment {
    static RequestQueue requestQueueBookmark;
    static RequestQueue requestQueueUserData;
    static Integer lastLetterId = 0;

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환

    RecyclerView rv_mypage_bookmark;
    Button btn_setting;
    FrameLayout fl_btn_setting;
    TextView tv_nickname, tv_email, tv_bookmarknum;

    //무한 스크롤
    boolean isLoading = false;

    //새로고침
    SwipeRefreshLayout sr_layout;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        myContext = (FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_mypage, container, false);

        lastLetterId = 0; //마이페이지 북마크 조회 기능 초기화

        findViewByIdAll(view);

        //get 요청 관련
        if (requestQueueBookmark == null) {
            requestQueueBookmark = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }

        if(requestQueueUserData == null){
            requestQueueUserData = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }

        //북마크 리사이클러뷰 설정 (get요청 포함)
        setBookmarkRv(view);

        //사용자 정보 가져오기 (get요청 포함)
        setUserData();


        //세팅 버튼
        fl_btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_settings_main = new SettingsMain();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment, fragment_settings_main).commit();//프래그먼트 전환

            }
        });

        //네비게이션 디버깅 코드
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.toggleNavigationBarItems(true);
            }
        },500);


        //스와이프 새로고침
        sr_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //서버통신
                try {
                    lastLetterId = 0; //마이페이지 북마크 조회 기능 초기화
                    setBookmarkRv(view); //북마크 리사이클러뷰 설정 (get요청 포함)
                    setUserData(); //사용자 정보 가져오기 (get요청 포함)
                }catch (Exception e){
                    Log.e("0","새로고침 오류");
                    sr_layout.setRefreshing(false); //새로고침 멈춤
                }

                sr_layout.setRefreshing(false); //새로고침 멈춤

            }
        });

        return view;

    }

    private void setUserData() {
        UserData userData = new UserData();
        tryGetUserData(userData); //get 요청
    }

    private void tryGetUserData(UserData userData) {
        JSONObject requestData1 = makeJsonObject();
        makeGetRequestUserData(requestData1, makeUserDataUrl());
    }

    private void makeGetRequestUserData(JSONObject requestData, String makeUserDataUrl) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                makeUserDataUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserData userData = processResponseForUserData(response);
                        setUserDataToText(userData);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("**에러: ", "사용자 정보 가져오기 오류");
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
                headers.put("x-access-token", MainActivity.getToken());

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        request.setShouldCache(false);
        requestQueueBookmark.add(request);
    }

    private void setUserDataToText(UserData userData) {
        tv_nickname.setText(userData.getNickname());
        tv_bookmarknum.setText("나의 북마크 ("+userData.getBookmarkCount()+")");
        tv_email.setText(userData.getEmail());
    }

    private UserData processResponseForUserData(JSONObject response) {
        Gson gson = new Gson();
        UserData userData = gson.fromJson(String.valueOf(response), UserData.class);

        return userData;

    }

    private String makeUserDataUrl() {
        String url;
        url = "http://3.13.65.158/v1/users";
        return url;
    }

    private void setBookmarkRv(View view) {
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv_mypage_bookmark.setLayoutManager(layoutManager2);
        BookmarkLetterAdapter bookmarkLetterAdapter = new BookmarkLetterAdapter();

        //데이터 세팅
        setNewsToRv(bookmarkLetterAdapter);
    }

    private void setNewsToRv(BookmarkLetterAdapter bookmarkLetterAdapter) {
        bookmarkLetterAdapter.removeAll(); //안 쌓이게 하기

        //서버에서 구독메일을 받아서 리사이클러뷰의 내용을 세팅함
        ArrayList<BookmarkLetter> bookmarkLetterArrayList = new ArrayList<>();
        tryGetLetter(bookmarkLetterArrayList, bookmarkLetterAdapter); //get 요청
    }

    private void tryGetLetter(ArrayList<BookmarkLetter> bookmarkLetterArrayList, BookmarkLetterAdapter bookmarkLetterAdapter) {
        JSONObject requestData1 = makeJsonObject();
        makeGetRequestBookmark(requestData1, makeMailBoxUrl(lastLetterId), bookmarkLetterArrayList, bookmarkLetterAdapter);
    }

    private void makeGetRequestBookmark(JSONObject requestData, String BookmarkUrl, ArrayList<BookmarkLetter> bookmarkLetterArrayList, BookmarkLetterAdapter bookmarkLetterAdapter) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BookmarkUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponseForBookmarkData(response, bookmarkLetterArrayList, bookmarkLetterAdapter);
                        setNewLetterListToRv(bookmarkLetterAdapter, bookmarkLetterArrayList);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("**에러: ", "북마크 메일 로드 오류");
                        Toast.makeText(getContext(), "북마크 오류", Toast.LENGTH_SHORT).show();

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
                headers.put("x-access-token", MainActivity.getToken());

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        request.setShouldCache(false);
        requestQueueBookmark.add(request);
    }

    private void setNewLetterListToRv(BookmarkLetterAdapter bookmarkLetterAdapter, ArrayList<BookmarkLetter> bookmarkLetterArrayList) {
        //데이터 세팅
        if (bookmarkLetterArrayList.size() != 0) {
            for (int i = 0; i < bookmarkLetterArrayList.size(); i++) {
                bookmarkLetterAdapter.addItem(bookmarkLetterArrayList.get(i));
            }
            // 하단 RV : 어답터 설정, 무한 스크롤 설정
            rv_mypage_bookmark.setAdapter(bookmarkLetterAdapter);
            initScrollListener(rv_mypage_bookmark, bookmarkLetterAdapter);
        }

    }

    private void processResponseForBookmarkData(JSONObject response, ArrayList<BookmarkLetter> bookmarkLetterArrayList, BookmarkLetterAdapter bookmarkLetterAdapter) {
        Gson gson = new Gson();
        BookmarkLetterResult bookmarkLetterResult = gson.fromJson(String.valueOf(response), BookmarkLetterResult.class);

        if (bookmarkLetterResult.getResultList().size() != 0) {
            for (int i = 0; i < bookmarkLetterResult.getResultList().size(); i++) {
                BookmarkLetter bookmarkLetter = new BookmarkLetter();

                bookmarkLetter.setLetterId(bookmarkLetterResult.getResultList().get(i).getLetterId());
                bookmarkLetter.setPlatformId(bookmarkLetterResult.getResultList().get(i).getPlatformId());
                bookmarkLetter.setPlatformName(bookmarkLetterResult.getResultList().get(i).getPlatformName());
                bookmarkLetter.setPlatformImageUrl(bookmarkLetterResult.getResultList().get(i).getPlatformImageUrl());
                bookmarkLetter.setBookmarkId(bookmarkLetterResult.getResultList().get(i).getBookmarkId());
                bookmarkLetter.setTitle(bookmarkLetterResult.getResultList().get(i).getTitle());
                bookmarkLetter.setThumbnailImageUrl(bookmarkLetterResult.getResultList().get(i).getThumbnailImageUrl());
                bookmarkLetter.setBookmarkCount(bookmarkLetterResult.getResultList().get(i).getBookmarkCount());
                bookmarkLetter.setCreatedAt(bookmarkLetterResult.getResultList().get(i).getCreatedAt());
                bookmarkLetter.setModifiedAt(bookmarkLetterResult.getResultList().get(i).getModifiedAt());
                bookmarkLetter.setSubscribing(bookmarkLetterResult.getResultList().get(i).getSubscribing());

                //마지막 letterId 저장
                if (i == bookmarkLetterResult.getResultList().size() - 1) {
                    lastLetterId = bookmarkLetterResult.getResultList().get(i).getLetterId();
                }
                bookmarkLetterArrayList.add(bookmarkLetter);
            }
        }

    }

    private String makeMailBoxUrl(Integer lastLetterId) {
        String url;
        url = "http://3.13.65.158/v1/users/current/bookmarks?lastLetterId=" + lastLetterId;
        return url;
    }

    private JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();

        try {
            requestData.put("lastLetterId", lastLetterId);
            requestData.put("x-access-token", MainActivity.getToken());

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return requestData;
    }

    //모든 UI요소 findViewById
    private void findViewByIdAll(View view) {
        rv_mypage_bookmark = view.findViewById(R.id.rv_mypage_bookmark);
        fl_btn_setting = view.findViewById(R.id.fl_btn_setting);
        btn_setting = view.findViewById(R.id.btn_setting);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_email = view.findViewById(R.id.tv_email);
        tv_bookmarknum = view.findViewById(R.id.tv_bookmarknum);
        sr_layout = view.findViewById(R.id.sr_layout);
    }

    private void initScrollListener(RecyclerView rv_search_result, BookmarkLetterAdapter bookmarkLetterAdapter) {
        rv_search_result.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == bookmarkLetterAdapter.getItemCount() - 1) {//bottom of list!
                        loadMore(bookmarkLetterAdapter);
                        isLoading = true;
                    }
                }
            }
        });


    }

    //핸들러 함수 실행, 생성 순서 정해주기 주의!
    private void loadMore(BookmarkLetterAdapter bookmarkLetterAdapter) {

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                bookmarkLetterAdapter.addItem(null); //로딩뷰 추가
                bookmarkLetterAdapter.notifyItemInserted(bookmarkLetterAdapter.getItems().size() - 1);
            }
        }, 300);


        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                //로딩뷰 제거
                bookmarkLetterAdapter.removeItems(bookmarkLetterAdapter.getItems().size() - 1);

                int scrollPosition = bookmarkLetterAdapter.getItems().size();

                int pastScrollPosition =  scrollPosition;
                bookmarkLetterAdapter.notifyItemRemoved(scrollPosition);

                //get요청
                ArrayList<BookmarkLetter> bookmarkLetterArrayListAdded = new ArrayList<>();
                tryGetLetter(bookmarkLetterArrayListAdded, bookmarkLetterAdapter);

                bookmarkLetterAdapter.notifyDataSetChanged();
                isLoading = false;
                rv_mypage_bookmark.scrollToPosition(pastScrollPosition-2);

            }
        }, 1200);


    }


}
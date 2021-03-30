package com.makeus.pineapple.mypage_settings.mypage;

import android.content.Context;
import android.content.Intent;
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
import com.makeus.pineapple.home.adapters.OldLetterAdapter;
import com.makeus.pineapple.loading.PopupLoading;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage_settings.mypage.adapter.BookmarkLetterAdapter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetterResult;
import com.makeus.pineapple.mypage_settings.mypage.data.UserData;
import com.makeus.pineapple.mypage_settings.settings.SettingsMain;
import com.makeus.pineapple.server_controllers.get.GetBookmarkLetters;
import com.makeus.pineapple.server_controllers.get.GetMailBoxBottom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.makeus.pineapple.main.MainActivity.fragmentManager;

public class Fragment3_MyPage extends Fragment {
    public static Integer page = -1;
    public static Integer pageLimit = 0;

    static View view;

    public static Integer lastLetterId = 0;

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환

    public static RecyclerView rv_mypage_bookmark;
    Button btn_setting;
    FrameLayout fl_btn_setting;
    TextView tv_nickname, tv_email, tv_bookmarknum;

    RequestQueue requestQueueUserData;
    public static BookmarkLetterAdapter bookmarkLetterAdapter;


    //새로고침
    public static SwipeRefreshLayout sr_layout;
    public static boolean setLoadingPopup = false;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        myContext = (FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3_mypage, container, false);
        lastLetterId = 0; //마이페이지 북마크 조회 기능 초기화
        setLoadingPopup = false;

        if(requestQueueUserData == null){
            requestQueueUserData = Volley.newRequestQueue(myContext); // 큐 객체 생성하기
        }
        findViewByIdAll(view);

        setMyPage(view);



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
                try {
                    Intent intent = new Intent(getContext(), PopupLoading.class);
                    intent.putExtra("pastFragmentNum", 3);
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    Log.e("0", "로딩 오류");
                }

                setLoadingPopup = false; //로딩팝업 관련
                //서버통신
                lastLetterId = 0; //마이페이지 북마크 조회 기능 초기화
                page = -1;
                pageLimit = 0;

                setMyPage(view);

            }
        });

        return view;

    }

    private void setMyPage(View view) {
        try {
            Intent intent = new Intent(getContext(), PopupLoading.class);
            intent.putExtra("pastFragmentNum", 3);
            getContext().startActivity(intent);
        } catch (Exception e) {
            Log.e("0", "로딩 오류");
        }

        setUserData(); //사용자 정보 가져오기 (get요청 포함)
        setBookmarkRv(view); //북마크 리사이클러뷰 설정 (get요청 포함)
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
        requestQueueUserData.add(request);
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
        bookmarkLetterAdapter = new BookmarkLetterAdapter();

        //데이터 세팅
        setNewsToRv(bookmarkLetterAdapter);
    }

    private void setNewsToRv(BookmarkLetterAdapter bookmarkLetterAdapter) {
        setLoadingPopup = false;

        GetBookmarkLetters getBookmarkLetters = new GetBookmarkLetters(
                getContext(),
                rv_mypage_bookmark,
                bookmarkLetterAdapter
        );
        getBookmarkLetters.tryRequest();
    }


    private JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
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


}
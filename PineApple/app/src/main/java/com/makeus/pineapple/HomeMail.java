package com.makeus.pineapple;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.bumptech.glide.Glide;
import com.makeus.pineapple.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.home.adapters.NewLetterAdapter;
import com.makeus.pineapple.mypage.Fragment3_MyPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.OVER_SCROLL_ALWAYS;
import static android.view.View.OVER_SCROLL_IF_CONTENT_SCROLLS;
import static com.makeus.pineapple.MainActivity.getToken;
import static com.makeus.pineapple.MainActivity.token;

public class HomeMail extends Fragment {
    static RequestQueue requestQueue; //리퀘스트를 위한 객체

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    FrameLayout fl_btn_back;

    Button btn_bookmark;
    TextView tv_title;
    TextView tv_brand;
    TextView tv_date;
    ImageView img_news;
    CircleImageView cimg_brand;

    WebView wv_news;

    //메일 데이터
    String newsTitle, newsBrand, newsDate, newsImage, newsBrandImage;
    Integer letterId, bookmarkId, bookmarkCount;

    Boolean isClicked = null;        //북마크 체크 기능

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        myContext = (FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mail, container, false);

        //어댑터로부터 정보 넘겨받기
        getDataFromAdapter();
        //
        findByViewAll(view);
        //정보세팅
        newsDataSettingToUi();

        //웹뷰 세팅
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext()); // 큐 객체 생성하기
        }
        MyWebView myWebView = new MyWebView();
        myWebView.wvSetting(wv_news);
        //get요청으로 html 정보 가져오기
        tryGetHtmlAndLoad(letterId);


        //북마크 체크/해제 기능
        btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked == false){ //북마크가 안 된 상태에서 북마크하려고 누를때
                    tryMakeBookmark();

                }
                else{ // 북마크가 된 상태에서 북마크를 없앨 때
                    tryRemoveBookmark();

                }

            }
        });

        //백버튼
        fl_btn_back = view.findViewById(R.id.fl_btn_back);
        fl_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myContext.getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void tryRemoveBookmark() {

        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_line);
        isClicked = false;
    }

    private void tryMakeBookmark() {

        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill);
        isClicked = true;

    }

    private void newsDataSettingToUi() {
        tv_title.setText(newsTitle);
        tv_brand.setText(newsBrand);
        tv_date.setText(newsDate);

        //G-glide로 이미지 설정
        String imageUrl = newsBrandImage;
        Glide.with(getContext()).load(imageUrl).into(cimg_brand);

        String imageUrl2 = newsImage;
        Glide.with(getContext()).load(imageUrl2)
                .error(R.color.pickyUnableGray)
                .into(img_news);

        //북마크 세팅
        if(bookmarkCount == 0){ //북마크 안 한 상태
            btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill);
            isClicked = false;
        }
        else{ //북마크 한 상태
            btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_line);
            isClicked = true;
        }
    }

    private void findByViewAll(View view) {
        tv_title = view.findViewById(R.id.tv_title);
        tv_brand = view.findViewById(R.id.tv_brand);
        tv_date = view.findViewById(R.id.tv_date);
        img_news = view.findViewById(R.id.img_news);
        cimg_brand = view.findViewById(R.id.cimg_brand);
        wv_news = view.findViewById(R.id.wv_news);
        btn_bookmark = view.findViewById(R.id.btn_bookmark);
    }

    private void getDataFromAdapter() {
        newsTitle = getArguments().getString("newsTitle"); // 전달한 key 값
        newsBrand = getArguments().getString("newsBrand"); // 전달한 key 값
        newsDate = getArguments().getString("newsDate"); // 전달한 key 값
        newsImage = getArguments().getString("newsImage"); // 전달한 key 값
        newsBrandImage = getArguments().getString("newsBrandImage"); // 전달한 key 값
        letterId = getArguments().getInt("letterId"); // 전달한 key 값
        bookmarkId = getArguments().getInt("bookmarkId"); // 전달한 key 값
        bookmarkCount = getArguments().getInt("bookmarkCount"); // 전달한 key 값
    }

    public class MyWebView extends WebViewClient {

        RequestQueue requestQueue; //리퀘스트를 위한 객체

        void wvSetting(WebView wv_news) {
            wv_news.getSettings().setJavaScriptEnabled(true);
            wv_news.setWebViewClient(new MyWebView());
            wv_news.setOverScrollMode(OVER_SCROLL_ALWAYS);

/*            String htmlString =
                    "<!DOCTYPE html>\n" +
                            "<html lagn=\"ko\"\n" +
                            "      xmlns:layout=\"http://www.ultraq.net.nz/thymeleaf/layout\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                            "    <meta http-equiv = \"X-UA-Compatible\"content = \"IE = edge\">\n" +
                            "\n" +
                            "</head>\n" +
                            "\n" +
                            "<body>\n" +
                            "44444\n" +
                            "</body>\n" +
                            "</html>";*/


            //스크롤 기능 추가
            wv_news.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    WebView wv = (WebView) v;
                    wv.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

        }


    }

    private void tryGetHtmlAndLoad(Integer letterId) {
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("x-access-token", getToken());
        wv_news.loadUrl(makeGetHtmlUrl(letterId) , extraHeaders);

    }

    private void makeGetRequest() {
        JSONObject requestData1 = makeJsonObject();
        makeGetRequestMailBox(requestData1, makeGetHtmlUrl(letterId));
    }

    private void makeGetRequestMailBox(JSONObject requestData, String getHtmlUrl) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getHtmlUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("홈메일 결과\n", response.toString() + "...");
                        String htmlString = response.toString();
                        wv_news.loadData(htmlString, "text/html", "utf-8");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("홈메일 결과\n", error.toString() + "...");
                        Toast.makeText(getContext(), "홈메일 오류", Toast.LENGTH_SHORT).show();

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
                headers.put("x-access-token", getToken());

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

    private String makeGetHtmlUrl(Integer letterId) {
        String url;
        url = "http://3.13.65.158/v1/letters/"+letterId+"/html";
        return url;
    }

    private JSONObject makeJsonObject() {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("x-access-token", getToken());

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return requestData;
    }

}
package com.makeus.pineapple.bookmark;

import android.util.Log;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookmarkFuncs {

    /* 삭제는 항상 메일의 북마크 아이디를 get해와서 진행해야함: ID가 계속 바뀌기 때문임*/
    Button btn_bookmark;
    RequestQueue requestQueueMailInform, requestQueueAddBookmark,requestQueueDeleteBookmark;

    public BookmarkFuncs(Button btn_bookmark) {
        this.btn_bookmark = btn_bookmark;
    }


    //북마크 관련
    public void tryPostBookmark(Integer letterId, RequestQueue requestQueueAddBookmark) {
        this.requestQueueAddBookmark = requestQueueAddBookmark;
        JSONObject requestData1 = makeJsonObject(letterId);
        makePostRequestBookmark(requestData1, makeAddBookmarkUrl());
    }

    private JSONObject makeJsonObject(Integer letterId) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("letterId", letterId);

        } catch (Exception e) {

        }
        return requestData;
    }

    private String makeAddBookmarkUrl() {
        String url;
        url = "http://3.13.65.158/v1/users/current/bookmarks";
        return url;
    }

    private void makePostRequestBookmark(JSONObject requestData, String makeAddBookmarkUrl) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                makeAddBookmarkUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponse(response);
                        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill); //이미지 바꿈
                        Log.e("0", "북마크 등록 성공");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("0", "북마크 등록 오류" + error.getMessage());

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
        requestQueueAddBookmark.add(request);

    }

    public void tryDeleteBookmark(Integer bookmarkId, RequestQueue requestQueueDeleteBookmark) {
        this.requestQueueDeleteBookmark = requestQueueDeleteBookmark;
        //북마크아이디 bookmarkId Get으로 가져오기

        JSONObject requestData1 = makeJsonObjectDeleteBookmark(bookmarkId);
        makeDeleteRequestBookmark(requestData1, makeDeleteBookmarkUrl(bookmarkId));
    }

    private String makeDeleteBookmarkUrl(Integer bookmarkID) {
        String url;
        url = "http://3.13.65.158/v1/users/current/bookmarks/"+ bookmarkID;
        return url;
    }

    private JSONObject makeJsonObjectDeleteBookmark(Integer bookmarkId) {
        JSONObject requestData = new JSONObject();
        return requestData;
    }

    private void makeDeleteRequestBookmark(JSONObject requestData, String makeDeleteBookmarkUrl) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                makeDeleteBookmarkUrl,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponse(response);
                        btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark_line); //이미지 바꿈
                        Log.e("0", "북마크 삭제 성공");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("0", "북마크 삭제 오류" + error.getMessage());

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
        requestQueueDeleteBookmark.add(request);
    }

    private void processResponse(JSONObject response) {
        Gson gson = new Gson();
        BookmakrResult bookmakrResult = gson.fromJson(String.valueOf(response), BookmakrResult.class);
        Log.d("북마크 동작: ", String.valueOf(bookmakrResult.isResult()));
    }
}

package com.makeus.pineapple.search.searchViewHolders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeus.pineapple.CalStringDate;
import com.makeus.pineapple.bookmark.AddOrDelBookmark;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.R;
import com.makeus.pineapple.HomeMail;
import com.makeus.pineapple.bookmark.BookmakrResult;
import com.makeus.pineapple.search.data.SearchedNews;
import com.makeus.pineapple.search.adapters.SearchedNewsRankAdapter;
import com.makeus.pineapple.server_controllers.server_data.NewsData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewRankHolder extends SearchViewHolder {

    static RequestQueue requestQueueAddBookmark;
    static RequestQueue requestQueueDeleteBookmark;

    TextView tv_title;
    TextView tv_brand;
    TextView tv_date;
    TextView tv_num_rank;
    ImageView img_news;
    CircleImageView cimg_brand;

    Button btn_bookmark_rank;
    //북마크 체크 기능
    Integer isClicked = -1;
    static RequestQueue requestQueueBookmarkAdd, requestQueueBookmarkDel, requestQueueGetLetterInform;        //북마크 관련

    //화면 전환
    FragmentActivity myContext;


    public SearchViewRankHolder(@NonNull View itemView) {
        super(itemView);

        myContext = (FragmentActivity) itemView.getContext();
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_brand = itemView.findViewById(R.id.tv_brand);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_num_rank = itemView.findViewById(R.id.tv_num_rank);
        btn_bookmark_rank = itemView.findViewById(R.id.btn_bookmark_rank);
        img_news = itemView.findViewById(R.id.img_news);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);

        //클릭 시 동작
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition(); //리사이클러뷰 내의 위치 알 수 있음
                if (pos != RecyclerView.NO_POSITION) {
                    // 데이터 리스트로부터 아이템 데이터 참조.
                    Fragment fragment_homemail = new HomeMail();

                    sendItemDataToNext(pos, fragment_homemail);
                    showHomeMail();

/*                    myContext.getSupportFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).
                            addToBackStack(null).
                            replace(R.id.container_fragment, fragment_homemail).commit();*/

                }
            }
        });


    }

    private void showHomeMail() {
        Fragment fragment_homemail = new HomeMail();
        // 누른 아이템에 대한 정보를 다음 프래그먼트로 전달
        int pos = getAdapterPosition(); //리사이클러뷰 내의 위치 알 수 있음
        sendItemDataToNext(pos, fragment_homemail);

        MainActivity.fragmentManager.beginTransaction().show(fragment_homemail).commit();
        MainActivity.fragmentManager.beginTransaction().
                setCustomAnimations(R.anim.enter_right, R.anim.exit_left).add(R.id.container_fragment, fragment_homemail).commit();
        if(MainActivity.fragment1_home != null) MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragment1_home).commit();
        if(MainActivity.fragment2_search != null) MainActivity.fragmentManager.beginTransaction().
                setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).
                addToBackStack(null).
                hide(MainActivity.fragment2_search).commit();
        if(MainActivity.fragment3_mypage != null) MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragment3_mypage).commit();

    }

    //클릭 시 홈메일 화면으로 데이터를 보내줌
    private void sendItemDataToNext( int pos, Fragment fragment){
        SearchedNews item = SearchedNewsRankAdapter.getItems().get(pos);

        // 누른 아이템에 대한 정보 프래그먼트로 전달
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
        bundle.putString("newsTitle", item.getTitle()); // key , value
        bundle.putString("newsBrand", item.getBrand()); // key , value
        bundle.putString("newsDate", item.getDate()); // key , value
        bundle.putString("newsImage", item.getImg_news()); // key , value
        bundle.putString("newsBrandImage", item.getImg_brand()); // key , value
        bundle.putInt("letterId", item.getLetterId()); // key , value
        bundle.putInt("bookmarkId", item.getBookmarkId()); // key , value
        bundle.putInt("bookmarkCount", item.getBookmarkCount()); // key , value
        //이전화면
        bundle.putInt("preView", 2);
        fragment.setArguments(bundle);
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {

        tv_title.setText(item.getTitle());
        tv_brand.setText(item.getBrand());
        tv_date.setText(CalStringDate.calDate(item.getDate()));
        tv_num_rank.setText(item.getNumRank().toString());

        // Glide로 이미지 표시하기
        String imageUrl = item.getImg_brand();
        Glide.with(myContext).load(imageUrl)
                .error(R.color.pickyUnableGray)
                .into(cimg_brand);

        String imageUrl2 = item.getImg_news();
        Glide.with(myContext).load(imageUrl2)
                .error(R.color.pickyUnableGray)
                .into(img_news);

        //북마크 체크 기능
        //isClicked != 0 이면 이미 찍혀있음
        isClicked = item.getBookmarkId();
        if (isClicked != 0) {
            btn_bookmark_rank.setBackgroundResource(R.drawable.btn_bookmark_fill);
        }
        AddOrDelBookmark addOrDelBookmark = new AddOrDelBookmark(btn_bookmark_rank, item.getLetterId(),
                myContext, requestQueueBookmarkAdd, requestQueueBookmarkDel, requestQueueGetLetterInform);
        //북마크 체크 기능
        btn_bookmark_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrDelBookmark.setBtnFunc(isClicked);
                isClicked *= -1;

            }
        });

    }

}

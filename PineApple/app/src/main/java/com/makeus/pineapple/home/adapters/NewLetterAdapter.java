package com.makeus.pineapple.home.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.makeus.pineapple.CalStringDate;
import com.makeus.pineapple.R;
import com.makeus.pineapple.bookmark.AddOrDelBookmark;
import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.home.data.HomeLetters;
import com.makeus.pineapple.HomeMail;
import com.makeus.pineapple.main.MainActivity;
import com.makeus.pineapple.search.SearchViewCode;
import com.makeus.pineapple.search.searchViewHolders.SearchViewRankBlindHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewRankHolder;
import com.makeus.pineapple.server_controllers.get.GetMailBoxTop;
import com.makeus.pineapple.server_controllers.server_data.NewsData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewLetterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HomeAdapters {

    static ArrayList<NewsData> items = new ArrayList<>();

    //뷰타입
    private final int VIEW_TYPE_NEW_NEWS = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_MORE = 2;

    @NonNull
    @Override //뷰홀더 객체의 생성,재사용시 자동으로 호출
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        //뷰홀더 구분

        if(viewType == VIEW_TYPE_NEW_NEWS){
            //인플레이션
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.home_view_new_letter, viewGroup, false);
            return new ViewHolder(itemView);
        }
        else if(viewType == VIEW_TYPE_LOADING){
            //인플레이션
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.view_loading_top, viewGroup, false);
            return new LoadingViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        //기존 뷰객체의 데이터를 position 변수에 맞게 꺼냄
        if (viewHolder instanceof ViewHolder) {
            populateItemRows((ViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    private void populateItemRows(ViewHolder viewHolder, int position) {
        NewsData item = items.get(position);
        viewHolder.setItem(item);

    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_NEW_NEWS;
//        return items.get(position) == null ? VIEW_TYPE_MORE : VIEW_TYPE_NEW_NEWS;
    }

    @Override
    public int getItemCount() { //어댑터에서 관리하는 아이템 개수 반환
        return items == null ? 0 : items.size();
    }

    //(1) 원래 뷰홀더
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_brand;
        TextView tv_date;
        Button btn_bookmark_new;
        ImageView img_news;
        CircleImageView cimg_brand;
        Integer isClicked = 0;        //북마크 체크 기능
        FragmentActivity myContext;        //화면 전환
        static RequestQueue requestQueueBookmarkAdd, requestQueueBookmarkDel, requestQueueGetLetterInform;        //북마크 관련

        Boolean isShowHomeMail = false; //중복클릭 방지

        public ViewHolder(View itemView) { //아이템을 위한 뷰를 담아두는곳
            super(itemView);

            myContext = (FragmentActivity) itemView.getContext(); //context
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_brand = itemView.findViewById(R.id.tv_brand);
            tv_date = itemView.findViewById(R.id.tv_date);
            btn_bookmark_new = itemView.findViewById(R.id.btn_bookmark_new);
            img_news = itemView.findViewById(R.id.img_news);
            cimg_brand = itemView.findViewById(R.id.cimg_brand);


            //클릭 시 홈메일 (상세)로 가는 동작
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isShowHomeMail == false){//중복클릭 방지
                        //중복클릭 방지
                        isShowHomeMail = true;

                        int pos = getAdapterPosition(); //리사이클러뷰 내의 위치 알 수 있음
                        if (pos != RecyclerView.NO_POSITION) {
                            showHomeMail();
                        }
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
            if(MainActivity.fragment1_home != null) MainActivity.fragmentManager.beginTransaction().
                    setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).
                    addToBackStack(null).hide(MainActivity.fragment1_home).commit();
            if(MainActivity.fragment2_search != null) MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragment2_search).commit();
            if(MainActivity.fragment3_mypage != null) MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragment3_mypage).commit();


            android.os.Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            //중복클릭 방지
                            isShowHomeMail = false;
                        }
                    }
                    ,500);
        }

        //클릭 시 홈메일 화면으로 데이터를 보내줌
        private void sendItemDataToNext(int pos, Fragment fragment_homemail) {
            NewsData item = items.get(pos);

            Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
            bundle.putString("newsTitle", item.getTitle()); // key , value
            bundle.putString("newsBrand", item.getPlatformName());
            bundle.putString("newsDate", item.getCreatedAt());
            bundle.putString("newsImage", item.getThumbnailImageUrl());
            bundle.putString("newsBrandImage", item.getPlatformImageUrl());
            bundle.putInt("letterId", item.getLetterId());
            bundle.putInt("bookmarkId", item.getBookmarkId());

            //이전화면
            bundle.putInt("preView", 1);
            fragment_homemail.setArguments(bundle);
        }

        //뷰 객체의 데이터를 다른 것으로 보이도록함
        public void setItem(NewsData item) {
            tv_title.setText(item.getTitle());
            tv_brand.setText(item.getPlatformName());

            //날짜 변환
            tv_date.setText(CalStringDate.calDate(item.getCreatedAt()));

            // Glide로 이미지 표시하기
            String imageUrl = item.getPlatformImageUrl();
            Glide.with(myContext).load(imageUrl)
                    .error(R.color.pickyUnableGray)
                    .into(cimg_brand);

            String imageUrl2 = item.getThumbnailImageUrl();
            Glide.with(myContext).load(imageUrl2)
                    .error(R.color.pickyUnableGray)
                    .into(img_news);

            //북마크 체크 기능
            //isClicked != 0 이면 이미 찍혀있음
            isClicked = item.getBookmarkId();
            if (isClicked != 0) {
                btn_bookmark_new.setBackgroundResource(R.drawable.btn_bookmark_fill);
            }
            AddOrDelBookmark addOrDelBookmark = new AddOrDelBookmark(btn_bookmark_new, item.getLetterId(),
                    myContext, requestQueueBookmarkAdd, requestQueueBookmarkDel, requestQueueGetLetterInform);
            //북마크 체크 기능
            btn_bookmark_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addOrDelBookmark.setBtnFunc(isClicked);
                    isClicked *= -1;

                }
            });
        }
    }

    //(2) 로딩뷰 홀더
    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        Context myContext;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            myContext = (FragmentActivity) itemView.getContext(); //context
        }

    }

    //어댑터에서 NewLetter 객체를 사용할 수 있도록하는 함수들
    public void removeItems(int position) {
        items.remove(position);
    }

    @Override
    public ArrayList<NewsData> getItems() {
        return items;
    }

    @Override
    public void addItem(HomeLetters item) {
        items.add((NewsData) item);
    }

    public void setItems(ArrayList<NewsData> items) {
        this.items = items;
    }

    @Override
    public NewsData getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, NewsData item) {
        items.set(position, item);
    }

    //ProgressBar would be displayed
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
//        NewsData item = items.get(position);
//        viewHolder.setItem(item);

    }

    //모든 내용 삭제
    @Override
    public void removeAll() {
        items.clear();
    }

}
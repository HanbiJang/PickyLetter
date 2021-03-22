package com.makeus.pineapple.mypage_settings.mypage.adapter;

import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.makeus.pineapple.R;
import com.makeus.pineapple.HomeMail;
import com.makeus.pineapple.bookmark.AddOrDelBookmark;
import com.makeus.pineapple.bookmark.BookmarkFuncs;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetter;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkLetterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static ArrayList<BookmarkLetter> items = new ArrayList<>();
    //뷰타입 - 로딩뷰, 원래뷰
    private final int VIEW_TYPE_BOOKMARK = 0;
    private final int VIEW_TYPE_LOADING = 1;

    @NonNull
    @Override //뷰홀더 객체의 생성,재사용시 자동으로 호출
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //xml 레이아웃으로 뷰객체를 생성, 뷰홀더 객체에 담아 반환
        //여러가지 뷰타입을 나눠 경우마다 다르게 만들 수 있음

        if (viewType == VIEW_TYPE_BOOKMARK) {
            //인플레이션
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.mypage_view_bookmark_letter, viewGroup, false);

            return new BookmarkLetterAdapter.ViewHolder(itemView);
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            //인플레이션
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.view_loading, viewGroup, false);

            return new LoadingViewHolder(itemView);

        }

        return null;

    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_BOOKMARK;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        //기존 뷰객체의 데이터를 position 변수에 맞게 꺼냄

        if (viewHolder instanceof BookmarkLetterAdapter.ViewHolder) {
            populateItemRows((BookmarkLetterAdapter.ViewHolder) viewHolder, position);
        } else if (viewHolder instanceof BookmarkLetterAdapter.LoadingViewHolder) {
            showLoadingView((BookmarkLetterAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    private void populateItemRows(BookmarkLetterAdapter.ViewHolder viewHolder, int position) {
        //정보 세팅
        BookmarkLetter item = items.get(position);
        viewHolder.setItem(item);

    }

    private void showLoadingView(BookmarkLetterAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    @Override
    public int getItemCount() { //어댑터에서 관리하는 아이템 개수 반환
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_brand;
        TextView tv_date;
        Button btn_bookmark_bookmark;
        ImageView img_news;
        CircleImageView cimg_brand;
        static RequestQueue requestQueueBookmarkAdd, requestQueueBookmarkDel, requestQueueGetLetterInform;        //북마크 관련

        //북마크 체크 기능
        Integer isClicked = -1; //이미 북마크를 했으니 true 설정

        //화면 전환
        FragmentActivity myContext;

        //요청
        RequestQueue requestQueueAddBookmark;
        RequestQueue requestQueueDeleteBookmark;


        public ViewHolder(View itemView) { //아이템을 위한 뷰를 담아두는곳
            super(itemView);
            myContext = (FragmentActivity) itemView.getContext();

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_brand = itemView.findViewById(R.id.tv_brand);
            tv_date = itemView.findViewById(R.id.tv_date);
            btn_bookmark_bookmark = itemView.findViewById(R.id.btn_bookmark_bookmark);
            img_news = itemView.findViewById(R.id.img_news);
            cimg_brand = itemView.findViewById(R.id.cimg_brand);


            //클릭 시 동작
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition(); //리사이클러뷰 내의 위치 알 수 있음
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        BookmarkLetter item = items.get(pos);
                        Fragment fragment_homemail = new HomeMail();

                        // 누른 아이템에 대한 정보 프래그먼트로 전달
                        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
                        bundle.putString("newsTitle", item.getTitle()); // key , value
                        bundle.putString("newsBrand", item.getPlatformName());
                        bundle.putString("newsDate", item.getCreatedAt());
                        bundle.putString("newsImage", item.getThumbnailImageUrl());
                        bundle.putString("newsBrandImage", item.getPlatformImageUrl());
                        bundle.putInt("letterId", item.getLetterId());
                        bundle.putInt("bookmarkId", item.getBookmarkId());
                        bundle.putInt("bookmarkCount", item.getBookmarkCount());
                        fragment_homemail.setArguments(bundle);

                        myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left_pop, R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment, fragment_homemail).commit();//프래그먼트 전환


                    }
                }
            });

        }

        public void setItem(BookmarkLetter item) { //뷰 객체의 데이터를 다른 것으로 보이도록함
            tv_title.setText(item.getTitle());
            tv_brand.setText(item.getPlatformName());
            tv_date.setText(item.getCreatedAt());

            // Glide로 이미지 표시하기
            String imageUrl = item.getPlatformImageUrl();
            Glide.with(myContext).load(imageUrl).into(cimg_brand);

            String imageUrl2 = item.getThumbnailImageUrl();
            Glide.with(myContext).load(imageUrl2)
                    .error(R.color.pickyUnableGray)
                    .into(img_news);


            //북마크 체크 기능
            //isClicked != 0 이면 이미 찍혀있음
            isClicked = item.getBookmarkId();
            if(isClicked != 0){
                btn_bookmark_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill);
            }
            AddOrDelBookmark addOrDelBookmark = new AddOrDelBookmark(btn_bookmark_bookmark,item.getLetterId(),
                    myContext,requestQueueBookmarkAdd,requestQueueBookmarkDel,requestQueueGetLetterInform);
            //북마크 체크 기능
            btn_bookmark_bookmark.setOnClickListener(new View.OnClickListener() {
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

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    //어댑터에서 NewLetter 객체를 사용할 수 있도록하는 함수들
    public void addItem(BookmarkLetter item) {
        items.add(item);
    }

    public void setItems(ArrayList<BookmarkLetter> items) {
        this.items = items;
    }

    public BookmarkLetter getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, BookmarkLetter item) {
        items.set(position, item);
    }


    public void removeItems(int position){
        items.remove(position);
    }

    public ArrayList<BookmarkLetter> getItems(){
        return items;
    }

    public void removeAll(){ items.clear(); }


}

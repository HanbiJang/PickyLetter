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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.makeus.pineapple.R;
import com.makeus.pineapple.HomeMail;
import com.makeus.pineapple.bookmark.AddOrDelBookmark;
import com.makeus.pineapple.bookmark.BookmarkFuncs;
import com.makeus.pineapple.search.adapters.SearchedNewsResultAdapter;
import com.makeus.pineapple.search.data.SearchedNews;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewResultHolder extends SearchViewHolder {

    TextView tv_title;
    TextView tv_brand;
    TextView tv_date;
    ImageView img_news;
    CircleImageView cimg_brand;
    Button btn_bookmark_result;

    //북마크 체크 기능
    Integer isClicked;
    static RequestQueue requestQueueBookmarkAdd, requestQueueBookmarkDel, requestQueueGetLetterInform;
    //화면 전환
    FragmentActivity myContext;


    public SearchViewResultHolder(@NonNull View itemView) {
        super(itemView);
        myContext = (FragmentActivity) itemView.getContext();
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_brand = itemView.findViewById(R.id.tv_brand);
        tv_date = itemView.findViewById(R.id.tv_date);
        btn_bookmark_result = itemView.findViewById(R.id.btn_bookmark_result);
        img_news = itemView.findViewById(R.id.img_news);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {
        tv_title.setText(item.getTitle());
        tv_brand.setText(item.getBrand());
        tv_date.setText(item.getDate());

        // Glide로 이미지 표시하기
        String imageUrl = item.getImg_brand();
        Glide.with(myContext).load(imageUrl).into(cimg_brand);

        String imageUrl2 = item.getImg_news();
        Glide.with(myContext).load(imageUrl2)
                .error(R.color.pickyUnableGray)
                .into(img_news);

        //클릭 시 동작
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition() ; //리사이클러뷰 내의 위치 알 수 있음
                if (pos != RecyclerView.NO_POSITION) {
                    // 데이터 리스트로부터 아이템 데이터 참조.
                    SearchedNews item = SearchedNewsResultAdapter.getItems().get(pos);
                    Fragment fragment_homemail = new HomeMail();

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
                    fragment_homemail.setArguments(bundle);

                    myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left,R.anim.enter_left_pop,R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment,fragment_homemail).commit();


                }
            }
        });

        //북마크 체크 기능
        //isClicked != 0 이면 이미 찍혀있음
        isClicked = item.getBookmarkId();
        if(isClicked != 0){
            btn_bookmark_result.setBackgroundResource(R.drawable.btn_bookmark_fill);
        }
        AddOrDelBookmark addOrDelBookmark = new AddOrDelBookmark(btn_bookmark_result,item.getLetterId(),
                myContext,requestQueueBookmarkAdd,requestQueueBookmarkDel,requestQueueGetLetterInform);
        //북마크 체크 기능
        btn_bookmark_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrDelBookmark.setBtnFunc(isClicked);
                isClicked *= -1;

            }
        });

    }
}

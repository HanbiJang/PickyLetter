package com.makeus.pineapple.search.searchViewHolders;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeus.pineapple.R;

import com.makeus.pineapple.search.PopupSub;
import com.makeus.pineapple.search.data.SearchedNews;
import com.makeus.pineapple.search.adapters.SearchedNewsRankAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewResultBlindHolder extends SearchViewHolder {

    TextView tv_title;
    TextView tv_brand;
    TextView tv_date;
    ImageView img_news;
    CircleImageView cimg_brand;

    //브랜드 이름 넘기기
    String brand;

    //화면 전환
    FragmentActivity myContext;


    public SearchViewResultBlindHolder(@NonNull View itemView) {
        super(itemView);

        myContext = (FragmentActivity) itemView.getContext();
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_brand = itemView.findViewById(R.id.tv_brand);
        tv_date = itemView.findViewById(R.id.tv_date);
        img_news = itemView.findViewById(R.id.img_news);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);

        //클릭 시 동작
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition() ; //리사이클러뷰 내의 위치 알 수 있음
                if (pos != RecyclerView.NO_POSITION) {
                    // 데이터 리스트로부터 아이템 데이터 참조.
                    SearchedNews item = SearchedNewsRankAdapter.getItems().get(pos);

                    Intent intent = new Intent(myContext, PopupSub.class);
                    //브랜드 이름 넘기기
                    intent.putExtra("brand",brand);

                    myContext.startActivity(intent);

                }
            }
        });
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {

        tv_title.setText(item.getTitle());
        //브랜드 이름 넘기기
        tv_brand.setText(item.getBrand());
        brand = item.getBrand();
        tv_date.setText(item.getDate());

        // Glide로 이미지 표시하기
        String imageUrl = item.getImg_brand();
        Glide.with(myContext).load(imageUrl).into(cimg_brand);

        String imageUrl2 = item.getImg_news();
        Glide.with(myContext).load(imageUrl2)
                .error(R.color.pickyUnableGray)
                .into(img_news);

    }
}

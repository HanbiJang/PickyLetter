package com.makeus.pineapple.search.searchViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.makeus.pineapple.R;
import com.makeus.pineapple.search.SearchedNews;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewRankHolder extends SearchViewHolder {

    TextView tv_title_rank;
    TextView tv_brand;
    TextView tv_date;
    TextView tv_num_rank;

    ImageView img_news;
    CircleImageView cimg_brand;

    Button btn_bookmark_rank;
    //북마크 체크 기능
    Boolean isClicked = false;


    public SearchViewRankHolder(@NonNull View itemView) {
        super(itemView);

        tv_title_rank = itemView.findViewById(R.id.tv_title_rank);
        tv_brand = itemView.findViewById(R.id.tv_brand);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_num_rank = itemView.findViewById(R.id.tv_num_rank);
        btn_bookmark_rank = itemView.findViewById(R.id.btn_bookmark_rank);

        img_news = itemView.findViewById(R.id.img_news);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {

        tv_title_rank.setText(item.getTitle());
        tv_brand.setText(item.getBrand());
        tv_date.setText(item.getDate());
        tv_num_rank.setText(item.getNumRank().toString());

        img_news.setImageResource(item.getImg_news());
        cimg_brand.setImageResource(item.getImg_brand());

        //북마크 체크 기능
        btn_bookmark_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked == false){
                    btn_bookmark_rank.setBackgroundResource(R.drawable.btn_bookmark_fill);
                    isClicked = true;
                }
                else{
                    btn_bookmark_rank.setBackgroundResource(R.drawable.btn_bookmark_line);
                    isClicked = false;
                }

            }
        });

    }
}

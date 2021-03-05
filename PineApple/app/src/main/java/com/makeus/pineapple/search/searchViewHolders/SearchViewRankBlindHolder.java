package com.makeus.pineapple.search.searchViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.makeus.pineapple.R;
import com.makeus.pineapple.search.SearchedNews;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewRankBlindHolder extends SearchViewHolder {

    TextView tv_title_rank_blind;
    TextView tv_brand;
    TextView tv_date;
    TextView tv_num_rank;
    ImageView img_news;
    CircleImageView cimg_brand;



    public SearchViewRankBlindHolder(@NonNull View itemView) {
        super(itemView);

        tv_title_rank_blind = itemView.findViewById(R.id.tv_title_rank_blind);
        tv_brand = itemView.findViewById(R.id.tv_brand);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_num_rank = itemView.findViewById(R.id.tv_num_rank_blind);

        img_news = itemView.findViewById(R.id.img_news);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {

        tv_title_rank_blind.setText(item.getTitle());
        tv_brand.setText(item.getBrand());
        tv_date.setText(item.getDate());
        tv_num_rank.setText(item.getNumRank().toString());

        img_news.setImageResource(item.getImg_news());
        cimg_brand.setImageResource(item.getImg_brand());

    }
}

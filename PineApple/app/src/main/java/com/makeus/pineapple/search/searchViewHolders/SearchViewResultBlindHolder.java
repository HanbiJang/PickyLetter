package com.makeus.pineapple.search.searchViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.makeus.pineapple.R;
import com.makeus.pineapple.search.SearchedNews;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewResultBlindHolder extends SearchViewHolder {

    TextView tv_brand;
    TextView tv_date;
    ImageView img_news;
    CircleImageView cimg_brand;


    public SearchViewResultBlindHolder(@NonNull View itemView) {
        super(itemView);

        tv_brand = itemView.findViewById(R.id.tv_brand);
        tv_date = itemView.findViewById(R.id.tv_date);

        img_news = itemView.findViewById(R.id.img_news);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {

        tv_brand.setText(item.getBrand());
        tv_date.setText(item.getDate());

        img_news.setImageResource(item.getImg_news());
        cimg_brand.setImageResource(item.getImg_brand());

    }
}

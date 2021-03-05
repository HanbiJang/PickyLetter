package com.makeus.pineapple.search.searchViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.makeus.pineapple.R;
import com.makeus.pineapple.search.SearchedNews;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewResultHolder extends SearchViewHolder {

    TextView tv_title_result;
    TextView tv_brand;
    TextView tv_date;

    ImageView img_news;
    CircleImageView cimg_brand;

    Button btn_bookmark_result;
    //북마크 체크 기능
    Boolean isClicked = false;


    public SearchViewResultHolder(@NonNull View itemView) {
        super(itemView);

        tv_title_result = itemView.findViewById(R.id.tv_title_result);
        tv_brand = itemView.findViewById(R.id.tv_brand);
        tv_date = itemView.findViewById(R.id.tv_date);
        btn_bookmark_result = itemView.findViewById(R.id.btn_bookmark_result);

        img_news = itemView.findViewById(R.id.img_news);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {

        tv_title_result.setText(item.getTitle());
        tv_brand.setText(item.getBrand());
        tv_date.setText(item.getDate());

        img_news.setImageResource(item.getImg_news());
        cimg_brand.setImageResource(item.getImg_brand());

        //북마크 체크 기능
        btn_bookmark_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked == false){
                    btn_bookmark_result.setBackgroundResource(R.drawable.btn_bookmark_fill);
                    isClicked = true;
                }
                else{
                    btn_bookmark_result.setBackgroundResource(R.drawable.btn_bookmark_line);
                    isClicked = false;
                }

            }
        });

    }
}

package com.makeus.pineapple.search.searchViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.makeus.pineapple.R;
import com.makeus.pineapple.search.SearchedNews;

public class SearchViewResultBlindHolder extends SearchViewHolder {

    TextView tv_brand;
    TextView tv_date;


    public SearchViewResultBlindHolder(@NonNull View itemView) {
        super(itemView);

        tv_brand = itemView.findViewById(R.id.tv_brand);
        tv_date = itemView.findViewById(R.id.tv_date);
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {

        tv_brand.setText(item.getBrand());
        tv_date.setText(item.getDate());

    }
}

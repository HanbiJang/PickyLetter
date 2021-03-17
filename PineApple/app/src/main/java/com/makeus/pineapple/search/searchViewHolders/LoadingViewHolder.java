package com.makeus.pineapple.search.searchViewHolders;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.makeus.pineapple.R;
import com.makeus.pineapple.search.SearchedNews;


//(2) 로딩뷰 홀더
public class LoadingViewHolder extends SearchViewHolder {

    ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }

    @Override
    public void setItem(SearchViewHolder viewHolder, SearchedNews item) {

    }
}
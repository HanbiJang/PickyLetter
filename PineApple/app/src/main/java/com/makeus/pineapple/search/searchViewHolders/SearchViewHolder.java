package com.makeus.pineapple.search.searchViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.search.SearchedNews;

public abstract class SearchViewHolder extends RecyclerView.ViewHolder {

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract void setItem(SearchViewHolder viewHolder, SearchedNews item);

}

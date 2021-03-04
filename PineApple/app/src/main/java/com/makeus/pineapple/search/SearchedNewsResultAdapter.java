package com.makeus.pineapple.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;
import com.makeus.pineapple.search.searchViewHolders.SearchViewHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewRankBlindHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewResultBlindHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewResultHolder;

import java.util.ArrayList;

public class SearchedNewsResultAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    ArrayList<SearchedNews> items = new ArrayList<>();


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        //뷰홀더 구분

        if(viewType == SearchViewCode.VIEW_SEARCH_RESULT) {
            itemView = inflater.inflate(R.layout.search_view_letter_result, parent, false);
            return new SearchViewResultHolder(itemView);

        }
        else if (viewType == SearchViewCode.VIEW_SEARCH_RESULT_BLIND){
            itemView = inflater.inflate(R.layout.search_view_letter_result_blind,parent,false);
            return new SearchViewResultBlindHolder(itemView);
        }

        else{
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        SearchedNews item= items.get(position);
        holder.setItem(holder, item);

    }

    @Override
    public int getItemViewType(int position){
        return items.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SearchedNews item){
        items.add(item);
    }

    public void setItems(ArrayList<SearchedNews> items){
        this.items = items;
    }



}

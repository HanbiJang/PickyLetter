package com.makeus.pineapple.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;
import com.makeus.pineapple.search.searchViewHolders.SearchViewHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewRankBlindHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewRankHolder;

import java.util.ArrayList;

public class SearchedNewsRankAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    ArrayList<SearchedNews> items = new ArrayList<>();


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        //뷰홀더 구분

        if(viewType == SearchViewCode.VIEW_RANK_SEARCH){
            itemView = inflater.inflate(R.layout.search_view_letter_rank, parent, false);
            return new SearchViewRankHolder(itemView);
        }


        else if (viewType == SearchViewCode.VIEW_SEARCH_RANK_BLIND){
            itemView = inflater.inflate(R.layout.search_view_letter_rank_blind,parent,false);
            return new SearchViewRankBlindHolder(itemView);

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

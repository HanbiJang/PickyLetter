package com.makeus.pineapple.search.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage_settings.mypage.adapter.BookmarkLetterAdapter;
import com.makeus.pineapple.mypage_settings.mypage.data.BookmarkLetter;
import com.makeus.pineapple.search.SearchViewCode;
import com.makeus.pineapple.search.data.SearchedNews;
import com.makeus.pineapple.search.searchViewHolders.LoadingViewHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewResultBlindHolder;
import com.makeus.pineapple.search.searchViewHolders.SearchViewResultHolder;

import java.util.ArrayList;

public class SearchedNewsResultAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    static ArrayList<SearchedNews>  items = new ArrayList<>();


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


/*        else if (viewType == SearchViewCode.VIEW_TYPE_LOADING){
            //인플레이션
            itemView = inflater.inflate(R.layout.view_loading,parent,false);
            return new LoadingViewHolder(itemView);
        }*/
        else if (viewType == SearchViewCode.VIEW_TYPE_MORE){
            //인플레이션
            itemView = inflater.inflate(R.layout.home_view_more,parent,false);
            return new LoadingViewHolder(itemView);
        }


        else{
            return null;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position)== null){
//            return SearchViewCode.VIEW_TYPE_LOADING;
            return SearchViewCode.VIEW_TYPE_MORE;
        }
        else{
            return items.get(position).getViewType();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        if (holder instanceof SearchViewHolder) {
            SearchedNews item= items.get(position);
            holder.setItem(holder, item);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }

    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        viewHolder.setItem(viewHolder,getItem(position));
    }

    public void removeAll(){items.clear();}

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

    public void removeItems(int position){
        items.remove(position);
    }

    public static ArrayList<SearchedNews> getItems(){
        return items;
    }

    public SearchedNews getItem(int position){
        return items.get(position);
    }



}

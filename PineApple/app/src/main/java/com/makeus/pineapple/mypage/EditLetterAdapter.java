package com.makeus.pineapple.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage.mypageSettingsViewHolders.EditLetterViewEndHolder;
import com.makeus.pineapple.mypage.mypageSettingsViewHolders.EditLetterViewHolder;
import com.makeus.pineapple.mypage.mypageSettingsViewHolders.EditLetterViewMiddleHolder;

import java.util.ArrayList;

public class EditLetterAdapter extends RecyclerView.Adapter<EditLetterViewHolder> {

    ArrayList<EditedLetter> items = new ArrayList<>();


    @NonNull
    @Override
    public EditLetterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        //뷰홀더 구분

        if(viewType == EditLetterViewCode.VIEW_EDIT_LETTER_MIDDLE){
            itemView = inflater.inflate(R.layout.settings_view_edit_letter, parent, false);
            return new EditLetterViewMiddleHolder(itemView);
        }
        else {
            itemView = inflater.inflate(R.layout.settings_view_edit_letter_end,parent,false);
            return new EditLetterViewEndHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull EditLetterViewHolder holder, int position) {

        EditedLetter item= items.get(position);
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

    public void addItem(EditedLetter item){
        items.add(item);
    }

    public void setItems(ArrayList<EditedLetter> items){
        this.items = items;
    }



}

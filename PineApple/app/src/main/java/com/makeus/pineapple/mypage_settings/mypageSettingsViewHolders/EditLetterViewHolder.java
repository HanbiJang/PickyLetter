package com.makeus.pineapple.mypage_settings.mypageSettingsViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.mypage_settings.settings.EditedLetter;

public abstract class EditLetterViewHolder extends RecyclerView.ViewHolder {

    public EditLetterViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void setItem(EditLetterViewHolder viewHolder, EditedLetter item);

}

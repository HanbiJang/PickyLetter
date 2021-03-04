package com.makeus.pineapple.mypage.mypageSettingsViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage.EditedLetter;

public class EditLetterViewEndHolder extends EditLetterViewHolder {

    TextView tv_brand;

    public EditLetterViewEndHolder(@NonNull View itemView) {
        super(itemView);

        tv_brand = itemView.findViewById(R.id.tv_brand);
    }

    @Override
    public void setItem(EditLetterViewHolder viewHolder, EditedLetter item) {

        tv_brand.setText(item.getBrand());

    }
}

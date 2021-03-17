package com.makeus.pineapple.mypage.mypageSettingsViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage.EditedLetter;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditLetterViewMiddleHolder extends EditLetterViewHolder {

    TextView tv_brand;
    CircleImageView cimg_brand;

    public EditLetterViewMiddleHolder(@NonNull View itemView) {
        super(itemView);

        tv_brand = itemView.findViewById(R.id.tv_brand);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);

    }

    @Override
    public void setItem(EditLetterViewHolder viewHolder, EditedLetter item) {

        tv_brand.setText(item.getBrand());
        cimg_brand.setImageResource(item.getImg_brand());

    }
}

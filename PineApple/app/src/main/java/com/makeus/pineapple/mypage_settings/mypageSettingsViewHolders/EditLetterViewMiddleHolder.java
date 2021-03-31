package com.makeus.pineapple.mypage_settings.mypageSettingsViewHolders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.makeus.pineapple.R;
import com.makeus.pineapple.mypage_settings.settings.EditedLetter;
import com.makeus.pineapple.popup.PopupSub;
import com.makeus.pineapple.popup.PopupUnSub;
import com.makeus.pineapple.server_controllers.delete.DeleteSubPlatform;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditLetterViewMiddleHolder extends EditLetterViewHolder {

    Context myContext;
    TextView tv_brand;
    CircleImageView cimg_brand;
    Button btn_del;


    public EditLetterViewMiddleHolder(@NonNull View itemView) {
        super(itemView);

        myContext = itemView.getContext();
        tv_brand = itemView.findViewById(R.id.tv_brand);
        cimg_brand = itemView.findViewById(R.id.cimg_brand);
        btn_del = itemView.findViewById(R.id.btn_del);
    }

    @Override
    public void setItem(EditLetterViewHolder viewHolder, EditedLetter item) {

        tv_brand.setText(item.getName());

        String imageUrl2 = item.getImageUrl();
        Glide.with(myContext).load(imageUrl2)
                .error(R.color.pickyUnableGray)
                .into(cimg_brand);

        //버튼 초기 설정
        if(item.getSubscribing() == true){ //삭제버튼
            btn_del.setBackgroundResource(R.drawable.btn_delete_fill);
        }
        else{ //추가버튼
            btn_del.setBackgroundResource(R.drawable.btn_add_fill);
        }

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //삭제 요청
                if (item.getSubscribing() == true) { // 구독 중

                    Intent intent = new Intent(myContext, PopupUnSub.class);
                    intent.putExtra("brand", item.getName());
                    intent.putExtra("platformId", item.getPlatformId());
                    myContext.startActivity(intent);

                }

                else{ // 구독 요청
                    //브랜드 명과 플랫폼 아이디를 넘기기
                    Intent intent = new Intent(myContext, PopupSub.class);
                    intent.putExtra("brand", item.getName());
                    intent.putExtra("platformId", item.getPlatformId());
                    myContext.startActivity(intent);

                }

            }
        });

    }
}

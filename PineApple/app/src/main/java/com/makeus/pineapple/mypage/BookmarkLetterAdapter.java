package com.makeus.pineapple.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;
import com.makeus.pineapple.home.NewLetter;

import java.util.ArrayList;

public class BookmarkLetterAdapter extends RecyclerView.Adapter<BookmarkLetterAdapter.ViewHolder> {

    ArrayList<NewLetter> items = new ArrayList<>();

    @NonNull
    @Override //뷰홀더 객체의 생성,재사용시 자동으로 호출
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //xml 레이아웃으로 뷰객체를 생성, 뷰홀더 객체에 담아 반환
        //여러가지 뷰타입을 나눠 경우마다 다르게 만들 수 있음

        //인플레이션
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.mypage_view_bookmark_letter, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //기존 뷰객체의 데이터를 position 변수에 맞게 꺼냄

        NewLetter item= items.get(position);
        viewHolder.setItem(item);

    }

    @Override
    public int getItemCount() { //어댑터에서 관리하는 아이템 개수 반환
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_brand;
        TextView tv_date;
        Button btn_bookmark_bookmark;
        //북마크 체크 기능
        Boolean isClicked = true;


        public ViewHolder(View itemView){ //아이템을 위한 뷰를 담아두는곳
            super(itemView);

            tv_brand = itemView.findViewById(R.id.tv_brand);
            tv_date = itemView.findViewById(R.id.tv_date);
            btn_bookmark_bookmark = itemView.findViewById(R.id.btn_bookmark_bookmark);

        }

        public void setItem(NewLetter item){ //뷰 객체의 데이터를 다른 것으로 보이도록함
            tv_brand.setText(item.getNewsBrand());
            tv_date.setText(item.getNewsDate());

            //북마크 체크 기능
            btn_bookmark_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill);//이미 눌려있음
            btn_bookmark_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClicked == false){
                        btn_bookmark_bookmark.setBackgroundResource(R.drawable.btn_bookmark_fill);
                        isClicked = true;
                    }
                    else{
                        btn_bookmark_bookmark.setBackgroundResource(R.drawable.btn_bookmark_line);
                        isClicked = false;
                    }

                }
            });
        }
    }

    //어댑터에서 NewLetter 객체를 사용할 수 있도록하는 함수들
    public void addItem(NewLetter item){
        items.add(item);
    }

    public void setItems(ArrayList<NewLetter> items){
        this.items=items;
    }

    public NewLetter getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, NewLetter item){
        items.set(position,item);
    }


}

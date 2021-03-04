package com.makeus.pineapple.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.R;

import java.util.ArrayList;

public class OldLetterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<OldLetter> items = new ArrayList<>();
    //뷰타입 - 로딩뷰, 원래뷰
    private final int VIEW_TYPE_PAST_NEWS = 0;
    private final int VIEW_TYPE_LOADING = 1;

    @NonNull
    @Override //뷰홀더 객체의 생성,재사용시 자동으로 호출
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //xml 레이아웃으로 뷰객체를 생성, 뷰홀더 객체에 담아 반환
        //여러가지 뷰타입을 나눠 경우마다 다르게 만들 수 있음

        if (viewType == VIEW_TYPE_PAST_NEWS){
            //인플레이션
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.home_view_past_letter, viewGroup, false);

            return new ViewHolder(itemView);
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            //인플레이션
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.view_loading, viewGroup, false);

            return new LoadingViewHolder(itemView);

        }

        return null;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        //기존 뷰객체의 데이터를 position 변수에 맞게 꺼냄

        if (viewHolder instanceof ViewHolder) {
            populateItemRows((ViewHolder) viewHolder, position);
        }
        else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }


    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_PAST_NEWS;
    }

    @Override
    public int getItemCount() { //어댑터에서 관리하는 아이템 개수 반환
        return items == null ? 0 : items.size();
    }

    //(1) 원래 뷰홀더
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_brand;
        TextView tv_date;
        Button btn_bookmark_past;
        //북마크 체크 기능
        Boolean isClicked = false;

        public ViewHolder(View itemView){ //아이템을 위한 뷰를 담아두는곳
            super(itemView);

            tv_brand = itemView.findViewById(R.id.tv_brand);
            tv_date = itemView.findViewById(R.id.tv_date);
            btn_bookmark_past = itemView.findViewById(R.id.btn_bookmark_past);

        }

        public void setItem(OldLetter item){ //뷰 객체의 데이터를 다른 것으로 보이도록함
            tv_brand.setText(item.getNewsBrand());
            tv_date.setText(item.getNewsDate());

            //북마크 체크 기능
            btn_bookmark_past.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClicked == false){
                        btn_bookmark_past.setBackgroundResource(R.drawable.btn_bookmark_fill);
                        isClicked = true;
                    }
                    else{
                        btn_bookmark_past.setBackgroundResource(R.drawable.btn_bookmark_line);
                        isClicked = false;
                    }

                }
            });

        }
    }

    //(2) 로딩뷰 홀더
    static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    //어댑터에서 NewLetter 객체를 사용할 수 있도록하는 함수들
    public void addItem(OldLetter item){
        items.add(item);
    }

    public void removeItems(int position){
        items.remove(position);
    }

    public void setItems(ArrayList<OldLetter> items){
        this.items=items;
    }

    public ArrayList<OldLetter> getItems(){
        return items;
    }

    public OldLetter getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, OldLetter item){
        items.set(position,item);
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ViewHolder viewHolder, int position) {
        //정보 세팅
        OldLetter item= items.get(position);
        Log.e(" ", "populateItemRows 내용:" + item + " " +position);
        viewHolder.setItem(item);

    }


}

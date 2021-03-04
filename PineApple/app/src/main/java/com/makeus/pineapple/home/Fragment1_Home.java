package com.makeus.pineapple.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.makeus.pineapple.R;
import com.makeus.pineapple.home.filters.PopupFilter;

public class Fragment1_Home extends Fragment {

    RecyclerView rv_newletter;
    Button btn_filter;
    FrameLayout fl_btn_filter;

    //무한 스크롤
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment1_home,container,false);
        //코드 작성
        //필터 버튼
        btn_filter = view.findViewById(R.id.btn_filter);
        fl_btn_filter = view.findViewById(R.id.fl_btn_filter);
        fl_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PopupFilter.class);
/*        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);*/
                startActivity(intent);
                //애니메이션 설정
                getActivity().overridePendingTransition(R.anim.bottom_up,R.anim.bottom_up);

            }
        });


        //리사이클러뷰1
        rv_newletter = view.findViewById(R.id.rv_newletter);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false);
        rv_newletter.setLayoutManager(layoutManager);
        NewLetterAdapter newLetterAdapter = new NewLetterAdapter();

        newLetterAdapter.addItem(new NewLetter("디독","02/01/2021"));
        newLetterAdapter.addItem(new NewLetter("디독","02/01/2021"));
        newLetterAdapter.addItem(new NewLetter("디독","02/01/2021"));



        rv_newletter.setAdapter(newLetterAdapter); //리사이클러뷰에 어답터 설정

        //리사이클러뷰2
        RecyclerView rv_oldletter = view.findViewById(R.id.rv_oldletter);
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false);
        rv_oldletter.setLayoutManager(layoutManager2);
        OldLetterAdapter oldLetterAdapter = new OldLetterAdapter();

        for (int i=0 ; i<9; i++){
            oldLetterAdapter.addItem(new OldLetter("디독","02/01/2021"));
        }


        rv_oldletter.setAdapter(oldLetterAdapter); //리사이클러뷰에 어답터 설정
        initScrollListener(rv_oldletter,oldLetterAdapter);

        return view;
    }
    private void initScrollListener(RecyclerView rv_oldletter,OldLetterAdapter oldLetterAdapter) {
        rv_oldletter.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == oldLetterAdapter.getItemCount() - 1) {
                        //bottom of list!
                        loadMore(oldLetterAdapter);
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore(OldLetterAdapter oldLetterAdapter) {
        oldLetterAdapter.addItem(null);

        oldLetterAdapter.notifyItemInserted(oldLetterAdapter.getItems().size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                oldLetterAdapter.removeItems(oldLetterAdapter.getItems().size() - 1);
                int scrollPosition = oldLetterAdapter.getItems().size();
                oldLetterAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 9;

                while (currentSize - 1 < nextLimit) {
                    oldLetterAdapter.addItem(new OldLetter("추가 디독","02/01/2021") );
                    currentSize++;
                }

                oldLetterAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }


}
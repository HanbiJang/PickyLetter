package com.makeus.pineapple.mypage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeus.pineapple.MainActivity;
import com.makeus.pineapple.R;

public class Fragment3_MyPage extends Fragment {

    MainActivity mainActivity;
    FragmentActivity myContext; //화면 전환
    Button btn_setting;
    FrameLayout fl_btn_setting;

    //무한 스크롤
    boolean isLoading = false;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        myContext=(FragmentActivity) context;
        mainActivity = (MainActivity) getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_mypage,container,false);

        //북마크 리사이클러뷰
        RecyclerView rv_mypage_bookmark = view.findViewById(R.id.rv_mypage_bookmark);
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false);
        rv_mypage_bookmark.setLayoutManager(layoutManager2);
        BookmarkLetterAdapter bookmarkLetterAdapter = new BookmarkLetterAdapter();

        bookmarkLetterAdapter.addItem(new BookmarkLetter("가게 배달지역 관리방식 개편 프로젝트","디독","02/01/2021",R.drawable.news_4,R.drawable.brand_1));
        bookmarkLetterAdapter.addItem(new BookmarkLetter("5달러 짜리 로고와 250달러 짜리 로고의 차이점","디독","02/01/2021",R.drawable.news_5,R.drawable.brand_1));
        bookmarkLetterAdapter.addItem(new BookmarkLetter("페이스북,인스타그램,유튜브의 잘못된 UI","디독","02/01/2021",R.drawable.news_6,R.drawable.brand_1));
        bookmarkLetterAdapter.addItem(new BookmarkLetter("주목받는 재택근무 관련주와 라이프스타일의 변화 \uD83D\uDC69\u200D\uD83D\uDCBB\n","어피티","02/01/2021",R.drawable.news_7,R.drawable.brand_3));
        bookmarkLetterAdapter.addItem(new BookmarkLetter("비상 걸린 콜센터,원격 콜센터 구축 업체는? \uD83D\uDCDE","어피티","02/01/2021",R.drawable.news_8,R.drawable.brand_3));
        bookmarkLetterAdapter.addItem(new BookmarkLetter("금 수요 폭등,거래소에는 호재","어피티","02/01/2021",R.drawable.news_9,R.drawable.brand_3));
        bookmarkLetterAdapter.addItem(new BookmarkLetter("타다 클라이언트 개발기","어피티","02/01/2021",R.drawable.news_10,R.drawable.brand_3));


        rv_mypage_bookmark.setAdapter(bookmarkLetterAdapter); //리사이클러뷰에 어답터 설정
        initScrollListener(rv_mypage_bookmark, bookmarkLetterAdapter);

        //세팅 버튼
        fl_btn_setting = view.findViewById(R.id.fl_btn_setting);
        btn_setting = view.findViewById(R.id.btn_setting);
        fl_btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Fragment fragment_settings_main = new SettingsMain();
                myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,R.anim.exit_left,R.anim.enter_left_pop,R.anim.exit_left_pop).addToBackStack(null).replace(R.id.container_fragment,fragment_settings_main).commit();//프래그먼트 전환

            }
        });

        return view;

    }

    private void initScrollListener(RecyclerView rv_search_result, BookmarkLetterAdapter bookmarkLetterAdapter) {
        rv_search_result.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == bookmarkLetterAdapter.getItemCount() - 1) {//bottom of list!
                        loadMore(bookmarkLetterAdapter);
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore(BookmarkLetterAdapter bookmarkLetterAdapter) {
        bookmarkLetterAdapter.addItem(null);
        bookmarkLetterAdapter.notifyItemInserted(bookmarkLetterAdapter.getItems().size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                bookmarkLetterAdapter.removeItems(bookmarkLetterAdapter.getItems().size() - 1);
                int scrollPosition = bookmarkLetterAdapter.getItems().size();
                bookmarkLetterAdapter.notifyItemRemoved(scrollPosition);

                int currentSize = scrollPosition;
                int nextLimit = currentSize + 3;

                while (currentSize - 1 < nextLimit) {

                    BookmarkLetter tmpBookmarkLetter = bookmarkLetterAdapter.getItem(bookmarkLetterAdapter.getItems().size() - 1); //마지막 요소 킵
                    //마지막 요소가 지워짐
                    bookmarkLetterAdapter.removeItems(bookmarkLetterAdapter.getItems().size() - 1);
                    //마지막 요소를 넣어야 함
                    bookmarkLetterAdapter.addItem(tmpBookmarkLetter); //스크롤 시 추가되는 내용
                    currentSize++;
                }

                bookmarkLetterAdapter.notifyDataSetChanged();
                isLoading = false;

            }
        }, 2000);


    }


}
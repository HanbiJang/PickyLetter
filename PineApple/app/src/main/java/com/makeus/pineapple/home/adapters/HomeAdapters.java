package com.makeus.pineapple.home.adapters;

import com.makeus.pineapple.home.data.HomeLetters;

import java.util.ArrayList;

public interface HomeAdapters {
    //상속 관계 : home.NewLetterAdapter , home.OldLetterAdapter

    void removeAll();

    void addItem(HomeLetters item);

    int getItemCount();

    ArrayList getItems();

    void notifyItemInserted(int i);

    void notifyItemRemoved(int scrollPosition);

    void removeItems(int i);

    HomeLetters getItem(int i);

    void notifyDataSetChanged();
}

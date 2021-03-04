package com.makeus.pineapple.search;

public class SearchedNews {

    Integer viewType;
    String title;
    String brand;
    String date;

    Integer numRank = -1;

    public SearchedNews(Integer viewType, String title, String brand, String date) {
        //검색 결과 나타내는 분
        this.viewType = viewType;
        this.title = title;
        this.brand = brand;
        this.date = date;
    }

    public SearchedNews(Integer viewType, String title, String brand, String date, Integer numRank) {
        this.viewType = viewType;
        this.title = title;
        this.brand = brand;
        this.date = date;
        this.numRank = numRank;
    }

    //

    public Integer getViewType() {
        return viewType;
    }

    public String getTitle() {
        return title;
    }

    public String getBrand() {
        return brand;
    }

    public String getDate() {
        return date;
    }

    public Integer getNumRank() { return numRank; }

    //

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNumRank(Integer numRank) { this.numRank = numRank; }
}

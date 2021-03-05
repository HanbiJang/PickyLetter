package com.makeus.pineapple.search;

public class SearchedNews {

    Integer viewType;
    String title;
    String brand;
    String date;
    Integer img_news;
    Integer img_brand;

    Integer numRank = -1;

    public SearchedNews(Integer viewType, String title, String brand, String date,Integer img_news, Integer img_brand) {
        //검색 결과 나타내는 분
        this.viewType = viewType;
        this.title = title;
        this.brand = brand;
        this.date = date;
        this.img_news = img_news;
        this.img_brand = img_brand;
    }

    public SearchedNews(Integer viewType, String title, String brand, String date, Integer numRank,Integer img_news, Integer img_brand) {
        this.viewType = viewType;
        this.title = title;
        this.brand = brand;
        this.date = date;
        this.numRank = numRank;
        this.img_news = img_news;
        this.img_brand = img_brand;
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

    public Integer getImg_news() {
        return img_news;
    }

    public Integer getImg_brand() {
        return img_brand;
    }

    public void setImg_news(Integer img_news) {
        this.img_news = img_news;
    }

    public void setImg_brand(Integer img_brand) {
        this.img_brand = img_brand;
    }
}

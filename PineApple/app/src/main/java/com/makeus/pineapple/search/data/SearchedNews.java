package com.makeus.pineapple.search.data;

public class SearchedNews {

    Integer viewType;
    String title;
    String brand;
    String date;
    String img_news;
    String img_brand;
    Integer numRank;
    Boolean isBookmarkClicked;

    public SearchedNews(){

    }

    //


    public Boolean getBookmarkClicked() {
        return isBookmarkClicked;
    }

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

    public Integer getNumRank() {
        return numRank;
    }

    //


    public void setBookmarkClicked(Boolean bookmarkClicked) {
        isBookmarkClicked = bookmarkClicked;
    }

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

    public String getImg_news() {
        return img_news;
    }

    public String getImg_brand() {
        return img_brand;
    }

    public void setImg_news(String img_news) {
        this.img_news = img_news;
    }

    public void setImg_brand(String  img_brand) { this.img_brand = img_brand; }

    public void setNumRank(Integer numRank) {
        this.numRank = numRank;
    }
}

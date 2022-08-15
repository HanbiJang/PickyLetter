package com.makeus.pineapple.search.data;

public class SearchedNews {

    Integer viewType;
    String title;
    String brand;
    String date;
    String img_news;
    String img_brand;
    Integer numRank;
    Integer isBookmarkClicked;
    Integer letterId, bookmarkId, bookmarkCount, platformId;
    String modifiedAt;
    Boolean subscribing;

    public SearchedNews(){

    }

    //


    public Integer getIsBookmarkClicked() {
        return isBookmarkClicked;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public Boolean getSubscribing() {
        return subscribing;
    }

    public Integer getBookmarkClicked() {
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

    public Integer getLetterId() {
        return letterId;
    }

    public Integer getBookmarkId() {
        return bookmarkId;
    }

    public Integer getBookmarkCount() {
        return bookmarkCount;
    }

    //


    public void setIsBookmarkClicked(Integer isBookmarkClicked) {
        this.isBookmarkClicked = isBookmarkClicked;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setSubscribing(Boolean subscribing) {
        this.subscribing = subscribing;
    }

    public void setBookmarkClicked(Integer bookmarkClicked) {
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

    public void setLetterId(Integer letterId) {
        this.letterId = letterId;
    }

    public void setBookmarkId(Integer bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public void setBookmarkCount(Integer bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }
}

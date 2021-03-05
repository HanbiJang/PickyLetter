package com.makeus.pineapple.mypage;

public class BookmarkLetter {

    String newsBrand;
    String newsDate;

    Integer img_news;
    Integer img_brand;



    public BookmarkLetter(String newsBrand, String newsDate,Integer img_news, Integer img_brand) {
        this.newsBrand = newsBrand;
        this.newsDate = newsDate;

        this.img_news = img_news;
        this.img_brand = img_brand;
    }

    public String getNewsBrand() {
        return newsBrand;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsBrand(String newsBrand) {
        this.newsBrand = newsBrand;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

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

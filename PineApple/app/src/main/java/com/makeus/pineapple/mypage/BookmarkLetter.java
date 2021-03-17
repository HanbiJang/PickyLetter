package com.makeus.pineapple.mypage;

public class BookmarkLetter {

    String newsTitle;
    String newsBrand;
    String newsDate;

    Integer img_news;
    Integer cimg_brand;

    public BookmarkLetter(String newsTitle, String newsBrand, String newsDate,Integer img_news, Integer img_brand) {
        this.newsTitle = newsTitle;
        this.newsBrand = newsBrand;
        this.newsDate = newsDate;

        this.img_news = img_news;
        this.cimg_brand = img_brand;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
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
        return cimg_brand;
    }

    public void setImg_news(Integer img_news) {
        this.img_news = img_news;
    }

    public void setImg_brand(Integer img_brand) {
        this.cimg_brand = img_brand;
    }
}

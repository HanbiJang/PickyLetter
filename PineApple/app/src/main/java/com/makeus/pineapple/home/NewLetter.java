package com.makeus.pineapple.home;

public class NewLetter {

    String newsBrand;
    String newsDate;
    String newsTitle;

    String img_news;
    String img_brand;

    public NewLetter() {

    }

    public NewLetter(String newstitle,String newsBrand, String newsDate,String img_news, String img_brand) {
        this.newsTitle = newstitle;
        this.newsBrand = newsBrand;
        this.newsDate = newsDate;
        this.img_news = img_news;
        this.img_brand = img_brand;
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

    public String getImg_news() {
        return img_news;
    }

    public String getImg_brand() {
        return img_brand;
    }

    public void setImg_news(String img_news) {
        this.img_news = img_news;
    }

    public void setImg_brand(String img_brand) {
        this.img_brand = img_brand;
    }
}

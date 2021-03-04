package com.makeus.pineapple.home;

public class OldLetter {

    String newsBrand;
    String newsDate;

    public OldLetter(String newsBrand, String newsDate) {
        this.newsBrand = newsBrand;
        this.newsDate = newsDate;
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
}

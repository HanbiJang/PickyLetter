package com.makeus.pineapple.mypage_settings.mypage.data;

public class UserData {
    /*{
            "userId": 4,
            "email": "mmmm@gmail.com",
            "nickname": "파인애플",
            "subscribingPlatformCount": 2,
            "bookmarkCount": 5
    }*/

    Integer userId,subscribingPlatformCount,bookmarkCount;
    String email, nickname;

    //get
    public Integer getUserId() {
        return userId;
    }

    public Integer getSubscribingPlatformCount() {
        return subscribingPlatformCount;
    }

    public Integer getBookmarkCount() {
        return bookmarkCount;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    //set
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setSubscribingPlatformCount(Integer subscribingPlatformCount) {
        this.subscribingPlatformCount = subscribingPlatformCount;
    }

    public void setBookmarkCount(Integer bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}


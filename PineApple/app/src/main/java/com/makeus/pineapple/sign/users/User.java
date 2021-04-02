package com.makeus.pineapple.sign.users;


public class User {
    Integer bookmarkCount;
    String email;
    String nickname;
    Integer subscribingPlatformCount;
    Integer userId;

    Integer loginCount = 0;

    public User(Integer bookmarkCount, String email, String nickname, Integer subscribingPlatformCount, Integer userId) {
        this.bookmarkCount = bookmarkCount;
        this.email = email;
        this.nickname = nickname;
        this.subscribingPlatformCount = subscribingPlatformCount;
        this.userId = userId;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public Integer getBookmarkCoung() {
        return bookmarkCount;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getSubscribingPlatformCount() {
        return subscribingPlatformCount;
    }

    public Integer getUserId() {
        return userId;
    }
    //

    public void setBookmarkCoung(Integer bookmarkCoung) {
        this.bookmarkCount = bookmarkCoung;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSubscribingPlatformCount(Integer subscribingPlatformCount) {
        this.subscribingPlatformCount = subscribingPlatformCount;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

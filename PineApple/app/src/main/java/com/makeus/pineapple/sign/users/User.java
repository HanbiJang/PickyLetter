package com.makeus.pineapple.sign.users;


public class User {
    Integer bookmarkCount;
    String email;
    String nickname;
    String subscribingPlatformCount;
    String userId;

    public User(Integer bookmarkCount, String email, String nickname, String subscribingPlatformCount, String userId) {
        this.bookmarkCount = bookmarkCount;
        this.email = email;
        this.nickname = nickname;
        this.subscribingPlatformCount = subscribingPlatformCount;
        this.userId = userId;
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

    public String getSubscribingPlatformCount() {
        return subscribingPlatformCount;
    }

    public String getUserId() {
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

    public void setSubscribingPlatformCount(String subscribingPlatformCount) {
        this.subscribingPlatformCount = subscribingPlatformCount;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

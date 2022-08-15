package com.makeus.pineapple.mypage_settings.mypage.data;

public class BookmarkLetter {

                /*"letterId": 18,
                  "platformId": 1,
                  "platformName": "퍼블리",
                  "platformImageUrl": "https://cdn.logo.com/hotlink-ok/logo-social.png",
                  "bookmarkId": 22,
                  "title": "4444",
                  "thumbnailImageUrl": null,
                  "bookmarkCount": 1,
                  "createdAt": "2021-03-10T10:38:54Z",
                  "modifiedAt": "2021-03-16T13:20:51Z",
                  "subscribing": true*/

    Integer letterId,platformId;
    String platformName,platformImageUrl;
    Integer bookmarkId;
    String title, thumbnailImageUrl;
    Integer bookmarkCount;
    String createdAt, modifiedAt;
    Boolean subscribing;

    public BookmarkLetter(Integer letterId, Integer platformId, String platformName, String platformImageUrl, Integer bookmarkId, String title, String thumbnailImageUrl, Integer bookmarkCount, String createdAt, String modifiedAt, Boolean subscribing) {
        this.letterId = letterId;
        this.platformId = platformId;
        this.platformName = platformName;
        this.platformImageUrl = platformImageUrl;
        this.bookmarkId = bookmarkId;
        this.title = title;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.bookmarkCount = bookmarkCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.subscribing = subscribing;
    }

    //get
    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformImageUrl() {
        return platformImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public Integer getLetterId() {
        return letterId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public Integer getBookmarkId() {
        return bookmarkId;
    }

    public Integer getBookmarkCount() {
        return bookmarkCount;
    }

    public Boolean getSubscribing() {
        return subscribing;
    }

    //set
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public void setPlatformImageUrl(String platformImageUrl) {
        this.platformImageUrl = platformImageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setLetterId(Integer letterId) {
        this.letterId = letterId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public void setBookmarkId(Integer bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public void setBookmarkCount(Integer bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public void setSubscribing(Boolean subscribing) {
        this.subscribing = subscribing;
    }
}

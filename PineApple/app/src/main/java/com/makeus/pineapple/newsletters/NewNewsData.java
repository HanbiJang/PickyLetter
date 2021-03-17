package com.makeus.pineapple.newsletters;

public class NewNewsData {
    /*                 "letterId": 18,
                  "platformId": 1,
                  "platformName": "퍼블리",
                  "platformImageUrl": "https://cdn.logo.com/hotlink-ok/logo-social.png",
                  "bookmarkId": 0,
                  "title": "4444",
                  "content": "44444",
                  "thumbnailImageUrl": null,
                  "bookmarkCount": 1,
                  "createdAt": "2021-03-10T10:38:54Z",
                  "modifiedAt": "2021-03-16T13:20:51Z"*/

    Integer letterId, platformId, bookmarkId, bookmarkCount;
    String platformName, platformImageUrl, title, content, thumbnailImageUrl, createdAt, modifiedAt;

    public NewNewsData(Integer letterId, Integer platformId, Integer bookmarkId, Integer bookmarkCount, String platformName, String platformImageUrl, String title, String content, String thumbnailImageUrl, String createdAt, String modifiedAt) {
        this.letterId = letterId;
        this.platformId = platformId;
        this.bookmarkId = bookmarkId;
        this.bookmarkCount = bookmarkCount;
        this.platformName = platformName;
        this.platformImageUrl = platformImageUrl;
        this.title = title;
        this.content = content;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    //


    public String getPlatformName() {
        return platformName;
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

    public String getPlatformImageUrl() {
        return platformImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    //


    public void setPlatformName(String platformName) {
        this.platformName = platformName;
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

    public void setPlatformImageUrl(String platformImageUrl) {
        this.platformImageUrl = platformImageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
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
}

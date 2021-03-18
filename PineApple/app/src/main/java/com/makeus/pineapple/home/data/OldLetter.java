package com.makeus.pineapple.home.data;

public class OldLetter implements HomeLetters{
        /* 서버 데이터구조:
                  "letterId": 18,
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

    Integer letterId;
    Integer platformId;
    String platformName;
    String platformImageUrl;
    Integer bookmarkId;
    String title;
    String content;
    String thumbnailImageUrl;
    Integer bookmarkCount;
    String createdAt;
    String modifiedAt;



    public OldLetter() {

    }


    //get

    public Integer getLetterId() {
        return letterId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformImageUrl() {
        return platformImageUrl;
    }

    public Integer getBookmarkId() {
        return bookmarkId;
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

    public Integer getBookmarkCount() {
        return bookmarkCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    //set
    @Override
    public void setLetterId(Integer letterId) {
        this.letterId = letterId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    @Override
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public void setPlatformImageUrl(String platformImageUrl) {
        this.platformImageUrl = platformImageUrl;
    }
    @Override
    public void setBookmarkId(Integer bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    @Override
    public void setBookmarkCount(Integer bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    @Override
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

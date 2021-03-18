package com.makeus.pineapple.search.data.server_data;

public class RankData {
/*    {
        "bookmarkCount": 0,
            "bookmarkId": 0,
            "createdAt": "2021-03-18T08:02:01.949Z",
            "letterId": 0,
            "modifiedAt": "2021-03-18T08:02:01.949Z",
            "platformId": 0,
            "platformImageUrl": "string",
            "platformName": "string",
            "subscribing": true,
            "thumbnailImageUrl": "string",
            "title": "string"
    }*/

    Integer bookmarkCount, bookmarkId, letterId, platformId;
    String createdAt,modifiedAt, platformImageUrl, platformName, thumbnailImageUrl, title;
    Boolean subscribing;

    //get
    public Integer getBookmarkCount() {
        return bookmarkCount;
    }

    public Integer getBookmarkId() {
        return bookmarkId;
    }

    public Integer getLetterId() {
        return letterId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public String getPlatformImageUrl() {
        return platformImageUrl;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getSubscribing() {
        return subscribing;
    }

    //set
    public void setBookmarkCount(Integer bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public void setBookmarkId(Integer bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public void setLetterId(Integer letterId) {
        this.letterId = letterId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setPlatformImageUrl(String platformImageUrl) {
        this.platformImageUrl = platformImageUrl;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubscribing(Boolean subscribing) {
        this.subscribing = subscribing;
    }
}

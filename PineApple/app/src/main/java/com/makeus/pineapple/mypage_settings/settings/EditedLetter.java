package com.makeus.pineapple.mypage_settings.settings;

public class EditedLetter {

    Integer viewType;

    String imageUrl, name;
    Integer platformId;
    boolean subscribing;

    public EditedLetter(Integer viewType, String imageUrl, String name, Integer platformId, boolean subscribing) {
        this.viewType = viewType;
        this.imageUrl = imageUrl;
        this.name = name;
        this.platformId = platformId;
        this.subscribing = subscribing;
    }

    //

    public Integer getViewType() {
        return viewType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public boolean getSubscribing() {
        return subscribing;
    }

    //

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public void setSubscribing(boolean subscribing) {
        this.subscribing = subscribing;
    }
}

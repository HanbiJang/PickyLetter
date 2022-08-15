package com.makeus.pineapple.server_controllers.server_data;

public class AllPlatformData {
    String imageUrl, name;
    Integer platformId;
    boolean subscribing;

    public AllPlatformData(String imageUrl, String name, Integer platformId, boolean subscribing) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.platformId = platformId;
        this.subscribing = subscribing;
    }

    //
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

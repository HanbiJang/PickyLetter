package com.makeus.pineapple.server_controllers.server_data;

public class SubscribingPlatformData {
    String imageUrl, name;
    Integer platformId;

    public SubscribingPlatformData(String imageUrl, String name, Integer platformId) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.platformId = platformId;
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
}

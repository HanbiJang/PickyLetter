package com.makeus.pineapple.server_controllers.server_data;

import java.util.ArrayList;

public class SubscribingPlatformResult {
    ArrayList<SubscribingPlatformData> resultList ;

    public SubscribingPlatformResult(ArrayList<SubscribingPlatformData> resultList) {
        this.resultList = resultList;
    }

    public ArrayList<SubscribingPlatformData> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<SubscribingPlatformData> resultList) {
        this.resultList = resultList;
    }
}

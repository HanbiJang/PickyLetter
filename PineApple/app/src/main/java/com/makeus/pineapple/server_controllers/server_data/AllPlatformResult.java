package com.makeus.pineapple.server_controllers.server_data;

import java.util.ArrayList;

public class AllPlatformResult {
    ArrayList<AllPlatformData> resultList ;

    public AllPlatformResult(ArrayList<AllPlatformData> resultList) {
        this.resultList = resultList;
    }

    public ArrayList<AllPlatformData> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<AllPlatformData> resultList) {
        this.resultList = resultList;
    }
}

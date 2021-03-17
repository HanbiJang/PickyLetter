package com.makeus.pineapple.newsletters;

import java.util.ArrayList;

public class NewNewsResult {
    ArrayList<NewNewsData> resultList;

    public ArrayList<NewNewsData> getNewNewsResultList() {
        return resultList;
    }

    public void setNewNewsResultList(ArrayList<NewNewsData> newNewsResultList) {
        this.resultList = newNewsResultList;
    }
}

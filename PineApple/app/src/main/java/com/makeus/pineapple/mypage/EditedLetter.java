package com.makeus.pineapple.mypage;

public class EditedLetter {

    Integer viewType;
    String brand;


    public EditedLetter(Integer viewType, String brand) {
        this.viewType = viewType;
        this.brand = brand;
    }


    //

    public Integer getViewType() {
        return viewType;
    }


    public String getBrand() {
        return brand;
    }


    //

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}

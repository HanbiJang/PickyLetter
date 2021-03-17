package com.makeus.pineapple.mypage;

public class EditedLetter {

    Integer viewType;
    String brand;
    Integer cimg_brand;


    public EditedLetter(Integer viewType, String brand,Integer img_brand) {
        this.viewType = viewType;
        this.brand = brand;
        this.cimg_brand = img_brand;
    }


    //

    public Integer getViewType() {
        return viewType;
    }
    public String getBrand() {
        return brand;
    }

    public Integer getImg_brand() {
        return cimg_brand;
    }


    //

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImg_brand(Integer img_brand) {
        this.cimg_brand = img_brand;
    }



}

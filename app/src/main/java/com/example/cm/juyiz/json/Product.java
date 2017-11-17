package com.example.cm.juyiz.json;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class Product {
    private int imgId;//图片
    private String title;//名称
    private String introduce;//简介
    private int price;//价格
    private int num;//购买数量
    private boolean isSelected;//是否被选中


    public Product() {
    }

    public Product(int imgId, String title, String introduce, int price) {
        this.imgId = imgId;
        this.title = title;
        this.introduce = introduce;
        this.price = price;
    }

    public Product(int imgId, String title, String introduce, int price, int num,boolean isSelected) {
        this.imgId = imgId;
        this.title = title;
        this.introduce = introduce;
        this.price = price;
        this.num = num;
        this.isSelected = isSelected;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

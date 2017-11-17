package com.example.cm.juyiz.json;

/**
 * 会员专享
 */

public class VipProduct {
    private int imgId;//图片
    private String title;//名称
    private int price;//价格
    private int save;//节省

    public VipProduct() {
    }

    public VipProduct(int imgId, String title, int price, int save) {
        this.imgId = imgId;
        this.title = title;
        this.price = price;
        this.save = save;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }
}

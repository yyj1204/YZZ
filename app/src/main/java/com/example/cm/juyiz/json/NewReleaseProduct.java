package com.example.cm.juyiz.json;

/**
 * 最新发布
 */

public class NewReleaseProduct {
    private int imgId;//图片
    private String imgUrl;//图片
    private String title;//名称
    private int price;//价格

    public NewReleaseProduct() {
    }

    public NewReleaseProduct(int imgId, String title, int price,String imgUrl) {
        this.imgId = imgId;
        this.title = title;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

}

package com.example.cm.juyiz.bean;

import java.io.Serializable;

/**
 * 商城首页 --- 顶部首页类型的超值热卖
 */
public class HomeMainSaleBean implements Serializable{
    private int id;
    private String main_title;
    private String sub_title;
    private String img_url;
    private int sale_type;
    private String goods_id;
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getSale_type() {
        return sale_type;
    }

    public void setSale_type(int sale_type) {
        this.sale_type = sale_type;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

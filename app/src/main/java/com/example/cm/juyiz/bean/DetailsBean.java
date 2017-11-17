package com.example.cm.juyiz.bean;

import java.util.List;

/**
 * 商品详情界面
 */

public class DetailsBean {
    private int id;
    private String title;
    private String introduction;
    private String promise_img_url;
    private List<Price> price;
    private String reward_ratio;
    private int is_free_shipping;
    private int status_id;
    private List<ImgUrl> goods_carousel;
    private List<ImgUrl> goods_details;


    public DetailsBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPromise_img_url() {
        return promise_img_url;
    }

    public void setPromise_img_url(String promise_img_url) {
        this.promise_img_url = promise_img_url;
    }

    public List<Price> getPrice() {
        return price;
    }

    public void setPrice(List<Price> price) {
        this.price = price;
    }

    public String getReward_ratio() {
        return reward_ratio;
    }

    public void setReward_ratio(String reward_ratio) {
        this.reward_ratio = reward_ratio;
    }

    public int getIs_free_shipping() {
        return is_free_shipping;
    }

    public void setIs_free_shipping(int is_free_shipping) {
        this.is_free_shipping = is_free_shipping;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public List<ImgUrl> getGoods_carousel() {
        return goods_carousel;
    }

    public void setGoods_carousel(List<ImgUrl> goods_carousel) {
        this.goods_carousel = goods_carousel;
    }

    public List<ImgUrl> getGoods_details() {
        return goods_details;
    }

    public void setGoods_details(List<ImgUrl> goods_details) {
        this.goods_details = goods_details;
    }
}

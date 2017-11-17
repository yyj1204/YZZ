package com.example.cm.juyiz.bean;
/**
 * 限时特价界面 --- 商品
 */

public class FlashSaleBean {
   private int good_id;
    private String title;
    private String thumb_url;
    private float original_price;
    private float promotion_price;
    private int saleCase;//抢购情况:0,结束；1：正在抢购；2：未开始
    private long stopTime;//剩余时间
    private int status_id;//销售情况：0：可以购买，1：下架，2：售罄

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public float getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(float original_price) {
        this.original_price = original_price;
    }

    public float getPromotion_price() {
        return promotion_price;
    }

    public void setPromotion_price(float promotion_price) {
        this.promotion_price = promotion_price;
    }

    public int getSaleCase() {
        return saleCase;
    }

    public void setSaleCase(int saleCase) {
        this.saleCase = saleCase;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }
    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }
}

package com.example.cm.juyiz.bean;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class OrderGoods {
    private int id;
    private int good_id;
    private int good_number;
    private float good_price;
    private String title;
    private String thumb_url;
    private int status_id;
    private int is_free_shipping;
    private int is_change;

    public OrderGoods() {
    }

    public OrderGoods(int id, int good_id, int good_number, float good_price, String title, String thumb_url, int status_id, int is_free_shipping, int is_change) {
        this.id = id;
        this.good_id = good_id;
        this.good_number = good_number;
        this.good_price = good_price;
        this.title = title;
        this.thumb_url = thumb_url;
        this.status_id = status_id;
        this.is_free_shipping = is_free_shipping;
        this.is_change = is_change;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public int getGood_number() {
        return good_number;
    }

    public void setGood_number(int good_number) {
        this.good_number = good_number;
    }

    public float getGood_price() {
        return good_price;
    }

    public void setGood_price(float good_price) {
        this.good_price = good_price;
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

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getIs_free_shipping() {
        return is_free_shipping;
    }

    public void setIs_free_shipping(int is_free_shipping) {
        this.is_free_shipping = is_free_shipping;
    }

    public int getIs_change() {
        return is_change;
    }

    public void setIs_change(int is_change) {
        this.is_change = is_change;
    }
}

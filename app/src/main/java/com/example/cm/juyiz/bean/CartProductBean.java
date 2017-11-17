package com.example.cm.juyiz.bean;

/**
 * 购物车商品
 */

public class CartProductBean {
    private int id;
    private int good_id;
    private int good_number;
    private float good_price;
    private String title;
    private String thumb_url;
    private int is_change;
    private boolean isSelected;//是否被选中

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

    public int getIs_change() {
        return is_change;
    }

    public void setIs_change(int is_change) {
        this.is_change = is_change;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

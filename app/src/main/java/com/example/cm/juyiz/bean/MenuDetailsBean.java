package com.example.cm.juyiz.bean;
/**
 * 商城首页 --- 顶部其他菜单类型 --- List子控件里的数据
 *  goods_id的下级属性
 */

public class MenuDetailsBean {
    private int id;
    private String title;
    private String thumb_url;
    private float price;

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

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

package com.example.cm.juyiz.bean;
/**
 * 商城首页 --- 顶部其他菜单类型的轮播图
 */
import java.io.Serializable;

public class HomeMenuCarouselBean implements Serializable{
    private String img_url;
    private int link_good_id;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getLink_good_id() {
        return link_good_id;
    }

    public void setLink_good_id(int link_good_id) {
        this.link_good_id = link_good_id;
    }
}

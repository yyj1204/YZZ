package com.example.cm.juyiz.json;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class HomeBannerImg implements Serializable{
    private int imgId;
    private String imgUrl;

    public HomeBannerImg(int imgId, String imgUrl) {
        this.imgId = imgId;
        this.imgUrl = imgUrl;
    }

    public HomeBannerImg() {
    }

    public int getImgId() {
        return imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

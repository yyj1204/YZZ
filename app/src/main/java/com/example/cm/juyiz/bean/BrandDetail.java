package com.example.cm.juyiz.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class BrandDetail {
    private String name;
    private List<SaleProductBean> goods;

    public BrandDetail() {
    }

    public BrandDetail(String title, List<SaleProductBean> goods) {
        this.name = title;
        this.goods = goods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SaleProductBean> getGoods() {
        return goods;
    }

    public void setGoods(List<SaleProductBean> goods) {
        this.goods = goods;
    }
}

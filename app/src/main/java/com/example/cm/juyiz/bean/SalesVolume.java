package com.example.cm.juyiz.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class SalesVolume {
    private String share_sales_amount;
    private List<MineSalesBean> order;

    public SalesVolume() {
    }

    public SalesVolume(String share_sales_amount, List<MineSalesBean> order) {
        this.share_sales_amount = share_sales_amount;
        this.order = order;
    }

    public String getShare_sales_amount() {
        return share_sales_amount;
    }

    public void setShare_sales_amount(String share_sales_amount) {
        this.share_sales_amount = share_sales_amount;
    }

    public List<MineSalesBean> getOrder() {
        return order;
    }

    public void setOrder(List<MineSalesBean> order) {
        this.order = order;
    }
}

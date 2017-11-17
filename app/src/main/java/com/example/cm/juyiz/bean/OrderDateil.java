package com.example.cm.juyiz.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class OrderDateil {
    private String member_id;
    private Address address;
    private List<OrderGoods> goods;
    private float freight;
    private float order_amount;
    private String terminal_code;

    public OrderDateil() {
    }

    public OrderDateil(Address address, List<OrderGoods> goods, float freight) {
        this.address = address;
        this.goods = goods;
        this.freight = freight;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public float getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(float order_amount) {
        this.order_amount = order_amount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<OrderGoods> goods) {
        this.goods = goods;
    }

    public float getFreight() {
        return freight;
    }

    public void setFreight(float freight) {
        this.freight = freight;
    }
}

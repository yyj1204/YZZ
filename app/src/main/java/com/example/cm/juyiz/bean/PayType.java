package com.example.cm.juyiz.bean;

/**
 * Created by Administrator on 2017/7/17 0017.
 */

public class PayType {
    private String  pay_code;//支付码
    private String pay_name;//支付类型名称String
    private String pay_icon;//图标
    private int is_select;

    public PayType() {
    }

    public PayType(String pay_code, String pay_name, String pay_icon) {
        this.pay_code = pay_code;
        this.pay_name = pay_name;
        this.pay_icon = pay_icon;
    }

    public int getIs_select() {
        return is_select;
    }

    public void setIs_select(int is_select) {
        this.is_select = is_select;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getPay_icon() {
        return pay_icon;
    }

    public void setPay_icon(String pay_icon) {
        this.pay_icon = pay_icon;
    }
}

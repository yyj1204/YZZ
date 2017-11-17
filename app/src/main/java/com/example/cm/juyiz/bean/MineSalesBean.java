package com.example.cm.juyiz.bean;

/**
 * 个人中心 --- 销售额
 */

public class MineSalesBean {
    private String order_number;
    private String reward_amount;
    private String mobile;
    private String wechat_nickname;
    private String wechat_headimgurl;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getReward_amount() {
        return reward_amount;
    }

    public void setReward_amount(String reward_amount) {
        this.reward_amount = reward_amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWechat_nickname() {
        return wechat_nickname;
    }

    public void setWechat_nickname(String wechat_nickname) {
        this.wechat_nickname = wechat_nickname;
    }

    public String getWechat_headimgurl() {
        return wechat_headimgurl;
    }

    public void setWechat_headimgurl(String wechat_headimgurl) {
        this.wechat_headimgurl = wechat_headimgurl;
    }
}
